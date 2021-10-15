/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package com.huahui.datasphere.platform.rest.core.ro;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.huahui.datasphere.rest.system.ro.security.BaseSecurityRO;
import com.huahui.datasphere.rest.system.ro.security.RoleRO;
import com.huahui.datasphere.rest.system.ro.security.SecurityLabelRO;


/**
 * The Class UserRO.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRO extends BaseSecurityRO {

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

    /** Is active. */
    private boolean active;

    /** The email. */
    private String email;
    /** The locale. */
    private String locale;

    /** The roles names. */
    private List<String> roles;

    /** The roles data. */
    private List<RoleRO> rolesData;

    /** The security labels. */
    private List<SecurityLabelRO> securityLabels;
    /**
     * User properties.
     */
    private List<UserPropertyRO> properties;

    /** The apis. */
    private List<UserAPIRO> endpoints;
    /**
     * External authentication flag.
     */
    private boolean external;
    /**
     * Security data source.
     */
    private String securityDataSource;
    /**
     * The email notification.
     * */
    private boolean emailNotification;
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
     * @param active
     *            the new active
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
     * Gets the roles.
     *
     * @return the roles
     */
    public List<String> getRoles() {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        return roles;
    }

    /**
     * Sets the roles.
     *
     * @param roles
     *            the new roles
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * Adds the role.
     *
     * @param role
     *            the role
     */
    public void addRole(String role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }

    /**
     * Adds the roles.
     *
     * @param roles
     *            the roles
     */
    public void addRoles(List<String> roles) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.addAll(roles);
    }

    public List<RoleRO> getRolesData() {
        if (rolesData == null) {
            rolesData = new ArrayList<>();
        }
        return rolesData;
    }

    public void setRolesData(List<RoleRO> rolesData) {
        this.rolesData = rolesData;
    }

    /**
     * Gets the security labels.
     *
     * @return the securityLabels
     */
    public List<SecurityLabelRO> getSecurityLabels() {
        return securityLabels;
    }

    /**
     * Sets the security labels.
     *
     * @param securityLabels
     *            the securityLabels to set
     */
    public void setSecurityLabels(List<SecurityLabelRO> securityLabels) {
        this.securityLabels = securityLabels;
    }

    /**
     * Adds the security label.
     *
     * @param securityLabelRO
     *            the security label ro
     */
    public void addSecurityLabel(SecurityLabelRO securityLabelRO) {
        if (securityLabels == null) {
            securityLabels = new ArrayList<>();
        }
        securityLabels.add(securityLabelRO);
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public List<UserPropertyRO> getProperties() {
        if (properties == null) {
            properties = new ArrayList<>();
        }

        return properties;
    }

    /**
     * Sets the properties.
     *
     * @param properties the new properties
     */
    public void setProperties(List<UserPropertyRO> properties) {
        this.properties = properties;
    }

    /**
     * Adds the property.
     *
     * @param property the property
     */
    public void addProperty(UserPropertyRO property) {
        if (properties == null) {
            properties = new ArrayList<>();
        }
        properties.add(property);
    }
    /**
     * Returns external user marker flag.
     * @return true, if external, false otherwise
     */
    public boolean isExternal() {
        return external;
    }
    /**
     * Sets external flag.
     * @param external the flag
     */
    public void setExternal(boolean external) {
        this.external = external;
    }

    /**
     * Gets the security data source.
     *
     * @return the source
     */
    public String getSecurityDataSource() {
        return securityDataSource;
    }

    /**
     * Sets the security data source.
     *
     * @param source the source to set
     */
    public void setSecurityDataSource(String source) {
        this.securityDataSource = source;
    }

	/**
	 * Gets the apis.
	 *
	 * @return the apis
	 */
	public List<UserAPIRO> getEndpoints() {
		return endpoints;
	}

	/**
	 * Sets the apis.
	 *
	 * @param apis the new apis
	 */
	public void setEndpoints(List<UserAPIRO> apis) {
		this.endpoints = apis;
	}

    /**
     * Gets the emailNotification.
     *
     * @return the emailNotification
     */
    public boolean isEmailNotification() {
        return emailNotification;
    }
    /**
     * Sets the emailNotification.
     *
     * @param emailNotification
     */
    public void setEmailNotification(boolean emailNotification) {
        this.emailNotification = emailNotification;
    }
}
