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

package com.huahui.datasphere.system.module;

import javax.annotation.Nullable;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.unidata.mdm.system.configuration.SystemConfigurationConstants;
import org.unidata.mdm.system.configuration.SystemConfigurationProperty;

import com.hazelcast.core.Hazelcast;
import com.huahui.datasphere.system.context.DatabaseMigrationContext;
import com.huahui.datasphere.system.migration.SystemMigrations;
import com.huahui.datasphere.system.service.DatabaseMigrationService;
import com.huahui.datasphere.system.service.PipelineService;
import com.huahui.datasphere.system.service.PlatformConfiguration;
import com.huahui.datasphere.system.service.RenderingService;
import com.huahui.datasphere.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.system.type.module.Module;
import com.huahui.datasphere.system.util.ContextUtils;
import com.huahui.datasphere.system.util.DataSourceUtils;
import com.huahui.datasphere.system.util.IdUtils;
import com.huahui.datasphere.system.util.PipelineUtils;
import com.huahui.datasphere.system.util.ResourceUtils;
import com.huahui.datasphere.system.util.TextUtils;

/**
 * @author Alexander Malyshev
 */
public class SystemModule implements Module {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemModule.class);

    @Autowired
    private DataSource systemDataSource;

    @Autowired
    private PipelineService pipelineService;

    @Autowired
    private RenderingService renderingService;

    @Autowired
    private DatabaseMigrationService migrationService;

    @Autowired
    private PlatformConfiguration platformConfiguration;

    @Autowired
    private DisposableBean transactionManager;

    private boolean install;

    /**
     * This module id.
     */
    public static final String MODULE_ID = "org.unidata.mdm.system";

    @Override
    public String getId() {
        return MODULE_ID;
    }

    @Override
    public String getVersion() {
        return "6.0";
    }

    @Override
    public String getName() {
        return "Unidata system";
    }

    @Override
    public String getDescription() {
        return "Unidata system (root) module";
    }

    @Nullable
    @Override
    public String getTag() {
        return null;
    }

    @Override
    public void install() {

        LOGGER.info("Install");

        IdUtils.init();
        ResourceUtils.init();
        TextUtils.init();

        migrate();
        install = true;
    }

    @Override
    public void start() {

        LOGGER.info("Start");

        ContextUtils.init();
        IdUtils.init();
        ResourceUtils.init();
        TextUtils.init();
        PipelineUtils.init();

        if (platformConfiguration.isDeveloperMode() && !install) {
            migrate();
        }
    }

    @Override
    public void ready() {
        LOGGER.info("Ready");
        pipelineService.init();
        renderingService.init();
    }

    @Override
    public void stop() {
        Hazelcast.shutdownAll();
        DataSourceUtils.shutdown(systemDataSource);
        try {
            transactionManager.destroy();
        } catch (Exception e) {
            LOGGER.warn("Failed to destroy transaction manager.", e);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigurationProperty<?>[] getConfigurationProperties() {
        return SystemConfigurationProperty.values();
    }

    private void migrate() {
        migrationService.migrate(DatabaseMigrationContext.builder()
                .schemaName(SystemConfigurationConstants.SYSTEM_SCHEMA_NAME)
                .logName(SystemConfigurationConstants.SYSTEM_MIGRATION_LOG_NAME)
                .dataSource(systemDataSource)
                .migrations(SystemMigrations.migrations())
                .build());
    }

    @Override
    public String[] getResourceBundleBasenames() {
        return new String[]{"system_messages"};
    }
}
