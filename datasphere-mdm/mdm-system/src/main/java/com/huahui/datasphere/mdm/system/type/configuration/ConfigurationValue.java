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

package com.huahui.datasphere.mdm.system.type.configuration;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.huahui.datasphere.mdm.system.type.configuration.impl.ImmutableConfigurationValue;
/**
 * Current value of a configuration property.
 * @author theseusyang
 */
public interface ConfigurationValue<T> {
    /**
     * Gets this property value's definition.
     * @return this property value's definition.
     */
    ConfigurationProperty<T> getProperty();
    /**
     * Gets property's current value.
     * @return current property's value
     */
    T getValue();
    /**
     * Empty PROPERTY and VALUE check.
     * @return true if neither property nor value set
     */
    boolean isEmpty();
    /**
     * Value presence check.
     * @return true if value is set
     */
    boolean hasValue();
    /**
     * Serializes self to string.
     * @return self as string
     */
    /**
     * {@inheritDoc}
     */
    default String serialize() {
        return Objects.nonNull(getProperty()) && Objects.nonNull(getProperty().getSerializer()) && hasValue()
                ? getProperty().getSerializer().apply(getValue())
                : StringUtils.EMPTY;
    }
    /**
     * Empty value singleton accessor.
     * @return empty value singleton
     */
    static ConfigurationValue<?> empty() {
        return ImmutableConfigurationValue.ABSENT;
    }
}
