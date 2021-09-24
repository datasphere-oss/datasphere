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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.huahui.datasphere.mdm.system.dao.impl.BaseDAOImpl;

import com.huahui.datasphere.mdm.core.dao.CustomStorageDao;
import com.huahui.datasphere.mdm.core.dao.rm.CustomSettingsObjectRowMapper;
import com.huahui.datasphere.mdm.core.po.CustomStorageRecordPO;

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
