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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.huahui.datasphere.portal.type.security.RightInf;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class LoginResponse.
 */
public class LoginResponse {

    /** The token. */
    @JsonProperty(value = "token")
    private String token;

    /** User Role. */
    @JsonProperty(value = "rights")
    private List<RightInf> rights;
    /**
     * User info.
     */
    @JsonProperty(value = "userInfo")
    private UserRO userInfo;

    /**
     * Force change password flag.
     */
    @JsonProperty(value = "forcePasswordChange")
    private Boolean forcePasswordChange;
    /**
     * Force change password flag.
     */
    @JsonProperty(value = "tokenTTL")
    private long tokenTTL;

    @JsonProperty(value = "buildVersion")
    private String buildVersion;

    /**
     * Force change license flag.
     */
    @JsonProperty(value = "forceLicenseChange")
    private Boolean forceLicenseChange;

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     *
     * @param token
     *            the new token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the rights
     */
    public List<RightInf> getRights() {
        return rights;
    }

    /**
     * @param rights
     *            the rights to set
     */
    public void setRights(List<RightInf> rights) {
        this.rights = rights;
    }

    /**
     * @return the userInfo
     */
    public UserRO getUserInfo() {
        return userInfo;
    }

    /**
     * @param userInfo
     *            the userInfo to set
     */
    public void setUserInfo(UserRO userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * Get force password change flag.
     * @return true if password should be changed, false otherwise
     */
    public Boolean getForcePasswordChange() {
        return forcePasswordChange;
    }

    /**
     * Set force password change flag.
     * @param forcePasswordChange true if if password should be changed, false otherwise
     */
    public void setForcePasswordChange(Boolean forcePasswordChange) {
        this.forcePasswordChange = forcePasswordChange;
    }
    /**
     * Return token time to live(in seconds)
     * @return token time to live
     */
	public long getTokenTTL() {
		return tokenTTL;
	}
	/**
	 * Sets token time to live.
	 * @param tokenTTL token time to live(in seconds)
	 */
	public void setTokenTTL(long tokenTTL) {
		this.tokenTTL = tokenTTL;
	}

    /**
     * Information about build version
     */
    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }

    public Boolean getForceLicenseChange() {
        return forceLicenseChange;
    }

    public void setForceLicenseChange(Boolean forceLicenseChange) {
        this.forceLicenseChange = forceLicenseChange;
    }

}
