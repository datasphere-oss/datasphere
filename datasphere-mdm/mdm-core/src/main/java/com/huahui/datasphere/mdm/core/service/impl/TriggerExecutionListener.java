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
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huahui.datasphere.mdm.core.service.JobService;

/**
 * Listener for starting next jobs after finish current.
 */
@Component
public class TriggerExecutionListener implements JobExecutionListener {
    private static final int DEFAULT_TRIGGER_START_TIMEOUT = 10;

    private static final Logger log = LoggerFactory.getLogger(TriggerExecutionListener.class);

    private Integer triggerTimeout;

    @Autowired
    private JobService jobServiceExt;

    @Override
    public void beforeJob(final JobExecution jobExecution) {
        // no-op.
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        final JobParameters jobParameters = jobExecution.getJobParameters();
        final String nameParamString;
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            nameParamString = JobService.SUCCESS_FINISH_JOB_ID_PARAMETER;
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            nameParamString = JobService.FAIL_FINISH_JOB_ID_PARAMETER;
        } else {
            nameParamString = null;
            log.warn("Wrong status [" + jobExecution.getStatus() + "] for scheduling trigger jobs");
        }

        if (nameParamString != null) {
            int index = 0;
            Long jobId;
            final ZonedDateTime zdt = ZonedDateTime.now();
            final Map<String, JobParameter> params = jobParameters.getParameters();

            while (params.containsKey(nameParamString + index)) {
                jobId = jobParameters.getLong(nameParamString + index);
                log.info("Scheduling next job by status [" + jobExecution.getStatus() + "], jobId [" + jobId + "]");
                zdt.plus(getTriggerTimeout(), ChronoUnit.SECONDS);
                jobServiceExt.schedule(jobId, generateCronExpression(
                        String.valueOf(zdt.getSecond()),
                        String.valueOf(zdt.getMinute()),
                        String.valueOf(zdt.getHour()),
                        String.valueOf(zdt.getDayOfMonth()),
                        String.valueOf(zdt.getMonth().getValue()),
                        "?",
                        String.valueOf(zdt.getYear())),
                        jobExecution.getJobId());
                index++;
            }
            if (index == 0) {
                log.debug("No trigger jobs found for job with job instance id [" + jobExecution.getJobId() + "]");
            }
        }
    }

    /**
     * Generate a CRON expression is a string comprising 6 or 7 fields separated by white space.
     *
     * @param seconds    mandatory = yes. allowed values = {@code  0-59    * / , -}
     * @param minutes    mandatory = yes. allowed values = {@code  0-59    * / , -}
     * @param hours      mandatory = yes. allowed values = {@code 0-23   * / , -}
     * @param dayOfMonth mandatory = yes. allowed values = {@code 1-31  * / , - ? L W}
     * @param month      mandatory = yes. allowed values = {@code 1-12 or JAN-DEC    * / , -}
     * @param dayOfWeek  mandatory = yes. allowed values = {@code 0-6 or SUN-SAT * / , - ? L #}
     * @param year       mandatory = no. allowed values = {@code 1970-2099    * / , -}
     * @return a CRON Formatted String.
     */
    private String generateCronExpression(final String seconds, final String minutes, final String hours,
                                          final String dayOfMonth,
                                          final String month, final String dayOfWeek, final String year) {
        return String.format("%1$s %2$s %3$s %4$s %5$s %6$s %7$s", seconds, minutes, hours, dayOfMonth, month, dayOfWeek, year);
    }

    private int getTriggerTimeout() {
        if (triggerTimeout == null) {

            // TODO: @Modules
            final String timeout = "";/*ServiceUtils.getConfigurationService()
                    .getSystemStringPropertyWithDefault(
                            ConfigurationConstants.JOB_TRIGGER_START_TIMEOUT_PROPERTY,
                            String.valueOf(DEFAULT_TRIGGER_START_TIMEOUT));*/

            try {
                triggerTimeout = Integer.valueOf(timeout);
            } catch (final NumberFormatException e) {
                triggerTimeout = DEFAULT_TRIGGER_START_TIMEOUT;
            }
        }

        return triggerTimeout;
    }
}
