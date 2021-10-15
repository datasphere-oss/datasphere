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

package org.datasphere.mdm.core.po.job;

import java.util.Date;

/**
 * @author Denis Kostovarov
 */
public class JobBatchJobInstancePO {

    public static final String TABLE_NAME = "job_batch_job_instance";

    public static final String FIELD_JOB_DEFINITION_ID = "job_definition_id";

    public static final String FIELD_JOB_INSTANCE_ID = "job_instance_id";

    public static final String FIELD_CREATE_DATE = "create_date";

    public static final String FIELD_CREATED_BY = "created_by";

    private Long jobDefinitionId;

    private Long jobInstanceId;

    private Date createDate;

    private String createdBy;

    public JobBatchJobInstancePO() {
        super();
    }

    public JobBatchJobInstancePO(Long jobDefinitionId, Long jobInstanceId) {
        super();
        this.jobDefinitionId = jobDefinitionId;
        this.jobInstanceId = jobInstanceId;
    }

    public Long getJobDefinitionId() {
        return jobDefinitionId;
    }

    public void setJobDefinitionId(Long jobId) {
        this.jobDefinitionId = jobId;
    }

    public Long getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(Long jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
