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
package com.huahui.datasphere.mdm.rest.core.ro;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.huahui.datasphere.mdm.core.type.security.Right;

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
    private List<Right> rights;
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
    public List<Right> getRights() {
        return rights;
    }

    /**
     * @param rights
     *            the rights to set
     */
    public void setRights(List<Right> rights) {
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
