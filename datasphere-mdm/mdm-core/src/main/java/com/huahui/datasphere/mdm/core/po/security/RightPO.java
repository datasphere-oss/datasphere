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

import java.io.Serializable;

/**
 * The persistent class for the s_right database table.
 * 
 * @author ilya.bykov
 */

public class RightPO extends BaseSecurityPO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant TABLE_NAME. */
	public static final String TABLE_NAME="s_right";
	/** The id. */
	private Integer id;

	/** The description. */
	private String description;

	/** The name. */
	private String name;

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
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RightPO other = (RightPO) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
	/**
	 * The Class Fields.
	 */
	public static final class Fields extends BaseSecurityPO.Fields {

		/**
		 * Instantiates a new fields.
		 */
		private Fields() {

		}

		/** The Constant NAME. */
		public static final String NAME = "NAME";
		public static final String S_ROLE_ID = "S_ROLE_ID";
		/** The Constant DESCRIPTION. */
		public static final String DESCRIPTION = "DESCRIPTION";
		
		
		/** All fields combined. */
		public static final String ALL_WITH_TABLE_NAME = String.join(
				DELIMETER, 
				String.join(DOT, TABLE_NAME, ID),
				String.join(DOT, TABLE_NAME, NAME), 
				String.join(DOT, TABLE_NAME, DESCRIPTION), 
				String.join(DOT, TABLE_NAME, CREATED_AT), 
				String.join(DOT, TABLE_NAME, UPDATED_AT),
				String.join(DOT, TABLE_NAME, CREATED_BY), 
				String.join(DOT, TABLE_NAME, UPDATED_BY));
		/** All fields combined, used for update queries. */
		public static final String ALL_TO_UPDATE = String.join(
				DELIMETER, 
				String.join(EQUALS,  NAME, NAME), 
				String.join(EQUALS, DESCRIPTION, DESCRIPTION), 
				String.join(EQUALS, CREATED_AT, CREATED_AT), 
				String.join(EQUALS, UPDATED_AT, UPDATED_AT),
				String.join(EQUALS,  CREATED_BY, CREATED_BY), 
				String.join(EQUALS, UPDATED_BY, UPDATED_BY));
		
		/** The Constant ALL_TO_INSERT. */
		public static final String ALL_TO_INSERT = String.join(
				DELIMETER, 
				String.join("",DOTS,  NAME), 
				String.join("",DOTS,  DESCRIPTION), 
				String.join("",DOTS,  CREATED_AT), 
				String.join("",DOTS,  UPDATED_AT),
				String.join("",DOTS,  CREATED_BY), 
				String.join("",DOTS,  UPDATED_BY));
		
		/** The Constant ALL. */
		public static final String ALL = String.join(
				DELIMETER, 
				NAME, 
			    DESCRIPTION, 
				CREATED_AT, 
				UPDATED_AT,
				CREATED_BY, 
				UPDATED_BY);
	}

	/**
	 * The Class Queries.
	 */
	public static final class Queries {

		/**
		 * Instantiates a new queries.
		 */
		private Queries() {

		}
		/** The Constant SELECT_ALL. */
		public static final String SELECT_ALL = 
				"SELECT " + Fields.ALL_WITH_TABLE_NAME 
					+ " FROM " + TABLE_NAME;
		/** The Constant SELECT_BY_ID. */
		public static final String SELECT_BY_ID = 
				"SELECT " + Fields.ALL_WITH_TABLE_NAME 
					+ " FROM " + TABLE_NAME 
					+ " WHERE " + Fields.ID	+ " = :" + Fields.ID;
		public static final String SELECT_BY_S_ROLE_ID = 
				"SELECT " + Fields.ALL_WITH_TABLE_NAME 
					+ " FROM " + TABLE_NAME 
					+ " WHERE " + Fields.S_ROLE_ID	+ " = :" + Fields.S_ROLE_ID;
		/** The Constant SELECT_BY_NAME. */
		public static final String SELECT_BY_NAME = 
				"SELECT " + Fields.ALL_WITH_TABLE_NAME 
					+ " FROM " + TABLE_NAME 
					+ " WHERE " + Fields.NAME + " = :" + Fields.NAME;		
		
		/** The Constant UPDATE_BY_NAME. */
		public static final String UPDATE_BY_NAME = 
				"UPDATE " +TABLE_NAME
					+ " SET "+ Fields.ALL_TO_UPDATE 
					+ " WHERE " + Fields.NAME + " = :" + Fields.NAME;
		
		/** The Constant DELETE_BY_NAME. */
		public static final String DELETE_BY_ID = 
				"DELETE FROM "+TABLE_NAME
					+ " WHERE "+Fields.ID+" = :"+Fields.ID;
		
		/** The Constant INSERT_NEW. */
		public static final String INSERT_NEW =
				"INSERT INTO "+TABLE_NAME+"("+Fields.ALL+") VALUES ("+Fields.ALL_TO_INSERT+")";
	}
}