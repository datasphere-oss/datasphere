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
package com.huahui.datasphere.mdm.rest.core.service;

import static com.huahui.datasphere.mdm.core.util.SecurityUtils.ADMIN_SYSTEM_MANAGEMENT;
import static com.huahui.datasphere.mdm.core.util.SecurityUtils.DATA_OPERATIONS_MANAGEMENT;
import static com.huahui.datasphere.mdm.core.util.SecurityUtils.EXECUTE_DATA_OPERATIONS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.huahui.datasphere.mdm.core.dto.PaginatedResultDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobExecutionDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobExecutionStepDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobTriggerDTO;
import com.huahui.datasphere.mdm.core.dto.job.StepExecutionPaginatedResultDTO;
import com.huahui.datasphere.mdm.core.service.JobService;
import com.huahui.datasphere.mdm.core.service.job.JobEnumParamExtractor;
import com.huahui.datasphere.mdm.core.service.job.JobTemplateParameters;
import com.huahui.datasphere.mdm.core.type.job.JobEnumType;
import com.huahui.datasphere.mdm.core.type.job.JobExecutionBatchStatus;
import com.huahui.datasphere.mdm.core.type.job.JobFilter;
import com.huahui.datasphere.mdm.core.util.FileUtils;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;
import com.huahui.datasphere.mdm.rest.system.ro.ErrorResponse;
import com.huahui.datasphere.mdm.rest.system.ro.RestResponse;
import com.huahui.datasphere.mdm.rest.system.service.AbstractRestService;

import com.huahui.datasphere.mdm.rest.core.converter.JobConverter;
import com.huahui.datasphere.mdm.rest.core.ro.job.JobExecutionRO;
import com.huahui.datasphere.mdm.rest.core.ro.job.JobMetaInfoRO;
import com.huahui.datasphere.mdm.rest.core.ro.job.JobRO;
import com.huahui.datasphere.mdm.rest.core.ro.job.JobSearchRO;
import com.huahui.datasphere.mdm.rest.core.ro.job.JobStepExecutionRO;
import com.huahui.datasphere.mdm.rest.core.ro.job.JobTriggerRO;
import com.huahui.datasphere.mdm.rest.core.ro.job.PaginatedJobExecutionsResultRO;
import com.huahui.datasphere.mdm.rest.core.ro.job.PaginatedJobStepExecutionsResultRO;
import com.huahui.datasphere.mdm.rest.core.ro.job.PaginatedJobsResultRO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Rest service for all job related activities.
 */
@Path("/jobs")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class JobRestService extends AbstractRestService {

    /** The Constant DATA_PARAM_FILE. */
    private static final String DATA_PARAM_FILE = "file";

    /** The job service. */
    @Autowired
    private JobService jobService;

    /**
     * Find all job meta names.
     *
     * @return the response
     * @throws Exception the exception
     */
    @GET
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/jobmetanames")
    @Operation(
        description = "Получить список внешних идентификаторов задач.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = List.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response findAllJobMetaNames() {
        List<JobMetaInfoRO> result = new ArrayList<>();

        Collection<String> jobNames = jobService.findAllUIJobReferences();

        if (!CollectionUtils.isEmpty(jobNames)) {
            for (String jobName : jobNames) {
                JobMetaInfoRO jobMeta = new JobMetaInfoRO();
                jobMeta.setJobNameReference(jobName);

                JobTemplateParameters parameters = jobService.findJobTemplateParameters(jobName);

                if (parameters != null) {
                    jobMeta.setParameters(JobConverter.convertJobTemplateParametersToRo(parameters));
                }

                result.add(jobMeta);
            }
        }

        return ok(result);
    }

    /**
     * Find all.
     *
     * @return the response
     * @throws Exception the exception
     */
    @GET
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Operation(
        description = "Получить список задач.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = PaginatedJobsResultRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response findAll() {

        List<JobDTO> allJobs = jobService.findAllJobsWithParams();

        final Map<Long, JobExecutionDTO> jobExecutionMap = jobService
                .findLastJobExecutions(allJobs.stream().map(JobDTO::getId).collect(Collectors.toList()));

        final List<JobRO> jobs = JobConverter.convertJobsDtoToRo(allJobs);

        jobs.stream().forEach(job -> {
            JobExecutionDTO jobExecution = jobExecutionMap.get(job.getId());

            if (jobExecution != null) {
                job.setLastExecution(JobConverter.convertJobExecutionDTOToRO(jobExecution, job.getId()));
            }
        });

        fillJobParametersMeta(jobs);

        PaginatedJobsResultRO resultRO = new PaginatedJobsResultRO();

        resultRO.setContent(jobs);
        resultRO.setTotalCount(jobs.size());

        return ok(resultRO);
    }
    /**
     * Get all job tags.
     *
     * @return the response
     * @throws Exception the exception
     */
    @GET
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/tags")
    @Operation(
        description = "Получить список тэгов.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = List.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response getAllTags() {
        List<String> tags = jobService.getAllTags();
        return ok(tags);
    }
    /**
     * Find jobs page.
     *
     * @param fromInd the from ind
     * @param itemCount the item count
     * @param status the status
     * @return the response
     * @throws Exception the exception
     */
    @POST
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/search")
    @Operation(
        description = "Получить ограниченный список задач.",
        method = "POST",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = PaginatedJobsResultRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response findJobsPage(@Parameter(description = "The request") JobSearchRO request) {
        Boolean enabled = null;
        if(StringUtils.equalsIgnoreCase("active", request.getEnabledStatus())) {
                enabled = true;
        }else if(StringUtils.equalsIgnoreCase("inactive", request.getEnabledStatus())) {
        	enabled = false;
        }

        JobFilter filter = new JobFilter();
		filter.setEnabled(enabled);
		filter.setFromInd(request.getStart());
		filter.setItemCount(request.getCount());
		filter.setName(request.getText());
		filter.setTags(request.getTags());
		if (request.getSortFields() != null && request.getSortFields().isEmpty()) {
			filter.setSortBy(request.getSortFields() == null ? null : request.getSortFields().get(0).getField());
			filter.setOrder(request.getSortFields() == null ? null : request.getSortFields().get(0).getOrder());
		}
		PaginatedResultDTO<JobDTO> paginatedResult = jobService.searchJobs(filter);

        PaginatedJobsResultRO resultRO = new PaginatedJobsResultRO();
        resultRO.setTotalCount(paginatedResult.getTotalCount());

        if (!CollectionUtils.isEmpty(paginatedResult.getPage())) {
            List<Long> jobIds = paginatedResult.getPage().stream().map(JobDTO::getId).collect(Collectors.toList());

            final Map<Long, JobExecutionDTO> jobExecutionMap = jobService.findLastJobExecutions(jobIds);

            final List<JobRO> jobs = JobConverter.convertJobsDtoToRo(paginatedResult.getPage());

            jobs.stream().forEach(job -> {
                JobExecutionDTO jobExecution = jobExecutionMap.get(job.getId());

                if (jobExecution != null) {
                    job.setLastExecution(JobConverter.convertJobExecutionDTOToRO(jobExecution, job.getId()));
                }
            });

            fillJobParametersMeta(jobs);

            resultRO.setContent(jobs);
        }

        return ok(resultRO);
    }



    /**
     * Creates the job.
     *
     * @param job the job
     * @return the response
     * @throws Exception the exception
     */
    @PUT
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isCreateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Operation(
        description = "Сохранить задачу.",
        method = "PUT",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = JobRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response createJob(@Parameter(description = "The job description.") JobRO job) {
        final JobRO resultJob = JobConverter.convertJobDtoToRo(jobService.saveJob(JobConverter.convertJobRoToDto(job)));

        fillJobParametersMeta(Collections.singletonList(resultJob));

        return ok(resultJob);
    }

    /**
     * Save job.
     *
     * @param jobId the job id
     * @param job the job
     * @return the response
     * @throws Exception the exception
     */
    @PUT
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isUpdateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/{jobId}")
    @Operation(
        description = "Редактировать задачу.",
        method = "PUT",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = JobRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response saveJob(
            @Parameter(description = "The job id") @PathParam("jobId") final Long jobId,
            @Parameter(description = "The job definition") JobRO job) {

        Objects.requireNonNull(jobId, "jobId must not be null");

        if (job.getId() != null) {
            Assert.isTrue(jobId.equals(job.getId()), "jobId must be equal to inner object's ID");
        } else {
            job.setId(jobId);
        }

        final JobRO newJob = JobConverter.convertJobDtoToRo(jobService.saveJob(JobConverter.convertJobRoToDto(job)));

        fillJobParametersMeta(Collections.singletonList(newJob));

        return ok(newJob);
    }

    /**
     * Find job executions.
     *
     * @param jobId the job id
     * @return the response
     * @throws Exception the exception
     */
    @GET
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/executions/{jobId}")
    @Operation(
        deprecated = true,
        description = "Получить список запусков задачи.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = List.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    @Deprecated
    public Response findJobExecutions(@Parameter(description = "Job id") @PathParam("jobId")
    final Long jobId) {

        List<JobExecutionDTO> executions = jobService.findAllJobExecutions(jobId);
        List<JobExecutionRO> executionsRO = JobConverter.convertJobExecutionsDTOToRO(executions, jobId);

        // Set restartable only if last execution not COMPLETED if it allowed in
        // spring batch for restarting.
        if (!CollectionUtils.isEmpty(executions)) {
            // Set not restartable for all elements in collection.
            executionsRO.forEach(e -> e.setRestartable(false));

            JobExecutionDTO jobExecution = executions.get(0);
            if (jobExecution.getJobExecutionExitStatus() != null
            && (jobExecution.getJobExecutionBatchStatus() == JobExecutionBatchStatus.STOPPED
            || jobExecution.getJobExecutionBatchStatus() == JobExecutionBatchStatus.FAILED)) {

                boolean restartable = jobService.isJobRestartable(jobId);
                executionsRO.get(0).setRestartable(restartable);
            }
        }

        return ok(executionsRO);
    }

    /**
     * Find job executions page.
     *
     * @param jobId the job id
     * @param fromInd the from ind
     * @param itemCount the item count
     * @return the response
     * @throws Exception the exception
     */
    @GET
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/executions/{jobId}/{fromInd}/{itemCount}")
    @Operation(
        description = "Получить ограниченный список запусков задачи.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = PaginatedJobExecutionsResultRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response findJobExecutionsPage(
            @Parameter(description = "The job id") @PathParam("jobId") final Long jobId,
            @Parameter(description = "From index") @PathParam("fromInd") final long fromInd,
            @Parameter(description = "Count") @PathParam("itemCount") final int itemCount) {

        PaginatedResultDTO<JobExecutionDTO> paginatedResult = jobService.findJobExecutions(jobId, fromInd, itemCount);
        PaginatedJobExecutionsResultRO resultRO = new PaginatedJobExecutionsResultRO();
        resultRO.setTotalCount(paginatedResult.getTotalCount());

        List<JobExecutionRO> executionsRO = JobConverter.convertJobExecutionsDTOToRO(paginatedResult.getPage(), jobId);
        resultRO.setContent(executionsRO);

        // Set restartable only if last execution not COMPLETED if it allowed in
        // spring batch for restarting.
        if (!CollectionUtils.isEmpty(paginatedResult.getPage())) {
            // Set not restartable for all elements in collection.
            executionsRO.stream().forEach(e -> e.setRestartable(false));

            Long lastJobExecutionId = jobService.findLastJobExecutionIds(Collections.singletonList(jobId)).get(jobId);

            // Set restartable flag only for last execution if allowed by
            // conditions.
            if (lastJobExecutionId != null) {
                paginatedResult.getPage().stream().filter(je -> lastJobExecutionId.equals(je.getId())).findFirst()
                        .ifPresent(jobExecution -> {
                            if (jobExecution.getJobExecutionBatchStatus() == JobExecutionBatchStatus.STOPPED
                             || jobExecution.getJobExecutionBatchStatus() == JobExecutionBatchStatus.FAILED) {

                                boolean restartable = jobService.isJobRestartable(jobId);
                                executionsRO.stream()
                                        .filter(jobExecutionRO -> jobExecution.getId().equals(jobExecutionRO.getId()))
                                        .findFirst()
                                        .ifPresent(jobExecutionRO -> jobExecutionRO.setRestartable(restartable));
                            }
                        });

            }
        }

        return ok(resultRO);
    }

    /**
     * Find step executions page.
     *
     * @param jobExecutionId the job execution id
     * @param fromInd the from ind
     * @param itemCount the item count
     * @return the response
     * @throws Exception the exception
     */
    @GET
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/stepexecutions/{jobExecutionId}/{fromInd}/{itemCount}")
    @Operation(
        description = "Получить ограниченный список шагов для указанного запуска задачи.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = PaginatedJobStepExecutionsResultRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response findStepExecutionsPage(
            @Parameter(description = "Job execution id") @PathParam("jobExecutionId") final Long jobExecutionId,
            @Parameter(description = "From index") @PathParam("fromInd") final long fromInd,
            @Parameter(description = "Count") @PathParam("itemCount") final int itemCount) {

        StepExecutionPaginatedResultDTO<JobExecutionStepDTO> paginatedResult
            = (StepExecutionPaginatedResultDTO<JobExecutionStepDTO>) jobService.searchStepExecutions(jobExecutionId, fromInd,
                itemCount);

        PaginatedJobStepExecutionsResultRO resultRO = new PaginatedJobStepExecutionsResultRO();
        resultRO.setTotalCount(paginatedResult.getTotalCount());
        resultRO.setCompletedCount(paginatedResult.getFinishedCount());

        List<JobStepExecutionRO> executionsRO = JobConverter.convertStepExecutionsDTOToRO(paginatedResult.getPage());
        resultRO.setContent(executionsRO);

        return ok(resultRO);
    }

    /**
     * Gets the job execution percentage.
     *
     * @param jobId the job id
     * @param execId the exec id
     * @return the job execution percentage
     * @throws Exception the exception
     */
    @GET
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/executions/{jobId}/{execId}/progress")
    @Operation(
        description = "Получить относительную готовность выполнения задачи.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Double.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response getJobExecutionPercentage(
            @PathParam("jobId") final Long jobId,
            @PathParam("execId") final Long execId) {
        final double progress = jobService.getJobProgress(jobId, execId);
        return ok(progress);
    }

    /**
     * Start job.
     *
     * @param jobId the job id
     * @return the response
     * @throws Exception the exception
     */
    @POST
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + EXECUTE_DATA_OPERATIONS + "')")
    @Path(value = "/start/{jobId}")
    @Operation(
        description = "Запуск задачи.",
        method = "POST",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = JobExecutionDTO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response startJob(@Parameter(description = "Идентификатор задачи", in = ParameterIn.PATH) @PathParam("jobId") final Long jobId) {
        JobExecutionDTO execution = jobService.start(jobId);
        return ok(execution.getId());
    }

    /**
     * Stop job.
     *
     * @param jobId the job id
     * @return the response
     * @throws Exception the exception
     */
    @POST
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + EXECUTE_DATA_OPERATIONS + "')")
    @Path(value = "/stop/{jobId}")
    @Operation(
        description = "Остановка задачи.",
        method = "POST",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Boolean.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response stopJob(@Parameter(description = "Идентификатор задачи", in = ParameterIn.PATH) @PathParam("jobId") final Long jobId) {
        jobService.stop(jobId);
        return ok(true);
    }

    /**
     * Restart job.
     *
     * @param jobExecutionId the job execution id
     * @return the response
     * @throws Exception the exception
     */
    @POST
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + EXECUTE_DATA_OPERATIONS + "')")
    @Path(value = "/restart/{jobExecutionId}")
    @Operation(
        description = "Перезапуск задачи.",
        method = "POST",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Long.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response restartJob(@Parameter(description = "Идентификатор запуска задачи", in = ParameterIn.PATH) @PathParam("jobExecutionId") final Long jobExecutionId) {
        Long newJobExecutionId = jobService.restart(jobExecutionId);
        return ok(newJobExecutionId);
    }

    /**
     * Mark job.
     *
     * @param jobId the job id
     * @param enabled the enabled
     * @return the response
     * @throws Exception the exception
     */
    @POST
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isUpdateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/mark/{jobId}/{enabled}")
    @Operation(
        description = "Активация/деактивация задачи.",
        method = "POST",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Boolean.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response markJob(
            @Parameter(description = "Идентификатор задачи", in = ParameterIn.PATH) @PathParam("jobId") final Long jobId,
            @Parameter(description = "Флаг состояния", in = ParameterIn.PATH) @PathParam("enabled") final boolean enabled) {
        if (enabled) {
            jobService.enableJob(jobId);
        } else {
            jobService.disableJob(jobId);
        }

        return ok(true);
    }

    /**
     * Removes the job.
     *
     * @param jobId the job id
     * @return the response
     * @throws Exception the exception
     */
    @DELETE
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isDeleteRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/{jobId}")
    @Operation(
        description = "Удалить задачу.",
        method = "DELETE",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Boolean.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response removeJob(@Parameter(description = "Идентификатор задачи", in = ParameterIn.PATH) @PathParam("jobId") final Long jobId) {
        jobService.removeJob(jobId);
        return ok(true);
    }

    /**
     * Find job triggers.
     *
     * @param jobId the job id
     * @return the response
     * @throws Exception the exception
     */
    @GET
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/{jobId}/triggers")
    @Operation(
        description = "Получить список триггеров.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = List.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response findJobTriggers(@Parameter(description = "Идентификатор задачи", in = ParameterIn.PATH) @PathParam("jobId") final Long jobId) {

        Assert.notNull(jobId, "jobId must not be null");

        final List<JobTriggerDTO> jobTriggers = jobService.findJobTriggers(jobId);

        final List<JobTriggerRO> jobTriggerROs = JobConverter.convertJobTriggersDtoToRo(jobTriggers);

        return ok(jobTriggerROs);
    }

    /**
     * Creates the job trigger.
     *
     * @param jobId the job id
     * @param jobTrigger the job trigger
     * @return the response
     * @throws Exception the exception
     */
    @PUT
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isCreateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/{jobId}/triggers/")
    @Operation(
        description = "Сохранить новый триггер",
        method = "PUT",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = JobTriggerRO.class)), description = "Описание триггера"),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = JobTriggerRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response createJobTrigger(
            @Parameter(description = "Идентификатор задачи", in = ParameterIn.PATH) @PathParam("jobId") final Long jobId,
            final JobTriggerRO jobTrigger) {

        Assert.notNull(jobId, "jobId must not be null");

        final JobTriggerDTO triggerDto = JobConverter.convertJobTriggerRoToDto(jobTrigger);
        triggerDto.setFinishJobId(jobId);

        final JobTriggerRO resultTrigger = JobConverter.convertJobTriggerDtoToRo(jobService.saveJobTrigger(triggerDto));

        return ok(resultTrigger);
    }

    /**
     * Save job trigger.
     *
     * @param jobId the job id
     * @param triggerId the trigger id
     * @param jobTrigger the job trigger
     * @return the response
     * @throws Exception the exception
     */
    @PUT
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isUpdateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/{jobId}/triggers/{triggerId}")
    @Operation(
        description = "Редактировать триггер",
        method = "PUT",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = JobTriggerRO.class)), description = "Описание триггера"),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = JobTriggerRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response saveJobTrigger(
            @Parameter(description = "Идентификатор задачи", in = ParameterIn.PATH) @PathParam("jobId") final Long jobId,
            @Parameter(description = "Идентификатор триггера", in = ParameterIn.PATH) @PathParam("triggerId") final Long triggerId,
            final JobTriggerRO jobTrigger) {

        Assert.notNull(jobId, "jobId must not be null");
        Assert.notNull(triggerId, "triggerId must not be null");

        final JobTriggerDTO triggerDto = JobConverter.convertJobTriggerRoToDto(jobTrigger);
        triggerDto.setId(triggerId);
        triggerDto.setFinishJobId(jobId);

        final JobTriggerRO newJobTrigger = JobConverter.convertJobTriggerDtoToRo(jobService.saveJobTrigger(triggerDto));

        return ok(newJobTrigger);
    }

    /**
     * Removes the trigger.
     *
     * @param jobId the job id
     * @param triggerId the trigger id
     * @return the response
     * @throws Exception the exception
     */
    @DELETE
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isDeleteRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/{jobId}/triggers/{triggerId}")
    @Operation(
        description = "Удалить триггер",
        method = "DELETE",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Boolean.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response removeTrigger(
            @Parameter(description = "Идентификатор задачи", in = ParameterIn.PATH) @PathParam("jobId") final Long jobId,
            @Parameter(description = "Идентификатор триггера", in = ParameterIn.PATH) @PathParam("triggerId") final Long triggerId) {
        jobService.removeTrigger(jobId, triggerId);
        return ok(true);
    }

    /**
     * Export jobs.
     *
     * @param jobsIds the jobs ids
     * @return the response
     * @throws Exception the exception
     */
    @POST
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')")
    @Path(value = "/export-jobs")
    @Operation(
        description = "Экспорт операций",
        method = "POST",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = List.class)), description = "Список идентификаторов"),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response exportJobs(List<Long> jobsIds) {
        jobService.exportJobs(SecurityUtils.getCurrentUserName(), jobsIds);
        return ok(new RestResponse<>(true));
    }

    /**
     * Import jobs.
     *
     * @param fileAttachment JSON file with jobs definitions.
     * @return the response
     */
    @POST
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or ("
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isCreateRightsForResource('"+ ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')"
            + " and "
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isUpdateRightsForResource('"+ ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')"
            + " and "
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isDeleteRightsForResource('"+ ADMIN_SYSTEM_MANAGEMENT + "," + DATA_OPERATIONS_MANAGEMENT + "')"
            + ")"
            )
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path(value = "/import-jobs")
    @Operation(
        description = "Импорт операций",
        method = "POST",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = String.class)), description = "Содержимое импорта"),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Boolean.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response importJobs(
            @Parameter(description = "Импортируемый файл") @Multipart(value = DATA_PARAM_FILE) final Attachment fileAttachment) {
        if (Objects.isNull(fileAttachment)) {
            return okOrNotFound(null);
        }
        java.nio.file.Path path = FileUtils.saveFileTempFolder(fileAttachment);
        jobService.importJobs(SecurityUtils.getCurrentUserName(), path);

        return ok(new RestResponse<>(true));
    }

    /**
     * Set multiselect value from meta for every existing job parameter.
     *
     * @param jobs List of jobs to fill with metadata.
     */
    private void fillJobParametersMeta(List<JobRO> jobs) {
        if (CollectionUtils.isEmpty(jobs)) {
            return;
        }

        for (JobRO job : jobs) {
            if (job.getParameters() == null) {
                continue;
            }

            JobTemplateParameters parameters = jobService.findJobTemplateParameters(job.getJobNameReference());

            if (parameters != null && !CollectionUtils.isEmpty(parameters.getValueMap())) {
                for (final Map.Entry<String, Object> entry : parameters.getValueMap().entrySet()) {
                    final Object val = entry.getValue();

                    if (val instanceof JobEnumParamExtractor) {
                        final JobEnumType jobEnum = ((JobEnumParamExtractor) val).extractParameters();

                        if (jobEnum.isMultiSelect()) {
                            job.getParameters().stream()
                                    .filter(jobParameterRO -> jobParameterRO.getName().equals(entry.getKey()))
                                    .forEach(jobParameterRO -> jobParameterRO.setMultiSelect(true));
                        }
                    }
                }
            }
        }
    }
}
