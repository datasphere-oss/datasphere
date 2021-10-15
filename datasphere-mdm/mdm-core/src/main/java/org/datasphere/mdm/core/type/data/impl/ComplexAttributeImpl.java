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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Stream;

import com.huahui.datasphere.mdm.core.type.data.ComplexAttribute;
import com.huahui.datasphere.mdm.core.type.data.DataRecord;

/**
 * @author theseusyang
 * Complex attribute implementation.
 */
public class ComplexAttributeImpl extends AbstractAttribute implements ComplexAttribute {
    /**
     * Empty records array.
     */
    private static final DataRecord[] EMPTY_RECORDS_ARRAY = new DataRecord[]{};
    /**
     * Nested records.
     */
    private List<DataRecord> records = new ArrayList<>(4);
    /**
     * Unattended mode.
     */
    private boolean unattended;
    /**
     * Special serialization constructor. Schould not be used otherwise.
     */
    protected ComplexAttributeImpl() {
        super();
    }
    /**
     * Constructor.
     * @param name the name of the complex attribute.
     */
    public ComplexAttributeImpl(String name) {
        super(name);
    }
    /**
     * Constructor.
     * @param name the name of the complex attribute.
     * @param values the values to set
     */
    public ComplexAttributeImpl(String name, Collection<DataRecord> values) {
        this(name);
        addAll(values);
    }
    /**
     * Don't touch links by record manipulations.
     * @param name the name
     * @param records the records
     * @return attribute
     */
    public static ComplexAttribute ofUnattended(String name, DataRecord... records) {
        return ofUnattended(name, Arrays.asList(records));
    }
    /**
     * Don't touch links by record manipulations.
     * @param name the name
     * @param records the records
     * @return attribute
     */
    public static ComplexAttribute ofUnattended(String name, Collection<DataRecord> records) {

        ComplexAttributeImpl impl = new ComplexAttributeImpl(name);
        impl.unattended = true;
        impl.addAll(records);

        return impl;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void add(DataRecord record) {

        if (Objects.nonNull(record)) {

            if (!unattended) {
                ((SerializableDataRecord) record).setHolderAttribute(this);
                ((SerializableDataRecord) record).setOrdinal(records.size());
            }

            records.add(record);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAll(Collection<DataRecord> values) {

        if (Objects.nonNull(values) && !values.isEmpty()) {
            for (DataRecord value : values) {
                add(value);
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord get(int i) {

        // Suppress AIOOB
        if (i >= records.size()) {
            return null;
        }

        return records.get(i);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord remove(int i) {

        // Suppress AIOOB
        if (i >= records.size()) {
            return null;
        }

        DataRecord removed = records.remove(i);
        if (!unattended) {
            ((SerializableDataRecord) removed).setHolderAttribute(null);
            ((SerializableDataRecord) removed).setOrdinal(-1);
        }

        return removed;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DataRecord> removeAll() {

        if (records.isEmpty()) {
            return Collections.emptyList();
        }

        ArrayList<DataRecord> removed = new ArrayList<>(size());
        while (!records.isEmpty()) {
            removed.add(remove(records.size() - 1));
        }

        return removed;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return records.size();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        while (!records.isEmpty()) {
            remove(records.size() - 1);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord[] toArray() {
        return records.isEmpty() ? EMPTY_RECORDS_ARRAY : records.toArray(new DataRecord[records.size()]);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DataRecord> toCollection() {
        return records.isEmpty() ? Collections.emptyList() : new ArrayList<>(records);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<DataRecord> stream() {
        return records.stream();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<DataRecord> iterator() {
        return records.isEmpty()
                ? Collections.emptyIterator()
                : new ComplexAttributeIterator(records.listIterator());
    }
    /**
     * @author theseusyang
     * This attribute data record's iterator.
     */
    private class ComplexAttributeIterator implements Iterator<DataRecord> {
        /**
         * Constructor.
         * @param it opened iterator
         */
        ComplexAttributeIterator(ListIterator<DataRecord> it) {
            super();
            this.it = it;
        }
        /**
         * Current index.
         */
        private ListIterator<DataRecord> it;
        /**
         * Current record pointer.
         */
        private DataRecord current = null;
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            return it.hasNext();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public DataRecord next() {
            current = it.next();
            return current;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void remove() {

            it.remove();

            // No exceptions means, the record is the current one
            // and it was successfullty removed
            if (!ComplexAttributeImpl.this.unattended) {
                ((SerializableDataRecord) current).setHolderAttribute(null);
                ((SerializableDataRecord) current).setOrdinal(-1);
            }
        }
    }
}
