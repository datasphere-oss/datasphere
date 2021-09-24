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

import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.batch.core.JobParametersBuilder;

import com.huahui.datasphere.mdm.core.dto.job.JobParameterDTO;
import com.huahui.datasphere.mdm.core.service.job.JobParameterProcessor;
import com.huahui.datasphere.mdm.core.type.job.CustomJobParameter;

/**
 * @author theseusyang
 * Default implementation.
 */
public class DefaultJobParameterProcessor implements JobParameterProcessor {

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(JobParameterDTO jobParameter, JobParametersBuilder builder) {
        switch (jobParameter.getType()) {
            case STRING:
                if (jobParameter.isMultiValue()) {
                    builder.addParameter(jobParameter.getName(), new CustomJobParameter(jobParameter.getStringArrayValue()));
                } else {
                    builder.addString(jobParameter.getName(), jobParameter.getStringValue());
                }
                break;
            case DATE:
                if (jobParameter.isMultiValue()) {
                    Date[] toValues;
                    ZonedDateTime[] fromValues = jobParameter.getDateArrayValue();

                    // Convert ZonedDateTime array to Date array.
                    if (fromValues != null) {
                        toValues = new Date[fromValues.length];

                        for (int i = 0; i < fromValues.length; i++) {
                            toValues[i] = Date.from(fromValues[i].toInstant());
                        }
                    } else {
                        toValues = new Date[0];
                    }
                    builder.addParameter(jobParameter.getName(), new CustomJobParameter(toValues));
                } else {
                    builder.addDate(jobParameter.getName(), Date.from(jobParameter.getDateValue().toInstant()));
                }
                break;
            case LONG:
                if (jobParameter.isMultiValue()) {
                    builder.addParameter(jobParameter.getName(), new CustomJobParameter(jobParameter.getLongArrayValue()));
                } else {
                    builder.addLong(jobParameter.getName(), jobParameter.getLongValue());
                }
                break;
            case DOUBLE:
                if (jobParameter.isMultiValue()) {
                    builder.addParameter(jobParameter.getName(), new CustomJobParameter(jobParameter.getDoubleArrayValue()));
                } else {
                    builder.addDouble(jobParameter.getName(), jobParameter.getDoubleValue());
                }
                break;
            case BOOLEAN:
                if (jobParameter.isMultiValue()) {
                    String[] toValues;
                    Boolean[] fromValues = jobParameter.getBooleanArrayValue();

                    if (fromValues != null) {
                        toValues = new String[fromValues.length];
                        for (int i = 0; i < fromValues.length; i++) {
                            toValues[i] = fromValues[i].toString();
                        }
                    } else {
                        toValues = new String[0];
                    }
                    builder.addParameter(jobParameter.getName(), new CustomJobParameter(toValues));
                } else {
                    final Boolean b = jobParameter.getBooleanValue();
                    builder.addString(jobParameter.getName(), b.toString());
                }
                break;
        }
    }
}
