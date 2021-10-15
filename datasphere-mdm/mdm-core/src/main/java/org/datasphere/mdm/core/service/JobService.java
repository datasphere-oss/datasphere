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

package com.huahui.datasphere.mdm.core.service;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.huahui.datasphere.mdm.core.dto.PaginatedResultDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobExecutionDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobExecutionStepDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobParameterDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobTriggerDTO;
import com.huahui.datasphere.mdm.core.exception.JobException;
import com.huahui.datasphere.mdm.core.service.job.JobTemplateParameters;
import com.huahui.datasphere.mdm.core.type.job.JobFilter;

public interface JobService {

    String SUCCESS_FINISH_JOB_ID_PARAMETER = "$success_finish_job_id$";
    String FAIL_FINISH_JOB_ID_PARAMETER = "$fail_finish_job_id$";

    Collection<JobDTO> findAll();

    /**
     * @param jobDto
     * @return
     */
    JobDTO saveJob(JobDTO jobDto);

    /**
     * @param jobId
     */
    void removeJob(long jobId);

    JobExecutionDTO runJob(long jobId, Collection<JobParameterDTO> jobParameters);

    JobExecutionDTO jobStatus(long jobId);

    JobDTO findJob(long jobId);

    Long stopJob(long jobId);

    /**
     * @param jobName
     * @return
     * @throws JobException
     */
    JobExecutionDTO run(String jobName) throws JobException;

    /**
     * @param jobName
     * @param jobParameters
     * @return
     * @throws JobException
     */
    JobExecutionDTO run(String jobName, Collection<JobParameterDTO> jobParameters) throws JobException;

    /**
     * @param jobId
     * @return
     * @throws JobException
     */
    JobExecutionDTO start(long jobId) throws JobException;

    /**
     * @param jobId jobId
     * @param  parentJobExecutionId parent job execution id
     * @return
     * @throws JobException
     */
    JobExecutionDTO start(long jobId, Long parentJobExecutionId) throws JobException;

    /**
     * Start system job
     *
     * @param jobDto job description
     * @return job execution
     */
    JobExecutionDTO startSystemJob(JobDTO jobDto);

    /**
     * Restart job by jobExecution ID.
     *
     * @param jobExecutionId
     */
    Long restart(long jobExecutionId);

    /**
     * @param jobId
     * @return
     * @throws JobException
     */
    Long stop(long jobId) throws JobException;

    /**
     * @param jobId
     * @throws JobException
     */
    void enableJob(long jobId) throws JobException;

    /**
     * @param jobId
     * @throws JobException
     */
    void disableJob(long jobId) throws JobException;

    /**
     * @param jobs
     * @throws JobException
     */
    void markErrorJobs(Collection<JobDTO> jobs, boolean error) throws JobException;

    List<JobDTO> findAllJobsWithParams();

    /**
	 * Search jobs by filter.
	 * @param filter job filter.
	 * @return search results.
	 */
    PaginatedResultDTO<JobDTO> searchJobs(JobFilter filter);

    /**
     * @return
     */
    Collection<String> findAllJobReferences();

    /**
     *
     * @return
     */
    Collection<String> findAllUIJobReferences();

    /**
     * @param jobName
     * @return
     */
    JobTemplateParameters findJobTemplateParameters(String jobName);

    /**
     * @return
     */
    @Deprecated
    List<JobExecutionDTO> findAllJobExecutions(long jobId);

    /**
     *
     * @param jobId
     * @param fromInd
     * @param itemCount
     * @return
     */
    PaginatedResultDTO<JobExecutionDTO> findJobExecutions(long jobId, long fromInd, int itemCount);

    PaginatedResultDTO<JobExecutionStepDTO> searchStepExecutions(long jobExecutionId, long fromInd, int itemCount);

    /**
     * @param jobIds
     * @return
     */
    Map<Long, JobExecutionDTO> findLastJobExecutions(List<Long> jobIds);

    /**
     *
     * @param jobIds
     * @return
     */
    Map<Long, Long> findLastJobExecutionIds(List<Long> jobIds);

    double getJobProgress(long jobId, long execId);

    List<JobTriggerDTO> findJobTriggers(Long jobId);

    JobTriggerDTO saveJobTrigger(JobTriggerDTO jobTriggerDto);

    void removeTrigger(Long jobId, Long triggerId);

    boolean isJobRestartable(long jobId);

    /**
     * @param complexParameter - any object which you want to pass to job
     * @return key in storage which help find complex parameter in partitioner
     */
    String putComplexParameter(Object complexParameter);

    /**
     * Schedule unidata job.
     *
     * @param jobId          Unidata job id.
     * @param cronExpression Cron expression.
     */
    void schedule(long jobId, String cronExpression);

    /**
     * Schedule unidata job.
     *
     * @param jobId          Unidata job id.
     * @param cronExpression Cron expression.
     * @param parentJobExecutionId parent job execution id
     */
    void schedule(long jobId, String cronExpression, Long parentJobExecutionId);

    void exportJobs(String userLogin, List<Long> jobsIds);

    void importJobs(String userLogin, Path path);

    List<String> getAllTags();
}
