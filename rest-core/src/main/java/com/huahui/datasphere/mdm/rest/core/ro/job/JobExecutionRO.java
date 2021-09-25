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
 * Date: 10.03.2016
 */

package com.huahui.datasphere.mdm.rest.core.ro.job;

import java.util.Date;
import java.util.List;

/**
 * FIXDOC: add file description.
 *
 * @author theseusyang
 */
public class JobExecutionRO {
    private Long id;

    private Long jobId;

    private Date startTime;

    private Date endTime;

    private String status;

    private Boolean restartable;

    private String exitDescription;

    private List<JobStepExecutionRO> stepExecutions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<JobStepExecutionRO> getStepExecutions() {
        return stepExecutions;
    }

    public void setStepExecutions(List<JobStepExecutionRO> stepExecutions) {
        this.stepExecutions = stepExecutions;
    }

    public Boolean isRestartable() {
        return restartable;
    }

    public void setRestartable(Boolean restartable) {
        this.restartable = restartable;
    }

    public String getExitDescription() {
        return exitDescription;
    }

    public void setExitDescription(String exitDescription) {
        this.exitDescription = exitDescription;
    }
}
