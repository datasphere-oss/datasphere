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

package org.datasphere.mdm.core.type.data.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.datasphere.mdm.core.type.data.ArrayAttribute;
import org.datasphere.mdm.core.type.data.Attribute;
import org.datasphere.mdm.core.type.data.AttributeIterator;
import org.datasphere.mdm.core.type.data.BinaryLargeValue;
import org.datasphere.mdm.core.type.data.CharacterLargeValue;
import org.datasphere.mdm.core.type.data.CodeAttribute;
import org.datasphere.mdm.core.type.data.ComplexAttribute;
import org.datasphere.mdm.core.type.data.DataRecord;
import org.datasphere.mdm.core.type.data.SimpleAttribute;

/**
 * @author Mikhail Mikhailov
 * Abstract record.
 */
public class AbstractDataRecord implements DataRecord {

    /**
     * The real data holder.
     */
    private DataRecord data;

    /**
     * Constructor.
     */
    public AbstractDataRecord() {
        super();
    }

    /**
     * Initializing constructor.
     * @param data the view to set
     */
    public AbstractDataRecord(DataRecord data) {
        this();
        internalSet(data);
    }

    /**
     * Internal data view copy.
     * @param other a record
     */
    public void internalGetSet(AbstractDataRecord other) {
        internalSet(other != null ? other.data : null);
    }

    /**
     * Nothing spectacular so far.
     * @param data the data to set
     */
    public void internalSet(DataRecord data) {

        if (Objects.isNull(data)) {
            return;
        }

        if (AbstractDataRecord.class.isInstance(data)) {
            this.data = ((AbstractDataRecord) data).data;
        } else {
            this.data = data;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getAttributeNames() {
        return Objects.isNull(data) ? Collections.emptyList() : data.getAttributeNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Attribute> getAttributeValues() {
        return Objects.isNull(data) ? Collections.emptyList() : data.getAttributeValues();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Attribute> getAttributes() {
        return Objects.isNull(data) ? Collections.emptyMap() : data.getAttributes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Attribute> getAttributesRecursive() {
        return Objects.isNull(data) ? Collections.emptyList() : data.getAttributesRecursive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AttributeIterator attributeIterator() {
        return Objects.isNull(data) ? new AttributeIterator() {
            @Override
            public Attribute next() { throw new NoSuchElementException(); }
            @Override
            public boolean hasNext() { return false; }
        } : data.attributeIterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<SimpleAttribute<?>> getSimpleAttributes() {
        return Objects.isNull(data) ? Collections.emptyList() : data.getSimpleAttributes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleAttribute<?> getSimpleAttribute(String name) {
        return Objects.isNull(data) ? null : data.getSimpleAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<SimpleAttribute<?>> getSimpleAttributeRecursive(String name) {
        return Objects.isNull(data) ? Collections.emptyList() : data.getSimpleAttributeRecursive(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<CodeAttribute<?>> getCodeAttributes() {
        return Objects.isNull(data) ? Collections.emptyList() : data.getCodeAttributes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeAttribute<?> getCodeAttribute(String name) {
        return Objects.isNull(data) ? null : data.getCodeAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ArrayAttribute<?>> getArrayAttributes() {
        return Objects.isNull(data) ? Collections.emptyList() : data.getArrayAttributes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayAttribute<?> getArrayAttribute(String name) {
        return Objects.isNull(data) ? null : data.getArrayAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ArrayAttribute<?>> getArrayAttributeRecursive(String name) {
        return Objects.isNull(data) ? Collections.emptyList() : data.getArrayAttributeRecursive(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ComplexAttribute getComplexAttribute(String name) {
        return Objects.isNull(data) ? null : data.getComplexAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ComplexAttribute> getComplexAttributeRecursive(String name) {
        return Objects.isNull(data) ? Collections.emptyList() : data.getComplexAttributeRecursive(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ComplexAttribute> getComplexAttributes() {
        return Objects.isNull(data) ? Collections.emptyList() : data.getComplexAttributes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAll(Collection<? extends Attribute> attributes) {

        if (Objects.isNull(data)) {
            return;
        }

        data.addAll(attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAttribute(Attribute attribute) {

        if (Objects.isNull(data)) {
            return ;
        }

        data.addAttribute(attribute);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attribute getAttribute(String name) {
        return Objects.isNull(data) ? null : data.getAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Attribute> getAttributeRecursive(String path) {
        return Objects.isNull(data) ? Collections.emptyList() : data.getAttributeRecursive(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String putAttribute(String name, String value) {
        return Objects.isNull(data) ? null : data.putAttribute(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long putAttribute(String name, Long value) {
        return Objects.isNull(data) ? null : data.putAttribute(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double putAttribute(String name, Double value) {
        return Objects.isNull(data) ? null : data.putAttribute(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean putAttribute(String name, Boolean value) {
        return Objects.isNull(data) ? null : data.putAttribute(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate putAttribute(String name, LocalDate value) {
        return Objects.isNull(data) ? null : data.putAttribute(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalTime putAttribute(String name, LocalTime value) {
        return Objects.isNull(data) ? null : data.putAttribute(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime putAttribute(String name, LocalDateTime value) {
        return Objects.isNull(data) ? null: data.putAttribute(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BinaryLargeValue putAttribute(String name, BinaryLargeValue value) {
        return Objects.isNull(data) ? null : data.putAttribute(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharacterLargeValue putAttribute(String name, CharacterLargeValue value) {
        return Objects.isNull(data) ? null : data.putAttribute(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DataRecord> putAttribute(String name, Collection<DataRecord> value) {
        return Objects.nonNull(data) ? data.putAttribute(name, value) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAttribute(String name) {
        return Objects.nonNull(data) && data.containsAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attribute removeAttribute(String name) {
        return Objects.isNull(data) ? null : data.removeAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Attribute> removeAttributeRecursive(String path) {
        return Objects.isNull(data) ? Collections.emptyList() : data.removeAttributeRecursive(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return Objects.isNull(data) ? 0 : data.getSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTopLevel() {
        return Objects.nonNull(data) && data.isTopLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasParent() {
        return Objects.nonNull(data) && data.hasParent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrdinal() {
        return Objects.nonNull(data) ? data.getOrdinal() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord getParentRecord() {
        return Objects.nonNull(data) ? data.getParentRecord() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ComplexAttribute getHolderAttribute() {
        return Objects.nonNull(data) ? data.getHolderAttribute() : null;
    }
}
