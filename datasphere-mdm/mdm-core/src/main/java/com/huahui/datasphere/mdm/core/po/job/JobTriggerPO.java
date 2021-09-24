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

import com.huahui.datasphere.mdm.core.po.AbstractObjectPO;

/**
 * Job trigger PO.
 * @author Denis Kostovarov
 */
public class JobTriggerPO extends AbstractObjectPO {
    public static final String TABLE_NAME = "job_trigger";
    public static final String FIELD_ID = "id";
    public static final String FIELD_FINISH_JOB_ID = "finish_job_id";
    public static final String FIELD_START_JOB_ID = "start_job_id";
    public static final String FIELD_SUCCESS_RULE = "success_rule";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DESCRIPTION = "description";

    private Long id;
    private Long finishJobId;
    private Long startJobId;
    private Boolean successRule;
    private String name;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFinishJobId() {
        return finishJobId;
    }

    public void setFinishJobId(Long finishJobId) {
        this.finishJobId = finishJobId;
    }

    public Long getStartJobId() {
        return startJobId;
    }

    public void setStartJobId(Long startJobId) {
        this.startJobId = startJobId;
    }

    public Boolean getSuccessRule() {
        return successRule;
    }

    public void setSuccessRule(Boolean successRule) {
        this.successRule = successRule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
