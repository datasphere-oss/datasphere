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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

/**
 * The persistent class for the s_roles database table.
 *
 * @author ilya.bykov
 */
public class RolePO extends BaseSecurityPO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The table name.
     */
    public static final String TABLE_NAME = "s_role";

    /**
     * The id.
     */
    private Integer id;

    /**
     * The description.
     */
    private String description;

    /**
     * The display name.
     */
    private String displayName;

    /**
     * The name.
     */
    private String name;

    /**
     * The r type.
     */
    private String rType;

    /**
     * The S users.
     */
    private List<UserPO> users;

    /**
     * The S resources.
     */
    private final List<ResourceRightPO> connectedResourceRights = new ArrayList<>();

    /**
     * Transient disconnected resources.
     */
    private transient List<ResourceRightPO> disconnectedResourceRights;

    /**
     * The S users.
     */
    private final List<LabelPO> labels = new ArrayList<>();

    /**
     * The S users.
     */
    private final List<LabelAttributeValuePO> labelAttributeValues = new ArrayList<>();

    /**
     * Property values.
     */
    private transient List<RolePropertyValuePO> properties = new ArrayList<>();

    /* The label attribute values. */
//    private List<LabelAttributeValuePO> labelAttributeValues;

    /**
     * Instantiates a new role po.
     */
    public RolePO() {
    }

    public RolePO(String name) {
        this.name = name;
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
     * @param id the new id
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
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return this.displayName;
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
     * @param name the new name
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
     * @param rType the new r type
     */
    public void setRType(String rType) {
        this.rType = rType;
    }

    /**
     * Gets the s users.
     *
     * @return the s users
     */
    public List<UserPO> getUsers() {
        return this.users;
    }

    /**
     * Sets the s users.
     *
     * @param users the new s users
     */
    public void setUsers(List<UserPO> users) {
        this.users = users;
    }

    /**
     * Gets the s resources.
     *
     * @return the sResources
     */
    public List<ResourceRightPO> getConnectedResourceRights() {
        return this.connectedResourceRights;
    }

    /**
     * Sets the s resources.
     *
     * @param resources the sResources to set
     */
    public void setConnectedResourceRights(List<ResourceRightPO> resources) {
        this.connectedResourceRights.clear();
        if (CollectionUtils.isNotEmpty(resources)) {
            this.connectedResourceRights.addAll(resources);
        }
    }

    public void addConnectedResourceRight(ResourceRightPO resourceRight) {
        this.connectedResourceRights.add(resourceRight);
    }

    /**
     * @return the disconnectedResourceRights
     */
    public List<ResourceRightPO> getDisconnectedResourceRights() {
        return disconnectedResourceRights;
    }


    /**
     * @param disconnectedResourceRights the disconnectedResourceRights to set
     */
    public void setDisconnectedResourceRights(List<ResourceRightPO> disconnectedResourceRights) {
        this.disconnectedResourceRights = disconnectedResourceRights;
    }

    /**
     * Gets the label p os.
     *
     * @return the label p os
     */
    public List<LabelPO> getLabelPOs() {
        return Collections.unmodifiableList(this.labels);
    }

    /**
     * Sets the label p os.
     *
     * @param labels the new label p os
     */
    public RolePO setLabelPOs(List<LabelPO> labels) {
        this.labels.clear();
        if (CollectionUtils.isNotEmpty(labels)) {
            this.labels.addAll(labels);
        }
        return this;
    }

    /**
     * Adds the label p os.
     *
     * @param labels the label p os
     */
    public RolePO addLabelPOs(List<LabelPO> labels) {
        if (CollectionUtils.isNotEmpty(labels)) {
            this.labels.addAll(labels);
        }
        return this;
    }

    public RolePO addLabelPO(LabelPO label) {
        this.labels.add(label);
        return this;
    }

    public List<LabelAttributeValuePO> getLabelAttributeValues() {
        return Collections.unmodifiableList(labelAttributeValues);
    }

    public RolePO addLabelAttributeValue(final LabelAttributeValuePO labelAttributeValuePO) {
        if (labelAttributeValuePO != null) {
            this.labelAttributeValues.add(labelAttributeValuePO);
        }
        return this;
    }

    public RolePO setLabelAttributeValues(final List<LabelAttributeValuePO> labelAttributeValues) {
        this.labelAttributeValues.clear();
        if (CollectionUtils.isNotEmpty(labelAttributeValues)) {
            this.labelAttributeValues.addAll(labelAttributeValues);
        }
        return this;
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
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
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
        RolePO other = (RolePO) obj;
        if (this.name == null) {
            if (other.name != null)
                return false;
        } else if (!this.name.equals(other.name)) {
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

        /**
         * The Constant NAME.
         */
        public static final String NAME = "NAME";

        /**
         * The Constant R_TYPE.
         */
        public static final String R_TYPE = "R_TYPE";

        /**
         * The Constant DISPLAY_NAME.
         */
        public static final String DISPLAY_NAME = "DISPLAY_NAME";

        /**
         * The Constant DESCRIPTION.
         */
        public static final String DESCRIPTION = "DESCRIPTION";

        /**
         * The Constant S_USER_ID.
         */
        public static final String S_USER_ID = "S_USER_ID";
        /**
         * The Constant S_ROLE_ID.
         */
        public static final String S_ROLE_ID = "S_ROLE_ID";
        public static final String S_RESOURCE_ID = "S_RESOURCE_ID";
        public static final String S_RIGHT_ID = "S_RIGHT_ID";
        /**
         * The Constant S_ROLE_ID.
         */
        public static final String S_ROLES_ID = "S_ROLES_ID";

        /**
         * All fields combined.
         */
        public static final String ALL_WITH_TABLE_NAME = String.join(
                DELIMETER,
                String.join(DOT, TABLE_NAME, ID),
                String.join(DOT, TABLE_NAME, NAME),
                String.join(DOT, TABLE_NAME, DISPLAY_NAME),
                String.join(DOT, TABLE_NAME, DESCRIPTION),
                String.join(DOT, TABLE_NAME, R_TYPE),
                String.join(DOT, TABLE_NAME, CREATED_AT),
                String.join(DOT, TABLE_NAME, UPDATED_AT),
                String.join(DOT, TABLE_NAME, CREATED_BY),
                String.join(DOT, TABLE_NAME, UPDATED_BY));
        /**
         * All fields combined, used for update queries.
         */
        public static final String ALL_TO_UPDATE = String.join(
                DELIMETER,
                String.join(EQUALS, NAME, NAME),
                String.join(EQUALS, DISPLAY_NAME, DISPLAY_NAME),
                String.join(EQUALS, DESCRIPTION, DESCRIPTION),
                String.join(EQUALS, R_TYPE, R_TYPE),
                String.join(EQUALS, CREATED_AT, CREATED_AT),
                String.join(EQUALS, UPDATED_AT, UPDATED_AT),
                String.join(EQUALS, CREATED_BY, CREATED_BY),
                String.join(EQUALS, UPDATED_BY, UPDATED_BY));
        public static final String ALL_TO_INSERT = String.join(
                DELIMETER,
                String.join("", DOTS, NAME),
                String.join("", DOTS, DISPLAY_NAME),
                String.join("", DOTS, DESCRIPTION),
                String.join("", DOTS, R_TYPE),
                String.join("", DOTS, CREATED_AT),
                String.join("", DOTS, UPDATED_AT),
                String.join("", DOTS, CREATED_BY),
                String.join("", DOTS, UPDATED_BY));
        public static final String ALL = String.join(
                DELIMETER,
                NAME,
                DISPLAY_NAME,
                DESCRIPTION,
                R_TYPE,
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

        /**
         * The Constant SELECT_ALL.
         */
        public static final String SELECT_ALL =
                "SELECT " + Fields.ALL_WITH_TABLE_NAME
                        + " FROM " + TABLE_NAME + " ORDER BY " + Fields.DISPLAY_NAME;
        /**
         * The Constant SELECT_BY_ID.
         */
        public static final String SELECT_BY_ID =
                "SELECT " + Fields.ALL_WITH_TABLE_NAME
                        + " FROM " + TABLE_NAME
                        + " WHERE " + Fields.ID + " = :" + Fields.ID;

        /**
         * The Constant SELECT_BY_NAME.
         */
        public static final String SELECT_BY_NAME =
                "SELECT " + Fields.ALL_WITH_TABLE_NAME
                        + " FROM " + TABLE_NAME
                        + " WHERE " + Fields.NAME + " = :" + Fields.NAME;

        /**
         * The Constant SELECT_BY_USER_ID.
         */
        public static final String SELECT_BY_USER_ID =
                "SELECT " + Fields.ALL_WITH_TABLE_NAME
                        + " FROM " + TABLE_NAME
                        + " inner join s_user_s_role susr on s_role.id=susr.s_roles_id where susr.s_users_id = :" + Fields.S_USER_ID;


        /**
         * The Constant DELETE_BY_NAME.
         */
        public static final String DELETE_BY_ID =
                "DELETE FROM " + TABLE_NAME
                        + " WHERE " + Fields.ID + " = :" + Fields.ID;

        /**
         * The Constant CLEAN_RESOURCES.
         */
        public static final String CLEAN_RESOURCES =
                "DELETE FROM S_RIGHT_S_RESOURCE "
                        + "WHERE " + Fields.S_ROLE_ID + " = :" + Fields.S_ROLE_ID;
        /**
         * The Constant CLEAN_PROPERTIES.
         */
        public static final String CLEAN_PROPERTIES =
                "DELETE FROM s_role_property_value "
                        + "WHERE role_id = :" + Fields.S_ROLE_ID;
        /**
         * The Constant CLEAN_USERS.
         */
        public static final String CLEAN_USERS =
                "DELETE FROM s_user_s_role "
                        + "WHERE " + Fields.S_ROLES_ID + " = :" + Fields.S_ROLES_ID;

        /**
         * The Constant CLEAN_LABELS.
         */
        public static final String CLEAN_LABELS = "DELETE FROM s_role_s_label " + "WHERE " + Fields.S_ROLE_ID + " = :"
                + Fields.S_ROLE_ID;
        public static final String ATTACH_LABELS = "insert into s_role_s_label(s_role_id, s_label_id) values (:"
                + Fields.S_ROLE_ID + ", (select id from s_label where name=:" + Fields.NAME + "))";
        public static final String ATTACH_RESOURCE_RIGHTS = "insert into s_right_s_resource(" + Fields.S_ROLE_ID + ", " + Fields.S_RESOURCE_ID + ", " + Fields.S_RIGHT_ID + ") values (:"
                + Fields.S_ROLE_ID + ", :" + Fields.S_RESOURCE_ID + ", :" + Fields.S_RIGHT_ID + ")";
        public static final String INSERT_NEW =
                "INSERT INTO " + TABLE_NAME + "(" + Fields.ALL + ") VALUES (" + Fields.ALL_TO_INSERT + ")";

        /**
         * The Constant UPDATE_BY_NAME.
         */
        public static final String UPDATE_BY_NAME =
                "UPDATE " + TABLE_NAME
                        + " SET " + Fields.ALL_TO_UPDATE
                        + " WHERE " + Fields.NAME + " = :" + Fields.NAME;
    }

    /**
     * @return the properties
     */
    public List<RolePropertyValuePO> getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(List<RolePropertyValuePO> properties) {
        this.properties.clear();
        if (CollectionUtils.isNotEmpty(properties)) {
            this.properties.addAll(properties);
        }
    }

    public void addProperty(final RolePropertyValuePO rolePropertyValuePO) {
        if (rolePropertyValuePO != null) {
            this.properties.add(rolePropertyValuePO);
        }
    }
}