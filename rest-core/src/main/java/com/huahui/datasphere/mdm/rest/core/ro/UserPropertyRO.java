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
package com.huahui.datasphere.mdm.rest.core.ro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class UserProperties.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPropertyRO {
    private Long id;
    /**
     * is required
     */
    private boolean required;
    /**
     * The name.
     */
    private String name;

    /**
     * The display name.
     */
    private String displayName;

    /**
     * The value.
     */
    private String value;

    private boolean readOnly;

    /**
     * type of ui control defined by integration developers.
     */
    private String fieldType;

    /**
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * Is required.
     * @return is required.
     */
    public boolean isRequired() {
		return required;
	}
    /**
     * Set required.
     * @param required
     */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name.
     *
     * @param displayName
     *            the new display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public void setValue(String value) {
        this.value = value;
    }

    public String getFieldType() {
        return fieldType;
    }

    public UserPropertyRO setFieldType(String fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public UserPropertyRO setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }
}