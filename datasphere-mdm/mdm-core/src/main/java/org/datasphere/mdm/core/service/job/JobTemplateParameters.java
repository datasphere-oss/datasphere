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
 * Date: 16.03.2016
 */

package com.huahui.datasphere.mdm.core.service.job;

import java.util.Map;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class JobTemplateParameters {
    private String jobName;
    private Map<String, Object> valueMap;
    private JobParameterProcessor parameterProcessor;
    private Map<String, JobParameterValidator> validators;

    public JobTemplateParameters() {
        super();
    }

    public JobTemplateParameters(String jobName) {
        this();
        this.jobName = jobName;
    }

    public JobTemplateParameters(String jobName, Map<String, Object> valueMap, JobParameterProcessor parameterProcessor) {
        super();
        this.jobName = jobName;
        this.valueMap = valueMap;
        this.parameterProcessor = parameterProcessor;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Map<String, Object> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, Object> valueMap) {
        this.valueMap = valueMap;
    }

    /**
     * @return the parameterProcessor
     */
    public JobParameterProcessor getParameterProcessor() {
        return parameterProcessor;
    }

    /**
     * @param parameterProcessor the parameterProcessor to set
     */
    public void setParameterProcessor(JobParameterProcessor parameterProcessor) {
        this.parameterProcessor = parameterProcessor;
    }

    public Map<String, JobParameterValidator> getValidators() {
        return validators;
    }

    public void setValidators(Map<String, JobParameterValidator> validators) {
        this.validators = validators;
    }
}