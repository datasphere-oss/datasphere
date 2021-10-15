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

/**
 * Date: 21.03.2016
 */

package org.datasphere.mdm.core.service.impl.job;

import org.datasphere.mdm.core.context.JobExecutionStartContext;
import org.datasphere.mdm.core.service.JobService;
import org.datasphere.mdm.core.service.job.JobCommonParameters;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.datasphere.mdm.system.configuration.ModuleConfiguration;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class JobLauncherDetail extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobLauncherDetail.class);

    private JobService jobService = null;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        if (jobService == null) {
            jobService = ModuleConfiguration.getBean(JobService.class);
        }

        Object parentJobExecutionId = context.getMergedJobDataMap().get(JobCommonParameters.PARAM_PARENT_JOB_EXECUTION_ID);
        execute(Long.parseLong((String) context.getMergedJobDataMap().get("jobId")),
                parentJobExecutionId == null ? null : Long.parseLong((String) parentJobExecutionId));
    }

    public void execute(long jobDefinitionId) {

        LOGGER.info("Executing job (jobDefinitionId = [{}]).", jobDefinitionId);

        jobService.start(JobExecutionStartContext.builder()
                .jobDefinitionId(jobDefinitionId)
                .build());
    }

    private void execute(long jobDefinitionId, Long parentJobExecutionId) {

        LOGGER.info("Execute job (jobDefinitionId = [{}], parentJobExecutionId = [{}]).",
                jobDefinitionId, parentJobExecutionId);

        jobService.start(JobExecutionStartContext.builder()
                .jobDefinitionId(jobDefinitionId)
                .parentJobExecutionId(parentJobExecutionId)
                .build());
    }
}
