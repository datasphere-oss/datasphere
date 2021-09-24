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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Denis Kostovarov
 */
public abstract class AbstractRole implements Role, Serializable {

    /**
     * SVUID.
     */
    private static final long serialVersionUID = -7221644432780048916L;
    /**
     * Name.
     */
    private String name;
    /**
     * Dsiplay name.
     */
    private String displayName;
    /**
     * Role type (system or user defined).
     */
    private RoleType roleType = RoleType.USER_DEFINED;
    /**
     * Security labels.
     */
    private List<SecurityLabel> securityLabels;
    /**
     * Rights.
     */
    private List<Right> rights;

    /**
     * Custom properties.
     */
    private List<CustomProperty> customProperties;
    /**
     * Constructor.
     */
    public AbstractRole() {
        super();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(final RoleType roleType) {
        this.roleType = roleType;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public List<Right> getRights() {
        return Objects.isNull(rights) ? Collections.emptyList() : Collections.unmodifiableList(rights);
    }

    public void setRights(final List<Right> rights) {
        this.rights = rights;
    }

    @Override
    public List<SecurityLabel> getSecurityLabels() {
        return Objects.isNull(securityLabels) ? Collections.emptyList() : Collections.unmodifiableList(securityLabels);
    }

    public void setSecurityLabels(final List<SecurityLabel> securityLabels) {
        this.securityLabels = securityLabels;
    }

    /**
     * @return the customProperties
     */
    @Override
    public List<CustomProperty> getProperties() {
        return Objects.isNull(customProperties) ? Collections.emptyList() : Collections.unmodifiableList(customProperties);
    }
    /**
     * @param customProperties the customProperties to set
     */
    public void setCustomProperties(List<CustomProperty> customProperties) {
        this.customProperties = customProperties;
    }


}
