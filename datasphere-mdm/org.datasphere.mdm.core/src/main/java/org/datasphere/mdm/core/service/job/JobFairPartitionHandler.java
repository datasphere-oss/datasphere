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

package org.datasphere.mdm.core.service.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.exception.JobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.StepExecutionSplitter;
import org.springframework.batch.integration.partition.StepExecutionRequest;
import org.springframework.batch.poller.DirectPoller;
import org.springframework.batch.poller.Poller;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.annotation.Payloads;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Sends step requests (partitions)  to workers via Hazelcast transport.
 *
 * @author amagdenko
 */
public class JobFairPartitionHandler implements PartitionHandler, InitializingBean {
    /**
     * This logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JobFairPartitionHandler.class);
    /**
     * Some magic from SB.
     */
    private int gridSize = 1;
    /**
     * BQueue for sending partitions to.
     */
    private BlockingQueue<Message<StepExecutionRequest>> messagingGateway;
    /**
     * Name of the step, which will be used to provess partitions.
     */
    private String stepName;
    /**
     * Poll interval to poll partitions for completion in millis.
     */
    private long pollInterval = 10000;
    /**
     * The job explorer to use. If null, one will be created.
     */
    private JobExplorer jobExplorer;
    /**
     * Timeout to wait before fail.
     */
    private long timeout = -1;
    /**
     * Data source to use.
     */
    private DataSource dataSource;
    /**
     * Run partitions grouped.
     */
    private boolean grouped;
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        Assert.notNull(stepName, "A step name must be provided for the remote workers.");
        Assert.state(messagingGateway != null, "The MessagingOperations must be set.");
        Assert.state(!(dataSource == null && jobExplorer == null), "Both dataSource and jobExplorer are null. Handler is misconfigured.");

        if (dataSource != null && jobExplorer == null) {
            JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
            jobExplorerFactoryBean.setDataSource(dataSource);
            jobExplorerFactoryBean.afterPropertiesSet();
            jobExplorer = jobExplorerFactoryBean.getObject();
        }
    }

    /**
     * When using job repository polling, the time limit to wait.
     *
     * @param timeout millisconds to wait, defaults to -1 (no timeout).
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * {@link org.springframework.batch.core.explore.JobExplorer} to use to query the job repository.  Either this or a
     * {@link javax.sql.DataSource} is required when using job repository polling.
     *
     * @param jobExplorer {@link org.springframework.batch.core.explore.JobExplorer} to use for lookups
     */
    public void setJobExplorer(JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }

    /**
     * How often to poll the job repository for the status of the slaves.
     *
     * @param pollInterval milliseconds between polls, defaults to 10000 (10 seconds).
     */
    public void setPollInterval(long pollInterval) {
        this.pollInterval = pollInterval;
    }

    /**
     * {@link javax.sql.DataSource} pointing to the job repository
     *
     * @param dataSource {@link javax.sql.DataSource} that points to the job repository's store
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * A pre-configured gateway for sending and receiving messages to the remote workers. Using this property allows a
     * large degree of control over the timeouts and other properties of the send. It should have channels set up
     * internally: <ul> <li>request channel capable of accepting {@link StepExecutionRequest} payloads</li> <li>reply
     * channel that returns a list of {@link StepExecution} results</li> </ul> The timeout for the repoy should be set
     * sufficiently long that the remote steps have time to complete.
     *
     * @param messagingGateway the {@link org.springframework.integration.core.MessagingTemplate} to set
     */
    public void setMessagingGateway(BlockingQueue<Message<StepExecutionRequest>> messagingGateway) {
        this.messagingGateway = messagingGateway;
    }

    /**
     * Passed to the {@link StepExecutionSplitter} in the {@link #handle(StepExecutionSplitter, StepExecution)} method,
     * instructing it how many {@link StepExecution} instances are required, ideally. The {@link StepExecutionSplitter}
     * is allowed to ignore the grid size in the case of a restart, since the input data partitions must be preserved.
     *
     * @param gridSize the number of step executions that will be created
     */
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    /**
     * The name of the {@link Step} that will be used to execute the partitioned {@link StepExecution}. This is a
     * regular Spring Batch step, with all the business logic required to complete an execution based on the input
     * parameters in its {@link StepExecution} context. The name will be translated into a {@link Step} instance by the
     * remote worker.
     *
     * @param stepName the name of the {@link Step} instance to execute business logic
     */
    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    /**
     * @return the stepName
     */
    public String getStepName() {
        return stepName;
    }

    /**
     * @return the grouped
     */
    public boolean isGrouped() {
        return grouped;
    }

    /**
     * @param grouped the grouped to set
     */
    public void setGrouped(boolean grouped) {
        this.grouped = grouped;
    }

    /**
     * @param messages the messages to be aggregated
     * @return the list as it was passed in
     */
    @Aggregator(sendPartialResultsOnExpiry = "true")
    public List<?> aggregate(@Payloads List<?> messages) {
        return messages;
    }

    /**
     * Sends {@link StepExecutionRequest} objects to the request channel of the {@link MessagingTemplate}, and then
     * receives the result back as a list of {@link StepExecution} on a reply channel. Use the {@link #aggregate(List)}
     * method as an aggregator of the individual remote replies. The receive timeout needs to be set realistically in
     * the {@link MessagingTemplate} <b>and</b> the aggregator, so that there is a good chance of all work being done.
     *
     * @see PartitionHandler#handle(StepExecutionSplitter, StepExecution)
     */
    @Override
    public Collection<StepExecution> handle(StepExecutionSplitter stepExecutionSplitter,
        final StepExecution masterStepExecution) throws Exception {

        Set<StepExecution> split = stepExecutionSplitter.split(masterStepExecution, gridSize);
        if (CollectionUtils.isEmpty(split)) {
            return Collections.emptyList();
        }

        Map<Integer, StepExecution> sortedSteps = new TreeMap<>();
        split.forEach(step -> sortedSteps.put(Integer.parseInt(
            step.getStepName().chars()
                .mapToObj(v -> (char) v)
                .filter(Character::isDigit)
                .map(String::valueOf)
                .collect(Collectors.joining())
            ), step));
        split.clear();

        if (isGrouped()) {
            return handleGrouped(sortedSteps);
        } else {
            return handleSequential(sortedSteps);
        }
    }

    /**
     * Handle grouped submission.
     * @param sortedSteps steps, sorted by step number
     * @return collection or null
     */
    private Collection<StepExecution> handleGrouped(final Map<Integer, StepExecution> sortedSteps) throws Exception {

        Map<String, Collection<StepExecution>> groups = new LinkedHashMap<>();
        int total = 0;
        for (StepExecution step : sortedSteps.values()) {

            String groupName = step.getExecutionContext().getString(JobCommonParameters.PARAM_PARTITION_GROUP);
            if (StringUtils.isBlank(groupName)) {
                LOGGER.warn("Message handler started in grouped mode, but group parameter is empty! StepExecution [{}] is discarded.",
                        step.getStepName());
                continue;
            }

            Collection<StepExecution> groupSteps = groups.get(groupName);
            if (CollectionUtils.isEmpty(groupSteps)) {
                groupSteps = new ArrayList<>();
                groups.put(groupName, groupSteps);
            }

            groupSteps.add(step);
            total++;
        }

        if (CollectionUtils.isEmpty(groups)) {
            return null;
        }

        int count = 0;
        final List<StepExecution> result = new ArrayList<>(total);
        for (Entry<String, Collection<StepExecution>> groupEntry : groups.entrySet()) {

            LOGGER.info("Processing step execution group {}.", groupEntry.getKey());
            for (StepExecution stepExecution : groupEntry.getValue()) {

                StepExecutionRequest request = new StepExecutionRequest(stepName, stepExecution.getJobExecutionId(), stepExecution.getId());
                Message<StepExecutionRequest> message = createMessage(count++, total, request);

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Sending request: " + request);
                }

                messagingGateway.put(message);
            }

            StepsPollingCallable callable = new StepsPollingCallable(groupEntry.getValue(), result);
            Poller<Collection<StepExecution>> poller = new DirectPoller<>(pollInterval);
            Future<Collection<StepExecution>> futures = poller.poll(callable);

            if (timeout >= 0) {
                futures.get(timeout, TimeUnit.MILLISECONDS);
            }
            else {
                futures.get();
            }
        }

        return result;
    }

    /**
     * Handle sequential execution.
     * @param sortedSteps steps
     * @return collection
     * @throws Exception
     */
    private Collection<StepExecution> handleSequential(final Map<Integer, StepExecution> sortedSteps) throws Exception {

        int messageCount = 0;
        int retryCount = 0;
        boolean isRetry = false;
        Message<StepExecutionRequest> message = null;
        Iterator<StepExecution> sei = sortedSteps.values().iterator();
        while (isRetry || sei.hasNext()) {

            if (!isRetry) {
                StepExecution stepExecution = sei.next();
                StepExecutionRequest request = new StepExecutionRequest(stepName, stepExecution.getJobExecutionId(), stepExecution.getId());
                message = createMessage(messageCount++, sortedSteps.size(), request);
            }

            boolean wasOffered = messagingGateway.offer(message, pollInterval, TimeUnit.MILLISECONDS);
            if (!wasOffered && ++retryCount < 3) {
                isRetry = true;
            } else {

                // Very unlikely to happen, but handle it though, if it does
                if (!wasOffered && retryCount == 3) {
                    LOGGER.warn("Sending message: {} failed after {} retries. Stopping.", message.getPayload(), retryCount);
                    throw new JobException("Queue put for step failed, max retry of 3 reached,",
                            CoreExceptionIds.EX_JOB_STEP_SUBMIT_FAILED);
                }

                isRetry = false;
                retryCount = 0;
            }
        }

        final List<StepExecution> result = new ArrayList<>(sortedSteps.size());

        StepsPollingCallable callable = new StepsPollingCallable(sortedSteps.values(), result);
        Poller<Collection<StepExecution>> poller = new DirectPoller<>(pollInterval);
        Future<Collection<StepExecution>> futures = poller.poll(callable);

        if (timeout >= 0) {
            return futures.get(timeout, TimeUnit.MILLISECONDS);
        } else {
            return futures.get();
        }
    }

    /**
     * Creates a new step request.
     * @param sequenceNumber seqno
     * @param sequenceSize the sequence size
     * @param stepExecutionRequest the request
     * @return message
     */
    private Message<StepExecutionRequest> createMessage(int sequenceNumber, int sequenceSize, StepExecutionRequest stepExecutionRequest) {

        return MessageBuilder.withPayload(stepExecutionRequest)
            .setSequenceNumber(sequenceNumber)
            .setSequenceSize(sequenceSize)
            .setCorrelationId(stepExecutionRequest.getJobExecutionId() + ":" + stepExecutionRequest.getStepName())
            .build();
    }
    /**
     * Steps polling callable.
     * @author Mikhail Mikhailov
     */
    private class StepsPollingCallable implements Callable<Collection<StepExecution>> {
        /**
         * In collection.
         */
        private final Collection<StepExecution> in;
        /**
         * Out collection.
         */
        private final Collection<StepExecution> out;
        /**
         * Constructor.
         * @param in collection to read from
         * @param out collection to write to
         */
        public StepsPollingCallable(Collection<StepExecution> in, Collection<StepExecution> out) {
            super();
            this.in = in;
            this.out = out;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public Collection<StepExecution> call() throws Exception {

            for (Iterator<StepExecution> i = in.iterator(); i.hasNext(); ) {

                StepExecution inStep = i.next();
                StepExecution outStep =
                    jobExplorer.getStepExecution(
                            inStep.getJobExecutionId(),
                            inStep.getId());

                if (!outStep.getStatus().isRunning()) {
                    out.add(outStep);
                    i.remove();
                }
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Currently waiting for {} partitions to finish", in.size());
            }

            if (this.in.isEmpty()) {
                return out;
            } else {
                // null means 'go to the next cycle'
                return null;
            }
        }
    }
}
