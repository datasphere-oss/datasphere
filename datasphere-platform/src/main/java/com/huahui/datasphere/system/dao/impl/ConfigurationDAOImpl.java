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

package com.huahui.datasphere.system.dao.impl;

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
import org.unidata.mdm.system.configuration.SystemConfigurationConstants;

import com.huahui.datasphere.system.dao.ConfigurationDAO;

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
