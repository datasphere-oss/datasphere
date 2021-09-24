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

package com.huahui.datasphere.mdm.core.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.huahui.datasphere.mdm.core.type.security.CustomProperty;
import com.huahui.datasphere.mdm.core.type.security.Right;
import com.huahui.datasphere.mdm.core.type.security.Role;
import com.huahui.datasphere.mdm.core.type.security.RoleType;
import com.huahui.datasphere.mdm.core.type.security.SecurityLabel;

/**
 * The Class RoleRO.
 */
public class RoleDTO extends BaseSecurityDTO implements Role, Serializable {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = 883041167295213217L;
    /** The name. */
    private String name;
    /** The display name. */
    private String displayName;
    /** The role type. */
    private RoleType type;
    /** The rights. */
    private List<Right> rights;
    /** The security labels. */
    private List<SecurityLabel> securityLabels;
    /** Custom properties. */
    private List<CustomProperty> properties;
    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
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
     * Gets the role type.
     *
     * @return the role type
     */
    @Override
    public RoleType getRoleType() {
        return type;
    }

    /**
     * Sets the role type.
     *
     * @param roleType
     *            the new role type
     */
    public void setRoleType(RoleType roleType) {
        this.type = roleType;
    }

    /**
     * Gets the rights.
     *
     * @return the rights
     */
    @Override
    public List<Right> getRights() {
        return Objects.isNull(rights) ? Collections.emptyList() : Collections.unmodifiableList(rights);
    }

    /**
     * Sets the rights.
     *
     * @param rights
     *            the new rights
     */
    public void setRights(List<Right> rights) {
        this.rights = rights;
    }

    /**
	 * Gets the display name.
	 *
	 * @return the displayName
	 */
    @Override
    public String getDisplayName() {
        return displayName;
    }

    /**
	 * Sets the display name.
	 *
	 * @param displayName
	 *            the displayName to set
	 */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
	 * Gets the security labels.
	 *
	 * @return the security labels
	 */
    @Override
    public List<SecurityLabel> getSecurityLabels() {
        return Objects.isNull(securityLabels) ? Collections.emptyList() : Collections.unmodifiableList(securityLabels);
    }

    /**
	 * Sets the security labels.
	 *
	 * @param securityLabels
	 *            the new security labels
	 */
    public void setSecurityLabels(final List<SecurityLabel> securityLabels) {
        this.securityLabels = securityLabels;
    }

    /**
     * @return the properties
     */
    @Override
    public List<CustomProperty> getProperties() {
        return Objects.isNull(properties) ? Collections.emptyList() : Collections.unmodifiableList(properties);
    }

    /**
     * @param customProperties the properties to set
     */
    public void setProperties(List<CustomProperty> customProperties) {
        this.properties = customProperties;
    }

}
