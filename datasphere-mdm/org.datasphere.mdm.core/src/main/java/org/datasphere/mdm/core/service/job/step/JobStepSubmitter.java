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

import org.datasphere.mdm.core.service.job.JobStepExecutionRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.step.NoSuchStepException;
import org.springframework.batch.integration.partition.StepExecutionRequest;
import org.springframework.messaging.Message;

/**
 * Step request processing worker. Just calls step handler.
 */
public class JobStepSubmitter implements Runnable {
    /**
     * This logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JobStepSubmitter.class);
    /**
     * Payload.
     */
    private final Message<StepExecutionRequest> arg;
    /**
     * May be canceled.
     */
    private final boolean canceled;
    /**
     * The handler.
     */
    private final JobStepExecutionRequestHandler handler;
    /**
     * Constructor.
     * @param arg the payload
     * @param handler the handler.
     * @param canceled whether this run is already canceled
     * @param number this worker step number.
     */
    public JobStepSubmitter(Message<StepExecutionRequest> arg, JobStepExecutionRequestHandler handler, boolean canceled) {
        super();
        this.arg = arg;
        this.handler = handler;
        this.canceled = canceled;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

        StepExecution stepExecution = null;
        final StepExecutionRequest request = arg.getPayload();
        final Long jobExecutionId = request.getJobExecutionId();
        final Long stepExecutionId = request.getStepExecutionId();

        LOGGER.debug("Received step chunk [jobName={}, jobExecutionId={}, stepExecutionId={}]",
                handler.getJobName(), jobExecutionId, stepExecutionId);

        try {

            stepExecution = handler.getJobExplorer().getStepExecution(jobExecutionId, stepExecutionId);
            if (stepExecution == null) {
                throw new NoSuchStepException("No StepExecution could be located for request.");
            }

            if (canceled) {
                stepExecution.setStatus(BatchStatus.STOPPED);
                handler.getJobRepository().update(stepExecution);

                LOGGER.debug("Canceled step chunk executed [jobName={}, jobExecutionId={}, stepExecutionId={}]",
                        handler.getJobName(), jobExecutionId, stepExecutionId);

                return;
            }

            final String stepName = request.getStepName();
            final Step step = handler.getStepLocator().getStep(stepName);

            step.execute(stepExecution);
        } catch (JobInterruptedException e) {

            // The receiver should update the stepExecution in repository
            stepExecution.addFailureException(e);
            stepExecution.setStatus(BatchStatus.STOPPED);
            handler.getJobRepository().update(stepExecution);

            LOGGER.info("Job step chunk interrupted exception caught for [jobName={}, jobExecutionId={}, stepExecutionId={}].",
                    handler.getJobName(), jobExecutionId, stepExecutionId, e);

        } catch (Throwable e) {

            // The receiver should update the stepExecution in repository
            if (stepExecution != null) {
                stepExecution.addFailureException(e);
                stepExecution.setStatus(BatchStatus.FAILED);
                handler.getJobRepository().update(stepExecution);
            }

            LOGGER.warn("Throwable caught for job step chunk [jobName={}, jobExecutionId={}, stepExecutionId={}].",
                    handler.getJobName(), jobExecutionId, stepExecutionId, e);
        }

        LOGGER.debug("Step chunk executed [jobName={}, jobExecutionId={}, stepExecutionId={}]",
                handler.getJobName(), jobExecutionId, stepExecutionId);
    }
}