package org.datasphere.mdm.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.datasphere.mdm.core.dao.StorageModelDAO;
import org.datasphere.mdm.core.dao.rm.AbstractRowMapper;
import org.datasphere.mdm.core.po.model.StoragePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.datasphere.mdm.system.dao.impl.BaseDAOImpl;

/**
 * @author Mikhail Mikhailov on Oct 2, 2020
 */
@Repository
public class StorageModelDAOImpl extends BaseDAOImpl implements StorageModelDAO {
    /**
     * SQL queries.
     */
    private final String loadByIdSQL;
    private final String loadAllSQL;
    private final String removeSQL;
    private final String updateSQL;
    private final String createSQL;
    /**
     * Default storeage RM.
     */
    private static final RowMapper<StoragePO> DEFAULT_ROW_MAPPER = new AbstractRowMapper<StoragePO>() {
        /**
         * {@inheritDoc}
         */
        @Override
        public StoragePO mapRow(ResultSet rs, int rowNum) throws SQLException {

            StoragePO storage = new StoragePO();
            storage.setId(rs.getString(StoragePO.FIELD_ID));
            storage.setDescription(rs.getString(StoragePO.FIELD_DECSRIPTION));

            super.mapRow(storage, rs, rowNum);

            return storage;
        }
    };
    /**
     * Constructor.
     */
    @Autowired
    public StorageModelDAOImpl(
            @Qualifier("coreDataSource") final DataSource dataSource,
            @Qualifier("storage-model-sql") final Properties sql) {
        super(dataSource);
        loadByIdSQL = sql.getProperty("loadByIdSQL");
        loadAllSQL = sql.getProperty("loadAllSQL");
        removeSQL = sql.getProperty("removeSQL");
        updateSQL = sql.getProperty("updateSQL");
        createSQL = sql.getProperty("createSQL");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<StoragePO> loadAll() {
        return getJdbcTemplate().query(loadAllSQL, DEFAULT_ROW_MAPPER);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public StoragePO loadById(String id) {
        return getJdbcTemplate().query(loadByIdSQL, rs -> rs.next() ? DEFAULT_ROW_MAPPER.mapRow(rs, 0) : null, id);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void create(StoragePO po) {
        getJdbcTemplate().update(createSQL, po.getId(), po.getDescription(), po.getCreatedBy());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void update(StoragePO po) {
        Timestamp ts = po.getUpdateDate() != null ? new Timestamp(po.getUpdateDate().getTime()) : new Timestamp(System.currentTimeMillis());
        getJdbcTemplate().update(updateSQL, po.getDescription(), po.getUpdatedBy(), ts, po.getId());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(String id) {
        getJdbcTemplate().update(removeSQL, id);
    }
}
