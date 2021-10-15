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

package org.datasphere.mdm.core.type.data.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.datasphere.mdm.core.type.data.CodeAttribute;

/**
 * @author Mikhail Mikhailov
 * Abstract code attribute.
 */
public abstract class AbstractCodeAttribute<T> extends AbstractAttribute implements CodeAttribute<T> {
    /**
     * Primary value.
     */
    private T value;
    /**
     * Supplementary values.
     */
    private List<T> supplementary;
    /**
     * Special serialization constructor. Schould not be used otherwise.
     */
    protected AbstractCodeAttribute() {
        super();
    }
    /**
     * Constructor.
     * @param name the name of the attribute
     */
    protected AbstractCodeAttribute(String name) {
        super(name);
    }
    /**
     * Constructor.
     * @param name
     */
    protected AbstractCodeAttribute(String name, T value) {
        this(name);
        this.value = value;
    }
    /**
     * Constructor.
     * @param name
     */
    protected AbstractCodeAttribute(String name, T value, List<T> supplementary) {
        this(name, value);
        this.supplementary = supplementary;
    }
    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.common.types.CodeAttribute#getValue()
     */
    @Override
    public T getValue() {
        return value;
    }
    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.common.types.CodeAttribute#setValue(java.lang.Object)
     */
    @Override
    public void setValue(T value) {
        this.value = value;
    }
    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.common.types.CodeAttribute#getSupplementary()
     */
    @Override
    public List<T> getSupplementary() {
        return Objects.isNull(supplementary) ? Collections.emptyList() : supplementary;
    }
    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.common.types.CodeAttribute#setSupplementary(java.util.List)
     */
    @Override
    public void setSupplementary(List<T> value) {
        this.supplementary = value;
    }
    /**
     * @see Object#hashCode()
     * TODO re-write this crap asap. Introduce solid value identity system instead.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDataType(), getValue());
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!getClass().isInstance(obj)) {
            return false;
        }

        AbstractCodeAttribute<?> other = (AbstractCodeAttribute<?>) obj;
        if (getDataType() != other.getDataType()) {
            return false;
        }

        return Objects.equals(getValue(), other.getValue());
    }

    /**
     * Creates code attribute.
     * @param type the type of the attribute
     * @param name the name of the attribute
     * @return attribute
     */
    public static CodeAttribute<?> of(CodeDataType type, String name) {

        if (Objects.isNull(name) || Objects.isNull(type)) {
            return null;
        }

        if (type == CodeDataType.STRING) {
            return new StringCodeAttributeImpl(name);
        } else if (type == CodeDataType.INTEGER) {
            return new IntegerCodeAttributeImpl(name);
        }

        return null;
    }

    /**
     * Creates code attribute
     * @param type the type of the attribut
     * @param name the name of the attribute
     * @param value the value to set
     * @return attribute
     */
    public static CodeAttribute<?> of(CodeDataType type, String name, Object value) {

        if (Objects.isNull(name) || Objects.isNull(type)) {
            return null;
        }

        if (type == CodeDataType.STRING) {
            return new StringCodeAttributeImpl(name, (String) value);
        } else if (type == CodeDataType.INTEGER) {
            return new IntegerCodeAttributeImpl(name, (Long) value);
        }

        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringBuilder("(name = ")
                .append(getName())
                .append(", value = ")
                .append(value)
                .append(", supplementary = ")
                .append(supplementary)
                .append(")")
                .toString();
    }
}
