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

package com.huahui.datasphere.mdm.core.service.job.step;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import com.huahui.datasphere.mdm.system.type.runtime.MeasurementContextName;
import com.huahui.datasphere.mdm.system.type.runtime.MeasurementPoint;

import com.huahui.datasphere.mdm.core.exception.CoreExceptionIds;
import com.huahui.datasphere.mdm.core.exception.JobException;
import com.huahui.datasphere.mdm.core.service.SecurityService;
import com.huahui.datasphere.mdm.core.service.job.JobCommonParameters;
import com.huahui.datasphere.mdm.core.type.job.ModularBatchJobStepExecutionListener;
import com.huahui.datasphere.mdm.core.type.security.SecurityConstants;

/**
 * @author theseusyang
 * Various utilities actually.
 */
public abstract class AbstractJobStepExecutionListener extends ModularBatchJobStepExecutionListener {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJobStepExecutionListener.class);
    /**
     * Run id.
     */
    @Value("#{jobParameters[" + JobCommonParameters.PARAM_RUN_ID + "]}")
    protected String runId;
    /**
     * The name of the user, who started the job.
     */
    @Value("#{jobParameters[" + JobCommonParameters.PARAM_USER_NAME + "]}")
    protected String userName;
    /**
     * The token of the user, who started the job.
     */
    @Value("#{jobParameters[" + JobCommonParameters.PARAM_USER_TOKEN + "]}")
    protected String userToken;
    /**
     * The name of the user, who is supposed to authenticate during the step run.
     */
    @Value("#{jobParameters[" + JobCommonParameters.PARAM_JOB_USER + "]}")
    protected String jobUser;
    /**
     * The context name.
     */
    protected MeasurementContextName contextName;
    /**
     * The security service.
     */
    @Autowired
    protected SecurityService securityService;

    /**
     * Constructor.
     */
    public AbstractJobStepExecutionListener() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {

        if (Objects.nonNull(contextName)) {
            MeasurementPoint.init(contextName);
            MeasurementPoint.start();
        }

        super.beforeStep(stepExecution);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        super.afterStep(stepExecution);
        if (Objects.nonNull(contextName)) {
            MeasurementPoint.stop();
        }

        return stepExecution.getExitStatus();
    }

    /**
     * Authenticate, if token supplied.
     */
    protected void authenticateIfNeeded() {

        String user;
        if (StringUtils.isNotBlank(jobUser)) {
            user =  jobUser;
        } else {
            user = StringUtils.isNotBlank(userName) && !SecurityConstants.SYSTEM_USER_NAME.equals(userName)
                    ? userName
                    : null;
        }

        if (Objects.nonNull(user)) {
            String token = securityService.getOrCreateInnerTokenByLogin(user);
            if (Objects.isNull(token)) {
                throw new JobException("User not found or deactivated!", CoreExceptionIds.EX_JOB_BAD_CREDENTIALS, user);
            }
            boolean authenticated = securityService.authenticate(token, true);
            LOGGER.info("Initiator user [{}] {} authenticated.", user, authenticated ? "successfully" : "could NOT be");
        }
    }

    /**
     * clear authentication
     */
    protected void clearAuthentication() {

        if (Objects.nonNull(SecurityContextHolder.getContext())) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @param userToken the userToken to set
     */
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    /**
     * Sets the name of the context.
     *
     * @param contextName the name of the measurement context
     */
    public void setContextName(MeasurementContextName contextName) {
        this.contextName = contextName;
    }
}
