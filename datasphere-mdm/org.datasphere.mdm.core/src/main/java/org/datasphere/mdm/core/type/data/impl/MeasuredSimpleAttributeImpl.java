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

import javax.annotation.Nonnull;

import org.datasphere.mdm.core.type.data.MeasuredValue;
import org.datasphere.mdm.core.type.data.SimpleAttribute;

public class MeasuredSimpleAttributeImpl extends AbstractSimpleAttribute<MeasuredValue> {
    /**
     * Special serialization constructor. Schould not be used otherwise.
     */
    protected MeasuredSimpleAttributeImpl() {
        super();
    }
    /**
     * Constructor.
     * @param name the attr name
     */
    public MeasuredSimpleAttributeImpl(String name) {
        this(name, MeasuredValue.of(null));
    }
    /**
     * Constructor.
     * @param name the attr name
     * @param value the value hold
     */
    public MeasuredSimpleAttributeImpl(String name, MeasuredValue value) {
        super(name, value);
    }
    /**
     * Constructor. Some value in initial units for the category, defined in MM.
     * @param name the attr name
     * @param value the value hold
     */
    public MeasuredSimpleAttributeImpl(String name, Double value) {
        this(name, MeasuredValue.of(value));
    }
    /**
     * Constructor.
     * @param numberSimpleAttribute a simple attribute to convert
     */
    public MeasuredSimpleAttributeImpl(@Nonnull SimpleAttribute<Double> numberSimpleAttribute) {
        this(numberSimpleAttribute.getName(), numberSimpleAttribute.getValue());
    }
    /**
     * Constructor.
     * @param name the attr name
     * @param categoryId the category ID
     * @param unitId the unit ID
     * @param initial the initial value
     */
    public MeasuredSimpleAttributeImpl(@Nonnull String name, String categoryId, String unitId, Double initial) {
        this(name, MeasuredValue.of(categoryId, unitId, initial));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleDataType getDataType() {
        return SimpleDataType.MEASURED;
    }
}
