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

package com.huahui.datasphere.mdm.core.service.impl;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import com.huahui.datasphere.mdm.system.dto.Param;
import com.huahui.datasphere.mdm.system.util.IdUtils;

import com.huahui.datasphere.mdm.core.convert.job.JobConverter;
import com.huahui.datasphere.mdm.core.dao.JobDao;
import com.huahui.datasphere.mdm.core.dto.PaginatedResultDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobExecutionDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobExecutionPaginatedResultDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobExecutionStepDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobPaginatedResultDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobParameterDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobTemplateParameterDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobTriggerDTO;
import com.huahui.datasphere.mdm.core.dto.job.StepExecutionPaginatedResultDTO;
import com.huahui.datasphere.mdm.core.exception.CoreExceptionIds;
import com.huahui.datasphere.mdm.core.exception.JobException;
import com.huahui.datasphere.mdm.core.po.job.JobPO;
import com.huahui.datasphere.mdm.core.po.job.JobParameterPO;
import com.huahui.datasphere.mdm.core.po.job.JobTriggerPO;
import com.huahui.datasphere.mdm.core.service.JobService;
import com.huahui.datasphere.mdm.core.service.job.ComplexJobParameterHolder;
import com.huahui.datasphere.mdm.core.service.job.JobCommonParameters;
import com.huahui.datasphere.mdm.core.service.job.JobEnumParamExtractor;
import com.huahui.datasphere.mdm.core.service.job.JobParameterProcessor;
import com.huahui.datasphere.mdm.core.service.job.JobParameterValidator;
import com.huahui.datasphere.mdm.core.service.job.JobTemplateParameters;
import com.huahui.datasphere.mdm.core.service.job.JobWithParamsRegistry;
import com.huahui.datasphere.mdm.core.type.job.JobEnumType;
import com.huahui.datasphere.mdm.core.type.job.JobExecutionFilter;
import com.huahui.datasphere.mdm.core.type.job.JobFilter;
import com.huahui.datasphere.mdm.core.type.job.StepExecutionFilter;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;

/**
 * Job service to manipulate all jobs deployed in system.
 *
 * @author Alexander Magdenko
 */
@Service
public class JobServiceImpl implements JobService, ApplicationContextAware {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);

    private static final String QUARTZ_GROUP = "quartz-batch";

    @Autowired
    private JobLauncher jobLauncher;

    private ApplicationContext applicationContext;

    @Autowired
    private JobDao jobDao;

    @Autowired
    private JobOperator jobOperator;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private JobWithParamsRegistry jobRegistry;

    @Autowired
    private ComplexJobParameterHolder complexJobParameterHolder;

    @Autowired
    private SchedulerFactoryBean quartzSchedulerFactory;

    /** The tx manager. */
    @Autowired
    private PlatformTransactionManager txManager;

    private DefaultJobParameterProcessor defaultJobParameterProcessor = new DefaultJobParameterProcessor();

    /**
     * Parameter processors..
     */
    private Map<String, JobParameterProcessor> parameterProcessors;

    /**
     * @param parameterProcessors the operations to set
     */
    @Resource(name = "jobParameterProcessorsMap")
    public void setOperations(Map<String, JobParameterProcessor> parameterProcessors) {
        this.parameterProcessors = parameterProcessors;
    }

    private static boolean isCronJob(JobPO job) {
        return StringUtils.hasText(job.getCronExpression());
    }

    /**
     * @param jobName job name
     * @return
     * @throws JobException
     */
    @Override
    public JobExecutionDTO run(String jobName) {
        return run(jobName, Collections.emptyList());
    }

    /**
     * @param jobName
     * @param jobParameters
     * @return
     * @throws JobException
     */
    @Override
    public JobExecutionDTO run(String jobName, Collection<JobParameterDTO> jobParameters) {
        Objects.requireNonNull(jobName, "Job name must be not null");

        final Job job = (Job) applicationContext.getBean(jobName);

        JobExecution execution;
        try {
            final JobParameters springJobParameters = convertJobParameters(new ArrayList<>(jobParameters),
                    parameterProcessors.get(job.getName()));

            execution = jobLauncher.run(job, springJobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobParametersInvalidException
                | JobInstanceAlreadyCompleteException e) {
            throw new JobException("Failed to run job:" + jobName, e, CoreExceptionIds.EX_JOB_BATCH_EXECUTION_FAILED,
                    jobName);
        }

        LOGGER.debug("Run job [jobName={}, jobStatus={}]", jobName, execution.getStatus());

        return JobConverter.jobExecutionDTOFromJobExecution(
                null/*JobConverter.convertJobPoToDto(job)*/,
                execution
        );
    }

    @Override
    public JobExecutionDTO runJob(final long jobId, final Collection<JobParameterDTO> jobParameters) {

        final JobPO job = jobDao.findJob(jobId);
        if (job == null) {
            final String message = "Job with id [{}] not found";
            LOGGER.warn(message, jobId);
            throw new JobException(message, CoreExceptionIds.EX_JOB_NOT_FOUND, jobId);
        }

        validateJobParameters(job.getJobNameReference(), jobParameters, true);

        final JobExecution jobExecution = start(job, jobParameters);
        return JobConverter.jobExecutionDTOFromJobExecution(
                JobConverter.convertJobPoToDto(job),
                jobExecution
        );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * @param jobId
     * @return
     * @throws JobException
     */
    @Override
    public JobExecutionDTO start(long jobId) {
        JobPO job = jobDao.findJob(jobId);

        if (job == null) {
            final String message = "Job with id [{}] not found";
            LOGGER.warn(message, jobId);
            throw new JobException(message, CoreExceptionIds.EX_JOB_NOT_FOUND, jobId);
        }

        final JobExecution jobExecution = start(job, Collections.singletonList(new JobParameterDTO("jobName", job.getName())));
        return JobConverter.jobExecutionDTOFromJobExecution(
                JobConverter.convertJobPoToDto(job),
                jobExecution
        );
    }

    /**
     * @param jobId
     * @return
     * @throws JobException
     */
    @Override
    public JobExecutionDTO start(long jobId, Long parentJobExecutionId) {
        JobPO job = jobDao.findJob(jobId);

        if (job == null) {
            final String message = "Job with id [{}] not found";
            LOGGER.warn(message, jobId);
            throw new JobException(message, CoreExceptionIds.EX_JOB_NOT_FOUND, jobId);
        }

        return JobConverter.jobExecutionDTOFromJobExecution(
                JobConverter.convertJobPoToDto(job),
                start(job, Arrays.asList(new JobParameterDTO("jobName", job.getName()),
                        new JobParameterDTO(JobCommonParameters.PARAM_PARENT_JOB_EXECUTION_ID, parentJobExecutionId)))
        );
    }

    private JobExecution start(final JobPO job, final Collection<JobParameterDTO> jobParameters) throws JobException {

        if (!job.isEnabled() || job.isError()) {
            final String message = "Job with id [{}] not enabled";
            LOGGER.warn(message, job.getId());
            throw new JobException(message, CoreExceptionIds.EX_JOB_DISABLED, job.getId());
        }

        if (findLastRunningJobExecutionId(job.getId()) != null) {
            final String message = "Job with name [{}] is already running";
            LOGGER.warn(message, job.getName());
            throw new JobException(message, CoreExceptionIds.EX_JOB_ALREADY_RUNNING, job.getName());
        }

        final List<JobParameterPO> jobParams = jobDao.getJobParameters(job.getId());

        final List<JobParameterDTO> dtoParams = merge(
                JobConverter.convertParamPoToDto(jobParams),
                jobParameters
        );

        addTriggerJobParams(job.getId(), dtoParams);
        addLastSuccessJobExecutionDate(job.getId(), dtoParams);

        JobParameters parameters = convertJobParameters(dtoParams, parameterProcessors.get(job.getJobNameReference()));
        JobExecution jobExecution;

        try {
            Job batchJob = jobRegistry.getJob(job.getJobNameReference());

            parameters = addGeneralParameters(parameters);

            jobExecution = jobLauncher.run(batchJob, parameters);

            jobDao.saveBatchJobInstance(job.getId(), jobExecution.getJobId(), SecurityUtils.getCurrentUserName(), new Date());

            LOGGER.debug("Started job [jobId={}, jobInstanceId={}, jobExecutionId={}]",
                    job.getId(), jobExecution.getJobId(), jobExecution.getId());

        } catch (final JobExecutionException e) {
            throw new JobException("Failed to execute job [{}] with jobReference [{}]",
                    e, CoreExceptionIds.EX_JOB_BATCH_EXECUTION_FAILED,
                    job.getName(), job.getJobNameReference());
        }

        return jobExecution;
    }

    private List<JobParameterDTO> merge(
            final Collection<JobParameterDTO> fromDB,
            final Collection<JobParameterDTO> fromRequest
    ) {
        final Set<JobParameterDTO> result = new HashSet<>(fromDB);
        result.removeAll(fromRequest);
        result.addAll(fromRequest);
        return new ArrayList<>(result);
    }

    /**
     * Start system job
     *
     * @param jobDto job description
     * @return job execution
     */
    @Override
    public JobExecutionDTO startSystemJob(final JobDTO jobDto) {
        try {
            JobParameters parameters = convertJobParameters(jobDto.getParameters(), null);
            parameters = addGeneralParameters(parameters);
            Job batchJob = jobRegistry.getJob(jobDto.getJobNameReference());
            return JobConverter.jobExecutionDTOFromJobExecution(
                    null,
                    jobLauncher.run(batchJob, parameters)
            );
        } catch (final JobExecutionException e) {
            throw new JobException("Failed to execute job [{}] with jobReference [{}]",
                    e, CoreExceptionIds.EX_JOB_BATCH_EXECUTION_FAILED,
                    jobDto.getName(), jobDto.getJobNameReference());
        }
    }

    private void addTriggerJobParams(final long jobId, final List<JobParameterDTO> dtoParams) {
        final List<JobTriggerDTO> jobTriggerIds = findJobTriggers(jobId);

        int successTriggerCount = 0;
        int failTriggerCount = 0;
        for (final JobTriggerDTO t : jobTriggerIds) {
            if (t.getSuccessRule()) {
                dtoParams.add(new JobParameterDTO(JobService.SUCCESS_FINISH_JOB_ID_PARAMETER + successTriggerCount++, t.getStartJobId()));
            } else {
                dtoParams.add(new JobParameterDTO(JobService.FAIL_FINISH_JOB_ID_PARAMETER + failTriggerCount++, t.getStartJobId()));
            }
        }
    }

    /**
     * Restart job by jobExecution ID.
     *
     * @param jobExecutionId
     */
    @Override
    public Long restart(long jobExecutionId) {
        LOGGER.debug("Restart job with executionId: {}", jobExecutionId);

        Long newJobExecutionId = null;
        try {
            newJobExecutionId = jobOperator.restart(jobExecutionId);

            LOGGER.debug("Restarted job [oldJobExecutionId={}, newJobExecutionId={}]",
                    jobExecutionId, newJobExecutionId);
        } catch (UnexpectedJobExecutionException | JobExecutionException e) {
            if (e.getCause() instanceof JobExecutionAlreadyRunningException) {
                throw new JobException("Failed to restart job with jobExecutionId: " + jobExecutionId, e,
                        CoreExceptionIds.EX_JOB_BATCH_RESTART_FAILED_ALREADY_RUNNING, jobExecutionId);
            }

            throw new JobException("Failed to restart job with jobExecutionId: " + jobExecutionId, e,
                    CoreExceptionIds.EX_JOB_BATCH_RESTART_FAILED, jobExecutionId);
        }

        return newJobExecutionId;
    }

    /**
     * @param jobId
     * @return
     * @throws JobException
     */
    @Override
    public Long stop(long jobId) {
        JobPO job = jobDao.findJob(jobId);

        if (job == null) {
            throw new JobException("Job with id [" + jobId + "] not found", CoreExceptionIds.EX_JOB_NOT_FOUND, jobId);
        }

        return stop(job);
    }

    /**
     * @param jobId
     * @throws JobException
     */
    @Override
    @Transactional
    public void enableJob(long jobId) {
        JobPO job = jobDao.findJob(jobId);

        if (job == null) {
            throw new JobException("Job with id [" + jobId + "] not found", CoreExceptionIds.EX_JOB_NOT_FOUND, jobId);
        }

        jobDao.markJobEnabled(jobId, true);

        if (isCronJob(job)) {
            schedule(job.getId(), job.getCronExpression());
        }
    }

    /**
     * @param jobId
     * @throws JobException
     */
    @Override
    @Transactional
    public void disableJob(long jobId) {
        JobPO job = jobDao.findJob(jobId);

        if (job == null) {
            throw new JobException("Job with id [" + jobId + "] not found", CoreExceptionIds.EX_JOB_NOT_FOUND, jobId);
        }

        jobDao.markJobEnabled(jobId, false);

        unschedule(jobId);

        // Make stop for already started job.
        stop(job);
    }

    /**
     * @param jobs
     * @throws JobException
     */
    @Override
    @Transactional
    public void markErrorJobs(Collection<JobDTO> jobs, boolean error) {
        Set<Long> jobIds = jobs.stream().map(JobDTO::getId).collect(Collectors.toSet());

        if (error) {
            jobDao.markJobError(jobIds, true);

            for (JobDTO job : jobs) {
                unschedule(job.getId());

                // Make stop for already started job.
                stop(JobConverter.convertJobDtoToPo(job));
            }
        } else {
            jobDao.markJobError(jobIds, false);

            List<JobPO> jobPOs = JobConverter.convertJobsDtoToPo(jobs);

            for (JobPO job : jobPOs) {
                if (isCronJob(job)) {
                    schedule(job.getId(), job.getCronExpression());
                }
            }
        }
    }

    @Override
    @Transactional
    public List<JobDTO> findAllJobsWithParams() {
        final List<JobPO> dbJobs = jobDao.getJobsWithParameters();

        return JobConverter.convertJobsPoToDto(dbJobs);
    }

    /**
     * @param filter
     * @return
     */
    @Override
    @Transactional
    public PaginatedResultDTO<JobDTO> searchJobs(JobFilter filter) {
        JobPaginatedResultDTO<JobDTO> result = new JobPaginatedResultDTO<>();


        List<JobPO> jobPOs = jobDao.searchJobs(filter);

        if (!CollectionUtils.isEmpty(jobPOs)) {
            Set<Long> jobIds = jobPOs.stream().map(JobPO::getId).collect(Collectors.toSet());

            Map<Long, List<JobParameterPO>> jobParametersMap = jobDao.getJobsParameters(new ArrayList<>(jobIds));

            jobPOs.forEach(jobPO -> jobPO.setParameters(jobParametersMap.get(jobPO.getId())));

            result.setPage(JobConverter.convertJobsPoToDto(jobPOs));
        }

        result.setTotalCount(jobDao.getJobsCount(filter));

        return result;
    }

    /**
     * @return
     */
    @Override
    public Collection<String> findAllJobReferences() {
        return jobRegistry.getJobNames();
    }

    /**
     * @return
     */
    @Override
    public Collection<String> findAllUIJobReferences() {
        return jobRegistry.getJobParameterNames();
    }

    /**
     * @param jobName
     * @return
     */
    @Override
    public JobTemplateParameters findJobTemplateParameters(String jobName) {

        JobTemplateParameters parameters = jobRegistry.getJobTemplateParameters(jobName);
        JobParameterProcessor processor = parameterProcessors.get(jobName);
        if (Objects.nonNull(processor)) {
            parameters = processor.filter(parameters);
        }

        return parameters;
    }

    /**
     * @return
     */
    @Override
    @Deprecated
    @Transactional
    public List<JobExecutionDTO> findAllJobExecutions(long jobId) {
        List<Long> batchJobIds = jobDao.findAllBatchJobIds(Collections.singletonList(jobId)).get(jobId);

        List<JobExecution> allExecutions = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(batchJobIds)) {
            Map<Long, List<JobExecution>> jobExecutionsMap = getJobExplorer().getJobExecutions(batchJobIds);

            if (MapUtils.isNotEmpty(jobExecutionsMap)) {
                jobExecutionsMap.values().stream()
                        .filter(v -> !CollectionUtils.isEmpty(v))
                        .forEach(allExecutions::addAll);
            }
        }

        allExecutions.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));

        return allExecutions.stream()
                .map(e -> JobConverter.jobExecutionDTOFromJobExecution(null, e))
                .collect(Collectors.toList());
    }

    /**
     * @param jobId
     * @param fromInd
     * @param itemCount
     * @return
     */
    @Override
    @Transactional
    public PaginatedResultDTO<JobExecutionDTO> findJobExecutions(long jobId, long fromInd, int itemCount) {
        List<Long> batchJobIds = jobDao.findAllBatchJobIds(Collections.singletonList(jobId)).get(jobId);

        JobExecutionPaginatedResultDTO<JobExecutionDTO> result = new JobExecutionPaginatedResultDTO<>();
        result.setTotalCount(0);

        if (!CollectionUtils.isEmpty(batchJobIds)) {
            JobExecutionFilter filter = new JobExecutionFilter();
            filter.setJobInstanceIds(batchJobIds);
            filter.setFromInd(fromInd);
            filter.setItemCount(itemCount);

            final PaginatedResultDTO<JobExecution> r = getJobExplorer().searchJobExecutions(filter);
            result.setTotalCount(r.getTotalCount());
            result.setPage(
                    r.getPage().stream()
                    .map(e -> JobConverter.jobExecutionDTOFromJobExecution(null, e))
                    .collect(Collectors.toList())
            );
        }

        return result;
    }

    @Override
    @Transactional
    public PaginatedResultDTO<JobExecutionStepDTO> searchStepExecutions(long jobExecutionId, long fromInd, int itemCount) {

        StepExecutionFilter filter = new StepExecutionFilter();
        filter.setJobExecutionId(jobExecutionId);
        filter.setFromInd(fromInd);
        filter.setItemCount(itemCount);

        final PaginatedResultDTO<StepExecution> stepExecutions = getJobExplorer().searchStepExecutions(filter);
        PaginatedResultDTO<JobExecutionStepDTO> result = new StepExecutionPaginatedResultDTO<>();
        result.setTotalCount(stepExecutions.getTotalCount());
        result.setPage(stepExecutions.getPage().stream()
            .map(JobConverter::stepExecutionDTOFromStepExecution)
            .collect(Collectors.toList()));

        return result;
    }

    /**
     * @param jobDto
     * @return
     */
    @Override
    @Transactional
    public JobDTO saveJob(final JobDTO jobDto) {
        Objects.requireNonNull(jobDto);

        validateJobParameters(jobDto.getJobNameReference(), jobDto.getParameters(), false);

        final JobDTO saved = jobDto.getId() == null || jobDto.getId() <= 0
                ? insertJob(jobDto)
                : updateJob(jobDto);

        return findJob(saved.getId());
    }

    private void validateJobParameters(
            final String jobNameReference,
            final Collection<JobParameterDTO> jobParameters,
            final boolean onlyUnknown
    ) {
        JobTemplateParameters jobTemplateParameters = findJobTemplateParameters(jobNameReference);

        if (jobTemplateParameters == null) {
            throw new JobException(
                    "Job with name reference [" + jobNameReference + "] not found!",
                    CoreExceptionIds.EX_JOB_NOT_FOUND,
                    jobNameReference
            );
        }

        if (MapUtils.isNotEmpty(jobTemplateParameters.getValidators())) {
            List<String> validateErrors = jobParameters.stream()
                    .map(param -> validateParameter(jobTemplateParameters.getValidators().get(param.getName()),
                            jobParameters, param.getName()))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(validateErrors)) {
                throw new JobException(
                        "Wrong parameters for job [" + jobNameReference + "]",
                        CoreExceptionIds.EX_JOB_PARAMETERS_VALIDATION_ERRORS,
                        validateErrors
                );
            }
        }

        final Set<String> templateParamNames =
                new HashSet<>(
                        Optional.ofNullable(findJobTemplateParameters(jobNameReference))
                                .map(JobTemplateParameters::getValueMap)
                                .orElseGet(HashMap::new)
                                .keySet()
                );

        final Set<String> parameters = Optional.ofNullable(jobParameters).orElseGet(ArrayList::new).stream()
                .map(JobTemplateParameterDTO::getName).collect(Collectors.toSet());

        final List<String> unknownParameters =
                parameters.stream()
                        .filter(((Predicate<String>) templateParamNames::contains).negate())
                        .collect(Collectors.toList());
        if (!unknownParameters.isEmpty()) {
            throw new JobException(
                    "Unknown parameters for job [" + jobNameReference + "]: " + unknownParameters,
                    CoreExceptionIds.EX_JOB_UNKNOWN_PARAMETERS,
                    jobNameReference,
                    unknownParameters
            );
        }

        if (!onlyUnknown) {
            final List<String> notSet = templateParamNames.stream()
                    .filter(((Predicate<String>) parameters::contains).negate())
                    .collect(Collectors.toList());
            if (!notSet.isEmpty()) {
                throw new JobException(
                        "Parameters for job [" + jobNameReference + "] not set: " + notSet,
                        CoreExceptionIds.EX_JOB_PARAMETERS_NOT_SET,
                        jobNameReference,
                        notSet
                );
            }
        }
    }

    /**
     * Validate job parameters for imported job from JSON file.
     * @param jobDto
     */
    private void validateAndResolveImportedJobParameters(final JobDTO jobDto) {

        final String jobNameReference = jobDto.getJobNameReference();
        final Collection<JobParameterDTO> jobParameters = jobDto.getParameters();

        JobTemplateParameters jobTemplateParameters = findJobTemplateParameters(jobNameReference);

        if (jobTemplateParameters == null) {
            throw new JobException(
                    "Job with name reference [" + jobNameReference + "] not found!",
                    CoreExceptionIds.EX_JOB_NOT_FOUND,
                    jobNameReference
            );
        }

        if (MapUtils.isNotEmpty(jobTemplateParameters.getValidators())) {
            List<String> validateErrors = jobParameters.stream()
                    .map(param -> validateParameter(jobTemplateParameters.getValidators().get(param.getName()),
                            jobParameters, param.getName()))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(validateErrors)) {
                throw new JobException(
                        "Wrong parameters for job [" + jobNameReference + "]",
                        CoreExceptionIds.EX_JOB_PARAMETERS_VALIDATION_ERRORS,
                        validateErrors
                );
            }
        }

        final Set<String> templateParamNames =
                new HashSet<>(
                        Optional.of(jobTemplateParameters)
                                .map(JobTemplateParameters::getValueMap)
                                .orElseGet(HashMap::new)
                                .keySet()
                );

        final Set<String> parameters = Optional.ofNullable(jobParameters).orElseGet(ArrayList::new).stream()
                .map(JobTemplateParameterDTO::getName)
                .collect(Collectors.toSet());

        final List<String> unknownParameters =
                parameters.stream()
                        .filter(((Predicate<String>) templateParamNames::contains).negate())
                        .collect(Collectors.toList());

        if (!unknownParameters.isEmpty() && jobParameters != null) {
            LOGGER.warn(
                    "Unknown parameters were deleted for job [jobReferenceName={}, jobName={}, parameters={}]",
                    jobNameReference,
                    jobDto.getName(),
                    unknownParameters
            );

            jobParameters.removeIf(jobParameter -> unknownParameters.contains(jobParameter.getName()));
        }

        final List<String> notSet = templateParamNames.stream()
                .filter(((Predicate<String>) parameters::contains).negate())
                .collect(Collectors.toList());
        if (!notSet.isEmpty()) {
            LOGGER.warn(
                    "Parameters not set for job [jobReferenceName={}}, jobName={}}, parameters={}]",
                    jobNameReference,
                    jobDto.getName(),
                    notSet
            );

            // Add missing parameter.
            Optional.of(jobTemplateParameters)
                    .map(JobTemplateParameters::getValueMap)
                    .orElseGet(HashMap::new)
                    .forEach((paramName, paramValue) -> {
                        if (!notSet.contains(paramName)) {
                            return;
                        }

                        JobParameterDTO jobParameterDTO = null;
                        if (paramValue instanceof String) {
                            jobParameterDTO = new JobParameterDTO(paramName, (String) paramValue);
                        } else if (paramValue instanceof Long) {
                            jobParameterDTO = new JobParameterDTO(paramName, (Long) paramValue);
                        } else if (paramValue instanceof Double) {
                            jobParameterDTO = new JobParameterDTO(paramName, (Double) paramValue);
                        } else if (paramValue instanceof ZonedDateTime) {
                            jobParameterDTO = new JobParameterDTO(paramName, (ZonedDateTime) paramValue);
                        } else if (paramValue instanceof Boolean) {
                            jobParameterDTO = new JobParameterDTO(paramName, (Boolean) paramValue);
                        } else if (paramValue instanceof JobEnumParamExtractor) {
                            final JobEnumType jobEnum = ((JobEnumParamExtractor) paramValue).extractParameters();

                            // Take first parameter value from extractor.
                            if (jobEnum != null &&
                                    !org.springframework.util.CollectionUtils.isEmpty(jobEnum.getParameters())) {
                                switch (jobEnum.getParameterType()) {
                                    case STRING:
                                        jobParameterDTO = new JobParameterDTO(paramName,
                                                (String)jobEnum.getParameters().get(0));
                                        break;
                                    case DATE:
                                        jobParameterDTO = new JobParameterDTO(paramName,
                                                (ZonedDateTime)jobEnum.getParameters().get(0));
                                        break;
                                    case LONG:
                                        jobParameterDTO = new JobParameterDTO(paramName,
                                                (Long)jobEnum.getParameters().get(0));
                                        break;
                                    case DOUBLE:
                                        jobParameterDTO = new JobParameterDTO(paramName,
                                                (Double)jobEnum.getParameters().get(0));
                                        break;
                                    case BOOLEAN:
                                        jobParameterDTO = new JobParameterDTO(paramName,
                                                (Boolean) jobEnum.getParameters().get(0));
                                        break;
                                    default:
                                        jobParameterDTO = null;
                                }
                            }
                        }

                        if (jobParameterDTO == null) {
                            throw new JobException(
                                    "Failed to prepare value for missing parameter [jobName=" + jobDto.getName() +
                                            ", jobReferenceName=" + jobNameReference +
                                            ", parameterName=" + paramName + "]",
                                    CoreExceptionIds.EX_JOB_PARAMETERS_VALIDATION_ERRORS,
                                    paramName
                            );
                        } else {
                            LOGGER.warn("Added missing parameter for job [jobReferenceName=" + jobNameReference +
                                            ", jobName=" + jobDto.getName() +
                                            ", parameterName=" + paramName +
                                            ", parameterValue=" + jobParameterDTO.getArrayValue()[0] +
                                            "]"
                                    );
                        }

                        jobDto.getParameters().add(jobParameterDTO);
                    });
        }
    }

    private List<String> validateParameter(JobParameterValidator validator, Collection<JobParameterDTO> jobParameters, String paramName) {
        return validator == null ? Collections.emptyList() : validator.validate(paramName, jobParameters);
    }

    /**
     * @param jobId
     */
    @Override
    @Transactional
    public void removeJob(long jobId) {
        JobPO job = jobDao.findJob(jobId);

        if (job == null) {
            throw new JobException("Job with id [" + jobId + "] not found", CoreExceptionIds.EX_JOB_NOT_FOUND, jobId);
        }

        stop(job);

        unschedule(jobId);

        jobDao.removeJob(jobId);

        LOGGER.debug("Job was removed: {}", jobId);
    }

    /**
     * @param jobDto
     * @return
     */
    private JobDTO insertJob(final JobDTO jobDto) {
        JobValidator.validateJob(jobDao, jobDto);

        final JobPO jobPo = JobConverter.convertJobDtoToPo(jobDto);

        jobPo.setEnabled(jobDto.isEnabled());
        jobPo.setError(false);
        jobPo.setCreateDate(new Date());
        jobPo.setCreatedBy(SecurityUtils.getCurrentUserName());

        addCreatedCommonParams(jobPo.getParameters());

        JobPO sameJobByParams = jobDao.checkJobByParams(jobPo);

        if (sameJobByParams != null) {
            throw new JobException("Job [" + sameJobByParams.getName() + "] with the same parameter set already exists",
                    CoreExceptionIds.EX_JOB_SAME_PARAMETERS,
                    sameJobByParams.getName(),
                    Collections.singletonList(new Param(JobValidator.VIOLATION_JOB_NAME, sameJobByParams.getName())));
        }

        jobDao.insertJob(jobPo);
        if (CollectionUtils.isNotEmpty(jobPo.getTags())) {
            jobDao.updateJobTags(jobPo);
        }
        if (jobPo.isEnabled() && !jobPo.isError() && isCronJob(jobPo)) {
            schedule(jobPo.getId(), jobDto.getCronExpression());
        }

        return JobConverter.convertJobPoToDto(jobPo);
    }

    /**
     * @param jobDto
     * @return
     */
    private JobDTO updateJob(final JobDTO jobDto) {
        JobValidator.validateJob(jobDao, jobDto);

        final JobPO jobPo = JobConverter.convertJobDtoToPo(jobDto);

        jobPo.setEnabled(jobDto.isEnabled());
        jobPo.setUpdateDate(new Date());
        jobPo.setUpdatedBy(SecurityUtils.getCurrentUserName());

        addUpdatedCommonParams(jobPo.getParameters());

        JobPO sameJobByParams = jobDao.checkJobByParams(jobPo);

        if (sameJobByParams != null) {
            throw new JobException("Job [" + sameJobByParams.getName() + "] with the same parameter set already exists",
                    CoreExceptionIds.EX_JOB_SAME_PARAMETERS,
                    sameJobByParams.getName(),
                    Collections.singletonList(new Param(JobValidator.VIOLATION_JOB_NAME, sameJobByParams.getName())));
        }

        unschedule(jobPo.getId());

        jobDao.updateJob(jobPo);

        if (jobDto.isEnabled() && !jobDto.isError() && isCronJob(jobPo)) {
            schedule(jobPo.getId(), jobPo.getCronExpression());
        }

        return JobConverter.convertJobPoToDto(jobPo);
    }

    /**
     * @param jobIds
     * @return
     */
    @Override
    public Map<Long, JobExecutionDTO> findLastJobExecutions(List<Long> jobIds) {
        return findLastJobExecutions(jobIds, false).entrySet().stream()
                .map(e -> Pair.of(e.getKey(), JobConverter.jobExecutionDTOFromJobExecution(null, e.getValue())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }


    private Map<Long, JobExecution> findLastJobExecutions(List<Long> jobIds, boolean loadSteps) {
        Map<Long, JobExecution> result = new HashMap<>();

        Map<Long, Long> jobMap = jobDao.findLastBatchJobIds(jobIds);

        Set<Long> batchJobIds = jobMap.values().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(batchJobIds)) {
            return result;
        }

        Map<Long, JobExecution> jobLastExecutionMap = getJobExplorer().getLastJobExecutions(batchJobIds, loadSteps);

        for (Entry<Long, Long> entry : jobMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }

            result.put(entry.getKey(), jobLastExecutionMap.get(entry.getValue()));
        }

        return result;
    }

    /**
     * @param jobIds
     * @return
     */
    @Override
    public Map<Long, Long> findLastJobExecutionIds(List<Long> jobIds) {
        Map<Long, Long> result = new HashMap<>();

        Map<Long, Long> jobMap = jobDao.findLastBatchJobIds(jobIds);

        Set<Long> batchJobIds = jobMap.values().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(batchJobIds)) {
            return Collections.emptyMap();
        }

        Map<Long, Long> jobLastExecutionIdMap = getJobExplorer().getLastJobExecutionIds(batchJobIds);

        for (Entry<Long, Long> entry : jobMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }

            result.put(entry.getKey(), jobLastExecutionIdMap.get(entry.getValue()));
        }

        return result;
    }

    @Override
    public double getJobProgress(final long jobId, final long execId) {
        final Long jobInstanceId = jobDao.findLastBatchJobIds(Collections.singletonList(jobId)).get(jobId);

        if (jobInstanceId != null) {
            final JobExecution jobExecution = getJobExplorer().
                    getLastJobExecutions(Collections.singletonList(jobInstanceId), true).get(jobInstanceId);

            if (jobExecution != null) {
                final Collection<StepExecution> steps = jobExecution.getStepExecutions();

                long amount = steps.size();
                long completedAmount = steps.stream()
                        .filter(step -> step.getStatus() == BatchStatus.COMPLETED)
                        .count();

                return 1.0 * completedAmount / amount;
            }
        }

        return 0;
    }

    @Override
    @Transactional
    public List<JobTriggerDTO> findJobTriggers(final Long jobId) {
        final List<JobTriggerPO> jobTriggers = jobDao.findAllJobTriggers(jobId);

        return JobConverter.convertJobTriggersPoToDTo(jobTriggers);
    }

    @Override
    @Transactional
    public JobTriggerDTO saveJobTrigger(final JobTriggerDTO jobTriggerDto) {
        Objects.requireNonNull(jobTriggerDto);

        if (jobTriggerDto.getId() == null) {
            return insertJobTrigger(jobTriggerDto);
        } else {
            return updateJobTrigger(jobTriggerDto);
        }
    }

    @Override
    @Transactional
    public void removeTrigger(Long jobId, Long triggerId) {
        Assert.notNull(jobId, "Job id cannot be null");
        Assert.notNull(triggerId, "Trigger id cannot be null");

        final JobTriggerPO jobTrigger = jobDao.findJobTrigger(jobId, triggerId);

        if (jobTrigger == null) {
            throw new JobException("Job trigger with job id [" + jobId + "] and trigger id ["
                    + triggerId + "] not found", CoreExceptionIds.EX_JOB_TRIGGER_NOT_FOUND, triggerId);
        }

        jobDao.removeJobTrigger(jobId, triggerId);

        LOGGER.debug("Job trigger was removed: jobId [{}[, triggerId [{}]", jobId, triggerId);
    }

    @Override
    public boolean isJobRestartable(long jobId) {
        JobPO job = jobDao.findJob(jobId);

        try {
            Job batchJob = jobRegistry.getJob(job.getJobNameReference());

            return batchJob.isRestartable();
        } catch (NoSuchJobException e) {
            LOGGER.warn("Failed to get batch job by jobName [jobId=" + jobId +
                    ", jobName=" + job.getJobNameReference() + ']', e);
        }

        return false;
    }

    @Override
    public JobExecutionDTO jobStatus(long jobId) {
        final Map<Long, JobExecution> lastJobExecutions = findLastJobExecutions(Collections.singletonList(jobId), true);
        if (!lastJobExecutions.containsKey(jobId)) {
            throw new JobException("Job execution not found", CoreExceptionIds.EX_JOB_EXECUTION_NOT_FOUND, jobId);
        }
        return JobConverter.jobExecutionDTOFromJobExecution(
                JobConverter.convertJobPoToDto(jobDao.findJob(jobId)),
                lastJobExecutions.get(jobId)
        );
    }

    private void addCreatedCommonParams(final List<JobParameterPO> params) {
        final Date createDate = new Date();
        final String createdBy = SecurityUtils.getCurrentUserName();

        for (final JobParameterPO param : params) {
            param.setCreateDate(createDate);
            param.setCreatedBy(createdBy);
        }
    }

    private void addUpdatedCommonParams(final List<JobParameterPO> params) {
        final Date updateDate = new Date();
        final String updatedBy = SecurityUtils.getCurrentUserName();

        for (final JobParameterPO param : params) {
            if (param.getId() != null) {
                param.setUpdateDate(updateDate);
                param.setUpdatedBy(updatedBy);
            } else {
                param.setCreateDate(updateDate);
                param.setCreatedBy(updatedBy);
            }
        }
    }

    private JobParameters convertJobParameters(List<JobParameterDTO> jobParameters, JobParameterProcessor parameterProcessor) {
        JobParametersBuilder builder = new JobParametersBuilder();

        if (!CollectionUtils.isEmpty(jobParameters)) {
            for (final JobParameterDTO jobParameter : jobParameters) {
                JobParameterProcessor processor = parameterProcessor != null
                        ? parameterProcessor
                        : defaultJobParameterProcessor;

                processor.process(jobParameter, builder);
            }
        }

        return builder.toJobParameters();
    }

    /**
     * Add general parameters as - userName + operationId + timestamp
     *
     * @param parameters - initial parameters
     * @return enriched parameters.
     */
    private JobParameters addGeneralParameters(JobParameters parameters) {

        JobParametersBuilder builder = new JobParametersBuilder(parameters);

        // Add timestamp in job params to avoid issue with completed job.
        builder.addString(JobCommonParameters.PARAM_START_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        // Add internal id - runId unconditionally, since operationId may be supplied from UI
        builder.addString(JobCommonParameters.PARAM_RUN_ID, IdUtils.v4String());

        if (isBlank(parameters.getString(JobCommonParameters.PARAM_USER_NAME))) {
            builder.addString(JobCommonParameters.PARAM_USER_NAME, SecurityUtils.getCurrentUserName());
            builder.addString(JobCommonParameters.PARAM_USER_TOKEN, SecurityUtils.getCurrentUserToken());
        }

        if (isBlank(parameters.getString(JobCommonParameters.PARAM_OPERATION_ID))) {
            builder.addString(JobCommonParameters.PARAM_OPERATION_ID, IdUtils.v4String());
        }

        return builder.toJobParameters();
    }

    /**
     * @param job
     * @return
     */
    private Long stop(JobPO job) {
        long jobId = job.getId();

        Long jobInstanceId = jobDao.findLastBatchJobIds(Collections.singletonList(jobId)).get(jobId);

        if (jobInstanceId == null) {
            LOGGER.debug("No jobInstances found for jobId: {}", jobId);
            return null;
        }

        try {
            final Long executionId = findLastRunningJobExecutionId(jobId);

            if (executionId != null) {
                jobOperator.stop(executionId);
            }

            return executionId;
        } catch (final JobExecutionException e) {
            throw new JobException("Failed to stop job: " + jobId, e,
                    CoreExceptionIds.EX_JOB_BATCH_STOP_FAILED, job.getJobNameReference());
        }
    }

    /**
     * Schedule unidata job.
     *
     * @param jobId Unidata job id.
     * @param cronExpression Cron expression.
     */
    @Override
    public void schedule(final long jobId, final String cronExpression, final Long parentJobExecutionId) {
        try {
            JobBuilder jobBuilder = JobBuilder
                    .newJob(JobLauncherDetail.class)
                    .withIdentity(createJobKey(jobId))
                    .usingJobData("jobId", String.valueOf(jobId));

            if (parentJobExecutionId != null) {
                jobBuilder.usingJobData(JobCommonParameters.PARAM_PARENT_JOB_EXECUTION_ID, String.valueOf(parentJobExecutionId));
            }
            JobDetail jobDetail = jobBuilder.build();
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(createTriggerKey(jobId))
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .build();

            quartzSchedulerFactory.getScheduler().scheduleJob(jobDetail, trigger);

            LOGGER.debug("Schedule job in quartz [jobId={}, triggerKey={}]", jobId, trigger.getKey());
        } catch (SchedulerException e) {
            LOGGER.error("Failed to schedule quartz job: " + jobId, e);
        }
    }

    /**
     * Schedule unidata job.
     *
     * @param jobId Unidata job id.
     * @param cronExpression Cron expression.
     */
    @Override
    public void schedule(final long jobId, final String cronExpression) {
        schedule(jobId, cronExpression, null);
    }

    /**
     * @param jobId
     */
    private void unschedule(long jobId) {
        try {
            TriggerKey key = createTriggerKey(jobId);


            if ((quartzSchedulerFactory.getObject() != null) && (quartzSchedulerFactory.getObject().checkExists(key))) {
                quartzSchedulerFactory.getObject().unscheduleJob(key);

                LOGGER.debug("Unschedule job in quartz [jobId={}, triggerKey={}]", jobId, key);
            }
        } catch (SchedulerException e) {
            LOGGER.error("Failed to unschedule job: " + jobId, e);
        }
    }

    private JobKey createJobKey(long jobId) {
        return JobKey.jobKey("job_" + jobId, QUARTZ_GROUP);
    }

    private TriggerKey createTriggerKey(long jobId) {
        return TriggerKey.triggerKey("trigger_" + jobId, QUARTZ_GROUP);
    }

    /**
     * @param jobId
     * @return
     */
    private Long findLastRunningJobExecutionId(final long jobId) {
        final Long jobInstanceId = jobDao.findLastBatchJobIds(Collections.singletonList(jobId)).get(jobId);

        if (jobInstanceId != null) {
            JobExecution jobExecution = getJobExplorer()
                    .getLastJobExecutions(Collections.singletonList(jobInstanceId), false)
                    .get(jobInstanceId);

            if (jobExecution != null &&
                    (jobExecution.getStatus() == BatchStatus.STARTED || jobExecution.getStatus() == BatchStatus.STARTING)) {
                return jobExecution.getId();
            }
        }

        return null;
    }

    private void addLastSuccessJobExecutionDate(final long jobId, final List<JobParameterDTO> jobParameters) {

        List<Long> jobInstanceIdIds = jobDao.findAllBatchJobIds(Collections.singletonList(jobId)).get(jobId);

        Date previousStartDate = null;
        if (CollectionUtils.isNotEmpty(jobInstanceIdIds)) {
            previousStartDate = getJobExplorer()
                    .getLastSuccessJobExecutionsDate(jobInstanceIdIds);
        }

        if (previousStartDate != null) {
            jobParameters.add(new JobParameterDTO(JobCommonParameters.PARAM_PREVIOUS_SUCCESS_START_DATE,
                    ZonedDateTime.ofInstant(previousStartDate.toInstant(), ZoneId.of("UTC"))));
        }
    }

    private JobTriggerDTO insertJobTrigger(final JobTriggerDTO jobTriggerDto) {
        JobValidator.validateJobTrigger(jobDao, jobTriggerDto);

        final JobTriggerPO jobPo = JobConverter.convertJobTriggerDtoToPo(jobTriggerDto);

        jobPo.setCreateDate(new Date());
        jobPo.setCreatedBy(SecurityUtils.getCurrentUserName());

        jobDao.insertJobTrigger(jobPo);

        return JobConverter.convertJobTriggerPoToDto(jobPo);
    }

    private JobTriggerDTO updateJobTrigger(final JobTriggerDTO jobTriggerDto) {
        JobValidator.validateJobTrigger(jobDao, jobTriggerDto);

        final JobTriggerPO jobTriggerPO = JobConverter.convertJobTriggerDtoToPo(jobTriggerDto);

        jobTriggerPO.setUpdateDate(new Date());
        jobTriggerPO.setUpdatedBy(SecurityUtils.getCurrentUserName());

        jobDao.updateJobTrigger(jobTriggerPO);

        return JobConverter.convertJobTriggerPoToDto(jobTriggerPO);
    }

    /**
     * Make casting to CustomJobExplorer because CustomJobExplorerFactoryBean can't return CustomJobExplorer to avoid
     * issues with Autowiring in another classes.
     *
     * @return
     */
    private CustomJobExplorer getJobExplorer() {
        return (CustomJobExplorer) jobExplorer;
    }

    /**
     * @param complexParameter - any object which you want to pass to job
     * @return key in storage which help find complex parameter in partitioner
     */
    @Override
    public String putComplexParameter(Object complexParameter) {
        String storageKey = IdUtils.v4String();
        complexJobParameterHolder.putComplexParameter(storageKey, complexParameter);
        return storageKey;
    }


    @Override
    @Transactional
    public Collection<JobDTO> findAll() {
        return jobDao.getJobsWithParameters().stream()
                .map(JobConverter::convertJobPoToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobDTO findJob(long jobId) {
        final JobPO jobWithParameters = jobDao.findJobWithParameters(jobId);
        return jobWithParameters != null ? JobConverter.convertJobPoToDto(jobWithParameters) : null;
    }

    @Override
    public Long stopJob(long jobId) {
        return stop(jobId);
    }

    @Override
    public void exportJobs(final String userLogin, final List<Long> jobsIds) {
        // TODO @Modules
//        asyncExecutor.async(() -> jobDao.findJobWithParameters(jobsIds))
//                .thenApply(JobConverter::convertJobsPoToDto)
//                .thenApply(JobConverter::convertJobsDtoToRo)
//                .thenApply(this::toJSON)
//                .whenComplete((json, ex) -> {
//                    final UpsertUserEventRequestContext.UpsertUserEventRequestContextBuilder jobExportUserEvent =
//                            new UpsertUserEventRequestContext.UpsertUserEventRequestContextBuilder()
//                                    .login(userLogin)
//                                    .type("JOB_EXPORT");
//                    if (ex != null) {
//                        handleExportImportError(
//                                ERROR_WHILE_EXPORTING_JOBS_LOG_MESSAGE,
//                                UserMessageConstants.JOBS_EXPORT_FAIL,
//                                ex,
//                                jobExportUserEvent
//                        );
//                        return;
//                    }
//                    sendExportDataToUser(json, jobExportUserEvent);
//                });
    }

    // TODO @Modules
//    private void sendExportDataToUser(String json, UpsertUserEventRequestContext.UpsertUserEventRequestContextBuilder jobExportUserEvent) {
//        try (final InputStream is = new ByteArrayInputStream(json.getBytes())) {
//            final UpsertUserEventRequestContext upsertUserEventRequestContext =
//                    jobExportUserEvent
//                            .content(MessageUtils.getMessage(UserMessageConstants.JOBS_EXPORT_SUCCESS))
//                            .build();
//            final UserEventDTO userEventDTO = userService.upsert(upsertUserEventRequestContext);
//            final UpsertLargeObjectContext saveLargeObjectRequestContext =
//                    new UpsertLargeObjectContext.UpsertLargeObjectContextBuilder()
//                            .eventKey(userEventDTO.getId())
//                            .mimeType("application/json")
//                            .binary(false)
//                            .inputStream(is)
//                            .filename(fileName())
//                            .build();
//            dataRecordsService.saveLargeObject(saveLargeObjectRequestContext);
//        } catch (IOException e) {
//            handleExportImportError(
//                    ERROR_WHILE_EXPORTING_JOBS_LOG_MESSAGE,
//                    UserMessageConstants.JOBS_EXPORT_FAIL,
//                    e,
//                    jobExportUserEvent
//            );
//        }
//    }
//
//    private void handleExportImportError(
//            final String logMessage,
//            final String userMessage,
//            final Throwable ex,
//            final UpsertUserEventRequestContext.UpsertUserEventRequestContextBuilder jobExportUserEvent
//    ) {
//        LOGGER.error(logMessage, ex);
//        final UpsertUserEventRequestContext upsertUserEventRequestContext =
//                jobExportUserEvent
//                        .content(MessageUtils.getMessage(userMessage))
//                        .build();
//        userService.upsert(upsertUserEventRequestContext);
//    }

    private String fileName() {
        try {
            return URLEncoder.encode(
                    "jobs_"
                            + DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd_HH-mm-ss")
                            + ".json",
                    StandardCharsets.UTF_8.name()
            );
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Error generating jobs file name", e);
        }
        return "jobs.json";
    }

    // TODO @Modules
//    private String toJSON(final Object object) {
//        return JsonUtils.write(object);
//    }
//
    @Override
    public void importJobs(final String userLogin, final Path path) {
        // TODO @Modules
//        asyncExecutor.async(() -> fromJSONToJobsList(path))
//                .thenApply(JobConverter::convertJobsRoToDto)
//                .whenComplete((jobs, ex) -> {
//                    final UpsertUserEventRequestContext.UpsertUserEventRequestContextBuilder jobImportUserEvent =
//                            new UpsertUserEventRequestContext.UpsertUserEventRequestContextBuilder()
//                                    .login(userLogin)
//                                    .type("JOB_IMPORT");
//
//                    StringBuilder details = new StringBuilder();
//
//                    if (ex != null) {
//                        String message = MessageUtils.getMessage(UserMessageConstants.JOBS_IMPORT,
//                                "0", jobs.size(), System.lineSeparator());
//                        LOGGER.error(ERROR_WHILE_IMPORTING_JOBS_LOG_MESSAGE, ex);
//
//                        final UpsertUserEventRequestContext upsertUserEventRequestContext =
//                                jobImportUserEvent
//                                        .content(message)
//                                        .details(ex.getMessage())
//                                        .build();
//                        userService.upsert(upsertUserEventRequestContext);
//                        return;
//                    }
//
//                    if (CollectionUtils.isEmpty(jobs)) {
//                        final UpsertUserEventRequestContext upsertUserEventRequestContext =
//                                jobImportUserEvent
//                                        .content(MessageUtils.getMessage(UserMessageConstants.JOBS_IMPORT,
//                                                "0", jobs.size(), System.lineSeparator()))
//                                        .build();
//                        userService.upsert(upsertUserEventRequestContext);
//                        return;
//                    }
//
//                    TransactionTemplate txTemplate = new TransactionTemplate(txManager);
//                    txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//
//                    int failedJobs = 0;
//                    int savedJobs = 0;
//
//                    for (JobDTO jobDto : jobs) {
//                        // Run every imported job in separate transaction.
//                        Boolean imported = txTemplate.execute(status -> {
//                            boolean result = true;
//                            try {
//                                importJob(jobDto);
//                                details.append(MessageUtils.getMessage(UserMessageConstants.JOB_IMPORT_SUCCESS,
//                                                jobDto.getName()))
//                                        .append(System.lineSeparator());
//                            } catch (Exception e) {
//                                LOGGER.error("Failed to import job [jobReferenceName=" + jobDto.getJobNameReference() +
//                                                ", jobName=" + jobDto.getName() + "]", e);
//                                result = false;
//
//                                details.append(MessageUtils.getMessage(
//                                        UserMessageConstants.JOB_IMPORT_FAIL, jobDto.getName()));
//
//                                if (e instanceof JobException) {
//                                    details.append(ReportUtil.SPACE)
//                                            .append(MessageUtils.getMessage(((JobException)e).getId().getCode(),
//                                                    ((JobException)e).getArgs()));
//                                }
//
//                                details.append(System.lineSeparator());
//                            }
//
//                            return result;
//                        });
//
//                        if (imported != null && imported) {
//                            savedJobs++;
//                        } else {
//                            failedJobs++;
//                        }
//                    }
//
//                    final UpsertUserEventRequestContext upsertUserEventRequestContext = jobImportUserEvent
//                            .content(MessageUtils.getMessage(UserMessageConstants.JOBS_IMPORT, savedJobs, failedJobs,
//                                    System.lineSeparator()))
//                            .details(details.toString())
//                            .build();
//                    userService.upsert(upsertUserEventRequestContext);
//                });
    }


    /**
     * Import job from JSON file.
     *
     * @param jobDto
     */
    private void importJob(JobDTO jobDto) {
        // 1. Find existing job by Name.
        JobPO existingJobPO = jobDao.findJob(jobDto.getName());

        // Imported job will update existing job otherwise it new job will be added.
        jobDto.setId(existingJobPO == null ? null : existingJobPO.getId());

        // 2. Validate job and job parameters.
        validateAndResolveImportedJobParameters(jobDto);

        // 3. Check if job exists with the same parameter values and remove it.
        final JobPO jobPo = JobConverter.convertJobDtoToPo(jobDto);
        JobPO sameJobByParams = jobDao.checkJobByParams(jobPo);

        if (sameJobByParams != null) {
            removeJob(sameJobByParams.getId());
        }

        // 4. Insert or update job.
        final JobDTO saved = jobDto.getId() == null?  insertJob(jobDto) : updateJob(jobDto);

        LOGGER.info("Saved job [id={}, jobReferenceName={}, name={}]",
                saved.getId(), saved.getJobNameReference(), jobDto.getName());
    }

    @Override
    public List<String> getAllTags() {
        return jobDao.getAllTags();
    }

    // TODO @Modules
//    private List<JobRO> fromJSONToJobsList(final Path path) {
//        return JsonUtils.read(path.toFile(), JOB_RO_LIST_TYPE_REFERENCE);
//    }
}
