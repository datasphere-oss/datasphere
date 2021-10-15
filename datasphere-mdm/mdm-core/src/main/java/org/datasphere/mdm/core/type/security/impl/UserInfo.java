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

package com.huahui.datasphere.mdm.core.type.security.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.huahui.datasphere.mdm.core.type.security.AbstractUserInfo;
import com.huahui.datasphere.mdm.core.type.security.Endpoint;

/**
 * The Class User.
 */
public class UserInfo extends AbstractUserInfo implements Serializable {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = -3578955731056238141L;

    /** The user. */
    private String login;

    /** The password. */
    private String password;
    /**
     * 'Password updated at' timestamp.
     */
    private Date createdAt;

    /** The updated by. */
    private String updatedBy;
    /**
     * External user flag.
     */
    private boolean external;

    /** The endpoints. */
    private List<Endpoint> endpoints;

    /**
     * Gets the user.
     *
     * @return the user
     */
    @Override
    public String getLogin() {
        return login;
    }

    /**
     * Sets the user.
     *
     * @param login
     *            the user to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    @Override
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
     * Gets the created at.
     *
     * @return the createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the created at.
     *
     * @param createdAt
     *            the createdAt to set
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Sets the updated by.
     *
     * @param updatedBy
     *            the new updated by
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;

    }

    /**
     * Gets the updated by.
     *
     * @return the updated by
     */
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Gets externl flag.
     *
     * @return flag
     */
    @Override
    public boolean isExternal() {
        return external;
    }

    /**
     * Sets user external flag.
     *
     * @param external
     *            the flag to set
     */
    public void setExternal(boolean external) {
        this.external = external;
    }

    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.common.integration.auth.User#getEndpoints()
     */
    @Override
    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    /**
     * Sets the endpoints.
     *
     * @param endpoints the new endpoints
     */
    @Override
    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }
}
