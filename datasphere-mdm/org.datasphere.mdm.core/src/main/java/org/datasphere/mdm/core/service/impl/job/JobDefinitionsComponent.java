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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.datasphere.mdm.core.context.JobDefinitionSchedulingContext;
import org.datasphere.mdm.core.context.JobDefinitionUpsertContext;
import org.datasphere.mdm.core.context.JobDefinitionsEnableContext;
import org.datasphere.mdm.core.context.JobDefinitionsMarkContext;
import org.datasphere.mdm.core.context.JobDefinitionsQueryContext;
import org.datasphere.mdm.core.context.JobDefinitionsRemoveContext;
import org.datasphere.mdm.core.context.JobExecutionCleanupContext;
import org.datasphere.mdm.core.context.JobExecutionStatusContext;
import org.datasphere.mdm.core.context.JobExecutionStopContext;
import org.datasphere.mdm.core.convert.job.JobDefinitionConverter;
import org.datasphere.mdm.core.dao.JobDefinitionsDAO;
import org.datasphere.mdm.core.dao.JobExecutionsDAO;
import org.datasphere.mdm.core.dto.job.JobDefinitionUpsertResult;
import org.datasphere.mdm.core.dto.job.JobDefinitionsQueryResult;
import org.datasphere.mdm.core.dto.job.JobExecutionStatusResult;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.exception.JobException;
import org.datasphere.mdm.core.exception.JobValidationException;
import org.datasphere.mdm.core.po.AbstractObjectPO;
import org.datasphere.mdm.core.po.job.ExecutionStatePO;
import org.datasphere.mdm.core.po.job.JobDefinitionPO;
import org.datasphere.mdm.core.type.job.JobDefinition;
import org.datasphere.mdm.core.type.job.JobExecutionState;
import org.datasphere.mdm.core.type.job.JobExecutionStatus;
import org.datasphere.mdm.core.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.datasphere.mdm.system.exception.ValidationResult;

/**
 * @author Mikhail Mikhailov on Jun 19, 2021
 * Serves the JobDefinition part of the
 * JobDescriptor -> JobDefinition -> JobInstance - JobExecution
 * chain.
 */
@Component
public class JobDefinitionsComponent {
    /**
     * Allowed sort fields.
     */
    private static final String[] ALLOWED_SORT_FIELDS = {
            JobDefinitionPO.FIELD_ID,
            JobDefinitionPO.FIELD_JOB_NAME,
            JobDefinitionPO.FIELD_NAME
    };
    /**
     * Allowed sort order.
     */
    private static final String[] ALLOWED_SORT_ORDER = {
            "ASC",
            "DESC"
    };
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JobDefinitionsComponent.class);

    @Autowired
    private JobValidationComponent jobValidationComponent;

    @Autowired
    private JobSchedulingComponent jobSchedulingComponent;

    @Autowired
    private JobExecutionsComponent jobExecutionsComponent;

    @Autowired
    private JobDefinitionsDAO jobDefinitionsDAO;

    @Autowired
    private JobExecutionsDAO jobExecutionsDAO;

    @Autowired
    private JobDefinitionConverter jobDefinitionConverter;

    /**
     * Constructor.
     */
    public JobDefinitionsComponent() {
        super();
    }
    /**
     * Does get job definition(s) by request.
     * @param ctx the request
     * @return result
     */
    public JobDefinitionsQueryResult get(JobDefinitionsQueryContext ctx) {

        // 1. Load
        Map<String, Object> filter = Collections.emptyMap();
        List<JobDefinitionPO> pos;

        if (CollectionUtils.isNotEmpty(ctx.getJobDifinitionIds())) {
            pos = jobDefinitionsDAO.load(ctx.getJobDifinitionIds());
        } else {

            filter = new HashMap<>();
            if (ctx.isActiveOnly()) {
                filter.put(JobDefinitionPO.FIELD_ENABLED, Boolean.TRUE);
            } else if (ctx.isInactiveOnly()) {
                filter.put(JobDefinitionPO.FIELD_ENABLED, Boolean.FALSE);
            }

            filter.put(JobDefinitionPO.FIELD_JOB_NAME, ctx.getJobNames());
            filter.put(JobDefinitionPO.FIELD_TAGS, ctx.getTags());
            filter.put(JobDefinitionPO.FIELD_NAME, ctx.getDefinitionNames());
            filter.put(AbstractObjectPO.FIELD_CREATED_BY, ctx.getCreatedBy());
            filter.put(JobDefinitionPO.FIELD_STATUS, Objects.isNull(ctx.getLastFinishedWith()) ? null : ctx.getLastFinishedWith().name());

            pos = jobDefinitionsDAO.load(filter, ensureSortField(ctx), ensureOrderDirection(ctx), ctx.getCount(), ctx.getFrom());
        }

        // 2. Convert and return
        return new JobDefinitionsQueryResult(
                pos.stream()
                    .filter(Objects::nonNull)
                    .map(jobDefinitionConverter::from)
                    .map(jd -> Pair.of(jd, state(jd.getId())))
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (p1, p2) -> p1, LinkedHashMap::new)),
                CollectionUtils.isNotEmpty(ctx.getJobDifinitionIds()) ? pos.size() : jobDefinitionsDAO.count(filter));
    }
    /**
     * Does upsert a job definition.
     * @param ctx the job definition context
     * @return result
     */
    public JobDefinitionUpsertResult upsert(JobDefinitionUpsertContext ctx) {

        final List<ValidationResult> validations = new ArrayList<>();

        validations.addAll(jobValidationComponent.validate(ctx.getDefinition(), ctx.isSkipCronWarnings()));
        validations.addAll(jobValidationComponent.validate(ctx.getDefinition().getJobName(), ctx.getDefinition().getParametersMap()));

        if (!validations.isEmpty()) {
            throw new JobValidationException("Job definition [{}] contains errors.",
                    CoreExceptionIds.EX_JOB_UPSERT_VALIDATION_ERROR,
                    validations, ctx.getDefinition().getDisplayName());
        }

        final JobDefinition jd = ctx.getDefinition();
        final JobDefinitionPO po = jobDefinitionConverter.to(jd);

        // May be import
        if (Objects.isNull(jd.getId()) && ctx.isCheckByName()) {

            List<JobDefinitionPO> pos = jobDefinitionsDAO.load(
                    Map.of(JobDefinitionPO.FIELD_NAME, Collections.singleton(jd.getDisplayName())), null, null, null, null);

            for (JobDefinitionPO other : pos) {

                if (StringUtils.equals(other.getJobName(), jd.getJobName())) {
                    po.setId(other.getId());
                    break;
                }
            }
        }

        po.setEnabled(jd.isEnabled());

        // Insert
        if (Objects.isNull(jd.getId())) {

            po.setError(false);
            po.setCreateDate(new Date());
            po.setCreatedBy(SecurityUtils.getCurrentUserName());

            Long id = jobDefinitionsDAO.insert(po);

            jd.setCreateDate(po.getCreateDate());
            jd.setCreatedBy(po.getCreatedBy());
            jd.setId(id);

        // Update
        } else {

            po.setUpdateDate(new Date());
            po.setUpdatedBy(SecurityUtils.getCurrentUserName());

            jobDefinitionsDAO.update(po);

            jobSchedulingComponent.unschedule(JobDefinitionSchedulingContext.builder()
                    .jobDefinitionId(po.getId())
                    .build());

            jd.setUpdateDate(po.getUpdateDate());
            jd.setUpdatedBy(po.getUpdatedBy());
        }

        if (jd.isEnabled() && !jd.isError() && StringUtils.isNotBlank(po.getCronExpression())) {

            jobSchedulingComponent.schedule(JobDefinitionSchedulingContext.builder()
                    .jobDefinitionId(po.getId())
                    .cronExpression(po.getCronExpression())
                    .build());
        }

        return new JobDefinitionUpsertResult(jd);
    }
    /**
     * Does enable or disable job definition.
     * @param ctx the context
     */
    public void enable(JobDefinitionsEnableContext ctx) {

        List<JobDefinitionPO> pos = jobDefinitionsDAO.load(ctx.getJobDifinitionIds());
        if (CollectionUtils.isEmpty(pos)) {
            failNoIds(ctx.getJobDifinitionIds());
        }

        for (JobDefinitionPO po : pos) {

            if (ctx.isEnabled()) {

                jobDefinitionsDAO.enable(po.getId(), true);
                if (StringUtils.isNotBlank(po.getCronExpression())) {

                    jobSchedulingComponent.schedule(JobDefinitionSchedulingContext.builder()
                            .jobDefinitionId(po.getId())
                            .cronExpression(po.getCronExpression())
                            .build());
                }
            } else {

                jobDefinitionsDAO.enable(po.getId(), false);
                if (StringUtils.isNotBlank(po.getCronExpression())) {

                    jobSchedulingComponent.unschedule(JobDefinitionSchedulingContext.builder()
                            .jobDefinitionId(po.getId())
                            .build());
                }

                jobExecutionsComponent.stop(JobExecutionStopContext.builder()
                        .jobDefinitionId(po.getId())
                        .build());
            }
        }
    }
    /**
     * Mark definition (s) error state.
     * @param ctx the context
     */
    public void mark(JobDefinitionsMarkContext ctx) {

        List<JobDefinitionPO> pos = jobDefinitionsDAO.load(ctx.getJobDifinitionIds());
        if (CollectionUtils.isEmpty(pos)) {
            failNoIds(ctx.getJobDifinitionIds());
        }

        for (JobDefinitionPO po : pos) {

            if (ctx.isError()) {

                jobDefinitionsDAO.mark(po.getId(), true);
                if (StringUtils.isNotBlank(po.getCronExpression())) {

                    jobSchedulingComponent.unschedule(JobDefinitionSchedulingContext.builder()
                            .jobDefinitionId(po.getId())
                            .build());
                }

                jobExecutionsComponent.stop(JobExecutionStopContext.builder()
                        .jobDefinitionId(po.getId())
                        .build());
            } else {

                jobDefinitionsDAO.mark(po.getId(), false);
                if (StringUtils.isNotBlank(po.getCronExpression())) {

                    jobSchedulingComponent.schedule(JobDefinitionSchedulingContext.builder()
                            .jobDefinitionId(po.getId())
                            .cronExpression(po.getCronExpression())
                            .build());
                }
            }
        }
    }
    /**
     * Removes a job definition.
     * @param ctx the context
     */
    public void remove(JobDefinitionsRemoveContext ctx) {

        List<JobDefinitionPO> pos = jobDefinitionsDAO.load(ctx.getJobDifinitionIds());
        if (CollectionUtils.isEmpty(pos)) {
            failNoIds(ctx.getJobDifinitionIds());
        }

        // 1. Check for running state. Disable removal, if some running instances found
        failIfRunningExist(ctx.getJobDifinitionIds());

        // 2. Remove artifacts
        for (JobDefinitionPO po : pos) {

            // 2.1. Remove cron scheduling
            if (StringUtils.isNotBlank(po.getCronExpression())) {

                jobSchedulingComponent.unschedule(JobDefinitionSchedulingContext.builder()
                        .jobDefinitionId(po.getId())
                        .build());
            }

            // 2.2. Remove internal SB bits
            jobExecutionsComponent.cleanup(JobExecutionCleanupContext.builder()
                    .jobDefinitionId(po.getId())
                    .build());

            // 2.3. Remove definition
            jobDefinitionsDAO.remove(po.getId());
        }

        LOGGER.info("Job definition(s) [{}] removed.", ctx.getJobDifinitionIds());
    }

    public List<String> tags() {
        return jobDefinitionsDAO.loadAllTags();
    }

    private JobExecutionState state(long jobDefinitionId) {

        JobExecutionState state = new JobExecutionState();
        ExecutionStatePO spo = jobExecutionsDAO.findLastFinishedState(jobDefinitionId);
        if (Objects.nonNull(spo)) {

            state
                .withStatus(JobExecutionStatus.fromValue(spo.getStatus()))
                .withExitCode(spo.getExitCode())
                .withExitDescription(spo.getExitMessage());

        }

        return state;
    }

    private String ensureSortField(JobDefinitionsQueryContext ctx) {

        final String field = ctx.getSorting().getKey();
        if (StringUtils.isBlank(field)) {
            return null;
        }

        if (StringUtils.equalsAnyIgnoreCase(field, ALLOWED_SORT_FIELDS)) {
            return field;
        }

        throw new JobException("Sort field [{}] not allowed.",
                CoreExceptionIds.EX_JOB_DEFINITION_SORT_FIELD_NOT_ALLOWED, field);
    }

    private String ensureOrderDirection(JobDefinitionsQueryContext ctx) {

        final String direction = ctx.getSorting().getValue();
        if (StringUtils.isBlank(direction)) {
            return null;
        }

        if (StringUtils.equalsAnyIgnoreCase(direction, ALLOWED_SORT_ORDER)) {
            return direction;
        }

        throw new JobException("Order direction [{}] not allowed.",
                CoreExceptionIds.EX_JOB_DEFINITION_ORDER_DIRECTION_NOT_ALLOWED, direction);
    }

    private void failNoIds(Collection<Long> ids) {
        throw new JobException("Job with id(s) [{}] not found", CoreExceptionIds.EX_JOB_NOT_FOUND, ids);
    }

    private void failIfRunningExist(Collection<Long> ids) {

        Map<Long, Long> running = new HashMap<>();
        for (Long id : ids) {

            JobExecutionStatusResult status = jobExecutionsComponent.status(JobExecutionStatusContext.builder()
                    .jobDefinitionId(id)
                    .progress(false)
                    .build());

            if (Objects.nonNull(status)
             && Objects.nonNull(status.getExecution())
             && status.getExecution().getState().getStatus().isActive()) {
                running.put(id, status.getExecution().getId());
            }
        }

        if (MapUtils.isNotEmpty(running)) {
            throw new JobException("Job definitions [{}] have active executions [{}] and cannot be removed. "
                    + "Please, stop them first or wait until they are complete.",
                    CoreExceptionIds.EX_JOB_REMOVE_FAILED_RUNNING_EXECUTIONS, running.keySet(), running.values());
        }
    }
}
