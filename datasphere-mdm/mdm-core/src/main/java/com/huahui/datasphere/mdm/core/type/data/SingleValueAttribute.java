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

package com.huahui.datasphere.mdm.core.type.data;

import java.util.Objects;

/**
 * @author theseusyang
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
