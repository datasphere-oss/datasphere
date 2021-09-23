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

package com.huahui.datasphere.system.type.configuration;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.huahui.datasphere.system.type.configuration.impl.ImmutableConfigurationValue;
/**
 * Current value of a configuration property.
 * @author Alexander Malyshev
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
