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

package com.huahui.datasphere.mdm.system.module;

import javax.annotation.Nullable;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.hazelcast.core.Hazelcast;
import com.huahui.datasphere.mdm.system.configuration.SystemConfigurationConstants;
import com.huahui.datasphere.mdm.system.configuration.SystemConfigurationProperty;
import com.huahui.datasphere.mdm.system.context.DatabaseMigrationContext;
import com.huahui.datasphere.mdm.system.migration.SystemMigrations;
import com.huahui.datasphere.mdm.system.service.DatabaseMigrationService;
import com.huahui.datasphere.mdm.system.service.PipelineService;
import com.huahui.datasphere.mdm.system.service.PlatformConfiguration;
import com.huahui.datasphere.mdm.system.service.RenderingService;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.mdm.system.type.module.Module;
import com.huahui.datasphere.mdm.system.util.ContextUtils;
import com.huahui.datasphere.mdm.system.util.DataSourceUtils;
import com.huahui.datasphere.mdm.system.util.IdUtils;
import com.huahui.datasphere.mdm.system.util.PipelineUtils;
import com.huahui.datasphere.mdm.system.util.ResourceUtils;
import com.huahui.datasphere.mdm.system.util.TextUtils;

/**
 * @author theseusyang
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
    public static final String MODULE_ID = "com.huahui.datasphere.mdm.system";

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
        return "Datasphere system";
    }

    @Override
    public String getDescription() {
        return "Datasphere system (root) module";
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
