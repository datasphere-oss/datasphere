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

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author theseusyang
 * Complex attribute.
 */
public interface ComplexAttribute extends Attribute, Iterable<DataRecord> {
    /**
     * {@inheritDoc}
     */
    @Override
    default AttributeType getAttributeType() {
        return AttributeType.COMPLEX;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isEmpty() {
        return size() == 0;
    }
    /**
     * Adds a record to this attribute.
     * @param record the record to add
     */
    void add(DataRecord record);
    /**
     * Adds all records to this attribute.
     * @param records the records to add
     */
    void addAll(Collection<DataRecord> records);
    /**
     * Gets the record at index i.
     * @param i the index
     * @return record or null
     */
    DataRecord get(int i);
    /**
     * Removes record at index i.
     * @param i the index to remove.
     */
    DataRecord remove(int i);
    /**
     * Removes all the records from this CA and returns the removed.
     */
    Collection<DataRecord> removeAll();
    /**
     * Clears the underlaying collection.
     */
    void clear();
    /**
     * Gets the number of records in this complex attribute.
     * @return the number of records
     */
    int size();
    /**
     * Returns records as array. Links will be still maintaned. Call clear to forget them.
     * @return array
     */
    DataRecord[] toArray();
    /**
     * Retirns unattended view of records of this complex attribute.
     * Adding to or removing items from this collection does not affect internal state of the attribute.
     * @return collection of records
     */
    Collection<DataRecord> toCollection();
    /**
     * Gets a stream of underlaying records.
     * @return stream of records
     */
    Stream<DataRecord> stream();
}
