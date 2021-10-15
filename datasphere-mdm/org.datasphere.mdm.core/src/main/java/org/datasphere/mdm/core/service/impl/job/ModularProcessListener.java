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
import org.springframework.batch.core.ItemProcessListener;

/**
 * @author Mikhail Mikhailov on Jul 12, 2021
 */
@SuppressWarnings("rawtypes")
public class ModularProcessListener extends ModularStepListenerSupport implements ItemProcessListener {
    /**
     * Constructor.
     * @param jobName the job name
     * @param stepName the step name
     */
    public ModularProcessListener(String jobName, String stepName) {
        super(jobName, stepName);
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void beforeProcess(Object item) {

        Collection<JobFraction> fractions = getFractions();
        if (CollectionUtils.isEmpty(fractions)) {
            return;
        }

        for (JobFraction fraction : fractions) {

            ItemProcessListener ipl = fraction.itemProcessListener(getStepName());
            if (Objects.isNull(ipl)) {
                continue;
            }

            ipl.beforeProcess(item);
        }
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void afterProcess(Object item, Object result) {

        Collection<JobFraction> fractions = getFractions();
        if (CollectionUtils.isEmpty(fractions)) {
            return;
        }

        for (JobFraction fraction : fractions) {

            ItemProcessListener ipl = fraction.itemProcessListener(getStepName());
            if (Objects.isNull(ipl)) {
                continue;
            }

            ipl.afterProcess(item, result);
        }
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onProcessError(Object item, Exception e) {

        Collection<JobFraction> fractions = getFractions();
        if (CollectionUtils.isEmpty(fractions)) {
            return;
        }

        for (JobFraction fraction : fractions) {

            ItemProcessListener ipl = fraction.itemProcessListener(getStepName());
            if (Objects.isNull(ipl)) {
                continue;
            }

            ipl.onProcessError(item, e);
        }
    }
}
