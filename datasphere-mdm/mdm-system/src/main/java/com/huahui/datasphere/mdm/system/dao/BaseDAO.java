

package com.huahui.datasphere.mdm.system.dao;

import java.sql.Connection;

import javax.sql.DataSource;

/**
 * Dao which contain common request to DB
 */
public interface BaseDAO {
    /**
     * Gets the default configured datasource.
     * @return data source
     */
    DataSource getDefaultDataSource();
    /**
     * Gets the bare connection from the data source.
     * @return connection
     */
    Connection getBareConnection();
}
