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

/**
 * Date: 21.03.2016
 */

package com.huahui.datasphere.mdm.core.service.impl;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.huahui.datasphere.mdm.core.service.JobService;
import com.huahui.datasphere.mdm.core.service.job.JobCommonParameters;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class JobLauncherDetail extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger(JobLauncherDetail.class);

    @Autowired
    private JobService jobService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // Process @Autowired injection for the given target object, based on the current web application context.
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        Object parentJobExecutionId = context.getMergedJobDataMap().get(JobCommonParameters.PARAM_PARENT_JOB_EXECUTION_ID);
        execute(Long.parseLong((String)context.getMergedJobDataMap().get("jobId")),
                parentJobExecutionId == null ? null : Long.parseLong((String)parentJobExecutionId));
    }

    public void execute(long jobId) {
        log.info("Execute job [jobId=" + jobId + ", jobService=" + jobService + ']');

        jobService.start(jobId);
    }

    private void execute(long jobId, Long parentJobExecutionId) {
        log.info("Execute job [jobId=" + jobId + ", jobService=" + jobService + ']');

        jobService.start(jobId, parentJobExecutionId);
    }
}
