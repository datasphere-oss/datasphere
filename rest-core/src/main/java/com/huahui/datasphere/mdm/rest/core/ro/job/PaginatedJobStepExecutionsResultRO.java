/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
/**
 * Date: 29.04.2016
 */

package com.huahui.datasphere.mdm.rest.core.ro.job;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * FIXDOC: add file description.
 *
 * @author theseusyang
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