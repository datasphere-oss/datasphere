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

/**
 * Date: 31.03.2016
 */

package com.huahui.datasphere.mdm.core.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.util.CollectionUtils;

import com.huahui.datasphere.mdm.core.dao.impl.CustomJdbcExecutionContextDao;
import com.huahui.datasphere.mdm.core.dao.impl.CustomJdbcJobExecutionDao;
import com.huahui.datasphere.mdm.core.dao.impl.CustomJdbcStepExecutionDao;
import com.huahui.datasphere.mdm.core.dto.PaginatedResultDTO;
import com.huahui.datasphere.mdm.core.type.job.JobExecutionFilter;
import com.huahui.datasphere.mdm.core.type.job.StepExecutionFilter;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class CustomJobExplorer extends SimpleJobExplorer {
    private CustomJdbcJobExecutionDao customJdbcJobExecutionDao;
    private CustomJdbcStepExecutionDao customJdbcStepExecutionDao;
    private CustomJdbcExecutionContextDao customJdbcExecutionContextDao;

    public CustomJobExplorer(
            JobInstanceDao jobInstanceDao,
            CustomJdbcJobExecutionDao jobExecutionDao,
            CustomJdbcStepExecutionDao stepExecutionDao,
            CustomJdbcExecutionContextDao ecDao
    ) {
        super(jobInstanceDao, jobExecutionDao, stepExecutionDao, ecDao);
        customJdbcJobExecutionDao = jobExecutionDao;
        customJdbcStepExecutionDao = stepExecutionDao;
        customJdbcExecutionContextDao = ecDao;
    }

    /**
     *
     * @param jobInstanceIds
     * @return
     */
    public Map<Long, List<JobExecution>> getJobExecutions(Collection<Long> jobInstanceIds) {
        Map<Long, List<JobExecution>> result = customJdbcJobExecutionDao.findJobExecutions(jobInstanceIds);

        final Map<Long, JobExecution> jobExecutionMap = new HashMap<>();
        result.values().stream()
            .filter(v -> !CollectionUtils.isEmpty(v))
            .flatMap(Collection::stream).forEach(jobExecution -> {
            jobExecutionMap.put(jobExecution.getId(), jobExecution);
        });

        if (CollectionUtils.isEmpty(jobExecutionMap)) {
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

        if (CollectionUtils.isEmpty(stepExecutionMap)) {
            return result;
        }

        // Load all stepExecutionContexts.
        Map<Long, ExecutionContext> stepExecutionContextMap =
            customJdbcExecutionContextDao.loadStepExecutionContexts(stepExecutionMap.keySet());

        stepExecutionMap.values().forEach(stepExecution -> {
            stepExecution.setExecutionContext(stepExecutionContextMap.get(stepExecution.getId()));
        });

        return result;
    }

    /**
     *
     * @param filter
     * @return
     */
    public PaginatedResultDTO<JobExecution> searchJobExecutions(JobExecutionFilter filter) {
        PaginatedResultDTO<JobExecution> result = customJdbcJobExecutionDao.searchJobExecutions(filter);

        final Map<Long, JobExecution> jobExecutionMap = new LinkedHashMap<>();
        result.getPage()
            .forEach(jobExecution -> jobExecutionMap.put(jobExecution.getId(), jobExecution));

        if (CollectionUtils.isEmpty(jobExecutionMap)) {
            return result;
        }

        return result;
    }

    /**
     *
     * @param filter
     * @return
     */
    public PaginatedResultDTO<StepExecution> searchStepExecutions(StepExecutionFilter filter) {
        return customJdbcStepExecutionDao.searchStepExecutions(filter);
    }

    /**
     *
     *
     * @param jobInstanceIds
     * @param loadSteps
     * @return
     */
    public Map<Long, JobExecution> getLastJobExecutions(Collection<Long> jobInstanceIds, boolean loadSteps) {
        Map<Long, JobExecution> result = customJdbcJobExecutionDao.findLastJobExecutions(jobInstanceIds);

        final Map<Long, JobExecution> jobExecutionMap = new LinkedHashMap<>();
        result.values().stream()
            .filter(Objects::nonNull)
            .forEach(jobExecution -> jobExecutionMap.put(jobExecution.getId(), jobExecution));

        if (!loadSteps || CollectionUtils.isEmpty(jobExecutionMap)) {
            return result;
        }

        // Fill executions with steps.
        customJdbcStepExecutionDao.fillStepExecutions(jobExecutionMap.values());

//        final Map<Long, StepExecution> stepExecutionMap = new HashMap<>();
//
//        jobExecutionMap.values().forEach(jobExecution -> {
//            if (!CollectionUtils.isEmpty(jobExecution.getStepExecutions())) {
//                jobExecution.getStepExecutions()
//                    .forEach(stepExecution -> {
//                        stepExecutionMap.put(stepExecution.getId(), stepExecution);
//                    });
//            }
//        });
//
//        if (CollectionUtils.isEmpty(stepExecutionMap)) {
//            return result;
//        }

        return result;
    }

    public Date getLastSuccessJobExecutionsDate(Collection<Long> jobInstanceIds) {
        Map<Long, JobExecution> result = customJdbcJobExecutionDao.findLastJobSuccessExecutions(jobInstanceIds);
        Date previousSuccessStartTime = null;
        Optional<JobExecution> jobExecution = result.values()
                .stream()
                .filter(Objects::nonNull)
                .findFirst();
        if(jobExecution.isPresent()){
            previousSuccessStartTime = jobExecution.get().getStartTime();
        }
        return previousSuccessStartTime;
    }

    public Map<Long, Long> getLastJobExecutionIds(Collection<Long> jobInstanceIds) {
        return customJdbcJobExecutionDao.findLastJobExecutionIds(jobInstanceIds);
    }
}
