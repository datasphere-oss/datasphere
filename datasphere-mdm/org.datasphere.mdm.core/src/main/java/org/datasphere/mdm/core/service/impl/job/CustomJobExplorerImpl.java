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

/**
 * Date: 31.03.2016
 */

package org.datasphere.mdm.core.service.impl.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.datasphere.mdm.core.dao.impl.CustomJdbcExecutionContextDAOImpl;
import org.datasphere.mdm.core.dao.impl.CustomJdbcJobExecutionDAOImpl;
import org.datasphere.mdm.core.dao.impl.CustomJdbcStepExecutionDAOImpl;
import org.datasphere.mdm.core.dto.job.JobPaginatedResult;
import org.datasphere.mdm.core.service.job.CustomJobExplorer;
import org.datasphere.mdm.core.service.job.JobExecutionFilter;
import org.datasphere.mdm.core.service.job.StepExecutionFilter;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.item.ExecutionContext;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class CustomJobExplorerImpl extends SimpleJobExplorer implements CustomJobExplorer {

    private CustomJdbcJobExecutionDAOImpl customJdbcJobExecutionDao;
    private CustomJdbcStepExecutionDAOImpl customJdbcStepExecutionDao;
    private CustomJdbcExecutionContextDAOImpl customJdbcExecutionContextDao;

    public CustomJobExplorerImpl(
            JobInstanceDao jobInstanceDao,
            CustomJdbcJobExecutionDAOImpl jobExecutionDao,
            CustomJdbcStepExecutionDAOImpl stepExecutionDao,
            CustomJdbcExecutionContextDAOImpl ecDao) {
        super(jobInstanceDao, jobExecutionDao, stepExecutionDao, ecDao);
        customJdbcJobExecutionDao = jobExecutionDao;
        customJdbcStepExecutionDao = stepExecutionDao;
        customJdbcExecutionContextDao = ecDao;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanupJobExecutions(Collection<Long> jobInstanceIds) {
        // Stuff removed by cascaded constraints
        customJdbcJobExecutionDao.cleanupJobExecutions(jobInstanceIds);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Long, List<JobExecution>> getJobExecutions(Collection<Long> jobInstanceIds) {

        Map<Long, List<JobExecution>> result = customJdbcJobExecutionDao.findJobExecutions(jobInstanceIds);

        final Map<Long, JobExecution> jobExecutionMap = new HashMap<>();
        result.forEach((jobInstanceId, jobExecutions) -> {

            if (CollectionUtils.isEmpty(jobExecutions)) {
                return;
            }

            jobExecutions.forEach(je -> {
                je.setJobInstance(getJobInstance(jobInstanceId));
                jobExecutionMap.put(je.getId(), je);
            });
        });

        if (MapUtils.isEmpty(jobExecutionMap)) {
            return result;
        }

        // Fill executions with steps.
        customJdbcStepExecutionDao.fillStepExecutions(jobExecutionMap.values());

        // Load all jobExecutionContexts.
        Map<Long, ExecutionContext> jobExecutionContextMap =
            customJdbcExecutionContextDao.loadJobExecutionContexts(jobExecutionMap.keySet());

        final Map<Long, StepExecution> stepExecutionMap = new HashMap<>();

        jobExecutionMap.values().forEach(jobExecution -> {

            jobExecution.setExecutionContext(jobExecutionContextMap.get(jobExecution.getId()));
            if (!CollectionUtils.isEmpty(jobExecution.getStepExecutions())) {
                jobExecution.getStepExecutions()
                    .forEach(stepExecution -> stepExecutionMap.put(stepExecution.getId(), stepExecution));
            }
        });

        if (MapUtils.isEmpty(stepExecutionMap)) {
            return result;
        }

        // Load all stepExecutionContexts.
        Map<Long, ExecutionContext> stepExecutionContextMap =
            customJdbcExecutionContextDao.loadStepExecutionContexts(stepExecutionMap.keySet());

        stepExecutionMap.values().forEach(stepExecution -> stepExecution.setExecutionContext(stepExecutionContextMap.get(stepExecution.getId())));

        return result;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public JobPaginatedResult<JobExecution> searchJobExecutions(JobExecutionFilter filter) {

        Pair<Map<Long, List<JobExecution>>, Integer> result = customJdbcJobExecutionDao.searchJobExecutions(filter);

        // JobPaginatedResult<JobExecution>
        return new JobPaginatedResult<>(
                result.getLeft().entrySet().stream()
                    .filter(entry -> CollectionUtils.isNotEmpty(entry.getValue()))
                    .map(entry -> {

                        JobInstance ji = getJobInstance(entry.getKey());
                        entry.getValue().forEach(je -> je.setJobInstance(ji));

                        return entry.getValue();
                    })
                    .flatMap(Collection::stream)
                    .sorted((je1, je2) -> Objects.nonNull(je1.getStartTime()) && Objects.nonNull(je2.getStartTime())
                            ? je2.getStartTime().compareTo(je1.getStartTime())
                            : (int) (je2.getId() - je1.getId()))
                    .collect(Collectors.toList()),
                result.getRight());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public JobPaginatedResult<StepExecution> searchStepExecutions(StepExecutionFilter filter) {
        return customJdbcStepExecutionDao.searchStepExecutions(filter);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Long, JobExecution> getLastJobExecutions(Collection<Long> jobInstanceIds, boolean loadSteps) {

        Map<Long, JobExecution> result = customJdbcJobExecutionDao.findLastJobExecutions(jobInstanceIds);

        final List<JobExecution> executions = new ArrayList<>();
        result.forEach((jobInstanceId, jobExecution) -> {

            if (Objects.isNull(jobExecution)) {
                return;
            }

            jobExecution.setJobInstance(getJobInstance(jobInstanceId));
            executions.add(jobExecution);
        });

        if (!loadSteps || CollectionUtils.isEmpty(executions)) {
            return result;
        }

        // Fill executions with steps.
        customJdbcStepExecutionDao.fillStepExecutions(executions);

        return result;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastSuccessJobExecutionsDate(Collection<Long> jobInstanceIds) {

        Map<Long, JobExecution> result = customJdbcJobExecutionDao.findLastJobSuccessExecutions(jobInstanceIds);
        Date previousSuccessStartTime = null;
        Optional<JobExecution> jobExecution = result.values()
                .stream()
                .filter(Objects::nonNull)
                .findFirst();

        if (jobExecution.isPresent()){
            previousSuccessStartTime = jobExecution.get().getStartTime();
        }

        return previousSuccessStartTime;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Long, Long> getLastJobExecutionIds(Collection<Long> jobInstanceIds) {
        return customJdbcJobExecutionDao.findLastJobExecutionIds(jobInstanceIds);
    }
}
