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
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;


/**
 * The persistent class for the s_user database table.
 *
 * @author ilya.bykov
 */
public class UserPO extends BaseUserPO implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant TABLE_NAME. */
    public static final String TABLE_NAME = "s_user";

    /** The auth type. */
    private String authType;
    /** The email. */
    private String email;

    /** The locale. */
    private String locale;
    /** The first name. */
    private String firstName;

    /** The last name. */
    private String lastName;

    /** The notes. */
    private String notes;

    /** The active. */
    private boolean active;
    /** The admin. */
    private boolean admin;

    /**
     * User external provider source name.
     */
    private String source;

    /**
     * External user mark.
     */
    private boolean external;

    // bi-directional one-to-one association to PasswordPO
    /** The S passwords. */
    private List<PasswordPO> password;
    /**
     * Email notification
     */
    private Boolean emailNotification;

    // bi-directional many-to-many association to RolePO
    /** The S roles. */
    private final List<RolePO> roles = new ArrayList<>();

    /** The apis. */
    private final List<ApiPO> apis = new ArrayList<>();

    /** The label attribute values. */
    private final List<LabelAttributeValuePO> labelAttributeValues = new ArrayList<>();
    /**
     * Property values.
     */
    private final transient List<UserPropertyValuePO> properties = new ArrayList<>();
    /**
     * Instantiates a new user po.
     */
    public UserPO() {
    }

    /**
     * Gets the auth type.
     *
     * @return the auth type
     */
    public String getAuthType() {
        return this.authType;
    }

    /**
     * Sets the auth type.
     *
     * @param authType
     *            the new auth type
     */
    public void setAuthType(String authType) {
        this.authType = authType;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets the email.
     *
     * @param email
     *            the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName
     *            the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName
     *            the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the notes.
     *
     * @return the notes
     */
    public String getNotes() {
        return this.notes;
    }

    /**
     * Sets the notes.
     *
     * @param notes
     *            the new notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the s passwords.
     *
     * @return the s passwords
     */
    public List<PasswordPO> getPassword() {
        if (this.password == null) {
            this.password = new ArrayList<>();
        }
        return this.password;
    }

    /**
     * Sets the s passwords.
     *
     * @param password
     *            the new s passwords
     */
    public void setPassword(List<PasswordPO> password) {
        this.password = password;
    }

    /**
     * Checks if is admin.
     *
     * @return the admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Sets the admin.
     *
     * @param admin
     *            the admin to set
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active.
     *
     * @param active
     *            the new active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Get user external provider source name.
     * @return external provider source name.
     */
    public String getSource() {
        return source;
    }

    /**
     * Set user external provider source name,.
     *
     * @param source    external provider source name,
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Checks user external mark.
     * @return user external mark.
     */
    public boolean isExternal() {
        return external;
    }

    /**
     * Sets user external mark.
     * @param external    user exetrnal mark.
     */
    public void setExternal(boolean external) {
        this.external = external;
    }

    /**
     * Gets the s roles.
     *
     * @return the s roles
     */
    public List<RolePO> getRoles() {
        return this.roles;
    }

    /**
     * Sets the s roles.
     *
     * @param roles
     *            the new s roles
     */
    public void setRoles(List<RolePO> roles) {
        this.roles.clear();
        if (CollectionUtils.isNotEmpty(roles)) {
            this.roles.addAll(roles);
        }
    }

    public void addRole(final RolePO role) {
        this.roles.add(role);
    }

    /**
     * Gets the label attribute values.
     *
     * @return the label attribute values
     */
    public List<LabelAttributeValuePO> getLabelAttributeValues() {
        return labelAttributeValues;
    }

    /**
     * Sets the label attribute values.
     *
     * @param labelAttributeValues
     *            the new label attribute values
     */
    public void setLabelAttributeValues(List<LabelAttributeValuePO> labelAttributeValues) {
        this.labelAttributeValues.clear();
        if (CollectionUtils.isNotEmpty(labelAttributeValues)) {
            this.labelAttributeValues.addAll(labelAttributeValues);
        }
    }

    /**
     * Adds the label attribute value.
     *
     * @param labelAttributeValue
     *            the label attribute value
     * @return the label attribute value po
     */
    public LabelAttributeValuePO addLabelAttributeValue(LabelAttributeValuePO labelAttributeValue) {
        getLabelAttributeValues().add(labelAttributeValue);

        return labelAttributeValue;
    }

    /**
     * Removes the label attribute value.
     *
     * @param labelAttributeValue
     *            the label attribute value
     * @return the label attribute value po
     */
    public LabelAttributeValuePO removeLabelAttributeValue(LabelAttributeValuePO labelAttributeValue) {
        getLabelAttributeValues().remove(labelAttributeValue);

        return labelAttributeValue;
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public List<UserPropertyValuePO> getProperties() {
        return properties;
    }

    /**
     * Sets the properties.
     *
     * @param properties the properties to set
     */
    public void setProperties(List<UserPropertyValuePO> properties) {
        this.properties.clear();
        if (CollectionUtils.isNotEmpty(properties)) {
            this.properties.addAll(properties);
        }
    }

    public void addProperty(UserPropertyValuePO property) {
        properties.add(property);
    }

    /**
     * Gets the apis.
     *
     * @return the apis
     */
    public List<ApiPO> getApis() {
        return apis;
    }

    /**
     * Sets the apis.
     *
     * @param apis the new apis
     */
    public void setApis(List<ApiPO> apis) {
        this.apis.clear();
        if (CollectionUtils.isNotEmpty(apis)) {
            this.apis.addAll(apis);
        }

    }

    public void addApi(ApiPO apiPO) {
        this.apis.add(apiPO);
    }
    /**
     * @return email notification flag
     */
    public Boolean isEmailNotification() {
        return emailNotification;
    }
    /**
     * @set email notification flag
     */
    public void setEmailNotification(Boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    /**
     * The Class Fields.
     */
    public static final class Fields extends BaseUserPO.Fields  {

        /**
         * Instantiates a new fields.
         */
        private Fields() {
            super();
        }

        /** The Constant S_USERS_ID. */
        public static final String S_USERS_ID = "S_USERS_ID";

        /** The Constant S_USER_ID. */
        public static final String S_USER_ID = "S_USER_ID";
        /** The Constant EMAIL. */
        public static final String EMAIL = "EMAIL";
        /** The Constant LOCALE. */
        public static final String LOCALE = "LOCALE";

        /** The Constant FIRST_NAME. */
        public static final String FIRST_NAME = "FIRST_NAME";

        /** The Constant LAST_NAME. */
        public static final String LAST_NAME = "LAST_NAME";

        /** The Constant NOTES. */
        public static final String NOTES = "NOTES";

        /** The Constant ACTIVE. */
        public static final String ACTIVE = "ACTIVE";

        /** The Constant ADMIN. */
        public static final String ADMIN = "ADMIN";

        /**
         * User external provider source.
         */
        public static final String SOURCE = "SOURCE";

        /**
         * User external mark.
         */
        public static final String EXTERNAL = "EXTERNAL";

        /**
         * Email notification.
         */
        public static final String EMAIL_NOTIFICATION = "EMAIL_NOTIFICATION";

        /**
         * Combined fields.
         */
        public static final String ALL = String.join(DELIMETER,   CREATED_AT,  CREATED_BY,
                LOGIN, EMAIL, LOCALE, FIRST_NAME, LAST_NAME, NOTES, ACTIVE, ADMIN, SOURCE, EXTERNAL, EMAIL_NOTIFICATION);
        /** All fields combined. */
        public static final String ALL_WITH_TABLE_NAME = String.join(
                DELIMETER,
                String.join(DOT, TABLE_NAME, ID),
                String.join(DOT, TABLE_NAME, LOGIN),
                String.join(DOT, TABLE_NAME, EMAIL),
                String.join(DOT, TABLE_NAME, LOCALE),
                String.join(DOT, TABLE_NAME, FIRST_NAME),
                String.join(DOT, TABLE_NAME, LAST_NAME),
                String.join(DOT, TABLE_NAME, NOTES),
                String.join(DOT, TABLE_NAME, ACTIVE),
                String.join(DOT, TABLE_NAME, ADMIN),
                String.join(DOT, TABLE_NAME, SOURCE),
                String.join(DOT, TABLE_NAME, EXTERNAL),
                String.join(DOT, TABLE_NAME, EMAIL_NOTIFICATION),
                String.join(DOT, TABLE_NAME, CREATED_AT),
                String.join(DOT, TABLE_NAME, UPDATED_AT),
                String.join(DOT, TABLE_NAME, CREATED_BY),
                String.join(DOT, TABLE_NAME, UPDATED_BY));
        /** All fields combined, used for update queries. */
        public static final String ALL_TO_UPDATE = String.join(
                DELIMETER,
                String.join(EQUALS, LOGIN, LOGIN),
                String.join(EQUALS, EMAIL, EMAIL),
                String.join(EQUALS, LOCALE, LOCALE),
                String.join(EQUALS, FIRST_NAME, FIRST_NAME),
                String.join(EQUALS, LAST_NAME, LAST_NAME),
                String.join(EQUALS, NOTES, NOTES),
                String.join(EQUALS, ACTIVE, ACTIVE),
                String.join(EQUALS, ADMIN, ADMIN),
                String.join(EQUALS, SOURCE, SOURCE),
                String.join(EQUALS, EXTERNAL, EXTERNAL),
                String.join(EQUALS, EMAIL_NOTIFICATION, EMAIL_NOTIFICATION),
                String.join(EQUALS, UPDATED_AT, UPDATED_AT),
                String.join(EQUALS, UPDATED_BY, UPDATED_BY));
        /** The Constant ALL_TO_INSERT. */
        public static final String ALL_TO_INSERT = String.join(
                DELIMETER,
                String.join("", DOTS, CREATED_AT),
                String.join("", DOTS, CREATED_BY),
                String.join("", DOTS, LOGIN),
                String.join("", DOTS, EMAIL),
                String.join("", DOTS, LOCALE),
                String.join("", DOTS, FIRST_NAME),
                String.join("", DOTS, LAST_NAME),
                String.join("", DOTS, NOTES),
                String.join("", DOTS, ACTIVE),
                String.join("", DOTS, ADMIN),
                String.join("", DOTS, SOURCE),
                String.join("", DOTS, EXTERNAL),
                String.join("", DOTS, EMAIL_NOTIFICATION));
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
        /** The Constant CLEAN_USERS. */
        public static final String CLEAN_USERS =
                "DELETE FROM s_user_s_role "
                        + "WHERE "+ Fields.S_USERS_ID + " = :" + Fields.S_USERS_ID;

        /** The Constant SELECT_BY_LOGIN. */
        public static final String SELECT_BY_LOGIN = "select " + Fields.ALL_WITH_TABLE_NAME +   " FROM " + TABLE_NAME +" where " + Fields.LOGIN + " = :"
                + Fields.LOGIN;
        /** The Constant SELECT_BY_EMAIL_SOURCE. */
        public static final String SELECT_BY_EMAIL_SOURCE = "select " + Fields.ALL_WITH_TABLE_NAME +   " FROM " + TABLE_NAME +" where " + Fields.EMAIL + " = :"
                + Fields.EMAIL + " and " + Fields.SOURCE + " = :" + Fields.SOURCE;

        /** The Constant ATTACH_LABELS. */
        public static final String ATTACH_LABELS = "insert into s_label_attribute_value(s_user_id, value, s_label_attribute_id, s_label_group) "
                + "values(?,?,(select sla.id from s_label_attribute sla where sla.name=? "
                + "and sla.s_label_id = (select id from s_label where name=?)), ?)";

        /** The Constant ATTACH_TO_ROLE. */
        public static final String ATTACH_TO_ROLE =
                "INSERT INTO s_user_s_role(s_users_id, s_roles_id) VALUES (:"+Fields.S_USERS_ID+",(select id from s_role where name=:"+RolePO.Fields.NAME+"))";

        /** The Constant ATTACH_TO_ROLE_BY_LOGIN. */
        public static final String ATTACH_TO_ROLE_BY_LOGIN =
                "INSERT INTO s_user_s_role(s_users_id, s_roles_id) VALUES (" +
                        "(select " + Fields.ID + " from " + TABLE_NAME + " where " + Fields.LOGIN + " = :" + Fields.LOGIN + ")," +
                        "(select id from s_role where name=:" + RolePO.Fields.NAME + ")" +
                        ")";

        /** The Constant ATTACH_TO_API. */
        public static final String ATTACH_TO_API =
                "INSERT INTO s_user_s_apis(s_user_id, s_api_id, created_at, updated_at, created_by, updated_by) "
                        + "SELECT su.id, sa.id, current_date, current_date, 'SYSTEM',  'SYSTEM' FROM "
                        + "(SELECT id FROM s_user WHERE "+Fields.LOGIN+"=:"+Fields.LOGIN+" ) su, (SELECT id FROM s_apis WHERE name IN (:api_names)) sa";

        /** The Constant DETACH_FROM_API. */
        public static final String DETACH_FROM_API =
                "DELETE FROM s_user_s_apis WHERE s_user_id IN (SELECT id FROM s_user WHERE "
                        + Fields.LOGIN + "=:" + Fields.LOGIN + " )";

        /** The Constant INSERT_NEW. */
        public static final String INSERT_NEW =
                "INSERT INTO " + TABLE_NAME + "("+Fields.ALL+") VALUES (" + Fields.ALL_TO_INSERT + ")";

        /** The Constant UPDATE_BY_NAME. */
        public static final String UPDATE_BY_ID =
                "UPDATE " + TABLE_NAME
                        + " SET " + Fields.ALL_TO_UPDATE
                        + " WHERE " + Fields.ID + " = :" + Fields.ID;
        /** The Constant CHANGE_LOCALE_BY_NAME. */
        public static final String CHANGE_LOCALE_BY_ID =
                "UPDATE " + TABLE_NAME
                        + " SET " + Fields.LOCALE + " = :" + Fields.LOCALE
                        + " WHERE " + Fields.ID + " = :" + Fields.ID;
    }
}