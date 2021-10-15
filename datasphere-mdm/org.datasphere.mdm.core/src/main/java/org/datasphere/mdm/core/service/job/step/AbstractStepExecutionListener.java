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

package org.datasphere.mdm.core.service.job.step;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.exception.JobException;
import org.datasphere.mdm.core.service.SecurityService;
import org.datasphere.mdm.core.service.job.JobCommonParameters;
import org.datasphere.mdm.core.type.job.impl.single.SingleJobParameterDefinition;
import org.datasphere.mdm.core.type.security.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.datasphere.mdm.system.type.runtime.MeasurementContextName;
import org.datasphere.mdm.system.type.runtime.MeasurementPoint;

/**
 * @author Mikhail Mikhailov
 * Various utilities actually.
 */
public abstract class AbstractStepExecutionListener implements StepExecutionListener {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStepExecutionListener.class);
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
    protected SingleJobParameterDefinition<String> jobUser;
    /**
     * The step execution.
     */
    @Value("#{stepExecution}")
    protected StepExecution stepExecution;
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
    protected AbstractStepExecutionListener() {
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
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        if (Objects.nonNull(contextName)) {
            MeasurementPoint.stop();
        }

        return stepExecution.getExitStatus();
    }
    /**
     * @return the stepName
     */
    public String getStepName() {
        return StringUtils.substringBefore(stepExecution.getStepName(), ":");
    }
    /**
     * @return the step execution
     */
    public StepExecution getStepExecution() {
        return stepExecution;
    }
    /**
     * Authenticate, if token supplied.
     */
    protected void authenticateIfNeeded() {

        String user;
        if (Objects.nonNull(jobUser) && !jobUser.isEmpty()) {
            user =  jobUser.single();
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
