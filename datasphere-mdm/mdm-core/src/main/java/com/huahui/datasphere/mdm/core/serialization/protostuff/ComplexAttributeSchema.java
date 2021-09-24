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
import java.util.Objects;

import com.huahui.datasphere.mdm.system.serialization.protostuff.CommonFieldValues;

import com.huahui.datasphere.mdm.core.serialization.VerifyableComplexAttribute;
import com.huahui.datasphere.mdm.core.type.data.ComplexAttribute;
import com.huahui.datasphere.mdm.core.type.data.DataRecord;
import com.huahui.datasphere.mdm.core.type.data.impl.AbstractAttribute;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;

/**
 * @author theseusyang
 * Complex attributes.
 */
public class ComplexAttributeSchema implements Schema<ComplexAttribute> {
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
    public boolean isInitialized(ComplexAttribute message) {

        if (message instanceof VerifyableComplexAttribute) {
            return ((VerifyableComplexAttribute) message).isValid();
        }

        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ComplexAttribute newMessage() {
        // Should not be called.
        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageName() {
        return ComplexAttribute.class.getSimpleName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageFullName() {
        return ComplexAttribute.class.getName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? super ComplexAttribute> typeClass() {
        return ComplexAttribute.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeFrom(Input input, ComplexAttribute message) throws IOException {

        int field;
        while ((field = input.readFieldNumber(this)) != CommonFieldValues.END_OF_RECORD) {

            switch (field) {
            case CoreFieldValues.FIELD_NAME_VAL:
                ((AbstractAttribute) message).setName(input.readString());
                break;
            case CoreFieldValues.DATA_RECORD_VAL:
                message.add(input.mergeObject(null, CoreSchemas.DATA_RECORD_SCHEMA));
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
    public void writeTo(Output output, ComplexAttribute message) throws IOException {

        output.writeString(CoreSchemaFields.FIELD_NAME.getValue(), message.getName(), false);
        if (Objects.isNull(message.isEmpty())) {
            return;
        }

        for (DataRecord child : message) {
            output.writeObject(CoreSchemaFields.DATA_RECORD.getValue(), child, CoreSchemas.DATA_RECORD_SCHEMA, true);
        }
    }
}
