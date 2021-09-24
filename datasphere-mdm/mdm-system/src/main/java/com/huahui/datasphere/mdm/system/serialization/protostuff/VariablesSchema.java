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

import com.huahui.datasphere.mdm.system.serialization.SerializedBooleanVariable;
import com.huahui.datasphere.mdm.system.serialization.SerializedDateVariable;
import com.huahui.datasphere.mdm.system.serialization.SerializedDoubleVariable;
import com.huahui.datasphere.mdm.system.serialization.SerializedFloatVariable;
import com.huahui.datasphere.mdm.system.serialization.SerializedInstantVariable;
import com.huahui.datasphere.mdm.system.serialization.SerializedIntegerVariable;
import com.huahui.datasphere.mdm.system.serialization.SerializedLongVariable;
import com.huahui.datasphere.mdm.system.serialization.SerializedStringVariable;
import com.huahui.datasphere.mdm.system.serialization.SerializedTimeVariable;
import com.huahui.datasphere.mdm.system.serialization.SerializedTimestampVariable;
import com.huahui.datasphere.mdm.system.type.variables.Variable;
import com.huahui.datasphere.mdm.system.type.variables.Variables;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;

/**
 * @author theseusyang
 * Date record schema.
 */
public class VariablesSchema implements Schema<Variables> {
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
    public boolean isInitialized(Variables message) {
        return message != null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Variables newMessage() {
        return new Variables();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageName() {
        return Variables.class.getSimpleName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageFullName() {
        return Variables.class.getName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<Variables> typeClass() {
        return Variables.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeFrom(Input input, Variables message) throws IOException {

        int field;
        while ((field = input.readFieldNumber(this)) != CommonFieldValues.END_OF_RECORD) {

            switch (field) {
            case SystemFieldValues.STRING_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedStringVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.INTEGER_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedIntegerVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.LONG_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedLongVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.FLOAT_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedFloatVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.DOUBLE_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedDoubleVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.BOOLEAN_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedBooleanVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.DATE_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedDateVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.TIME_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedTimeVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.TIMESTAMP_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedTimestampVariable(), SystemSchemas.VARIABLE_SCHEMA));
                break;
            case SystemFieldValues.INSTANT_VARIABLE_VAL:
                message.add(input.mergeObject(new SerializedInstantVariable(), SystemSchemas.VARIABLE_SCHEMA));
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
    public void writeTo(Output output, Variables message) throws IOException {

        for (Variable<?> v : message) {

            switch (v.getType()) {
            case INTEGER:
                output.writeObject(SystemSchemaFields.INTEGER_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case LONG:
                output.writeObject(SystemSchemaFields.LONG_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case BOOLEAN:
                output.writeObject(SystemSchemaFields.BOOLEAN_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case FLOAT:
                output.writeObject(SystemSchemaFields.FLOAT_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case DOUBLE:
                output.writeObject(SystemSchemaFields.DOUBLE_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case STRING:
                output.writeObject(SystemSchemaFields.STRING_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case DATE:
                output.writeObject(SystemSchemaFields.DATE_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case TIME:
                output.writeObject(SystemSchemaFields.TIME_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case TIMESTAMP:
                output.writeObject(SystemSchemaFields.TIMESTAMP_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            case INSTANT:
                output.writeObject(SystemSchemaFields.INSTANT_VARIABLE.getValue(), v, SystemSchemas.VARIABLE_SCHEMA, false);
                break;
            default:
                break;
            }
        }
    }
}
