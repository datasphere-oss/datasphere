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

package com.huahui.datasphere.mdm.core.po.job;

import java.util.ArrayList;
import java.util.List;

import com.huahui.datasphere.mdm.core.po.AbstractObjectPO;

/**
 * Job PO from/to database.
 * @author Denis Kostovarov
 */
public class JobPO extends AbstractObjectPO {
    public static final String TABLE_NAME = "job";
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_ENABLED = "enabled";
    public static final String FIELD_ERROR = "error";
    public static final String FIELD_CRON_EXPRESSION = "cron_expr";
    public static final String FIELD_JOB_NAME_REFERENCE = "job_name_ref";
    public static final String FIELD_DESCRIPTION = "descr";
    public static final String FIELD_TAGS = "tags";

    private Long id;
    private String name;
    private boolean enabled;
    private boolean error;
    private String cronExpression;
    private String jobNameReference;
    private String description;
    private List<JobParameterPO> parameters;
    private List<String> tags;

    public JobPO() {
    }

    public JobPO(
            final Long id,
            final String name,
            final boolean enabled,
            final String cronExpression,
            final String jobNameReference,
            final String description
    ) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.cronExpression = cronExpression;
        this.jobNameReference = jobNameReference;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JobParameterPO> getParameters() {
        return parameters;
    }

    public void setParameters(List<JobParameterPO> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(final JobParameterPO param) {
        if (parameters == null) {
            parameters = new ArrayList<>();
        }

        parameters.add(param);
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

    public String getJobNameReference() {
        return jobNameReference;
    }

    public void setJobNameReference(String jobNameReference) {
        this.jobNameReference = jobNameReference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
