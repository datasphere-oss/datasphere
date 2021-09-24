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

import com.huahui.datasphere.mdm.core.type.data.ArrayAttribute;
import com.huahui.datasphere.mdm.core.type.data.CodeAttribute;
import com.huahui.datasphere.mdm.core.type.data.ComplexAttribute;
import com.huahui.datasphere.mdm.core.type.data.DataRecord;
import com.huahui.datasphere.mdm.core.type.data.SimpleAttribute;
import com.huahui.datasphere.mdm.core.type.formless.BundlesArray;
import com.huahui.datasphere.mdm.core.type.formless.DataBundle;

import io.protostuff.Schema;

/**
 * @author theseusyang
 * Just a static schemas holder.
 */
public final class CoreSchemas {
    /**
     * Data bundles array.
     */
    public static final Schema<BundlesArray> BUNDLES_ARRAY_SCHEMA = new BundlesArraySchema();
    /**
     * Record + variables bundle schema.
     */
    public static final Schema<DataBundle> DATA_BUNDLE_SCHEMA = new DataBundleSchema();
    /**
     * Data record schema.
     */
    public static final Schema<DataRecord> DATA_RECORD_SCHEMA = new DataRecordSchema();
    /**
     * Simple attribute schema.
     */
    public static final Schema<SimpleAttribute<?>> SIMPLE_ATTRIBUTE_SCHEMA = new SimpleAttributeSchema();
    /**
     * Code attribute schema.
     */
    public static final Schema<CodeAttribute<?>> CODE_ATTRIBUTE_SCHEMA = new CodeAttributeSchema();
    /**
     * Array attribute schema.
     */
    public static final Schema<ArrayAttribute<?>> ARRAY_ATTRIBUTE_SCHEMA = new ArrayAttributeSchema();
    /**
     * Complex attribute schema.
     */
    public static final Schema<ComplexAttribute> COMPLEX_ATTRIBUTE_SCHEMA = new ComplexAttributeSchema();
    /**
     * Constructor.
     */
    private CoreSchemas() {
        super();
    }
}
