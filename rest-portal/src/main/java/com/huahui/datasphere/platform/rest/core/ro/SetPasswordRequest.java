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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO !!!!!Rewrite all this code!!!!!
 */
public class SetPasswordRequest {
    /** The user name. */
    @Deprecated
    @JsonProperty(value = "userName")
    private String userName;

    /** The password. */
    @JsonProperty(required = true, value = "password")
    private String password;

    /** The password. */
    @JsonProperty(required = false, value = "oldPassword")
    private String oldPassword;

    /**
     * Gets the user name.
     *
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     *
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets old password.
     * @return old password
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Sets old password.
     * @param  oldPassword old password
     * @param  oldPassword old password
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
