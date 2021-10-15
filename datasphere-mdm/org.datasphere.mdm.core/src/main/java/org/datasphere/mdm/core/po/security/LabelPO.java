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
 * The persistent class for the s_label database table.
 *
 * @author ilya.bykov
 */

public class LabelPO extends BaseSecurityPO implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    /**
     * Table name
     */
    public static final String TABLE_NAME = "s_label";
    /**
     * id field
     */
    private Integer id;

    /** The description. */
    private String description;

    /** The display name. */
    private String displayName;

    /** The name. */
    private String name;

    /** The label attribute. */
    // bi-directional many-to-one association to LabelAttributePO
    private final List<LabelAttributePO> labelAttribute = new ArrayList<>();

    /** The roles. */
    private List<RolePO> roles;

    /**
     * Instantiates a new label po.
     */
    public LabelPO() {
    }

    public LabelPO(int id) {
        this.id=id;
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
     * @param displayName
     *            the new display name
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
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the label attribute.
     *
     * @return the label attribute
     */
    public List<LabelAttributePO> getLabelAttribute() {
        return Collections.unmodifiableList(this.labelAttribute);
    }

    /**
     * Sets the label attribute po.
     *
     * @param labelAttribute
     *            the new label attribute po
     */
    public void setLabelAttributePO(List<LabelAttributePO> labelAttribute) {
        if (CollectionUtils.isEmpty(labelAttribute)) {
            return;
        }
        this.labelAttribute.clear();
        this.labelAttribute.addAll(labelAttribute);
    }

    /**
     * Adds the label attribute po.
     *
     * @param labelAttribute
     *            the label attribute
     * @return the label attribute po
     */
    public LabelAttributePO addLabelAttributePO(LabelAttributePO labelAttribute) {
        this.labelAttribute.add(labelAttribute);
        labelAttribute.setLabel(this);

        return labelAttribute;
    }

    /**
     * Removes the label attribute.
     *
     * @param labelAttribute
     *            the label attribute
     * @return the label attribute po
     */
    public LabelAttributePO removeLabelAttribute(LabelAttributePO labelAttribute) {
        getLabelAttribute().remove(labelAttribute);
        labelAttribute.setLabel(null);

        return labelAttribute;
    }

    /**
     * Gets the roles.
     *
     * @return the roles
     */
    public List<RolePO> getRoles() {
        if (this.roles == null) {
            this.roles = new ArrayList<RolePO>();
        }
        return this.roles;
    }

    /**
     * Sets the roles.
     *
     * @param roles
     *            the new roles
     */
    public void setRoles(List<RolePO> roles) {
        this.roles = roles;
    }

    /**
     * The Class Fields.
     */
    public static final class Fields extends BaseSecurityPO.Fields {

        /**
         * Instantiates a new fields.
         */
        private Fields() {
            super();
        }

        /** The Constant NAME. */
        public static final String NAME = "NAME";
        public static final String S_LABEL_ID = "S_LABEL_ID";
        /** The Constant DISPLAY_NAME. */
        public static final String DISPLAY_NAME = "DISPLAY_NAME";
        /**
         * Role name.
         */
        public static final String ROLE_NAME = "ROLE_NAME";
        /** The Constant DESCRIPTION. */
        public static final String DESCRIPTION = "DESCRIPTION";
        /** All fields combined */
        public static final String ALL = String.join(DELIMETER, NAME, DISPLAY_NAME, DESCRIPTION, CREATED_AT,
                UPDATED_AT, CREATED_BY, UPDATED_BY);
        public static final String ALL_WITH_TABLE_NAME = String.join(
                DELIMETER,
                String.join(DOT, TABLE_NAME, ID),
                String.join(DOT, TABLE_NAME, NAME),
                String.join(DOT, TABLE_NAME, DISPLAY_NAME),
                String.join(DOT, TABLE_NAME, DESCRIPTION),
                String.join(DOT, TABLE_NAME, CREATED_AT),
                String.join(DOT, TABLE_NAME, UPDATED_AT),
                String.join(DOT, TABLE_NAME, CREATED_BY),
                String.join(DOT, TABLE_NAME, UPDATED_BY));
        /** All fields combined, used for update queries. */
        public static final String ALL_TO_UPDATE = String.join(
                DELIMETER,
                String.join(EQUALS,  NAME, NAME),
                String.join(EQUALS, DISPLAY_NAME, DISPLAY_NAME),
                String.join(EQUALS, DESCRIPTION, DESCRIPTION),
                String.join(EQUALS, CREATED_AT, CREATED_AT),
                String.join(EQUALS, UPDATED_AT, UPDATED_AT),
                String.join(EQUALS,  CREATED_BY, CREATED_BY),
                String.join(EQUALS, UPDATED_BY, UPDATED_BY));
        public static final String ALL_TO_INSERT = String.join(
                DELIMETER,
                String.join("",DOTS,  NAME),
                String.join("",DOTS,  DISPLAY_NAME),
                String.join("",DOTS,  DESCRIPTION),
                String.join("",DOTS,  CREATED_AT),
                String.join("",DOTS,  UPDATED_AT),
                String.join("",DOTS,  CREATED_BY),
                String.join("",DOTS,  UPDATED_BY));

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
        public static final String SELECT_BY_NAME = "SELECT " + Fields.ALL_WITH_TABLE_NAME + " FROM " + TABLE_NAME + " WHERE "
                + Fields.NAME + " = :" + Fields.NAME;
        /** The Constant SELECT_BY_ROLE_NAME. */
        public static final String SELECT_BY_ROLE_NAME = "SELECT " + Fields.ALL_WITH_TABLE_NAME + " FROM " + TABLE_NAME + " WHERE "
                + Fields.ID + " IN (select s_label_id from s_role_s_label where s_role_id=(select id from s_role where name=:"+Fields.ROLE_NAME+"))";
        public static final String SELECT_ALL = "SELECT " + Fields.ALL_WITH_TABLE_NAME + " FROM " + TABLE_NAME;
        /** The Constant DELETE_BY_NAME. */
        public static final String DELETE_BY_ID =
                "DELETE FROM "+TABLE_NAME
                        + " WHERE "+Fields.ID+" = :"+Fields.ID;
        /** The Constant CLEAN_LABELS. */
        public static final String CLEAN_ROLE_LABELS =
                "DELETE FROM s_role_s_label "
                        + "WHERE "+ Fields.S_LABEL_ID + " = :" + Fields.S_LABEL_ID;
        public static final String CLEAN_LABEL_ATTRIBUTES =
                "DELETE FROM s_label_attribute "
                        + "WHERE "+ Fields.ID + " IN(select sla.id from s_label_attribute sla "
                        + "inner join s_label sl on sla.s_label_id=sl.id where sl.id= :"+Fields.ID+")";
        public static final String CLEAN_LABEL_ATTRIBUTE_VALUES =
                "DELETE FROM s_label_attribute_value "
                        + "WHERE "+ Fields.ID + " IN(select slav.id from s_label_attribute_value slav "
                        + "inner join s_label_attribute sla on sla.id=slav.s_label_attribute_id "
                        + "inner join s_label sl on sla.s_label_id=sl.id where sl.id= :"+Fields.ID+")";
        public static final String INSERT_NEW =
                "INSERT INTO "+TABLE_NAME+"("+Fields.ALL+") VALUES ("+Fields.ALL_TO_INSERT+")";
        /** The Constant UPDATE_BY_NAME. */
        public static final String UPDATE_BY_NAME =
                "UPDATE " +TABLE_NAME
                        + " SET "+ Fields.ALL_TO_UPDATE
                        + " WHERE " + Fields.NAME + " = :" + Fields.NAME;
    }
}