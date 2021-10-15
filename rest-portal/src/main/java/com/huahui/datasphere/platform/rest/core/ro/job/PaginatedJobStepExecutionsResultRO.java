/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
/**
 * Date: 29.04.2016
 */

package com.huahui.datasphere.platform.rest.core.ro.job;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class PaginatedJobStepExecutionsResultRO {
    private List<JobStepExecutionRO> content;

    @JsonProperty(value = "total_count")
    private int totalCount;

    @JsonProperty(value = "completed_count")
    private int completedCount;

    public List<JobStepExecutionRO> getContent() {
        return content;
    }

    public void setContent(List<JobStepExecutionRO> content) {
        this.content = content;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * @return the completedCount
     */
    public int getCompletedCount() {
        return completedCount;
    }

    /**
     * @param completedCount the completedCount to set
     */
    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }
}
