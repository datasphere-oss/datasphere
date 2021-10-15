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

import java.util.List;

/**
 * @author Mikhail Mikhailov
 * Integer code attribute.
 */
public class IntegerCodeAttributeImpl extends AbstractCodeAttribute<Long> {
    /**
     * Special serialization constructor. Schould not be used otherwise.
     */
    protected IntegerCodeAttributeImpl() {
        super();
    }
    /**
     * Constructor.
     * @param name the name of the attribute
     */
    public IntegerCodeAttributeImpl(String name) {
        super(name);
    }

    /**
     * Constructor.
     * @param name the name
     * @param value the value
     */
    public IntegerCodeAttributeImpl(String name, Long value) {
        super(name, value);
    }

    /**
     * Constructor.
     * @param name
     */
    protected IntegerCodeAttributeImpl(String name, Long value, List<Long> supplementary) {
        super(name, value, supplementary);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeDataType getDataType() {
        return CodeDataType.INTEGER;
    }

    /**
     * Fluent part for compatibility.
     * @param value the value
     * @return self
     */
    public IntegerCodeAttributeImpl withValue(Long value) {
        setValue(value);
        return this;
    }

    /**
     * Fluent part for compatibility.
     * @param value the value
     * @return self
     */
    public IntegerCodeAttributeImpl withSupplementary(List<Long> value) {
        setSupplementary(value);
        return this;
    }
}
