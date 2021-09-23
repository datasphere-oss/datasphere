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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.huahui.datasphere.system.dao.ConfigurationDAO;
import com.huahui.datasphere.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.system.type.configuration.ConfigurationUpdatesReader;
import com.huahui.datasphere.system.type.configuration.ConfigurationUpdatesWriter;

@Component
@Order(2)
public class DatabaseConfigurationComponent implements ConfigurationUpdatesWriter, ConfigurationUpdatesReader {

    private final ConfigurationDAO configurationDAO;

    private final boolean bootstrap;

    public DatabaseConfigurationComponent(final ConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
        this.bootstrap = configurationDAO.isBootstrap();
    }

    @SuppressWarnings("unchecked")
    private byte[] serialize(ConfigurationProperty<?> p, Optional<?> v) {

        Object opt = v.orElse(null);
        Function<Object, String> f = (Function<Object, String>) p.getSerializer();
        String value = (v.isEmpty() ? StringUtils.EMPTY : f.apply(opt));

        return value.getBytes(StandardCharsets.UTF_8);
    }

    @SuppressWarnings("unchecked")
    private Optional<?> deserialize(ConfigurationProperty<?> p, byte[] v) {

        if (ArrayUtils.isEmpty(v)) {
            return Optional.empty();
        }
        String value = new String(v, StandardCharsets.UTF_8);
        if (StringUtils.isBlank(value)) {
            return Optional.empty();
        }

        Function<String, Object> f = (Function<String, Object>) p.getDeserializer();
        return Optional.of(f.apply(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<ConfigurationProperty<?>, Optional<?>> read(Collection<ConfigurationProperty<?>> properties) {

        if (bootstrap) {
            return Collections.emptyMap();
        }

        Map<String, ConfigurationProperty<?>> uncompacted = properties.stream()
                .collect(Collectors.toMap(ConfigurationProperty::getKey, Function.identity()));

        return configurationDAO.fetch(new ArrayList<>(uncompacted.keySet()))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        e -> uncompacted.get(e.getKey()),
                        e -> this.deserialize(uncompacted.get(e.getKey()), e.getValue())));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void write(Map<ConfigurationProperty<?>, Optional<?>> updates) {

        if (MapUtils.isEmpty(updates)) {
            return;
        }

        configurationDAO.save(updates.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().getKey(),
                        e -> this.serialize(e.getKey(), e.getValue()))));
    }
}
