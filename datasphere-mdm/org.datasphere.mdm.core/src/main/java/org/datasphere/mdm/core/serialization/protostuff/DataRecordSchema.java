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

package org.datasphere.mdm.core.serialization.protostuff;

import java.io.IOException;
import java.util.Objects;

import org.datasphere.mdm.core.serialization.SerializedBlobSimpleAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedBooleanSimpleAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedClobSimpleAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedComplexAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedDateArrayAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedDateSimpleAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedIntegerArrayAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedIntegerCodeAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedIntegerSimpleAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedMeasuredSimpleAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedNumberArrayAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedNumberSimpleAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedStringArrayAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedStringCodeAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedStringSimpleAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedTimeArrayAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedTimeSimpleAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedTimestampArrayAttributeImpl;
import org.datasphere.mdm.core.serialization.SerializedTimestampSimpleAttributeImpl;
import org.datasphere.mdm.core.type.data.ArrayAttribute;
import org.datasphere.mdm.core.type.data.Attribute;
import org.datasphere.mdm.core.type.data.CodeAttribute;
import org.datasphere.mdm.core.type.data.ComplexAttribute;
import org.datasphere.mdm.core.type.data.DataRecord;
import org.datasphere.mdm.core.type.data.SimpleAttribute;
import org.datasphere.mdm.core.type.data.impl.SerializableDataRecord;
import org.datasphere.mdm.system.serialization.protostuff.CommonFieldValues;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;

/**
 * @author Mikhail Mikhailov
 * Date record schema.
 */
public class DataRecordSchema implements Schema<DataRecord> {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldName(int number) {
        return CoreSchemaFields.intToString(number);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getFieldNumber(String name) {
        return CoreSchemaFields.stringToInt(name);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInitialized(DataRecord message) {
        return message != null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord newMessage() {
        return new SerializableDataRecord();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageName() {
        return DataRecord.class.getSimpleName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageFullName() {
        return DataRecord.class.getName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<DataRecord> typeClass() {
        return DataRecord.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeFrom(Input input, DataRecord message) throws IOException {

        int field;
        while ((field = input.readFieldNumber(this)) != CommonFieldValues.END_OF_RECORD) {

            switch (field) {
            case CoreFieldValues.STRING_SIMPLE_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedStringSimpleAttributeImpl(), CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.INTEGER_SIMPLE_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedIntegerSimpleAttributeImpl(), CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.NUMBER_SIMPLE_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedNumberSimpleAttributeImpl(), CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.BOOLEAN_SIMPLE_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedBooleanSimpleAttributeImpl(), CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.BLOB_SIMPLE_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedBlobSimpleAttributeImpl(), CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.CLOB_SIMPLE_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedClobSimpleAttributeImpl(), CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.DATE_SIMPLE_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedDateSimpleAttributeImpl(), CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.TIME_SIMPLE_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedTimeSimpleAttributeImpl(), CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.TIMESTAMP_SIMPLE_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedTimestampSimpleAttributeImpl(), CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.MEASURED_SIMPLE_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedMeasuredSimpleAttributeImpl(), CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.STRING_CODE_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedStringCodeAttributeImpl(), CoreSchemas.CODE_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.INTEGER_CODE_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedIntegerCodeAttributeImpl(), CoreSchemas.CODE_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.STRING_ARRAY_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedStringArrayAttributeImpl(), CoreSchemas.ARRAY_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.INTEGER_ARRAY_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedIntegerArrayAttributeImpl(), CoreSchemas.ARRAY_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.NUMBER_ARRAY_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedNumberArrayAttributeImpl(), CoreSchemas.ARRAY_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.DATE_ARRAY_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedDateArrayAttributeImpl(), CoreSchemas.ARRAY_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.TIME_ARRAY_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedTimeArrayAttributeImpl(), CoreSchemas.ARRAY_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.TIMESTAMP_ARRAY_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedTimestampArrayAttributeImpl(), CoreSchemas.ARRAY_ATTRIBUTE_SCHEMA));
                break;
            case CoreFieldValues.COMPLEX_ATTRIBUTE_VAL:
                message.addAttribute(input.mergeObject(new SerializedComplexAttributeImpl(), CoreSchemas.COMPLEX_ATTRIBUTE_SCHEMA));
                break;
            default:
                break;
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeTo(Output output, DataRecord message) throws IOException {

        for (Attribute attribute : message.getAttributeValues()) {

            switch (attribute.getAttributeType()) {
            case SIMPLE:
                writeSimpleAttributeTo(output, (SimpleAttribute<?>) attribute);
                break;
            case CODE:
                writeCodeAttributeTo(output, (CodeAttribute<?>) attribute);
                break;
            case ARRAY:
                writeArrayAttributeTo(output, (ArrayAttribute<?>) attribute);
                break;
            case COMPLEX:
                writeComplexAttributeTo(output, (ComplexAttribute) attribute);
                break;
            }
        }

    }
    /**
     * Writes simple attribute.
     * @param output the output
     * @param attribute the attribute
     * @throws IOException
     */
    private void writeSimpleAttributeTo(Output output, SimpleAttribute<?> attribute) throws IOException {

        switch (attribute.getDataType()) {
        case BLOB:
            output.writeObject(CoreSchemaFields.BLOB_SIMPLE_ATTRIBUTE.getValue(), attribute, CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA, false);
            break;
        case CLOB:
            output.writeObject(CoreSchemaFields.CLOB_SIMPLE_ATTRIBUTE.getValue(), attribute, CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA, false);
            break;
        case BOOLEAN:
            output.writeObject(CoreSchemaFields.BOOLEAN_SIMPLE_ATTRIBUTE.getValue(), attribute, CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA, false);
            break;
        case INTEGER:
            output.writeObject(CoreSchemaFields.INTEGER_SIMPLE_ATTRIBUTE.getValue(), attribute, CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA, false);
            break;
        case MEASURED:
            output.writeObject(CoreSchemaFields.MEASURED_SIMPLE_ATTRIBUTE.getValue(), attribute, CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA, false);
            break;
        case NUMBER:
            output.writeObject(CoreSchemaFields.NUMBER_SIMPLE_ATTRIBUTE.getValue(), attribute, CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA, false);
            break;
        case STRING:
            output.writeObject(CoreSchemaFields.STRING_SIMPLE_ATTRIBUTE.getValue(), attribute, CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA, false);
            break;
        case DATE:
            output.writeObject(CoreSchemaFields.DATE_SIMPLE_ATTRIBUTE.getValue(), attribute, CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA, false);
            break;
        case TIME:
            output.writeObject(CoreSchemaFields.TIME_SIMPLE_ATTRIBUTE.getValue(), attribute, CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA, false);
            break;
        case TIMESTAMP:
            output.writeObject(CoreSchemaFields.TIMESTAMP_SIMPLE_ATTRIBUTE.getValue(), attribute, CoreSchemas.SIMPLE_ATTRIBUTE_SCHEMA, false);
            break;
        default:
            break;
        }
    }
    /**
     * Writes code attribute.
     * @param output the output
     * @param attribute the attribute
     * @throws IOException
     */
    private void writeCodeAttributeTo(Output output, CodeAttribute<?> attribute) throws IOException {

        if (Objects.isNull(attribute.getValue())) {
            return;
        }

        switch (attribute.getDataType()) {
        case INTEGER:
            output.writeObject(CoreSchemaFields.INTEGER_CODE_ATTRIBUTE.getValue(), attribute, CoreSchemas.CODE_ATTRIBUTE_SCHEMA, false);
            break;
        case STRING:
            output.writeObject(CoreSchemaFields.STRING_CODE_ATTRIBUTE.getValue(), attribute, CoreSchemas.CODE_ATTRIBUTE_SCHEMA, false);
            break;
        default:
            break;
        }
    }
    /**
     * Writes array attribute.
     * @param output the output
     * @param attribute the attribute
     * @throws IOException
     */
    private void writeArrayAttributeTo(Output output, ArrayAttribute<?> attribute) throws IOException {

        switch (attribute.getDataType()) {
        case DATE:
            output.writeObject(CoreSchemaFields.DATE_ARRAY_ATTRIBUTE.getValue(), attribute, CoreSchemas.ARRAY_ATTRIBUTE_SCHEMA, false);
            break;
        case INTEGER:
            output.writeObject(CoreSchemaFields.INTEGER_ARRAY_ATTRIBUTE.getValue(), attribute, CoreSchemas.ARRAY_ATTRIBUTE_SCHEMA, false);
            break;
        case NUMBER:
            output.writeObject(CoreSchemaFields.NUMBER_ARRAY_ATTRIBUTE.getValue(), attribute, CoreSchemas.ARRAY_ATTRIBUTE_SCHEMA, false);
            break;
        case STRING:
            output.writeObject(CoreSchemaFields.STRING_ARRAY_ATTRIBUTE.getValue(), attribute, CoreSchemas.ARRAY_ATTRIBUTE_SCHEMA, false);
            break;
        case TIME:
            output.writeObject(CoreSchemaFields.TIME_ARRAY_ATTRIBUTE.getValue(), attribute, CoreSchemas.ARRAY_ATTRIBUTE_SCHEMA, false);
            break;
        case TIMESTAMP:
            output.writeObject(CoreSchemaFields.TIMESTAMP_ARRAY_ATTRIBUTE.getValue(), attribute, CoreSchemas.ARRAY_ATTRIBUTE_SCHEMA, false);
            break;
        default:
            break;
        }
    }
    /**
     * Writes complex attribute.
     * @param output the output
     * @param attribute the attribute
     * @throws IOException
     */
    private void writeComplexAttributeTo(Output output, ComplexAttribute attribute) throws IOException {
        output.writeObject(CoreSchemaFields.COMPLEX_ATTRIBUTE.getValue(), attribute, CoreSchemas.COMPLEX_ATTRIBUTE_SCHEMA, false);
    }
}
