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

package com.huahui.datasphere.mdm.core.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import com.huahui.datasphere.mdm.system.util.TextUtils;

import com.huahui.datasphere.mdm.core.dto.job.JobParameterDTO;
import com.huahui.datasphere.mdm.core.service.job.JobParameterValidator;

/**
 * Job parameter validator for positive values
 *  Error if value <= 0 or null
 * @author Dmitry Kopin on 09.08.2018.
 */
@Component("positiveValueJobParameterValidator")
public class PositiveValueJobParameterValidator implements JobParameterValidator {

    private static final String NOT_POSITIVE_PARAMETER_VALUE = "app.job.parameters.value.notpositive";

    @Override
    public List<String> validate(String paramName, Collection<JobParameterDTO> jobParameters) {

        if (CollectionUtils.isNotEmpty(jobParameters)) {
            JobParameterDTO jobParameter = jobParameters.stream()
                    .filter(param -> param.getName().equals(paramName))
                    .findFirst()
                    .orElse(null);
            if (jobParameter == null) {
                return Collections.emptyList();
            }

            return jobParameter.getLongValue() != null && jobParameter.getLongValue() > 0
                    ? Collections.emptyList()
                    : Collections.singletonList(TextUtils.getText(NOT_POSITIVE_PARAMETER_VALUE, jobParameter.getName()));

        }

        return Collections.emptyList();
    }
}
