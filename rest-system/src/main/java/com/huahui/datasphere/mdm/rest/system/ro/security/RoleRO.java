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
package com.huahui.datasphere.mdm.rest.system.ro.security;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class RoleRO.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleRO extends BaseSecurityRO {

    /** The name. */
    private String name;

    /** The display name. */
    private String displayName;
    /** The role type. */
    private RoleTypeRO type;
    /** The rights. */
    private List<RightRO> rights;

    /**
     * Security labels.
     */
    private List<SecurityLabelRO> securityLabels;
    /**
     * Role properties.
     */
    private List<RolePropertyRO> properties;
    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the rights.
     *
     * @return the rights
     */
    public List<RightRO> getRights() {
        return rights;
    }

    /**
     * Sets the rights.
     *
     * @param rights
     *            the new rights
     */
    public void setRights(List<RightRO> rights) {
        this.rights = rights;
    }

    /**
     * Adds the right.
     *
     * @param right
     *            the right
     */
    public void addRight(RightRO right) {
        if (this.rights == null) {
            this.rights = new ArrayList<>();
        }
        this.rights.add(right);
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name.
     *
     * @param displayName
     *            the new display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public RoleTypeRO getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(RoleTypeRO type) {
        this.type = type;
    }

    /**
     * @return the securityLabels
     */
    public List<SecurityLabelRO> getSecurityLabels() {
        if (this.securityLabels == null) {
            this.securityLabels = new ArrayList<>();
        }
        return securityLabels;
    }

    /**
     * @param securityLabels
     *            the securityLabels to set
     */
    public void setSecurityLabels(List<SecurityLabelRO> securityLabels) {
        this.securityLabels = securityLabels;
    }

    public void addSecurityLabel(SecurityLabelRO securityLabelRO) {
        if (this.securityLabels == null) {
            this.securityLabels = new ArrayList<>();
        }
        this.securityLabels.add(securityLabelRO);
    }

    /**
     * @return the properties
     */
    public List<RolePropertyRO> getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(List<RolePropertyRO> properties) {
        this.properties = properties;
    }

}
