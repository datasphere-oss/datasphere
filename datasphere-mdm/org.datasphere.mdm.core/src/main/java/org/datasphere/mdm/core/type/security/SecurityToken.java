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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class SecurityToken.
 */
public class SecurityToken implements Serializable {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The id.
     */
    private int id;
    /**
     * The token.
     */
    private String token;
    /**
     * The created at.
     */
    private Date createdAt;
    /**
     * The last used at.
     */
    private Date lastUsedAt;
    /**
     * The expiring at.
     */
    private Date expiringAt;
    /**
     * The user.
     */
    private User user;
    /**
     * Endpoint, audit related.
     */
    private EndpointType endpoint;
    /**
     * IP, audit related.
     */
    private String userIp;
    /**
     * Server IP, audit related
     */
    private String serverIp;
    /**
     * User rights map.
     */
    private final Map<String, Right> rightsMap = new HashMap<>();
    /**
     * Roles map.
     */
    private final Map<String, Role> rolesMap = new HashMap<>();
    /**
     * Resource to labeles map.
     */
    private final Map<String, List<SecurityLabel>> labelsMap = new HashMap<>();
    /**
     * inner
     */
    private final boolean inner;

    public SecurityToken(){
        this.inner = false;
    }

    public SecurityToken(boolean inner){
        this.inner = inner;
    }
    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

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
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
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
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the rightsMap
     */
    public Map<String, Right> getRightsMap() {
        return rightsMap;
    }


    /**
     * @return the rolesMap
     */
    public Map<String, Role> getRolesMap() {
        return rolesMap;
    }

    /**
     * Tells whether a user has particular role.
     * @param roleName the name
     * @return true, if he has, false, if he hasn't
     */
    public boolean hasRole(String roleName) {
        return rolesMap.containsKey(roleName);
    }

    /**
     * @return the resourcesMap
     */
    public Map<String, List<SecurityLabel>> getLabelsMap() {
        return labelsMap;
    }

    public EndpointType getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(EndpointType endpoint) {
        this.endpoint = endpoint;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public boolean isInner() {
        return inner;
    }

    /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
    @Override
    public String toString() {
        return "SecurityToken [id=" + id +
                ", token=" + token +
                ", createdAt=" + createdAt +
                ", lastUsedAt=" + lastUsedAt +
                ", expiringAt=" + expiringAt +
                ", user=" + user + "]";
    }
}
