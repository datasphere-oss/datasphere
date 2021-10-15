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
package org.datasphere.mdm.core.convert.job;

import org.datasphere.mdm.core.type.job.JobExecutionStatus;
import org.datasphere.mdm.core.type.job.JobExecutionStep;
import org.datasphere.mdm.core.type.job.StepExecutionState;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;
import org.datasphere.mdm.system.convert.Converter;

/**
 * @author Mikhail Mikhailov on Jul 8, 2021
 */
@Component
public class StepExecutionConverter extends Converter<StepExecution, JobExecutionStep> {

    /**
     * Constructor.
     * @param to
     * @param from
     */
    public StepExecutionConverter() {
        super(StepExecutionConverter::convert, null);
    }

    private static JobExecutionStep convert(final StepExecution source) {

        JobExecutionStep target = new JobExecutionStep();

        target.setId(source.getId());
        target.setJobExecutionId(source.getJobExecutionId());
        target.setStepName(source.getStepName());
        target.setStartTime(source.getStartTime() == null ? null : source.getStartTime().toInstant());
        target.setEndTime(source.getEndTime() == null ? null : source.getEndTime().toInstant());
        target.setLastUpdated(source.getLastUpdated() == null ? null : source.getLastUpdated().toInstant());
        target.setCommitCount(source.getCommitCount());
        target.setProcessSkipCount(source.getProcessSkipCount());
        target.setReadCount(source.getReadCount());
        target.setReadSkipCount(source.getReadSkipCount());
        target.setRollbackCount(source.getRollbackCount());
        target.setWriteCount(source.getWriteCount());
        target.setWriteSkipCount(source.getWriteSkipCount());
        target.setFailures(source.getFailureExceptions());
        target.setState(new StepExecutionState()
                .withStatus(JobExecutionStatus.valueOf(source.getStatus().name()))
                .withExitDescription(source.getExitStatus().getExitDescription())
                .withExitCode(source.getExitStatus().getExitCode()));

        return target;
    }
}
