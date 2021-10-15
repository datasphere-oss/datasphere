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

package com.huahui.datasphere.mdm.core.type.data;

import java.util.Date;

/**
 * @author theseusyang
 * Info section (Common part).
 */
public abstract class InfoSection {
    /**
     * Created by.
     */
    protected String createdBy;
    /**
     * Updated by.
     */
    protected String updatedBy;
    /**
     * Create date.
     */
    protected Date createDate;
    /**
     * Update date.
     */
    protected Date updateDate;
    /**
     * Record status.
     */
    protected RecordStatus status;

    /**
     * Valid from.
     */
    protected Date validFrom;
    /**
     * Valid to.
     */
    protected Date validTo;
    /**
     * OperationType from composeBVR
     */
    protected OperationType operationType;
    /**
     * Gets createdBy field.
     * @return field
     */
    public String getCreatedBy() {
        return createdBy;
    }
    /**
     * Sets created by field.
     * @param createdBy the value to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /**
     * Gets updatedBy field.
     * @return field
     */
    public String getUpdatedBy() {
        return updatedBy;
    }
    /**
     * Sets updated by field.
     * @param updatedBy the value to set
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    /**
     * Gets createDate field.
     * @return field
     */
    public Date getCreateDate() {
        return createDate;
    }
    /**
     * Sets create date field.
     * @param createDate value to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * Gets updateDate field.
     * @return field
     */
    public Date getUpdateDate() {
        return updateDate;
    }
    /**
     * Sets update date field.
     * @param updateDate value to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    /**
     * Gets status field.
     * @return field
     */
    public RecordStatus getStatus() {
        return status;
    }
    /**
     * Sets status field.
     * @param status value to set
     */
    public void setStatus(RecordStatus status) {
        this.status = status;
    }


    /**
     * Gets validFrom field.
     * @return field
     */
    public Date getValidFrom() {
        return validFrom;
    }
    /**
     * Sets valid from field.
     * @param validFrom the value to set
     */
    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }
    /**
     * Gets validTo field.
     * @return field
     */
    public Date getValidTo() {
        return validTo;
    }
    /**
     * Sets valid to field.
     * @param validTo the value to set
     */
    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }
    /**
     * Gets entity operationType field.
     * @return the operationType
     */
    public OperationType getOperationType() {
        return operationType;
    }
    /**
     * Sets entity operationType field.
     * @param operationType
     */
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}
