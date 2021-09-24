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

/**
 * @author Denis Kostovarov
 */
public abstract class AbstractSecuredResource implements SecuredResource, Serializable {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = -4784151607289806225L;
    /**
     * Name.
     */
    private String name;
    /**
     * Display name.
     */
    private String displayName;
    /**
     * Type.
     */
    private SecuredResourceType type;
    /**
     * Category.
     */
    private SecuredResourceCategory category;
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SecuredResourceType getType() {
        return type;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SecuredResourceCategory getCategory() {
        return category;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }
    /**
     * Sets name.
     * @param name the name
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Sets display name.
     * @param displayName the display name
     */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    /**
     * Sets type.
     * @param securedResourceType the type to set
     */
    public void setType(final SecuredResourceType securedResourceType) {
        this.type = securedResourceType;
    }
    /**
     * Sets a category.
     * @param category the category to set
     */
    public void setCategory(SecuredResourceCategory category) {
        this.category = category;
    }
}
