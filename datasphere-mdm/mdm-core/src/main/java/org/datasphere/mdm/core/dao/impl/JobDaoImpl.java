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

package com.huahui.datasphere.mdm.core.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.huahui.datasphere.mdm.system.dao.impl.BaseDAOImpl;
import com.huahui.datasphere.mdm.system.exception.PlatformFailureException;

import com.huahui.datasphere.mdm.core.dao.JobDao;
import com.huahui.datasphere.mdm.core.dao.rm.JobParameterRowMapper;
import com.huahui.datasphere.mdm.core.dao.rm.JobRowMapper;
import com.huahui.datasphere.mdm.core.dao.rm.JobTriggerRowMapper;
import com.huahui.datasphere.mdm.core.exception.CoreExceptionIds;
import com.huahui.datasphere.mdm.core.po.AbstractObjectPO;
import com.huahui.datasphere.mdm.core.po.job.JobBatchJobInstancePO;
import com.huahui.datasphere.mdm.core.po.job.JobPO;
import com.huahui.datasphere.mdm.core.po.job.JobParameterPO;
import com.huahui.datasphere.mdm.core.po.job.JobTriggerPO;
import com.huahui.datasphere.mdm.core.type.job.JobFilter;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;

/**
 * {@inheritDoc}
 * JobDao implementation
 *
 * @author Denis Kostovarov
 */
@Repository
public class JobDaoImpl extends BaseDAOImpl implements JobDao {

    private final String selectAllJobsSQL;
    private final String selectAllJobsAndParamsSQL;
    private final String selectJobsCountByFilterSQL;
    private final String searchJobsByFilterAscSQL;
    private final String searchJobsByFilterDescSQL;
    private final String selectByJobNameSQL;
    private final String selectByJobIdSQL;
    private final String selectByJobIdWithParametersSQL;
    private final String selectJobParametersByJobIdSQL;
    private final String selectJobParametersByJobIdsSQL;
    private final String updateJobByIdSQL;
    private final String updateJobEnabledByIdSQL;
    private final String updateJobErrorByIdsSQL;
    private final String selectJobInstancesByJobIdsSQL;
    private final String selectJobLastExecutionSQL;
    private final String selectJobCheckParamsSQL;
    private final String selectJobsByRefNameSQL;
    private final String deleteJobParamsSQL;
    private final String deleteRemovedJobParamsSQL;
    private final String deleteBatchJobInstanceSQL;
    private final String deleteJobSQL;
    private final String updateJobTriggerByIdSQL;
    private final String selectJobTriggersByIdSQL;
    private final String selectJobJobsByIdAndTriggerRuleSQL;
    private final String selectTriggerByJobIdTriggerIdSQL;
    private final String deleteJobTriggerSQL;
    private final String selectTriggerByTriggerNameSQL;
    private final String deleteJobTriggersSQL;
    private final String selectAllTagsSQL;
    private final String updateJobTagsSQL;
    private final String upsertJobParameterSQL;
    private final String selectJobsByIdsWithParamsSQL;
    private final String selectAllJobsWithParamsSQL;

    private SimpleJdbcInsert jobTemplateInsertActor;
    private SimpleJdbcInsert jobBatchJobInstanceInsertActor;
    private SimpleJdbcInsert jobTriggerTemplateInsertActor;

    @Autowired
    public JobDaoImpl(
            @Qualifier("coreDataSource") final DataSource dataSource,
            @Qualifier("job-sql") final Properties sql) {

        super(dataSource);

        selectAllJobsSQL = sql.getProperty("selectAllJobsSQL");
        selectAllJobsAndParamsSQL = sql.getProperty("selectAllJobsAndParamsSQL");
        selectJobsCountByFilterSQL = sql.getProperty("selectJobsCountByFilterSQL");
        searchJobsByFilterAscSQL = sql.getProperty("searchJobsByFilterAscSQL");
        searchJobsByFilterDescSQL = sql.getProperty("searchJobsByFilterDescSQL");
        selectByJobNameSQL = sql.getProperty("selectByJobNameSQL");
        selectByJobIdSQL = sql.getProperty("selectByJobIdSQL");
        selectByJobIdWithParametersSQL = sql.getProperty("selectByJobIdWithParametersSQL");
        selectJobParametersByJobIdSQL = sql.getProperty("selectJobParametersByJobIdSQL");
        selectJobParametersByJobIdsSQL = sql.getProperty("selectJobParametersByJobIdsSQL");
        updateJobByIdSQL = sql.getProperty("updateJobByIdSQL");
        updateJobEnabledByIdSQL = sql.getProperty("updateJobEnabledByIdSQL");
        updateJobErrorByIdsSQL = sql.getProperty("updateJobErrorByIdsSQL");
        selectJobInstancesByJobIdsSQL = sql.getProperty("selectJobInstancesByJobIdsSQL");
        selectJobLastExecutionSQL = sql.getProperty("selectJobLastExecutionSQL");
        selectJobCheckParamsSQL = sql.getProperty("selectJobCheckParamsSQL");
        selectJobsByRefNameSQL = sql.getProperty("selectJobsByRefNameSQL");
        deleteJobParamsSQL = sql.getProperty("deleteJobParamsSQL");
        deleteRemovedJobParamsSQL = sql.getProperty("deleteRemovedJobParamsSQL");
        deleteBatchJobInstanceSQL = sql.getProperty("deleteBatchJobInstanceSQL");
        deleteJobSQL = sql.getProperty("deleteJobSQL");
        updateJobTriggerByIdSQL = sql.getProperty("updateJobTriggerByIdSQL");
        selectJobTriggersByIdSQL = sql.getProperty("selectJobTriggersByIdSQL");
        selectJobJobsByIdAndTriggerRuleSQL = sql.getProperty("selectJobJobsByIdAndTriggerRuleSQL");
        selectTriggerByJobIdTriggerIdSQL = sql.getProperty("selectTriggerByJobIdTriggerIdSQL");
        deleteJobTriggerSQL = sql.getProperty("deleteJobTriggerSQL");
        selectTriggerByTriggerNameSQL = sql.getProperty("selectTriggerByTriggerNameSQL");
        deleteJobTriggersSQL = sql.getProperty("deleteJobTriggersSQL");
        selectAllTagsSQL = sql.getProperty("selectAllTagsSQL");
        updateJobTagsSQL = sql.getProperty("updateJobTagsSQL");
        upsertJobParameterSQL = sql.getProperty("upsertJobParameterSQL");
        selectJobsByIdsWithParamsSQL = sql.getProperty("selectJobsByIdsWithParamsSQL");
        selectAllJobsWithParamsSQL = sql.getProperty("selectAllJobsWithParamsSQL");

        jobTemplateInsertActor = new SimpleJdbcInsert(dataSource)
                .withTableName(JobPO.TABLE_NAME)
                .usingColumns(AbstractObjectPO.FIELD_CREATE_DATE, AbstractObjectPO.FIELD_CREATED_BY, JobPO.FIELD_NAME,
                        JobPO.FIELD_ENABLED, JobPO.FIELD_CRON_EXPRESSION, JobPO.FIELD_JOB_NAME_REFERENCE,
                        JobPO.FIELD_DESCRIPTION, JobPO.FIELD_TAGS)
                .usingGeneratedKeyColumns(JobPO.FIELD_ID);

        jobBatchJobInstanceInsertActor = new SimpleJdbcInsert(dataSource)
                .withTableName(JobBatchJobInstancePO.TABLE_NAME)
                .usingColumns(AbstractObjectPO.FIELD_CREATE_DATE, AbstractObjectPO.FIELD_CREATED_BY,
                        JobBatchJobInstancePO.FIELD_JOB_ID, JobBatchJobInstancePO.FIELD_JOB_INSTANCE_ID);

        jobTriggerTemplateInsertActor = new SimpleJdbcInsert(dataSource)
                .withTableName(JobTriggerPO.TABLE_NAME)
                .usingColumns(AbstractObjectPO.FIELD_CREATE_DATE, AbstractObjectPO.FIELD_CREATED_BY,
                        JobTriggerPO.FIELD_FINISH_JOB_ID, JobTriggerPO.FIELD_START_JOB_ID,
                        JobTriggerPO.FIELD_SUCCESS_RULE, JobTriggerPO.FIELD_NAME, JobTriggerPO.FIELD_DESCRIPTION)
                .usingGeneratedKeyColumns(JobTriggerPO.FIELD_ID);;
    }

    @Override
    public List<JobPO> getJobs() {
        return getJdbcTemplate().query(selectAllJobsSQL, new JobRowMapper());
    }

    @Override
    public int getJobsCount(JobFilter filter) {
        Map<String, Object> params = new HashMap<>();

        if (filter.getEnabled() == null) {
            params.put("activeFilterEnabled", 0);
            params.put("enabled", filter.getEnabled());
        } else {
            params.put("activeFilterEnabled", 1);
            params.put("enabled", filter.getEnabled());
        }
        params.put("name", filter.getName());
		String tagsAsString = CollectionUtils.isEmpty(filter.getTags()) ? null : String.join(",", filter.getTags());
		params.put("tags", tagsAsString);
        return getNamedJdbcTemplate().queryForObject(selectJobsCountByFilterSQL, params, Integer.class);
    }

    @Override
    public List<JobPO> searchJobs(JobFilter filter) {
        Map<String, Object> params = new HashMap<>();

        params.put("limit", filter.getItemCount());
        params.put("offset", filter.getFromInd());
        params.put("name", filter.getName());
		params.put("sort_by", StringUtils.isEmpty(filter.getSortBy()) ? "name" : filter.getSortBy());
		String tagsAsString = CollectionUtils.isEmpty(filter.getTags()) ? null : String.join(",", filter.getTags());
		params.put("tags", tagsAsString);

        if (filter.getEnabled() == null) {
            params.put("activeFilterEnabled", 0);
            params.put("enabled", filter.getEnabled());
        } else {
            params.put("activeFilterEnabled", 1);
            params.put("enabled", filter.getEnabled());
        }
		if (StringUtils.equalsIgnoreCase(filter.getOrder(), "desc")) {
			return getNamedJdbcTemplate().query(searchJobsByFilterDescSQL, params, new JobRowMapper());
		} else {
			return getNamedJdbcTemplate().query(searchJobsByFilterAscSQL, params, new JobRowMapper());
		}
    }

    @Override
    public List<JobPO> getJobsWithParameters() {
        final Map<Long, JobPO> jobs = new HashMap<>();
        getJdbcTemplate().query(selectAllJobsAndParamsSQL, rs -> {
            while (rs.next()) {
                JobPO j = jobs.get(rs.getLong("j_" + JobPO.FIELD_ID));
                if (j == null) {
                    j = extractResultFromRs(rs);

                    jobs.put(j.getId(), j);
                }

                final JobParameterPO jobParameterPO = extractJobParameter(rs);
                if (jobParameterPO != null) {
                    j.addParameter(jobParameterPO);
                }
            }
            return null;
        });

        return new ArrayList<>(jobs.values());
    }

    @Override
    public JobPO findJob(long jobId) {
        final List<JobPO> jobs = getNamedJdbcTemplate().query(selectByJobIdSQL,
                Collections.singletonMap(JobPO.FIELD_ID, jobId), new JobRowMapper());
        if (jobs.size() == 1) {
            return jobs.get(0);
        }

        return null;
    }

    @Override
    public JobPO findJobWithParameters(long jobId) {
        return getNamedJdbcTemplate().query(
                selectByJobIdWithParametersSQL,
                Collections.singletonMap(JobPO.FIELD_ID, jobId),
                rs -> {
                    if (!rs.next()) {
                        return null;
                    }

                    final JobPO result = extractResultFromRs(rs);
                    do {
                        final JobParameterPO jobParameterPO = extractJobParameter(rs);
                        if (jobParameterPO != null) {
                            result.addParameter(jobParameterPO);
                        }
                    }
                    while (rs.next());

                    return result;
                }
        );
    }

    private JobPO extractResultFromRs(ResultSet rs) throws SQLException {
        final JobPO result = new JobPO();
        result.setId(rs.getLong("j_" + JobPO.FIELD_ID));
        result.setName(rs.getString("j_" + JobPO.FIELD_NAME));
        result.setEnabled(rs.getBoolean("j_" + JobPO.FIELD_ENABLED));
        result.setError(rs.getBoolean("j_" + JobPO.FIELD_ERROR));
        result.setCreatedBy(rs.getString("j_" + AbstractObjectPO.FIELD_CREATED_BY));
        result.setUpdatedBy(rs.getString("j_" + AbstractObjectPO.FIELD_UPDATED_BY));
        result.setCreateDate(rs.getTime("j_" + AbstractObjectPO.FIELD_CREATE_DATE));
        result.setUpdateDate(rs.getTime("j_" + AbstractObjectPO.FIELD_UPDATE_DATE));
        result.setDescription(rs.getString("j_" + JobPO.FIELD_DESCRIPTION));
        result.setCronExpression(rs.getString("j_" + JobPO.FIELD_CRON_EXPRESSION));
        result.setJobNameReference(rs.getString("j_" + JobPO.FIELD_JOB_NAME_REFERENCE));
    	Array tags = rs.getArray("j_" + JobPO.FIELD_TAGS);
		if (tags != null) {
			result.setTags(Arrays.asList((String[]) tags.getArray()));
		}
        return result;
    }

    private JobParameterPO extractJobParameter(ResultSet rs) throws SQLException {
        final long paramId = rs.getLong("p_" + JobParameterPO.FIELD_ID);
        if (!rs.wasNull()) {
            final String name = rs.getString("p_" + JobParameterPO.FIELD_NAME);
            final String valString = rs.getString("p_" + JobParameterPO.FIELD_VAL_STRING);
            final Timestamp valDate = rs.getTimestamp("p_" + JobParameterPO.FIELD_VAL_DATE);
            Long valLong = rs.getLong("p_" + JobParameterPO.FIELD_VAL_LONG);
            if (rs.wasNull()) {
                valLong = null;
            }
            Double valDouble = rs.getDouble("p_" + JobParameterPO.FIELD_VAL_DOUBLE);
            if (rs.wasNull()) {
                valDouble = null;
            }
            Boolean valBoolean = rs.getBoolean("p_" + JobParameterPO.FIELD_VAL_BOOLEAN);
            if (rs.wasNull()) {
                valBoolean = null;
            }

            String[] valStringArr = null;
            Array stringArr = rs.getArray("p_" + JobParameterPO.FIELD_VAL_ARR_STRING);
            if (stringArr != null) {
                valStringArr = (String[])stringArr.getArray();
            }

            Timestamp[] valDateArr = null;
            Array dateArr = rs.getArray("p_" + JobParameterPO.FIELD_VAL_ARR_DATE);
            if (dateArr != null) {
                valDateArr = (Timestamp[])dateArr.getArray();
            }

            Long[] valLongArr = null;
            Array longArr = rs.getArray("p_" + JobParameterPO.FIELD_VAL_ARR_LONG);
            if (longArr != null) {
                valLongArr = (Long[])longArr.getArray();
            }

            Double[] valDoubleArr = null;
            Array doubleArr = rs.getArray("p_" + JobParameterPO.FIELD_VAL_ARR_DOUBLE);
            if (doubleArr != null) {
                valDoubleArr = (Double[])doubleArr.getArray();
            }

            Boolean[] valBooleanArr = null;
            Array booleanArr = rs.getArray("p_" + JobParameterPO.FIELD_VAL_ARR_BOOLEAN);
            if (booleanArr != null) {
                valBooleanArr = (Boolean[])booleanArr.getArray();
            }

            final JobParameterPO p = JobParameterRowMapper.validateAndCreateJobParameter(name,
                    valString, valDate, valLong, valDouble, valBoolean,
                    valStringArr, valDateArr, valLongArr, valDoubleArr, valBooleanArr);
            if (p != null) {
                p.setId(paramId);
                p.setJobId(rs.getLong("j_" + JobPO.FIELD_ID));
                p.setCreatedBy(rs.getString("p_" + AbstractObjectPO.FIELD_CREATED_BY));
                p.setUpdatedBy(rs.getString("p_" + AbstractObjectPO.FIELD_UPDATED_BY));
                p.setCreateDate(rs.getTime("p_" + AbstractObjectPO.FIELD_CREATE_DATE));
                p.setUpdateDate(rs.getTime("p_" + AbstractObjectPO.FIELD_UPDATE_DATE));
                return p;
            }
        }
        return null;
    }

    @Override
    public JobPO findJob(final String jobName) {
        final List<JobPO> jobs = getNamedJdbcTemplate().query(selectByJobNameSQL,
                Collections.singletonMap(JobPO.FIELD_NAME, jobName), new JobRowMapper());
        if (jobs.size() == 1) {
            return jobs.get(0);
        }

        return null;
    }

    @Override
    public List<JobParameterPO> getJobParameters(final long jobId) {
        return getNamedJdbcTemplate().query(selectJobParametersByJobIdSQL,
                Collections.singletonMap(JobParameterPO.FIELD_JOB_ID, jobId),
                new JobParameterRowMapper());
    }

    @Override
    public Map<Long, List<JobParameterPO>> getJobsParameters(final List<Long> jobIds) {
        final Map<Long, List<JobParameterPO>> result = new HashMap<>();

        if (!CollectionUtils.isEmpty(jobIds)) {
            final List<JobParameterPO> params = getNamedJdbcTemplate().query(selectJobParametersByJobIdsSQL,
                    Collections.singletonMap("ids", jobIds), new JobParameterRowMapper());
            if (!CollectionUtils.isEmpty(params)) {
                for (final JobParameterPO p : params) {
                    List<JobParameterPO> jobParams = result.get(p.getJobId());
                    if (jobParams == null) {
                        jobParams = new ArrayList<>();
                        result.put(p.getJobId(), jobParams);
                    }
                    jobParams.add(p);
                }
            }
        }

        return result;
    }

    @Override
    public long insertJob(final JobPO newJob) {

        final Map<String, Object> params = new HashMap<>();
        params.put(AbstractObjectPO.FIELD_CREATED_BY, newJob.getCreatedBy());
        params.put(AbstractObjectPO.FIELD_CREATE_DATE, newJob.getCreateDate());
        params.put(JobPO.FIELD_NAME, newJob.getName());
        params.put(JobPO.FIELD_CRON_EXPRESSION, newJob.getCronExpression());
        params.put(JobPO.FIELD_DESCRIPTION, newJob.getDescription());
        params.put(JobPO.FIELD_JOB_NAME_REFERENCE, newJob.getJobNameReference());
        params.put(JobPO.FIELD_ENABLED, newJob.isEnabled());

        try (Connection con = getBareConnection()) {
			params.put(JobPO.FIELD_TAGS, CollectionUtils.isEmpty(newJob.getTags())
					? null
					: con.createArrayOf("text", newJob.getTags().toArray()));
		} catch (SQLException e) {
			throw new PlatformFailureException("Job import failed", e,
					CoreExceptionIds.EX_JOB_IMPORT_FAILED);
		}
        final Number jobId = jobTemplateInsertActor.executeAndReturnKey(params);

        newJob.setId(jobId.longValue());
        upsertParameters(newJob);

        newJob.setParameters(getJobParameters(newJob.getId()));

        return newJob.getId();
    }

	@Override
	public void updateJobTags(final JobPO job) {
		Map<String, Object> params = new HashMap<>();
		params.put(JobPO.FIELD_ID, job.getId());
		String tagsAsString = CollectionUtils.isEmpty(job.getTags()) ? null : String.join(",", job.getTags());
		params.put(JobPO.FIELD_TAGS, tagsAsString);
		getNamedJdbcTemplate().update(updateJobTagsSQL, params);

	}

    @Override
    public boolean updateJob(final JobPO job) {

        Objects.requireNonNull(job, "JobPO cannot be null");
        Objects.requireNonNull(job.getUpdateDate(), "JobPO update date cannot be null");
        Objects.requireNonNull(job.getUpdatedBy(), "JobPO updated by cannot be null");

        int updatesCount = getJdbcTemplate().execute(updateJobByIdSQL, (PreparedStatementCallback<Integer>) ps -> {
            ps.setString(1, job.getName());
            ps.setTimestamp(2, new Timestamp(job.getUpdateDate().getTime()));
            ps.setString(3, job.getUpdatedBy());
            ps.setBoolean(4, job.isEnabled());
            ps.setString(5, job.getCronExpression());
            ps.setString(6, job.getDescription());
            ps.setString(7, job.getJobNameReference());
            String tagsAsString = CollectionUtils.isEmpty(job.getTags()) ? null : String.join(",", job.getTags());
            ps.setString(8, tagsAsString);
            ps.setLong(9, job.getId());

            return ps.executeUpdate();
        });

        if (updatesCount == 0) {
            throw new PlatformFailureException("Failed to update job by ID: " + job.getId(),
                    CoreExceptionIds.EX_JOB_UPDATE_ERROR, job);
        }

        if (CollectionUtils.isEmpty(job.getParameters())) {
            // delete all job parameters
            getNamedJdbcTemplate().execute(deleteJobParamsSQL,
                    Collections.singletonMap(JobParameterPO.FIELD_JOB_ID, job.getId()),
                    PreparedStatement::executeUpdate);
        } else {
            // delete removed parameters
            deleteRemovedParameters(job);

            upsertParameters(job);

            job.setParameters(getJobParameters(job.getId()));
        }

        return updatesCount == 1;
    }

    private void upsertParameters(final JobPO job) {
        if (CollectionUtils.isEmpty(job.getParameters())) {
            return;
        }
        try (Connection con = getJdbcTemplate().getDataSource().getConnection()) {
            List<SqlParameterSource> parameterSources = new ArrayList<>();
            for (JobParameterPO record : job.getParameters()) {
                MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                        .addValue(JobParameterPO.FIELD_JOB_ID, job.getId())
                        .addValue(JobParameterPO.FIELD_NAME, record.getName())
                        .addValue(
                                AbstractObjectPO.FIELD_CREATE_DATE,
                                record.getCreateDate() != null ? record.getCreateDate() : new Date()
                        )
                        .addValue(
                                AbstractObjectPO.FIELD_CREATED_BY,
                                record.getCreatedBy() != null ? record.getCreatedBy() : SecurityUtils.getCurrentUserName()
                        )
                        .addValue(AbstractObjectPO.FIELD_UPDATE_DATE, record.getUpdateDate())
                        .addValue(AbstractObjectPO.FIELD_UPDATED_BY, record.getUpdatedBy())
                        .addValue(JobParameterPO.FIELD_VAL_STRING, record.getStringValue())
                        .addValue(JobParameterPO.FIELD_VAL_DATE, record.getDateValue() == null
                                ? null
                                : Timestamp.valueOf(record.getDateValue().toLocalDateTime()))
                        .addValue(JobParameterPO.FIELD_VAL_LONG, record.getLongValue())
                        .addValue(JobParameterPO.FIELD_VAL_DOUBLE, record.getDoubleValue())
                        .addValue(JobParameterPO.FIELD_VAL_BOOLEAN, record.getBooleanValue())

                        .addValue(JobParameterPO.FIELD_VAL_ARR_STRING, record.getStringArrayValue() == null ?
                                null : con.createArrayOf("text", record.getStringArrayValue()))

                        .addValue(JobParameterPO.FIELD_VAL_ARR_DATE, record.getDateArrayValue() == null ?
                                null :
                                con.createArrayOf("timestamp",
                                        convertZonedDateTime(record.getDateArrayValue())))

                        .addValue(JobParameterPO.FIELD_VAL_ARR_LONG, record.getLongArrayValue() == null ?
                                null : con.createArrayOf("bigint", record.getLongArrayValue()))

                        .addValue(JobParameterPO.FIELD_VAL_ARR_DOUBLE, record.getDoubleArrayValue() == null ?
                                null : con.createArrayOf("double", record.getDoubleArrayValue()))

                        .addValue(JobParameterPO.FIELD_VAL_ARR_BOOLEAN, record.getBooleanArrayValue() == null ?
                                null : con.createArrayOf("boolean", record.getBooleanArrayValue()));
                parameterSources.add(parameterSource);
            }
            SqlParameterSource[] batchArgs = parameterSources.toArray(new SqlParameterSource[parameterSources.size()]);

            getNamedJdbcTemplate().batchUpdate(upsertJobParameterSQL, batchArgs);
        } catch (SQLException e) {
            throw new PlatformFailureException("Failed to prepare parameters for save job with id: " + job.getId(), e,
                    CoreExceptionIds.EX_JOB_PARAMETER_PREPARE_UPSERT_ERROR, job.getId());
        }
    }

    private Timestamp[] convertZonedDateTime(ZonedDateTime[] dateTimes) {
        Timestamp[] result = new Timestamp[dateTimes.length];

        for (int i = 0; i < dateTimes.length; i++) {
            result[i] = Timestamp.from(dateTimes[i].toInstant());
        }

        return result;
    }

    @Override
    public boolean removeJob(long jobId) {
        getNamedJdbcTemplate().execute(deleteJobParamsSQL, Collections.singletonMap("job_id", jobId), PreparedStatement::executeUpdate);

        getJdbcTemplate().update(deleteBatchJobInstanceSQL, jobId);

        getJdbcTemplate().update(deleteJobTriggersSQL, jobId, jobId);

        int jobAffectedRows = getJdbcTemplate().update(deleteJobSQL, jobId);

        if (jobAffectedRows != 1) {
            throw new PlatformFailureException("Failed to delete job with id: " + jobId, CoreExceptionIds.EX_JOB_DELETE_FAILED, jobId);
        }
        return true;
    }

    @Override
    public void markJobEnabled(long jobId, boolean enabled) {
        final Map<String, Object> params = new HashMap<>();
        params.put("enabled", enabled);
        params.put("id", jobId);

        getNamedJdbcTemplate().update(updateJobEnabledByIdSQL, params);
    }

    @Override
    public void markJobError(Collection<Long> jobIds, boolean error) {
        final Map<String, Object> params = new HashMap<>();
        params.put("error", error);
        params.put("ids", jobIds);

        getNamedJdbcTemplate().update(updateJobErrorByIdsSQL, params);
    }

    @Override
    public void saveBatchJobInstance(final long jobId, final long batchJobId, final String username,
                                     final Date dateCreated) {
        final JobBatchJobInstancePO jobInstance = new JobBatchJobInstancePO();
        jobInstance.setJobId(jobId);
        jobInstance.setJobInstanceId(batchJobId);
        jobInstance.setCreateDate(dateCreated);
        jobInstance.setCreatedBy(username);

        final Map<String, Object> params = new HashMap<>();
        params.put(AbstractObjectPO.FIELD_CREATED_BY, jobInstance.getCreatedBy());
        params.put(AbstractObjectPO.FIELD_CREATE_DATE, jobInstance.getCreateDate());
        params.put(JobBatchJobInstancePO.FIELD_JOB_ID, jobInstance.getJobId());
        params.put(JobBatchJobInstancePO.FIELD_JOB_INSTANCE_ID, jobInstance.getJobInstanceId());

        jobBatchJobInstanceInsertActor.execute(params);
    }

    @Override
    public Map<Long, List<Long>> findAllBatchJobIds(final List<Long> jobIds) {
        final Map<Long, List<Long>> result = new HashMap<>();

        if (!CollectionUtils.isEmpty(jobIds)) {
            getNamedJdbcTemplate().query(selectJobInstancesByJobIdsSQL, Collections.singletonMap("ids", jobIds),
                    rs -> {
                        while (rs.next()) {
                            final Long jobId = rs.getLong(1);
                            final Long jobInstanceId = rs.getLong(2);

                            List<Long> instanceList = result.get(jobId);
                            if (instanceList == null) {
                                instanceList = new ArrayList<>();
                                result.put(jobId, instanceList);
                            }
                            instanceList.add(jobInstanceId);
                        }
                        return null;
                    });
        }

        return result;
    }

    @Override
    public Map<Long, Long> findLastBatchJobIds(List<Long> jobIds) {
        final Map<Long, Long> result = new HashMap<>();

        if (!CollectionUtils.isEmpty(jobIds)) {

            getNamedJdbcTemplate().query(selectJobLastExecutionSQL, Collections.singletonMap("ids", jobIds), rs -> {
                while (rs.next()) {
                    result.put(rs.getLong(1), rs.getLong(2));
                }
                return null;
            });
        }

        return result;
    }

    @Override
    public JobPO checkJobByParams(final JobPO jobPo) {

        Objects.requireNonNull(jobPo, "JobPO cannot be null.");

        if (!CollectionUtils.isEmpty(jobPo.getParameters())) {
            try (Connection con = getJdbcTemplate().getDataSource().getConnection()) {
                final Map<String, Object> paramMap = new HashMap<>();
                final StringBuilder paramSb = new StringBuilder();
                final List<JobParameterPO> parameters = jobPo.getParameters();
                for (int i = 0; i < parameters.size(); i++) {
                    final JobParameterPO p = parameters.get(i);
                    final String paramName = "param" + i;
                    final String valName = "val" + i;
                    paramSb.append("p.name = :").append(paramName).append(" and p.").append(p.getType().getFieldName())
                            .append(" = :").append(valName).append(" or ");
                    paramMap.put(paramName, p.getName());

                    Object paramValue = null;
                    switch (p.getType()) {
                        case STRING_ARR:{
                            paramValue = con.createArrayOf("text", p.getStringArrayValue());
                            break;
                        }
                        case DATE_ARR:{
                            // FIXME: time API support in jdbc 4.2 (http://openjdk.java.net/jeps/170)
                            // FIXME: but postgres driver 9.4-1206-jdbc42 is not yet compatible
                            paramValue = con.createArrayOf("timestamp",
                                    convertZonedDateTime(p.getDateArrayValue()));
                            break;
                        }
                        case LONG_ARR:{
                            paramValue = con.createArrayOf("bigint", p.getLongArrayValue());
                            break;
                        }
                        case DOUBLE_ARR:{
                            paramValue = con.createArrayOf("double", p.getDoubleArrayValue());
                            break;
                        }
                        case BOOLEAN_ARR:{
                            paramValue = con.createArrayOf("boolean", p.getBooleanArrayValue());
                            break;
                        }
                        case DATE: {
                            // FIXME: time API support in jdbc 4.2 (http://openjdk.java.net/jeps/170)
                            // FIXME: but postgres driver 9.4-1206-jdbc42 is not yet compatible
                            final ZonedDateTime zdt = (ZonedDateTime) p.getValueObject();
                            paramValue = Timestamp.from(zdt.toInstant());
                            break;
                        }
                        case TEXT:
                        case STRING:
                        case LONG:
                        case DOUBLE:
                        case BOOLEAN: {
                            paramValue = p.getValueObject();
                            break;
                        }
                    }

                    paramMap.put(valName, paramValue);
                }

                paramSb.delete(paramSb.lastIndexOf(" or "), paramSb.length());

                final String sql = String.format(selectJobCheckParamsSQL, paramSb.toString());
                paramMap.put("jobNameRef", jobPo.getJobNameReference());
                paramMap.put("jobId", jobPo.getId());

                return getNamedJdbcTemplate().query(sql, paramMap, rs -> {
                    while (rs.next()) {
                        final int paramCount = rs.getInt("param_count");
                        if (paramCount == parameters.size()) {
                            JobPO job = new JobPO(rs.getLong("job_" + JobPO.FIELD_ID),
                                    rs.getString("job_" + JobPO.FIELD_NAME),
                                    rs.getBoolean("job_" + JobPO.FIELD_ENABLED),
                                    rs.getString("job_" + JobPO.FIELD_CRON_EXPRESSION),
                                    rs.getString(JobPO.FIELD_JOB_NAME_REFERENCE),
                                    rs.getString("job_" + JobPO.FIELD_DESCRIPTION)
                                    );

                            job.setError(rs.getBoolean("job_" + JobPO.FIELD_ERROR));

                            return job;
                        }
                    }
                    return null;
                });
            } catch (SQLException e) {
                throw new PlatformFailureException("Failed to prepare job parameters for job validation check", e,
                        CoreExceptionIds.EX_JOB_PARAMETER_PREPARE_VALIDATION_ERROR);
            }
        } else {
            final Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(JobPO.FIELD_JOB_NAME_REFERENCE, jobPo.getJobNameReference());
            paramMap.put(JobPO.FIELD_ID, jobPo.getId());
            // TODO: why we can't add job with empty params if exists job with params.
            return getNamedJdbcTemplate().query(selectJobsByRefNameSQL, paramMap, rs -> {
                if (rs.next()) {
                    JobPO job = new JobPO(rs.getLong(JobPO.FIELD_ID),
                            rs.getString(JobPO.FIELD_NAME),
                            rs.getBoolean(JobPO.FIELD_ENABLED),
                            rs.getString(JobPO.FIELD_CRON_EXPRESSION),
                            rs.getString(JobPO.FIELD_JOB_NAME_REFERENCE),
                            rs.getString(JobPO.FIELD_DESCRIPTION)
                            );

                    job.setError(rs.getBoolean(JobPO.FIELD_ERROR));

                    return job;
                }
                return null;
            });
        }
    }

    @Override
    public long insertJobTrigger(final JobTriggerPO jobTrigger) {
        final Map<String, Object> params = new HashMap<>();
        params.put(AbstractObjectPO.FIELD_CREATED_BY, jobTrigger.getCreatedBy());
        params.put(AbstractObjectPO.FIELD_CREATE_DATE, jobTrigger.getCreateDate());
        params.put(JobTriggerPO.FIELD_FINISH_JOB_ID, jobTrigger.getFinishJobId());
        params.put(JobTriggerPO.FIELD_START_JOB_ID, jobTrigger.getStartJobId());
        params.put(JobTriggerPO.FIELD_SUCCESS_RULE, jobTrigger.getSuccessRule());
        params.put(JobTriggerPO.FIELD_NAME, jobTrigger.getName());
        params.put(JobTriggerPO.FIELD_DESCRIPTION, jobTrigger.getDescription());

        final Number jobId = jobTriggerTemplateInsertActor.executeAndReturnKey(params);

        jobTrigger.setId(jobId.longValue());

        return jobTrigger.getId();
    }

    @Override
    public boolean updateJobTrigger(final JobTriggerPO jobTrigger) {

        Objects.requireNonNull(jobTrigger, "JobTriggerPO cannot be null.");
        Objects.requireNonNull(jobTrigger.getUpdateDate(), "JobTriggerPO update date cannot be null.");
        Objects.requireNonNull(jobTrigger.getUpdatedBy(), "JobTriggerPO updated by cannot be null.");

        int updatesCount = getJdbcTemplate().update(updateJobTriggerByIdSQL, ps -> {
            ps.setLong(1, jobTrigger.getStartJobId());
            ps.setBoolean(2, jobTrigger.getSuccessRule());
            ps.setString(3, jobTrigger.getName());
            ps.setString(4, jobTrigger.getDescription());
            ps.setString(5, jobTrigger.getUpdatedBy());
            ps.setDate(6, new java.sql.Date(jobTrigger.getUpdateDate().getTime()));
            ps.setLong(7, jobTrigger.getId());
        });

        if (updatesCount == 0) {
            throw new PlatformFailureException("Failed to update job trigger by ID: " + jobTrigger.getId(),
                    CoreExceptionIds.EX_JOB_TRIGGER_UPDATE_ERROR, jobTrigger);
        }

        return updatesCount == 1;
    }

    @Override
    public List<JobTriggerPO> findAllJobTriggers(final long jobId) {
        return getNamedJdbcTemplate().query(selectJobTriggersByIdSQL,
                Collections.singletonMap(JobTriggerPO.FIELD_FINISH_JOB_ID, jobId), new JobTriggerRowMapper());
    }

    @Override
    public List<Long> getTriggerSuccessfulJobIds(final long jobId) {
        final Map<String, Object> params = new HashMap<>();
        params.put(JobTriggerPO.FIELD_FINISH_JOB_ID, jobId);
        params.put(JobTriggerPO.FIELD_SUCCESS_RULE, true);

        return getNamedJdbcTemplate().queryForList(selectJobJobsByIdAndTriggerRuleSQL, params, Long.class);
    }

    @Override
    public List<Long> getTriggerFailedJobIds(final long jobId) {
        final Map<String, Object> params = new HashMap<>();
        params.put(JobTriggerPO.FIELD_FINISH_JOB_ID, jobId);
        params.put(JobTriggerPO.FIELD_SUCCESS_RULE, false);

        return getNamedJdbcTemplate().queryForList(selectJobJobsByIdAndTriggerRuleSQL, params, Long.class);
    }

    @Override
    public JobTriggerPO findJobTrigger(final Long jobId, final Long triggerId) {
        final Map<String, Object> params = new HashMap<>();
        params.put(JobTriggerPO.FIELD_ID, triggerId);
        params.put(JobTriggerPO.FIELD_FINISH_JOB_ID, jobId);
        final List<JobTriggerPO> jobs = getNamedJdbcTemplate().query(selectTriggerByJobIdTriggerIdSQL,
                params, new JobTriggerRowMapper());
        if (jobs.size() == 1) {
            return jobs.get(0);
        }

        return null;
    }

    @Override
    public JobTriggerPO findJobTrigger(final String name) {
        final List<JobTriggerPO> jobs = getNamedJdbcTemplate().query(selectTriggerByTriggerNameSQL,
                Collections.singletonMap(JobTriggerPO.FIELD_NAME, name), new JobTriggerRowMapper());
        if (jobs.size() == 1) {
            return jobs.get(0);
        }

        return null;
    }

    @Override
    public boolean removeJobTrigger(Long jobId, Long triggerId) {
        int jobAffectedRows = getJdbcTemplate().update(deleteJobTriggerSQL, jobId, triggerId);

        if (jobAffectedRows != 1) {
            throw new PlatformFailureException("Failed to delete job trigger with job id: " + jobId
                    + " and trigger id: " + triggerId, CoreExceptionIds.EX_JOB_TRIGGER_DELETE_FAILED, triggerId);
        }
        return true;
    }

    private void deleteRemovedParameters(final JobPO job) {

        // Old
        final List<JobParameterPO> jobParamList = getJobParameters(job.getId());

        // Merge with new
        final List<Long> idsToRemove = Stream.concat(jobParamList.stream(), job.getParameters().stream())
                .map(JobParameterPO::getId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        getNamedJdbcTemplate().update(deleteRemovedJobParamsSQL, Map.of("ids_to_remove", idsToRemove));
    }

    @Override
    public List<JobPO> findJobWithParameters(final List<Long> jobsIds) {
        final List<Map<String, Object>> jobs =
                getNamedJdbcTemplate().queryForList(
                        CollectionUtils.isEmpty(jobsIds) ? selectAllJobsWithParamsSQL : selectJobsByIdsWithParamsSQL,
                        Collections.singletonMap("jobsIds", jobsIds)
                );
        return new ArrayList<>(
                jobs.stream()
                        .reduce(new HashMap<>(), this::appendJobWithParameter, this::mergerJobs)
                        .values()
        );
    }

    private Map<Long, JobPO> appendJobWithParameter(final Map<Long, JobPO> jobs, final Map<String, Object> row) {
        final Long jobId = (Long) row.get("jobId");
        if (!jobs.containsKey(jobId)) {
        	JobPO job =     new JobPO(
                    jobId,
                    (String) row.get("jobName"),
                    (Boolean) row.get("jobEnabled"),
                    (String) row.get("jobCronExpr"),
                    (String) row.get("jobNameRef"),
                    (String) row.get("jobDescr")
            );
        	Array tags = (Array) row.get("jobTags");
    		if (tags != null) {
    			try {
					job.setTags(Arrays.asList((String[]) tags.getArray()));
				} catch (SQLException e) {
			            throw new PlatformFailureException(
			                    "Exception while retrieving job tags",
                                e,
                                CoreExceptionIds.EX_JOB_EXPORT_FAILED
                        );
				}
    		}
			jobs.put(jobId, job);
        }
        final JobParameterPO param = extractJobParameter(jobId, row);
        if (param != null) {
            jobs.get(jobId).addParameter(param);
        }
        return jobs;
    }

    private JobParameterPO extractJobParameter(final Long jobId, Map<String, Object> row) {
        try {
            final String name = (String) row.get("jobParameterName");
            final String valString = (String) row.get("jobParameterValString");
            final Timestamp valDate = (Timestamp) row.get("jobParameterValDate");
            final Long valLong = (Long) row.get("jobParameterValLong");
            final Double valDouble = (Double) row.get("jobParameterValDouble");
            final Boolean valBoolean = (Boolean) row.get("jobParameterValBoolean");

            String[] valStringArr = null;
            final Array stringArr = (Array)row.get("jobParameterValArrString");
            if (stringArr != null) {
                valStringArr = (String[])stringArr.getArray();
            }

            Timestamp[] valDateArr = null;
            final Array dateArr = (Array)row.get("jobParameterValArrDate");
            if (dateArr != null) {
                valDateArr = (Timestamp[])dateArr.getArray();
            }

            Long[] valLongArr = null;
            final Array longArr = (Array)row.get("jobParameterValArrLong");
            if (longArr != null) {
                valLongArr = (Long[])longArr.getArray();
            }

            Double[] valDoubleArr = null;
            final Array doubleArr = (Array)row.get("jobParameterValArrDouble");
            if (doubleArr != null) {
                valDoubleArr = (Double[])doubleArr.getArray();
            }

            Boolean[] valBooleanArr = null;
            final Array booleanArr = (Array)row.get("jobParameterValArrBoolean");
            if (booleanArr != null) {
                valBooleanArr = (Boolean[])booleanArr.getArray();
            }

            final JobParameterPO jobParameter = JobParameterRowMapper.validateAndCreateJobParameter(
                    name, valString, valDate, valLong, valDouble, valBoolean,
                    valStringArr, valDateArr, valLongArr, valDoubleArr, valBooleanArr);
            if (jobParameter != null) {
                jobParameter.setId((Long) row.get("jobParameterId"));
                jobParameter.setJobId(jobId);
                jobParameter.setCreatedBy((String) row.get("jobParameterCreatedBy"));
                jobParameter.setUpdatedBy((String) row.get("jobParameterUpdatedBy"));
                jobParameter.setCreateDate((Date) row.get("jobParameterCreateDate"));
                jobParameter.setUpdateDate((Date) row.get("jobParameterUpdateDate"));
                return jobParameter;
            }
            return null;
        } catch (SQLException e) {
            throw new PlatformFailureException(
                    "Failed to extract job parameters for job with id: " + jobId,
                    e,
                    CoreExceptionIds.EX_JOB_PARAMETER_EXTRACT_ERROR,
                    jobId
            );
        }
    }


    private Map<Long, JobPO> mergerJobs(final Map<Long, JobPO> jobs1, final Map<Long, JobPO> jobs2) {
        jobs2.forEach((key, value) -> {
            if (jobs1.containsKey(key)) {
                value.getParameters().forEach(p -> jobs1.get(key).addParameter(p));
            }
            else {
                jobs1.put(key, value);
            }
        });
        return jobs1;
    }

    @Transactional
    @Override
    public void saveJobs(List<JobPO> jobs) {
        jobs.forEach(job -> {
            removeJob(job.getName());
            insertJob(job);
        });
    }

    private void removeJob(String name) {
        final JobPO job = findJob(name);
        if (job != null) {
            removeJob(job.getId());
        }
    }
    @Override
    public List<String> getAllTags() {
    	return getJdbcTemplate().queryForList(selectAllTagsSQL, String.class);
    }
}
