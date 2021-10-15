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

import java.util.Date;
import java.util.List;

/**
 * @author theseusyang
 */
public class StepExecutionDTO {

    private JobExecutionDTO jobExecution;

    private String stepName;

    private BatchStatus status;

    private int readCount = 0;

    private int writeCount = 0;

    private int commitCount = 0;

    private int rollbackCount = 0;

    private int readSkipCount = 0;

    private int processSkipCount = 0;

    private int writeSkipCount = 0;

    private Date startTime = new Date(System.currentTimeMillis());

    private Date endTime = null;

    private Date lastUpdated = null;

    private ExitStatus exitStatus;

    private boolean terminateOnly;

    private int filterCount;

    private List<Throwable> failureExceptions;

    public JobExecutionDTO getJobExecution() {
        return jobExecution;
    }

    public void setJobExecution(JobExecutionDTO jobExecution) {
        this.jobExecution = jobExecution;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public void setStatus(BatchStatus status) {
        this.status = status;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getWriteCount() {
        return writeCount;
    }

    public void setWriteCount(int writeCount) {
        this.writeCount = writeCount;
    }

    public int getCommitCount() {
        return commitCount;
    }

    public void setCommitCount(int commitCount) {
        this.commitCount = commitCount;
    }

    public int getRollbackCount() {
        return rollbackCount;
    }

    public void setRollbackCount(int rollbackCount) {
        this.rollbackCount = rollbackCount;
    }

    public int getReadSkipCount() {
        return readSkipCount;
    }

    public void setReadSkipCount(int readSkipCount) {
        this.readSkipCount = readSkipCount;
    }

    public int getProcessSkipCount() {
        return processSkipCount;
    }

    public void setProcessSkipCount(int processSkipCount) {
        this.processSkipCount = processSkipCount;
    }

    public int getWriteSkipCount() {
        return writeSkipCount;
    }

    public void setWriteSkipCount(int writeSkipCount) {
        this.writeSkipCount = writeSkipCount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public ExitStatus getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(ExitStatus exitStatus) {
        this.exitStatus = exitStatus;
    }

    public boolean isTerminateOnly() {
        return terminateOnly;
    }

    public void setTerminateOnly(boolean terminateOnly) {
        this.terminateOnly = terminateOnly;
    }

    public int getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(int filterCount) {
        this.filterCount = filterCount;
    }

    public List<Throwable> getFailureExceptions() {
        return failureExceptions;
    }

    public void setFailureExceptions(List<Throwable> failureExceptions) {
        this.failureExceptions = failureExceptions;
    }

    public enum BatchStatus {
        COMPLETED,
        STARTING,
        STARTED,
        STOPPING,
        STOPPED,
        FAILED,
        ABANDONED,
        UNKNOWN;
    }

    public static class ExitStatus {
        private final String exitCode;

        private final String exitDescription;

        public ExitStatus(String exitCode, String exitDescription) {
            this.exitCode = exitCode;
            this.exitDescription = exitDescription;
        }

        public String getExitCode() {
            return exitCode;
        }

        public String getExitDescription() {
            return exitDescription;
        }
    }
}
