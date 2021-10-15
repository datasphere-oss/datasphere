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

package org.datasphere.mdm.core.type.security;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


/**
 * UserInfo adapter class.
 *
 * @author Denis Kostovarov
 */
public abstract class AbstractUserInfo implements User, Serializable {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = -3808483534222562205L;
    /**
     * Login name.
     */
    private String name;
    /**
     * Email field.
     */
    private String email;
    /**
     * Locale field.
     */
    private Locale locale;
    /**
     * Rights list.
     */
    private List<Right> rights;
    /**
     * Roles list.
     */
    private List<Role> roles;
    /**
     * Labels list.
     */
    private List<SecurityLabel> labels;
    /**
     * Custom properties.
     */
    private List<CustomProperty> customProperties;
    /**
     * Token string.
     */
    private String customToken;
    /**
     * Password update time stamp.
     */
    private Date passwordUpdatedAt;
    /**
     * Password updated by.
     */
    private String passwordUpdatedBy;
    /**
     * Security data source (collection of security providers) name.
     */
    private String securityDataSource;
    /**
     * Has authorization from security data source or not.
     */
    private boolean hasAuthorization;
    /**
     * Has profile from security data source or not.
     */
    private boolean hasProfile;
    /**
     * Is admin user or not.
     */
    private boolean admin;
    /**
     * Force password change flag.
     */
    private boolean forcePasswordChangeFlag;
    /**
     * Time stamp of the last update.
     */
    private Date updatedAt;
    /**
     * Email notification
     */
    private Boolean emailNotification;
    /**
     * Constructor.
     */
    public AbstractUserInfo() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }
    /**
     * Sets name.
     * @param name the name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmail() {
        return email;
    }
    /**
     * Sets email.
     * @param email the email
     */
    @Override
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Locale getLocale() {
        return locale;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getCustomToken() {
        return customToken;
    }
    /**
     * Sets custom token.
     * @param customToken the token
     */
    @Override
    public void setCustomToken(String customToken) {
        this.customToken = customToken;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Date getPasswordUpdatedAt() {
        return passwordUpdatedAt;
    }
    /**
     * Sets password update time stamp.
     * @param passwordUpdatedAt time stamp
     */
    @Override
    public void setPasswordUpdatedAt(Date passwordUpdatedAt) {
        this.passwordUpdatedAt = passwordUpdatedAt;
    }
    /**
     * Gets the password updated by.
     *
     * @return the password updated by
     */
    public String getPasswordUpdatedBy() {
    	return passwordUpdatedBy;
    }
    /**
     * Sets the password updated by.
     *
     * @param passwordUpdatedBy the new password updated by
     */
    public void setPasswordUpdatedBy(String passwordUpdatedBy) {
    	this.passwordUpdatedBy = passwordUpdatedBy;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getForcePasswordChangeFlag() {
        return forcePasswordChangeFlag;
    }
    /**
     * Sets password change flag.
     * @param forcePasswordChangeFlag the flag
     */
    @Override
    public void setForcePasswordChangeFlag(boolean forcePasswordChangeFlag) {
        this.forcePasswordChangeFlag = forcePasswordChangeFlag;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getSecurityDataSource() {
        return securityDataSource;
    }
    /**
     * Sets security data source.
     * @param source the source
     */
    @Override
    public void setSecurityDataSource(String source) {
        this.securityDataSource = source;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }
    /**
     * Sets last update time stamp.
     * @param lastUpdated the time stamp
     */
    @Override
    public void setUpdatedAt(Date lastUpdated) {
        this.updatedAt = lastUpdated;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAuthorization() {
        return hasAuthorization;
    }
    /**
     * Sets authorization flag.
     * @param hasAuthorization the flag
     */
    @Override
    public void setHasAuthorization(boolean hasAuthorization) {
        this.hasAuthorization = hasAuthorization;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasProfile() {
        return hasProfile;
    }
    /**
     * Sets profile flag.
     * @param hasProfile the flag
     */
    @Override
    public void setHasProfile(boolean hasProfile) {
        this.hasProfile = hasProfile;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAdmin() {
        return admin;
    }
    /**
     * Sets admin flag.
     * @param admin the flag
     */
    @Override
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Right> getRights() {
        return Objects.isNull(rights) ? Collections.emptyList() : Collections.unmodifiableList(rights);
    }
    /**
     * Sets rights.
     * @param rights the rights
     */
    public void setRights(final List<Right> rights) {
        this.rights = rights;
    }
    /**
     * Gets the roles.
     *
     * @return the roles
     */
    @Override
    public List<Role> getRoles() {
        return Objects.isNull(roles) ? Collections.emptyList() : Collections.unmodifiableList(roles);
    }
    /**
     * Sets the roles.
     *
     * @param roles the roles to set
     */
    @Override
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    /**
     * Get security labels for this user.
     *
     * @return list with security labels for this user, if any configured.
     */
    @Override
    public List<SecurityLabel> getLabels() {
        return Objects.isNull(labels) ? Collections.emptyList() : Collections.unmodifiableList(labels);
    }
    /**
     * Set security labels for this user.
     *
     * @param labels
     *            list with security labels for this user.
     */
    @Override
    public void setLabels(List<SecurityLabel> labels) {
        this.labels = labels;
    }
    /**
     * @return the customProperties
     */
    @Override
    public List<CustomProperty> getCustomProperties() {
        return Objects.isNull(customProperties) ? Collections.emptyList() : Collections.unmodifiableList(customProperties);
    }
    /**
     * @param customProperties the customProperties to set
     */
    public void setCustomProperties(List<CustomProperty> customProperties) {
        this.customProperties = customProperties;
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

}
