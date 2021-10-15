/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 * 
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.service.job;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.context.UpsertUserEventRequestContext;
import org.datasphere.mdm.core.dto.UserEventDTO;
import org.datasphere.mdm.core.dto.reports.JobReportConstants;
import org.datasphere.mdm.core.service.UserService;
import org.datasphere.mdm.core.type.security.SecurityConstants;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.datasphere.mdm.system.util.TextUtils;

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
    @Value("#{jobParameters[" + JobCommonParameters.PARAM_JOB_NAME + "]}")
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
