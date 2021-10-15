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

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.type.job.JobFraction;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * @author Mikhail Mikhailov on Jul 12, 2021
 */
public class ModularJobExecutionListener extends ModularJobListenerSupport implements JobExecutionListener {
    /**
     * Constructor.
     * @param jobName the job name
     */
    public ModularJobExecutionListener(String jobName) {
        super(jobName);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeJob(JobExecution je) {

        Collection<JobFraction> fractions = getFractions();
        if (CollectionUtils.isEmpty(fractions)) {
            return;
        }

        for (JobFraction mjf : fractions) {

            JobExecutionListener jel = mjf.jobExecutionListener();
            if (Objects.isNull(jel)) {
                continue;
            }

            jel.beforeJob(je);
        }
    }
    /**
     * Run fractions after job.
     * @param je the job execution
     */
    @Override
    public void afterJob(JobExecution je) {

        Collection<JobFraction> fractions = getFractions();
        if (CollectionUtils.isEmpty(fractions)) {
            return;
        }

        for (JobFraction mjf : fractions) {

            JobExecutionListener jel = mjf.jobExecutionListener();
            if (Objects.isNull(jel)) {
                continue;
            }

            jel.afterJob(je);
        }
    }
}
