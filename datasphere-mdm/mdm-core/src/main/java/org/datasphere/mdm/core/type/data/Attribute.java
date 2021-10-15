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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author theseusyang
 * Basic attribute properties.
 */
public interface Attribute {
    /**
     * @author theseusyang
     * Denotes type of an attribute.
     */
    public enum AttributeType {
        /**
         * Simple attribute. Can contain single value.
         * Can not contain other attributes.
         */
        SIMPLE,
        /**
         * Array attribute. Can contain multiple values of the same type.
         * Can not contain other attributes.
         */
        ARRAY,
        /**
         * Complex attribute. Cannot contain simple values.
         * Contains other nested records and thus attributes.
         */
        COMPLEX,
        /**
         * Code attribute. Key attribute for lookup entity records.
         * Can not contain other attributes, but can contain supplementary values.
         */
        CODE
    }
    /**
     * Gets type of this attribute.
     * @return type
     */
    AttributeType getAttributeType();
    /**
     * Gets name of this attribute.
     * @return name
     */
    String getName();
    /**
     * Gets the parent record point, the simple attribute is currently associated with.
     * @return parent link or null
     */
    DataRecord getRecord();
    /**
     * Sets the current association with a record.
     * @param record the record holding the attribute
     */
    void setRecord(DataRecord record);
    /**
     * Returns true, if this attribute holds no values (null for SDTs, {@link ArrayValue} or records).
     * @return true for no values, false otherwise
     */
    boolean isEmpty();
    /**
     * Returns true, if this attribute type can hold only a single value (i. e. is a code or simple attribute).
     * Return false for complex and array..
     * @return true, if this attribute type can hold only a single value, false otherwise.
     */
    default boolean isSingleValue() {
        return false;
    }
    /**
     * Returns true, if the attribute is a simple one.
     * @return true, if the attribute is a simple one
     */
    default boolean isSimple() {
        return getAttributeType() == AttributeType.SIMPLE;
    }
    /**
     * Returns true, if the attribute is a code one.
     * @return true, if the attribute is a code one
     */
    default boolean isCode() {
        return getAttributeType() == AttributeType.CODE;
    }
    /**
     * Returns true, if the attribute is an array one.
     * @return true, if the attribute is an array one
     */
    default boolean isArray() {
        return getAttributeType() == AttributeType.ARRAY;
    }
    /**
     * Returns true, if the attribute is a complex one.
     * @return true, if the attribute is a complex one
     */
    default boolean isComplex() {
        return getAttributeType() == AttributeType.COMPLEX;
    }
    /**
     * Saves a couple of ugly casts.
     * @return self as a cast type
     */
    @SuppressWarnings("unchecked")
    default <T extends Attribute> T narrow() {
        return (T) this;
    }
    /**
     * Gets the local attribute path.
     * @return local path, such as complex_attr[1].simple_attr
     */
    default String toLocalPath() {

        StringBuilder sb = new StringBuilder(128);
        Attribute attr = this;
        while (true) {

            sb.insert(0, attr.getName());
            if (Objects.nonNull(attr.getRecord()) && !attr.getRecord().isTopLevel()) {
                sb.insert(0, "[" + attr.getRecord().getOrdinal() + "].");
                attr = attr.getRecord().getHolderAttribute();
            } else {
                break;
            }
        }

        return sb.toString();
    }
    /**
     * Gets the attribute's model path.
     * @return model path, such as complex_attr.simple_attr
     */
    default String toModelPath() {

        StringBuilder sb = new StringBuilder(128);
        Attribute attr = this;
        while (true) {

            sb.insert(0, attr.getName());
            if (Objects.nonNull(attr.getRecord()) && !attr.getRecord().isTopLevel()) {
                sb.insert(0, ".");
                attr = attr.getRecord().getHolderAttribute();
            } else {
                break;
            }
        }

        return sb.toString();
    }
    /**
     * Gets the path segments.
     * @return list of path segments.
     */
    default List<String> toPathSegments() {

        List<String> parents = getRecord() == null || getRecord().isTopLevel()
                ? Collections.emptyList()
                : getRecord().getHolderAttribute().toPathSegments();

        List<String> segments = new ArrayList<>(parents.size() + 1);
        segments.addAll(parents);
        segments.add(getName());

        return segments;
    }
}
