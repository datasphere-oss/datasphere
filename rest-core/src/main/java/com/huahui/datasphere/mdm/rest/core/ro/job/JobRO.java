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

import java.util.List;

/**
 * FIXDOC: add file description.
 *
 * @author theseusyang
 */
public class JobRO {
    private Long id;

    private String name;

    private String cronExpression;

    private String jobNameReference;

    private String description;

    private boolean enabled;

    private boolean error;

    private boolean skipCronWarnings;

    private List<JobParameterRO> parameters;

    private JobExecutionRO lastExecution;
    
    private List<String> tags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getJobNameReference() {
        return jobNameReference;
    }

    public void setJobNameReference(String jobNameReference) {
        this.jobNameReference = jobNameReference;
    }

    public List<JobParameterRO> getParameters() {
        return parameters;
    }

    public void setParameters(List<JobParameterRO> parameters) {
        this.parameters = parameters;
    }

    public JobExecutionRO getLastExecution() {
        return lastExecution;
    }

    public void setLastExecution(JobExecutionRO lastExecution) {
        this.lastExecution = lastExecution;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isSkipCronWarnings() {
        return skipCronWarnings;
    }

    public void setSkipCronWarnings(boolean skipCronWarnings) {
        this.skipCronWarnings = skipCronWarnings;
    }

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
