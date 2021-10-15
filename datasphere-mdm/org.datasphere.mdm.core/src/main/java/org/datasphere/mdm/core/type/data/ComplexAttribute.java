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

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Mikhail Mikhailov
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
