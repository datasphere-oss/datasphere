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
