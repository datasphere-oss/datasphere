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
 * Date: 16.03.2016
 */

package com.huahui.datasphere.mdm.core.configuration.job;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.huahui.datasphere.mdm.core.service.impl.TriggerExecutionListener;
import com.huahui.datasphere.mdm.core.service.job.JobParameterProcessor;
import com.huahui.datasphere.mdm.core.service.job.JobTemplateParameters;
import com.huahui.datasphere.mdm.core.service.job.JobWithParamsRegistry;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class JobWithParamsRegistryImpl extends MapJobRegistry implements JobWithParamsRegistry {
    private static final Logger log = LoggerFactory.getLogger(JobWithParamsRegistryImpl.class);

    private final ConcurrentMap<String, JobTemplateParameters> map = new ConcurrentHashMap<>();
    // Concurrent map
    private Map<String, JobParameterProcessor> jobParameterProcessorsMap;

    @Autowired
    private TriggerExecutionListener listener;

    @Override
    public void register(JobTemplateParameters parameters) throws DuplicateJobException {
        log.debug("Register job parameters [jobName={}, parameters={}]", parameters.getJobName(), parameters);

        Assert.notNull(parameters);
        final String name = parameters.getJobName();

        Assert.notNull(name, "Job configuration must have a name.");
        if (map.containsKey(name)) {
            throw new DuplicateJobException("A job parameters configuration with this name [" + name
                + "] was already registered");
        }

        JobParameterProcessor parameterProcessor = parameters.getParameterProcessor();
        if (Objects.nonNull(parameterProcessor)) {
            jobParameterProcessorsMap.put(name, parameterProcessor);
        }

        map.put(name, parameters);
    }

    @Override
    public void registerTriggerListener(final Job j) {
        if (j instanceof AbstractJob) {
            final AbstractJob job = (AbstractJob) j;
            job.setJobExecutionListeners(new JobExecutionListener[]{listener});
        } else {
            log.warn("Trigger support disabled for job with name '" + j + "'");
        }
    }

    @Override
    public JobTemplateParameters getJobTemplateParameters(String name){
        return map.get(name);
    }

    @Override
    public Set<String> getJobParameterNames() {
        return Collections.unmodifiableSet(map.keySet());
    }

    /**
     * @return the jobParameterProcessorsMap
     */
    public Map<String, JobParameterProcessor> getJobParameterProcessorsMap() {
        return jobParameterProcessorsMap;
    }

    /**
     * @param jobParameterProcessorsMap the jobParameterProcessorsMap to set
     */
    public void setJobParameterProcessorsMap(Map<String, JobParameterProcessor> jobParameterProcessorsMap) {
        this.jobParameterProcessorsMap = jobParameterProcessorsMap;
    }
}
