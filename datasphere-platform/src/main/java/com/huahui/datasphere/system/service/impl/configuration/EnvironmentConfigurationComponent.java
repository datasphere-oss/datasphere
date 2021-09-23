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

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.huahui.datasphere.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.system.type.configuration.ConfigurationUpdatesReader;

@Component
@Order(1)
public class EnvironmentConfigurationComponent implements ConfigurationUpdatesReader {

    private final Environment environment;

    public EnvironmentConfigurationComponent(final Environment environment) {
        this.environment = environment;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<ConfigurationProperty<?>, Optional<?>> read(Collection<ConfigurationProperty<?>> properties) {
        return properties.stream()
                .map(p -> {
                    return Pair.of(p,
                            Optional.ofNullable(environment.getProperty(p.getKey()))
                                .map(value -> p.getDeserializer().apply(value)));
                })
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }
}
