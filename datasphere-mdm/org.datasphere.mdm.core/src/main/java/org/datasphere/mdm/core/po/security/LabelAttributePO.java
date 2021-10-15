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
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

/**
 * The persistent class for the s_label_attribute database table.
 *
 * @author ilya.bykov
 */
public class LabelAttributePO extends BaseSecurityPO implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant TABLE_NAME. */
    public static final String TABLE_NAME = "s_label_attribute";
    /** The id. */
    private Integer id;

    /** The description. */
    private String description;

    /** The name. */
    private String name;

    /** The path. */
    private String path;

    /** The label attribute values. */
    // bi-directional many-to-one association to LabelAttributeValuePO
    private final List<LabelAttributeValuePO> labelAttributeValues = new ArrayList<>();

    /** The label. */
    // bi-directional many-to-one association to LabelPO
    private LabelPO label;

    /**
     * Instantiates a new label attribute po.
     */
    public LabelAttributePO() {
    }

    public LabelAttributePO(final Integer id, final String name, final String path, final String description) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.description = description;
    }

    public LabelAttributePO(Integer id) {
        this.id = id;
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
     * Gets the path.
     *
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Sets the path.
     *
     * @param path
     *            the new path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Sets the path.
     *
     * @param value
     *            the new path
     */
    public void setValue(String value) {
        this.path = value;
    }

    /**
     * Gets the label attribute values.
     *
     * @return the label attribute values
     */
    public List<LabelAttributeValuePO> getLabelAttributeValues() {
        return this.labelAttributeValues;
    }

    /**
     * Sets the label attribute values.
     *
     * @param labelAttributeValues
     *            the new label attribute values
     */
    public void setLabelAttributeValues(Collection<LabelAttributeValuePO> labelAttributeValues) {
        if (CollectionUtils.isEmpty(labelAttributeValues)) {
            return;
        }
        this.labelAttributeValues.clear();
        this.labelAttributeValues.addAll(labelAttributeValues);
    }

    /**
     * Adds the label attribute value.
     *
     * @param labelAttributeValue
     *            the label attribute value
     * @return the label attribute value po
     */
    public LabelAttributePO addLabelAttributeValue(LabelAttributeValuePO labelAttributeValue) {
        labelAttributeValues.add(labelAttributeValue);
        return this;
    }

    /**
     * Removes the label attribute po.
     *
     * @param labelAttributeValue
     *            the label attribute value
     * @return the label attribute value po
     */
    public LabelAttributeValuePO removeLabelAttributePO(LabelAttributeValuePO labelAttributeValue) {
        getLabelAttributeValues().remove(labelAttributeValue);
        labelAttributeValue.setLabelAttribute(null);

        return labelAttributeValue;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public LabelPO getLabel() {
        return this.label;
    }

    /**
     * Sets the label.
     *
     * @param label
     *            the new label
     */
    public void setLabel(LabelPO label) {
        this.label = label;
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

        /** The Constant S_LABEL_ID. */
        public static final String S_LABEL_ID = "S_LABEL_ID";

        /** The Constant VALUE. Use path instead */
        @Deprecated
        public static final String VALUE = "VALUE";

        /** The Constant DESCRIPTION. */
        public static final String DESCRIPTION = "DESCRIPTION";

        /** The Constant PATH. */
        public static final String PATH = "PATH";
        public static final String ALL_WITH_TABLE_NAME = String.join(
                DELIMETER,
                String.join(DOT, TABLE_NAME, ID),
                String.join(DOT, TABLE_NAME, NAME),
                String.join(DOT, TABLE_NAME, S_LABEL_ID),
                String.join(DOT, TABLE_NAME, VALUE),
                String.join(DOT, TABLE_NAME, PATH),
                String.join(DOT, TABLE_NAME, DESCRIPTION),
                String.join(DOT, TABLE_NAME, CREATED_AT),
                String.join(DOT, TABLE_NAME, UPDATED_AT),
                String.join(DOT, TABLE_NAME, CREATED_BY),
                String.join(DOT, TABLE_NAME, UPDATED_BY));
        /** All fields combined, used for update queries. */
        public static final String ALL_TO_UPDATE = String.join(
                DELIMETER,
                String.join(EQUALS,  NAME, NAME),
                String.join(EQUALS, S_LABEL_ID, S_LABEL_ID),
                String.join(EQUALS, VALUE, VALUE),
                String.join(EQUALS, PATH, PATH),
                String.join(EQUALS, DESCRIPTION, DESCRIPTION),
                String.join(EQUALS, CREATED_AT, CREATED_AT),
                String.join(EQUALS, UPDATED_AT, UPDATED_AT),
                String.join(EQUALS,  CREATED_BY, CREATED_BY),
                String.join(EQUALS, UPDATED_BY, UPDATED_BY));
        public static final String ALL_TO_INSERT = String.join(
                DELIMETER,
                String.join("",DOTS,  NAME),
                String.join("",DOTS,  S_LABEL_ID),
                String.join("",DOTS,  VALUE),
                String.join("",DOTS,  PATH),
                String.join("",DOTS,  DESCRIPTION),
                String.join("",DOTS,  CREATED_AT),
                String.join("",DOTS,  UPDATED_AT),
                String.join("",DOTS,  CREATED_BY),
                String.join("",DOTS,  UPDATED_BY));
        /** All fields combined. */
        public static final String ALL = String.join(
                DELIMETER,
                NAME,
                S_LABEL_ID,
                VALUE,
                PATH,
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

        /** The Constant SELECT_BY_USER_ID. */
        public static final String SELECT_BY_NAME = "SELECT " + Fields.ALL_WITH_TABLE_NAME + " FROM " + TABLE_NAME + " WHERE "
                + Fields.NAME + " = :" + Fields.NAME;
        public static final String DELETE_BY_NAME =
                "DELETE FROM "+TABLE_NAME
                        + " WHERE "+Fields.NAME+" = :"+Fields.NAME;
        public static final String DELETE_BY_ID =
                "DELETE FROM "+TABLE_NAME
                        + " WHERE "+Fields.ID+" = :"+Fields.ID;
        /** The Constant SELECT_BY_LABEL_ID. */
        public static final String SELECT_BY_LABEL_ID = "SELECT " + Fields.ALL_WITH_TABLE_NAME + " FROM " + TABLE_NAME + " WHERE "
                + Fields.S_LABEL_ID + " = :" + Fields.S_LABEL_ID;

        /** The Constant SELECT_BY_ID. */
        public static final String SELECT_BY_ID = "SELECT " + Fields.ALL_WITH_TABLE_NAME + " FROM " + TABLE_NAME + " WHERE " + Fields.ID
                + " = :" + Fields.ID;
        public static final String INSERT_NEW =
                "INSERT INTO "+TABLE_NAME+"("+Fields.ALL+") VALUES ("+Fields.ALL_TO_INSERT+")";
        /** The Constant UPDATE_BY_NAME. */
        public static final String UPDATE_BY_NAME =
                "UPDATE " +TABLE_NAME
                        + " SET "+ Fields.ALL_TO_UPDATE
                        + " WHERE " + Fields.NAME + " = :" + Fields.NAME;
        public static final String UPDATE_BY_ID =
                "UPDATE " +TABLE_NAME
                        + " SET "+ Fields.ALL_TO_UPDATE
                        + " WHERE " + Fields.ID + " = :" + Fields.ID;
    }
}