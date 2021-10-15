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
package org.datasphere.mdm.core.service.impl.job;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.datasphere.mdm.core.service.impl.job.JobDescriptorInstance.JobDescriptorInstanceBuilder;
import org.datasphere.mdm.core.service.job.CustomJobRegistry;
import org.datasphere.mdm.core.type.job.JobDescriptor;
import org.datasphere.mdm.core.type.job.JobFraction;
import org.datasphere.mdm.core.type.job.JobParameterDescriptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.datasphere.mdm.system.service.AfterPlatformStartup;
import org.datasphere.mdm.system.type.touch.Touch;

/**
 * @author Mikhail Mikhailov on Jan 17, 2020
 */
public class JobDescriptorFactoryBean extends AbstractFactoryBean<JobDescriptor> implements AfterPlatformStartup {

    private JobDescriptorInstanceBuilder builder = JobDescriptorInstance.builder();

    private String jobName;

    public JobDescriptorFactoryBean() {
        super();
        setSingleton(true);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPlatformStartup() {

        // Try to gather
        BeanFactory bf = getBeanFactory();
        Objects.requireNonNull(bf, "Bean factory must not be null.");
        Objects.requireNonNull(jobName, "Job name must not be null.");

        CustomJobRegistry moduleService = bf.getBean(CustomJobRegistry.class);
        List<JobFraction> sources = moduleService.getFractions(jobName).stream()
            .sorted(Comparator.comparingInt(JobFraction::getOrder))
            .collect(Collectors.toList());

        try {

            JobDescriptorInstance instance = (JobDescriptorInstance) getObject();
            instance.setFractions(sources);

        } catch (Exception e) {
            logger.error("Failed to initialize modular support for job [" + jobName + "].", e);
        }
    }
    /**
     * Sets job name.
     * @param jobName the jobName to set
     */
    public void setJobName(String jobName) {
        // Save jobName also locally to enable fractions resolution.
        this.jobName = jobName;
        builder.jobName(jobName);
    }
    /**
     * Display name.
     * @param displayName the display name
     * @return self
     */
    public void setDisplayName(String displayName) {
        builder.displayName(displayName);
    }
    /**
     * Display name supplier (can be used for i18n purposes).
     * @param displayName the display name
     * @return self
     */
    public void setDisplayName(Supplier<String> displayNameSupplier) {
        builder.displayName(displayNameSupplier);
    }
    /**
     * Display description.
     * @param description the description
     * @return self
     */
    public void setDescription(String description) {
        builder.description(description);
    }
    /**
     * Display description (can be used for i18n purposes).
     * @param displayName the display name
     * @return self
     */
    public void setDescription(Supplier<String> description) {
        builder.description(description);
    }
    /**
     * Set the fractions directly.
     * @param fractions the fractions to set
     */
    public void setFractions(List<JobFraction> fractions) {
        builder.fractions(fractions);
    }
    /**
     * Adds parameter(s).
     * @param p the parameter(s) to add
     * @return self
     */
    public void setParameter(JobParameterDescriptor<?>... p) {
        builder.parameter(p);
    }
    /**
     * Adds parameter(s).
     * @param p the parameter(s) to add
     * @return self
     */
    public void setParameters(Collection<JobParameterDescriptor<?>> p) {
        builder.parameters(p);
    }
    /**
     * Adds touch(s).
     * @param t the touch(s) to add
     * @return self
     */
    public void setTouch(Touch<?>... t) {
        builder.touch(t);
    }
    /**
     * Adds touches(s).
     * @param t the touches(s) to add
     * @return self
     */
    public void setTouches(Collection<Touch<?>> t) {
        builder.touches(t);
    }
    /**
     * Adds fraction(s).
     * @param f the fraction(s) to add
     * @return self
     */
    public void setFraction(JobFraction... f) {
        builder.fraction(f);
    }
    /**
     * Adds fraction(s).
     * @param f the fraction(s) to add
     * @return self
     */
    public void setFractions(Collection<JobFraction> f) {
        builder.fractions(f);
    }
    /**
     * Modularity mark.
     * @param modular the mark
     * @return self
     */
    public void setModular(boolean modular) {
        builder.modular(modular);
    }
    /**
     * System mark.
     * @param system the mark
     * @return self
     */
    public void setSystem(boolean system) {
        builder.system(system);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<JobDescriptor> getObjectType() {
        return JobDescriptor.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected JobDescriptor createInstance() {
        return builder.build();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Objects.requireNonNull(jobName, "Job name must not be null.");
        super.afterPropertiesSet();
    }
}
