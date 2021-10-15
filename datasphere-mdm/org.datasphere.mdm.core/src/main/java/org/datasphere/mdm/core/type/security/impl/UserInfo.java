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

package org.datasphere.mdm.core.type.security.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.datasphere.mdm.core.type.security.AbstractUserInfo;
import org.datasphere.mdm.core.type.security.Endpoint;

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
