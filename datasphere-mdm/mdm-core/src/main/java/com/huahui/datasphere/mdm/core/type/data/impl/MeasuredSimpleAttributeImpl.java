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

package com.huahui.datasphere.mdm.core.type.data.impl;

import javax.annotation.Nonnull;

import com.huahui.datasphere.mdm.core.type.data.MeasuredValue;
import com.huahui.datasphere.mdm.core.type.data.SimpleAttribute;

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
