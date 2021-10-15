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
 * @author Mikhail Mikhailov on Jul 8, 2021
 */
public class StepExecutionQueryContext {
    /**
     * The execution id.
     */
    private final Long jobExecutionId;
    /**
     * The instance id
     */
    private final Long jobInstanceId;
    /**
     * Start id.
     */
    private final Long from;
    /**
     * Count
     */
    private final Integer count;
    /**
     * Constructor.
     */
    private StepExecutionQueryContext(StepExecutionQueryContextBuilder b) {
        super();
        this.jobExecutionId = b.jobExecutionId;
        this.jobInstanceId = b.jobInstanceId;
        this.from = b.from;
        this.count = b.count;
    }
    /**
     * @return the jobExecutionId
     */
    public Long getJobExecutionId() {
        return jobExecutionId;
    }
    /**
     * @return the jobInstanceId
     */
    public Long getJobInstanceId() {
        return jobInstanceId;
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
     * Builder rmethod.
     * @return builder
     */
    public static StepExecutionQueryContextBuilder builder() {
        return new StepExecutionQueryContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Jul 8, 2021
     * Builder.
     */
    public static class StepExecutionQueryContextBuilder {
        /**
         * The execution id.
         */
        private Long jobExecutionId;
        /**
         * The instance id
         */
        private Long jobInstanceId;
        /**
         * Start id.
         */
        private Long from;
        /**
         * Count
         */
        private Integer count;
        /**
         * Constructor.
         */
        private StepExecutionQueryContextBuilder() {
            super();
        }
        /**
         * Sets execution id.
         * @param jobExecutionId
         * @return self
         */
        public StepExecutionQueryContextBuilder jobExecutionId(Long jobExecutionId) {
            this.jobExecutionId = jobExecutionId;
            return this;
        }
        /**
         * Sets instance id.
         * @param jobInstanceId
         * @return self
         */
        public StepExecutionQueryContextBuilder jobInstanceId(Long jobInstanceId) {
            this.jobInstanceId = jobInstanceId;
            return this;
        }
        /**
         * Sets the from index.
         * @param from the from index
         * @return self
         */
        public StepExecutionQueryContextBuilder from(Long from) {
            this.from = from;
            return this;
        }
        /**
         * Sets the return count.
         * @param count the return count
         * @return self
         */
        public StepExecutionQueryContextBuilder count(Integer count) {
            this.count = count;
            return this;
        }
        /**
         * Instance.
         * @return instance
         */
        public StepExecutionQueryContext build() {
            return new StepExecutionQueryContext(this);
        }
    }
}
