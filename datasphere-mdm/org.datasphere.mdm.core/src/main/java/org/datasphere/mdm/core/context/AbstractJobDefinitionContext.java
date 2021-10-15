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
 * @author Mikhail Mikhailov on Jul 5, 2021
 * A single definition base context.
 */
public class AbstractJobDefinitionContext {
    /**
     * The job definition id.
     */
    protected final Long jobDefinitionId;
    /**
     * Parent execution id.
     */
    protected final Long parentJobExecutionId;
    /**
     * Constructor.
     */
    protected AbstractJobDefinitionContext(AbstractJobDefinitionContextBuilder<?> b) {
        super();
        this.jobDefinitionId = b.jobDefinitionId;
        this.parentJobExecutionId = b.parentJobExecutionId;
    }
    /**
     * @return the jobDefinitionId
     */
    public Long getJobDefinitionId() {
        return jobDefinitionId;
    }
    /**
     * @return the parentJobExecutionId
     */
    public Long getParentJobExecutionId() {
        return parentJobExecutionId;
    }
    /**
     * @author Mikhail Mikhailov on Jul 6, 2021
     * Base builder with id.
     */
    public abstract static class AbstractJobDefinitionContextBuilder<X extends AbstractJobDefinitionContextBuilder<X>> {
        /**
         * The definition id.
         */
        private Long jobDefinitionId;
        /**
         * Parent execution id.
         */
        private Long parentJobExecutionId;
        /**
         * Constructor.
         */
        protected AbstractJobDefinitionContextBuilder() {
            super();
        }
        /**
         * Sets definition id.
         * @param jobDefinitionId the definition id
         * @return self
         */
        public X jobDefinitionId(Long jobDefinitionId) {
            this.jobDefinitionId = jobDefinitionId;
            return self();
        }
        /**
         * Parent execution id.
         * @param parentJobExecutionId the execution id
         * @return self
         */
        public X parentJobExecutionId(Long parentJobExecutionId) {
            this.parentJobExecutionId = parentJobExecutionId;
            return self();
        }
        /**
         * The builder method override.
         * @return object
         */
        public abstract AbstractJobDefinitionContext build();
        /*
         * Cast self to X.
         * @return self
         */
        @SuppressWarnings("unchecked")
        protected X self() {
            return (X) this;
        }
    }
}
