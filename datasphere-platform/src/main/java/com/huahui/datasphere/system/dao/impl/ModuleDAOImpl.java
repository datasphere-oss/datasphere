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
import org.unidata.mdm.system.configuration.SystemConfigurationConstants;

import com.huahui.datasphere.system.dao.ModuleDAO;
import com.huahui.datasphere.system.dto.ModuleInfo;
import com.huahui.datasphere.system.type.module.Module;

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
