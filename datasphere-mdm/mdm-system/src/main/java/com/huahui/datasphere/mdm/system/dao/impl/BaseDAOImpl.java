

/**
 *
 */
package com.huahui.datasphere.mdm.system.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.huahui.datasphere.mdm.system.dao.BaseDAO;
import com.huahui.datasphere.mdm.system.exception.PlatformFailureException;
import com.huahui.datasphere.mdm.system.exception.SystemExceptionIds;

/**
 * Base DAO class.
 */
public abstract class BaseDAOImpl implements BaseDAO {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDAOImpl.class);
    /**
     * JDBC template.
     */
    protected final JdbcTemplate jdbcTemplate;
    /**
     * Named parameter template.
     */
    protected final NamedParameterJdbcTemplate namedJdbcTemplate;
    /**
     * Constructor.
     */
    public BaseDAOImpl(DataSource dataSource) {
        super();
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DataSource getDefaultDataSource() {
        return jdbcTemplate.getDataSource();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getBareConnection() {
        try {
            DataSource dataSource = jdbcTemplate.getDataSource();
            Objects.requireNonNull(dataSource, "DataSource cannot be null.");
            return dataSource.getConnection();
        } catch (SQLException e) {
            final String message = "Cannot get bare connection from JDBC template.";
            LOGGER.error(message);
            throw new PlatformFailureException(message, SystemExceptionIds.EX_SYSTEM_CONNECTION_GET, e);
        }
    }
    /**
     * @return the jdbcTemplate
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
    /**
     * @return the namedJdbcTemplate
     */
    public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
        return namedJdbcTemplate;
    }
}
