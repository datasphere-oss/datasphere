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
package org.datasphere.mdm.core.dto.job;

import java.util.Objects;

import org.datasphere.mdm.core.type.job.JobExecution;

/**
 * @author Mikhail Mikhailov on Jul 6, 2021
 */
public class JobExecutionStartResult {
    /**
     * The execution.
     */
    private JobExecution execution;
    /**
     * Constructor.
     */
    public JobExecutionStartResult() {
        super();
    }
    /**
     * Constructor.
     */
    public JobExecutionStartResult(JobExecution execution) {
        super();
        this.execution = execution;
    }
    /**
     * @return the execution
     */
    public JobExecution getExecution() {
        return execution;
    }
    /**
     * @param execution the execution to set
     */
    public void setExecution(JobExecution execution) {
        this.execution = execution;
    }
    /**
     * Returns true, if the execution is missing.
     * @return true, if the execution is missing
     */
    public boolean isEmpty() {
        return Objects.isNull(execution);
    }
}
