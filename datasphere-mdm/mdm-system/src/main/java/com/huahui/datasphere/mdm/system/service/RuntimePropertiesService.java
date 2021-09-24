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

package com.huahui.datasphere.mdm.system.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationValue;
/**
 * Configuration property service.
 * The service allowes discovery and updates on configuration properties at runtime.
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
