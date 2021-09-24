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

package com.huahui.datasphere.mdm.core.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.huahui.datasphere.mdm.core.po.job.JobPO;
import com.huahui.datasphere.mdm.core.po.job.JobParameterPO;
import com.huahui.datasphere.mdm.core.po.job.JobTriggerPO;
import com.huahui.datasphere.mdm.core.type.job.JobFilter;

/**
 * Dao for unidata job templates.
 *
 * @author Denis Kostovarov
 */
public interface JobDao {

    /**
     * Get all job templates from database.
     *
     * @return List of all jobs or list with size = 0.
     */
    List<JobPO> getJobs();

    /**
     * Get count of all jobs in database.
     *
     * @param filter
     * @return
     */
    int getJobsCount(JobFilter filter);

    /**
     * Search jobs by filter conditions.
     *
     * @param filter
     * @return
     */
    List<JobPO> searchJobs(JobFilter filter);

    /**
     * Get all job templates from database with parameters.
     *
     * @return List of all jobs or list with size = 0.
     */
    List<JobPO> getJobsWithParameters();

    /**
     * Find a particular job with specified ID.
     *
     * @param jobId Job ID to find
     * @return Job or null if not found.
     */
    JobPO findJob(long jobId);

    /**
     * Find a particular job with specified ID. Load job parameters.
     *
     * @param jobId Job ID to find
     * @return Job or null if not found.
     */
    JobPO findJobWithParameters(long jobId);

    /**
     * Find a particular job with specified name.
     *
     * @param jobName Job name to find
     * @return Job template or null if not found.
     */
    JobPO findJob(String jobName);

    /**
     * Get job parameters by job id.
     *
     * @param jobId Job identifier.
     * @return Found parameters or list with size = 0.
     */
    List<JobParameterPO> getJobParameters(long jobId);

    /**
     * Get parameters for specified job ids.
     *
     * @param jobIds Job identifiers.
     * @return Found parameters job id --> list of parameters.
     */
    Map<Long, List<JobParameterPO>> getJobsParameters(List<Long> jobIds);

    /**
     * Saves newly created job
     *
     * @param newJob Filled DTO job entity.
     * @return Job id.
     */
    long insertJob(JobPO newJob);

    /**
     * Update earlier created job.
     *
     * @param job Changed job entity.
     */
    boolean updateJob(JobPO job);

    /**
     * Remove job with specified id.
     *
     * @param jobId Job Id.
     * @return <code>true</code> if success.
     */
    boolean removeJob(long jobId);

    /**
     * Enable/disable job with selected flag.
     *
     * @param jobId
     * @param enabled
     */
    void markJobEnabled(long jobId, boolean enabled);

    /**
     * @param jobIds
     * @param error
     */
    void markJobError(Collection<Long> jobIds, boolean error);

    /**
     * Save batch job instance associated with Unidata job.
     *
     * @param jobId       Job id.
     * @param batchJobId  Batch job instance id,
     * @param username    Current username.
     * @param dateCreated Current date.
     */
    void saveBatchJobInstance(long jobId, long batchJobId, String username, Date dateCreated);

    /**
     * Find all batch job executions.
     *
     * @param jobIds Job ids to query
     * @return Found execution ids associated with queried ids.
     */
    Map<Long, List<Long>> findAllBatchJobIds(List<Long> jobIds);

    /**
     * Find last batch job instance.
     *
     * @param jobIds Job ids to query.
     * @return Found last executions associated with wueried ids.
     */
    Map<Long, Long> findLastBatchJobIds(List<Long> jobIds);

    /**
     * Check whether job exists or not with same parameters.
     *
     * @param jobPo Job object to check against.
     * @return Job object with same parameters.
     */
    JobPO checkJobByParams(JobPO jobPo);

    /**
     * Create new Job trigger
     *
     * @param jobTrigger Job trigger database object.
     * @return id of newly created trigger.
     */
    long insertJobTrigger(JobTriggerPO jobTrigger);

    /**
     * Update Job trigger.
     *
     * @param jobTrigger Job trigger database object.
     * @return <code>true</code> if successful.
     */
    boolean updateJobTrigger(JobTriggerPO jobTrigger);

    /**
     * Find triggers for specified job id.
     *
     * @param jobId Job id.
     * @return Found job trigger ids.
     */
    List<JobTriggerPO> findAllJobTriggers(long jobId);

    /**
     * Find job successfull trigger ids.
     *
     * @param jobId Job id.
     * @return found triggers or <code>null</code>.
     */
    List<Long> getTriggerSuccessfulJobIds(long jobId);

    /**
     * Find job failed trigger ids.
     *
     * @param jobId Job id.
     * @return found triggers or <code>null</code>.
     */
    List<Long> getTriggerFailedJobIds(long jobId);

    /**
     * Find Job trigger by job id and trigger id.
     * @param jobId        Job id.
     * @param triggerId    Trigger id.
     * @return Job trigger object or <code>null</code>
     */
    JobTriggerPO findJobTrigger(Long jobId, Long triggerId);

    /**
     * Find Job trigger by name.
     * @param name    Trigger name.
     * @return Job trigger object or <code>null</code>.
     */
    JobTriggerPO findJobTrigger(String name);

    /**
     * Remove trigger for specified job id and trigger id.
     * @param jobId        Job id.
     * @param triggerId    Trigger id
     * @return <code>true</code> if success.
     */
    boolean removeJobTrigger(Long jobId, Long triggerId);

    /**
     * Find jobs with specified IDs. Load jobs parameters.
     *
     * @param jobsIds Jobs IDs to find
     * @return List of jobs.
     */
    List<JobPO> findJobWithParameters(List<Long> jobsIds);

    void saveJobs(List<JobPO> jobs);

	List<String> getAllTags();

	void updateJobTags(JobPO job);
}
