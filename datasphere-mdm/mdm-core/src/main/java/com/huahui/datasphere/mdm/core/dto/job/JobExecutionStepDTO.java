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

package com.huahui.datasphere.mdm.core.dto.job;

import java.time.ZonedDateTime;

import com.huahui.datasphere.mdm.core.type.job.JobExecutionBatchStatus;

public class JobExecutionStepDTO {
    // The step execution ID
    private final Long id;
    // The job execution ID
    private final Long jobExecutionId;

    private final String stepName;

    private final JobExecutionBatchStatus jobExecutionBatchStatus;

    private final JobExecutionExitStatusDTO jobExecutionExitStatus;

    private final ZonedDateTime startTime;

    private final ZonedDateTime endTime;

    private final ZonedDateTime lastUpdated;

    private int readCount = 0;

    private int writeCount = 0;

    private int commitCount = 0;

    private int rollbackCount = 0;

    private int readSkipCount = 0;

    private int processSkipCount = 0;

    private int writeSkipCount = 0;

    public JobExecutionStepDTO(
            final Long id,
            final Long jobExecutionId,
            final String stepName,
            final JobExecutionBatchStatus jobExecutionBatchStatus,
            final JobExecutionExitStatusDTO jobExecutionExitStatus,
            final ZonedDateTime startTime,
            final ZonedDateTime endTime,
            final ZonedDateTime lastUpdated
    ) {
        this.id = id;
        this.jobExecutionId = jobExecutionId;
        this.stepName = stepName;
        this.jobExecutionBatchStatus = jobExecutionBatchStatus;
        this.jobExecutionExitStatus = jobExecutionExitStatus;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lastUpdated = lastUpdated;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the jobExecutionId
     */
    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public String getStepName() {
        return stepName;
    }

    public JobExecutionBatchStatus getJobExecutionBatchStatus() {
        return jobExecutionBatchStatus;
    }

    public JobExecutionExitStatusDTO getJobExecutionExitStatus() {
        return jobExecutionExitStatus;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @return the readCount
     */
    public int getReadCount() {
        return readCount;
    }

    /**
     * @param readCount the readCount to set
     */
    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    /**
     * @return the writeCount
     */
    public int getWriteCount() {
        return writeCount;
    }

    /**
     * @param writeCount the writeCount to set
     */
    public void setWriteCount(int writeCount) {
        this.writeCount = writeCount;
    }

    /**
     * @return the commitCount
     */
    public int getCommitCount() {
        return commitCount;
    }

    /**
     * @param commitCount the commitCount to set
     */
    public void setCommitCount(int commitCount) {
        this.commitCount = commitCount;
    }

    /**
     * @return the rollbackCount
     */
    public int getRollbackCount() {
        return rollbackCount;
    }

    /**
     * @param rollbackCount the rollbackCount to set
     */
    public void setRollbackCount(int rollbackCount) {
        this.rollbackCount = rollbackCount;
    }

    /**
     * @return the readSkipCount
     */
    public int getReadSkipCount() {
        return readSkipCount;
    }

    /**
     * @param readSkipCount the readSkipCount to set
     */
    public void setReadSkipCount(int readSkipCount) {
        this.readSkipCount = readSkipCount;
    }

    /**
     * @return the processSkipCount
     */
    public int getProcessSkipCount() {
        return processSkipCount;
    }

    /**
     * @param processSkipCount the processSkipCount to set
     */
    public void setProcessSkipCount(int processSkipCount) {
        this.processSkipCount = processSkipCount;
    }

    /**
     * @return the writeSkipCount
     */
    public int getWriteSkipCount() {
        return writeSkipCount;
    }

    /**
     * @param writeSkipCount the writeSkipCount to set
     */
    public void setWriteSkipCount(int writeSkipCount) {
        this.writeSkipCount = writeSkipCount;
    }
}
