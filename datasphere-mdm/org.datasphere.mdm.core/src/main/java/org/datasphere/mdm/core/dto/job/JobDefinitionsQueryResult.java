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

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.MapUtils;
import org.datasphere.mdm.core.type.job.JobDefinition;
import org.datasphere.mdm.core.type.job.JobExecutionState;

/**
 * @author Mikhail Mikhailov on Jun 24, 2021
 */
public class JobDefinitionsQueryResult {
    /**
     * The definitions.
     */
    private Map<JobDefinition, JobExecutionState> definitions;
    /**
     * Total count of all job definitions.
     */
    private int totalCount;
    /**
     * Constructor.
     */
    public JobDefinitionsQueryResult() {
        super();
    }
    /**
     * Constructor.
     */
    public JobDefinitionsQueryResult(Map<JobDefinition, JobExecutionState> definitions) {
        super();
        this.definitions = definitions;
    }
    /**
     * Constructor.
     * @param definitions
     * @param totalCount
     */
    public JobDefinitionsQueryResult(Map<JobDefinition, JobExecutionState> definitions, int totalCount) {
        this(definitions);
        this.totalCount = totalCount;
    }
    /**
     * @return the definitions
     */
    public Map<JobDefinition, JobExecutionState> getDefinitions() {
        return Objects.isNull(definitions) ? Collections.emptyMap() : definitions;
    }
    /**
     * @param definitions the definitions to set
     */
    public void setDefinitions(Map<JobDefinition, JobExecutionState> definitions) {
        this.definitions = definitions;
    }
    /**
     * Returns true, if empty.
     * @return true, if empty
     */
    public boolean isEmpty() {
        return MapUtils.isEmpty(definitions);
    }
    /**
     * @return the totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }
    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
