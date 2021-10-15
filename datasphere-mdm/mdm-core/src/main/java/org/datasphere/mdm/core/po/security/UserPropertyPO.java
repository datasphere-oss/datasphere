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

/**
 * Date: 05.07.2016
 */

package com.huahui.datasphere.mdm.core.po.security;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class UserPropertyPO extends BaseSecurityPO {
    private Long id;
    private String name;
    private boolean required;
    private String displayName;
    private boolean readOnly;
    /**
     * type of ui control defined by integration developers.
     */
    private String fieldType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public UserPropertyPO setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    public String getFieldType() {
        return fieldType;
    }

    public UserPropertyPO setFieldType(String fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public enum FieldColumns {
        ID,
        REQUIRED,
        NAME,
        DISPLAY_NAME,
        READ_ONLY,
        FIELD_TYPE
    }
}
