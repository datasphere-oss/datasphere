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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.huahui.datasphere.mdm.core.type.data.ArrayAttribute;
import com.huahui.datasphere.mdm.core.type.data.ArrayValue;

/**
 * @author theseusyang
 * Array attribute.
 */
public abstract class AbstractArrayAttribute<T> extends AbstractAttribute implements ArrayAttribute<T> {
    /**
     * The contained value.
     */
    private List<ArrayValue<T>> value;
    /**
     * Special serialization constructor. Schould not be used otherwise.
     */
    protected AbstractArrayAttribute() {
        super();
    }
    /**
     * Constructor.
     * @param name the name
     */
    protected AbstractArrayAttribute(String name) {
        super(name);
    }

	/**
     * Constructor.
     * @param name the name
     * @param value the value
     */
    protected AbstractArrayAttribute(String name, List<ArrayValue<T>> value) {
        super(name);
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ArrayValue<T>> getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(List<ArrayValue<T>> value) {
        this.value = value;
    }

    /**
     * @see Iterable#iterator()
     */
    @Override
    public Iterator<ArrayValue<T>> iterator() {
        return value == null ? Collections.emptyIterator() : value.iterator();
    }

    /**
     * @see Object#hashCode()
     * TODO re-write this crap asap. Introduce solid value identity system instead.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDataType()) + Objects.hash(toArray());
    }

    /**
     * @see Object#equals(Object)
     */
    @SuppressWarnings("unchecked")
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

        ArrayAttribute<T> other = (ArrayAttribute<T>) obj;
        if (getDataType() != other.getDataType()) {
            return false;
        }

        return Arrays.equals(toArray(), other.toArray());
    }

    /**
     * Creates an array attribute
     * @param type array value type
     * @param name the name of the attribute
     * @return attribute
     */
    public static ArrayAttribute<?> of(ArrayDataType type, String name) {

        if (Objects.isNull(name) || Objects.isNull(type)) {
            return null;
        }

        switch (type) {
        case STRING:
            return new StringArrayAttributeImpl(name);
        case DICTIONARY:
            return new DictionaryArrayAttributeImpl(name);
        case DATE:
            return new DateArrayAttributeImpl(name);
        case INTEGER:
            return new IntegerArrayAttributeImpl(name);
        case NUMBER:
            return new NumberArrayAttributeImpl(name);
        case TIME:
            return new TimeArrayAttributeImpl(name);
        case TIMESTAMP:
            return new TimestampArrayAttributeImpl(name);
        }

        return null;
    }

    /**
     * Creates an array attribute.
     * @param type array value type
     * @param name name of the attribute
     * @param value the value
     * @return attribute
     */
    public static ArrayAttribute<?> of(ArrayDataType type, String name, Object[] value) {

        if (Objects.isNull(name) || Objects.isNull(type)) {
            return null;
        }

        switch (type) {
        case STRING:
            return new StringArrayAttributeImpl(name, value == null
                ? null
                : Arrays.stream(value).map(v -> new StringArrayValue((String) v)).collect(Collectors.toList()));
        case DICTIONARY:
            return new DictionaryArrayAttributeImpl(name, value == null
                ? null
                : Arrays.stream(value).map(v -> new DictionaryArrayValue((String) v)).collect(Collectors.toList()));
        case DATE:
            return new DateArrayAttributeImpl(name, value == null
                ? null
                : Arrays.stream(value).map(v -> new DateArrayValue((LocalDate) v)).collect(Collectors.toList()));
        case INTEGER:
            return new IntegerArrayAttributeImpl(name, value == null
                ? null
                : Arrays.stream(value).map(v -> new IntegerArrayValue((Long) v)).collect(Collectors.toList()));
        case NUMBER:
            return new NumberArrayAttributeImpl(name, value == null
                ? null
                : Arrays.stream(value).map(v -> new NumberArrayValue((Double) v)).collect(Collectors.toList()));
        case TIME:
            return new TimeArrayAttributeImpl(name, value == null
                ? null
                : Arrays.stream(value).map(v -> new TimeArrayValue((LocalTime) v)).collect(Collectors.toList()));
        case TIMESTAMP:
            return new TimestampArrayAttributeImpl(name, value == null
                ? null
                : Arrays.stream(value).map(v -> new TimestampArrayValue((LocalDateTime) v)).collect(Collectors.toList()));
        }

        return null;
    }

    /**
     * Creates an array attribute.
     * @param type array value type
     * @param name name of the attribute
     * @param value the value
     * @return attribute
     */
    public static ArrayAttribute<?> of(ArrayDataType type, String name, List<?> value) {

        if (Objects.isNull(name) || Objects.isNull(type)) {
            return null;
        }

        switch (type) {
        case STRING:
            return new StringArrayAttributeImpl(name, value == null
                ? null
                : value.stream().map(v -> new StringArrayValue((String) v)).collect(Collectors.toList()));
        case DICTIONARY:
            return new DictionaryArrayAttributeImpl(name, value == null
                ? null
                : value.stream().map(v -> new DictionaryArrayValue((String) v)).collect(Collectors.toList()));
        case DATE:
            return new DateArrayAttributeImpl(name, value == null
                ? null
                : value.stream().map(v -> new DateArrayValue((LocalDate) v)).collect(Collectors.toList()));
        case INTEGER:
            return new IntegerArrayAttributeImpl(name, value == null
                ? null
                : value.stream().map(v -> new IntegerArrayValue((Long) v)).collect(Collectors.toList()));
        case NUMBER:
            return new NumberArrayAttributeImpl(name, value == null
                ? null
                : value.stream().map(v -> new NumberArrayValue((Double) v)).collect(Collectors.toList()));
        case TIME:
            return new TimeArrayAttributeImpl(name, value == null
                ? null
                : value.stream().map(v -> new TimeArrayValue((LocalTime) v)).collect(Collectors.toList()));
        case TIMESTAMP:
            return new TimestampArrayAttributeImpl(name, value == null
                ? null
                : value.stream().map(v -> new TimestampArrayValue((LocalDateTime) v)).collect(Collectors.toList()));
        }

        return null;
    }
    /**
     *
     */
    @Override
	public String toString() {
    	StringBuilder result= new StringBuilder("[");
    	if(value!=null){
    		for (ArrayValue<T> arrayValue : value) {
				result.append(arrayValue.toString()).append(", ");
			}
    	}
    	result.append("]");
		return result.toString();
	}
}
