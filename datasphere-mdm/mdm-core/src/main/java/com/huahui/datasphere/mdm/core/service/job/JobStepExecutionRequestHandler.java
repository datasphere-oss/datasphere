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

/**
 * Date: 25.02.2016
 */

package com.huahui.datasphere.mdm.core.service.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.StepLocator;
import org.springframework.batch.integration.partition.StepExecutionRequest;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ITopic;
import com.huahui.datasphere.mdm.core.service.job.step.JobQueueStepReader;
import com.huahui.datasphere.mdm.core.service.job.step.JobTopicStepReader;

/**
 * Step execution receiving bean. Created on per job basis.
 *
 * @author amagdenko
 */
public class JobStepExecutionRequestHandler implements InitializingBean, DisposableBean, ApplicationContextAware {
    /**
     * This logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JobStepExecutionRequestHandler.class);
    /**
     * Will be taken for worker thread count, if nothing is specified in the job config.
     */
    private static final int DEFAULT_THREAD_COUNT = 1;
    /**
     * Will be taken for termination timeout, if nothing is specified in the job config.
     */
    private static final int DEFAULT_AWAIT_TERMINATION_IN_SECONDS = 10;
    /**
     * Job explorer instance.
     */
    private JobExplorer jobExplorer;
    /**
     * Job repository instance.
     */
    private JobRepository jobRepository;
    /**
     * Step locator instance.
     */
    private StepLocator stepLocator;
    /**
     * The queue to use.
     */
    private IQueue<Message<StepExecutionRequest>> queue;
    /**
     * The queue to use.
     */
    private ITopic<Message<StepExecutionRequest>> topic;
    /**
     * Workrer pool size (number of simultaneously active worker threads in a pool).
     */
    private int threadCount = DEFAULT_THREAD_COUNT;
    /**
     * Await termination in seconds.
     */
    private int awaitTerminationInSeconds = DEFAULT_AWAIT_TERMINATION_IN_SECONDS;
    /**
     * Job name.
     */
    private String jobName;
    /**
     * Step reader instance.
     */
    private JobQueueStepReader queueStepReader;
    /**
     * Step reader instance.
     */
    private JobTopicStepReader topicStepReader;
    /**
     * HZ instance.
     */
    private HazelcastInstance hazelcastInstance;
    /**
     * Used to locate a {@link Step} to execute for each request.
     *
     * @param stepLocator a {@link StepLocator}
     */
    public void setStepLocator(StepLocator stepLocator) {
        this.stepLocator = stepLocator;
    }
    /**
     * @return the stepLocator
     */
    public StepLocator getStepLocator() {
        return stepLocator;
    }
    /**
     * An explorer that should be used to check for {@link StepExecution} completion.
     *
     * @param jobExplorer a {@link JobExplorer} that is linked to the shared repository used by all remote workers.
     */
    public void setJobExplorer(JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }
    /**
     * @return the jobExplorer
     */
    public JobExplorer getJobExplorer() {
        return jobExplorer;
    }
    /**
     * Sets the job reporsitory instance to use.
     *
     * @param jobRepository the jobRepository to set
     */
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
    /**
     * @return the jobRepository
     */
    public JobRepository getJobRepository() {
        return jobRepository;
    }
    /**
     * The job tag.
     *
     * @param jobName the jobName to set
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    /**
     * Job name getter.
     *
     * @return the jobName
     */
    public String getJobName() {
        return jobName == null ? "UNKNOWN" : jobName;
    }
    /**
     * Queue instance setter.
     *
     * @param queue the queue to set
     */
    public void setQueue(IQueue<Message<StepExecutionRequest>> queue) {
        this.queue = queue;
    }
    /**
     * @return the queue
     */
    public IQueue<Message<StepExecutionRequest>> getQueue() {
        return queue;
    }
    /**
     * Queue instance setter.
     *
     * @param topic the queue to set
     */
    public void setTopic(ITopic<Message<StepExecutionRequest>> topic) {
        this.topic = topic;
    }
    /**
     * @return the queue
     */
    public ITopic<Message<StepExecutionRequest>> getTopic() {
        return topic;
    }
    /**
     * Thread count setter.
     *
     * @param threadCount the thread count
     */
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }
    /**
     * @return the threadCount
     */
    public int getThreadCount() {
        return threadCount;
    }
    /**
     * Await termination timeout setter.
     * @param awaitTerminationInSeconds the timeout
     */
    public void setAwaitTerminationInSeconds(int awaitTerminationInSeconds) {
        this.awaitTerminationInSeconds = awaitTerminationInSeconds;
    }
    /**
     * @return the awaitTerminationInMinutes
     */
    public int getAwaitTerminationInSeconds() {
        return awaitTerminationInSeconds;
    }
    /**
     * @return the hazelcastInstance
     */
    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        Assert.isTrue(queue != null || topic != null, "Both Topic and Queue cannot be null");
        Assert.isTrue(threadCount > 0, "Thread count must be positive");

        if (queue != null) {
            queueStepReader = new JobQueueStepReader(this);
        }

        if (topic != null) {
            topicStepReader = new JobTopicStepReader(this);
        }

        LOGGER.info("Step excution handler startup for job [{}] complete.", getJobName());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() throws Exception {

        LOGGER.info("Step excution handler shutdown for job [{}] requested.", getJobName());
        if (queueStepReader != null) {
            queueStepReader.shutdown();
        }

        if (topicStepReader != null) {
            topicStepReader.shutdown();
        }

        LOGGER.info("Step excution handler shutdown for job [{}] done.", getJobName());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        hazelcastInstance = applicationContext.getBean(HazelcastInstance.class);
    }

}
