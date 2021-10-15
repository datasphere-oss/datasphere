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

import java.util.List;

import org.datasphere.mdm.core.type.job.JobExecutionStep;

/**
 * @author Mikhail Mikhailov
 * Steps page.
 */
public class StepExecutionQueryResult extends JobPaginatedResult<JobExecutionStep> {
    /**
     * Constructor.
     */
    public StepExecutionQueryResult() {
        super();
    }

    public StepExecutionQueryResult(List<JobExecutionStep> page) {
        super(page);
    }

    public StepExecutionQueryResult(List<JobExecutionStep> page, int totalCount) {
        super(page, totalCount);
    }

    public StepExecutionQueryResult(List<JobExecutionStep> page, int totalCount, int finishedCount) {
        super(page, totalCount, finishedCount);
    }
}
