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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Denis Kostovarov
 */
abstract class JobMetaDTO<T extends JobTemplateParameterDTO> {
    private String name;
    private String jobNameReference;
    private List<T> parameters;

    JobMetaDTO(){
        //no-op.
    }

    JobMetaDTO(String name, String jobNameReference, List<T> parameters) {
        this.name = name;
        this.jobNameReference = jobNameReference;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobNameReference() {
        return jobNameReference;
    }

    public void setJobNameReference(String jobNameReference) {
        this.jobNameReference = jobNameReference;
    }

    public List<T> getParameters() {
        return parameters;
    }

    public void setParameters(List<T> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(T param) {
        if (parameters == null) {
            parameters = new ArrayList<>();
        }
        parameters.add(param);
    }
}
