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

import java.sql.Types;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.huahui.datasphere.mdm.system.dao.impl.BaseDAOImpl;

import com.huahui.datasphere.mdm.core.dao.LibrariesDAO;
import com.huahui.datasphere.mdm.core.po.LibraryPO;

/**
 * @author theseusyang on Oct 1, 2020
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
