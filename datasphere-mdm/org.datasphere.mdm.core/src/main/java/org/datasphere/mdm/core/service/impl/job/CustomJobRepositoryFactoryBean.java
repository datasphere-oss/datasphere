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

import static org.springframework.batch.support.DatabaseType.SYBASE;

import java.lang.reflect.Field;
import java.sql.Types;

import javax.sql.DataSource;

import org.datasphere.mdm.core.dao.impl.CustomJdbcJobExecutionDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer;
import org.springframework.batch.core.repository.dao.JdbcExecutionContextDao;
import org.springframework.batch.core.repository.dao.JdbcJobExecutionDao;
import org.springframework.batch.core.repository.dao.JdbcJobInstanceDao;
import org.springframework.batch.core.repository.dao.JdbcStepExecutionDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.repository.support.AbstractJobRepositoryFactoryBean;
import org.springframework.batch.item.database.support.DataFieldMaxValueIncrementerFactory;
import org.springframework.batch.item.database.support.DefaultDataFieldMaxValueIncrementerFactory;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author Aleksandr Magdenko
 */
public class CustomJobRepositoryFactoryBean extends AbstractJobRepositoryFactoryBean implements InitializingBean {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CustomJobRepositoryFactoryBean.class);

    private DataSource dataSource;

    private JdbcOperations jdbcOperations;

    private NamedParameterJdbcTemplate namedJdbcOperations;

    private String databaseType;

    private String tablePrefix = AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;

    private DataFieldMaxValueIncrementerFactory incrementerFactory;

    private int maxVarCharLength = AbstractJdbcBatchMetadataDao.DEFAULT_EXIT_MESSAGE_LENGTH;

    private LobHandler lobHandler;

    private ExecutionContextSerializer serializer;

    private Integer lobType;

    /**
     * @param type a value from the {@link Types} class to indicate the type to use for a CLOB
     */
    public void setClobType(int type) {
        this.lobType = type;
    }

    /**
     * A custom implementation of the {@link ExecutionContextSerializer}.
     * The default, if not injected, is the {@link Jackson2ExecutionContextStringSerializer}.
     *
     * @param serializer used to serialize/deserialize {@link org.springframework.batch.item.ExecutionContext}
     * @see ExecutionContextSerializer
     */
    public void setSerializer(ExecutionContextSerializer serializer) {
        this.serializer = serializer;
    }

    /**
     * A special handler for large objects. The default is usually fine, except
     * for some (usually older) versions of Oracle. The default is determined
     * from the data base type.
     *
     * @param lobHandler the {@link LobHandler} to set
     * @see LobHandler
     */
    public void setLobHandler(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    /**
     * Public setter for the length of long string columns in database. Do not
     * set this if you haven't modified the schema. Note this value will be used
     * for the exit message in both {@link JdbcJobExecutionDao} and
     * {@link JdbcStepExecutionDao} and also the short version of the execution
     * context in {@link JdbcExecutionContextDao} . For databases with
     * multi-byte character sets this number can be smaller (by up to a factor
     * of 2 for 2-byte characters) than the declaration of the column length in
     * the DDL for the tables.
     *
     * @param maxVarCharLength the exitMessageLength to set
     */
    public void setMaxVarCharLength(int maxVarCharLength) {
        this.maxVarCharLength = maxVarCharLength;
    }

    /**
     * Public setter for the {@link DataSource}.
     *
     * @param dataSource a {@link DataSource}
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Public setter for the {@link JdbcOperations}. If this property is not set explicitly,
     * a new {@link JdbcTemplate} will be created for the configured DataSource by default.
     *
     * @param jdbcOperations a {@link JdbcOperations}
     */
    public void setJdbcOperations(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    /**
     * Sets the database type.
     *
     * @param dbType as specified by
     * {@link DefaultDataFieldMaxValueIncrementerFactory}
     */
    public void setDatabaseType(String dbType) {
        this.databaseType = dbType;
    }

    /**
     * Sets the table prefix for all the batch meta-data tables.
     *
     * @param tablePrefix prefix prepended to batch meta-data tables
     */
    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public void setIncrementerFactory(DataFieldMaxValueIncrementerFactory incrementerFactory) {
        this.incrementerFactory = incrementerFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Assert.notNull(dataSource, "DataSource must not be null.");

        if (jdbcOperations == null) {
            jdbcOperations = new JdbcTemplate(dataSource);
        }

        if (namedJdbcOperations == null) {
            namedJdbcOperations = new NamedParameterJdbcTemplate(dataSource);
        }

        if (incrementerFactory == null) {
            incrementerFactory = new DefaultDataFieldMaxValueIncrementerFactory(dataSource);
        }

        if (databaseType == null) {
            databaseType = DatabaseType.fromMetaData(dataSource).name();
            LOGGER.info("No database type set, using meta data indicating: {}.", databaseType);
        }

        if (lobHandler == null && databaseType.equalsIgnoreCase(DatabaseType.ORACLE.toString())) {
            lobHandler = new DefaultLobHandler();
        }

        if (serializer == null) {
            serializer = new Jackson2ExecutionContextStringSerializer();
        }

        Assert.isTrue(incrementerFactory.isSupportedIncrementerType(databaseType), "'" + databaseType
                + "' is an unsupported database type.  The supported database types are "
                + StringUtils.arrayToCommaDelimitedString(incrementerFactory.getSupportedIncrementerTypes()));

        if (lobType != null) {
            Assert.isTrue(isValidTypes(lobType), "lobType must be a value from the java.sql.Types class");
        }

        super.afterPropertiesSet();
    }

    @Override
    protected JobInstanceDao createJobInstanceDao() throws Exception {
        JdbcJobInstanceDao dao = new JdbcJobInstanceDao();
        dao.setJdbcTemplate(jdbcOperations);
        dao.setJobIncrementer(incrementerFactory.getIncrementer(databaseType, tablePrefix + "JOB_SEQ"));
        dao.setTablePrefix(tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    @Override
    protected JobExecutionDao createJobExecutionDao() throws Exception {
        CustomJdbcJobExecutionDAOImpl dao = new CustomJdbcJobExecutionDAOImpl();
        dao.setJdbcTemplate(jdbcOperations);
        dao.setJobExecutionIncrementer(incrementerFactory.getIncrementer(databaseType, tablePrefix + "JOB_EXECUTION_SEQ"));
        dao.setTablePrefix(tablePrefix);
        dao.setClobTypeToUse(determineClobTypeToUse(this.databaseType));
        dao.setExitMessageLength(maxVarCharLength);
        dao.afterPropertiesSet();
        return dao;
    }

    @Override
    protected StepExecutionDao createStepExecutionDao() throws Exception {
        JdbcStepExecutionDao dao = new JdbcStepExecutionDao();
        dao.setJdbcTemplate(jdbcOperations);
        dao.setStepExecutionIncrementer(incrementerFactory.getIncrementer(databaseType, tablePrefix + "STEP_EXECUTION_SEQ"));
        dao.setTablePrefix(tablePrefix);
        dao.setClobTypeToUse(determineClobTypeToUse(this.databaseType));
        dao.setExitMessageLength(maxVarCharLength);
        dao.afterPropertiesSet();
        return dao;
    }

    @Override
    protected ExecutionContextDao createExecutionContextDao() throws Exception {
        JdbcExecutionContextDao dao = new JdbcExecutionContextDao();
        dao.setJdbcTemplate(jdbcOperations);
        dao.setTablePrefix(tablePrefix);
        dao.setClobTypeToUse(determineClobTypeToUse(this.databaseType));
        dao.setSerializer(serializer);

        if (lobHandler != null) {
            dao.setLobHandler(lobHandler);
        }

        dao.afterPropertiesSet();
        // Assume the same length.
        dao.setShortContextLength(maxVarCharLength);
        return dao;
    }

    private int determineClobTypeToUse(String databaseType) {
        if (lobType != null) {
            return lobType;
        } else {
            if (SYBASE == DatabaseType.valueOf(databaseType.toUpperCase())) {
                return Types.LONGVARCHAR;
            } else {
                return Types.CLOB;
            }
        }
    }

    private boolean isValidTypes(int value) throws IllegalAccessException {

        boolean result = false;
        for (Field field : Types.class.getFields()) {
            int curValue = field.getInt(null);
            if (curValue == value) {
                result = true;
                break;
            }
        }

        return result;
    }
}