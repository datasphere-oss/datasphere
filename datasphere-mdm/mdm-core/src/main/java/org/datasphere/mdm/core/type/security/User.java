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

package com.huahui.datasphere.mdm.core.type.security;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Integration user DTO.
 *
 * @author Denis Kostovarov
 */
public interface User {
    /**
     * Get name of user.
     *
     * @return user name
     */
    String getName();
    /**
     * Optional setter for external profile support.
     *
     * @param value the value
     */
    void setName(String value);
    /**
     * Get user email.
     *
     * @return user email.
     */
    String getEmail();
    /**
     * Optional setter for external profile support.
     *
     * @param value the value to set
     */
    void setEmail(String value);
    /**
     * Gets this user's locale.
     * @return locale
     */
    Locale getLocale();
    /**
     * Sets this user's locale.
     * @param locale the locale to set
     */
    void setLocale(Locale locale);
    /**
     * Get user login.
     *
     * @return user login.
     */
    String getLogin();

    /**
     * Get user password.
     *
     * @return user password.
     */
    String getPassword();
    /**
     * Get user rights.
     *
     * @return user rights.
     */
    List<Right> getRights();
    /**
     * Gets user roles.
     *
     * @return list of roles
     */
    List<Role> getRoles();
    /**
     * Sets roles list.
     * This is for the case, if the user is authenticated elsewhere, but uses the profile created in UD.
     * @param roles the roles to set
     */
    void setRoles(List<Role> roles);
    /**
     * Get security labels for this user.
     *
     * @return list with security labels for this user, if any configured.
     */
    List<SecurityLabel> getLabels();
    /**
     * Get endpoints for this user.
     *
     * @return list with endpoints for this user, if any configured.
     */
    List<Endpoint> getEndpoints();
    /**
     * Set endpoints for this user.
     *
     * @param endpoints list with endpoints for this user, if any configured.
     */
    void setEndpoints(List<Endpoint> endpoints);
    /**
     * Set security labels for this user.
     *
     * @param labels
     *            list with security labels for this user.
     */
    void setLabels(List<SecurityLabel> labels);
    /**
     * Get user token.
     *
     * @return user token.
     */
    String getCustomToken();
    /**
     * Optional setter for external profile support.
     *
     * @param value the value to set
     */
    void setCustomToken(String value);
    /**
     * Get last password change.
     *
     * @return last password change.
     */
    Date getPasswordUpdatedAt();
    /**
     * Optional setter for external profile support.
     *
     * @param value the value to set
     */
    void setPasswordUpdatedAt(Date value);
    /**
     * Whether user should change his/her password.
     *
     * @return user change password status.
     */
    boolean getForcePasswordChangeFlag();
    /**
     * Optional setter for external profile support.
     *
     * @param value the value to set
     */
    void setForcePasswordChangeFlag(boolean value);
    /**
     * Flag indicating whether object is already has authorization parameters.
     *
     * @return authorization state flag.
     */
    boolean hasAuthorization();
    /**
     * Optional setter for external profile support.
     *
     * @param value the value to set
     */
    void setHasAuthorization(boolean value);
    /**
     * Flag indicating whether object is already has profile loaded.
     *
     * @return profile state flag.
     */
    boolean hasProfile();
    /**
     * Optional setter for external profile support.
     *
     * @param value the value to set
     */
    void setHasProfile(boolean value);
    /**
     * Shorthand for role 'admin' check.
     *
     * @return admin role flag.
     */
    boolean isAdmin();
    /**
     * Optional setter for external profile support.
     *
     * @param value the value to set
     */
    void setAdmin(boolean value);
    /**
     * Get security data source (providers collection) name.
     * @return security data source name.
     */
    String getSecurityDataSource();
    /**
     * Optional setter for external profile support.
     * @param value
     */
    void setSecurityDataSource(String value);
    /**
     * System property to distinct external users.
     *
     * @return external user flag.
     */
    default boolean isExternal() {
        return false;
    }
    /**
     * Get last date of update.
     *
     * @return last update date.
     */
    Date getUpdatedAt();
    /**
     * Optional setter for external profile support.
     *
     * @param value the value to set
     */
    void setUpdatedAt(Date value);
    /**
     * Gets collection of the custom properties, defined for this user.
     * @return collection of custom properties
     */
    List<CustomProperty> getCustomProperties();
    /**
     * @return email notification flag
     */
    Boolean isEmailNotification();
    /**
     * @set email notification flag
     */
    void setEmailNotification(Boolean emailNotification);
}
