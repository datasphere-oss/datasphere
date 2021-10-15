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

package org.datasphere.mdm.core.service.job.step;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.service.job.JobStepExecutionRequestHandler;
import org.datasphere.mdm.core.util.JobUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.integration.partition.StepExecutionRequest;
import org.springframework.messaging.Message;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import com.hazelcast.cluster.Member;
import com.hazelcast.topic.MessageListener;

/**
 * @author Mikhail Mikhailov
 * Hazelcast step reader.
 * Creates fixed single thread pool to take messages from a hazelcast distributed partitioning queue.
 * Submits valid messages to {@link JobStepSubmitter} instances, which are hold in a separate pool of configured length.
 */
public class JobTopicStepReader implements MessageListener<Message<StepExecutionRequest>> {
    /**
     * This logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JobTopicStepReader.class);
    /**
     * Worker threads pool executor.
     */
    private final ThreadPoolExecutor workerExecutor;
    /**
     * Step handler bean instance.
     */
    private final JobStepExecutionRequestHandler handler;
    /**
     * True if this step handler has been canceled.
     */
    private volatile boolean canceled = false;
    /**
     * Constructor.
     * @param threadCount the number of threads to initialize for request processing.
     * @param awaitTerminationInMinutes wait that number of minutes for pool shutdown
     */
    public JobTopicStepReader(JobStepExecutionRequestHandler handler) {

        super();
        this.handler = handler;
        this.handler.getTopic().addMessageListener(this);

        // 1. Create pool for workers.
        workerExecutor = new ThreadPoolExecutor(handler.getThreadCount(), handler.getThreadCount(), 60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new CustomizableThreadFactory(handler.getJobName() + "-HazelcastStepWorker-"));

        LOGGER.info("'{}' job step executor initialized successfully.", handler.getJobName());
    }

    /**
     * Does hierarhical shutdown.
     */
    public void shutdown() {

        // Set state
        canceled = true;
        try {

            LOGGER.debug("Attempting worker pool shutdown for job [{}]", handler.getJobName());

            workerExecutor.shutdown();
            boolean success = workerExecutor.awaitTermination(handler.getAwaitTerminationInSeconds(), TimeUnit.SECONDS);
            if (!success) {
                LOGGER.debug("Graceful worker pool shutdown with timeout of {} minutes unsuccessful. Force termination.", handler.getAwaitTerminationInSeconds());
                workerExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Failed to shutdown worker executor properly", e);
        } finally {
            LOGGER.info("Worker executor shutdown complete for step handler [{}].", handler.getJobName());
        }

        LOGGER.info("End cleaning distributed job resources for job [{}].", handler.getJobName());
    }

    @Override
    public void onMessage(com.hazelcast.topic.Message<Message<StepExecutionRequest>> message) {

        if (canceled) {
            LOGGER.info("Job step reader [job={}] already canceled. Exiting.", handler.getJobName());
            return;
        }

        Member sender = message.getPublishingMember();
        long ts = message.getPublishTime();

        Message<StepExecutionRequest> payload = message.getMessageObject();

        Member localMember = handler.getHazelcastInstance().getCluster().getLocalMember();
        String targetUuid = (String) payload.getHeaders().get(JobUtils.JOB_CLUSTER_MEMBER_UUID);

        if (StringUtils.equals(localMember.getUuid().toString(), targetUuid)) {
            LOGGER.info("Processing targeted step execution request to {} from {} sent on {}.", targetUuid, sender.getAddress(), new Date(ts));
            workerExecutor.submit(new JobStepSubmitter(payload, handler, canceled));
        } else {
            LOGGER.info("Discarding targeted step execution request to {} from {} sent on {}.", targetUuid, sender.getAddress(), new Date(ts));
        }
    }
}
