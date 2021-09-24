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

import java.sql.Timestamp;

/**
 * Base class for the security persistance objects.
 * 
 * @author ilya.bykov
 *
 */
public abstract class BaseSecurityPO {
	
	/** The Constant DELIMETER. */
	protected static final String DELIMETER = ", ";
	/** The Constant DELIMETER. */
	protected static final String DOT = ".";
	/** The Constant DELIMETER. */
	protected static final String EQUALS = " = :";
	/** The Constant DELIMETER. */
	protected static final String DOTS = ":";
	/** The Constant rows limit. */
	public static final String ROWS_LIMIT = "rowsLimit";
	/** The updated at. */
	private Timestamp updatedAt;

	/** The updated by. */
	private String updatedBy;
	/** The created at. */
	private Timestamp createdAt;

	/** The created by. */
	private String createdBy;

	/**
	 * Gets the updated at.
	 *
	 * @return the updated at
	 */
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * Sets the updated at.
	 *
	 * @param updatedAt
	 *            the new updated at
	 */
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * Gets the updated by.
	 *
	 * @return the updated by
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * Sets the updated by.
	 *
	 * @param updatedBy
	 *            the new updated by
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * Gets the created at.
	 *
	 * @return the created at
	 */
	public Timestamp getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets the created at.
	 *
	 * @param createdAt
	 *            the new created at
	 */
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Gets the created by.
	 *
	 * @return the created by
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets the created by.
	 *
	 * @param createdBy
	 *            the new created by
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * The Class Fields.
	 */
	public static class Fields {

		/**
		 * Instantiates a new fields.
		 */
		protected Fields() {

		}

		/** The Constant ID. */
		public static final String ID = "ID";

		/** The Constant CREATED_AT. */
		public static final String CREATED_AT = "CREATED_AT";

		/** The Constant UPDATED_AT. */
		public static final String UPDATED_AT = "UPDATED_AT";

		/** The Constant CREATED_BY. */
		public static final String CREATED_BY = "CREATED_BY";

		/** The Constant UPDATED_BY. */
		public static final String UPDATED_BY = "UPDATED_BY";
	}
}
