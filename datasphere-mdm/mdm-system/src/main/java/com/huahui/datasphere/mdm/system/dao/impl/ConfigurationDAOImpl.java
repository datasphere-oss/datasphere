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

package com.huahui.datasphere.mdm.system.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.huahui.datasphere.mdm.system.configuration.SystemConfigurationConstants;
import com.huahui.datasphere.mdm.system.dao.ConfigurationDAO;

@Repository
public class ConfigurationDAOImpl extends NamedParameterJdbcDaoSupport implements ConfigurationDAO {

    private static final String FIELD_NAME = "name";
    private static final String FIELD_VALUE = "value";

    private final String selectAllSQL;
    private final String upsertPropertiesSQL;
    private final String selectByNamesSQL;
    private final String checkBootstrapSQL;

    @Autowired
    public ConfigurationDAOImpl(
            @Qualifier("systemDataSource") final DataSource dataSource,
            @Qualifier("configuration-sql") final Properties sql
    ) {
        setDataSource(dataSource);
        selectAllSQL = sql.getProperty("selectAllSQL");
        upsertPropertiesSQL = sql.getProperty("upsertPropertiesSQL");
        selectByNamesSQL = sql.getProperty("selectByNamesSQL");
        checkBootstrapSQL = sql.getProperty("checkBootstrapSQL");
    }

    @Override
    public Map<String, byte[]> fetchAllProperties() {
        return getJdbcTemplate().query(
                selectAllSQL,
                (rs, rowNum) -> Pair.of(rs.getString(FIELD_NAME), rs.getBytes(FIELD_VALUE))).stream()
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, byte[]> fetch(List<String> names) {
        return getNamedParameterJdbcTemplate().query(selectByNamesSQL, Collections.singletonMap(FIELD_NAME, names),
                (rs, rowNum) -> Pair.of(rs.getString(FIELD_NAME), rs.getBytes(FIELD_VALUE))).stream()
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    @Override
    public void save(final Map<String, byte[]> properties) {
        getNamedParameterJdbcTemplate().batchUpdate(
                upsertPropertiesSQL,
                properties.entrySet().stream()
                        .map(o ->
                            new MapSqlParameterSource()
                                    .addValue(FIELD_NAME, o.getKey())
                                    .addValue(FIELD_VALUE, o.getValue()))
                        .toArray(MapSqlParameterSource[]::new)
        );
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBootstrap() {
        return getJdbcTemplate().query(
                checkBootstrapSQL,
                rs -> rs.next() && rs.getBoolean(1),
                        SystemConfigurationConstants.SYSTEM_SCHEMA_NAME);
    }
}
