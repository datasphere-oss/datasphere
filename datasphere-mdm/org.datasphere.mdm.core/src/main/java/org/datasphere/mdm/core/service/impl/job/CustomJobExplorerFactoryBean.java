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

import java.util.Objects;

import javax.sql.DataSource;

import org.datasphere.mdm.core.dao.impl.CustomJdbcExecutionContextDAOImpl;
import org.datasphere.mdm.core.dao.impl.CustomJdbcJobExecutionDAOImpl;
import org.datasphere.mdm.core.dao.impl.CustomJdbcStepExecutionDAOImpl;
import org.datasphere.mdm.core.service.job.CustomJobExplorer;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.AbstractJobExplorerFactoryBean;
import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer;
import org.springframework.batch.core.repository.dao.JdbcJobInstanceDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * @author amagdenko
 *
 */
public class CustomJobExplorerFactoryBean extends AbstractJobExplorerFactoryBean implements InitializingBean {

    private TransactionManager transactionManager;

    private ProxyFactory proxyFactory;

    private DataSource dataSource;

    private JdbcOperations jdbcOperations;

    private String tablePrefix = AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;

    private LobHandler lobHandler;

    private ExecutionContextSerializer serializer;

    private DataFieldMaxValueIncrementer incrementer = new AbstractDataFieldMaxValueIncrementer() {
           @Override
           protected long getNextKey() {
               throw new IllegalStateException("JobExplorer is read only.");
           }
    };
    /**
     * A custom implementation of the {@link ExecutionContextSerializer}.
     * The default, if not injected, is the {@link Jackson2ExecutionContextStringSerializer}.
     *
     * @param serializer used to serialize/deserialize an {@link ExecutionContext}
     * @see ExecutionContextSerializer
     */
    public void setSerializer(ExecutionContextSerializer serializer) {
        this.serializer = serializer;
    }
    /**
     * Public setter for the {@link DataSource}.
     *
     * @param dataSource
     *            a {@link DataSource}
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /**
     * Public setter for the {@link JdbcOperations}. If this property is not set explicitly,
     * a new {@link JdbcTemplate} will be created for the configured DataSource by default.
     * @param jdbcOperations a {@link JdbcOperations}
     */
    public void setJdbcOperations(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }
    /**
     * Sets the table prefix for all the batch meta-data tables.
     *
     * @param tablePrefix prefix for the batch meta-data tables
     */
    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }
    /**
     * The lob handler to use when saving {@link ExecutionContext} instances.
     * Defaults to null which works for most databases.
     *
     * @param lobHandler Large object handler for saving {@link ExecutionContext}
     */
    public void setLobHandler(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Objects.requireNonNull(dataSource, "DataSource must not be null.");

        if (jdbcOperations == null) {
            jdbcOperations = new JdbcTemplate(dataSource);
        }

        if (serializer == null) {
            serializer = new Jackson2ExecutionContextStringSerializer();
        }

        initializeProxy();
    }

    @Override
    protected JobInstanceDao createJobInstanceDao() throws Exception {
        JdbcJobInstanceDao dao = new JdbcJobInstanceDao();
        dao.setJdbcTemplate(jdbcOperations);
        dao.setJobIncrementer(incrementer);
        dao.setTablePrefix(tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    @Override
    protected CustomJdbcExecutionContextDAOImpl createExecutionContextDao() throws Exception {
        CustomJdbcExecutionContextDAOImpl dao = new CustomJdbcExecutionContextDAOImpl();
        dao.setJdbcTemplate(jdbcOperations);
        dao.setLobHandler(lobHandler);
        dao.setTablePrefix(tablePrefix);
        dao.setSerializer(serializer);
        dao.afterPropertiesSet();
        return dao;
    }

    @Override
    protected CustomJdbcJobExecutionDAOImpl createJobExecutionDao() throws Exception {
        CustomJdbcJobExecutionDAOImpl dao = new CustomJdbcJobExecutionDAOImpl();
        dao.setJdbcTemplate(jdbcOperations);
        dao.setJobExecutionIncrementer(incrementer);
        dao.setTablePrefix(tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    @Override
    protected CustomJdbcStepExecutionDAOImpl createStepExecutionDao() throws Exception {
        CustomJdbcStepExecutionDAOImpl dao = new CustomJdbcStepExecutionDAOImpl();
        dao.setJdbcTemplate(jdbcOperations);
        dao.setStepExecutionIncrementer(incrementer);
        dao.setTablePrefix(tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    @Override
    public CustomJobExplorer getObject() throws Exception {

        if (proxyFactory == null) {
            afterPropertiesSet();
        }

        return (CustomJobExplorer) proxyFactory.getProxy();
    }

    private CustomJobExplorer getTarget() throws Exception {
        return new CustomJobExplorerImpl(
                createJobInstanceDao(),
                createJobExecutionDao(),
                createStepExecutionDao(),
                createExecutionContextDao());
    }

    private void initializeProxy() throws Exception {

        if (proxyFactory == null) {

            NameMatchTransactionAttributeSource mtas = new NameMatchTransactionAttributeSource();
            mtas.addTransactionalMethod("*", new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED));

            TransactionInterceptor advice = new TransactionInterceptor(transactionManager, mtas);

            proxyFactory = new ProxyFactory();
            proxyFactory.addAdvice(advice);
            proxyFactory.setProxyTargetClass(true);
            proxyFactory.addInterface(JobExplorer.class);
            proxyFactory.setTarget(getTarget());
        }
    }
}
