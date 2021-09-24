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

import com.huahui.datasphere.mdm.core.type.formless.BundlesArray;
import com.huahui.datasphere.mdm.core.type.formless.DataBundle;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;

/**
 * @author theseusyang
 * DataBundle (record + free form variables) schema.
 */
public class BundlesArraySchema implements Schema<BundlesArray> {
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
    public boolean isInitialized(BundlesArray message) {
        return message != null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public BundlesArray newMessage() {
        return new BundlesArray();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageName() {
        return BundlesArray.class.getSimpleName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String messageFullName() {
        return BundlesArray.class.getName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<BundlesArray> typeClass() {
        return BundlesArray.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeFrom(Input input, BundlesArray message) throws IOException {

        int field;
        while ((field = input.readFieldNumber(this)) != CommonFieldValues.END_OF_RECORD) {
            if (field == CoreFieldValues.DATA_BUNDLE_VAL) {
                message.add(input.mergeObject(new DataBundle(), CoreSchemas.DATA_BUNDLE_SCHEMA));
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeTo(Output output, BundlesArray message) throws IOException {
        for (DataBundle b : message) {
            output.writeObject(CoreSchemaFields.DATA_BUNDLE.getValue(), b, CoreSchemas.DATA_BUNDLE_SCHEMA, false);
        }
    }
}
