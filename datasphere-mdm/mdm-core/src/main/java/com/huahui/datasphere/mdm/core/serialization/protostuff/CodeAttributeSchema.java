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

import com.huahui.datasphere.mdm.system.serialization.protostuff.CommonFieldValues;

import com.huahui.datasphere.mdm.core.serialization.VerifyableCodeAttribute;
import com.huahui.datasphere.mdm.core.type.data.CodeAttribute;
import com.huahui.datasphere.mdm.core.type.data.impl.AbstractAttribute;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;

/**
 * @author theseusyang
 * Code attributes.
 */
public class CodeAttributeSchema implements Schema<CodeAttribute<?>> {
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
    public boolean isInitialized(CodeAttribute<?> message) {

        if (message instanceof VerifyableCodeAttribute<?>) {
            return ((VerifyableCodeAttribute<?>) message).isValid();
        }

        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public CodeAttribute<?> newMessage() {
        // Should not be called.
        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageName() {
        return CodeAttribute.class.getSimpleName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageFullName() {
        return CodeAttribute.class.getName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? super CodeAttribute<?>> typeClass() {
        return CodeAttribute.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeFrom(Input input, CodeAttribute<?> message) throws IOException {

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
            default:
                break;
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeTo(Output output, CodeAttribute<?> message) throws IOException {

        output.writeString(CoreSchemaFields.FIELD_NAME.getValue(), message.getName(), false);

        switch (message.getDataType()) {
        case STRING:
            String stringVal = message.castValue();
            output.writeString(CoreSchemaFields.STRING_VALUE.getValue(), stringVal, false);
            break;
        case INTEGER:
            Long integerVal = message.castValue();
            output.writeInt64(CoreSchemaFields.INTEGER_VALUE.getValue(), integerVal, false);
            break;
        default:
            break;
        }

    }
}
