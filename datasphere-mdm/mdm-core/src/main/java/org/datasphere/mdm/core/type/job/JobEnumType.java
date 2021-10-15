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

package com.huahui.datasphere.mdm.core.type.job;

import java.util.List;

/**
 * @author Denis Kostovarov
 */
public class JobEnumType {
    private boolean multiSelect;
    private JobParameterType parameterType;

    private List<?> parameters;

    public JobParameterType getParameterType() {
        return parameterType;
    }

    public void setParameterType(JobParameterType parameterType) {
        this.parameterType = parameterType;
    }

    public List<?> getParameters() {
        return parameters;
    }

    public void setParameters(List<?> parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the multiSelect
     */
    public boolean isMultiSelect() {
        return multiSelect;
    }

    /**
     * @param multiSelect the multiSelect to set
     */
    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }
}
