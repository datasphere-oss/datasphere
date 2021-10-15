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

package com.huahui.datasphere.mdm.core.po.security;

/**
 * @author Denis Kostovarov
 */
public abstract class BaseTokenPO extends BaseSecurityPO {
	/** The id. */
	private Integer id;
	/** The token. */
	private String token;
	/** The user. */
	private BaseUserPO user;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the token.
	 *
	 * @return the token
	 */
	public String getToken() {
		return this.token;
	}

	/**
	 * Sets the token.
	 *
	 * @param token
	 *            the new token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public BaseUserPO getUser() {
		return this.user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user
	 *            the new user
	 */
	public void setUser(BaseUserPO user) {
		this.user = user;
	}

	/**
	 * The Class Fields.
	 */
	public static class Fields extends BaseSecurityPO.Fields {

		/**
		 * Instantiates a new fields.
		 */
		protected Fields() {
			super();
		}

		/** The Constant TOKEN. */
		public static final String TOKEN = "TOKEN";
	}
}
