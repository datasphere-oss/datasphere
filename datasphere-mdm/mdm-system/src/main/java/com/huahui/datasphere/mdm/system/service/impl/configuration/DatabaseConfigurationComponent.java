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

import com.huahui.datasphere.mdm.system.dao.ConfigurationDAO;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationUpdatesReader;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationUpdatesWriter;

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
