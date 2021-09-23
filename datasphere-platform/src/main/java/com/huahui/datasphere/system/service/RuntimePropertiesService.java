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

package com.huahui.datasphere.system.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.huahui.datasphere.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.system.type.configuration.ConfigurationValue;
/**
 * Configuration property service.
 * The service allowes discovery and updates on configuration properties at runtime.
 * @author Alexander Malyshev
 */
public interface RuntimePropertiesService extends BeanPostProcessor, ModulePostProcessor, InitializingBean {
    /**
     * Adds known property definitions to the platform.
     * Called automatically from the {@linkplain ModuleService} during init stage.
     * This is not a public method and should not be called from client code.
     * @param configurationProperties the property definitions to load
     */
    void load(Collection<ConfigurationProperty<?>> configurationProperties);
    /**
     * Update values for the given properties.
     * @param properties the property values to update
     */
    void update(Map<String, String> properties);
    /**
     * Gets current values for all available property definitions.
     * @return collection of values.
     */
    Collection<ConfigurationValue<?>> getAll();
    /**
     * Filters current property values by property group.
     * @param groupName the group name to filter by
     * @return collection of values
     */
    Collection<ConfigurationValue<?>> getByGroup(String groupName);
    /**
     * Gets current value of a property with specified key.
     * @param name property name
     * @return value
     */
    ConfigurationValue<?> getByKey(String name);
    /**
     * Gets current value of a property by specified property.
     * @param property the property instance
     * @return value
     */
    <T> ConfigurationValue<T> getByProperty(ConfigurationProperty<T> property);
}
