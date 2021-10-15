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
 * Execution query context
 */
public class JobExecutionQueryContext extends AbstractJobDefinitionContext {
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
     * Start id.
     */
    private final Long from;
    /**
     * Count
     */
    private final Integer count;
    /**
     * Does resartable state processing, if set to true.
     */
    private final boolean restartable;
    /**
     * Constructor.
     * @param b
     */
    private JobExecutionQueryContext(JobExecutionQueryContextBuilder b) {
        super(b);
        this.jobName = b.jobName;
        this.jobExecutionId = b.jobExecutionId;
        this.from = b.from;
        this.count = b.count;
        this.restartable = b.restartable;
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
     * @return the from
     */
    public Long getFrom() {
        return from;
    }
    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }
    /**
     * @return the restartable
     */
    public boolean isRestartable() {
        return restartable;
    }
    /**
     * The builder.
     * @return builder
     */
    public static JobExecutionQueryContextBuilder builder() {
        return new JobExecutionQueryContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Jul 7, 2021
     * Job execution query builder.
     */
    public static class JobExecutionQueryContextBuilder extends AbstractJobDefinitionContextBuilder<JobExecutionQueryContextBuilder> {
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
         * Start id.
         */
        private Long from;
        /**
         * Count
         */
        private Integer count;
        /**
         * Does resartable state processing, if set to true.
         */
        private boolean restartable;
        /**
         * Constructor.
         */
        private JobExecutionQueryContextBuilder() {
            super();
        }
        /**
         * Sets job name.
         * @param jobName the job name
         * @return self
         */
        public JobExecutionQueryContextBuilder jobName(String jobName) {
            this.jobName = jobName;
            return self();
        }
        /**
         * Sets the execution id to stop.
         * @param jobExecutionId the job execution id to stop
         * @return self
         */
        public JobExecutionQueryContextBuilder jobExecutionId(Long jobExecutionId) {
            this.jobExecutionId = jobExecutionId;
            return self();
        }
        /**
         * Sets the from index.
         * @param from the from index
         * @return self
         */
        public JobExecutionQueryContextBuilder from(Long from) {
            this.from = from;
            return self();
        }
        /**
         * Sets the return count.
         * @param count the return count
         * @return self
         */
        public JobExecutionQueryContextBuilder count(Integer count) {
            this.count = count;
            return self();
        }
        /**
         * Does resartable state processing, if set to true.
         * @param restartable the state
         * @return self
         */
        public JobExecutionQueryContextBuilder restartable(boolean restartable) {
            this.restartable = restartable;
            return self();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public JobExecutionQueryContext build() {
            return new JobExecutionQueryContext(this);
        }
    }
}
