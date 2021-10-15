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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.core.repository.dao.JdbcExecutionContextDao;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.serializer.Serializer;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author amagdenko
 * Extends standard ExecutionContext DAO by adding two collection methods,
 * allowing to load several execution contexts at once.
 */
public class CustomJdbcExecutionContextDAOImpl extends JdbcExecutionContextDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomJdbcExecutionContextDAOImpl.class);

    /**
     * Custom mapper.
     */
    private static final ExecutionContextRowMapper EXECUTION_CONTEXT_ROW_MAPPER = new ExecutionContextRowMapper();

    private static final String FIND_JOB_EXECUTION_CONTEXT_BY_JOB_EXECUTION_IDS =
        "SELECT SHORT_CONTEXT, SERIALIZED_CONTEXT, JOB_EXECUTION_ID " +
            "FROM %PREFIX%JOB_EXECUTION_CONTEXT " +
            "WHERE JOB_EXECUTION_ID in (SELECT UNNEST(?))";

    private static final String FIND_STEP_EXECUTION_CONTEXT_BY_STEP_EXECUTION_IDS =
        "SELECT SHORT_CONTEXT, SERIALIZED_CONTEXT, STEP_EXECUTION_ID "+
            "FROM %PREFIX%STEP_EXECUTION_CONTEXT " +
            "WHERE STEP_EXECUTION_ID in (SELECT UNNEST(?))";

    private ExecutionContextSerializer serializer;

    /**
   	 * Setter for {@link Serializer} implementation
   	 *
   	 * @param serializer
   	 */
    @Override
   	public void setSerializer(ExecutionContextSerializer serializer) {
        super.setSerializer(serializer);
   		this.serializer = serializer;
   	}

    public Map<Long, ExecutionContext> loadJobExecutionContexts(Collection<Long> jobExecutionIds) {

        if (CollectionUtils.isEmpty(jobExecutionIds)) {
            return Collections.emptyMap();
        }

        // Couldn't be actually. Just want to make SONAR happy.
        DataSource dataSource = ((JdbcTemplate) getJdbcTemplate()).getDataSource();
        if (Objects.isNull(dataSource)) {
            return Collections.emptyMap();
        }

        final Map<Long, ExecutionContext> result = new LinkedHashMap<>();
        try (Connection connection = dataSource.getConnection()) {

            Array jea = connection.createArrayOf("int8", jobExecutionIds.toArray(Long[]::new));
            getJdbcTemplate().query(getQuery(FIND_JOB_EXECUTION_CONTEXT_BY_JOB_EXECUTION_IDS),
                rs -> {
                    long jobExecutionId = rs.getLong("JOB_EXECUTION_ID");
                    result.put(jobExecutionId, EXECUTION_CONTEXT_ROW_MAPPER.mapRow(serializer, rs));
                },
                jea);

        } catch (SQLException e) {
            LOGGER.warn("Exception caught.", e);
        }

        return result;
    }

    public Map<Long, ExecutionContext> loadStepExecutionContexts(Collection<Long> stepExecutionIds) {

        if (CollectionUtils.isEmpty(stepExecutionIds)) {
            return Collections.emptyMap();
        }

        // Couldn't be actually. Just want to make SONAR happy.
        DataSource dataSource = ((JdbcTemplate) getJdbcTemplate()).getDataSource();
        if (Objects.isNull(dataSource)) {
            return Collections.emptyMap();
        }

        final Map<Long, ExecutionContext> result = new LinkedHashMap<>();
        try (Connection connection = dataSource.getConnection()) {

            Array sea = connection.createArrayOf("int8", stepExecutionIds.toArray(Long[]::new));
            getJdbcTemplate().query(getQuery(FIND_STEP_EXECUTION_CONTEXT_BY_STEP_EXECUTION_IDS),
                rs -> {
                    long stepExecutionId = rs.getLong("STEP_EXECUTION_ID");
                    result.put(stepExecutionId, EXECUTION_CONTEXT_ROW_MAPPER.mapRow(serializer, rs));
                },
                sea);

        } catch (SQLException e) {
            LOGGER.warn("Exception caught.", e);
        }

        return result;
    }

    private static class ExecutionContextRowMapper {

        public ExecutionContext mapRow(ExecutionContextSerializer serializer, ResultSet rs) throws SQLException {

            ExecutionContext ec = new ExecutionContext();
            String serializedContext = rs.getString("SERIALIZED_CONTEXT");
            if (serializedContext == null) {
                serializedContext = rs.getString("SHORT_CONTEXT");
            }

            Map<String, Object> map;
            try {
                ByteArrayInputStream in = new ByteArrayInputStream(serializedContext.getBytes(StandardCharsets.UTF_8));
                map = serializer.deserialize(in);
            } catch (IOException ioe) {
                throw new IllegalArgumentException("Unable to deserialize the execution context", ioe);
            }

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                ec.put(entry.getKey(), entry.getValue());
            }

            return ec;
        }
    }
}
