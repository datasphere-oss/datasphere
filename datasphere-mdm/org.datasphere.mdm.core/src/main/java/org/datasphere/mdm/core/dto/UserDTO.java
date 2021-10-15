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

package org.datasphere.mdm.core.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.datasphere.mdm.core.type.security.CustomProperty;
import org.datasphere.mdm.core.type.security.Endpoint;
import org.datasphere.mdm.core.type.security.Right;
import org.datasphere.mdm.core.type.security.Role;
import org.datasphere.mdm.core.type.security.SecurityLabel;

/**
 * The Class UserRO.
 * @author ilya.bykov
 */
public class UserDTO extends BaseSecurityDTO {

    /**
     * SVUID.
     */
    private static final long serialVersionUID = 1179950904997430308L;

    /** The login. */
    private String login;

    /** The first name. */
    private String firstName;

    /** The last name. */
    private String lastName;

    /** The full name. */
    private String fullName;

    /** The admin. */
    private boolean admin;

    /**  Is active. */
    private boolean active;

    /** The email. */
    private String email;

    /** Locale. */
    private Locale locale;

    /** The roles. */
    private transient List<Role> roles;

    /** The roles. */
    private transient List<Right> rights;

    /** The security labels. */
    private transient List<SecurityLabel> securityLabels;

    /** The properties. */
    private transient List<UserPropertyDTO> properties;

    /** The apis. */
    private List<Endpoint> endpoints;
    /**
     * User external mark.
     */
    private boolean external;
    /**
     * User external authentication/authorization/profile source name.
     */
    private String securityDataSource;
    /**
     * The email notification.
     * */
    private Boolean emailNotification;

    /**
     * Gets the login.
     *
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
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
     * @param active the new active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
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
     * Gets the full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name.
     *
     * @param fullName
     *            the new full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Checks if is admin.
     *
     * @return true, if is admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Sets the admin.
     *
     * @param admin
     *            the new admin
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * Sets the login.
     *
     * @param login
     *            the new login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
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
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Gets the roles.
     *
     * @return the roles
     */
    public List<Role> getRoles() {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        return roles;
    }

    /**
     * Sets the roles.
     *
     * @param roles
     *            the new roles
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * Adds the role.
     *
     * @param role
     *            the role
     */
    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(role);
    }

    /**
     * Adds the roles.
     *
     * @param roles
     *            the roles
     */
    public void addRoles(List<Role> roles) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.addAll(roles);
    }

    /**
     * Gets the rights.
     *
     * @return the rights
     */
    public List<Right> getRights() {
        if (this.rights == null) {
            this.rights = new ArrayList<>();
        }
        return rights;
    }

    /**
     * Sets the rights.
     *
     * @param rights
     *            the new rights
     */
    public void setRights(List<Right> rights) {
        this.rights = rights;
    }

    /**
     * Gets the security labels.
     *
     * @return the security labels
     */
    public List<SecurityLabel> getSecurityLabels() {
        return securityLabels;
    }

    /**
     * Sets the security labels.
     *
     * @param securityLabels the new security labels
     */
    public void setSecurityLabels(final List<SecurityLabel> securityLabels) {
        this.securityLabels = securityLabels;
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public List<UserPropertyDTO> getProperties() {
        return properties;
    }


    /**
     * Sets the properties.
     *
     * @param properties the new properties
     */
    public void setProperties(List<UserPropertyDTO> properties) {
        this.properties = properties;
    }

    /**
     * Whether user has external authentication/authorization/profile.
     *
     * @return true, if is external
     */
    public boolean isExternal() {
        return external;
    }

    /**
     * Sets user external authentication/authorization/profile source.
     * @param external    external parameter.
     */
    public void setExternal(boolean external) {
        this.external = external;
    }

    /**
     * Get user authentication provider source.
     * @return Authentication provider source.
     */
    public String getSecurityDataSource() {
        return securityDataSource;
    }

    /**
     * Set authentication provider source.
     * @param source    authentication provider source.
     */
    public void setSecurityDataSource(String source) {
        this.securityDataSource = source;
    }

    /**
     * Gets the custom properties.
     *
     * @return the customProperties
     */
    public List<CustomProperty> getCustomProperties() {
        return Objects.isNull(properties) ? Collections.emptyList() : Collections.unmodifiableList(properties);
    }

    /**
     * Gets the apis.
     *
     * @return the apis
     */
    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    /**
     * Sets the apis.
     *
     * @param endpoints the new apis
     */
    public void setEnpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    /**
     * Gets the emailNotification.
     *
     * @return the emailNotification
     */
    public Boolean isEmailNotification() {
        return emailNotification;
    }
    /**
     * Sets the emailNotification.
     *
     * @param emailNotification
     */
    public void setEmailNotification(Boolean emailNotification) {
        this.emailNotification = emailNotification;
    }
}
