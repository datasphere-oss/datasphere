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

package org.datasphere.mdm.core.type.job;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class JobExecutionStep {
    /**
     * The step execution ID.
     */
    private Long id;
    /**
     * The job execution ID.
     */
    private Long jobExecutionId;
    /**
     * The staep name
     */
    private String stepName;
    /**
     * Current state.
     */
    private StepExecutionState state;
    /**
     * Start time.
     */
    private Instant startTime;
    /**
     * End time.
     */
    private Instant endTime;
    /**
     * last update time.
     */
    private Instant lastUpdated;
    /**
     * Counters: read.
     */
    private int readCount = 0;
    /**
     * Counters: write.
     */
    private int writeCount = 0;
    /**
     * Counters: commit.
     */
    private int commitCount = 0;
    /**
     * Counters: rollback.
     */
    private int rollbackCount = 0;
    /**
     * Counters: skip.
     */
    private int readSkipCount = 0;
    /**
     * Counters: process skip.
     */
    private int processSkipCount = 0;
    /**
     * Counters: write skip.
     */
    private int writeSkipCount = 0;
    /**
     * Failures.
     */
    private List<Throwable> failures;

    public JobExecutionStep() {
        super();
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the jobExecutionId
     */
    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    /**
     * @param jobExecutionId the jobExecutionId to set
     */
    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }

    public String getStepName() {
        return stepName;
    }

    /**
     * @param stepName the stepName to set
     */
    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public StepExecutionState getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(StepExecutionState state) {
        this.state = state;
    }

    public Instant getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated the lastUpdated to set
     */
    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
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

    /**
     * Failures
     * @return failures
     */
    public List<Throwable> getFailures() {
        return Objects.isNull(failures) ? Collections.emptyList() : failures;
    }

    /**
     * Stes failures.
     * @param failures to set
     */
    public void setFailures(List<Throwable> failures) {
        this.failures = failures;
    }
}
