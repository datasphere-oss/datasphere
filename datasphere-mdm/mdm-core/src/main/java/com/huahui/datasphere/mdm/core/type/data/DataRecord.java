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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @author theseusyang
 * Data record, which may contain other data records as part of complex attributes.
 */
public interface DataRecord {
    /**
     * Gets all attribute names on this level.
     * E. g. it doesn't include attribute names from nested record of complex attributes.
     * @return unmodifiable view of all attribute names
     */
    Collection<String> getAttributeNames();
    /**
     * Gets all attributes as map.
     * @return all attributes as map.
     */
    Map<String, Attribute> getAttributes();
    /**
     * Gets all attributes on the first level regardless of their type.
     * @return unmodifiable view of all attributes.
     */
    Collection<Attribute> getAttributeValues();
    /**
     * Gets all attributes regardless of their type recursively.
     * @return unmodifiable view of all attributes.
     */
    Collection<Attribute> getAttributesRecursive();
    /**
     * Gets attribute by name.
     * @param name the name of the attribute
     * @return attribute or null, if not found
     */
    Attribute getAttribute(String name);
    /**
     * Gets attributes by name recursive.
     * @param path the name of the attribute (path, may be with '.')
     * @return attribute or null, if not found
     */
    Collection<Attribute> getAttributeRecursive(String path);
    /**
     * Gets RW attribute iterator.
     * @return iterator
     */
    AttributeIterator attributeIterator();
    /**
     * Gets all simple attributes.
     * @return read-only list of simple attributes
     */
    Collection<SimpleAttribute<?>> getSimpleAttributes();
    /**
     * Gets simple attribute by name.
     * @param name the name of the attribute
     * @return simple attribute or null
     */
    SimpleAttribute<?> getSimpleAttribute(String name);
    /**
     * Gets simple attribute recursive by name.
     * @param name the name of the attribute
     * @return simple attributes or empty collection
     */
    Collection<SimpleAttribute<?>> getSimpleAttributeRecursive(String name);
    /**
     * Gets all code attributes.
     * @return read-only list of code attributes
     */
    Collection<CodeAttribute<?>> getCodeAttributes();
    /**
     * Gets code attribute by name.
     * @param name the name of the attribute
     * @return code attribute or null
     */
    CodeAttribute<?> getCodeAttribute(String name);
    /**
     * Gets all array attributes.
     * @return read-only list of array attributes
     */
    Collection<ArrayAttribute<?>> getArrayAttributes();
    /**
     * Gets array attribute by name.
     * @param name the name of the attribute
     * @return array attribute or null
     */
    ArrayAttribute<?> getArrayAttribute(String name);
    /**
     * Gets array attribute recursive by name.
     * @param name the name of the attribute
     * @return simple attributes or empty collection
     */
    Collection<ArrayAttribute<?>> getArrayAttributeRecursive(String name);
    /**
     * Gets all complex attributes.
     * @return read-only list of complex attributes
     */
    Collection<ComplexAttribute> getComplexAttributes();
    /**
     * Gets a complex attribute by name.
     * @param name the name
     * @return complex attribute or null
     */
    ComplexAttribute getComplexAttribute(String name);
    /**
     * Gets a complex attribute recursive by name.
     * @param name the name
     * @return complex attributes or empty collection
     */
    Collection<ComplexAttribute> getComplexAttributeRecursive(String name);
    /**
     * Adds an attribute.
     * @param attribute the attribute to add
     */
    void addAttribute(Attribute attribute);
    /**
     * Adds several attributes at once.
     * @param attributes attributes to add
     */
    void addAll(Collection<? extends Attribute> attributes);
    /**
     * Puts a {@link String} attribute.
     * @param name name of the attribute
     * @param value value of the attribute
     * @return previous value if the key was already mapped
     */
    String putAttribute(String name, String value);
    /**
     * Puts a {@link Long} attribute.
     * @param name name of the attribute
     * @param value value of the attribute
     * @return previous value if the key was already mapped
     */
    Long putAttribute(String name, Long value);
    /**
     * Puts a {@link Double} attribute.
     * @param name name of the attribute
     * @param value value of the attribute
     * @return previous value if the key was already mapped
     */
    Double putAttribute(String name, Double value);
    /**
     * Puts a {@link Boolean} attribute.
     * @param name name of the attribute
     * @param value value of the attribute
     * @return previous value if the key was already mapped
     */
    Boolean putAttribute(String name, Boolean value);
    /**
     * Puts a {@link LocalDate} attribute.
     * @param name name of the attribute
     * @param value value of the attribute
     * @return previous value if the key was already mapped
     */
    LocalDate putAttribute(String name, LocalDate value);
    /**
     * Puts a {@link LocalTime} attribute.
     * @param name name of the attribute
     * @param value value of the attribute
     * @return previous value if the key was already mapped
     */
    LocalTime putAttribute(String name, LocalTime value);
    /**
     * Puts a {@link LocalDateTime} attribute.
     * @param name name of the attribute
     * @param value value of the attribute
     * @return previous value if the key was already mapped
     */
    LocalDateTime putAttribute(String name, LocalDateTime value);
    /**
     * Puts a {@link BinaryLargeValue} attribute.
     * @param name name of the attribute
     * @param value value of the attribute
     * @return previous value if the key was already mapped
     */
    BinaryLargeValue putAttribute(String name, BinaryLargeValue value);
    /**
     * Puts a {@link CharacterLargeValue} attribute.
     * @param name name of the attribute
     * @param value value of the attribute
     * @return previous value if the key was already mapped
     */
    CharacterLargeValue putAttribute(String name, CharacterLargeValue value);
    /**
     * Puts a collection of {@link DataRecord} to complex attribute with name 'name'.
     * @param name name of the attribute to put collection to
     * @param value value of the complext attribute
     * @return previous value if the key was already mapped
     */
    Collection<DataRecord> putAttribute(String name, Collection<DataRecord> value);
    /**
     * Checks presence of an attribute.
     * @param name the attribute's name.
     * @return true, if mapped, false otherwise
     */
    boolean containsAttribute(String name);
    /**
     * Removes attribute, if it can be found.
     * @param name the attribute name.
     * @return the value, if it was mapped, null otherwise
     */
    Attribute removeAttribute(String name);
    /**
     * Removes attributes recursive by given path.
     * @param path the path
     * @return removed attributes
     */
    Collection<Attribute> removeAttributeRecursive(String path);
    /**
     * Gets the number of all attributes in a data record.
     * @return size
     */
    int getSize();
    /**
     * Tells whether this record is a top level record (i. e. not owned by a complex attribute).
     * @return true, if so, false otherwise
     */
    boolean isTopLevel();
    /**
     * Returns true, if contains no attributes.
     * @return true, if contains no attributes, false otherwise
     */
    default boolean isEmpty() {
        return getSize() == 0;
    }
    /**
     * Tells whether this record has a parent record (i. e. owned by a complex attribute).
     * @return true, if so, false otherwise
     */
    boolean hasParent();
    /**
     * Gets ordinal starting from 0.
     * @return ordinal number
     */
    int getOrdinal();
    /**
     * Gets its parent record if any.
     * @return parent record or null, if this is a top level record
     */
    DataRecord getParentRecord();
    /**
     * Gets its holder attribute if any.
     * @return parent attribute or null, if this is a top level record
     */
    ComplexAttribute getHolderAttribute();
    /**
     * Gets the local record path.
     * @return local path, such as complex_attr[1]
     */
    default String toLocalPath() {

        if (isTopLevel()) {
            return StringUtils.EMPTY;
        }

        return new StringBuilder(128)
                .append(getHolderAttribute().toLocalPath())
                .append("[" + getOrdinal() + "]").toString();
    }
    /**
     * Gets the path segments of this record.
     * @return path segments
     */
    default List<String> toPathSegments() {

        if (isTopLevel()) {
            return Collections.emptyList();
        }

        return getHolderAttribute().toPathSegments();
    }
}
