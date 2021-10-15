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

package com.huahui.datasphere.mdm.core.service.job;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.huahui.datasphere.mdm.system.util.TextUtils;

import com.huahui.datasphere.mdm.core.context.UpsertUserEventRequestContext;
import com.huahui.datasphere.mdm.core.dto.UserEventDTO;
import com.huahui.datasphere.mdm.core.dto.reports.JobReportConstants;
import com.huahui.datasphere.mdm.core.service.UserService;
import com.huahui.datasphere.mdm.core.type.security.SecurityConstants;

/**
 * Notification Generator for result of batch job
 */
public abstract class NotificationGenerator implements JobExecutionListener {
    /**
     * user service
     */
    @Autowired
    private UserService userService;
    /**
     * User event ids.
     */
    private List<String> userEventIds;
    /**
     * User name
     */
    @Value("#{jobParameters[" + JobCommonParameters.PARAM_USER_NAME + "]}")
    protected String userName;
    /**
     * The job alias name.
     */
    @Value("#{jobParameters[" + JobCommonParameters.PARAM_JOB_ALIAS + "]}")
    protected String jobName;
    /**
     * Additional users, to send notifications to.
     */
    @Value("#{jobParameters[" + JobCommonParameters.PARAM_USER_SELECTOR + "]}")
    protected String usersSelector;
    /**
     * Job description
     */
    protected String jobDescription;

    @Override
    public void beforeJob(JobExecution jobExecution) {}

    @Override
    public void afterJob(JobExecution jobExecution) {
        userEventIds = getUserEventIds(jobExecution);
    }

    private List<String> getUserEventIds(JobExecution jobExecution) {

        Set<String> userNames = new HashSet<>();
        String userNameValue = userName;
        String usersSelectorValue = usersSelector;
        if (StringUtils.isNotBlank(userNameValue)) {

            if(SecurityConstants.SYSTEM_USER_NAME.equals(userNameValue)){
                userNameValue = SecurityConstants.ADMIN;
            }

            userNames.add(userNameValue);
        }

        if (StringUtils.isNotBlank(usersSelectorValue)) {

            String[] split = StringUtils.split(usersSelectorValue, "|");
            if (Objects.nonNull(split)) {

                for (String s : split) {
                    userNames.add(StringUtils.trim(s));
                }
            }
        }

        if (userNames.isEmpty()) {
            return Collections.emptyList();
        }

        final String generalMessage = getGeneralMessage(jobExecution);
        final String additionMessage = getAdditionMessage(jobExecution);
        final String result = StringUtils.isNotBlank(additionMessage) ?
                generalMessage + StringUtils.LF + StringUtils.LF + additionMessage :
                generalMessage;

        return userNames.stream()
            .map(name -> {

                final String reportType = jobExecution.getJobParameters()
                        .getString(JobReportConstants.JOB_REPORT_TYPE, "Text report");

                UpsertUserEventRequestContext eCtx = UpsertUserEventRequestContext.builder()
                        .type(reportType)
                        .content(result)
                        .details(getDetailsMessage(jobExecution))
                        .login(name)
                        .build();

                UserEventDTO userEventDTO = userService.upsert(eCtx);
                return userEventDTO.getId();
            })
            .collect(Collectors.toList());
    }

    protected String getGeneralMessage(JobExecution jobExecution) {
        String message = convertStatusToMessage(jobExecution.getStatus());
        return TextUtils.getText(jobDescription) + StringUtils.LF +  message;
    }

    @Nonnull
    protected String convertStatusToMessage(BatchStatus status) {
        String code;
        switch (status) {
        case STARTED:
            code = JobReportConstants.JOB_STATUS_STARTED;
            break;
        case STARTING:
            code = JobReportConstants.JOB_STATUS_STARTING;
            break;
        case COMPLETED:
            code = JobReportConstants.JOB_STATUS_COMPLETED;
            break;
        case STOPPING:
            code = JobReportConstants.JOB_STATUS_STOPPING;
            break;
        case STOPPED:
            code = JobReportConstants.JOB_STATUS_STOPPED;
            break;
        case FAILED:
            code = JobReportConstants.JOB_STATUS_FAILED;
            break;
        default:
            code = JobReportConstants.JOB_STATUS_UNKNOWN;
        }
        return StringUtils.isNotBlank(jobName)
                ? TextUtils.getText(code + JobReportConstants.JOB_NAMED_SUFFIX, jobName)
                : TextUtils.getText(code);

    }

    @Nonnull
    protected abstract String getAdditionMessage(JobExecution jobExecution);

    @Nonnull
    protected String getDetailsMessage(JobExecution jobExecution) {
        return "";
    }

    protected final List<String> getUserEventIds() {
        return userEventIds;
    }

    /**
     * @param usersSelector the usersSelector to set
     */
    public void setUsersSelector(String usersSelector) {
        this.usersSelector = usersSelector;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
