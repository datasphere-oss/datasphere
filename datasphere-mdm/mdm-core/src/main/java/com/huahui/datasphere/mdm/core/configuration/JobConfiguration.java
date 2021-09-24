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

package com.huahui.datasphere.mdm.core.configuration;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.huahui.datasphere.mdm.core.configuration.job.CustomJobExecutionContextSerializer;
import com.huahui.datasphere.mdm.core.configuration.job.CustomJobExplorerFactoryBean;
import com.huahui.datasphere.mdm.core.configuration.job.CustomJobRegistryBeanPostProcessor;
import com.huahui.datasphere.mdm.core.configuration.job.CustomJobRepositoryFactoryBean;
import com.huahui.datasphere.mdm.core.configuration.job.JobWithParamsRegistryImpl;
import com.huahui.datasphere.mdm.core.configuration.job.UnidataSchedulerFactoryBean;
import com.huahui.datasphere.mdm.core.service.impl.job.CleanUnusedBinariesJob;
import com.huahui.datasphere.mdm.core.service.job.JobParameterProcessor;
import com.huahui.datasphere.mdm.core.service.job.JobWithParamsRegistry;

/**
 * @author theseusyang
 */
@Configuration
public class JobConfiguration {
    /**
     * Custom table prefix.
     */
    public static final String UNIDATA_BATCH_JOB_BATCH_TABLE_PREFIX = "BATCH_";
    /**
     * TX manager.
     */
    @Autowired
    private PlatformTransactionManager transactionManager;
    /**
     * The 'core' datasource.
     */
    @Autowired
    @Qualifier("coreDataSource")
    private DataSource coreDataSource;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(CronTrigger cleanUnusedBinaryDataJobTrigger) {

        Properties qp = new Properties();
        qp.putAll(
            Map.of( "org.quartz.jobStore.isClustered", "true",
                    "org.quartz.jobStore.useProperties", "true",
                    "org.quartz.jobStore.tablePrefix", CoreConfigurationConstants.CORE_SCHEMA_NAME + ".qrtz_",
                    "org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate",
                    "org.quartz.scheduler.instanceId", "AUTO"));

        UnidataSchedulerFactoryBean bean = new UnidataSchedulerFactoryBean();
        bean.setDataSource(coreDataSource);
        bean.setTransactionManager(transactionManager);
        bean.setOverwriteExistingJobs(true);
        bean.setStartupDelay(30);
        bean.setAutoStartup(false);
        bean.setQuartzProperties(qp);
        bean.setTriggers(cleanUnusedBinaryDataJobTrigger);

        return bean;
    }

    // SB section
    @Bean
    public CustomJobExecutionContextSerializer customJobExecutionContextSerializer() {
        return new CustomJobExecutionContextSerializer();
    }

    @Bean
    public MapFactoryBean jobParameterProcessorsMap() {
        final MapFactoryBean mapFactoryBean = new MapFactoryBean();
        mapFactoryBean.setSourceMap(new ConcurrentHashMap<>());
        mapFactoryBean.setTargetMapClass(ConcurrentHashMap.class);
        return mapFactoryBean;
    }
    /**
     * Creates a job repository.
     * @return a job repository
     * @throws Exception
     */
    @Bean
    public JobRepository jobRepository(CustomJobExecutionContextSerializer customJobExecutionContextSerializer) throws Exception {
        final CustomJobRepositoryFactoryBean customJobRepositoryFactoryBean = new CustomJobRepositoryFactoryBean();
        customJobRepositoryFactoryBean.setDataSource(coreDataSource);
        customJobRepositoryFactoryBean.setTransactionManager(transactionManager);
        customJobRepositoryFactoryBean.setDatabaseType("postgres");
        customJobRepositoryFactoryBean.setTablePrefix(UNIDATA_BATCH_JOB_BATCH_TABLE_PREFIX);
        customJobRepositoryFactoryBean.setSerializer(customJobExecutionContextSerializer);
        return customJobRepositoryFactoryBean.getObject();
    }
    /**
     * Creates a job explorer.
     * @return a job explorer
     * @throws Exception
     */
    @Bean
    public JobExplorer jobExplorer(CustomJobExecutionContextSerializer customJobExecutionContextSerializer) throws Exception {
        final CustomJobExplorerFactoryBean customJobExplorerFactoryBean = new CustomJobExplorerFactoryBean();
        customJobExplorerFactoryBean.setDataSource(coreDataSource);
        customJobExplorerFactoryBean.setTransactionManager(transactionManager);
        customJobExplorerFactoryBean.setTablePrefix(UNIDATA_BATCH_JOB_BATCH_TABLE_PREFIX);
        customJobExplorerFactoryBean.setSerializer(customJobExecutionContextSerializer);
        return customJobExplorerFactoryBean.getObject();
    }
    /**
     * The a job registry.
     * @return the job registry
     */
    @Bean
    public JobWithParamsRegistry jobRegistry(@Autowired Map<String, JobParameterProcessor> jobParameterProcessorsMap) {
        final JobWithParamsRegistryImpl jobWithParamsRegistry = new JobWithParamsRegistryImpl();
        jobWithParamsRegistry.setJobParameterProcessorsMap(jobParameterProcessorsMap);
        return jobWithParamsRegistry;
    }
    /**
     * Custom job beans post-processor.
     * @param jobRegistry the custom registry
     * @param jobGroupName the groupd name, if defined
     * @return BPP
     */
    @Bean
    public BeanPostProcessor jobBeansPostProcessor(
            @Autowired JobWithParamsRegistry jobRegistry,
            @Autowired(required = false) String jobGroupName) {
        CustomJobRegistryBeanPostProcessor cjpp = new CustomJobRegistryBeanPostProcessor();
        cjpp.setJobWithParamsRegistry(jobRegistry);
        cjpp.setGroupName(jobGroupName);
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
            @Autowired JobWithParamsRegistry jobRegistry,
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
    public StepBuilderFactory stepBuilders(@Autowired final JobRepository jobRepository) {
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
