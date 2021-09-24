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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.huahui.datasphere.mdm.system.configuration.SystemConfigurationConstants;
import com.huahui.datasphere.mdm.system.dao.ModuleDAO;
import com.huahui.datasphere.mdm.system.dto.ModuleInfo;
import com.huahui.datasphere.mdm.system.type.module.Module;

@Repository
public class ModuleDAOImpl extends NamedParameterJdbcDaoSupport implements ModuleDAO {

    private static final String MODULE_ID_COLUMN = "module_id";
    private static final String VERSION_COLUMN = "version";
    private static final String TAG_COLUMN = "tag";
    private static final String STATUS_COLUMN = "status";

    private final String moduleInfoTableExists;
    private final String fetchModulesInfo;
    private final String saveModulesInfo;

    public ModuleDAOImpl(@Qualifier("systemDataSource") final DataSource dataSource) {
        setDataSource(dataSource);
        initTemplateConfig();
        moduleInfoTableExists = "select exists(select 1 from pg_tables where schemaname = '"
                        + SystemConfigurationConstants.SYSTEM_SCHEMA_NAME
                        + "' and tablename = 'modules_info')";
        fetchModulesInfo = "select * from modules_info";
        saveModulesInfo = "insert into modules_info(module_id, version, tag, status) "
                + "values (:module_id, :version, :tag, :status) "
                + "on conflict (module_id) do update set version = :version, tag = :tag, status = :status";
    }

    @Override
    public boolean moduleInfoTableExists() {
        return getJdbcTemplate().queryForObject(moduleInfoTableExists, Boolean.class);
    }

    @Override
    public List<ModuleInfo> fetchModulesInfo() {
        if (!moduleInfoTableExists()) {
            return Collections.emptyList();
        }
        return getJdbcTemplate().query(
                fetchModulesInfo,
                (rs, num) -> {
                    final ModuleInfo moduleInfo = new ModuleInfo(
                            new ModuleStub(
                                    rs.getString(MODULE_ID_COLUMN),
                                    rs.getString(VERSION_COLUMN),
                                    rs.getString(TAG_COLUMN)
                            )
                    );
                    moduleInfo.setModuleStatus(ModuleInfo.ModuleStatus.valueOf(rs.getString(STATUS_COLUMN)));
                    return moduleInfo;
                }
        );
    }

    @Override
    public void saveModulesInfo(Collection<ModuleInfo> modulesInfo) {
        getNamedParameterJdbcTemplate().batchUpdate(
                saveModulesInfo,
                modulesInfo.stream()
                        .map(mi -> {
                            final Module module = mi.getModule();
                            final Map<String, Object> params = new HashMap<>();
                            params.put(MODULE_ID_COLUMN, module.getId());
                            params.put(VERSION_COLUMN, module.getVersion());
                            params.put(TAG_COLUMN, module.getTag());
                            params.put(STATUS_COLUMN, mi.getModuleStatus().name());
                            return params;
                        })
                        .map(MapSqlParameterSource::new)
                        .toArray(MapSqlParameterSource[]::new)
        );
    }

    public static class ModuleStub implements Module {

        private final String id;
        private final String version;
        private final String tag;

        public ModuleStub(
                final String id,
                final String version,
                final String tag
        ) {
            this.id = id;
            this.version = version;
            this.tag = tag;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getVersion() {
            return version;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Nullable
        @Override
        public String getTag() {
            return tag;
        }
    }
}
