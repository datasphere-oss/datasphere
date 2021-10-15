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

package com.huahui.datasphere.mdm.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.JdbcStepExecutionDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import com.huahui.datasphere.mdm.core.dao.CustomJobDaoSupport;
import com.huahui.datasphere.mdm.core.dto.PaginatedResultDTO;
import com.huahui.datasphere.mdm.core.dto.job.StepExecutionPaginatedResultDTO;
import com.huahui.datasphere.mdm.core.type.job.StepExecutionFilter;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class CustomJdbcStepExecutionDao extends JdbcStepExecutionDao {
    private static final String FIND_STEP_EXECUTIONS_BY_JOB_EXECUTION_IDS =
            "SELECT STEP_EXECUTION_ID, STEP_NAME, START_TIME, END_TIME, STATUS, COMMIT_COUNT," +
                    " READ_COUNT, FILTER_COUNT, WRITE_COUNT, EXIT_CODE, EXIT_MESSAGE, READ_SKIP_COUNT, WRITE_SKIP_COUNT, " +
                    "PROCESS_SKIP_COUNT, ROLLBACK_COUNT, LAST_UPDATED, VERSION, JOB_EXECUTION_ID " +
                    "from %PREFIX%STEP_EXECUTION " +
                    "where JOB_EXECUTION_ID IN (select tmp.id from t_tmp_id tmp where tmp.list_id = ?)";

    private static final String SEARCH_STEP_EXECUTIONS_BY_FILTER =
            "SELECT STEP_EXECUTION_ID, STEP_NAME, START_TIME, END_TIME, STATUS, COMMIT_COUNT," +
                    " READ_COUNT, FILTER_COUNT, WRITE_COUNT, EXIT_CODE, EXIT_MESSAGE, READ_SKIP_COUNT, WRITE_SKIP_COUNT, " +
                    "PROCESS_SKIP_COUNT, ROLLBACK_COUNT, LAST_UPDATED, VERSION, JOB_EXECUTION_ID " +
                    "from %PREFIX%STEP_EXECUTION " +
                    "where JOB_EXECUTION_ID = ?" +
                    " order by STEP_EXECUTION_ID desc" +
                    " limit ? offset ?";

    private static final String GET_STEP_EXECUTIONS_COUNT_BY_STATUS =
            "select count(status) as cnt, status" +
                " from %PREFIX%step_execution" +
                " where JOB_EXECUTION_ID = ?" +
                " group by status";

    private CustomJobDaoSupport daoHelper;

    private static final RowMapper<Pair<Integer, BatchStatus>> COUNT_MAPPER = (rs, pos) -> {
        return new ImmutablePair<>(rs.getInt("cnt"), BatchStatus.valueOf(rs.getString("status")));
    };

    public void setDaoHelper(CustomJobDaoSupport daoHelper) {
        this.daoHelper = daoHelper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Assert.notNull(daoHelper, "The daoHelper must not be null.");
    }

    public void fillStepExecutions(Collection<JobExecution> jobExecutions) {
        Assert.notNull(jobExecutions, "Job executions cannot be null.");

        Map<Long, JobExecution> jobExecutionMap = new LinkedHashMap<>();

        jobExecutions.forEach(jobExecution -> {
            jobExecutionMap.put(jobExecution.getId(), jobExecution);
        });


        long listId = daoHelper.insertLongsToTemp(jobExecutionMap.keySet());

        getJdbcTemplate().query(
                getQuery(FIND_STEP_EXECUTIONS_BY_JOB_EXECUTION_IDS),
                rs -> {
                    long jobExecutionId = rs.getLong("JOB_EXECUTION_ID");

                    JobExecution jobExecution = jobExecutionMap.get(jobExecutionId);

                    Assert.notNull(jobExecution);

                    new StepExecutionRowMapper(jobExecution).mapRow(rs, 0);
                },
                listId
        );
    }

    /**
     *
     * @param filter
     * @return
     */
    public PaginatedResultDTO<StepExecution> searchStepExecutions(StepExecutionFilter filter) {
        final List<StepExecution> stepExecutions = new ArrayList<>();

        getJdbcTemplate().query(getQuery(SEARCH_STEP_EXECUTIONS_BY_FILTER),
                rs -> {
                    while (rs.next()) {
                        JobExecution jobExecution = new JobExecution(rs.getLong("JOB_EXECUTION_ID"));

                        StepExecution stepExecution = new StepExecutionRowMapper(jobExecution).mapRow(rs, 0);

                        stepExecutions.add(stepExecution);
                    }

                    return null;
                },
                filter.getJobExecutionId(), filter.getItemCount(), filter.getFromInd());

        StepExecutionPaginatedResultDTO<StepExecution> paginatedResult = new StepExecutionPaginatedResultDTO<>();
        paginatedResult.setPage(stepExecutions);

        int completed = 0;
        int total = 0;

        List<Pair<Integer, BatchStatus>> states = getJdbcTemplate().query(
                getQuery(GET_STEP_EXECUTIONS_COUNT_BY_STATUS),
                COUNT_MAPPER,
                filter.getJobExecutionId());

        for (Pair<Integer, BatchStatus> state : states) {

            total += state.getLeft();
            if (!state.getRight().isRunning()
             && state.getRight() != BatchStatus.UNKNOWN) {
                completed += state.getLeft();
            }
        }

        paginatedResult.setTotalCount(total);
        paginatedResult.setFinishedCount(completed);

        return paginatedResult;

    }

    private static class StepExecutionRowMapper implements RowMapper<StepExecution> {

        private final JobExecution jobExecution;

        StepExecutionRowMapper(JobExecution jobExecution) {
            this.jobExecution = jobExecution;
        }

        @Override
        public StepExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
            StepExecution stepExecution = new StepExecution(rs.getString(2), jobExecution, rs.getLong(1));
            stepExecution.setStartTime(rs.getTimestamp(3));
            stepExecution.setEndTime(rs.getTimestamp(4));
            stepExecution.setStatus(BatchStatus.valueOf(rs.getString(5)));
            stepExecution.setCommitCount(rs.getInt(6));
            stepExecution.setReadCount(rs.getInt(7));
            stepExecution.setFilterCount(rs.getInt(8));
            stepExecution.setWriteCount(rs.getInt(9));
            stepExecution.setExitStatus(new ExitStatus(rs.getString(10), rs.getString(11)));
            stepExecution.setReadSkipCount(rs.getInt(12));
            stepExecution.setWriteSkipCount(rs.getInt(13));
            stepExecution.setProcessSkipCount(rs.getInt(14));
            stepExecution.setRollbackCount(rs.getInt(15));
            stepExecution.setLastUpdated(rs.getTimestamp(16));
            stepExecution.setVersion(rs.getInt(17));
            return stepExecution;
        }
    }
}
