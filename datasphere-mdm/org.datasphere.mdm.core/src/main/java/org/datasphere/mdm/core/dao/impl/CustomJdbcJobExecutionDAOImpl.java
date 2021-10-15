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

package org.datasphere.mdm.core.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.datasphere.mdm.core.service.job.JobExecutionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameter.ParameterType;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.repository.dao.JdbcJobExecutionDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class CustomJdbcJobExecutionDAOImpl extends JdbcJobExecutionDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomJdbcJobExecutionDAOImpl.class);

    private static final CustomJobExecutionRowMapper JOB_EXECUTION_ROW_MAPPER = new CustomJobExecutionRowMapper();

    // Custom queries.
    private static final String FIND_JOB_EXECUTIONS_BY_JOB_INSTANCE_IDS =
        "SELECT JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS," +
            " EXIT_CODE, EXIT_MESSAGE, CREATE_TIME, LAST_UPDATED, VERSION, JOB_CONFIGURATION_LOCATION, JOB_INSTANCE_ID" +
            " from %PREFIX%JOB_EXECUTION" +
            " where JOB_INSTANCE_ID in (SELECT UNNEST(?))" +
            " order by JOB_EXECUTION_ID desc";

    private static final String SEARCH_JOB_EXECUTIONS_BY_FILTER =
        "SELECT JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS," +
            " EXIT_CODE, EXIT_MESSAGE, CREATE_TIME, LAST_UPDATED, VERSION, JOB_CONFIGURATION_LOCATION, JOB_INSTANCE_ID" +
            " from %PREFIX%JOB_EXECUTION" +
            " where JOB_INSTANCE_ID in (SELECT UNNEST(?))" +
            " order by CREATE_TIME desc" +
            " limit ? offset ?";

    private static final String GET_JOB_EXECUTIONS_COUNT_BY_FILTER =
        "SELECT count(JOB_EXECUTION_ID)" +
            " from %PREFIX%JOB_EXECUTION" +
            " where JOB_INSTANCE_ID in (SELECT UNNEST(?))";

    private static final String FIND_LAST_JOB_EXECUTION_BY_JOB_INSTANCE_IDS =
        "SELECT DISTINCT ON (JOB_INSTANCE_ID) " +
            "JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS," +
            " EXIT_CODE, EXIT_MESSAGE, CREATE_TIME, LAST_UPDATED, VERSION, JOB_CONFIGURATION_LOCATION, JOB_INSTANCE_ID" +
            " from %PREFIX%JOB_EXECUTION" +
            " where JOB_INSTANCE_ID in (SELECT UNNEST(?))" +
            " order by JOB_INSTANCE_ID, JOB_EXECUTION_ID desc";

    private static final String FIND_LAST_JOB_EXECUTION_BY_JOB_INSTANCE_IDS_AND_STATUS =
        "SELECT DISTINCT ON (JOB_INSTANCE_ID) " +
                "JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS," +
                " EXIT_CODE, EXIT_MESSAGE, CREATE_TIME, LAST_UPDATED, VERSION, JOB_CONFIGURATION_LOCATION, JOB_INSTANCE_ID" +
                " from %PREFIX%JOB_EXECUTION" +
                " where JOB_INSTANCE_ID in (SELECT UNNEST(?)) AND STATUS = ?" +
                " order by JOB_INSTANCE_ID desc, JOB_EXECUTION_ID desc";

    private static final String FIND_LAST_JOB_EXECUTION_ID_BY_JOB_INSTANCE_IDS =
        "SELECT DISTINCT ON (JOB_INSTANCE_ID) " +
            "JOB_EXECUTION_ID, JOB_INSTANCE_ID" +
            " from %PREFIX%JOB_EXECUTION" +
            " where JOB_INSTANCE_ID in (SELECT UNNEST(?))" +
            " order by JOB_INSTANCE_ID, JOB_EXECUTION_ID desc";

    private static final String FIND_JOB_EXECUTIONS_PARAMS_BY_JOB_INSTANCE_IDS =
        "SELECT jep.JOB_EXECUTION_ID, KEY_NAME, TYPE_CD, STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL, IDENTIFYING, " +
                "string_val_arr, date_val_arr, long_val_arr, double_val_arr " +
            "from %PREFIX%JOB_EXECUTION_PARAMS jep" +
            "  inner join %PREFIX%JOB_EXECUTION je on je.JOB_EXECUTION_ID = jep.JOB_EXECUTION_ID " +
            "where je.job_instance_id in (SELECT UNNEST(?))";

    private static final String FIND_LAST_JOB_EXECUTION_PARAMS_BY_JOB_INSTANCE_IDS =
        "SELECT jep.JOB_EXECUTION_ID, KEY_NAME, TYPE_CD, STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL, IDENTIFYING, " +
                "string_val_arr, date_val_arr, long_val_arr, double_val_arr " +
            "from %PREFIX%JOB_EXECUTION_PARAMS jep " +
            "where jep.JOB_EXECUTION_ID in (" +
            "  SELECT DISTINCT ON (JOB_INSTANCE_ID) JOB_EXECUTION_ID " +
            "  from %PREFIX%JOB_EXECUTION " +
            "  where JOB_INSTANCE_ID in (SELECT UNNEST(?)) " +
            "  order by JOB_INSTANCE_ID, JOB_EXECUTION_ID desc )";

    private static final String CLEANUP_BY_JOB_INSTANCE_IDS = "DELETE FROM %PREFIX%JOB_INSTANCE WHERE JOB_INSTANCE_ID IN (SELECT UNNEST(?))";

    private static final String FIELD_JOB_INSTANCE_ID = "JOB_INSTANCE_ID";

    private static final String FIELD_JOB_EXECUTION_ID = "JOB_EXECUTION_ID";
    /**
     * Finds all job executions for each given instance id
     * @param jobInstanceIds instance ids
     * @return map
     */
    public Map<Long, List<JobExecution>> findJobExecutions(Collection<Long> jobInstanceIds) {

        if (CollectionUtils.isEmpty(jobInstanceIds)) {
            return Collections.emptyMap();
        }

        final Map<Long, JobParameters> jobParameters = loadJobExecutionParameters(jobInstanceIds);
        final Map<Long, List<JobExecution>> result = new HashMap<>();

        // Couldn't be actually. Just want to make SONAR happy.
        DataSource dataSource = ((JdbcTemplate) getJdbcTemplate()).getDataSource();
        if (Objects.isNull(dataSource)) {
            return Collections.emptyMap();
        }

        try (Connection connection = dataSource.getConnection()) {

            Array jea = connection.createArrayOf("int8", jobInstanceIds.toArray(Long[]::new));
            getJdbcTemplate().query(getQuery(FIND_JOB_EXECUTIONS_BY_JOB_INSTANCE_IDS),
                rs -> {
                    long jobInstanceId = rs.getLong(FIELD_JOB_INSTANCE_ID);
                    result.computeIfAbsent(jobInstanceId, k -> new ArrayList<>()).add(JOB_EXECUTION_ROW_MAPPER.mapRow(jobParameters, rs));
                },
                jea);

        } catch (SQLException e) {
            LOGGER.warn("Job executions lookup failed.", e);
        }

        return result;
    }
    /**
     * Searches job executions by given criteria.
     *
     * @param filter the filter criteria
     * @return pair
     */
    public Pair<Map<Long, List<JobExecution>>, Integer> searchJobExecutions(JobExecutionFilter filter) {

        if (filter == null || CollectionUtils.isEmpty(filter.getJobInstanceIds())) {
            return null;
        }

        final Map<Long, JobParameters> jobParameters = loadJobExecutionParameters(filter.getJobInstanceIds());
        final Map<Long, List<JobExecution>> jobExecutions = new HashMap<>();

        // Couldn't be actually. Just want to make SONAR happy.
        DataSource dataSource = ((JdbcTemplate) getJdbcTemplate()).getDataSource();
        if (Objects.isNull(dataSource)) {
            return null;
        }

        final RowCallbackHandler h = rs -> jobExecutions
                .computeIfAbsent(rs.getLong(FIELD_JOB_INSTANCE_ID), k -> new ArrayList<>())
                .add(JOB_EXECUTION_ROW_MAPPER.mapRow(jobParameters, rs));

        try (Connection connection = dataSource.getConnection()) {

            Array jea = connection.createArrayOf("int8", filter.getJobInstanceIds().toArray(Long[]::new));
            getJdbcTemplate().query(getQuery(SEARCH_JOB_EXECUTIONS_BY_FILTER),
                h,
                jea,
                filter.getItemsCount(),
                filter.getFromIndex());

            return Pair.of(
                    jobExecutions,
                    getJdbcTemplate().queryForObject(getQuery(GET_JOB_EXECUTIONS_COUNT_BY_FILTER), Integer.class, jea));

        } catch (SQLException e) {
            LOGGER.warn("Job execution search failed.", e);
        }

        return null;
    }
    /**
     * Finds last execuion for each given instance id.
     *
     * @param jobInstanceIds instance ids
     * @return map
     */
    public Map<Long, JobExecution> findLastJobExecutions(Collection<Long> jobInstanceIds) {

        if (CollectionUtils.isEmpty(jobInstanceIds)) {
            return Collections.emptyMap();
        }

        // Couldn't be actually. Just want to make SONAR happy.
        DataSource dataSource = ((JdbcTemplate) getJdbcTemplate()).getDataSource();
        if (Objects.isNull(dataSource)) {
            return Collections.emptyMap();
        }

        final Map<Long, JobParameters> jobParameters = loadLastJobExecutionParameters(jobInstanceIds);
        final Map<Long, JobExecution> result = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {

            Array jea = connection.createArrayOf("int8", jobInstanceIds.toArray(Long[]::new));
            getJdbcTemplate().query(getQuery(FIND_LAST_JOB_EXECUTION_BY_JOB_INSTANCE_IDS),
                rs -> {
                    long jobInstanceId = rs.getLong(FIELD_JOB_INSTANCE_ID);
                    result.put(jobInstanceId, JOB_EXECUTION_ROW_MAPPER.mapRow(jobParameters, rs));
                },
                jea);

        } catch (SQLException e) {
            LOGGER.warn("Last execution lookup failed.", e);
        }

        return result;
    }
    /**
     * Finds last SUCCESSFUL execuion for each given instance id.
     *
     * @param jobInstanceIds instance ids
     * @return map
     */
    public Map<Long, JobExecution> findLastJobSuccessExecutions(Collection<Long> jobInstanceIds) {

        if (CollectionUtils.isEmpty(jobInstanceIds)) {
            return Collections.emptyMap();
        }

        // Couldn't be actually. Just want to make SONAR happy.
        DataSource dataSource = ((JdbcTemplate) getJdbcTemplate()).getDataSource();
        if (Objects.isNull(dataSource)) {
            return Collections.emptyMap();
        }

        final Map<Long, JobParameters> jobParameters = loadLastJobExecutionParameters(jobInstanceIds);
        final Map<Long, JobExecution> result = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {

            Array jea = connection.createArrayOf("int8", jobInstanceIds.toArray(Long[]::new));
            getJdbcTemplate().query(getQuery(FIND_LAST_JOB_EXECUTION_BY_JOB_INSTANCE_IDS_AND_STATUS),
                rs -> {
                    long jobInstanceId = rs.getLong(FIELD_JOB_INSTANCE_ID);
                    result.put(jobInstanceId, JOB_EXECUTION_ROW_MAPPER.mapRow(jobParameters, rs));
                },
                jea,
                BatchStatus.COMPLETED.toString());

        } catch (SQLException e) {
            LOGGER.warn("Last successful execution lookup failed.", e);
        }

        return result;
    }
    /**
     * Finds the very last execution id.
     *
     * @param jobInstanceIds the instance ids to look executions for.
     * @return map of found executions, keyed by instance ids
     */
    public Map<Long, Long> findLastJobExecutionIds(Collection<Long> jobInstanceIds) {

        if (CollectionUtils.isEmpty(jobInstanceIds)) {
            return Collections.emptyMap();
        }

        // Couldn't be actually. Just want to make SONAR happy.
        DataSource dataSource = ((JdbcTemplate) getJdbcTemplate()).getDataSource();
        if (Objects.isNull(dataSource)) {
            return Collections.emptyMap();
        }

        final Map<Long, Long> result = new HashMap<>();
        final RowCallbackHandler h = rs -> result.put(rs.getLong(FIELD_JOB_INSTANCE_ID), rs.getLong(FIELD_JOB_EXECUTION_ID));
        try (Connection connection = dataSource.getConnection()) {

            Array jea = connection.createArrayOf("int8", jobInstanceIds.toArray(Long[]::new));
            getJdbcTemplate().query(getQuery(FIND_LAST_JOB_EXECUTION_ID_BY_JOB_INSTANCE_IDS), h, jea);

        } catch (SQLException e) {
            LOGGER.warn("Last execution id lookup failed.", e);
        }

        return result;
    }
    /**
     * Removes given instance ids.
     * All dependent objects should be removed cascaded by constraints.
     *
     * @param jobInstanceIds the instance ids
     */
    public void cleanupJobExecutions(Collection<Long> jobInstanceIds) {

        if (CollectionUtils.isEmpty(jobInstanceIds)) {
            return;
        }

        DataSource dataSource = ((JdbcTemplate) getJdbcTemplate()).getDataSource();
        if (Objects.isNull(dataSource)) {
            return;
        }

        try (Connection connection = dataSource.getConnection()) {

            Array jea = connection.createArrayOf("int8", jobInstanceIds.toArray(Long[]::new));
            getJdbcTemplate().update(getQuery(CLEANUP_BY_JOB_INSTANCE_IDS), jea);

        } catch (SQLException e) {
            LOGGER.warn("Cleanup failed.", e);
        }
    }
    /**
     * Loads execution parameters.
     *
     * @param jobInstanceIds instance ids
     * @return map
     */
    private Map<Long, JobParameters> loadJobExecutionParameters(Collection<Long> jobInstanceIds) {

        // Couldn't be actually. Just want to make SONAR happy.
        DataSource dataSource = ((JdbcTemplate) getJdbcTemplate()).getDataSource();
        if (Objects.isNull(dataSource)) {
            return Collections.emptyMap();
        }

        Map<Long, Map<String, JobParameter>> jobExecutionParamsMap = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {

            Array jia = connection.createArrayOf("int8", jobInstanceIds.toArray(Long[]::new));
            getJdbcTemplate().query(getQuery(FIND_JOB_EXECUTIONS_PARAMS_BY_JOB_INSTANCE_IDS),
                rs -> {

                    ParameterType type = ParameterType.valueOf(rs.getString(3));
                    JobParameter value = readJobParameter(type, rs);

                    long jobExecutionId = rs.getLong(FIELD_JOB_EXECUTION_ID);
                    jobExecutionParamsMap.computeIfAbsent(jobExecutionId, k -> new HashMap<>()).put(rs.getString(2), value);
                },
                jia);

        } catch (SQLException e) {
            LOGGER.warn("Exception caught.", e);
        }

        Map<Long, JobParameters> result = new HashMap<>();
        jobExecutionParamsMap.forEach((id, map) -> result.put(id, new JobParameters(map)));

        return result;
    }

    /**
     * Load parameters only for last execution for every jobInstanceId.
     *
     * @param jobInstanceIds instance ids
     * @return map
     */
    private Map<Long, JobParameters> loadLastJobExecutionParameters(Collection<Long> jobInstanceIds) {

        // Couldn't be actually. Just want to make SONAR happy.
        DataSource dataSource = ((JdbcTemplate) getJdbcTemplate()).getDataSource();
        if (Objects.isNull(dataSource)) {
            return Collections.emptyMap();
        }

        Map<Long, Map<String, JobParameter>> jobExecutionParamsMap = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {

            Array jia = connection.createArrayOf("int8", jobInstanceIds.toArray(Long[]::new));
            getJdbcTemplate().query(getQuery(FIND_LAST_JOB_EXECUTION_PARAMS_BY_JOB_INSTANCE_IDS),
                rs -> {

                    ParameterType type = ParameterType.valueOf(rs.getString(3));
                    JobParameter value = readJobParameter(type, rs);

                    long jobExecutionId = rs.getLong(FIELD_JOB_EXECUTION_ID);
                    jobExecutionParamsMap.computeIfAbsent(jobExecutionId, k -> new HashMap<>()).put(rs.getString(2), value);
                },
                jia);

        } catch (SQLException e) {
            LOGGER.warn("Exception caught.", e);
        }

        Map<Long, JobParameters> result = new HashMap<>();
        jobExecutionParamsMap.forEach((id, map) -> result.put(id, new JobParameters(map)));

        return result;
    }
    /**
     * Reads a single parameter.
     *
     * @param type parameter type
     * @param rs result set
     * @return parameter
     * @throws SQLException
     */
    private JobParameter readJobParameter(ParameterType type, ResultSet rs) throws SQLException {

        JobParameter value = null;
        if (type == ParameterType.STRING) {
            value = new JobParameter(rs.getString(4), rs.getString(8).equalsIgnoreCase("Y"));
        } else if (type == ParameterType.LONG) {
            value = new JobParameter(rs.getLong(6), rs.getString(8).equalsIgnoreCase("Y"));
        } else if (type == ParameterType.DOUBLE) {
            value = new JobParameter(rs.getDouble(7), rs.getString(8).equalsIgnoreCase("Y"));
        } else if (type == ParameterType.DATE) {
            value = new JobParameter(rs.getTimestamp(5), rs.getString(8).equalsIgnoreCase("Y"));
        }

        return value;
    }
    /**
   	 * Re-usable mapper for {@link JobExecution} instances.
   	 *
   	 * @author Dave Syer
   	 */
    private static final class CustomJobExecutionRowMapper {

        public JobExecution mapRow(Map<Long, JobParameters> jobExecutionParameters, ResultSet rs) throws SQLException {

            Long id = rs.getLong(1);
            String jobConfigurationLocation = rs.getString(10);

            JobExecution jobExecution = new JobExecution(id, jobExecutionParameters.get(id), jobConfigurationLocation);

            jobExecution.setStartTime(rs.getTimestamp(2));
            jobExecution.setEndTime(rs.getTimestamp(3));
            jobExecution.setStatus(BatchStatus.valueOf(rs.getString(4)));
            jobExecution.setExitStatus(new ExitStatus(rs.getString(5), rs.getString(6)));
            jobExecution.setCreateTime(rs.getTimestamp(7));
            jobExecution.setLastUpdated(rs.getTimestamp(8));
            jobExecution.setVersion(rs.getInt(9));

            return jobExecution;
        }
    }
}
