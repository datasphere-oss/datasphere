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
import java.util.ArrayList;
import java.util.List;

import com.huahui.datasphere.mdm.system.serialization.protostuff.CommonFieldValues;
import com.huahui.datasphere.mdm.system.serialization.protostuff.OutpuMappingSupport;
import com.huahui.datasphere.mdm.system.util.ByteUtils;

import com.huahui.datasphere.mdm.core.serialization.VerifyableArrayAttribute;
import com.huahui.datasphere.mdm.core.type.data.ArrayAttribute;
import com.huahui.datasphere.mdm.core.type.data.impl.AbstractAttribute;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;

/**
 * @author theseusyang
 * Array attributes.
 */
public class ArrayAttributeSchema implements Schema<ArrayAttribute<?>>, OutpuMappingSupport {
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
    public boolean isInitialized(ArrayAttribute<?> message) {

        if (message instanceof VerifyableArrayAttribute<?>) {
            return ((VerifyableArrayAttribute<?>) message).isValid();
        }

        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayAttribute<?> newMessage() {
        // Should not be called.
        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageName() {
        return ArrayAttribute.class.getSimpleName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageFullName() {
        return ArrayAttribute.class.getName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? super ArrayAttribute<?>> typeClass() {
        return ArrayAttribute.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeFrom(Input input, ArrayAttribute<?> message) throws IOException {

        List<Object> objects = new ArrayList<>();
        int field;
        while ((field = input.readFieldNumber(this)) != CommonFieldValues.END_OF_RECORD) {

            switch (field) {
            case CoreFieldValues.FIELD_NAME_VAL:
                ((AbstractAttribute) message).setName(input.readString());
                break;
            case CoreFieldValues.STRING_ARRAY_VALUE_VAL:
                objects.add(input.readString());
                break;
            case CoreFieldValues.INTEGER_ARRAY_VALUE_VAL:
                objects.add(input.readInt64());
                break;
            case CoreFieldValues.NUMBER_ARRAY_VALUE_VAL:
                objects.add(input.readDouble());
                break;
            case CoreFieldValues.DATE_ARRAY_VALUE_VAL:
                objects.add(ByteUtils.unpackLocalDate(input.readByteArray()));
                break;
            case CoreFieldValues.TIME_ARRAY_VALUE_VAL:
                objects.add(ByteUtils.unpackLocalTime(input.readByteArray()));
                break;
            case CoreFieldValues.TIMESTAMP_ARRAY_VALUE_VAL:
                objects.add(ByteUtils.unpackLocalDateTime(input.readByteArray()));
                break;
            default:
                break;
            }
        }

        message.castValue(objects);
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void writeTo(Output output, ArrayAttribute<?> message) throws IOException {

        output.writeString(CoreSchemaFields.FIELD_NAME.getValue(), message.getName(), false);
        if (message.isEmpty()) {
            return;
        }

        switch (message.getDataType()) {
        case STRING:
            Object[] stringVal = ((ArrayAttribute<String>) message).toArray();
            writeArrayTo(output, stringVal, (o, i) -> o.writeString(CoreSchemaFields.STRING_ARRAY_VALUE.getValue(), i.toString(), false));
            break;
        case INTEGER:
            Object[] integerVal = ((ArrayAttribute<Long>) message).toArray();
            writeArrayTo(output, integerVal, (o, i) -> o.writeInt64(CoreSchemaFields.INTEGER_ARRAY_VALUE.getValue(), (Long) i, false));
            break;
        case NUMBER:
            Object[] numberVal = ((ArrayAttribute<Double>) message).toArray();
            writeArrayTo(output, numberVal, (o, i) -> o.writeDouble(CoreSchemaFields.NUMBER_ARRAY_VALUE.getValue(), (Double) i, false));
            break;
        case DATE:
            Object[] dateVal = ((ArrayAttribute<LocalDate>) message).toArray();
            writeArrayTo(output, dateVal, (o, i) -> o.writeByteArray(CoreSchemaFields.DATE_ARRAY_VALUE.getValue(), ByteUtils.packLocalDate((LocalDate) i), false));
            break;
        case TIME:
            Object[] timeVal = ((ArrayAttribute<LocalTime>) message).toArray();
            writeArrayTo(output, timeVal, (o, i) -> o.writeByteArray(CoreSchemaFields.TIME_ARRAY_VALUE.getValue(), ByteUtils.packLocalTime((LocalTime) i), false));
            break;
        case TIMESTAMP:
            Object[] timestampVal = ((ArrayAttribute<LocalDateTime>) message).toArray();
            writeArrayTo(output, timestampVal, (o, i) -> o.writeByteArray(CoreSchemaFields.TIMESTAMP_ARRAY_VALUE.getValue(), ByteUtils.packLocalDateTime((LocalDateTime) i), false));
            break;
        default:
            break;
        }
    }
}
