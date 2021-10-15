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

package org.datasphere.mdm.core.type.data.extended;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import org.datasphere.mdm.core.type.data.ArrayAttribute;
import org.datasphere.mdm.core.type.data.Attribute;
import org.datasphere.mdm.core.type.data.CodeAttribute;
import org.datasphere.mdm.core.type.data.CodeLinkValue;
import org.datasphere.mdm.core.type.data.ComplexAttribute;
import org.datasphere.mdm.core.type.data.DataRecord;
import org.datasphere.mdm.core.type.data.SimpleAttribute;
import org.datasphere.mdm.core.type.data.impl.ComplexAttributeImpl;
import org.datasphere.mdm.core.type.data.impl.DateArrayAttributeImpl;
import org.datasphere.mdm.core.type.data.impl.DateArrayValue;
import org.datasphere.mdm.core.type.data.impl.IntegerArrayAttributeImpl;
import org.datasphere.mdm.core.type.data.impl.IntegerArrayValue;
import org.datasphere.mdm.core.type.data.impl.IntegerCodeAttributeImpl;
import org.datasphere.mdm.core.type.data.impl.NumberArrayAttributeImpl;
import org.datasphere.mdm.core.type.data.impl.NumberArrayValue;
import org.datasphere.mdm.core.type.data.impl.SerializableDataRecord;
import org.datasphere.mdm.core.type.data.impl.StringArrayAttributeImpl;
import org.datasphere.mdm.core.type.data.impl.StringArrayValue;
import org.datasphere.mdm.core.type.data.impl.StringCodeAttributeImpl;
import org.datasphere.mdm.core.type.data.impl.StringSimpleAttributeImpl;
import org.datasphere.mdm.core.type.data.impl.TimeArrayAttributeImpl;
import org.datasphere.mdm.core.type.data.impl.TimeArrayValue;
import org.datasphere.mdm.core.type.data.impl.TimestampArrayAttributeImpl;
import org.datasphere.mdm.core.type.data.impl.TimestampArrayValue;

/**
 * @author Dmitrii Kopin
 * Data view - simple and complex attributes additional information builder
 */
public class ExtendedSerializableDataRecordBuilder{


    private ExtendedSerializableDataRecordBuilder(){

    }
    /**
     * Copies data record.
     * @param other the attribute to copy
     * @param  sourceSystem source system
     * @param  externalId external identifier
     * @return cloned data record
     */
    public static SerializableDataRecord of(DataRecord other, String sourceSystem, String externalId) {

        if (Objects.isNull(other)) {
            return null;
        }

        Collection<Attribute> attrs = other.getAttributeValues();
        SerializableDataRecord record = new SerializableDataRecord(attrs.size());
        for (Attribute attr : attrs) {
            switch (attr.getAttributeType()) {
                case SIMPLE:
                    record.addAttribute(of((SimpleAttribute<?>) attr, sourceSystem, externalId));
                    break;
                case ARRAY:
                    record.addAttribute(of((ArrayAttribute<?>) attr, sourceSystem, externalId));
                    break;
                case CODE:
                    record.addAttribute(of((CodeAttribute<?>) attr, sourceSystem, externalId));
                    break;
                case COMPLEX:
                    record.addAttribute(of((ComplexAttribute) attr, sourceSystem, externalId));
                    break;
            }
        }

        return record;
    }

    /**
     * Copies simple attribute.
     * @param attr the attribute to copy
     * @param  sourceSystem source system
     * @param  externalId external identifier
     * @return cloned attribute
     */
    public static ComplexAttribute of(ComplexAttribute attr, String sourceSystem, String externalId) {

        if (Objects.isNull(attr)) {
            return null;
        }

        ComplexAttributeImpl result = new ExtendedComplexAttributeImpl(attr.getName(), sourceSystem, externalId);
        for (DataRecord dr : attr) {
            result.add(of(dr, sourceSystem, externalId));
        }

        return result;
    }

    /**
     * Copies simple attribute.
     * @param attr the attribute to copy
     * @param  sourceSystem source system
     * @param  externalId external identifier
     * @return cloned attribute
     */
    public static SimpleAttribute<?> of(SimpleAttribute<?> attr, String sourceSystem, String externalId) {

        if (Objects.isNull(attr)) {
            return null;
        }

        SimpleAttribute<?> result;
        switch (attr.getDataType()) {
            case BLOB:
                result = new ExtendedBlobSimpleAttributeImpl(attr.getName(), attr.castValue(), sourceSystem, externalId);
                break;
            case BOOLEAN:
                result = new ExtendedBooleanSimpleAttributeImpl(attr.getName(), attr.castValue(), sourceSystem, externalId);
                break;
            case CLOB:
                result = new ExtendedClobSimpleAttributeImpl(attr.getName(), attr.castValue(), sourceSystem, externalId);
                break;
            case DATE:
                result = new ExtendedDateSimpleAttributeImpl(attr.getName(), attr.castValue(), sourceSystem, externalId);
                break;
            case INTEGER:
                result = new ExtendedIntegerSimpleAttributeImpl(attr.getName(), attr.castValue(), sourceSystem, externalId);
                break;
            case NUMBER:
                result = new ExtendedNumberSimpleAttributeImpl(attr.getName(), attr.castValue(), sourceSystem, externalId);
                break;
            case MEASURED:
                result = new ExtendedMeasuredSimpleAttributeImpl(attr.getName(), attr.castValue(), sourceSystem, externalId);
                break;
            case STRING:
                result = new ExtendedStringSimpleAttributeImpl(attr.getName(), attr.castValue(), sourceSystem, externalId);
                ((StringSimpleAttributeImpl) result).setLinkEtalonId(((CodeLinkValue) attr).getLinkEtalonId());
                break;
            case DICTIONARY:
                result = new ExtendedDictionarySimpleAttributeImpl(attr.getName(), attr.castValue(), sourceSystem, externalId);
                ((StringSimpleAttributeImpl) result).setLinkEtalonId(((CodeLinkValue) attr).getLinkEtalonId());
                break;
            case TIME:
                result = new ExtendedTimeSimpleAttributeImpl(attr.getName(), attr.castValue(), sourceSystem, externalId);
                break;
            case TIMESTAMP:
                result = new ExtendedTimestampSimpleAttributeImpl(attr.getName(), attr.castValue(), sourceSystem, externalId);
                break;
            case ENUM:
                result = new ExtendedEnumSimpleAttributeImpl(attr.getName(), attr.castValue(), sourceSystem, externalId);
                break;
            default:
                return null;
        }

        result.setDisplayValue(attr.getDisplayValue());
        return result;
    }

    /**
     * Copies array attribute.
     * @param attr the attribute to copy
     * @param  sourceSystem source system
     * @param  externalId external identifier
     * @return cloned attribute
     */
    public static ArrayAttribute<?> of(ArrayAttribute<?> attr, String sourceSystem, String externalId) {

        if (Objects.isNull(attr)) {
            return null;
        }

        ArrayAttribute<?> result;
        switch (attr.getDataType()) {
            case DATE:
                result = new ExtendedDateArrayAttributeImpl(attr.getName(), sourceSystem , externalId);
                ((DateArrayAttributeImpl) result).setValue(attr.isEmpty()
                        ? null
                        : attr.getValue().stream().map(v -> new DateArrayValue((LocalDate) v.getValue())).collect(Collectors.toList()));
                break;
            case INTEGER:
                result = new ExtendedIntegerArrayAttributeImpl(attr.getName(), sourceSystem , externalId);
                ((IntegerArrayAttributeImpl) result).setValue(attr.isEmpty()
                        ? null
                        : attr.getValue().stream().map(v -> new IntegerArrayValue((Long) v.getValue())).collect(Collectors.toList()));
                break;
            case NUMBER:
                result = new ExtendedNumberArrayAttributeImpl(attr.getName(), sourceSystem , externalId);
                ((NumberArrayAttributeImpl) result).setValue(attr.isEmpty()
                        ? null
                        : attr.getValue().stream().map(v -> new NumberArrayValue((Double) v.getValue())).collect(Collectors.toList()));
                break;
            case STRING:
                result = new ExtendedStringArrayAttributeImpl(attr.getName(), sourceSystem , externalId);
                ((StringArrayAttributeImpl) result).setValue(attr.isEmpty()
                        ? null
                        : attr.getValue().stream().map(v -> new StringArrayValue((String) v.getValue())).collect(Collectors.toList()));
                break;
            case TIME:
                result = new ExtendedTimeArrayAttributeImpl(attr.getName(), sourceSystem , externalId);
                ((TimeArrayAttributeImpl) result).setValue(attr.isEmpty()
                        ? null
                        : attr.getValue().stream().map(v -> new TimeArrayValue((LocalTime) v.getValue())).collect(Collectors.toList()));
                break;
            case TIMESTAMP:
                result = new ExtendedTimestampArrayAttributeImpl(attr.getName(), sourceSystem , externalId);
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
     * @param  sourceSystem source system
     * @param  externalId external identifier
     * @return cloned attribute
     */
    public static CodeAttribute<?> of (CodeAttribute<?> attr, String sourceSystem, String externalId) {

        if (Objects.isNull(attr)) {
            return null;
        }

        CodeAttribute<?> result;
        switch (attr.getDataType()) {
            case INTEGER:
                result = new ExtendedIntegerCodeAttributeImpl(attr.getName(), attr.castValue(), sourceSystem, externalId);
                ((IntegerCodeAttributeImpl) result).setSupplementary(attr.hasSupplementary()
                        ? attr.getSupplementary().stream().map(v -> (Long) v).collect(Collectors.toList())
                        : null);
                break;
            case STRING:
                result = new ExtendedStringCodeAttributeImpl(attr.getName(), attr.castValue(), sourceSystem, externalId);
                ((StringCodeAttributeImpl) result).setSupplementary(attr.hasSupplementary()
                        ? attr.getSupplementary().stream().map(v -> (String) v).collect(Collectors.toList())
                        : null);
                break;
            default:
                return null;
        }

        return result;
    }
}
