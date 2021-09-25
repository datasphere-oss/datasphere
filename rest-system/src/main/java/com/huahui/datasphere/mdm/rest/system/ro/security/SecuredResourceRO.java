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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class SecuredResourceRO.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecuredResourceRO extends BaseSecurityRO {

    /** The name. */
    private String name;

    /** The display name. */
    private String displayName;

    /** The type. */
    private SecuredResourceTypeRO type;

    /** The type. */
    private SecuredResourceCategoryRO category;

    /** Parent. */
    private SecuredResourceRO parent;

    /** Children. */
    private List<SecuredResourceRO> children;
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
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the display name.
     *
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name.
     *
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public SecuredResourceTypeRO getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType(SecuredResourceTypeRO type) {
        this.type = type;
    }



    /**
     * @return the category
     */
    public SecuredResourceCategoryRO getCategory() {
        return category;
    }


    /**
     * @param category the category to set
     */
    public void setCategory(SecuredResourceCategoryRO category) {
        this.category = category;
    }

    /**
     * @return the parent
     */
    public SecuredResourceRO getParent() {
        return parent;
    }


    /**
     * @param parent the parent to set
     */
    public void setParent(SecuredResourceRO parent) {
        this.parent = parent;
    }


    /**
     * @return the children
     */
    public List<SecuredResourceRO> getChildren() {
        return children;
    }


    /**
     * @param children the children to set
     */
    public void setChildren(List<SecuredResourceRO> children) {
        this.children = children;
    }

}
