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

import org.datasphere.mdm.core.dao.CustomStorageDao;
import org.datasphere.mdm.core.dao.rm.CustomSettingsObjectRowMapper;
import org.datasphere.mdm.core.po.CustomStorageRecordPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.datasphere.mdm.system.dao.impl.BaseDAOImpl;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Dmitry Kopin on 25.08.2017.
 */
@Repository
public class CustomStorageDaoImpl extends BaseDAOImpl implements CustomStorageDao {

    private final String upsertRecord;

    private final String deleteRecord;

    private final String deleteRecordsByUser;

    private final String deleteRecordsByKey;

    private final String loadRecord;

    private final String loadRecordsByUser;

    private final String loadRecordsByKey;


    @Autowired
    public CustomStorageDaoImpl(
            @Qualifier("coreDataSource") final DataSource dataSource,
            @Qualifier("custom-storage-sql") final Properties sql
    ) {
        super(dataSource);
        upsertRecord = sql.getProperty("upsertRecord");
        deleteRecord = sql.getProperty("deleteRecord");
        deleteRecordsByUser = sql.getProperty("deleteRecordsByUser");
        deleteRecordsByKey = sql.getProperty("deleteRecordsByKey");
        loadRecord = sql.getProperty("loadRecord");
        loadRecordsByUser = sql.getProperty("loadRecordsByUser");
        loadRecordsByKey = sql.getProperty("loadRecordsByKey");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createRecords(@Nonnull List<CustomStorageRecordPO> customStorageRecords) {
        List<Map<String, Object>> records = new ArrayList<>();
        customStorageRecords.forEach(recordPO -> {
            Map<String, Object> recordMap = new HashMap<>();
            recordMap.put(CustomStorageRecordPO.FIELD_KEY, extractKey(recordPO.getKey()));
            recordMap.put(CustomStorageRecordPO.FIELD_USER_NAME, extractUserName(recordPO.getUser()));
            recordMap.put(CustomStorageRecordPO.FIELD_VALUE, recordPO.getValue());
            records.add(recordMap);
        });
        namedJdbcTemplate.batchUpdate(upsertRecord, records.toArray(new Map[records.size()]));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void updateRecords(@Nonnull List<CustomStorageRecordPO> customStorageRecords) {
        List<Map<String, Object>> records = new ArrayList<>();
        customStorageRecords.forEach(recordPO -> {
            Map<String, Object> recordMap = new HashMap<>();
            recordMap.put(CustomStorageRecordPO.FIELD_KEY, extractKey(recordPO.getKey()));
            recordMap.put(CustomStorageRecordPO.FIELD_USER_NAME, extractUserName(recordPO.getUser()));
            recordMap.put(CustomStorageRecordPO.FIELD_VALUE, recordPO.getValue());
            records.add(recordMap);
        });
        namedJdbcTemplate.batchUpdate(upsertRecord, records.toArray(new Map[records.size()]));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deleteRecords(List<CustomStorageRecordPO> customStorageRecords) {
        List<Map<String, Object>> records = new ArrayList<>();
        customStorageRecords.forEach(recordPO -> {
            Map<String, Object> recordMap = new HashMap<>();
            recordMap.put(CustomStorageRecordPO.FIELD_KEY, extractKey(recordPO.getKey()));
            recordMap.put(CustomStorageRecordPO.FIELD_USER_NAME, extractUserName(recordPO.getUser()));
            records.add(recordMap);
        });
        namedJdbcTemplate.batchUpdate(deleteRecord, records.toArray(new Map[records.size()]));
    }

    @Override
    public void deleteRecordsByKey(String key) {
        Map<String, Object> params = new HashMap<>();
        params.put(CustomStorageRecordPO.FIELD_KEY, extractKey(key));
        namedJdbcTemplate.update(deleteRecordsByKey, params);
    }

    @Override
    public void deleteRecordsByUserName(String userName) {
        Map<String, Object> params = new HashMap<>();
        params.put(CustomStorageRecordPO.FIELD_USER_NAME, extractUserName(userName));
        namedJdbcTemplate.update(deleteRecordsByUser, params);
    }


    @Override
    public List<CustomStorageRecordPO> getRecordsByUserName(String userName) {
        Map<String, Object> params = new HashMap<>();
        params.put(CustomStorageRecordPO.FIELD_USER_NAME, extractUserName(userName));
        return namedJdbcTemplate.query(loadRecordsByUser, params, CustomSettingsObjectRowMapper.DEFAULT_ROW_MAPPER);
    }

    @Override
    public List<CustomStorageRecordPO> getRecordsByKey(String key) {
        Map<String, Object> params = new HashMap<>();
        params.put(CustomStorageRecordPO.FIELD_KEY, extractKey(key));
        return namedJdbcTemplate.query(loadRecordsByKey, params, CustomSettingsObjectRowMapper.DEFAULT_ROW_MAPPER);
    }

    @Override
    public List<CustomStorageRecordPO> getRecordsByKeyAndUser(String key, String userName) {
        Map<String, Object> params = new HashMap<>();
        params.put(CustomStorageRecordPO.FIELD_KEY, extractKey(key));
        params.put(CustomStorageRecordPO.FIELD_USER_NAME, extractUserName(userName));
        return namedJdbcTemplate.query(loadRecord, params, CustomSettingsObjectRowMapper.DEFAULT_ROW_MAPPER);
    }

    private String extractKey(String key){
        return key != null ? key : "";
    }

    private String extractUserName(String userName){
        return userName != null ? userName : "";
    }

}
