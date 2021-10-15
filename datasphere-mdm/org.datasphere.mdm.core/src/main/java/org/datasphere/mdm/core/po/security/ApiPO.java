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
 * The Class ApiPO.
 * 
 * @author ilya.bykov
 */
public class ApiPO extends BaseSecurityPO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant TABLE_NAME. */
	public static final String TABLE_NAME = "S_APIS";
	
	/** The id. */
	private int id;
	/** The name. */
	private String name;

	/** The display name. */
	private String displayName;

	/** The description. */
	private String description;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
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
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
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
	 * The Class Fields.
	 */
	public static final class Fields extends BaseUserPO.Fields {

		/**
		 * Instantiates a new fields.
		 */
		private Fields() {
			super();
		}

		/** The Constant NAME. */
		public static final String NAME = "NAME";
		/** The Constant S_USER_ID. */
		public static final String S_USER_ID = "S_USER_ID";
		/** The Constant S_USER_ID. */
		public static final String DISPLAY_NAME = "DISPLAY_NAME";
		/** The Constant EMAIL. */
		public static final String DESCRIPTION = "DESCRIPTION";

		/** All fields combined. */
		public static final String ALL_WITH_TABLE_NAME = String.join(DELIMETER, String.join(DOT, TABLE_NAME, ID),
				String.join(DOT, TABLE_NAME, NAME), String.join(DOT, TABLE_NAME, DISPLAY_NAME),
				String.join(DOT, TABLE_NAME, DESCRIPTION), String.join(DOT, TABLE_NAME, CREATED_AT),
				String.join(DOT, TABLE_NAME, UPDATED_AT), String.join(DOT, TABLE_NAME, CREATED_BY),
				String.join(DOT, TABLE_NAME, UPDATED_BY));
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
		public static final String SELECT_ALL = "SELECT " + Fields.ALL_WITH_TABLE_NAME + " FROM " + TABLE_NAME;
		/** The Constant SELECT_BY_ID. */
		public static final String SELECT_BY_ID = "SELECT " + Fields.ALL_WITH_TABLE_NAME + " FROM " + TABLE_NAME
				+ " WHERE " + Fields.ID + " = :" + Fields.ID;

		/** The Constant SELECT_BY_NAME. */
		public static final String SELECT_BY_NAME = "SELECT " + Fields.ALL_WITH_TABLE_NAME + " FROM " + TABLE_NAME
				+ " WHERE " + Fields.NAME + " = :" + Fields.NAME;
		/**
		 * The Constant SELECT_BY_USER_ID.
		 */
		public static final String SELECT_BY_USER_ID = "SELECT " + Fields.ALL_WITH_TABLE_NAME + " FROM " + TABLE_NAME
				+ "  INNER JOIN s_user_s_apis susa ON s_apis.id=susa.s_api_id WHERE susa." + Fields.S_USER_ID + " = :"
				+ Fields.S_USER_ID;

	}
}
