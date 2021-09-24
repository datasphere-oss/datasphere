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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import com.huahui.datasphere.mdm.core.type.security.SecuredResource;
import com.huahui.datasphere.mdm.core.type.security.SecuredResourceCategory;
import com.huahui.datasphere.mdm.core.type.security.SecuredResourceType;

/**
 * The Class SecuredResourceRO.
 * @author ilya.bykov
 */
public class SecuredResourceDTO extends BaseSecurityDTO implements SecuredResource, Serializable {

    /**
     * SVUID.
     */
    private static final long serialVersionUID = 3212929937675356287L;

    /** The name. */
    private String name;

    /** The display name. */
    private String displayName;

    /** The type. */
    private SecuredResourceType type;

    /** The type. */
    private SecuredResourceCategory category;

    /**
     * Parent resource.
     */
    private SecuredResourceDTO parent;

    /** Children. */
    private Collection<SecuredResourceDTO> children = new ArrayList<>();
    /**
     * Default constructor.
     */
    public SecuredResourceDTO() {
        super();
    }
    /**
     * Default constructor.
     * @param other object to copy fields from
     */
    public SecuredResourceDTO(SecuredResource other) {
        super();
        if (Objects.nonNull(other)) {
            this.name = other.getName();
            this.displayName = other.getDisplayName();
            this.type = other.getType();
            this.category = other.getCategory();
        }
    }

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
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    @Override
    public SecuredResourceType getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the type to set
     */
    public void setType(SecuredResourceType type) {
        this.type = type;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SecuredResourceCategory getCategory() {
        return category;
    }

    /**
     * Sets the category.
     *
     * @param category the category to set
     */
    public void setCategory(SecuredResourceCategory category) {
        this.category = category;
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
     * @return the parent
     */
    public SecuredResourceDTO getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(SecuredResourceDTO parent) {
        this.parent = parent;
    }
    /**
     * @return the children
     */
    public Collection<SecuredResourceDTO> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(Collection<SecuredResourceDTO> children) {
        this.children = children;
    }
}
