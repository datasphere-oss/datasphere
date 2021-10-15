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

package org.datasphere.mdm.core.type.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.datasphere.mdm.core.type.data.impl.DateArrayValue;
import org.datasphere.mdm.core.type.data.impl.DictionaryArrayValue;
import org.datasphere.mdm.core.type.data.impl.IntegerArrayValue;
import org.datasphere.mdm.core.type.data.impl.NumberArrayValue;
import org.datasphere.mdm.core.type.data.impl.StringArrayValue;
import org.datasphere.mdm.core.type.data.impl.TimeArrayValue;
import org.datasphere.mdm.core.type.data.impl.TimestampArrayValue;
import org.datasphere.mdm.core.type.model.AttributeElement.AttributeValueType;
import org.datasphere.mdm.search.type.FieldType;

/**
 * @author Mikhail Mikhailov
 * Array attribute.
 */
public interface ArrayAttribute<T> extends Attribute, Iterable<ArrayValue<T>> {
    /**
     * @author Mikhail Mikhailov
     * Denotes type of the contained data.
     */
    public enum ArrayDataType {
        /**
         * The string type.
         */
        STRING,
        /**
         * Dictionary, as defined in XSD.
         */
        DICTIONARY,
        /**
         * The integer type (long 8 bytes).
         */
        INTEGER,
        /**
         * The floating point type (double 8 bytes).
         */
        NUMBER,
        /**
         * The date type.
         */
        DATE,
        /**
         * The time type.
         */
        TIME,
        /**
         * The timestamp type.
         */
        TIMESTAMP;
        /**
         * Converts self to search type.
         * @return self as search type
         */
        public FieldType toSearchType() {

            switch (this) {
            case STRING:
                return FieldType.STRING;
            case DATE:
                return FieldType.DATE;
            case TIME:
                return FieldType.TIME;
            case TIMESTAMP:
                return FieldType.TIMESTAMP;
            case INTEGER:
                return FieldType.INTEGER;
            case NUMBER:
                return FieldType.NUMBER;
            default:
                break;
            }

            return null;
        }
        /**
         * Converts self to model type.
         * @return self as search type
         */
        public AttributeValueType toModelType() {

            switch (this) {
            case STRING:
                return AttributeValueType.STRING;
            case DATE:
                return AttributeValueType.DATE;
            case TIME:
                return AttributeValueType.TIME;
            case TIMESTAMP:
                return AttributeValueType.TIMESTAMP;
            case INTEGER:
                return AttributeValueType.INTEGER;
            case NUMBER:
                return AttributeValueType.NUMBER;
            default:
                break;
            }

            return null;
        }
        /**
         * Converts model type to self.
         * @return model type as self
         */
        public static ArrayDataType fromModelType(AttributeValueType type) {

            switch (type) {
            case STRING:
                return ArrayDataType.STRING;
            case DATE:
                return ArrayDataType.DATE;
            case TIME:
                return ArrayDataType.TIME;
            case TIMESTAMP:
                return ArrayDataType.TIMESTAMP;
            case INTEGER:
                return ArrayDataType.INTEGER;
            case NUMBER:
                return ArrayDataType.NUMBER;
            default:
                break;
            }

            return null;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    default AttributeType getAttributeType() {
        return AttributeType.ARRAY;
    }
    /**
     * Gets type of contained data.
     * @return type
     */
    ArrayDataType getDataType();
    /**
     * Gets contained value.
     * @return value.
     */
    List<ArrayValue<T>> getValue();
    /**
     * Sets the value.
     * @param value to set
     */
    void setValue(List<ArrayValue<T>> value);
    /**
     * Casts and sets the value.
     * @param value to set
     */
    @SuppressWarnings("unchecked")
    default void castValue(Object[] value) {
        if (Objects.isNull(value) || value.length == 0) {
            return;
        }

        final List<ArrayValue<T>> result = new ArrayList<>(value.length);
        for (int i = 0; i < value.length; i++) {

            Object v = value[i];
            switch (getDataType()) {
            case DATE:
                result.add((ArrayValue<T>) new DateArrayValue((LocalDate) v));
                break;
            case INTEGER:
                result.add((ArrayValue<T>) new IntegerArrayValue((Long) v));
                break;
            case NUMBER:
                result.add((ArrayValue<T>) new NumberArrayValue((Double) v));
                break;
            case STRING:
                result.add((ArrayValue<T>) new StringArrayValue((String) v));
                break;
            case DICTIONARY:
                result.add((ArrayValue<T>) new DictionaryArrayValue((String) v));
                break;
            case TIME:
                result.add((ArrayValue<T>) new TimeArrayValue((LocalTime) v));
                break;
            case TIMESTAMP:
                result.add((ArrayValue<T>) new TimestampArrayValue((LocalDateTime) v));
                break;
            }
        }

        setValue(result);
    }

    /**
     * Casts and sets the value.
     * @param value to set
     */
    @SuppressWarnings("unchecked")
    default void castValue(List<?> value) {
        if (Objects.isNull(value) || value.isEmpty()) {
            return;
        }

        final List<ArrayValue<T>> result = new ArrayList<>(value.size());
        for (int i = 0; i < value.size(); i++) {

            Object v = value.get(i);
            switch (getDataType()) {
            case DATE:
                result.add((ArrayValue<T>) new DateArrayValue((LocalDate) v));
                break;
            case INTEGER:
                result.add((ArrayValue<T>) new IntegerArrayValue((Long) v));
                break;
            case NUMBER:
                result.add((ArrayValue<T>) new NumberArrayValue((Double) v));
                break;
            case STRING:
                result.add((ArrayValue<T>) new StringArrayValue((String) v));
                break;
            case DICTIONARY:
                result.add((ArrayValue<T>) new DictionaryArrayValue((String) v));
                break;
            case TIME:
                result.add((ArrayValue<T>) new TimeArrayValue((LocalTime) v));
                break;
            case TIMESTAMP:
                result.add((ArrayValue<T>) new TimestampArrayValue((LocalDateTime) v));
                break;
            }
        }

        setValue(result);
    }

    /**
     * Empty mark.
     * @return true, if empty, false otherwise
     */
    @Override
    default boolean isEmpty() {
        return getValue() == null || getValue().isEmpty();
    }

    /**
     * Returns the underlaying objects as a collection.
     * @return collection
     */
    @SuppressWarnings("unchecked")
    default<V> List<V> getValues() {
        if (isEmpty()) {
            return Collections.emptyList();
        }
        return getValue().stream()
                .map(ArrayValue::getValue)
                .map(v -> (V) v)
                .collect(Collectors.toList());
    }

    /**
     * Collects values to array.
     * @return array of values
     */
    default Object[] toArray() {
        if (isEmpty()) {
            return ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        return getValues().toArray();
    }

    /**
     * Collects values to array.
     * @param cl array class for result
     * @return array of values
     */
    @SuppressWarnings("unchecked")
    default T[] toArray(Class<T[]> cl) {
        Object[] array = toArray();
        if (ArrayUtils.isNotEmpty(array)) {
            return Arrays.copyOf(array, array.length, cl);
        } else {
            return (T[]) ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
    }
}
