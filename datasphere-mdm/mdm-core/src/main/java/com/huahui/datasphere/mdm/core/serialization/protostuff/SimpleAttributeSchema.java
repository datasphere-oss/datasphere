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

package com.huahui.datasphere.mdm.core.serialization.protostuff;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import com.huahui.datasphere.mdm.system.serialization.protostuff.CommonFieldValues;
import com.huahui.datasphere.mdm.system.util.ByteUtils;

import com.huahui.datasphere.mdm.core.serialization.VerifyableSimpleAttribute;
import com.huahui.datasphere.mdm.core.type.data.BinaryLargeValue;
import com.huahui.datasphere.mdm.core.type.data.CharacterLargeValue;
import com.huahui.datasphere.mdm.core.type.data.LargeValue;
import com.huahui.datasphere.mdm.core.type.data.MeasuredValue;
import com.huahui.datasphere.mdm.core.type.data.SimpleAttribute;
import com.huahui.datasphere.mdm.core.type.data.SimpleAttribute.SimpleDataType;
import com.huahui.datasphere.mdm.core.type.data.impl.AbstractAttribute;
import com.huahui.datasphere.mdm.core.type.data.impl.AbstractLargeValue;
import com.huahui.datasphere.mdm.core.type.data.impl.BinaryLargeValueImpl;
import com.huahui.datasphere.mdm.core.type.data.impl.CharacterLargeValueImpl;
import com.huahui.datasphere.mdm.core.type.lob.LargeObjectAcceptance;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;

/**
 * @author theseusyang
 * Simple attributes.
 */
public class SimpleAttributeSchema implements Schema<SimpleAttribute<?>> {
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
    @SuppressWarnings("rawtypes")
    @Override
    public boolean isInitialized(SimpleAttribute<?> message) {

        if (message instanceof VerifyableSimpleAttribute) {
            return ((VerifyableSimpleAttribute) message).isValid();
        }

        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleAttribute<?> newMessage() {
        // Should never be called.
        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageName() {
        return SimpleAttribute.class.getSimpleName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageFullName() {
        return SimpleAttribute.class.getName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? super SimpleAttribute<?>> typeClass() {
        return SimpleAttribute.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeFrom(Input input, SimpleAttribute<?> message) throws IOException {

        MeasuredValue mv = null;
        AbstractLargeValue<?> lob = null;
        int field;
        while ((field = input.readFieldNumber(this)) != CommonFieldValues.END_OF_RECORD) {

            switch (field) {
            case CoreFieldValues.FIELD_NAME_VAL:
                ((AbstractAttribute) message).setName(input.readString());
                break;
            case CoreFieldValues.STRING_VALUE_VAL:
                message.castValue(input.readString());
                break;
            case CoreFieldValues.INTEGER_VALUE_VAL:
                message.castValue(input.readInt64());
                break;
            case CoreFieldValues.NUMBER_VALUE_VAL:
                message.castValue(input.readDouble());
                break;
            case CoreFieldValues.BOOLEAN_VALUE_VAL:
                message.castValue(input.readBool());
                break;
            case CoreFieldValues.DATE_VALUE_VAL:
                message.castValue(ByteUtils.unpackLocalDate(input.readByteArray()));
                break;
            case CoreFieldValues.TIME_VALUE_VAL:
                message.castValue(ByteUtils.unpackLocalTime(input.readByteArray()));
                break;
            case CoreFieldValues.TIMESTAMP_VALUE_VAL:
                message.castValue(ByteUtils.unpackLocalDateTime(input.readByteArray()));
                break;
            // LOB
            case CoreFieldValues.LARGE_VALUE_ID_VAL:
                lob = ensureLargeValue(message);
                lob.setId(input.readString());
                break;
            case CoreFieldValues.LARGE_VALUE_FILENAME_VAL:
                lob = ensureLargeValue(message);
                lob.setFileName(input.readString());
                break;
            case CoreFieldValues.LARGE_VALUE_MIME_TYPE_VAL:
                lob = ensureLargeValue(message);
                lob.setMimeType(input.readString());
                break;
            case CoreFieldValues.LARGE_VALUE_SIZE_VAL:
                lob = ensureLargeValue(message);
                lob.setSize(input.readInt64());
                break;
            case CoreFieldValues.LARGE_VALUE_ACCEPTANCE_VAL:
                lob = ensureLargeValue(message);
                lob.setAcceptance(LargeObjectAcceptance.valueOf(input.readString()));
                break;
            // Measured value
            case CoreFieldValues.MEASURED_UNIT_ID_VAL:
                mv = ensureMeasuredValue(message);
                mv.setUnitId(input.readString());
                break;
            case CoreFieldValues.MEASURED_CATEGORY_ID_VAL:
                mv = ensureMeasuredValue(message);
                mv.setCategoryId(input.readString());
                break;
            case CoreFieldValues.MEASURED_INITIAL_VAL:
                mv = ensureMeasuredValue(message);
                mv.setInitialValue(input.readDouble());
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
    public void writeTo(Output output, SimpleAttribute<?> message) throws IOException {

        output.writeString(CoreSchemaFields.FIELD_NAME.getValue(), message.getName(), false);

        if (Objects.isNull(message.getValue())) {
            return;
        }

        switch (message.getDataType()) {
        case STRING:
            String stringVal = message.castValue();
            output.writeString(CoreSchemaFields.STRING_VALUE.getValue(), stringVal, false);
            break;
        case BOOLEAN:
            Boolean booleanVal = message.castValue();
            output.writeBool(CoreSchemaFields.BOOLEAN_VALUE.getValue(), booleanVal, false);
            break;
        case INTEGER:
            Long integerVal = message.castValue();
            output.writeInt64(CoreSchemaFields.INTEGER_VALUE.getValue(), integerVal, false);
            break;
        case NUMBER:
            Double numberVal = message.castValue();
            output.writeDouble(CoreSchemaFields.NUMBER_VALUE.getValue(), numberVal, false);
            break;
        case DATE:
            LocalDate dateVal = message.castValue();
            output.writeByteArray(CoreSchemaFields.DATE_VALUE.getValue(), ByteUtils.packLocalDate(dateVal), false);
            break;
        case TIME:
            LocalTime timeVal = message.castValue();
            output.writeByteArray(CoreSchemaFields.TIME_VALUE.getValue(), ByteUtils.packLocalTime(timeVal), false);
            break;
        case TIMESTAMP:
            LocalDateTime timestampVal = message.castValue();
            output.writeByteArray(CoreSchemaFields.TIMESTAMP_VALUE.getValue(), ByteUtils.packLocalDateTime(timestampVal), false);
            break;
        case MEASURED:
            MeasuredValue measureValue = message.castValue();
            writeMeasuredAttributeTo(output, measureValue);
            break;
        case BLOB:
            BinaryLargeValue binaryVal = message.castValue();
            writeLargeValueTo(output, binaryVal);
            break;
        case CLOB:
            CharacterLargeValue characterVal = message.castValue();
            writeLargeValueTo(output, characterVal);
            break;
        default:
            break;
        }

    }

    /**
     * Ensures existence of the LOB object for an attribute.
     * @param attribute the attribute
     * @return the value
     */
    private AbstractLargeValue<?> ensureLargeValue(SimpleAttribute<?> attribute) {

        AbstractLargeValue<?> lob = attribute.castValue();
        if (lob == null) {
            lob = attribute.getDataType() == SimpleDataType.BLOB ? new BinaryLargeValueImpl() : new CharacterLargeValueImpl();
            attribute.castValue(lob);
        }

        return lob;
    }
    /**
     * Ensures existence of the MV object for an attribute.
     * @param attribute the attribute
     * @return the value
     */
    private MeasuredValue ensureMeasuredValue(SimpleAttribute<?> attribute) {

        MeasuredValue mv = attribute.castValue();
        if (mv == null) {
            mv = MeasuredValue.of(null, null, null);
            attribute.castValue(mv);
        }

        return mv;
    }
    /**
     * Writes large value to stream.
     * @param output the output
     * @param largeValue large value
     */
    private void writeLargeValueTo(Output output, LargeValue largeValue) throws IOException {

        String bvId = largeValue.getId();
        String fileName = largeValue.getFileName();
        String mimeType = largeValue.getMimeType();
        long size = largeValue.getSize();
        LargeObjectAcceptance acceptance = largeValue.getAcceptance();

        if (Objects.nonNull(bvId)) {
            output.writeString(CoreSchemaFields.LARGE_VALUE_ID.getValue(), bvId, false);
        }

        if (Objects.nonNull(fileName)) {
            output.writeString(CoreSchemaFields.LARGE_VALUE_FILENAME.getValue(), fileName, false);
        }

        if (Objects.nonNull(mimeType)) {
            output.writeString(CoreSchemaFields.LARGE_VALUE_MIME_TYPE.getValue(), mimeType, false);
        }

        if (Objects.nonNull(acceptance)) {
            output.writeString(CoreSchemaFields.LARGE_VALUE_ACCEPTANCE.getValue(), acceptance.name(), false);
        }

        output.writeInt64(CoreSchemaFields.LARGE_VALUE_SIZE.getValue(), size, false);
    }
    /**
     * Writes measured attribute. Base value is not written to dest!
     * @param output the output
     * @param measured the attribute
     * @throws IOException
     */
    private void writeMeasuredAttributeTo(Output output, MeasuredValue measured) throws IOException {

        String unitId = measured.getUnitId();
        String categoryId = measured.getCategoryId();
        Double initialValue = measured.getInitialValue();

        if (Objects.nonNull(unitId)) {
            output.writeString(CoreSchemaFields.MEASURED_UNIT_ID.getValue(), unitId, false);
        }

        if (Objects.nonNull(categoryId)) {
            output.writeString(CoreSchemaFields.MEASURED_CATEGORY_ID.getValue(), categoryId, false);
        }

        if (initialValue != null){
            output.writeDouble(CoreSchemaFields.MEASURED_INITIAL.getValue(), initialValue, false);
        }
    }
}
