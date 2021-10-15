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

package com.huahui.datasphere.mdm.core.type.data.impl;

import com.huahui.datasphere.mdm.core.type.data.Attribute;
import com.huahui.datasphere.mdm.core.type.data.DataRecord;

/**
 * @author theseusyang
 * Abstract attribute name holder.
 */
public abstract class AbstractAttribute implements Attribute {
    /**
     * Attribute name.
     */
    private String name;
    /**
     * Record link.
     */
    private DataRecord record;
    /**
     * Special serialization constructor. Schould not be used otherwise.
     */
    protected AbstractAttribute() {
        super();
    }
    /**
     * Constructor.
     * @param name the name of the attribute.
     */
    protected AbstractAttribute(String name) {
        super();
        this.name = name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }
    /**
     * Sets the attribute name. Used for serialization. Should not be used otherwise.
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord getRecord() {
        return record;
    }
    /**
     * @param record the record to set
     */
    @Override
    public void setRecord(DataRecord record) {
        this.record = record;
    }
}
