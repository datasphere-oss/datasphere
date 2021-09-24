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

package com.huahui.datasphere.mdm.core.module;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.JobScope;
import org.springframework.batch.core.scope.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import com.huahui.datasphere.mdm.system.context.DatabaseMigrationContext;
import com.huahui.datasphere.mdm.system.service.DatabaseMigrationService;
import com.huahui.datasphere.mdm.system.service.ModularPostProcessingRegistrar;
import com.huahui.datasphere.mdm.system.service.PlatformConfiguration;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.mdm.system.type.messaging.DomainType;
import com.huahui.datasphere.mdm.system.type.module.AbstractModule;
import com.huahui.datasphere.mdm.system.type.module.Dependency;
import com.huahui.datasphere.mdm.system.type.pipeline.Segment;
import com.huahui.datasphere.mdm.system.util.DataSourceUtils;
import com.huahui.datasphere.mdm.system.util.JsonUtils;

import com.huahui.datasphere.mdm.core.configuration.CoreConfiguration;
import com.huahui.datasphere.mdm.core.configuration.CoreConfigurationConstants;
import com.huahui.datasphere.mdm.core.configuration.CoreConfigurationProperty;
import com.huahui.datasphere.mdm.core.configuration.CoreMessagingDomain;
import com.huahui.datasphere.mdm.core.configuration.job.CustomJobRegistryBeanPostProcessor;
import com.huahui.datasphere.mdm.core.migration.CoreSchemaMigrations;
import com.huahui.datasphere.mdm.core.migration.UninstallCoreSchemaMigrations;
import com.huahui.datasphere.mdm.core.service.AuditStorageService;
import com.huahui.datasphere.mdm.core.service.RoleService;
import com.huahui.datasphere.mdm.core.service.impl.AsyncRareTaskExecutor;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;

public class CoreModule extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreModule.class);
    /**
     * This module id.
     */
    public static final String MODULE_ID = "com.huahui.datasphere.mdm.core";

    private static final List<Dependency> DEPENDENCIES = Arrays.asList(
            new Dependency("com.huahui.datasphere.mdm.system", "6.0"),
            new Dependency("com.huahui.datasphere.mdm.search", "6.0")
    );

    @Autowired
    private List<AuditStorageService> auditStorageServices;

    @Autowired
    private DataSource coreDataSource;

    @Autowired
    private AsyncRareTaskExecutor asyncRareTaskExecutor;

    @Autowired
    private ThreadPoolTaskExecutor jobThreadPoolTaskExecutor;

    @Autowired
    private SchedulerFactoryBean quartzSchedulerFactory;

    @Autowired
    private CustomJobRegistryBeanPostProcessor customJobRegistryBeanPostProcessor;

    @Autowired
    private JobScope jobScope;

    @Autowired
    private StepScope stepScope;

    @Autowired
    private ModularPostProcessingRegistrar modularPostProcessingRegistrar;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DatabaseMigrationService migrationService;

    @Autowired
    private PlatformConfiguration platformConfiguration;

    @Autowired
    private CoreConfiguration coreConfiguration;

    private boolean install;

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
        return "Unidata core";
    }

    @Override
    public String getDescription() {
        return "Unidata core module";
    }

    @Override
    public Collection<Dependency> getDependencies() {
        return DEPENDENCIES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getResourceBundleBasenames() {
        return new String[]{ "core_messages" };
    }

    @Override
    public ConfigurationProperty<?>[] getConfigurationProperties() {
        return CoreConfigurationProperty.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainType[] getMessagingDomains() {
        return new DomainType[]{ CoreMessagingDomain.DOMAIN };
    }

    @Override
    public void install() {
        LOGGER.info("Install");
        migrate();
        install = true;
    }

    @Override
    public void uninstall() {
        LOGGER.info("Uninstall");
        migrationService.migrate(DatabaseMigrationContext.builder()
                .schemaName(CoreConfigurationConstants.CORE_SCHEMA_NAME)
                .logName(CoreConfigurationConstants.CORE_LOG_NAME)
                .dataSource(coreDataSource)
                .migrations(UninstallCoreSchemaMigrations.migrations())
                .build());
    }

    @Override
    public void start() {
        LOGGER.info("Starting...");

        SecurityUtils.init();
        JsonUtils.init();

        if (platformConfiguration.isDeveloperMode() && !install) {
            migrate();
        }

        // Prepare audit services
        if (CollectionUtils.isNotEmpty(auditStorageServices)) {
            auditStorageServices.forEach(AuditStorageService::prepare);
        }

        // Register pre/post-processors
        modularPostProcessingRegistrar.registerBeanPostProcessor(customJobRegistryBeanPostProcessor);

        modularPostProcessingRegistrar.registerBeanFactoryPostProcessor(jobScope);
        modularPostProcessingRegistrar.registerBeanFactoryPostProcessor(stepScope);

        roleService.init();

        addSegments(coreConfiguration.getBeansOfType(Segment.class).values());

        LOGGER.info("Started");
    }

    @Override
    public void stop() {
        LOGGER.info("Stopping...");
        asyncRareTaskExecutor.shutdown();
        jobThreadPoolTaskExecutor.shutdown();
        DataSourceUtils.shutdown(coreDataSource);

        try {
            quartzSchedulerFactory.destroy();
        } catch (Exception e) {
            LOGGER.warn("Scheduler was not shut down properly.", e);
        }

        LOGGER.info("Stopped.");
    }

    private void migrate() {
        migrationService.migrate(DatabaseMigrationContext.builder()
                .schemaName(CoreConfigurationConstants.CORE_SCHEMA_NAME)
                .logName(CoreConfigurationConstants.CORE_LOG_NAME)
                .dataSource(coreDataSource)
                .migrations(CoreSchemaMigrations.migrations())
                .build());
    }
}
