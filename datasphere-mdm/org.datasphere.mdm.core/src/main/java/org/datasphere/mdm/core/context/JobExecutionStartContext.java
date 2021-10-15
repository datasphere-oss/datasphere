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
package org.datasphere.mdm.core.context;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.datasphere.mdm.core.type.job.JobParameterDefinition;

/**
 * @author Mikhail Mikhailov on Jul 6, 2021
 * Start execution context.
 */
public class JobExecutionStartContext extends AbstractJobDefinitionContext {
    /**
     * The job name.
     * This can start a job without any definitions just using the supplied parameters.
     */
    private final String jobName;
    /**
     * Optional supplied parameters.
     */
    private final Map<String, JobParameterDefinition<?>> parameters;
    /**
     * Restart a job
     */
    private final boolean restart;
    /**
     * The job execution id, that can be used for restarting of a job.
     */
    private final Long jobExecutionId;
    /**
     * Constructor.
     * @param b
     */
    private JobExecutionStartContext(JobExecutionStartContextBuilder b) {
        super(b);
        this.jobName = b.jobName;
        this.parameters = b.parameters;
        this.restart = b.restart;
        this.jobExecutionId = b.jobExecutionId;
    }
    /**
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
    }
    /**
     * @return the parameters
     */
    public Map<String, JobParameterDefinition<?>> getParameters() {
        return Objects.isNull(parameters) ? Collections.emptyMap() : parameters;
    }
    /**
     * @return the restart
     */
    public boolean isRestart() {
        return restart;
    }
    /**
     * @return the jobExecutionId
     */
    public Long getJobExecutionId() {
        return jobExecutionId;
    }
    /**
     * Builder call.
     * @return builder
     */
    public static JobExecutionStartContextBuilder builder() {
        return new JobExecutionStartContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Jul 6, 2021
     * Start execution context builder.
     */
    public static class JobExecutionStartContextBuilder extends AbstractJobDefinitionContextBuilder<JobExecutionStartContextBuilder> {
        /**
         * The job name.
         * This can start a job without any definitions just using the supplied parameters.
         */
        private String jobName;
        /**
         * Optional supplied parameters.
         */
        private Map<String, JobParameterDefinition<?>> parameters;
        /**
         * Restart a job
         */
        private boolean restart;
        /**
         * The job execution id, that can be used for restarting of a job.
         */
        private Long jobExecutionId;
        /**
         * Constructor.
         */
        private JobExecutionStartContextBuilder() {
            super();
        }
        /**
         * Sets job name.
         * @param jobName the job name
         * @return self
         */
        public JobExecutionStartContextBuilder jobName(String jobName) {
            this.jobName = jobName;
            return self();
        }
        /**
         * Sets job name.
         * @param jobName the job name
         * @return self
         */
        public JobExecutionStartContextBuilder parameter(JobParameterDefinition<?>... parameters) {
            if (ArrayUtils.isNotEmpty(parameters)) {
                return parameters(Arrays.asList(parameters));
            }
            return self();
        }
        /**
         * Sets the execution id to restart.
         * Note: retsart flag must also be set to 'true'.
         * @param jobExecutionId the job execution id to restart
         * @return self
         */
        public JobExecutionStartContextBuilder jobExecutionId(Long jobExecutionId) {
            this.jobExecutionId = jobExecutionId;
            return self();
        }
        /**
         * Sets restart flag.
         * Note: jobExecutionId must not be null for this to work.
         * @param restart the restart state
         * @return self
         */
        public JobExecutionStartContextBuilder restart(boolean restart) {
            this.restart = restart;
            return self();
        }
        /**
         * Sets job name.
         * @param jobName the job name
         * @return self
         */
        public JobExecutionStartContextBuilder parameters(Collection<JobParameterDefinition<?>> parameters) {

            if (CollectionUtils.isNotEmpty(parameters)) {

                for (JobParameterDefinition<?> jpd : parameters) {

                    if (Objects.isNull(jpd)) {
                        continue;
                    }

                    if (Objects.isNull(this.parameters)) {
                        this.parameters = new HashMap<>();
                    }

                    this.parameters.put(jpd.getName(), jpd);
                }
            }

            return self();
        }
        /**
         * Builds the context.
         * {@inheritDoc}
         */
        @Override
        public JobExecutionStartContext build() {
            return new JobExecutionStartContext(this);
        }
    }
}
