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

import java.sql.Types;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.dao.LibrariesDAO;
import org.datasphere.mdm.core.po.LibraryPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.datasphere.mdm.system.dao.impl.BaseDAOImpl;

/**
 * @author Mikhail Mikhailov on Oct 1, 2020
 * Libraries CRUD.
 */
@Repository
public class LibrariesDAOImpl extends BaseDAOImpl implements LibrariesDAO {
    /**
     * Libs filter SQL types.
     */
    private static final int[] LIBRARIES_FILTER_TYPES = {
         // With data?
        Types.BOOLEAN,
        // Storage ID
        Types.VARCHAR,
        // Name
        Types.VARCHAR,
        Types.VARCHAR,
        // MIME type
        Types.VARCHAR,
        Types.VARCHAR
    };
    /**
     * Libs remove SQL types.
     */
    private static final int[] LIBRARIES_REMOVE_TYPES = {
        // Storage ID
        Types.VARCHAR,
        // Name
        Types.VARCHAR,
        // Version
        Types.VARCHAR,
        Types.VARCHAR
    };
    /**
     * Default SSPO row mapper.
     */
    private static final RowMapper<LibraryPO> DEFAULT_ROW_MAPPER = (rs, row) -> {

        LibraryPO result = new LibraryPO();
        result.setStorageId(rs.getString(LibraryPO.FIELD_STORAGE_ID));
        result.setName(rs.getString(LibraryPO.FIELD_NAME));
        result.setVersion(rs.getString(LibraryPO.FIELD_VERSION));
        result.setMimeType(rs.getString(LibraryPO.FIELD_MIME_TYPE));
        result.setEditable(rs.getBoolean(LibraryPO.FIELD_EDITABLE));
        result.setDescription(rs.getString(LibraryPO.FIELD_DESCRIPTION));
        result.setCreatedBy(rs.getString(LibraryPO.FIELD_CREATED_BY));
        result.setCreateDate(rs.getTimestamp(LibraryPO.FIELD_CREATE_DATE));
        result.setContent(rs.getBytes(LibraryPO.FIELD_CONTENT));
        result.setSize(rs.getLong(LibraryPO.FIELD_SIZE));

        return result;
    };
    /**
     * Queries.
     */
    private final String listCurrentSQL;
    private final String listAllSQL;
    private final String exactGetSQL;
    private final String latestGetSQL;
    private final String saveSQL;
    private final String removeSQL;
    /**
     * Constructor.
     */
    @Autowired
    public LibrariesDAOImpl(
            @Qualifier("coreDataSource") final DataSource dataSource,
            @Qualifier("libraries-sql") final Properties sql) {
        super(dataSource);
        listCurrentSQL = sql.getProperty("listCurrentSQL");
        listAllSQL = sql.getProperty("listAllSQL");
        exactGetSQL = sql.getProperty("exactGetSQL");
        latestGetSQL = sql.getProperty("latestGetSQL");
        saveSQL = sql.getProperty("saveSQL");
        removeSQL = sql.getProperty("removeSQL");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public LibraryPO load(String storageId, String name, String version, boolean withData) {
        return getJdbcTemplate().query(exactGetSQL,
                rs -> rs.next() ? DEFAULT_ROW_MAPPER.mapRow(rs, 0) : null,
                withData, storageId, name, version);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public LibraryPO load(String storageId, String name, boolean withData) {
        return getJdbcTemplate().query(latestGetSQL,
                rs -> rs.next() ? DEFAULT_ROW_MAPPER.mapRow(rs, 0) : null,
                withData, storageId, name);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<LibraryPO> load(String storageId, String name, String mimeType, boolean latest, boolean withData) {

        final Object[] params = { withData, storageId, name, name, mimeType, mimeType };
        return latest
                ? getJdbcTemplate().query(listCurrentSQL, params, LIBRARIES_FILTER_TYPES, DEFAULT_ROW_MAPPER)
                : getJdbcTemplate().query(listAllSQL, params, LIBRARIES_FILTER_TYPES, DEFAULT_ROW_MAPPER);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(LibraryPO po, boolean force) {

        List<Integer> indicator = getJdbcTemplate().queryForList(saveSQL, Integer.class,
                po.getStorageId(),
                po.getName(),
                po.getVersion(),
                po.getMimeType(),
                po.isEditable(),
                po.getDescription(),
                po.getCreatedBy(),
                po.getContent(),
                po.getSize(),
                force);

        return CollectionUtils.isNotEmpty(indicator) && indicator.get(0) == 1;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(String storageId, String name, String version) {
        final Object[] params = { storageId, name, version, version };
        getJdbcTemplate().update(removeSQL, params, LIBRARIES_REMOVE_TYPES);
    }
}
