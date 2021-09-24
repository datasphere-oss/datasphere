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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import com.huahui.datasphere.mdm.core.type.data.BinaryLargeValue;
import com.huahui.datasphere.mdm.core.type.data.CharacterLargeValue;
import com.huahui.datasphere.mdm.core.type.data.MeasuredValue;
import com.huahui.datasphere.mdm.core.type.data.SimpleAttribute;

/**
 * @author theseusyang
 * Abstract simple attribute value holder.
 */
public abstract class AbstractSimpleAttribute<T> extends AbstractAttribute implements SimpleAttribute<T> {
    /**
     * Value.
     */
    protected T value;
    /**
     * Display value (title) for the contained data value.
     */
    protected String displayValue;
    /**
     * Special serialization constructor. Schould not be used otherwise.
     */
    protected AbstractSimpleAttribute() {
        super();
    }
    /**
     * Constructor.
     * @param name
     */
    protected AbstractSimpleAttribute(String name) {
        super(name);
    }
    /**
     * Constructor.
     * @param name
     */
    protected AbstractSimpleAttribute(String name, T value) {
        super(name);
        this.value = value;
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
    public void setValue(T value) {
        this.value = value;
    }
    /**
     * @return the displayValue
     */
    @Override
    public String getDisplayValue() {
        return displayValue;
    }
    /**
     * @param displayValue the displayValue to set
     */
    @Override
    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }
    /**
     * @see Object#hashCode()
     * Not a good thing. Re-write this crap asap. Introduce solid value identity system instead.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDataType(), Objects.toString(getValue()));
    }
    /**
     * @see Object#equals(Object)
     * Not a good thing. Re-write this crap asap. Introduce solid value identity system instead.
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!SimpleAttribute.class.isInstance(obj)) {
            return false;
        }

        SimpleAttribute<?> other = (SimpleAttribute<?>) obj;
        if (getDataType() != other.getDataType()) {
            return false;
        }

        return Objects.equals(Objects.toString(getValue()), Objects.toString(other.getValue()));
    }
    /**
     * Creates
     * @param type
     * @param name
     * @return
     */
    public static SimpleAttribute<?> of(SimpleDataType type, String name) {

        if (Objects.isNull(name) || Objects.isNull(type)) {
            return null;
        }

        switch (type) {
        case STRING:
            return new StringSimpleAttributeImpl(name);
        case DICTIONARY:
            return new DictionarySimpleAttributeImpl(name);
        case BLOB:
            return new BlobSimpleAttributeImpl(name);
        case BOOLEAN:
            return new BooleanSimpleAttributeImpl(name);
        case CLOB:
            return new ClobSimpleAttributeImpl(name);
        case DATE:
            return new DateSimpleAttributeImpl(name);
        case ENUM:
            return new EnumSimpleAttributeImpl(name);
        case INTEGER:
            return new IntegerSimpleAttributeImpl(name);
        case LINK:
            return new LinkSimpleAttributeImpl(name);
        case NUMBER:
            return new NumberSimpleAttributeImpl(name);
        case MEASURED:
            return new MeasuredSimpleAttributeImpl(name);
        case TIME:
            return new TimeSimpleAttributeImpl(name);
        case TIMESTAMP:
            return new TimestampSimpleAttributeImpl(name);
        }

        return null;
    }
    /**
     * Creates
     * @param type
     * @param name
     * @return
     */
    public static SimpleAttribute<?> of(SimpleDataType type, String name, Object value) {

        if (Objects.isNull(name) || Objects.isNull(type)) {
            return null;
        }

        switch (type) {
        case STRING:
            return new StringSimpleAttributeImpl(name, (String) value);
        case DICTIONARY:
            return new DictionarySimpleAttributeImpl(name, (String) value);
        case BLOB:
            return new BlobSimpleAttributeImpl(name, (BinaryLargeValue) value);
        case BOOLEAN:
            return new BooleanSimpleAttributeImpl(name, (Boolean) value);
        case CLOB:
            return new ClobSimpleAttributeImpl(name, (CharacterLargeValue) value);
        case DATE:
            return new DateSimpleAttributeImpl(name, (LocalDate) value);
        case ENUM:
            return new EnumSimpleAttributeImpl(name, (String) value);
        case INTEGER:
            return new IntegerSimpleAttributeImpl(name, (Long) value);
        case LINK:
            return new LinkSimpleAttributeImpl(name, (String) value);
        case NUMBER:
            return new NumberSimpleAttributeImpl(name, (Double) value);
        case MEASURED:
            return new MeasuredSimpleAttributeImpl(name, (MeasuredValue) value);
        case TIME:
            return new TimeSimpleAttributeImpl(name, (LocalTime) value);
        case TIMESTAMP:
            return new TimestampSimpleAttributeImpl(name, (LocalDateTime) value);
        }

        return null;
    }
}
