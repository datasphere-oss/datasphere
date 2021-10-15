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

package com.huahui.datasphere.mdm.core.type.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Denis Kostovarov
 */
public abstract class AbstractSecurityLabel implements SecurityLabel, Serializable {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = -4760502359316421111L;
    private String name;
    private String displayName;
    private String description;
    private List<SecurityLabelAttribute> attributes;

    public AbstractSecurityLabel() {
        attributes = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public List<SecurityLabelAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setAttributes(final List<SecurityLabelAttribute> attributes) {
        this.attributes = attributes;
    }
}
