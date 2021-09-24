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

package com.huahui.datasphere.mdm.core.dto;

import java.io.Serializable;

import com.huahui.datasphere.mdm.core.type.security.CustomProperty;

/**
 * The Class UserProperty.
 *
 * @author ilya.bykov
 */
public class UserPropertyDTO implements CustomProperty, Serializable {
	/**
     * SVUID.
     */
    private static final long serialVersionUID = -8381928058844771461L;

    /** Identifier. */
	private Long id;

	/**  Is required. */
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

	/**
	 * UN-12188
	 */
	private boolean readOnly;

	/**
	 * the type of control defined by integration developers
	 */
	private String fieldType;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Checks if is required.
	 *
	 * @return true, if is required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Sets the required.
	 *
	 * @param required the new required
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	@Override
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
	@Override
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
	@Override
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

	public boolean isReadOnly() {
		return readOnly;
	}

	public UserPropertyDTO setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}

	@Override
	public String getFieldType() {
		return fieldType;
	}

	public UserPropertyDTO setFieldType(String fieldType) {
		this.fieldType = fieldType;
		return this;
	}
}
