/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package com.huahui.datasphere.platform.rest.core.converter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.unidata.mdm.core.dto.job.JobDTO;
import org.unidata.mdm.core.dto.job.JobExecutionDTO;
import org.unidata.mdm.core.dto.job.JobExecutionExitStatusDTO;
import org.unidata.mdm.core.dto.job.JobExecutionStepDTO;
import org.unidata.mdm.core.dto.job.JobParameterDTO;
import org.unidata.mdm.core.dto.job.JobTemplateDTO;
import org.unidata.mdm.core.dto.job.JobTemplateParameterDTO;
import org.unidata.mdm.core.dto.job.JobTriggerDTO;
import org.unidata.mdm.core.service.job.JobEnumParamExtractor;
import org.unidata.mdm.core.service.job.JobTemplateParameters;
import org.unidata.mdm.core.type.job.JobEnumType;
import org.unidata.mdm.core.type.job.JobExecutionBatchStatus;
import org.unidata.mdm.system.util.ConvertUtils;

import com.huahui.datasphere.platform.rest.core.ro.job.JobExecutionRO;
import com.huahui.datasphere.platform.rest.core.ro.job.JobParameterRO;
import com.huahui.datasphere.platform.rest.core.ro.job.JobRO;
import com.huahui.datasphere.platform.rest.core.ro.job.JobStepExecutionRO;
import com.huahui.datasphere.platform.rest.core.ro.job.JobTriggerRO;

/**
 * @author Mikhail Mikhailov on Dec 25, 2019
 */
public class JobConverter {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JobConverter.class);
    /**
     * Constructor.
     */
    private JobConverter() {
        super();
    }

    public static List<JobRO> convertJobsDtoToRo(final List<JobDTO> jobDTOs) {

        Assert.notNull(jobDTOs, "Job DTO list is null");
        final List<JobRO> result = new ArrayList<>(jobDTOs.size());

        for (final JobDTO dto : jobDTOs) {
            final JobRO ro = new JobRO();
            ro.setId(dto.getId());
            ro.setName(dto.getName());
            ro.setCronExpression(dto.getCronExpression());
            ro.setJobNameReference(dto.getJobNameReference());
            ro.setEnabled(dto.isEnabled());
            ro.setError(dto.isError());
            ro.setDescription(dto.getDescription());
            ro.setTags(dto.getTags());
            ro.setParameters(convertParamDtoToRo(dto.getParameters()));

            result.add(ro);
        }

        return result;
    }

    public static List<JobParameterRO> convertTemplateParamDtoToRo(final List<JobTemplateParameterDTO> parameterDTOs) {
        if (parameterDTOs == null) {
            return new ArrayList<>();
        }

        final List<JobParameterRO> result = new ArrayList<>(parameterDTOs.size());

        for (final JobTemplateParameterDTO dto : parameterDTOs) {
            final JobParameterRO ro = new JobParameterRO(dto.getName(), dto.getType());

            result.add(ro);
        }
        return result;
    }

    public static List<JobParameterRO> convertParamDtoToRo(final List<JobParameterDTO> parameterDTOs) {
        if (parameterDTOs == null) {
            return new ArrayList<>();
        }

        final List<JobParameterRO> result = new ArrayList<>(parameterDTOs.size());

        for (final JobParameterDTO dto : parameterDTOs) {
            final JobParameterRO ro;

            switch (dto.getType()) {
                case STRING:
                    ro = new JobParameterRO(dto.getName(), dto.getStringArrayValue());
                    break;
                case DATE:
                    ro = new JobParameterRO(dto.getName(), dto.getDateArrayValue());
                    break;
                case LONG:
                    ro = new JobParameterRO(dto.getName(), dto.getLongArrayValue());
                    break;
                case DOUBLE:
                    ro = new JobParameterRO(dto.getName(), dto.getDoubleArrayValue());
                    break;
                case BOOLEAN:
                    ro = new JobParameterRO(dto.getName(), dto.getBooleanArrayValue());
                    break;
                default:
                    continue;
            }

            ro.setId(dto.getId());
            result.add(ro);
        }
        return result;
    }

    public static JobDTO convertJobRoToDto(final JobRO ro) {
        Assert.notNull(ro, "Job RO object is null");

        final JobDTO dto = new JobDTO();
        dto.setId(ro.getId());
        dto.setName(ro.getName());
        dto.setEnabled(ro.isEnabled());
        dto.setError(ro.isError());
        dto.setDescription(ro.getDescription());
        dto.setCronExpression(ro.getCronExpression());
        dto.setJobNameReference(ro.getJobNameReference());
        dto.setSkipCronWarnings(ro.isSkipCronWarnings());
        dto.setTags(ro.getTags());
        dto.setParameters(convertParamRoToDto(ro.getParameters()));

        return dto;
    }

    public static List<JobParameterDTO> convertParamRoToDto(final List<JobParameterRO> parameterROs) {
        if (parameterROs == null) {
            return new ArrayList<>();
        }

        final List<JobParameterDTO> result = new ArrayList<>(parameterROs.size());

        for (final JobParameterRO ro : parameterROs) {
            final JobParameterDTO dto;
            switch (ro.getType()) {
                case STRING:
                    dto = new JobParameterDTO(ro.getName(), ro.getStringArrayValue());
                    break;
                case DATE:
                    dto = new JobParameterDTO(ro.getName(), ro.getDateArrayValue());
                    break;
                case LONG:
                    dto = new JobParameterDTO(ro.getName(), ro.getLongArrayValue());
                    break;
                case DOUBLE:
                    dto = new JobParameterDTO(ro.getName(), ro.getDoubleArrayValue());
                    break;
                case BOOLEAN:
                    dto = new JobParameterDTO(ro.getName(), ro.getBooleanArrayValue());
                    break;
                default:
                    continue;
            }

            dto.setId(ro.getId());
            result.add(dto);
        }
        return result;
    }


    public static JobRO convertJobDtoToRo(final JobDTO dto) {
        Assert.notNull(dto, "Job DTO object is null");

        final JobRO ro = new JobRO();
        ro.setId(dto.getId());
        ro.setName(dto.getName());
        ro.setEnabled(dto.isEnabled());
        ro.setError(dto.isError());
        ro.setDescription(dto.getDescription());
        ro.setCronExpression(dto.getCronExpression());
        ro.setJobNameReference(dto.getJobNameReference());
        ro.setSkipCronWarnings(dto.isSkipCronWarnings());
        ro.setTags(dto.getTags());
        ro.setParameters(convertParamDtoToRo(dto.getParameters()));

        return ro;
    }

    public static List<JobExecutionRO> convertJobExecutionsDTOToRO(Collection<JobExecutionDTO> executions, Long jobId) {
        List<JobExecutionRO> result = new ArrayList<>();

        if (!CollectionUtils.isEmpty(executions)) {
            result.addAll(executions.stream()
                    .map(execution -> convertJobExecutionDTOToRO(execution, jobId))
                    .collect(Collectors.toList()));
        }

        return result;
    }

    public static JobExecutionRO convertJobExecutionDTOToRO(final JobExecutionDTO jobExecution, Long jobId) {
        Assert.notNull(jobExecution, "jobExecution object is null");

        final JobExecutionRO ro = new JobExecutionRO();
        ro.setId(jobExecution.getId());
        // Set external unidata jobId instead of springBatch jobId.
        ro.setJobId(jobId);
        ro.setStatus(Objects.isNull(jobExecution.getJobExecutionBatchStatus()) ? null : jobExecution.getJobExecutionBatchStatus().name());
        ro.setStartTime(ConvertUtils.zonedDateTime2Date(jobExecution.getStartTime()));
        ro.setEndTime(ConvertUtils.zonedDateTime2Date(jobExecution.getEndTime()));

        if (jobExecution.getJobExecutionExitStatus().getExitCode().equals(JobExecutionBatchStatus.FAILED.name())) {
            ro.setExitDescription(jobExecution.getJobExecutionExitStatus().getExitDescription());
        }

        ro.setStepExecutions(convertStepExecutionsDTOToRO(jobExecution.getJobExecutionSteps()));

        return ro;
    }

    public static List<JobStepExecutionRO> convertStepExecutionsDTOToRO(Collection<JobExecutionStepDTO> stepExecutions) {
        List<JobStepExecutionRO> result = new ArrayList<>();

        if (!CollectionUtils.isEmpty(stepExecutions)) {
            result.addAll(stepExecutions.stream()
                    .map(JobConverter::convertStepExecutionDTOToRO)
                    .collect(Collectors.toList()));
        }

        return result;
    }

    public static JobStepExecutionRO convertStepExecutionDTOToRO(final JobExecutionStepDTO stepExecution) {
        Assert.notNull(stepExecution, "stepExecution object is null");

        JobStepExecutionRO ro = new JobStepExecutionRO();

        ro.setId(stepExecution.getId());
        ro.setStepName(stepExecution.getStepName());
        ro.setStartTime(ConvertUtils.zonedDateTime2Date(stepExecution.getStartTime()));
        ro.setEndTime(ConvertUtils.zonedDateTime2Date(stepExecution.getEndTime()));
        ro.setStatus(stepExecution.getJobExecutionBatchStatus() == null ? null : stepExecution.getJobExecutionBatchStatus().name());
        ro.setJobExecutionId(stepExecution.getJobExecutionId());

        JobExecutionExitStatusDTO exitStatus = stepExecution.getJobExecutionExitStatus();

        if (exitStatus != null) {
            ro.setExitCode(exitStatus.getExitCode());
            ro.setExitDescription(exitStatus.getExitDescription());
        }

        return ro;
    }

    public static JobRO convertJobTemplateDtoToRo(final JobTemplateDTO jobTemplateDTO) {
        Assert.notNull(jobTemplateDTO, "'jobTemplateDTO' parameter cannot be null");

        final JobRO result = new JobRO();

        result.setName(jobTemplateDTO.getName());
        result.setJobNameReference(jobTemplateDTO.getJobNameReference());
        result.setParameters(convertTemplateParamDtoToRo(jobTemplateDTO.getParameters()));

        return result;
    }

    public static List<JobParameterRO> convertJobTemplateParametersToRo(final JobTemplateParameters parameters) {
        if (parameters == null || CollectionUtils.isEmpty(parameters.getValueMap())) {
            return Collections.emptyList();
        }

        final List<JobParameterRO> result = new ArrayList<>();
        for (final Entry<String, Object> entry : parameters.getValueMap().entrySet()) {
            final JobParameterRO ro;
            final Object val = entry.getValue();

            if (val instanceof String) {
                ro = new JobParameterRO(entry.getKey(), (String) val);
            } else if (val instanceof Long) {
                ro = new JobParameterRO(entry.getKey(), (Long) val);
            } else if (val instanceof Double) {
                ro = new JobParameterRO(entry.getKey(), (Double) val);
            } else if (val instanceof ZonedDateTime) {
                ro = new JobParameterRO(entry.getKey(), (ZonedDateTime) val);
            } else if (val instanceof Boolean) {
                ro = new JobParameterRO(entry.getKey(), (Boolean) val);
            } else if (val instanceof JobEnumParamExtractor) {
                final JobEnumType jobEnum = ((JobEnumParamExtractor) val).extractParameters();
                if (jobEnum != null && !CollectionUtils.isEmpty(jobEnum.getParameters())) {
                    switch (jobEnum.getParameterType()) {
                        case STRING:
                            ro = new JobParameterRO(entry.getKey(), jobEnum.getParameters().toArray(new String[0]));
                            break;
                        case DATE:
                            ro = new JobParameterRO(entry.getKey(), jobEnum.getParameters().toArray(new ZonedDateTime[]{}));
                            break;
                        case LONG:
                            ro = new JobParameterRO(entry.getKey(), jobEnum.getParameters().toArray(new Long[]{}));
                            break;
                        case DOUBLE:
                            ro = new JobParameterRO(entry.getKey(), jobEnum.getParameters().toArray(new Double[]{}));
                            break;
                        case BOOLEAN:
                            if (jobEnum.getParameters().size() == 1) {
                                ro = new JobParameterRO(entry.getKey(), (Boolean) jobEnum.getParameters().get(0));
                            } else {
                                final Boolean[] boolArray = new Boolean[]{Boolean.FALSE, Boolean.TRUE};
                                ro = new JobParameterRO(entry.getKey(), boolArray);
                            }
                            break;
                        default:
                            ro = null;
                    }

                    if (Objects.nonNull(ro)) {
                        ro.setMultiSelect(jobEnum.isMultiSelect());
                    }

                } else {
                    ro = null;
                }
            } else {
                LOGGER.error("Cannot convert parameter [" + entry.getKey() + "] of type [" + (val != null ? val.getClass() : null) + "]");
                ro = null;
            }

            if (ro != null) {
                result.add(ro);
            }
        }

        return result;
    }

    public static JobTriggerRO convertJobTriggerDtoToRo(final JobTriggerDTO dto) {
        Assert.notNull(dto, "Cannot convert null element");

        final JobTriggerRO ro = new JobTriggerRO();
        ro.setId(dto.getId());
        ro.setStartJobId(dto.getStartJobId());
        ro.setSuccessRule(dto.getSuccessRule());
        ro.setName(dto.getName());
        ro.setDescription(dto.getDescription());

        return ro;
    }

    public static List<JobDTO> convertJobsRoToDto(List<JobRO> jobROs) {
        return jobROs.stream().map(JobConverter::convertJobRoToDto).collect(Collectors.toList());
    }

    public static List<JobTriggerRO> convertJobTriggersDtoToRo(final List<JobTriggerDTO> dtos) {
        return dtos.stream().map(JobConverter::convertJobTriggerDtoToRo).collect(Collectors.toList());
    }

    public static JobTriggerDTO convertJobTriggerRoToDto(JobTriggerRO ro) {
        Assert.notNull(ro, "Cannot convert null element");

        final JobTriggerDTO dto = new JobTriggerDTO();
        dto.setId(ro.getId());
        dto.setStartJobId(ro.getStartJobId());
        dto.setSuccessRule(ro.getSuccessRule());
        dto.setName(ro.getName());
        dto.setDescription(ro.getDescription());

        return dto;
    }
}
