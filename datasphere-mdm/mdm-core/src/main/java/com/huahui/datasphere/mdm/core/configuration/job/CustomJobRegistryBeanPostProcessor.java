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
 * Date: 11.05.2016
 */

package com.huahui.datasphere.mdm.core.configuration.job;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.GroupAwareJob;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

import com.huahui.datasphere.mdm.core.service.job.JobTemplateParameters;
import com.huahui.datasphere.mdm.core.service.job.JobWithParamsRegistry;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class CustomJobRegistryBeanPostProcessor implements BeanPostProcessor, InitializingBean, DisposableBean {
    private static Log logger = LogFactory.getLog(CustomJobRegistryBeanPostProcessor.class);

    private Collection<String> jobNames = new HashSet<>();

    private JobWithParamsRegistry jobRegistry = null;

    private String groupName = null;

    public void setJobWithParamsRegistry(JobWithParamsRegistry jobRegistry) {
        this.jobRegistry = jobRegistry;
    }

    /**
   	 * The group name for jobs registered by this component. Optional (defaults
   	 * to null, which means that jobs are registered with their bean names).
   	 * Useful where there is a hierarchy of application contexts all
   	 * contributing to the same {@link JobRegistry}: child contexts can then
   	 * define an instance with a unique group name to avoid clashes between job
   	 * names.
   	 *
   	 * @param groupName the groupName to set
   	 */
   	public void setGroupName(String groupName) {
   		this.groupName = groupName;
   	}

    /**
   	 * Make sure the registry is set before use.
   	 *
   	 * @see InitializingBean#afterPropertiesSet()
   	 */
   	@Override
   	public void afterPropertiesSet() throws Exception {
   		Assert.notNull(jobRegistry, "JobRegistry must not be null");
   	}

   	/**
   	 * Unregister all the {@link Job} instances that were registered by this
   	 * post processor.
   	 * @see DisposableBean#destroy()
   	 */
   	@Override
   	public void destroy() throws Exception {
   		for (String name : jobNames) {
   			if (logger.isDebugEnabled()) {
   				logger.debug("Unregistering job: " + name);
   			}
   			jobRegistry.unregister(name);
   		}
   		jobNames.clear();
   	}

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof JobTemplateParameters) {
            JobTemplateParameters parameters = (JobTemplateParameters) bean;

            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Registering job parameters: " + parameters.getJobName());
                }

                jobRegistry.register(parameters);
            }
            catch (DuplicateJobException e) {
                throw new FatalBeanException("Cannot register job parameters configuration", e);
            }
        } else if (bean instanceof Job) {
            jobRegistry.registerTriggerListener((Job) bean);

            Job job = (Job) bean;
            try {

                job = this.groupName == null ? job : new GroupAwareJob(this.groupName, job);
                ReferenceJobFactory jobFactory = new ReferenceJobFactory(job);
                String name = jobFactory.getJobName();
                if (logger.isDebugEnabled()) {
                    logger.debug("Registering job: " + name);
                }
                jobRegistry.register(jobFactory);
                jobNames.add(name);
            }
            catch (DuplicateJobException e) {
                throw new FatalBeanException("Cannot register job configuration", e);
            }
            return job;
        }

        return bean;
    }

    /**
   	 * Determine a group name for the job to be registered. Default
   	 * implementation just returns the {@link #setGroupName(String) groupName}
   	 * configured. Provides an extension point for specialised subclasses.
   	 *
   	 * @param beanDefinition the bean definition for the job
   	 * @param job the job
   	 * @return a group name for the job (or null if not needed)
   	 */
   	protected String getGroupName(BeanDefinition beanDefinition, Job job) {
   		return groupName;
   	}

   	/**
   	 * Do nothing.
   	 *
   	 * @see BeanPostProcessor#postProcessBeforeInitialization(Object,
   	 * String)
   	 */
   	@Override
   	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
   		return bean;
   	}
}
