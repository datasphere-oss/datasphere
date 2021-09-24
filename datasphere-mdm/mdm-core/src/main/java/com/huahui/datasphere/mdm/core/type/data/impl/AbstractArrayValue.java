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

import com.huahui.datasphere.mdm.core.type.data.ArrayValue;

/**
 * @author theseusyang
 * Array value.
 */
public abstract class AbstractArrayValue<T> implements ArrayValue<T> {
    /**
     * The value
     */
    private T value;
    /**
     * Display value.
     */
    private String displayValue;
    /**
     * Constructor.
     */
    protected AbstractArrayValue() {
        super();
    }
    /**
     * Constructor.
     */
    protected AbstractArrayValue(T value) {
        this();
        this.value = value;
    }
    /**
     * Constructor.
     */
    protected AbstractArrayValue(T value, String displayValue) {
        this(value);
        this.displayValue = displayValue;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(T value) {
        this.value = value;
    }

	@Override
	public String toString() {
		return  value.toString();
	}
}
