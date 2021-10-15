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

import com.huahui.datasphere.mdm.core.type.security.Endpoint;

/**
 * The Class UserAPIDTO.
 * @author ilya.bykov
 */
public class UserEndpointDTO extends BaseSecurityDTO implements Endpoint {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The name. */
	private String name;

	/** The display name. */
	private String displayName;

	/** The description. */
	private String description;

	public UserEndpointDTO(String name, String displayName, String description) {
	    super();
	    this.name = name;
	    this.displayName = displayName;
	    this.description = description;
	}

	public UserEndpointDTO() {
	    super();
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
	 * @param name the new name
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
	 * @param displayName the new display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	@Override
    public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
