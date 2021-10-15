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
 *         FP number simple attribute.
 */
public class NumberSimpleAttributeImpl extends AbstractSimpleAttribute<Double> {

    /**
     * Special serialization constructor. Schould not be used otherwise.
     */
    protected NumberSimpleAttributeImpl() {
        super();
    }
    /**
     * Constructor.
     *
     * @param name
     */
    public NumberSimpleAttributeImpl(String name) {
        super(name);
    }

    /**
     * Constructor.
     *
     * @param name  the name
     * @param value the value
     */
    public NumberSimpleAttributeImpl(String name, Double value) {
        super(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleDataType getDataType() {
        return SimpleDataType.NUMBER;
    }

    /**
     * Fluent part for compatibility.
     *
     * @param value the value
     * @return self
     */
    public NumberSimpleAttributeImpl withValue(Double value) {
        setValue(value);
        return this;
    }
}
