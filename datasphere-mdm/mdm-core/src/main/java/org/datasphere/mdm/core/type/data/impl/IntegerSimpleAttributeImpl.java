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

import com.huahui.datasphere.mdm.core.type.data.CodeLinkValue;

/**
 * @author theseusyang
 * Long simple attribute.
 */
public class IntegerSimpleAttributeImpl extends AbstractSimpleAttribute<Long> implements CodeLinkValue {

    /**
     * Link to record of the code attribute.
     */
    private String linkEtalonId;
    /**
     * Special serialization constructor. Schould not be used otherwise.
     */
    protected IntegerSimpleAttributeImpl() {
        super();
    }
    /**
     * Constructor.
     * @param name
     */
    public IntegerSimpleAttributeImpl(String name) {
        super(name);
    }

    /**
     * Constructor.
     * @param name the name
     * @param value the value
     */
    public IntegerSimpleAttributeImpl(String name, Long value) {
        super(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleDataType getDataType() {
        return SimpleDataType.INTEGER;
    }

    /**
     * Fluent part for compatibility.
     * @param value the value
     * @return self
     */
    public IntegerSimpleAttributeImpl withValue(Long value) {
        setValue(value);
        return this;
    }

    /**
     * @return the linkEtalonId
     */
    @Override
    public String getLinkEtalonId() {
        return linkEtalonId;
    }

    /**
     * @param linkEtalonId the linkEtalonId to set
     */
    @Override
    public void setLinkEtalonId(String linkEtalonId) {
        this.linkEtalonId = linkEtalonId;
    }
}
