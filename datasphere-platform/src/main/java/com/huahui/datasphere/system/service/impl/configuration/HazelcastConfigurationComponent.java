/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 *
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.huahui.datasphere.system.service.impl.configuration;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.impl.MapListenerAdapter;
import com.huahui.datasphere.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.system.type.configuration.ConfigurationUpdatesWriter;

@Component
@Order(3)
public class HazelcastConfigurationComponent extends MapListenerAdapter<String, String> implements ConfigurationUpdatesWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HazelcastConfigurationComponent.class);

    private static final String CONFIGURATION_MAP_NAME = "configuration";

    private final LocalConfigurationComponent localConfigurationComponent;

    private final IMap<String, String> properties;

    public HazelcastConfigurationComponent(
            final HazelcastInstance hazelcastInstance,
            final LocalConfigurationComponent localConfigurationComponent) {
        super();
        this.localConfigurationComponent = localConfigurationComponent;
        this.properties = hazelcastInstance.getMap(CONFIGURATION_MAP_NAME);
        this.properties.addEntryListener(this, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(Map<ConfigurationProperty<?>, Optional<?>> updates) {

        if (MapUtils.isEmpty(updates)) {
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, String> converted = updates.entrySet().stream()
            .map(e -> {

                String key = e.getKey().getKey();
                Object opt = e.getValue().orElse(null);
                Function<Object, String> f = (Function<Object, String>) e.getKey().getSerializer();
                String value = (e.getValue().isEmpty() ? StringUtils.EMPTY : f.apply(opt));

                return Pair.of(key, value);
            })
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        properties.putAll(converted);
    }

    @Override
    public void onEntryEvent(EntryEvent<String, String> event) {

        if (event.getMember().localMember()) {
            return;
        }

        ConfigurationProperty<?> p = localConfigurationComponent.property(event.getKey());
        if (Objects.isNull(p)) {
            LOGGER.warn("Received update for unknown property: {}.", event.getKey());
            return;
        }

        final Map<ConfigurationProperty<?>, Optional<?>> update = Map.of(p, StringUtils.isBlank(event.getValue())
                ? Optional.empty()
                : Optional.of(p.getDeserializer().apply(event.getValue())));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Update configuration from Hazelcast. Available properties: {}", update);
        }

        localConfigurationComponent.upsert(update);
    }
}
