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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.type.data.ArrayAttribute;
import org.datasphere.mdm.core.type.data.Attribute;
import org.datasphere.mdm.core.type.data.AttributeIterator;
import org.datasphere.mdm.core.type.data.BinaryLargeValue;
import org.datasphere.mdm.core.type.data.CharacterLargeValue;
import org.datasphere.mdm.core.type.data.CodeAttribute;
import org.datasphere.mdm.core.type.data.CodeLinkValue;
import org.datasphere.mdm.core.type.data.ComplexAttribute;
import org.datasphere.mdm.core.type.data.DataRecord;
import org.datasphere.mdm.core.type.data.MeasuredValue;
import org.datasphere.mdm.core.type.data.SimpleAttribute;
import org.datasphere.mdm.core.type.data.Attribute.AttributeType;
import org.datasphere.mdm.core.type.data.SimpleAttribute.SimpleDataType;
import org.datasphere.mdm.system.exception.PlatformFailureException;

/**
 * @author Mikhail Mikhailov
 * Data view - simple and complex attributes.
 */
public class SerializableDataRecord implements DataRecord {

    /**
     * Internal storage, allowing O(1) key access.
     * Hash map from JDK is taken temporarilly. Looking for a memory effective replacement.
     */
    private Map<String, Attribute> map;
    /**
     * Ordinal of this record.
     */
    private int ordinal = -1;
    /**
     * This record's holder attribute.
     */
    private ComplexAttribute holder;
    /**
     * Constructor.
     * @param predictedSize number of attributes.
     */
    public SerializableDataRecord(int predictedSize) {
        super();
        map = new HashMap<>(predictedSize);
    }

    /**
     * Constructor.
     */
    public SerializableDataRecord() {
        this(16);
    }

    /**
     * Constructor.
     */
    public static SerializableDataRecord of(DataRecord other) {

        if (Objects.isNull(other)) {
            return null;
        }

        Collection<Attribute> attrs = other.getAttributeValues();
        SerializableDataRecord record = new SerializableDataRecord(attrs.size());
        for (Attribute attr : attrs) {
            switch (attr.getAttributeType()) {
            case SIMPLE:
                record.addAttribute(of((SimpleAttribute<?>) attr));
                break;
            case ARRAY:
                record.addAttribute(of((ArrayAttribute<?>) attr));
                break;
            case CODE:
                record.addAttribute(of((CodeAttribute<?>) attr));
                break;
            case COMPLEX:
                record.addAttribute(of((ComplexAttribute) attr));
                break;
            }
        }

        return record;
    }

    /**
     * Copies simple attribute.
     * @param attr the attribute to copy
     * @return new attribute
     */
    public static ComplexAttribute of(ComplexAttribute attr) {

        if (Objects.isNull(attr)) {
            return null;
        }

        ComplexAttributeImpl result = new ComplexAttributeImpl(attr.getName());
        for (DataRecord dr : attr) {
            result.add(of(dr));
        }

        return result;
    }

    /**
     * Copies simple attribute.
     * @param attr the attribute to copy
     * @return new attribute
     */
    public static SimpleAttribute<?> of(SimpleAttribute<?> attr) {

        if (Objects.isNull(attr)) {
            return null;
        }

        SimpleAttribute<?> result;
        switch (attr.getDataType()) {
        case BLOB:
            result = new BlobSimpleAttributeImpl(attr.getName(), attr.castValue());
            break;
        case BOOLEAN:
            result = new BooleanSimpleAttributeImpl(attr.getName(), attr.castValue());
            break;
        case CLOB:
            result = new ClobSimpleAttributeImpl(attr.getName(), attr.castValue());
            break;
        case DATE:
            result = new DateSimpleAttributeImpl(attr.getName(), attr.castValue());
            break;
        case INTEGER:
            result = new IntegerSimpleAttributeImpl(attr.getName(), attr.castValue());
            break;
        case NUMBER:
            result = new NumberSimpleAttributeImpl(attr.getName(), attr.castValue());
            break;
        case MEASURED:
            result = new MeasuredSimpleAttributeImpl(attr.getName(), attr.<MeasuredValue>castValue());
            break;
        case STRING:
            result = new StringSimpleAttributeImpl(attr.getName(), attr.castValue());
            ((StringSimpleAttributeImpl) result).setLinkEtalonId(((CodeLinkValue) attr).getLinkEtalonId());
            break;
        case DICTIONARY:
            result = new DictionarySimpleAttributeImpl(attr.getName(), attr.castValue());
            break;
        case TIME:
            result = new TimeSimpleAttributeImpl(attr.getName(), attr.castValue());
            break;
        case TIMESTAMP:
            result = new TimestampSimpleAttributeImpl(attr.getName(), attr.castValue());
            break;
        case ENUM:
            result = new EnumSimpleAttributeImpl(attr.getName(), attr.castValue());
            break;
        default:
            return null;
        }

        ((AbstractSimpleAttribute<?>) result).setDisplayValue(attr.getDisplayValue());
        return result;
    }

    /**
     * Copies array attribute.
     * @param attr the attribute to copy
     * @return cloned attribute
     */
    public static ArrayAttribute<?> of (ArrayAttribute<?> attr) {

        if (Objects.isNull(attr)) {
            return null;
        }

        ArrayAttribute<?> result;
        switch (attr.getDataType()) {
        case DATE:
            result = new DateArrayAttributeImpl(attr.getName());
            ((DateArrayAttributeImpl) result).setValue(attr.isEmpty()
                    ? null
                    : attr.getValue().stream().map(v -> new DateArrayValue((LocalDate) v.getValue())).collect(Collectors.toList()));
            break;
        case INTEGER:
            result = new IntegerArrayAttributeImpl(attr.getName());
            ((IntegerArrayAttributeImpl) result).setValue(attr.isEmpty()
                    ? null
                    : attr.getValue().stream().map(v -> new IntegerArrayValue((Long) v.getValue())).collect(Collectors.toList()));
            break;
        case NUMBER:
            result = new NumberArrayAttributeImpl(attr.getName());
            ((NumberArrayAttributeImpl) result).setValue(attr.isEmpty()
                    ? null
                    : attr.getValue().stream().map(v -> new NumberArrayValue((Double) v.getValue())).collect(Collectors.toList()));
            break;
        case STRING:
            result = new StringArrayAttributeImpl(attr.getName());
            ((StringArrayAttributeImpl) result).setValue(attr.isEmpty()
                    ? null
                    : attr.getValue().stream().map(v -> new StringArrayValue((String) v.getValue())).collect(Collectors.toList()));
            break;
        case TIME:
            result = new TimeArrayAttributeImpl(attr.getName());
            ((TimeArrayAttributeImpl) result).setValue(attr.isEmpty()
                    ? null
                    : attr.getValue().stream().map(v -> new TimeArrayValue((LocalTime) v.getValue())).collect(Collectors.toList()));
            break;
        case TIMESTAMP:
            result = new TimestampArrayAttributeImpl(attr.getName());
            ((TimestampArrayAttributeImpl) result).setValue(attr.isEmpty()
                    ? null
                    : attr.getValue().stream().map(v -> new TimestampArrayValue((LocalDateTime) v.getValue())).collect(Collectors.toList()));
            break;
        default:
            return null;
        }

        return result;
    }

    /**
     * Copies code attribute.
     * @param attr the attribute
     * @return copy
     */
    public static CodeAttribute<?> of (CodeAttribute<?> attr) {

        if (Objects.isNull(attr)) {
            return null;
        }

        CodeAttribute<?> result;
        switch (attr.getDataType()) {
        case INTEGER:
            result = new IntegerCodeAttributeImpl(attr.getName(), attr.castValue());
            ((IntegerCodeAttributeImpl) result).setSupplementary(attr.hasSupplementary()
                    ? attr.getSupplementary().stream().map(Long.class::cast).collect(Collectors.toList())
                    : null);
            break;
        case STRING:
            result = new StringCodeAttributeImpl(attr.getName(), attr.castValue());
            ((StringCodeAttributeImpl) result).setSupplementary(attr.hasSupplementary()
                    ? attr.getSupplementary().stream().map(String.class::cast).collect(Collectors.toList())
                    : null);
            break;
        default:
            return null;
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Attribute> getAttributes() {
        return map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getAttributeNames() {
        return map.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Attribute> getAttributeValues() {
        return map.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Attribute> getAttributesRecursive() {

        List<Attribute> collected = new ArrayList<>(map.size());
        for (Entry<String, Attribute> en : map.entrySet()) {
            if (en.getValue().getAttributeType() == AttributeType.COMPLEX) {
                collected.add(en.getValue());

                ComplexAttribute inner = (ComplexAttribute) en.getValue();
                if (inner.isEmpty()) {
                    continue;
                }

                for (DataRecord r : inner) {
                    collected.addAll(r.getAttributesRecursive());
                }
            } else {
                collected.add(en.getValue());
            }
        }

        return collected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attribute getAttribute(String name) {
        return map.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Attribute> getAttributeRecursive(String path) {

        String[] tokens = StringUtils.split(path, '.');
        if (Objects.nonNull(tokens) && tokens.length > 0) {

            if (tokens.length == 1) {
                Attribute get = getAttribute(path);
                return get != null ? Collections.singletonList(get) : Collections.emptyList();
            }

            Attribute complex = getAttribute(tokens[0]);
            if (Objects.nonNull(complex) && complex.getAttributeType() == AttributeType.COMPLEX) {

                ComplexAttribute complexAttr = (ComplexAttribute) complex;
                List<Attribute> result = new ArrayList<>(complexAttr.size());
                for (DataRecord nested : complexAttr) {
                    result.addAll(nested.getAttributeRecursive(StringUtils.join(tokens, '.', 1, tokens.length)));
                }

                return result;
            }
        }

        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AttributeIterator attributeIterator() {
        return new AttributeIteratorImpl(map.entrySet().iterator());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<SimpleAttribute<?>> getSimpleAttributes() {
        return map.values().stream()
                .filter(a -> a.getAttributeType() == AttributeType.SIMPLE)
                .map(Attribute::<SimpleAttribute<?>>narrow)
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleAttribute<?> getSimpleAttribute(String name) {
        Attribute attr = getAttribute(name);
        return attr != null && attr.getAttributeType() == AttributeType.SIMPLE ? (SimpleAttribute<?>) attr : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<SimpleAttribute<?>> getSimpleAttributeRecursive(String name) {

        Collection<Attribute> attrs = getAttributeRecursive(name);
        if (!attrs.isEmpty()) {
            return attrs.stream()
                    .filter(attr -> attr.getAttributeType() == AttributeType.SIMPLE)
                    .map(Attribute::<SimpleAttribute<?>>narrow)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<CodeAttribute<?>> getCodeAttributes() {
        return map.values().stream()
                .filter(a -> a.getAttributeType() == AttributeType.CODE)
                .map(Attribute::<CodeAttribute<?>>narrow)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeAttribute<?> getCodeAttribute(String name) {
        Attribute attr = getAttribute(name);
        return attr != null && attr.getAttributeType() == AttributeType.CODE ? (CodeAttribute<?>) attr : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ArrayAttribute<?>> getArrayAttributes() {
        return map.values().stream()
                .filter(a -> a.getAttributeType() == AttributeType.ARRAY)
                .map(Attribute::<ArrayAttribute<?>>narrow)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayAttribute<?> getArrayAttribute(String name) {
        Attribute attr = getAttribute(name);
        return attr != null && attr.getAttributeType() == AttributeType.ARRAY ? (ArrayAttribute<?>) attr : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ArrayAttribute<?>> getArrayAttributeRecursive(String name) {

        Collection<Attribute> attrs = getAttributeRecursive(name);
        if (!attrs.isEmpty()) {
            return attrs.stream()
                    .filter(attr -> attr.getAttributeType() == AttributeType.ARRAY)
                    .map(Attribute::<ArrayAttribute<?>>narrow)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ComplexAttribute getComplexAttribute(String name) {
        Attribute attr = getAttribute(name);
        return attr != null && attr.getAttributeType() == AttributeType.COMPLEX ? (ComplexAttribute) attr : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ComplexAttribute> getComplexAttributeRecursive(String name) {

        Collection<Attribute> attrs = getAttributeRecursive(name);
        if (!attrs.isEmpty()) {
            return attrs.stream()
                    .filter(attr -> attr.getAttributeType() == AttributeType.COMPLEX)
                    .map(Attribute::<ComplexAttribute>narrow)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ComplexAttribute> getComplexAttributes() {
        return map.values().stream()
                .filter(a -> a.getAttributeType() == AttributeType.COMPLEX)
                .map(Attribute::<ComplexAttribute>narrow)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAll(Collection<? extends Attribute> attributes) {
        for (Attribute attribute : attributes) {
            addAttribute(attribute);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAttribute(Attribute attribute) {
        if (Objects.isNull(attribute)) {
            return;
        }

        map.put(attribute.getName(), attribute);
        attribute.setRecord(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String putAttribute(String name, String value) {

        String oldValue = null;
        Attribute existing = map.get(name);
        if (Objects.isNull(existing)) {
            addAttribute(new StringSimpleAttributeImpl(name, value));
        } else {
            if (existing.getAttributeType() != AttributeType.SIMPLE) {

                throw new PlatformFailureException(
                        "Put string simple attribute '{}': Attribute exists and is not simple.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_STRING_NOT_SIMPLE,
                        name);
            } else if (((SimpleAttribute<?>) existing).getDataType() != SimpleDataType.STRING) {

                throw new PlatformFailureException(
                        "Put string simple attribute '{}': Attribute exists and is not string.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_STRING_NOT_STRING,
                        name);
            }

            StringSimpleAttributeImpl cast = (StringSimpleAttributeImpl) existing;
            oldValue = cast.getValue();
            cast.setValue(value);
        }

        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long putAttribute(String name, Long value) {

        Long oldValue = null;
        Attribute existing = map.get(name);
        if (Objects.isNull(existing)) {
            addAttribute(new IntegerSimpleAttributeImpl(name, value));
        } else {
            if (existing.getAttributeType() != AttributeType.SIMPLE) {

                throw new PlatformFailureException(
                        "Put integer simple attribute '{}': Attribute exists and is not simple.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_INT_NOT_SIMPLE,
                        name);
            } else if (((SimpleAttribute<?>) existing).getDataType() != SimpleDataType.INTEGER) {

                throw new PlatformFailureException(
                        "Put integer simple attribute '{}': Attribute exists and is not integer.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_INT_NOT_INT,
                        name);
            }

            IntegerSimpleAttributeImpl cast = (IntegerSimpleAttributeImpl) existing;
            oldValue = cast.getValue();
            cast.setValue(value);
        }

        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double putAttribute(String name, Double value) {

        Double oldValue = null;
        Attribute existing = map.get(name);
        if (Objects.isNull(existing)) {
            addAttribute(new NumberSimpleAttributeImpl(name, value));
        } else {
            if (existing.getAttributeType() != AttributeType.SIMPLE) {

                throw new PlatformFailureException(
                        "Put numeric simple attribute '{}': Attribute exists and is not simple.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_NUM_NOT_SIMPLE,
                        name);

            } else if (((SimpleAttribute<?>) existing).getDataType() != SimpleDataType.NUMBER) {

                throw new PlatformFailureException(
                        "Put numeric simple attribute '{}': Attribute exists and is not numeric.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_NUM_NOT_NUM,
                        name);
            }

            NumberSimpleAttributeImpl cast = (NumberSimpleAttributeImpl) existing;
            oldValue = cast.getValue();
            cast.setValue(value);
        }

        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean putAttribute(String name, Boolean value) {

        Boolean oldValue = null;
        Attribute existing = map.get(name);
        if (Objects.isNull(existing)) {
            addAttribute(new BooleanSimpleAttributeImpl(name, value));
        } else {
            if (existing.getAttributeType() != AttributeType.SIMPLE) {

                throw new PlatformFailureException(
                        "Put boolean simple attribute '{}': Attribute exists and is not simple.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_BOOL_NOT_SIMPLE,
                        name);
            } else if (((SimpleAttribute<?>) existing).getDataType() != SimpleDataType.BOOLEAN) {

                throw new PlatformFailureException(
                        "Put boolean simple attribute '{}': Attribute exists and is not boolean.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_BOOL_NOT_BOOL,
                        name);
            }

            BooleanSimpleAttributeImpl cast = (BooleanSimpleAttributeImpl) existing;
            oldValue = cast.getValue();
            cast.setValue(value);
        }

        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate putAttribute(String name, LocalDate value) {

        LocalDate oldValue = null;
        Attribute existing = map.get(name);
        if (Objects.isNull(existing)) {
            addAttribute(new DateSimpleAttributeImpl(name, value));
        } else {
            if (existing.getAttributeType() != AttributeType.SIMPLE) {

                throw new PlatformFailureException(
                        "Put date simple attribute '{}': Attribute exists and is not simple.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_DATE_NOT_SIMPLE,
                        name);
            } else if (((SimpleAttribute<?>) existing).getDataType() != SimpleDataType.DATE) {

                throw new PlatformFailureException(
                        "Put date simple attribute '{}': Attribute exists and is not date.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_DATE_NOT_DATE,
                        name);
            }

            DateSimpleAttributeImpl cast = (DateSimpleAttributeImpl) existing;
            oldValue = cast.getValue();
            cast.setValue(value);
        }

        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalTime putAttribute(String name, LocalTime value) {

        LocalTime oldValue = null;
        Attribute existing = map.get(name);
        if (Objects.isNull(existing)) {
            addAttribute(new TimeSimpleAttributeImpl(name, value));
        } else {
            if (existing.getAttributeType() != AttributeType.SIMPLE) {
                throw new PlatformFailureException(
                        "Put time simple attribute '{}': Attribute exists and is not simple.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_TIME_NOT_SIMPLE,
                        name);
            } else if (((SimpleAttribute<?>) existing).getDataType() != SimpleDataType.TIME) {

                throw new PlatformFailureException(
                        "Put time simple attribute '{}': Attribute exists and is not time.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_TIME_NOT_TIME,
                        name);
            }

            TimeSimpleAttributeImpl cast = (TimeSimpleAttributeImpl) existing;
            oldValue = cast.getValue();
            cast.setValue(value);
        }

        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime putAttribute(String name, LocalDateTime value) {

        LocalDateTime oldValue = null;
        Attribute existing = map.get(name);
        if (Objects.isNull(existing)) {
            addAttribute(new TimestampSimpleAttributeImpl(name, value));
        } else {
            if (existing.getAttributeType() != AttributeType.SIMPLE) {

                throw new PlatformFailureException(
                        "Put timestamp simple attribute '{}': Attribute exists and is not simple.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_TIMESTAMP_NOT_SIMPLE,
                        name);
            } else if (((SimpleAttribute<?>) existing).getDataType() != SimpleDataType.TIMESTAMP) {

                throw new PlatformFailureException(
                        "Put timestamp simple attribute '{}': Attribute exists and is not timestamp.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_TIMESTAMP_NOT_TIMESTAMP,
                        name);
            }

            TimestampSimpleAttributeImpl cast = (TimestampSimpleAttributeImpl) existing;
            oldValue = cast.getValue();
            cast.setValue(value);
        }

        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BinaryLargeValue putAttribute(String name, BinaryLargeValue value) {

        BinaryLargeValue oldValue = null;
        Attribute existing = map.get(name);
        if (Objects.isNull(existing)) {
            addAttribute(new BlobSimpleAttributeImpl(name, value));
        } else {
            if (existing.getAttributeType() != AttributeType.SIMPLE) {

                throw new PlatformFailureException(
                        "Put BLOB simple attribute '{}': Attribute exists and is not simple.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_BLOB_NOT_SIMPLE,
                        name);
            } else if (((SimpleAttribute<?>) existing).getDataType() != SimpleDataType.BLOB) {

                throw new PlatformFailureException(
                        "Put BLOB simple attribute '{}': Attribute exists and is not BLOB.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_BLOB_NOT_BLOB,
                        name);
            }

            BlobSimpleAttributeImpl cast = (BlobSimpleAttributeImpl) existing;
            oldValue = cast.getValue();
            cast.setValue(value);
        }

        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharacterLargeValue putAttribute(String name, CharacterLargeValue value) {

        CharacterLargeValue oldValue = null;
        Attribute existing = map.get(name);
        if (Objects.isNull(existing)) {
            addAttribute(new ClobSimpleAttributeImpl(name, value));
        } else {
            if (existing.getAttributeType() != AttributeType.SIMPLE) {

                throw new PlatformFailureException(
                        "Put CLOB simple attribute '{}': Attribute exists and is not simple.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_CLOB_NOT_SIMPLE,
                        name);
            } else if (((SimpleAttribute<?>) existing).getDataType() != SimpleDataType.CLOB) {

                throw new PlatformFailureException(
                        "Put CLOB simple attribute '{}': Attribute exists and is not CLOB.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_CLOB_NOT_CLOB,
                        name);
            }

            ClobSimpleAttributeImpl cast = (ClobSimpleAttributeImpl) existing;
            oldValue = cast.getValue();
            cast.setValue(value);
        }

        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DataRecord> putAttribute(String name, Collection<DataRecord> value) {

        Collection<DataRecord> oldValue = null;
        Attribute existing = map.get(name);
        if (Objects.isNull(existing)) {
            addAttribute(new ComplexAttributeImpl(name, value));
        } else {
            if (existing.getAttributeType() != AttributeType.COMPLEX) {

                throw new PlatformFailureException(
                        "Put complex attribute '{}': Attribute exists and is not complex.",
                        CoreExceptionIds.EX_DATA_ATTRIBUTE_PUT_NOT_COMPLEX,
                        name);
            }

            ComplexAttributeImpl cast = (ComplexAttributeImpl) existing;
            oldValue = cast.removeAll();
            cast.addAll(value);
        }

        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAttribute(String name) {
        return map.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attribute removeAttribute(String name) {

        Attribute existing = map.remove(name);
        if (Objects.nonNull(existing)) {
            existing.setRecord(null);
        }

        return existing;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Attribute> removeAttributeRecursive(String path) {

        String[] tokens = StringUtils.split(path, '.');
        if (Objects.nonNull(tokens) && tokens.length > 0) {

            if (tokens.length == 1) {
                Attribute removed = removeAttribute(path);
                return removed != null ? Collections.singletonList(removed) : Collections.emptyList();
            }


            if (tokens.length > 0) {

                Attribute complex = getAttribute(tokens[0]);
                if (Objects.nonNull(complex) && complex.getAttributeType() == AttributeType.COMPLEX) {

                    ComplexAttribute complexAttr = ((ComplexAttribute) complex);
                    List<Attribute> result = new ArrayList<>(complexAttr.size());
                    for (DataRecord nested : complexAttr) {
                        result.addAll(nested.removeAttributeRecursive(StringUtils.join(tokens, '.', 1, tokens.length)));
                    }

                    return result;
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return map.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTopLevel() {
        return !hasParent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasParent() {
        return Objects.nonNull(holder) && Objects.nonNull(holder.getRecord());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrdinal() {
        return ordinal;
    }

    /**
     * Sets this record's ordinal.
     * @param ordinal the ordinal to set
     */
    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord getParentRecord() {
        return Objects.nonNull(holder) ? holder.getRecord() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ComplexAttribute getHolderAttribute() {
        return holder;
    }

    /**
     * Sets the holder attribute.
     * @param holder the holder attribute
     */
    public void setHolderAttribute(ComplexAttribute holder) {
        this.holder = holder;
    }
}
