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

/**
 * @author Mikhail Mikhailov on Jul 6, 2021
 * Stop execution context.
 */
public class JobExecutionStopContext extends AbstractJobDefinitionContext {
    /**
     * The job name.
     * This can stop a system job.
     */
    private final String jobName;
    /**
     * The job execution id, that can be used for stopping of a job.
     */
    private final Long jobExecutionId;
    /**
     * Constructor.
     * @param b
     */
    private JobExecutionStopContext(JobExecutionStopContextBuilder b) {
        super(b);
        this.jobName = b.jobName;
        this.jobExecutionId = b.jobExecutionId;
    }
    /**
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
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
    public static JobExecutionStopContextBuilder builder() {
        return new JobExecutionStopContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Jul 6, 2021
     * Stop execution context builder.
     */
    public static class JobExecutionStopContextBuilder extends AbstractJobDefinitionContextBuilder<JobExecutionStopContextBuilder> {
        /**
         * The job name.
         * This can start a job without any definitions just using the supplied parameters.
         */
        private String jobName;
        /**
         * The job execution id, that can be used for stopping of a job.
         */
        private Long jobExecutionId;
        /**
         * Constructor.
         */
        private JobExecutionStopContextBuilder() {
            super();
        }
        /**
         * Sets job name.
         * @param jobName the job name
         * @return self
         */
        public JobExecutionStopContextBuilder jobName(String jobName) {
            this.jobName = jobName;
            return self();
        }
        /**
         * Sets the execution id to stop.
         * @param jobExecutionId the job execution id to stop
         * @return self
         */
        public JobExecutionStopContextBuilder jobExecutionId(Long jobExecutionId) {
            this.jobExecutionId = jobExecutionId;
            return self();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public JobExecutionStopContext build() {
            return new JobExecutionStopContext(this);
        }
    }
}
