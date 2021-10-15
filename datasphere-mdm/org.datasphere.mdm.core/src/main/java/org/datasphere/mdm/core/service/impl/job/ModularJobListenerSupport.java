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
package org.datasphere.mdm.core.service.impl.job;

import java.util.Collection;
import java.util.Objects;

import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.exception.JobException;
import org.datasphere.mdm.core.type.job.JobDescriptor;
import org.datasphere.mdm.core.type.job.JobFraction;
import org.datasphere.mdm.core.util.JobUtils;

/**
 * @author Mikhail Mikhailov on Jul 12, 2021
 */
public abstract class ModularJobListenerSupport {
    /**
     * Job name. Null will cause havoc.
     */
    private final String jobName;
    /**
     * Constructor.
     * @param jobName the job name
     */
    protected ModularJobListenerSupport(String jobName) {
        super();
        this.jobName = jobName;
    }
    /**
     * Gets fractions.
     * @return fractions
     */
    public Collection<JobFraction> getFractions() {

        JobDescriptor descriptor = JobUtils.getDescriptor(jobName);
        if (Objects.isNull(descriptor)) {
            throw new JobException("Decsriptor for job [{}] not found at runtime.",
                    CoreExceptionIds.EX_JOB_DESCRIPTOR_NOT_FOUND_AT_RUNTIME, jobName);
        }

        return descriptor.getFractions();
    }
    /**
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
    }
}
