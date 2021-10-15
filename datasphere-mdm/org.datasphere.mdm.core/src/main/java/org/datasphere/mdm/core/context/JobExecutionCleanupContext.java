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
 * @author Mikhail Mikhailov on Jul 7, 2021
 * Does cleanup in runtime tables for either a single execution id
 * or for the whole job (either name or definition id).
 */
public class JobExecutionCleanupContext extends AbstractJobDefinitionContext {
    /**
     * The job name.
     * This can stop a system job.
     */
    private final String jobName;
    /**
     * The job instance id to clean up.
     */
    private final Long jobInstanceId;
    /**
     * Constructor.
     * @param b
     */
    private JobExecutionCleanupContext(JobExecutionCleanupContextBuilder b) {
        super(b);
        this.jobName = b.jobName;
        this.jobInstanceId = b.jobInstanceId;
    }
    /**
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
    }
    /**
     * @return the jobInstanceId
     */
    public Long getJobInstanceId() {
        return jobInstanceId;
    }
    /**
     * The builder.
     * @return builder
     */
    public static JobExecutionCleanupContextBuilder builder() {
        return new JobExecutionCleanupContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Jul 7, 2021
     * Job cleanup conetxt builder.
     */
    public static class JobExecutionCleanupContextBuilder extends AbstractJobDefinitionContextBuilder<JobExecutionCleanupContextBuilder> {
        /**
         * The job name.
         * This can start a job without any definitions just using the supplied parameters.
         */
        private String jobName;
        /**
         * The job instance id to cleanup.
         */
        private Long jobInstanceId;
        /**
         * Constructor.
         */
        private JobExecutionCleanupContextBuilder() {
            super();
        }
        /**
         * Sets job name.
         * @param jobName the job name
         * @return self
         */
        public JobExecutionCleanupContextBuilder jobName(String jobName) {
            this.jobName = jobName;
            return self();
        }
        /**
         * Sets the instance id to cleanup.
         * @param jobInstanceId the job instance id to cleanup
         * @return self
         */
        public JobExecutionCleanupContextBuilder jobInstanceId(Long jobInstanceId) {
            this.jobInstanceId = jobInstanceId;
            return self();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public JobExecutionCleanupContext build() {
            return new JobExecutionCleanupContext(this);
        }
    }
}
