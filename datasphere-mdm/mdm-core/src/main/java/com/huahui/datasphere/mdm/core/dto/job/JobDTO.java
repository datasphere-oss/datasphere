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

package com.huahui.datasphere.mdm.core.dto.job;

import java.util.List;

/**
 * @author Denis Kostovarov
 */
public class JobDTO extends JobMetaDTO<JobParameterDTO> {
    private Long id;
    private boolean enabled;
    private boolean error;
    private String cronExpression;
    private String description;    
    private boolean skipCronWarnings;
    private List<String> tags;

    public JobDTO() {
    }

    public JobDTO(
            final Long id,
            final String name,
            final String jobNameReference,
            final List<JobParameterDTO> parameters,
            final boolean enabled,
            final boolean error,
            final String cronExpression,
            final String description,
            final boolean skipCronWarnings
    ) {
        super(name, jobNameReference, parameters);
        this.id = id;
        this.enabled = enabled;
        this.error = error;
        this.cronExpression = cronExpression;
        this.description = description;
        this.skipCronWarnings = skipCronWarnings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
