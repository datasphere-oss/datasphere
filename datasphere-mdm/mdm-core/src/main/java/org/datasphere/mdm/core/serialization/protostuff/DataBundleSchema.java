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
import com.huahui.datasphere.mdm.system.serialization.protostuff.SystemFieldValues;
import com.huahui.datasphere.mdm.system.serialization.protostuff.SystemSchemaFields;
import com.huahui.datasphere.mdm.system.serialization.protostuff.SystemSchemas;
import com.huahui.datasphere.mdm.system.type.variables.Variables;

import com.huahui.datasphere.mdm.core.type.data.impl.SerializableDataRecord;
import com.huahui.datasphere.mdm.core.type.formless.DataBundle;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;

/**
 * @author theseusyang
 * DataBundle (record + free form variables) schema.
 */
public class DataBundleSchema implements Schema<DataBundle> {
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
    public boolean isInitialized(DataBundle message) {
        return message != null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DataBundle newMessage() {
        return new DataBundle();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageName() {
        return DataBundle.class.getSimpleName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageFullName() {
        return DataBundle.class.getName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<DataBundle> typeClass() {
        return DataBundle.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeFrom(Input input, DataBundle message) throws IOException {

        int field;
        while ((field = input.readFieldNumber(this)) != CommonFieldValues.END_OF_RECORD) {

            switch (field) {
            case CoreFieldValues.DATA_RECORD_VAL:
                message.setRecord(input.mergeObject(new SerializableDataRecord(), CoreSchemas.DATA_RECORD_SCHEMA));
                break;
            case SystemFieldValues.VARIABLES_VAL:
                message.setVariables(input.mergeObject(new Variables(), SystemSchemas.VARIABLES_SCHEMA));
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
    public void writeTo(Output output, DataBundle message) throws IOException {

        if (message.hasVariables()) {
            output.writeObject(SystemSchemaFields.VARIABLES.getValue(), message.getVariables(), SystemSchemas.VARIABLES_SCHEMA, false);
        }

        if (message.hasRecord()) {
            output.writeObject(CoreSchemaFields.DATA_RECORD.getValue(), message.getRecord(), CoreSchemas.DATA_RECORD_SCHEMA, false);
        }
    }
}
