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
 * Date: 11.05.2016
 */

package org.datasphere.mdm.core.service.impl.job;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.exception.JobException;
import org.datasphere.mdm.core.service.job.CustomJobRegistry;
import org.datasphere.mdm.core.service.job.JobFairPartitionHandler;
import org.datasphere.mdm.core.service.job.JobTargetedPartitionHandler;
import org.datasphere.mdm.core.type.annotation.JobRef;
import org.datasphere.mdm.core.type.job.JobDescriptor;
import org.datasphere.mdm.core.type.job.JobFraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.PartitionStep;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.step.AbstractStep;
import org.springframework.batch.core.step.item.ChunkOrientedTasklet;
import org.springframework.batch.core.step.item.ChunkProcessor;
import org.springframework.batch.core.step.item.ChunkProvider;
import org.springframework.batch.core.step.item.SimpleChunkProcessor;
import org.springframework.batch.core.step.item.SimpleChunkProvider;
import org.springframework.batch.core.step.job.JobStep;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.datasphere.mdm.system.configuration.ModuleConfiguration;
import org.datasphere.mdm.system.service.AfterPlatformStartup;

/**
 * @author amagdenko<br>
 * Registers normal {@link Job}, {@link JobFraction} and also {@link JobDescriptor} beans at the registry.
 */
public class CustomJobRegistryBeanPostProcessor implements BeanPostProcessor, InitializingBean, DisposableBean, AfterPlatformStartup {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomJobRegistryBeanPostProcessor.class);

    private CustomJobRegistry jobRegistry = null;

    public void setJobRegistry(CustomJobRegistry jobRegistry) {
        this.jobRegistry = jobRegistry;
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

        if (Objects.isNull(jobRegistry)) {
            return;
        }

        for (String name : jobRegistry.getJobNames()) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Unregistering job [{}].", name);
            }

            jobRegistry.unregister(name);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Object result = checkDescriptor(bean);
        if (Objects.nonNull(result)) {
            return result;
        }

        result = checkFraction(bean);
        if (Objects.nonNull(result)) {
            return result;
        }

        result = checkJob(bean);
        if (Objects.nonNull(result)) {
            return result;
        }

        return bean;
    }

    /**
     * This is used to verify pairity of jobs and descriptors.
     * If unpaired objects found, throws and renders platform non-functioning.
     */
    @Override
    public void afterPlatformStartup() {

        List<String> failures = new ArrayList<>();
        for (String jobName : jobRegistry.getJobNames()) {

            JobDescriptor descriptor = jobRegistry.getDescriptor(jobName);
            if (Objects.isNull(descriptor)) {
                failures.add(jobName);
            } else {
                wireIfModular(descriptor);
            }
        }

        if (CollectionUtils.isNotEmpty(failures)) {
            final String message = "Discovered job(s) [{}] without a valid descriptor. Stopping.";
            LOGGER.error(message, failures);
            throw new JobException(message, CoreExceptionIds.EX_JOB_DESCRIPTOR_MISSING, failures);
        }
    }

    private void wireIfModular(JobDescriptor descriptor) {

        if (!descriptor.isModular()) {
            return;
        }

        try {

            // A safe cast, since we do not create GroupAwareJob(s).
            AbstractJob job = (AbstractJob) jobRegistry.getJob(descriptor.getJobName());

            job.registerJobExecutionListener(new ModularJobExecutionListener(descriptor.getJobName()));
            for (String stepName : job.getStepNames()) {
                wireStep(descriptor, (AbstractStep) job.getStep(stepName));
            }

        } catch (Exception e) {
            LOGGER.error("Failed to find the job [{}].", descriptor.getJobName(), e);
        }
    }

    private void wireStep(JobDescriptor descriptor, AbstractStep step) {

        boolean isJobStep = (step instanceof JobStep);
        boolean isTaskletStep = (step instanceof TaskletStep);
        boolean isPartitionStep = (step instanceof PartitionStep);
        if (isJobStep || isTaskletStep) {

            step.registerStepExecutionListener(new ModularStepExecutionListener(descriptor.getJobName(), step.getName()));
            if (isTaskletStep) {

                Tasklet tasklet = ((TaskletStep) step).getTasklet();
                if (tasklet instanceof ChunkOrientedTasklet) {
                    wireTasklet(descriptor, step, (ChunkOrientedTasklet<?>) tasklet);
                }
            }

        } else if (isPartitionStep) {
            wirePartitionStep(descriptor, (PartitionStep) step);
        }
    }

    // Bad things
    private void wirePartitionStep(JobDescriptor descriptor, PartitionStep step) {

        Field ph = ReflectionUtils.findField(PartitionStep.class, "partitionHandler", PartitionHandler.class);
        if (Objects.nonNull(ph)) {

            ReflectionUtils.makeAccessible(ph);
            PartitionHandler h = (PartitionHandler) ReflectionUtils.getField(ph, step);

            if (Objects.nonNull(h)) {

                AbstractStep s = null;
                if (h instanceof TaskExecutorPartitionHandler) {
                    s = (AbstractStep) ((TaskExecutorPartitionHandler) h).getStep();
                } else if (h instanceof JobFairPartitionHandler) {
                    s = extractStep(((JobFairPartitionHandler) h).getStepName());
                } else if (h instanceof JobTargetedPartitionHandler) {
                    s = extractStep(((JobTargetedPartitionHandler) h).getStepName());
                }

                if (Objects.nonNull(s)) {
                    wireStep(descriptor, s);
                }
            }
        }
    }

    private void wireTasklet(JobDescriptor descriptor, AbstractStep step, ChunkOrientedTasklet<?> chot) {

        SimpleChunkProvider<?> provider = null;
        SimpleChunkProcessor<?, ?> processor = null;

        Field chunkProviderField = ReflectionUtils.findField(ChunkOrientedTasklet.class, "chunkProvider", ChunkProvider.class);
        if (Objects.nonNull(chunkProviderField)) {

            ReflectionUtils.makeAccessible(chunkProviderField);
            ChunkProvider<?> p = (ChunkProvider<?>) ReflectionUtils.getField(chunkProviderField, chot);

            if (Objects.nonNull(p) && (p instanceof SimpleChunkProvider)) {
                provider = (SimpleChunkProvider<?>) p;
            }
        }

        Field chunkProcessorField = ReflectionUtils.findField(ChunkOrientedTasklet.class, "chunkProcessor", ChunkProcessor.class);
        if (Objects.nonNull(chunkProcessorField)) {

            ReflectionUtils.makeAccessible(chunkProcessorField);
            ChunkProcessor<?> p = (ChunkProcessor<?>) ReflectionUtils.getField(chunkProcessorField, chot);

            if (Objects.nonNull(p) && (p instanceof SimpleChunkProcessor)) {
                processor = (SimpleChunkProcessor<?, ?>) p;
            }
        }

        if (Objects.nonNull(provider)) {
            provider.registerListener(new ModularReadListener(descriptor.getJobName(), step.getName()));
        }

        if (Objects.nonNull(processor)) {
            processor.registerListener(new ModularProcessListener(descriptor.getJobName(), step.getName()));
            processor.registerListener(new ModularWriteListener(descriptor.getJobName(), step.getName()));
        }
    }

    // Let fail, if bean not found
    private AbstractStep extractStep(String name) {
        return (AbstractStep) ModuleConfiguration.getBean(name, Step.class);
    }

    private Object checkJob(Object bean) {

        if (bean instanceof Job) {

            Job job = (Job) bean;
            try {
                jobRegistry.register(new ReferenceJobFactory(job));
            } catch (DuplicateJobException e) {
                throw new FatalBeanException("Cannot register job configuration", e);
            }

            return job;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private Object checkDescriptor(Object bean) {

        JobDescriptor descriptor = null;
        if (bean instanceof JobDescriptor) {
            descriptor = (JobDescriptor) bean;
        } else if (bean instanceof FactoryBean && (JobDescriptor.class.isAssignableFrom(((FactoryBean<?>) bean).getObjectType()))) {
            try {
                descriptor = ((FactoryBean<JobDescriptor>) bean).getObject();
            } catch (Exception e) {
                throw new FatalBeanException("Failed to get job descriptor instance.", e);
            }
        }

        if (Objects.nonNull(descriptor)) {
            jobRegistry.register(descriptor);
            return bean;
        }

        return null;
    }

    private Object checkFraction(Object bean) {

        if (bean instanceof JobFraction) {

            JobFraction fraction = (JobFraction) bean;

            JobRef ref = fraction.getClass().getAnnotation(JobRef.class);
            String jobName = Objects.isNull(ref) ? null : ref.value();

            if (Objects.isNull(jobName) || StringUtils.isBlank(jobName)) {
                LOGGER.warn("Discovered job fraction with id [{}] without proper job reference. Ignoring.", fraction.getId());
            } else {
                jobRegistry.register(jobName, fraction);
            }

            return bean;
        }

        return null;
    }
}
