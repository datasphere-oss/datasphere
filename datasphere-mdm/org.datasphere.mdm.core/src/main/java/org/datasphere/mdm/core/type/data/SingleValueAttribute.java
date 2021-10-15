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

package org.datasphere.mdm.core.type.data;

import java.util.Objects;

/**
 * @author Mikhail Mikhailov
 * Single value attribute (code or simple).
 */
public interface SingleValueAttribute<T> extends Attribute {
    /**
     * Gets contained value.
     * @return value.
     */
    T getValue();
    /**
     * Sets the value.
     * @param value to set
     */
    void setValue(T value);
    /**
     * Should not be used without reason.
     * @return cast value
     */
    @SuppressWarnings("unchecked")
    default<V> V castValue() {
        return (V) getValue();
    }
    /**
     * Tries to cast the supplied object value to internal type and sets it.
     * @param o the object.
     */
    @SuppressWarnings("unchecked")
    default void castValue(Object o) {
        setValue((T) o);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isEmpty() {
        return Objects.isNull(getValue());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isSingleValue() {
        return true;
    }
}
