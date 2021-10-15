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

/**
 * @author Mikhail Mikhailov
 * Boolean simple attribute.
 */
public class BooleanSimpleAttributeImpl extends AbstractSimpleAttribute<Boolean> {

    /**
     * Special serialization constructor. Schould not be used otherwise.
     */
    protected BooleanSimpleAttributeImpl() {
        super();
    }
     /**
     * Constructor.
     * @param name the name
     */
    public BooleanSimpleAttributeImpl(String name) {
        super(name);
    }

    /**
     * Constructor.
     * @param name the name
     * @param value the value
     */
    public BooleanSimpleAttributeImpl(String name, Boolean value) {
        super(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleDataType getDataType() {
        return SimpleDataType.BOOLEAN;
    }

    /**
     * Fluent part for compatibility.
     * @param value the value
     * @return self
     */
    public BooleanSimpleAttributeImpl withValue(Boolean value) {
        setValue(value);
        return this;
    }
}
