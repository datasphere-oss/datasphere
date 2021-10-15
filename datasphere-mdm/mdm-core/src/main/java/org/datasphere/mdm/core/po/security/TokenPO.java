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
 * The persistent class for the s_token database table.
 * 
 * @author ilya.bykov
 */
public class TokenPO extends BaseTokenPO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "s_token";

	/**
	 * Instantiates a new token po.
	 */
	public TokenPO() {
	}

	/**
	 * The Class Fields.
	 */
	public static final class Fields extends BaseTokenPO.Fields {

		/**
		 * Instantiates a new fields.
		 */
		private Fields() {
			super();
		}

		/** The Constant S_USER_ID. */
		public static final String S_USER_ID = "S_USER_ID";
		public static final String ALL_WITH_TABLE_NAME = String.join(
				DELIMETER, 
				String.join(DOT, TABLE_NAME, ID),
				String.join(DOT, TABLE_NAME, TOKEN), 
				String.join(DOT, TABLE_NAME, S_USER_ID),
				String.join(DOT, TABLE_NAME, CREATED_AT), 
				String.join(DOT, TABLE_NAME, UPDATED_AT),
				String.join(DOT, TABLE_NAME, CREATED_BY), 
				String.join(DOT, TABLE_NAME, UPDATED_BY));
		/** All fields combined, used for update queries. */
		public static final String ALL_TO_UPDATE = String.join(
				DELIMETER, 
				String.join(EQUALS,  TOKEN, TOKEN), 
				String.join(EQUALS, S_USER_ID, S_USER_ID),
				String.join(EQUALS, CREATED_AT, CREATED_AT), 
				String.join(EQUALS, UPDATED_AT, UPDATED_AT),
				String.join(EQUALS,  CREATED_BY, CREATED_BY), 
				String.join(EQUALS, UPDATED_BY, UPDATED_BY));
		public static final String ALL_TO_INSERT = String.join(
				DELIMETER, 
				String.join("",DOTS,  TOKEN), 
				String.join("",DOTS,  S_USER_ID),
				String.join("",DOTS,  CREATED_AT), 
				String.join("",DOTS,  UPDATED_AT),
				String.join("",DOTS,  CREATED_BY), 
				String.join("",DOTS,  UPDATED_BY));
		public static final String ALL = String.join(
				DELIMETER, 
				TOKEN, 
				S_USER_ID,
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

		/** The Constant SELECT_BY_USER_ID. */
		public static final String SELECT_BY_USER_ID = "select " + Fields.ALL_WITH_TABLE_NAME +" FROM "+TABLE_NAME+ " where " + Fields.S_USER_ID + " = :"
				+ Fields.S_USER_ID;

		/** The Constant SELECT_BY_TOKEN. */
		public static final String SELECT_BY_TOKEN = "select " + Fields.ALL_WITH_TABLE_NAME +" FROM "+TABLE_NAME+ " where " + Fields.TOKEN + " = :"
				+ Fields.TOKEN;
		public static final String INSERT_NEW =
				"INSERT INTO "+TABLE_NAME+"("+Fields.ALL+") VALUES ("+Fields.ALL_TO_INSERT+")";
		public static final String DELETE_BY_ID = 
				"DELETE FROM "+TABLE_NAME
					+ " WHERE "+Fields.ID+" = :"+Fields.ID;
		public static final String DELETE_BY_TOKEN = 
				"DELETE FROM "+TABLE_NAME
					+ " WHERE "+Fields.TOKEN+" = :"+Fields.TOKEN;
		
	}
}