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
 * The persistent class for the s_resource database table.
 *
 * @author ilya.bykov
 */
public class ResourcePO extends BaseSecurityPO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant TABLE_NAME. */
	private static final String TABLE_NAME="s_resource";
	/** The id. */
	private Integer id;
	/** Parent id. */
	private Integer parentId;
	/** Parent name insert support. */
	private String parentName;
	/** The name. */
	private String name;
	/** The name. */
	private String displayName;
	/** The r type. */
	private String rType;
	/** The category */
	private String category;

	/**
	 * Instantiates a new resource po.
	 */
	public ResourcePO() {
	    super();
	}

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
     * @return the parent
     */
    public Integer getParentId() {
        return parentId;
    }


    /**
     * @param parent the parent to set
     */
    public void setParentId(Integer parent) {
        this.parentId = parent;
    }



    /**
     * @return the parentName
     */
    public String getParentName() {
        return parentName;
    }


    /**
     * @param parentName the parentName to set
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
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

	/**
	 * Gets the r type.
	 *
	 * @return the r type
	 */
	public String getRType() {
		return this.rType;
	}

	/**
	 * Sets the r type.
	 *
	 * @param rType
	 *            the new r type
	 */
	public void setRType(String rType) {
		this.rType = rType;
	}


    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }


    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
	 * Gets the display name.
	 *
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 *
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Is top level or not
	 * @return
	 */
	public boolean isTopLevel() {
	    return parentId == null;
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
		ResourcePO other = (ResourcePO) obj;

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
        public static final String PARENT_ID = "parent_id";

		/** The Constant NAME. */
		public static final String NAME = "NAME";

		/** The Constant R_TYPE. */
		public static final String R_TYPE = "R_TYPE";

		/** The Constant CATEGORY. */
        public static final String CATEGORY = "CATEGORY";

		/** The Constant S_RESOURCE_ID. */
		public static final String S_RESOURCE_ID = "S_RESOURCE_ID";

		/** The Constant DISPLAY_NAME. */
		public static final String DISPLAY_NAME = "DISPLAY_NAME";

		/** All fields combined. */
		public static final String ALL_WITH_TABLE_NAME = String.join(
				DELIMETER,
				String.join(DOT, TABLE_NAME, ID),
				String.join(DOT, TABLE_NAME, PARENT_ID),
				String.join(DOT, TABLE_NAME, NAME),
				String.join(DOT, TABLE_NAME, DISPLAY_NAME),
				String.join(DOT, TABLE_NAME, R_TYPE),
				String.join(DOT, TABLE_NAME, CATEGORY),
				String.join(DOT, TABLE_NAME, CREATED_AT),
				String.join(DOT, TABLE_NAME, UPDATED_AT),
				String.join(DOT, TABLE_NAME, CREATED_BY),
				String.join(DOT, TABLE_NAME, UPDATED_BY));
		/** All fields combined, used for update queries. */
		public static final String ALL_TO_UPDATE = String.join(
				DELIMETER,
				String.join(EQUALS,  NAME, NAME),
				String.join(EQUALS, DISPLAY_NAME, DISPLAY_NAME),
				String.join(EQUALS,  R_TYPE, R_TYPE),
				String.join(EQUALS,  CATEGORY, CATEGORY),
				String.join(EQUALS, CREATED_AT, CREATED_AT),
				String.join(EQUALS, UPDATED_AT, UPDATED_AT),
				String.join(EQUALS,  CREATED_BY, CREATED_BY),
				String.join(EQUALS, UPDATED_BY, UPDATED_BY));

		/** The Constant ALL_TO_INSERT. */
		public static final String ALL_TO_INSERT = String.join(
				DELIMETER,
				String.join("", DOTS, NAME),
				String.join("", DOTS, DISPLAY_NAME),
				String.join("", DOTS, R_TYPE),
				String.join("", DOTS, CATEGORY),
				String.join("", DOTS, CREATED_AT),
				String.join("", DOTS, UPDATED_AT),
				String.join("", DOTS, CREATED_BY),
				String.join("", DOTS, UPDATED_BY));

		/** The Constant ALL. */
		public static final String ALL = String.join(
				DELIMETER,
				NAME,
				DISPLAY_NAME,
				R_TYPE,
				CATEGORY,
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

		/** The Constant CLEAN_RESOURCES. */
		public static final String CLEAN_RESOURCES =
				"DELETE FROM S_RIGHT_S_RESOURCE "
				+ "WHERE "+ Fields.S_RESOURCE_ID + " = :" + Fields.S_RESOURCE_ID;

		/** The Constant INSERT_ROLE. */
		public static final String INSERT_NEW =
				"INSERT INTO "+TABLE_NAME+"("+Fields.ALL+") VALUES ("+Fields.ALL_TO_INSERT+")";
	}
}