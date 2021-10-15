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
 */
public class JobDefinitionSchedulingContext extends AbstractJobDefinitionContext {
    /**
     * The cron expression.
     */
    private final String cronExpression;
    /**
     * Constructor.
     */
    private JobDefinitionSchedulingContext(JobDefinitionSchedulingContextBuilder b) {
        super(b);
        this.cronExpression = b.cronExpression;
    }
    /**
     * @return the cronExpression
     */
    public String getCronExpression() {
        return cronExpression;
    }
    /**
     * Builder instance.
     * @return builder instance
     */
    public static JobDefinitionSchedulingContextBuilder builder() {
        return new JobDefinitionSchedulingContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Jul 6, 2021
     * Builder.
     */
    public static class JobDefinitionSchedulingContextBuilder extends AbstractJobDefinitionContextBuilder<JobDefinitionSchedulingContextBuilder> {
        /**
         * The cron expression.
         */
        private String cronExpression;

        private JobDefinitionSchedulingContextBuilder() {
            super();
        }

        public JobDefinitionSchedulingContextBuilder cronExpression(String cronExpression) {
            this.cronExpression = cronExpression;
            return this;
        }

        @Override
        public JobDefinitionSchedulingContext build() {
            return new JobDefinitionSchedulingContext(this);
        }
    }
}
