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

package com.huahui.datasphere.mdm.system.type.configuration.impl;

import java.util.Objects;

import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationValue;
/**
 * Immutable current value of a configuration property - one time shot.
 * @author theseusyang
 */
public class ImmutableConfigurationValue<T> implements ConfigurationValue<T> {
    /**
     * A totally empty configuration value.
     */
    public static final ConfigurationValue<?> ABSENT = new ImmutableConfigurationValue<>(null, null);
    /**
     * The property desсription.
     */
    private final ConfigurationProperty<T> property;
    /**
     * Current value.
     */
    private final T value;
    /**
     * Constructor.
     * @param property the property description
     * @param value the current value of the property
     */
    public ImmutableConfigurationValue(final ConfigurationProperty<T> property, final T value) {
        super();
        this.property = property;
        this.value = value;
    }
    /**
     * Gets this property value's definition.
     * @return this property value's definition.
     */
    @Override
    public ConfigurationProperty<T> getProperty() {
        return property;
    }
    /**
     * Gets property's current value.
     * @return current property's value
     */
    @Override
    public T getValue() {

        if (isEmpty()) {
            return null;
        }

        return Objects.isNull(value) ? property.getDefaultValue() : value;
    }
    /**
     * Empty PROPERTY and VALUE check.
     * @return true if neither property nor value set
     */
    @Override
    public boolean isEmpty() {
        return Objects.isNull(property) && Objects.isNull(value);
    }
    /**
     * Value presence check.
     * @return true if value is set
     */
    @Override
    public boolean hasValue() {
        return Objects.nonNull(getValue());
    }
}
