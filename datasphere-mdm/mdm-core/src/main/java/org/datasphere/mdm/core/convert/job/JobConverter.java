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

package com.huahui.datasphere.mdm.core.convert.job;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.util.Assert;
import com.huahui.datasphere.mdm.system.util.ConvertUtils;

import com.huahui.datasphere.mdm.core.dto.job.JobDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobExecutionDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobExecutionExitStatusDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobExecutionStepDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobParameterDTO;
import com.huahui.datasphere.mdm.core.dto.job.JobTriggerDTO;
import com.huahui.datasphere.mdm.core.dto.job.StepExecutionDTO;
import com.huahui.datasphere.mdm.core.exception.CoreExceptionIds;
import com.huahui.datasphere.mdm.core.exception.JobException;
import com.huahui.datasphere.mdm.core.po.job.JobPO;
import com.huahui.datasphere.mdm.core.po.job.JobParameterPO;
import com.huahui.datasphere.mdm.core.po.job.JobTriggerPO;
import com.huahui.datasphere.mdm.core.type.job.CustomJobParameter;
import com.huahui.datasphere.mdm.core.type.job.JobExecutionBatchStatus;
import com.huahui.datasphere.mdm.core.type.job.JobParameterType;

/**
 * @author Denis Kostovarov
 */
public class JobConverter {
    private static final Logger log = LoggerFactory.getLogger(JobConverter.class);

    private JobConverter() {
        // No-op.
    }

    public static List<JobDTO> convertJobsPoToDto(final Collection<JobPO> jobPOs) {
        Assert.notNull(jobPOs, "Job POs list is null");

        final List<JobDTO> result = new ArrayList<>(jobPOs.size());

        for (final JobPO po : jobPOs) {
            JobDTO dto = convertJobPoToDto(po);

            result.add(dto);
        }

        return result;
    }

    public static List<JobPO> convertJobsDtoToPo(final Collection<JobDTO> jobDTOs) {
        Assert.notNull(jobDTOs, "Job DTOs list is null");

        final List<JobPO> result = new ArrayList<>(jobDTOs.size());

        for (final JobDTO dto : jobDTOs) {
            JobPO po = convertJobDtoToPo(dto);

            result.add(po);
        }

        return result;
    }

    public static List<JobParameterDTO> convertParamPoToDto(final List<JobParameterPO> parameterPOs) {
        if (parameterPOs == null) {
            return new ArrayList<>();
        }

        final List<JobParameterDTO> result = new ArrayList<>(parameterPOs.size());

        for (final JobParameterPO po : parameterPOs) {
            final JobParameterDTO dto;
            switch (po.getType()) {
                case STRING:
                case STRING_ARR:
                    dto = new JobParameterDTO(po.getName(), (String[])po.getArrayValue());
                    break;
                case DATE:
                case DATE_ARR:
                    dto = new JobParameterDTO(po.getName(), (ZonedDateTime[])po.getArrayValue());
                    break;
                case LONG:
                case LONG_ARR:
                    dto = new JobParameterDTO(po.getName(), (Long[])po.getArrayValue());
                    break;
                case DOUBLE:
                case DOUBLE_ARR:
                    dto = new JobParameterDTO(po.getName(), (Double[])po.getArrayValue());
                    break;
                case BOOLEAN:
                case BOOLEAN_ARR:
                    dto = new JobParameterDTO(po.getName(), (Boolean[])po.getArrayValue());
                    break;
                default:
                    dto = null;
            }
            dto.setId(po.getId());

            result.add(dto);
        }
        return result;
    }


    public static JobPO convertJobDtoToPo(JobDTO dto) {
        Assert.notNull(dto, "Job DTO object is null");

        final JobPO po = new JobPO();
        po.setId(dto.getId());
        po.setName(dto.getName());
        po.setEnabled(dto.isEnabled());
        po.setError(dto.isError());
        po.setDescription(dto.getDescription());
        po.setCronExpression(dto.getCronExpression());
        po.setJobNameReference(dto.getJobNameReference());
        po.setTags(dto.getTags());
        po.setParameters(convertParamDtoToPo(dto.getParameters()));

        return po;
    }

    private static List<JobParameterPO> convertParamDtoToPo(final List<JobParameterDTO> parameterDTOs) {
        if (parameterDTOs == null) {
            return new ArrayList<>();
        }

        final List<JobParameterPO> result = new ArrayList<>(parameterDTOs.size());

        for (final JobParameterDTO dto : parameterDTOs) {
            final JobParameterPO po;
            switch (dto.getType()) {
                case STRING: {
                    po = dto.getValueSize() > 1 ?
                            new JobParameterPO(dto.getName(), dto.getStringArrayValue()) :
                            new JobParameterPO(dto.getName(), dto.getStringValue());
                    break;
                }
                case DATE: {
                    po = dto.getValueSize() > 1 ?
                            new JobParameterPO(dto.getName(), dto.getDateArrayValue()) :
                            new JobParameterPO(dto.getName(), dto.getDateValue());
                    break;
                }
                case LONG: {
                    po = dto.getValueSize() > 1 ?
                            new JobParameterPO(dto.getName(), dto.getLongArrayValue()) :
                            new JobParameterPO(dto.getName(), dto.getLongValue());
                    break;
                }
                case DOUBLE: {
                    po = dto.getValueSize() > 1 ?
                            new JobParameterPO(dto.getName(), dto.getDoubleArrayValue()) :
                            new JobParameterPO(dto.getName(), dto.getDoubleValue());
                    break;
                }
                case BOOLEAN: {
                    po = dto.getValueSize() > 1 ?
                            new JobParameterPO(dto.getName(), dto.getBooleanArrayValue()) :
                            new JobParameterPO(dto.getName(), dto.getBooleanValue());
                    break;
                }
                default:
                    po = null;
            }
            po.setId(dto.getId());

            result.add(po);
        }
        return result;
    }
    public static JobExecutionDTO jobExecutionDTOFromJobExecution(
            final JobDTO jobDTO,
            final JobExecution jobExecution
    ) {
        return new JobExecutionDTO(
                jobExecution.getId(),
                jobDTO,
                jobExecution.getJobParameters().getParameters().entrySet()
                        .stream()
                        .map(entry -> springJobParameterToJobParameterDTO(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()),
                ConvertUtils.zonedDateTimeFromDate(jobExecution.getStartTime()),
                ConvertUtils.zonedDateTimeFromDate(jobExecution.getCreateTime()),
                ConvertUtils.zonedDateTimeFromDate(jobExecution.getEndTime()),
                ConvertUtils.zonedDateTimeFromDate(jobExecution.getLastUpdated()),
                JobExecutionBatchStatus.valueOf(jobExecution.getStatus().name()),
                new JobExecutionExitStatusDTO(
                        jobExecution.getExitStatus().getExitCode(),
                        jobExecution.getExitStatus().getExitDescription()
                ),
                jobExecution.getStepExecutions().stream()
                        .map(JobConverter::stepExecutionDTOFromStepExecution)
                        .collect(Collectors.toList())
        );
    }

    public static JobExecutionStepDTO stepExecutionDTOFromStepExecution(final StepExecution step) {

        JobExecutionStepDTO jes = new JobExecutionStepDTO(
                step.getId(),
                step.getJobExecutionId(),
                step.getStepName(),
                JobExecutionBatchStatus.valueOf(step.getStatus().name()),
                new JobExecutionExitStatusDTO(
                        step.getExitStatus().getExitCode(),
                        step.getExitStatus().getExitDescription()
                ),

                ConvertUtils.zonedDateTimeFromDate(step.getStartTime()),
                ConvertUtils.zonedDateTimeFromDate(step.getEndTime()),
                ConvertUtils.zonedDateTimeFromDate(step.getLastUpdated()));

        jes.setCommitCount(step.getCommitCount());
        jes.setProcessSkipCount(step.getProcessSkipCount());
        jes.setReadCount(step.getReadCount());
        jes.setReadSkipCount(step.getReadSkipCount());
        jes.setRollbackCount(step.getRollbackCount());
        jes.setWriteCount(step.getWriteCount());
        jes.setWriteSkipCount(step.getWriteSkipCount());

        return jes;
    }

    // TODO: fix that convertion from Spring to DTO
    public static JobParameterDTO springJobParameterToJobParameterDTO(String name, JobParameter jobParameter) {

        if (jobParameter == null || jobParameter.getValue() == null) {
            return new JobParameterDTO(name);
        }

        switch (jobParameter.getType()) {
            case DATE:
                return new JobParameterDTO(
                        name,
                        ZonedDateTime.ofInstant(((Date) jobParameter.getValue()).toInstant(), ZoneId.systemDefault())
                );
            case DOUBLE: {
                if (jobParameter instanceof CustomJobParameter) {
                    return new JobParameterDTO(name, (Double[]) jobParameter.getValue());
                }
                return new JobParameterDTO(name, (Double) jobParameter.getValue());
            }
            case LONG: {
                if (jobParameter instanceof CustomJobParameter) {
                    return new JobParameterDTO(name, (Long[]) jobParameter.getValue());
                }
                return new JobParameterDTO(name, (Long) jobParameter.getValue());
            }
            case STRING: {
                // TODO: resolve problem with convertion Spring STRING type to Unidata BOOLEAN type.
                if (jobParameter instanceof CustomJobParameter) {
                    return new JobParameterDTO(name, (String[]) jobParameter.getValue());
                }
                return new JobParameterDTO(name, (String) jobParameter.getValue());
            }
        }
        return new JobParameterDTO(name, jobParameter.getValue().toString());
    }

    public static JobDTO convertJobPoToDto(final JobPO jobPo) {
        Assert.notNull(jobPo, "Job PO object is null");

        final JobDTO dto = new JobDTO();
        dto.setId(jobPo.getId());
        dto.setName(jobPo.getName());
        dto.setEnabled(jobPo.isEnabled());
        dto.setError(jobPo.isError());
        dto.setDescription(jobPo.getDescription());
        dto.setCronExpression(jobPo.getCronExpression());
        dto.setJobNameReference(jobPo.getJobNameReference());
        dto.setTags(jobPo.getTags());
        dto.setParameters(convertParamPoToDto(jobPo.getParameters()));

        return dto;
    }


    public static JobParameterType convertParameterType(JobParameterPO.JobParameterType type) {
        Assert.notNull(type, "'type' cannot be null");

        switch (type) {
            case STRING:
                return JobParameterType.STRING;
            case LONG:
                return JobParameterType.LONG;
            case DOUBLE:
                return JobParameterType.DOUBLE;
            case DATE:
                return JobParameterType.DATE;
            case BOOLEAN:
                return JobParameterType.BOOLEAN;
        }

        throw new JobException("Unable to convert job parameter type: [{}]", CoreExceptionIds.EX_JOB_PARAMETER_INVALID_TYPE, type);
    }



    public static List<JobTriggerDTO> convertJobTriggersPoToDTo(final List<JobTriggerPO> pos) {
        return pos.stream().map(JobConverter::convertJobTriggerPoToDto).collect(Collectors.toList());
    }

    public static JobTriggerPO convertJobTriggerDtoToPo(final JobTriggerDTO dto) {
        Assert.notNull(dto, "Cannot convert null element");

        final JobTriggerPO po = new JobTriggerPO();
        po.setId(dto.getId());
        po.setFinishJobId(dto.getFinishJobId());
        po.setStartJobId(dto.getStartJobId());
        po.setSuccessRule(dto.getSuccessRule());
        po.setName(dto.getName());
        po.setDescription(dto.getDescription());

        return po;
    }

    public static JobTriggerDTO convertJobTriggerPoToDto(final JobTriggerPO po) {
        Assert.notNull(po, "Cannot convert null element");

        final JobTriggerDTO dto = new JobTriggerDTO();
        dto.setId(po.getId());
        dto.setFinishJobId(po.getFinishJobId());
        dto.setStartJobId(po.getStartJobId());
        dto.setSuccessRule(po.getSuccessRule());
        dto.setName(po.getName());
        dto.setDescription(po.getDescription());

        return dto;
    }

    public static StepExecutionDTO convertStepExecutionToDTO(final StepExecution stepExecution) {
        final StepExecutionDTO stepExecutionDTO = new StepExecutionDTO();
        stepExecutionDTO.setCommitCount(stepExecution.getCommitCount());
        stepExecutionDTO.setEndTime(stepExecution.getEndTime());
        stepExecutionDTO.setExitStatus(
                new StepExecutionDTO.ExitStatus(
                        stepExecution.getExitStatus().getExitCode(),
                        stepExecution.getExitStatus().getExitDescription()
                )
        );
        stepExecutionDTO.setFailureExceptions(stepExecution.getFailureExceptions());
        stepExecutionDTO.setFilterCount(stepExecution.getFilterCount());
        stepExecutionDTO.setJobExecution(
                jobExecutionDTOFromJobExecution(null, stepExecution.getJobExecution())
        );
        stepExecutionDTO.setLastUpdated(stepExecution.getLastUpdated());
        stepExecutionDTO.setProcessSkipCount(stepExecution.getProcessSkipCount());
        stepExecutionDTO.setReadCount(stepExecution.getReadCount());
        stepExecutionDTO.setReadSkipCount(stepExecution.getReadSkipCount());
        stepExecutionDTO.setRollbackCount(stepExecution.getRollbackCount());
        stepExecutionDTO.setStartTime(stepExecution.getStartTime());
        stepExecutionDTO.setStatus(StepExecutionDTO.BatchStatus.valueOf(stepExecution.getStatus().name()));
        stepExecutionDTO.setStepName(stepExecution.getStepName());
        stepExecutionDTO.setTerminateOnly(stepExecution.isTerminateOnly());
        stepExecutionDTO.setWriteCount(stepExecution.getWriteCount());
        stepExecutionDTO.setWriteSkipCount(stepExecution.getWriteSkipCount());
        return stepExecutionDTO;
    }
}
