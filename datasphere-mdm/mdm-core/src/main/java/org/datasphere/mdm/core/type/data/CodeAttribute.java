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

import java.util.List;

import com.huahui.datasphere.mdm.search.type.FieldType;

import com.huahui.datasphere.mdm.core.type.model.AttributeElement.AttributeValueType;

/**
 * @author theseusyang
 * Code attribute values.
 */
public interface CodeAttribute<T> extends SingleValueAttribute<T> {
    /**
     * @author theseusyang
     * Denotes type of the contained data.
     */
    enum CodeDataType {
        /**
         * The string type.
         */
        STRING,
        /**
         * The integer type (long 8 bytes).
         */
        INTEGER;
        /**
         * Converts self to search type.
         * @return self as search type
         */
        public FieldType toSearchType() {

            switch (this) {
            case STRING:
                return FieldType.STRING;
            case INTEGER:
                return FieldType.INTEGER;
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
            case INTEGER:
                return AttributeValueType.INTEGER;
            default:
                break;
            }

            return null;
        }
        /**
         * Converts model type to self .
         * @return model type as self
         */
        public static CodeDataType fromModelType(AttributeValueType type) {

            switch (type) {
            case STRING:
                return CodeDataType.STRING;
            case INTEGER:
                return CodeDataType.INTEGER;
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
        return AttributeType.CODE;
    }
    /**
     * Gets type of contained data.
     * @return type
     */
    CodeDataType getDataType();

    /**
     * Gets contained supplementary values.
     * @return values.
     */
    List<T> getSupplementary();
    /**
     * Gets contained supplementary values.
     * @return values.
     */
    @SuppressWarnings("unchecked")
    default<V> List<V> castSupplementary() {
        return (List<V>) getSupplementary();
    }
    /**
     * Sets the supplementary values.
     * @param value to set
     */
    void setSupplementary(List<T> value);
    /**
     * Tells, whether this attribute has supplementary values set.
     * @return true, if so, false otherwise
     */
    default boolean hasSupplementary() {
        return getSupplementary() != null && !getSupplementary().isEmpty();
    }
}
