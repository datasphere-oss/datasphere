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

package com.huahui.datasphere.system.type.configuration.impl;

import java.util.Objects;

import com.huahui.datasphere.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.system.type.configuration.ConfigurationValue;
/**
 * Immutable current value of a configuration property - one time shot.
 * @author Alexander Malyshev
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
