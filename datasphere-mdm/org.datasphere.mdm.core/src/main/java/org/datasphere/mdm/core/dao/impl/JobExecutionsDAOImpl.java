package org.datasphere.mdm.core.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.lang3.tuple.Pair;
import org.datasphere.mdm.core.dao.JobExecutionsDAO;
import org.datasphere.mdm.core.dao.vendor.VendorUtils;
import org.datasphere.mdm.core.po.job.ExecutionStatePO;
import org.datasphere.mdm.core.po.job.JobBatchJobInstancePO;
import org.datasphere.mdm.core.po.job.JobSysJobInstancePO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.datasphere.mdm.system.dao.impl.BaseDAOImpl;

/**
 * @author Mikhail Mikhailov on Jul 5, 2021
 */
@Repository
public class JobExecutionsDAOImpl extends BaseDAOImpl implements JobExecutionsDAO {
    /**
     * This DAO logger.
     */
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(JobExecutionsDAOImpl.class);
    /**
     * Last executions.
     */
    private final String findLastInstanceIdsSQL;
    /**
     * List sys job instance id.
     */
    private final String findLastSystemInstanceIdsSQL;
    /**
     * All instance ids.
     */
    private final String findAllInstanceIdsSQL;
    /**
     * All sys job instance ids.
     */
    private final String findAllSystemInstanceIdsSQL;
    /**
     * Latest finished state.
     */
    private final String selectLatestFinishedStateSQL;
    /**
     * Insert def job ref.
     */
    private final String insertDefinitionJobReferenceSQL;
    /**
     * Insert sys job ref.
     */
    private final String insertSystemJobReferenceSQL;
    /**
     * Constructor.
     * @param dataSource
     */
    public JobExecutionsDAOImpl(
            @Qualifier("coreDataSource") final DataSource dataSource,
            @Qualifier("job-executions-sql") final Properties sql) {
        super(dataSource);
        findLastInstanceIdsSQL = sql.getProperty("findLastInstanceIdsSQL");
        findLastSystemInstanceIdsSQL = sql.getProperty("findLastSystemInstanceIdsSQL");
        findAllInstanceIdsSQL = sql.getProperty("findAllInstanceIdsSQL");
        findAllSystemInstanceIdsSQL = sql.getProperty("findAllSystemInstanceIdsSQL");
        selectLatestFinishedStateSQL = sql.getProperty("selectLatestFinishedStateSQL");
        insertDefinitionJobReferenceSQL = sql.getProperty("insertDefinitionJobReferenceSQL");
        insertSystemJobReferenceSQL = sql.getProperty("insertSystemJobReferenceSQL");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Long, Long> findLastInstanceIds(List<Long> jobDefinitionIds) {

        try (Connection c = getBareConnection()) {

            Array a = c.createArrayOf(VendorUtils.LONG_SQL_TYPE, jobDefinitionIds.toArray(Long[]::new));
            return getJdbcTemplate().query(findLastInstanceIdsSQL, (rs, row) -> Pair.of(rs.getLong(1), rs.getLong(2)), a)
                .stream()
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        } catch (SQLException sqle) {
            LOGGER.warn("Cannot find last executions. SQLE caught.", sqle);
        }

        return Collections.emptyMap();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Long findLastInstanceId(String jobName) {
        return getJdbcTemplate().query(findLastSystemInstanceIdsSQL, rs -> rs.next() ? rs.getLong(1) : null, jobName);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Long, List<Long>> findAllInstanceIds(final List<Long> jobDefinitionIds) {

        final Map<Long, List<Long>> result = new HashMap<>();
        final RowCallbackHandler rch = rs -> result.computeIfAbsent(rs.getLong(1), k -> new ArrayList<>()).add(rs.getLong(2));
        try (Connection c = getBareConnection()) {

            Array a = c.createArrayOf(VendorUtils.LONG_SQL_TYPE, jobDefinitionIds.toArray(Long[]::new));
            getJdbcTemplate().query(findAllInstanceIdsSQL, rch, a);

        } catch (SQLException sqle) {
            LOGGER.warn("Cannot find last executions. SQLE caught.", sqle);
        }

        return result;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> findAllInstanceIds(String jobName) {
        return getJdbcTemplate().query(findAllSystemInstanceIdsSQL, (rs, row) -> rs.getLong(1), jobName);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionStatePO findLastFinishedState(long jobDefibitionId) {
        return getJdbcTemplate().query(selectLatestFinishedStateSQL, rs -> {

            if (rs.next()) {

                ExecutionStatePO hit = new ExecutionStatePO();

                hit.setExitCode(rs.getString(ExecutionStatePO.FIELD_EXIT_CODE));
                hit.setExitMessage(rs.getString(ExecutionStatePO.FIELD_EXIT_MESSAGE));
                hit.setStatus(rs.getString(ExecutionStatePO.FIELD_STATUS));

                return hit;
            }

            return null;
        } , jobDefibitionId);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void save(JobBatchJobInstancePO po) {
        getJdbcTemplate().update(insertDefinitionJobReferenceSQL, po.getJobDefinitionId(), po.getJobInstanceId(), po.getCreatedBy());

    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void save(JobSysJobInstancePO po) {
        getJdbcTemplate().update(insertSystemJobReferenceSQL, po.getJobName(), po.getJobInstanceId(), po.getCreatedBy());
    }
}
