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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class contains rights
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RightRO extends BaseSecurityRO {
    /** The secured resource. */
    private SecuredResourceRO securedResource;

    /** The create. */
    private boolean create;

    /** The update. */
    private boolean update;

    /** The delete. */
    private boolean delete;

    /** The read. */
    private boolean read;
    /**
     * Gets the secured resource.
     *
     * @return the secured resource
     */
    public SecuredResourceRO getSecuredResource() {
        return securedResource;
    }

    /**
     * Sets the secured resource.
     *
     * @param securedResource the new secured resource
     */
    public void setSecuredResource(SecuredResourceRO securedResource) {
        this.securedResource = securedResource;
    }

    /**
     * Checks if is creates the.
     *
     * @return the create
     */
    public boolean isCreate() {
        return create;
    }

    /**
     * Sets the creates the.
     *
     * @param create the create to set
     */
    public void setCreate(boolean create) {
        this.create = create;
    }

    /**
     * Checks if is update.
     *
     * @return the update
     */
    public boolean isUpdate() {
        return update;
    }

    /**
     * Sets the update.
     *
     * @param update the update to set
     */
    public void setUpdate(boolean update) {
        this.update = update;
    }

    /**
     * Checks if is delete.
     *
     * @return the delete
     */
    public boolean isDelete() {
        return delete;
    }

    /**
     * Sets the delete.
     *
     * @param delete the delete to set
     */
    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    /**
     * Checks if is read.
     *
     * @return the read
     */
    public boolean isRead() {
        return read;
    }

    /**
     * Sets the read.
     *
     * @param read the read to set
     */
    public void setRead(boolean read) {
        this.read = read;
    }



}
