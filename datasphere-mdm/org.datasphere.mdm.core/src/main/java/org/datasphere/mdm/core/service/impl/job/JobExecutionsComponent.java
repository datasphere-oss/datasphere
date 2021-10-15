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
package org.datasphere.mdm.core.service.impl.job;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.context.JobExecutionCleanupContext;
import org.datasphere.mdm.core.context.JobExecutionQueryContext;
import org.datasphere.mdm.core.context.JobExecutionStartContext;
import org.datasphere.mdm.core.context.JobExecutionStatusContext;
import org.datasphere.mdm.core.context.JobExecutionStopContext;
import org.datasphere.mdm.core.context.StepExecutionQueryContext;
import org.datasphere.mdm.core.convert.job.JobDefinitionConverter;
import org.datasphere.mdm.core.convert.job.JobExecutionConverter;
import org.datasphere.mdm.core.convert.job.StepExecutionConverter;
import org.datasphere.mdm.core.dao.JobDefinitionsDAO;
import org.datasphere.mdm.core.dao.JobExecutionsDAO;
import org.datasphere.mdm.core.dto.job.JobExecutionStartResult;
import org.datasphere.mdm.core.dto.job.JobExecutionStatusResult;
import org.datasphere.mdm.core.dto.job.JobExecutionsQueryResult;
import org.datasphere.mdm.core.dto.job.JobPaginatedResult;
import org.datasphere.mdm.core.dto.job.StepExecutionQueryResult;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.exception.JobException;
import org.datasphere.mdm.core.exception.JobValidationException;
import org.datasphere.mdm.core.po.job.JobBatchJobInstancePO;
import org.datasphere.mdm.core.po.job.JobDefinitionPO;
import org.datasphere.mdm.core.po.job.JobSysJobInstancePO;
import org.datasphere.mdm.core.service.job.CustomJobExplorer;
import org.datasphere.mdm.core.service.job.CustomJobRegistry;
import org.datasphere.mdm.core.service.job.JobCommonParameters;
import org.datasphere.mdm.core.service.job.JobExecutionFilter;
import org.datasphere.mdm.core.service.job.StepExecutionFilter;
import org.datasphere.mdm.core.type.job.JobDefinition;
import org.datasphere.mdm.core.type.job.JobDescriptor;
import org.datasphere.mdm.core.type.job.JobExecution;
import org.datasphere.mdm.core.type.job.JobParameterDefinition;
import org.datasphere.mdm.core.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.datasphere.mdm.system.exception.ValidationResult;
import org.datasphere.mdm.system.util.IdUtils;

/**
 * @author Mikhail Mikhailov on Jul 4, 2021
 */
@Component
public class JobExecutionsComponent {
    /**
     * This logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutionsComponent.class);
    /**
     * The job operator.
     */
    @Autowired
    private JobOperator jobOperator;
    /**
     * The job explorer.
     */
    @Autowired
    private CustomJobExplorer jobExplorer;
    /**
     * The JL.
     */
    @Autowired
    private JobLauncher jobLauncher;
    /**
     * The JR.
     */
    @Autowired
    private CustomJobRegistry jobRegistry;
    /**
     * Our DAO.
     */
    @Autowired
    private JobExecutionsDAO jobExecutionsDAO;
    /**
     * Not our DAO.
     */
    @Autowired
    private JobDefinitionsDAO jobDefinitionsDAO;
    /**
     * Converter.
     */
    @Autowired
    private JobDefinitionConverter jobDefinitionConverter;
    /**
     * JE converter.
     */
    @Autowired
    private JobExecutionConverter jobExecutionConverter;
    /**
     * SE converter.
     */
    @Autowired
    private StepExecutionConverter stepExecutionConverter;
    /**
     * For parameters validation.
     */
    @Autowired
    private JobValidationComponent jobValidationComponent;
    /**
     * Parameter factory.
     */
    @Autowired
    private JobParameterFactory jobParameterFactory;
    /**
     * Constructor.
     */
    public JobExecutionsComponent() {
        super();
    }
    /**
     * Stops a job, currently being executed.
     * @param ctx the context
     */
    public void stop(JobExecutionStopContext ctx) {

        try {

            // 1. Select instance id
            Long jobInstanceId = null;
            if (Objects.nonNull(ctx.getJobDefinitionId()) && ctx.getJobDefinitionId() > 0) {
                jobInstanceId = jobExecutionsDAO
                        .findLastInstanceIds(Collections.singletonList(ctx.getJobDefinitionId()))
                        .get(ctx.getJobDefinitionId());
            } else if (StringUtils.isNotBlank(ctx.getJobName())) {
                jobInstanceId = jobExecutionsDAO.findLastInstanceId(ctx.getJobName());
            }

            // 2. Select execution id
            Long jobExecutionId = null;
            if (jobInstanceId == null) {
                jobExecutionId = ctx.getJobExecutionId();
            } else {

                org.springframework.batch.core.JobExecution je = jobExplorer
                        .getLastJobExecutions(Collections.singletonList(jobInstanceId), false)
                        .get(jobInstanceId);

                if (je != null && (je.getStatus() == BatchStatus.STARTED || je.getStatus() == BatchStatus.STARTING)) {
                    jobExecutionId = je.getId();
                }
            }

            // 3. Nothing to stop. Return.
            if (jobExecutionId == null) {
                return;
            }

            jobOperator.stop(jobExecutionId);

        } catch (final JobExecutionException e) {
            throw new JobException("Failed to stop job definition.", e, CoreExceptionIds.EX_JOB_BATCH_STOP_FAILED);
        }
    }
    /**
     * Does cleanup in runtime tables for either a single execution id
     * or for the whole job (either name or definition id).
     * @param ctx the context
     */
    public void cleanup(JobExecutionCleanupContext ctx) {

        List<Long> instanceIds = null;
        if (Objects.nonNull(ctx.getJobDefinitionId()) && ctx.getJobDefinitionId() > 0) {
            instanceIds = jobExecutionsDAO
                    .findAllInstanceIds(Collections.singletonList(ctx.getJobDefinitionId()))
                    .get(ctx.getJobDefinitionId());
        } else if (StringUtils.isNotBlank(ctx.getJobName())) {
            instanceIds = jobExecutionsDAO
                    .findAllInstanceIds(ctx.getJobName());
        } else if (Objects.nonNull(ctx.getJobInstanceId())) {
            instanceIds = Collections.singletonList(ctx.getJobInstanceId());
        }

        if (CollectionUtils.isEmpty(instanceIds)) {
            return;
        }

        jobExplorer.cleanupJobExecutions(instanceIds);
    }
    /**
     * Does start a job.
     * @param ctx the context
     * @return result
     */
    public JobExecutionStartResult start(JobExecutionStartContext ctx) {

        // 1. Check for restart
        if (ctx.isRestart()) {

            // 1.1. Check fields
            ensureExecutionId(ctx);

            try {
                Long newJobExecutionId = jobOperator.restart(ctx.getJobExecutionId());
                return new JobExecutionStartResult(jobExecutionConverter.to(jobExplorer.getJobExecution(newJobExecutionId)));
            } catch (UnexpectedJobExecutionException | JobExecutionException e) {

                if (e.getCause() instanceof JobExecutionAlreadyRunningException) {
                    throw new JobException("Failed to restart job with jobExecutionId: [{}].", e,
                            CoreExceptionIds.EX_JOB_BATCH_RESTART_FAILED_ALREADY_RUNNING,
                            ctx.getJobExecutionId());
                }

                failRestart(ctx.getJobExecutionId(), e);
            }
        }

        // 2. Check criteria
        ensureSelectionCrieria(ctx);

        // 3. Check for job definition ID being supplied
        String jobName = null;
        Map<String, JobParameterDefinition<?>> parameters = new HashMap<>();
        if (Objects.nonNull(ctx.getJobDefinitionId()) && ctx.getJobDefinitionId() > 0) {

            List<JobDefinitionPO> pos = jobDefinitionsDAO.load(Collections.singletonList(ctx.getJobDefinitionId()));
            if (CollectionUtils.isEmpty(pos) || pos.size() != 1) {
                failDefinition(ctx.getJobDefinitionId());
            }

            JobDefinition jd = jobDefinitionConverter.from(pos.get(0));

            // 3.1. Don't run disabled jobs or job in error state.
            // Also disable instances, if one is already running.
            ensureJobStartable(jd);

            jobName = jd.getJobName();
            parameters.putAll(jd.getParametersMap());
        }

        // 4. Merge parameters
        if (MapUtils.isNotEmpty(ctx.getParameters())) {
            parameters.putAll(ctx.getParameters());
        }

        // 5. Check for plain job name
        if (Objects.isNull(jobName)) {
            jobName = ctx.getJobName();
        }

        // 6. Check general prerequisites (name etc.)
        ensureJobName(jobName);

        // 7. Check parameters
        ensureParameters(jobName, parameters);

        // 8. Convert parameters, run and return
        return new JobExecutionStartResult(execute(ctx.getJobDefinitionId(), jobName, convert(jobName, ctx, parameters)));
    }

    public JobExecutionsQueryResult executions(JobExecutionQueryContext ctx) {

        List<Long> instanceIds = null;
        if (Objects.nonNull(ctx.getJobDefinitionId()) && ctx.getJobDefinitionId() > 0) {
            instanceIds = jobExecutionsDAO
                    .findAllInstanceIds(Collections.singletonList(ctx.getJobDefinitionId()))
                    .get(ctx.getJobDefinitionId());
        } else if (StringUtils.isNotBlank(ctx.getJobName())) {
            instanceIds = jobExecutionsDAO
                    .findAllInstanceIds(ctx.getJobName());
        }

        int totalCount = 0;
        Collection<org.springframework.batch.core.JobExecution> executions = Collections.emptyList();

        if (CollectionUtils.isNotEmpty(instanceIds)) {

            JobExecutionFilter filter = new JobExecutionFilter();
            filter.setJobInstanceIds(instanceIds);
            filter.setFromIndex(ctx.getFrom());
            filter.setItemsCount(ctx.getCount());

            final JobPaginatedResult<org.springframework.batch.core.JobExecution> r = jobExplorer.searchJobExecutions(filter);
            executions = r.getPage();
            totalCount = r.getTotalCount();
        }

        if (Objects.nonNull(ctx.getJobExecutionId()) && ctx.getJobExecutionId() > 0) {
            org.springframework.batch.core.JobExecution je = jobExplorer.getJobExecution(ctx.getJobExecutionId());
            if (Objects.nonNull(je)) {
                executions = Collections.singleton(je);
                totalCount = 1;
            }
        }

        return new JobExecutionsQueryResult(executions.stream()
                .map(je -> {

                    JobExecution execution = jobExecutionConverter.to(je);
                    if (ctx.isRestartable() && (je.getStatus() == BatchStatus.STOPPED || je.getStatus() == BatchStatus.FAILED)) {
                        execution.setRestartable(isRestartable(je.getJobInstance().getJobName()));
                    }

                    return execution;
                })
                .collect(Collectors.toList()), totalCount);
    }

    public StepExecutionQueryResult steps(StepExecutionQueryContext ctx) {

        int totalCount = 0;
        Collection<StepExecution> executions = Collections.emptyList();

        if (Objects.nonNull(ctx.getJobExecutionId()) && ctx.getJobExecutionId() > 0) {

            StepExecutionFilter filter = new StepExecutionFilter();
            filter.setJobExecutionId(ctx.getJobExecutionId());
            filter.setFromIndex(ctx.getFrom());
            filter.setItemsCount(ctx.getCount());

            JobPaginatedResult<StepExecution> page = jobExplorer.searchStepExecutions(filter);

            executions = page.getPage();
            totalCount = page.getTotalCount();

        }

        if (Objects.nonNull(ctx.getJobInstanceId()) && ctx.getJobInstanceId() > 0) {

            org.springframework.batch.core.JobExecution je = jobExplorer
                .getLastJobExecutions(Collections.singleton(ctx.getJobInstanceId()), true)
                .get(ctx.getJobInstanceId());

            executions = je.getStepExecutions();
            totalCount = executions.size();
        }

        return new StepExecutionQueryResult(stepExecutionConverter.to(executions), totalCount);
    }

    public JobExecutionStatusResult status(JobExecutionStatusContext ctx) {

        Long instanceId = null;
        if (Objects.nonNull(ctx.getJobDefinitionId()) && ctx.getJobDefinitionId() > 0) {
            instanceId = jobExecutionsDAO
                    .findLastInstanceIds(Collections.singletonList(ctx.getJobDefinitionId()))
                    .get(ctx.getJobDefinitionId());
        } else if (StringUtils.isNotBlank(ctx.getJobName())) {
            instanceId = jobExecutionsDAO
                    .findLastInstanceId(ctx.getJobName());
        }

        double progress = -1d;
        org.springframework.batch.core.JobExecution je = null;
        if (Objects.nonNull(instanceId)) {
            je = jobExplorer
                    .getLastJobExecutions(Collections.singletonList(instanceId), true)
                    .get(instanceId);
        } else if (Objects.nonNull(ctx.getJobExecutionId()) && ctx.getJobExecutionId() > 0) {
            je = jobExplorer.getJobExecution(ctx.getJobExecutionId());
        }

        if (je != null && ctx.isProgress()) {

            final Collection<StepExecution> steps = je.getStepExecutions();

            long amount = steps.size();
            long complete = steps.stream()
                    .filter(step -> step.getStatus() == BatchStatus.COMPLETED)
                    .count();

            progress = 1.0 * complete / amount;
        }

        return new JobExecutionStatusResult(jobExecutionConverter.to(je), progress);
    }

    private JobExecution execute(Long definitionId, String jobName, JobParameters parameters) {

        try {

            // 1. Find job
            Job batchJob = jobRegistry.getJob(jobName);

            // 2. Run
            org.springframework.batch.core.JobExecution jobExecution = jobLauncher.run(batchJob, parameters);

            // 3. Save (associate) excutionId with either job name or definition in a cross tables.
            if (Objects.nonNull(definitionId) && definitionId > 0) {

                JobBatchJobInstancePO po = new JobBatchJobInstancePO();

                po.setJobDefinitionId(definitionId);
                po.setJobInstanceId(jobExecution.getJobId());
                po.setCreatedBy(SecurityUtils.getCurrentUserName());

                jobExecutionsDAO.save(po);
            } else {

                JobSysJobInstancePO po = new JobSysJobInstancePO();

                po.setJobName(jobName);
                po.setJobInstanceId(jobExecution.getJobId());
                po.setCreatedBy(SecurityUtils.getCurrentUserName());

                jobExecutionsDAO.save(po);
            }

            return jobExecutionConverter.to(jobExecution);

        } catch (final JobExecutionException e) {
            throw new JobException("Failed to execute job [{}].", e, CoreExceptionIds.EX_JOB_BATCH_EXECUTION_FAILED, jobName);
        }
    }

    private boolean isRestartable(String jobName) {
        try {
            Job batchJob = jobRegistry.getJob(jobName);
            return batchJob.isRestartable();
        } catch (NoSuchJobException e) {
            LOGGER.warn("Failed to get batch job by jobName [{}]", jobName, e);
        }

        return false;
    }
    private void ensureJobStartable(JobDefinition jd)  {

        if (!jd.isEnabled() || jd.isError()) {
            final String message = "Job [{}] with id [{}] not enabled or is in error state";
            LOGGER.warn(message, jd.getJobName(), jd.getId());
            throw new JobException(message, CoreExceptionIds.EX_JOB_DISABLED, jd.getJobName(), jd.getId());
        }

        final Long jobInstanceId = jobExecutionsDAO
                .findLastInstanceIds(Collections.singletonList(jd.getId()))
                .get(jd.getId());

        if (jobInstanceId != null) {

            org.springframework.batch.core.JobExecution sbje = jobExplorer
                    .getLastJobExecutions(Collections.singletonList(jobInstanceId), false)
                    .get(jobInstanceId);

            if (sbje != null && (sbje.getStatus() == BatchStatus.STARTED || sbje.getStatus() == BatchStatus.STARTING)) {
                final String message = "Job with name [{}] is already running";
                LOGGER.warn(message, jd.getDisplayName());
                throw new JobException(message, CoreExceptionIds.EX_JOB_ALREADY_RUNNING, jd.getDisplayName());
            }
        }
    }

    private void ensureJobName(String jobName) {
        if (StringUtils.isBlank(jobName)) {
            final String message = "Cannot start job. Job name missing.";
            LOGGER.warn(message);
            throw new JobException(message, CoreExceptionIds.EX_JOB_RUN_JOB_NAME_MISSING);
        }
    }

    private void ensureParameters(String jobName, Map<String, JobParameterDefinition<?>> parameters) {

        // 1. Add read only/required
        JobDescriptor descriptor = jobRegistry.getDescriptor(jobName);
        descriptor.getAllParameters().values().forEach(d -> {

            // 1.1. Force read-only condition. Let fail in validation, if default was not supplied.
            if (d.isReadOnly()) {
                parameters.remove(d.getName());
            }

            if (d.hasDefaultValue()) {

                // 1.2. Force configured value for RO parameters
                if (d.isReadOnly()) {
                    parameters.put(d.getName(), jobParameterFactory.fromValue(d, d.getDefaultValue()));
                    return;
                }

                // 1.3. Supply defaults for otherwise missing parameters
                JobParameterDefinition<?> p = parameters.get(d.getName());
                if ((Objects.isNull(p) || p.isEmpty()) && d.hasDefaultValue()) {
                    parameters.put(d.getName(), jobParameterFactory.fromValue(d, d.getDefaultValue()));
                }
            }
        });

        // 2. Validate
        List<ValidationResult> validations = jobValidationComponent.validate(jobName, parameters);
        if (CollectionUtils.isNotEmpty(validations)) {
            throw new JobValidationException(
                    "Invalid parameters for job [{}] encounted.",
                    CoreExceptionIds.EX_JOB_PARAMETERS_VALIDATION_ERRORS,
                    validations, jobName);
        }
    }

    private void ensureExecutionId(JobExecutionStartContext ctx) {
        if (ctx.isRestart() && Objects.isNull(ctx.getJobExecutionId())) {
            failRestart(null, null);
        }
    }

    private void ensureSelectionCrieria(JobExecutionStartContext ctx) {
        if ((Objects.isNull(ctx.getJobDefinitionId()) && ctx.getJobDefinitionId() > 0) && Objects.nonNull(ctx.getJobName())) {
            final String message = "Cannot start job - ambiguous start criteria. Both jobName [{}] and jobDefinitionId [{}] specified.";
            LOGGER.warn(message, ctx.getJobName(), ctx.getJobDefinitionId());
            throw new JobException(message, CoreExceptionIds.EX_JOB_RUN_JOB_NAME_AND_DEFINITION_ID_SPECIFIED,
                    ctx.getJobName(), ctx.getJobDefinitionId());
        }
    }

    private JobParameters convert(String jobName, JobExecutionStartContext ctx, Map<String, JobParameterDefinition<?>> params) {

        // 1. Explicit / user parameters
        JobParametersBuilder builder = new JobParametersBuilder();
        for (JobParameterDefinition<?> jpd : params.values()) {
            builder.addString(jpd.getName(), jobParameterFactory.toString(jobName, jpd), false);
        }

        // 2. Add timestamp in job params to avoid issue with completed job.
        builder.addString(JobCommonParameters.PARAM_START_TIMESTAMP, String.valueOf(System.currentTimeMillis()));

        // 3. Add internal id - runId unconditionally, since operationId may be supplied from UI
        builder.addString(JobCommonParameters.PARAM_RUN_ID, IdUtils.v4String());

        // 4. Add security info
        JobParameterDefinition<?> user = params.get(JobCommonParameters.PARAM_USER_NAME);
        if (Objects.isNull(user) || user.isEmpty()) {
            builder.addString(JobCommonParameters.PARAM_USER_NAME, SecurityUtils.getCurrentUserName());
            builder.addString(JobCommonParameters.PARAM_USER_TOKEN, SecurityUtils.getCurrentUserToken());
        }

        // 5. Add automatic operation id
        JobParameterDefinition<?> opid = params.get(JobCommonParameters.PARAM_OPERATION_ID);
        if (Objects.isNull(opid) || opid.isEmpty()) {
            builder.addString(JobCommonParameters.PARAM_OPERATION_ID, IdUtils.v4String());
        }

        // 6. Last startup TS
        List<Long> ids = null;
        if (Objects.nonNull(ctx.getJobDefinitionId()) && ctx.getJobDefinitionId() > 0) {
            ids = jobExecutionsDAO.findAllInstanceIds(Collections.singletonList(ctx.getJobDefinitionId()))
                    .get(ctx.getJobDefinitionId());
        } else {
            ids = jobExecutionsDAO.findAllInstanceIds(jobName);
        }

        // 7. The most important parameter - the job name.
        builder.addString(JobCommonParameters.PARAM_JOB_NAME, jobName);

        if (CollectionUtils.isNotEmpty(ids)) {

            Date previousStartDate = jobExplorer.getLastSuccessJobExecutionsDate(ids);
            if (previousStartDate != null) {
                builder.addDate(JobCommonParameters.PARAM_PREVIOUS_SUCCESS_START_DATE, previousStartDate);
            }
        }

        return builder.toJobParameters();
    }

    private void failDefinition(Long jobDefinitionId) {
        final String message = "Job with id [{}] not found";
        LOGGER.warn(message, jobDefinitionId);
        throw new JobException(message, CoreExceptionIds.EX_JOB_NOT_FOUND, jobDefinitionId);
    }

    private void failRestart(Long jobExecutionId, Exception e) {
        final String message = "Failed to restart job with jobExecutionId: [{}].";
        LOGGER.warn(message, jobExecutionId);
        throw new JobException(message, e, CoreExceptionIds.EX_JOB_BATCH_RESTART_FAILED, jobExecutionId);
    }
}
