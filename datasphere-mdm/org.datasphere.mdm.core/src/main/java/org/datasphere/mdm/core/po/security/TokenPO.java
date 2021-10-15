/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 * 
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.po.security;

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