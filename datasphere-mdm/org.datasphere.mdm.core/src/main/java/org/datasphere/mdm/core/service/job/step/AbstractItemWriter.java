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

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Mikhail Mikhailov on Feb 17, 2020
 */
public abstract class AbstractItemWriter<T> implements ItemWriter<T> {
    /**
     * The step execution.
     */
    @Value("#{stepExecution}")
    protected StepExecution stepExecution;
    /**
     * @return the stepName
     */
    public String getStepName() {
        return StringUtils.substringBefore(stepExecution.getStepName(), ":");
    }
    /**
     * @return the stepExecution
     */
    public StepExecution getStepExecution() {
        return stepExecution;
    }
}
