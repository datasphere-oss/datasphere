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

package org.datasphere.mdm.core.configuration;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.datasphere.mdm.core.convert.InstantPropertyEditor;
import org.datasphere.mdm.core.convert.LocalDatePropertyEditor;
import org.datasphere.mdm.core.convert.LocalDateTimePropertyEditor;
import org.datasphere.mdm.core.convert.LocalTimePropertyEditor;
import org.datasphere.mdm.core.convert.job.JobParameterEditorRegistrar;
import org.datasphere.mdm.core.service.impl.job.CleanUnusedBinariesJob;
import org.datasphere.mdm.core.service.impl.job.CustomCronTriggerBeanPostProcessor;
import org.datasphere.mdm.core.service.impl.job.CustomJobExplorerFactoryBean;
import org.datasphere.mdm.core.service.impl.job.CustomJobRegistryBeanPostProcessor;
import org.datasphere.mdm.core.service.impl.job.CustomJobRegistryImpl;
import org.datasphere.mdm.core.service.impl.job.CustomJobRepositoryFactoryBean;
import org.datasphere.mdm.core.service.impl.job.CustomSchedulerFactoryBean;
import org.datasphere.mdm.core.service.impl.job.JobParameterFactory;
import org.datasphere.mdm.core.service.job.CustomJobRegistry;
import org.quartz.JobDetail;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.JobScope;
import org.springframework.batch.core.scope.StepScope;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.datasphere.mdm.system.configuration.ModuleConfiguration;
import org.datasphere.mdm.system.util.DataSourceUtils;

/**
 * @author Alexander Malyshev
 */
@Configuration
@EnableTransactionManagement
public class CoreConfiguration extends ModuleConfiguration {
    /**
     * This configuration ID.
     */
    public static final ConfigurationId ID = () -> "CORE_CONFIGURATION";

    public CoreConfiguration() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigurationId getId() {
        return ID;
    }

    @Bean(name = "coreDataSource")
    public DataSource coreDataSource() {
    	Properties properties = getPropertiesByPrefix(CoreConfigurationConstants.PROPERTY_CORE_DATASOURCE_PREFIX, true);
    	return DataSourceUtils.newPoolingXADataSource(properties);
    }

    @Bean("binary-data-sql")
    public PropertiesFactoryBean binaryDataSql() {
        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/db/binary-data-sql.xml"));
        return propertiesFactoryBean;
    }

    @Bean("security-sql")
    public PropertiesFactoryBean securitySql() {
        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/db/security-sql.xml"));
        return propertiesFactoryBean;
    }

    @Bean
    public ProviderManager authenticationManager(final AuthenticationProvider authenticationProvider) {
        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }

    /**
     * use this way, not     @PropertySource(name = "securitySql", value = "classpath:db/security-sql.xml" )
     * for old functionality support (like @Qualifier("security-sql") final Properties sql)
     * and for pleasure while autoset sql query by org.datasphere.mdm.core.dao.@SqlQuery
     *
     * @return
     */
    @Bean("securitySql")
    public PropertiesFactoryBean securitySqlProperties() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("db/security-sql.xml"));
        return bean;
    }

    @Bean("job-definitions-sql")
    public PropertiesFactoryBean jobDefinitionsProperties() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("db/job-definitions-sql.xml"));
        return bean;
    }

    @Bean("job-executions-sql")
    public PropertiesFactoryBean jobExecutionsProperties() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("db/job-executions-sql.xml"));
        return bean;
    }

    @Bean("configuration-sql")
    public PropertiesFactoryBean configurationSqlProperties() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("db/configuration-sql.xml"));
        return bean;
    }

    @Bean("custom-storage-sql")
    public PropertiesFactoryBean customStorageSqlProperties() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("db/custom-storage-sql.xml"));
        return bean;
    }

    @Bean("audit-sql")
    public PropertiesFactoryBean auditSqlProperties() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("db/audit-sql.xml"));
        return bean;
    }

    @Bean("binary-data-sql")
    public PropertiesFactoryBean binaryDataSqlProperties() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("db/binary-data-sql.xml"));
        return bean;
    }

    @Bean("storage-model-sql")
    public PropertiesFactoryBean storageSql() {
        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/db/storage-model-sql.xml"));
        return propertiesFactoryBean;
    }

    @Bean("libraries-sql")
    public PropertiesFactoryBean librariesSqlProperties() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("db/libraries-sql.xml"));
        return bean;
    }
    // Jobs
    @Bean
    public CustomCronTriggerBeanPostProcessor cronTriggerBeanPostProcessor() {
        return new CustomCronTriggerBeanPostProcessor();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(
            @Autowired CustomCronTriggerBeanPostProcessor cronTriggerBeanPostProcessor,
            @Autowired @Qualifier("coreDataSource") DataSource coreDataSource,
            @Autowired PlatformTransactionManager transactionManager) {

        Properties qp = new Properties();
        qp.putAll(
            Map.of( "org.quartz.jobStore.isClustered", "true",
                    "org.quartz.jobStore.useProperties", "true",
                    "org.quartz.jobStore.tablePrefix", CoreConfigurationConstants.CORE_SCHEMA_NAME + ".qrtz_",
                    "org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate",
                    "org.quartz.scheduler.instanceId", "AUTO"));

        CustomSchedulerFactoryBean bean = new CustomSchedulerFactoryBean();
        bean.setDataSource(coreDataSource);
        bean.setTransactionManager(transactionManager);
        bean.setOverwriteExistingJobs(true);
        bean.setStartupDelay(30);
        bean.setAutoStartup(false);
        bean.setQuartzProperties(qp);
        bean.setCollector(cronTriggerBeanPostProcessor);

        return bean;
    }

    @Bean("jobParameterEditorConfigurer")
    public CustomEditorConfigurer jobParameterEditorConfigurer(@Autowired JobParameterFactory jobParameterFactory) {

        PropertyEditorRegistrar[] registrars = {
                new JobParameterEditorRegistrar(jobParameterFactory)
        };

        CustomEditorConfigurer configurer = new CustomEditorConfigurer();
        configurer.setPropertyEditorRegistrars(registrars);
        configurer.setCustomEditors(Map.of(
                Instant.class, InstantPropertyEditor.class,
                LocalDate.class, LocalDatePropertyEditor.class,
                LocalTime.class, LocalTimePropertyEditor.class,
                LocalDateTime.class, LocalDateTimePropertyEditor.class));

        return configurer;
    }

    @Bean
    public JobParameterFactory jobParameterFactory(@Autowired CustomJobRegistry jobRegistry) {
        return new JobParameterFactory(jobRegistry);
    }
    /**
     * The a job registry.
     * @return the job registry
     */
    @Bean
    public CustomJobRegistry jobRegistry() {
        return new CustomJobRegistryImpl();
    }
    // SB section
    /**
     * Creates a job repository.
     * @return a job repository
     * @throws Exception
     */
    @Bean
    public JobRepository jobRepository(
            @Autowired @Qualifier("coreDataSource") DataSource coreDataSource,
            @Autowired PlatformTransactionManager transactionManager) throws Exception {
        final CustomJobRepositoryFactoryBean customJobRepositoryFactoryBean = new CustomJobRepositoryFactoryBean();
        customJobRepositoryFactoryBean.setDataSource(coreDataSource);
        customJobRepositoryFactoryBean.setTransactionManager(transactionManager);
        customJobRepositoryFactoryBean.setDatabaseType("postgres");
        customJobRepositoryFactoryBean.setTablePrefix(CoreConfigurationConstants.CORE_BATCH_TABLE_PREFIX);
        return customJobRepositoryFactoryBean.getObject();
    }
    /**
     * Creates a job explorer.
     * @return a job explorer
     * @throws Exception
     */
    @Bean
    public JobExplorer jobExplorer(
            @Autowired @Qualifier("coreDataSource") DataSource coreDataSource,
            @Autowired PlatformTransactionManager transactionManager) throws Exception {
        final CustomJobExplorerFactoryBean customJobExplorerFactoryBean = new CustomJobExplorerFactoryBean();
        customJobExplorerFactoryBean.setDataSource(coreDataSource);
        customJobExplorerFactoryBean.setTransactionManager(transactionManager);
        customJobExplorerFactoryBean.setTablePrefix(CoreConfigurationConstants.CORE_BATCH_TABLE_PREFIX);
        return customJobExplorerFactoryBean.getObject();
    }
    /**
     * Custom job beans post-processor.
     * @param jobRegistry the custom registry
     * @param jobGroupName the groupd name, if defined
     * @return BPP
     */
    @Bean
    public CustomJobRegistryBeanPostProcessor jobBeansPostProcessor(
            @Autowired CustomJobRegistry jobRegistry) {
        CustomJobRegistryBeanPostProcessor cjpp = new CustomJobRegistryBeanPostProcessor();
        cjpp.setJobRegistry(jobRegistry);
        return cjpp;
    }
    /**
     * Thread pool for launching jobs.
     * @param coreJobPoolSize start size param
     * @param maxJobPoolSize max size param
     * @param jobQueueCapacity queue capacity param
     * @return thread pool executor
     */
    @Bean
    public ThreadPoolTaskExecutor jobThreadPoolTaskExecutor(
            @Value("${" + CoreConfigurationConstants.PROPERTY_JOB_MIN_THREAD_POOL_SIZE + ":4}") final int coreJobPoolSize,
            @Value("${" + CoreConfigurationConstants.PROPERTY_JOB_MAX_THREAD_POOL_SIZE + ":32}") final int maxJobPoolSize,
            @Value("${" + CoreConfigurationConstants.PROPERTY_JOB_QUEUE_SIZE + ":100}") final int jobQueueCapacity) {

        final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(coreJobPoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxJobPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(jobQueueCapacity);

        return threadPoolTaskExecutor;
    }
    /**
     * Creates job luncher.
     * @param jobRepository the job repository
     * @return luncher
     */
    @Bean
    public JobLauncher jobLauncher(
            @Autowired final JobRepository jobRepository,
            @Autowired final ThreadPoolTaskExecutor jobThreadPoolTaskExecutor) {

        final SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        simpleJobLauncher.setTaskExecutor(jobThreadPoolTaskExecutor);

        return simpleJobLauncher;
    }

    @Bean
    public JobOperator jobOperator(
            @Autowired JobRepository jobRepository,
            @Autowired JobExplorer jobExplorer,
            @Autowired JobRegistry jobRegistry,
            @Autowired JobLauncher jobLauncher) {
        SimpleJobOperator sjo = new SimpleJobOperator();
        sjo.setJobExplorer(jobExplorer);
        sjo.setJobRepository(jobRepository);
        sjo.setJobRegistry(jobRegistry);
        sjo.setJobLauncher(jobLauncher);
        return sjo;
    }
    /**
     * Provide job builders, since we do not import @EnableBatchProcessing
     * @param jobRepository the repository
     * @return Job factory
     * @throws Exception
     */
    @Bean
    public JobBuilderFactory jobBuilders(@Autowired final JobRepository jobRepository) {
        return new JobBuilderFactory(jobRepository);
    }
    /**
     * Provide step builders, since we do not import @EnableBatchProcessing
     * @param jobRepository the repository
     * @return Step factory
     * @throws Exception
     */
    @Bean
    public StepBuilderFactory stepBuilders(
            @Autowired final JobRepository jobRepository,
            @Autowired final PlatformTransactionManager transactionManager) {
        return new StepBuilderFactory(jobRepository, transactionManager);
    }
    // System jobs
    /**
     * CUB job details.
     * @return CUB job details
     */
    @Bean
    public JobDetailFactoryBean cleanUnusedBinariesJobDetail() {

        JobDetailFactoryBean jdfb = new JobDetailFactoryBean();

        jdfb.setJobClass(CleanUnusedBinariesJob.class);
        jdfb.setName(CoreConfigurationConstants.CORE_JOB_CLEAN_UNUSED_LOBS);
        jdfb.setGroup(CoreConfigurationConstants.CORE_JOB_GROUP_NAME);
        jdfb.setDurability(true);

        return jdfb;
    }
    /**
     * CUB job trigger.
     * @return CUB job trigger
     */
    @Bean
    public CronTriggerFactoryBean cleanUnusedBinaryDataJobTrigger(
            JobDetail cleanUnusedBinariesJobDetail,
            @Value("${" + CoreConfigurationConstants.PROPERTY_JOB_CLEAN_BINARIES_CRONEX + ":0 0 0/1 * * ?}") String cronExpression) {

        CronTriggerFactoryBean ctfb = new CronTriggerFactoryBean();

        ctfb.setJobDetail(cleanUnusedBinariesJobDetail);
        ctfb.setGroup(CoreConfigurationConstants.CORE_JOB_GROUP_NAME);
        ctfb.setCronExpression(cronExpression);

        return ctfb;
    }

    // Have to do this in the same fashion, as done in
    // AbstractBatchConfiguration.class, since we do not use it directly
    @Bean
    public static StepScope stepScope() {
        StepScope stepScope = new StepScope();
        stepScope.setAutoProxy(false);
        return stepScope;
    }

    @Bean
    public static JobScope jobScope() {
        JobScope jobScope = new JobScope();
        jobScope.setAutoProxy(false);
        return jobScope;
    }
}
