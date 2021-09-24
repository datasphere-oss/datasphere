/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.huahui.datasphere.mdm.system.service.impl.configuration;

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
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationUpdatesWriter;

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
