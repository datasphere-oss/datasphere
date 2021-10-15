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
package org.datasphere.mdm.core.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.dao.JobDefinitionsDAO;
import org.datasphere.mdm.core.dao.vendor.VendorUtils;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.exception.JobException;
import org.datasphere.mdm.core.po.AbstractObjectPO;
import org.datasphere.mdm.core.po.job.JobDefinitionPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.datasphere.mdm.system.dao.impl.BaseDAOImpl;
import org.datasphere.mdm.system.exception.PlatformFailureException;

/**
 * @author Mikhail Mikhailov on Jun 24, 2021
 */
@Repository
public class JobDefinitionsDAOImpl extends BaseDAOImpl implements JobDefinitionsDAO {
    /**
     * Definitions filter SQL types.
     */
    private static final int[] DEFINITIONS_FILTER_TYPES = {
            // Job names
            Types.ARRAY,
            Types.ARRAY,
            // Tags
            Types.ARRAY,
            Types.ARRAY,
            // Def names
            Types.ARRAY,
            Types.ARRAY,
            // Enabled
            Types.BOOLEAN,
            Types.BOOLEAN,
            // Created by
            Types.VARCHAR,
            Types.VARCHAR,
            // Status
            Types.VARCHAR,
            Types.VARCHAR,
    };
    /**
     * Definitions insert SQL types (needed due to array member).
     * /*
            created_by,
            name,
            job_name,
            description,
            cron_expression,
            parameters,
            enabled,
            tags
         */
    private static final int[] DEFINITIONS_INSERT_TYPES = {
        // Created by
        Types.VARCHAR,
        // Definition name
        Types.VARCHAR,
        // Job name
        Types.VARCHAR,
        // Description
        Types.VARCHAR,
        // Cron expression
        Types.VARCHAR,
        // Parameters
        Types.VARCHAR,
        // Enabled
        Types.BOOLEAN,
        // Tags
        Types.ARRAY
    };

    private static final RowMapper<JobDefinitionPO> DEFAULT_ROW_MAPPER = (rs, row) -> {

        final JobDefinitionPO jd = new JobDefinitionPO();

        jd.setId(rs.getLong(JobDefinitionPO.FIELD_ID));
        jd.setName(rs.getString(JobDefinitionPO.FIELD_NAME));
        jd.setJobName(rs.getString(JobDefinitionPO.FIELD_JOB_NAME));
        jd.setCronExpression(rs.getString(JobDefinitionPO.FIELD_CRON_EXPRESSION));
        jd.setDescription(rs.getString(JobDefinitionPO.FIELD_DESCRIPTION));
        jd.setEnabled(rs.getBoolean(JobDefinitionPO.FIELD_ENABLED));
        jd.setError(rs.getBoolean(JobDefinitionPO.FIELD_ERROR));
        jd.setParametersAsString(rs.getString(JobDefinitionPO.FIELD_PARAMETERS));

        Array tags = rs.getArray(JobDefinitionPO.FIELD_TAGS);
        if (tags != null) {
            jd.setTags(Arrays.asList((String[]) tags.getArray()));
        }

        jd.setCreatedBy(rs.getString(AbstractObjectPO.FIELD_CREATED_BY));
        jd.setUpdatedBy(rs.getString(AbstractObjectPO.FIELD_UPDATED_BY));
        jd.setCreateDate(rs.getTimestamp(AbstractObjectPO.FIELD_CREATE_DATE));
        jd.setUpdateDate(rs.getTimestamp(AbstractObjectPO.FIELD_UPDATE_DATE));

        return jd;
    };
    /**
     * Load definitions by criteria.
     */
    private final String loadDefinitionsSQL;
    /**
     * Count by filter.
     */
    private final String countDefinitionsSQL;
    /**
     * Load by ID(s).
     */
    private final String loadDefinitionsByIdSQL;
    /**
     * Insert JD.
     */
    private final String insertDefinitionSQL;
    /**
     * Update JD.
     */
    private final String updateDefinitionSQL;
    /**
     * Enables/disables JD.
     */
    private final String updateDefinitionEnabledSQL;
    /**
     * Marks a JD as having errors.
     */
    private final String updateDefinitionErrorSQL;
    /**
     * Deletes a definition.
     */
    private final String deleteDefinitionSQL;
    /**
     * Loads all tags.
     */
    private final String loadAllTagsSQL;
    /**
     * Constructor.
     * @param dataSource
     */
    @Autowired
    public JobDefinitionsDAOImpl(
            @Qualifier("coreDataSource") final DataSource dataSource,
            @Qualifier("job-definitions-sql") final Properties sql) {

        super(dataSource);

        loadDefinitionsSQL = sql.getProperty("loadDefinitionsSQL");
        countDefinitionsSQL = sql.getProperty("countDefinitionsSQL");
        loadDefinitionsByIdSQL = sql.getProperty("loadDefinitionsByIdSQL");
        insertDefinitionSQL = sql.getProperty("insertDefinitionSQL");
        updateDefinitionSQL = sql.getProperty("updateDefinitionSQL");
        updateDefinitionEnabledSQL = sql.getProperty("updateDefinitionEnabledSQL");
        updateDefinitionErrorSQL = sql.getProperty("updateDefinitionErrorSQL");
        deleteDefinitionSQL = sql.getProperty("deleteDefinitionSQL");
        loadAllTagsSQL = sql.getProperty("loadAllTagsSQL");
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<JobDefinitionPO> load(Map<String, Object> fields,
            String sortBy, String sortOrder,
            Integer limit, Long offset) {

        try (Connection c = getBareConnection()) {

            Collection<String> jobNames = (Collection<String>) fields.get(JobDefinitionPO.FIELD_JOB_NAME);
            Collection<String> defNames = (Collection<String>) fields.get(JobDefinitionPO.FIELD_NAME);
            Collection<String> tags = (Collection<String>) fields.get(JobDefinitionPO.FIELD_TAGS);
            Boolean enabled = (Boolean) fields.get(JobDefinitionPO.FIELD_ENABLED);
            String createdBy = (String) fields.get(AbstractObjectPO.FIELD_CREATED_BY);
            String status = (String) fields.get(JobDefinitionPO.FIELD_STATUS);

            Array jobNamesParam = CollectionUtils.isEmpty(jobNames) ? null : c.createArrayOf(VendorUtils.VARCHAR_SQL_TYPE, jobNames.toArray(String[]::new));
            Array defNamesParam = CollectionUtils.isEmpty(defNames) ? null : c.createArrayOf(VendorUtils.VARCHAR_SQL_TYPE, defNames.toArray(String[]::new));
            Array tagsParam = CollectionUtils.isEmpty(tags) ? null : c.createArrayOf(VendorUtils.VARCHAR_SQL_TYPE, tags.toArray(String[]::new));

            final Object[] params = {
                    jobNamesParam, jobNamesParam,
                    tagsParam, tagsParam,
                    defNamesParam, defNamesParam,
                    enabled, enabled,
                    createdBy, createdBy,
                    status, status
            };

            final String query = new StringBuilder(loadDefinitionsSQL)
                    .append(" order by ")
                    .append(StringUtils.isBlank(sortBy) ? JobDefinitionPO.FIELD_ID : sortBy)
                    .append(" ")
                    .append(StringUtils.isBlank(sortOrder) ? "asc" : sortOrder)
                    .append(" limit ")
                    .append(limit)
                    .append(" offset ")
                    .append(offset)
                    .toString();

            return getJdbcTemplate().query(query, params, DEFINITIONS_FILTER_TYPES, DEFAULT_ROW_MAPPER);
        } catch (SQLException e) {
            failFetch(e);
        }

        return Collections.emptyList();
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public int count(Map<String, Object> fields) {

        try (Connection c = getBareConnection()) {

            Collection<String> jobNames = (Collection<String>) fields.get(JobDefinitionPO.FIELD_JOB_NAME);
            Collection<String> defNames = (Collection<String>) fields.get(JobDefinitionPO.FIELD_NAME);
            Collection<String> tags = (Collection<String>) fields.get(JobDefinitionPO.FIELD_TAGS);
            Boolean enabled = (Boolean) fields.get(JobDefinitionPO.FIELD_ENABLED);
            String createdBy = (String) fields.get(AbstractObjectPO.FIELD_CREATED_BY);
            String status = (String) fields.get(JobDefinitionPO.FIELD_STATUS);

            Array jobNamesParam = CollectionUtils.isEmpty(jobNames) ? null : c.createArrayOf(VendorUtils.VARCHAR_SQL_TYPE, jobNames.toArray(String[]::new));
            Array defNamesParam = CollectionUtils.isEmpty(defNames) ? null : c.createArrayOf(VendorUtils.VARCHAR_SQL_TYPE, defNames.toArray(String[]::new));
            Array tagsParam = CollectionUtils.isEmpty(tags) ? null : c.createArrayOf(VendorUtils.VARCHAR_SQL_TYPE, tags.toArray(String[]::new));

            final Object[] params = {
                    jobNamesParam, jobNamesParam,
                    tagsParam, tagsParam,
                    defNamesParam, defNamesParam,
                    enabled, enabled,
                    createdBy, createdBy,
                    status, status
            };

            return getJdbcTemplate().queryForObject(countDefinitionsSQL, params, DEFINITIONS_FILTER_TYPES, Integer.class);
        } catch (SQLException e) {
            failFetch(e);
        }

        return 0;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<JobDefinitionPO> load(Collection<Long> ids) {

        try (Connection c = getBareConnection()) {

            Array idsParam = c.createArrayOf(VendorUtils.LONG_SQL_TYPE, ids.toArray(Long[]::new));
            return getJdbcTemplate().query(loadDefinitionsByIdSQL, DEFAULT_ROW_MAPPER, idsParam);
        } catch (SQLException e) {
            failFetch(e);
        }

        return Collections.emptyList();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> loadAllTags() {
        return getJdbcTemplate().queryForList(loadAllTagsSQL, String.class);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public long insert(JobDefinitionPO po) {

        try (Connection c = getBareConnection()) {

            Array tagsParam = CollectionUtils.isNotEmpty(po.getTags())
                    ? c.createArrayOf(VendorUtils.VARCHAR_SQL_TYPE, po.getTags().toArray(String[]::new))
                    : null;

            final Object[] args = new Object[] {
                    po.getCreatedBy(),
                    po.getName(),
                    po.getJobName(),
                    po.getDescription(),
                    po.getCronExpression(),
                    po.getParametersAsString(),
                    po.isEnabled(),
                    tagsParam
            };

            return jdbcTemplate.queryForObject(insertDefinitionSQL, args, DEFINITIONS_INSERT_TYPES, Long.class);
        } catch (SQLException e) {
            throw new JobException("Failed to insert job definition. Exception caught:", e,
                    CoreExceptionIds.EX_JOB_DEFINITION_PUT_FAILURE);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void update(JobDefinitionPO po) {

        try (Connection c = getBareConnection()) {

            Array tagsParam = CollectionUtils.isNotEmpty(po.getTags())
                    ? c.createArrayOf(VendorUtils.VARCHAR_SQL_TYPE, po.getTags().toArray(String[]::new))
                    : null;

            int updatesCount = getJdbcTemplate().update(updateDefinitionSQL, ps -> {

                ps.setString(1, po.getName());
                ps.setString(2, po.getJobName());
                ps.setString(3, po.getDescription());
                ps.setString(4, po.getCronExpression());
                ps.setString(5, po.getParametersAsString());
                ps.setBoolean(6, po.isEnabled());
                ps.setArray(7, tagsParam);
                ps.setTimestamp(8, new Timestamp(po.getUpdateDate().getTime()));
                ps.setString(9, po.getUpdatedBy());
                ps.setLong(10, po.getId());

            });

            if (updatesCount == 0) {
                throw new PlatformFailureException("Failed to update job by ID: [{}].",
                        CoreExceptionIds.EX_JOB_UPDATE_ERROR, po.getId());
            }
        } catch (SQLException e) {
            failPut(e);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void enable(Long id, boolean state) {
        getJdbcTemplate().update(updateDefinitionEnabledSQL, state, id);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void mark(Long id, boolean state) {
        getJdbcTemplate().update(updateDefinitionErrorSQL, state, id);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Long id) {
        getJdbcTemplate().update(deleteDefinitionSQL, id);
    }

    private void failFetch(SQLException e) {
        throw new JobException("Failed to fetch job definition(s).", e,
                CoreExceptionIds.EX_JOB_DEFINITION_FETCH_FAILURE);
    }

    private void failPut(SQLException e) {
        throw new JobException("Failed to put job definition.", e,
                CoreExceptionIds.EX_JOB_DEFINITION_PUT_FAILURE);
    }
}
