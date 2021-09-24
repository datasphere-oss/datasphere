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

package com.huahui.datasphere.mdm.core.po;

import java.util.Date;

/**
 * @author Michael Yashin. Created on 26.05.2015.
 */
public abstract class AbstractObjectPO implements ObjectPO {
    /**
     * Create date.
     */
    public static final String FIELD_CREATE_DATE = "create_date";
    /**
     * Update date.
     */
    public static final String FIELD_UPDATE_DATE = "update_date";
    /**
     * Created by.
     */
    public static final String FIELD_CREATED_BY = "created_by";
    /**
     * Updated by.
     */
    public static final String FIELD_UPDATED_BY = "updated_by";
    /**
     * Create time stamp.
     */
    protected Date createDate;
    /**
     * Update time stamp.
     */
    protected Date updateDate;
    /**
     * Created by.
     */
    protected String createdBy;
    /**
     * Updated by.
     */
    protected String updatedBy;
    /**
     * Gets create date.
     * @return date
     */
    public Date getCreateDate() {
        return createDate;
    }
    /**
     * Sets create date.
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * Gets update date.
     * @return
     */
    public Date getUpdateDate() {
        return updateDate;
    }
    /**
     * Sets update date.
     * @param updateDate
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    /**
     * Gets created by.
     * @return
     */
    public String getCreatedBy() {
        return createdBy;
    }
    /**
     * Sets created by.
     * @param createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /**
     * Gets updated by.
     * @return
     */
    public String getUpdatedBy() {
        return updatedBy;
    }
    /**
     * Sets updated by.
     * @param updatedBy
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
