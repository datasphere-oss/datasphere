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
import java.util.ArrayList;
import java.util.List;

import com.huahui.datasphere.mdm.core.type.security.SecurityLabel;
import com.huahui.datasphere.mdm.core.type.security.SecurityLabelAttribute;

/**
 * Security label data transfer object.
 *
 * @author ilya.bykov
 *
 */
public class SecurityLabelDTO extends BaseSecurityDTO implements SecurityLabel, Serializable {
	/**
     * SVUID.
     */
    private static final long serialVersionUID = 7787793632986876965L;
    /**
	 * Security label name.
	 */
	private String name;
	/**
	 * Display name.
	 */
	private String displayName;
	/**
	 * Description.
	 */
	private String description;
	/**
	 * Security label attributes.
	 */
	private List<SecurityLabelAttribute> attributes;

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the displayName
	 */
	@Override
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the attributes
	 */
	@Override
	public List<SecurityLabelAttribute> getAttributes() {
		if (attributes == null) {
			attributes = new ArrayList<>();
		}
		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(final List<SecurityLabelAttribute> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
