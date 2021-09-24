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

/**
 * @author theseusyang
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
