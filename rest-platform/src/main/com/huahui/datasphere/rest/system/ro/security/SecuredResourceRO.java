/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package com.huahui.datasphere.rest.system.ro.security;

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
