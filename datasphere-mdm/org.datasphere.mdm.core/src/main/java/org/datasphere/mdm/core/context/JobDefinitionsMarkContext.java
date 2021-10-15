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

import java.util.Objects;

/**
 * @author Mikhail Mikhailov on Jul 4, 2021
 */
public class JobDefinitionsMarkContext extends AbstractJobDefinitionsContext {
    /**
     * True for enable, false for disable.
     */
    private final boolean error;
    /**
     * Constructor.
     */
    public JobDefinitionsMarkContext(JobDefinitionsMarkContextBuilder b) {
        super(b);
        this.error = b.error;
    }
    /**
     * @return the state
     */
    public boolean isError() {
        return error;
    }
    /**
     * @return builder
     */
    public static JobDefinitionsMarkContextBuilder builder() {
        return new JobDefinitionsMarkContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Jul 4, 2021
     * Builder.
     */
    public static class JobDefinitionsMarkContextBuilder extends AbstractJobDefinitionsContextBuilder<JobDefinitionsMarkContextBuilder> {
        /**
         * True for enable, false for disable.
         */
        private boolean error;
        /**
         * Constructor.
         */
        private JobDefinitionsMarkContextBuilder() {
            super();
        }
        /**
         * Stops forcibly all currently running job instances of this definitions.
         * @param error the state
         * @return self
         */
        public JobDefinitionsMarkContextBuilder error(boolean error) {
            this.error = error;
            return this;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public JobDefinitionsMarkContext build() {
            Objects.requireNonNull(this.jobDefinitionIds, "Job definition id(s) must not be null.");
            return new JobDefinitionsMarkContext(this);
        }
    }
}
