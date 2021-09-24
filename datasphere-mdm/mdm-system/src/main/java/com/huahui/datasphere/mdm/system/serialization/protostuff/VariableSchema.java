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

package com.huahui.datasphere.mdm.system.serialization.protostuff;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import com.huahui.datasphere.mdm.system.serialization.SerializedVariable;
import com.huahui.datasphere.mdm.system.type.variables.Variable;
import com.huahui.datasphere.mdm.system.util.ByteUtils;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;

/**
 * @author theseusyang
 * Variables.
 */
public class VariableSchema implements Schema<Variable<?>> {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldName(int number) {
        return SystemSchemaFields.intToString(number);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getFieldNumber(String name) {
        return SystemSchemaFields.stringToInt(name);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInitialized(Variable<?> message) {
        return message != null && message.getName() != null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Variable<?> newMessage() {
        // Should never be called.
        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageName() {
        return Variable.class.getSimpleName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageFullName() {
        return Variable.class.getName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? super Variable<?>> typeClass() {
        return Variable.class;
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void mergeFrom(Input input, Variable<?> message) throws IOException {

        int field;
        while ((field = input.readFieldNumber(this)) != CommonFieldValues.END_OF_RECORD) {

            switch (field) {
            case SystemFieldValues.FIELD_NAME_VAL:
                ((SerializedVariable) message).setName(input.readString());
                break;
            case SystemFieldValues.STRING_VALUE_VAL:
                ((Variable<String>) message).setValue(input.readString());
                break;
            case SystemFieldValues.INTEGER_VALUE_VAL:
                ((Variable<Integer>) message).setValue(input.readInt32());
                break;
            case SystemFieldValues.LONG_VALUE_VAL:
                ((Variable<Long>) message).setValue(input.readInt64());
                break;
            case SystemFieldValues.FLOAT_VALUE_VAL:
                ((Variable<Float>) message).setValue(input.readFloat());
                break;
            case SystemFieldValues.DOUBLE_VALUE_VAL:
                ((Variable<Double>) message).setValue(input.readDouble());
                break;
            case SystemFieldValues.BOOLEAN_VALUE_VAL:
                ((Variable<Boolean>) message).setValue(input.readBool());
                break;
            case SystemFieldValues.DATE_VALUE_VAL:
                ((Variable<LocalDate>) message).setValue(ByteUtils.unpackLocalDate(input.readByteArray()));
                break;
            case SystemFieldValues.TIME_VALUE_VAL:
                ((Variable<LocalTime>) message).setValue(ByteUtils.unpackLocalTime(input.readByteArray()));
                break;
            case SystemFieldValues.TIMESTAMP_VALUE_VAL:
                ((Variable<LocalDateTime>) message).setValue(ByteUtils.unpackLocalDateTime(input.readByteArray()));
                break;
            case SystemFieldValues.INSTANT_VALUE_VAL:
                ((Variable<Instant>) message).setValue(ByteUtils.unpackInstant(input.readByteArray()));
                break;
            default:
                break;
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void writeTo(Output output, Variable<?> message) throws IOException {

        output.writeString(SystemSchemaFields.FIELD_NAME.getValue(), message.getName(), false);

        if (Objects.isNull(message.getValue())) {
            return;
        }

        switch (message.getType()) {
        case STRING:
            String stringVal = ((Variable<String>) message).getValue();
            output.writeString(SystemSchemaFields.STRING_VALUE.getValue(), stringVal, false);
            break;
        case BOOLEAN:
            Boolean booleanVal = ((Variable<Boolean>) message).getValue();
            output.writeBool(SystemSchemaFields.BOOLEAN_VALUE.getValue(), booleanVal, false);
            break;
        case INTEGER:
            Integer integerVal = ((Variable<Integer>) message).getValue();
            output.writeInt32(SystemSchemaFields.INTEGER_VALUE.getValue(), integerVal, false);
            break;
        case LONG:
            Long longVal = ((Variable<Long>) message).getValue();
            output.writeInt64(SystemSchemaFields.LONG_VALUE.getValue(), longVal, false);
            break;
        case FLOAT:
            Float floatVal = ((Variable<Float>) message).getValue();
            output.writeFloat(SystemSchemaFields.FLOAT_VALUE.getValue(), floatVal, false);
            break;
        case DOUBLE:
            Double doubleVal = ((Variable<Double>) message).getValue();
            output.writeDouble(SystemSchemaFields.DOUBLE_VALUE.getValue(), doubleVal, false);
            break;
        case DATE:
            LocalDate dateVal = ((Variable<LocalDate>) message).getValue();
            output.writeByteArray(SystemSchemaFields.DATE_VALUE.getValue(), ByteUtils.packLocalDate(dateVal), false);
            break;
        case TIME:
            LocalTime timeVal = ((Variable<LocalTime>) message).getValue();
            output.writeByteArray(SystemSchemaFields.TIME_VALUE.getValue(), ByteUtils.packLocalTime(timeVal), false);
            break;
        case TIMESTAMP:
            LocalDateTime timestampVal = ((Variable<LocalDateTime>) message).getValue();
            output.writeByteArray(SystemSchemaFields.TIMESTAMP_VALUE.getValue(), ByteUtils.packLocalDateTime(timestampVal), false);
            break;
        case INSTANT:
            Instant instantVal = ((Variable<Instant>) message).getValue();
            output.writeByteArray(SystemSchemaFields.INSTANT_VALUE.getValue(), ByteUtils.packInstant(instantVal), false);
            break;
        default:
            break;
        }
    }
}
