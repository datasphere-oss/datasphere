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

package org.datasphere.mdm.core.module;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.configuration.CoreConfiguration;
import org.datasphere.mdm.core.configuration.CoreConfigurationConstants;
import org.datasphere.mdm.core.configuration.CoreConfigurationProperty;
import org.datasphere.mdm.core.configuration.CoreMessagingDomain;
import org.datasphere.mdm.core.migration.CoreSchemaMigrations;
import org.datasphere.mdm.core.migration.UninstallCoreSchemaMigrations;
import org.datasphere.mdm.core.service.AuditStorageService;
import org.datasphere.mdm.core.service.RoleService;
import org.datasphere.mdm.core.service.impl.AsyncRareTaskExecutor;
import org.datasphere.mdm.core.service.impl.job.CustomCronTriggerBeanPostProcessor;
import org.datasphere.mdm.core.service.impl.job.CustomJobRegistryBeanPostProcessor;
import org.datasphere.mdm.core.util.JobUtils;
import org.datasphere.mdm.core.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.JobScope;
import org.springframework.batch.core.scope.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.datasphere.mdm.system.configuration.SystemConfigurationConstants;
import org.datasphere.mdm.system.context.DatabaseMigrationContext;
import org.datasphere.mdm.system.service.DatabaseMigrationService;
import org.datasphere.mdm.system.service.ModularPostProcessingRegistrar;
import org.datasphere.mdm.system.type.annotation.ConfigurationRef;
import org.datasphere.mdm.system.type.configuration.ConfigurationProperty;
import org.datasphere.mdm.system.type.configuration.ConfigurationValue;
import org.datasphere.mdm.system.type.messaging.DomainType;
import org.datasphere.mdm.system.type.module.AbstractModule;
import org.datasphere.mdm.system.type.module.Dependency;
import org.datasphere.mdm.system.type.pipeline.Segment;
import org.datasphere.mdm.system.util.DataSourceUtils;
import org.datasphere.mdm.system.util.JsonUtils;

public class CoreModule extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreModule.class);
    /**
     * This module id.
     */
    public static final String MODULE_ID = "org.datasphere.mdm.core";

    private static final List<Dependency> DEPENDENCIES = Arrays.asList(
            new Dependency("org.datasphere.mdm.system", "6.0"),
            new Dependency("org.datasphere.mdm.search", "6.0")
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
    private CustomCronTriggerBeanPostProcessor customCronTriggerBeanPostProcessor;

    @Autowired
    @Qualifier("jobParameterEditorConfigurer")
    private CustomEditorConfigurer jobParameterEditorConfigurer;

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
    private CoreConfiguration coreConfiguration;
    /**
     * Whether we run in developer mode.
     */
    @ConfigurationRef(SystemConfigurationConstants.PROPERTY_DEVELOPER_MODE)
    private ConfigurationValue<Boolean> developerMode;

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
        JobUtils.init();

        if (developerMode.getValue() && !install) {
            migrate();
        }

        // Prepare audit services
        if (CollectionUtils.isNotEmpty(auditStorageServices)) {
            auditStorageServices.forEach(AuditStorageService::prepare);
        }

        // Register pre/post-processors
        modularPostProcessingRegistrar.registerBeanPostProcessor(customJobRegistryBeanPostProcessor);
        modularPostProcessingRegistrar.registerBeanPostProcessor(customCronTriggerBeanPostProcessor);

        modularPostProcessingRegistrar.registerBeanFactoryPostProcessor(jobScope);
        modularPostProcessingRegistrar.registerBeanFactoryPostProcessor(stepScope);
        modularPostProcessingRegistrar.registerBeanFactoryPostProcessor(jobParameterEditorConfigurer);

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
