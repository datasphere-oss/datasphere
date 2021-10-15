/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 * 
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.type.data;

import java.util.Date;

/**
 * @author Mikhail Mikhailov
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
