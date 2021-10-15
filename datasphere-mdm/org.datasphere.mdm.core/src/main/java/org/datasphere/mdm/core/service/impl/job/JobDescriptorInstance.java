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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.datasphere.mdm.core.type.job.JobDescriptor;
import org.datasphere.mdm.core.type.job.JobFraction;
import org.datasphere.mdm.core.type.job.JobParameterDescriptor;
import org.datasphere.mdm.system.type.touch.Touch;

/**
 * @author Mikhail Mikhailov on Jun 12, 2021
 * The job descriptor.
 */
class JobDescriptorInstance implements JobDescriptor {
    /**
     * Param name.
     */
    private final String jobName;
    /**
     * Display name.
     */
    private final Supplier<String> displayName;
    /**
     * Display description supplier (can be used for i18n purposes).
     */
    private final Supplier<String> descriptionSupplier;
    /**
     * Parameters.
     */
    private final Map<String, JobParameterDescriptor<?>> parameters;
    /**
     * Touches.
     */
    private final List<Touch<?>> touches;
    /**
     * Fractions.
     */
    private List<JobFraction> fractions;
    /**
     * Modularity support mark.
     */
    private final boolean modular;
    /**
     * System job indicator.
     * System jobs are not editable from UI (although visible).
     */
    private final boolean system;
    /**
     * Constructor.
     */
    private JobDescriptorInstance(JobDescriptorInstanceBuilder b) {
        super();
        this.jobName = b.jobName;
        this.displayName = Objects.isNull(b.displayNameSupplier) ? () -> b.displayName : b.displayNameSupplier;
        this.descriptionSupplier = Objects.isNull(b.descriptionSupplier) ? () -> b.description : b.descriptionSupplier;
        this.parameters = Objects.isNull(b.parameters) ? Collections.emptyMap() : b.parameters;
        this.touches = Objects.isNull(b.touches) ? Collections.emptyList() : new ArrayList<>(b.touches.values());
        this.fractions = Objects.isNull(b.fractions) ? null : new ArrayList<>(b.fractions.values());
        this.modular = b.modular;
        this.system = b.system;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getJobName() {
        return jobName;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
        return displayName.get();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return descriptionSupplier.get();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<JobParameterDescriptor<?>> getParametersCollection() {
        return Objects.isNull(parameters) ? Collections.emptyList() : parameters.values();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, JobParameterDescriptor<?>> getParametersMap() {
        return Objects.isNull(parameters) ? Collections.emptyMap() : parameters;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, JobParameterDescriptor<?>> getAllParameters() {

        Map<String, JobParameterDescriptor<?>> retval = new HashMap<>();

        retval.putAll(getParametersMap());
        if (Objects.nonNull(fractions)) {

            for (JobFraction fraction : fractions) {
                retval.putAll(fraction.getParametersMap());
            }
        }

        return retval;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public JobParameterDescriptor<?> findParameter(String name) {

        JobParameterDescriptor<?> hit = parameters.get(name);
        if (Objects.isNull(hit) && Objects.nonNull(fractions)) {

            for (JobFraction fraction : fractions) {

                hit = fraction.findParameter(name);
                if (Objects.nonNull(hit)) {
                    break;
                }
            }
        }

        return hit;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Touch<?>> getTouches() {
        return Objects.isNull(touches) ? Collections.emptyList() : touches;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<JobFraction> getFractions() {
        return Objects.isNull(fractions) ? Collections.emptyList() : fractions;
    }
    /**
     * Fractions are mutable.
     * @param fractions the fractions to set
     */
    public void setFractions(List<JobFraction> fractions) {
        if (CollectionUtils.isNotEmpty(fractions)) {
            if (Objects.isNull(this.fractions)) {
                this.fractions = new ArrayList<>();
            }
            this.fractions.addAll(fractions);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isModular() {
        return modular;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSystem() {
        return system;
    }
    /**
     * Builder instance.
     * @param jobName the JOB name.
     * @return builder instance
     */
    public static JobDescriptorInstanceBuilder builder() {
        return new JobDescriptorInstanceBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Jun 12, 2021
     * Parameter descriptor builder.
     */
    static class JobDescriptorInstanceBuilder {
        /**
         * Name.
         */
        private String jobName;
        /**
         * Display name.
         */
        private String displayName;
        /**
         * Display name supplier (can be used for i18n purposes).
         */
        private Supplier<String> displayNameSupplier;
        /**
         * Display description.
         */
        private String description;
        /**
         * Display description supplier (can be used for i18n purposes).
         */
        private Supplier<String> descriptionSupplier;
        /**
         * Parameters.
         */
        private Map<String, JobParameterDescriptor<?>> parameters;
        /**
         * Touches.
         */
        private Map<String, Touch<?>> touches;
        /**
         * Fractions.
         */
        private Map<String, JobFraction> fractions;
        /**
         * Modularity support mark.
         */
        private boolean modular;
        /**
         * System job indicator.
         * System jobs are not editable from UI (although visible).
         */
        private boolean system;
        /**
         * Private ctor.
         */
        private JobDescriptorInstanceBuilder() {
            super();
        }
        /**
         * Job bean name.
         * @param jobName the JOB name
         * @return self
         */
        public JobDescriptorInstanceBuilder jobName(String jobName) {
            this.jobName = jobName;
            return this;
        }
        /**
         * Display name.
         * @param displayName the display name
         * @return self
         */
        public JobDescriptorInstanceBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }
        /**
         * Display name supplier (can be used for i18n purposes).
         * @param displayName the display name
         * @return self
         */
        public JobDescriptorInstanceBuilder displayName(Supplier<String> displayNameSupplier) {
            this.displayNameSupplier = displayNameSupplier;
            return this;
        }
        /**
         * Display description.
         * @param description the description
         * @return self
         */
        public JobDescriptorInstanceBuilder description(String description) {
            this.description = description;
            return this;
        }
        /**
         * Display description (can be used for i18n purposes).
         * @param displayName the display name
         * @return self
         */
        public JobDescriptorInstanceBuilder description(Supplier<String> description) {
            this.descriptionSupplier = description;
            return this;
        }
        /**
         * Adds parameter(s).
         * @param p the parameter(s) to add
         * @return self
         */
        public JobDescriptorInstanceBuilder parameter(JobParameterDescriptor<?>... p) {
            if (ArrayUtils.isNotEmpty(p)) {
                return parameters(Arrays.asList(p));
            }
            return this;
        }
        /**
         * Adds parameter(s).
         * @param p the parameter(s) to add
         * @return self
         */
        public JobDescriptorInstanceBuilder parameters(Collection<JobParameterDescriptor<?>> p) {
            if (CollectionUtils.isNotEmpty(p)) {

                if (Objects.isNull(parameters)) {
                    parameters = new HashMap<>();
                }

                for (JobParameterDescriptor<?> param : p) {
                    parameters.put(param.getName(), param);
                }
            }
            return this;
        }
        /**
         * Adds touch(s).
         * @param t the touch(s) to add
         * @return self
         */
        public JobDescriptorInstanceBuilder touch(Touch<?>... t) {
            if (ArrayUtils.isNotEmpty(t)) {
                return touches(Arrays.asList(t));
            }
            return this;
        }
        /**
         * Adds touches(s).
         * @param t the touches(s) to add
         * @return self
         */
        public JobDescriptorInstanceBuilder touches(Collection<Touch<?>> t) {
            if (CollectionUtils.isNotEmpty(t)) {

                if (Objects.isNull(touches)) {
                    touches = new HashMap<>();
                }

                for (Touch<?> touch : t) {
                    touches.put(touch.getTouchName(), touch);
                }
            }
            return this;
        }
        /**
         * Adds fraction(s).
         * @param f the fraction(s) to add
         * @return self
         */
        public JobDescriptorInstanceBuilder fraction(JobFraction... f) {
            if (ArrayUtils.isNotEmpty(f)) {
                return fractions(Arrays.asList(f));
            }
            return this;
        }
        /**
         * Adds fraction(s).
         * @param f the fraction(s) to add
         * @return self
         */
        public JobDescriptorInstanceBuilder fractions(Collection<JobFraction> f) {
            if (CollectionUtils.isNotEmpty(f)) {

                if (Objects.isNull(fractions)) {
                    fractions = new HashMap<>();
                }

                for (JobFraction fraction : f) {
                    fractions.put(fraction.getId(), fraction);
                }
            }
            return this;
        }
        /**
         * Modularity mark.
         * @param modular the mark
         * @return self
         */
        public JobDescriptorInstanceBuilder modular(boolean modular) {
            this.modular = modular;
            return this;
        }
        /**
         * System job indicator.
         * System jobs are not editable from UI (although visible).
         * @param system the mark
         * @return self
         */
        public JobDescriptorInstanceBuilder system(boolean system) {
            this.system = system;
            return this;
        }
        /**
         * Builder method.
         * @return Job descriptor instance
         */
        public JobDescriptor build() {
            Objects.requireNonNull(jobName, "Job name must not be null.");
            return new JobDescriptorInstance(this);
        }
    }
}
