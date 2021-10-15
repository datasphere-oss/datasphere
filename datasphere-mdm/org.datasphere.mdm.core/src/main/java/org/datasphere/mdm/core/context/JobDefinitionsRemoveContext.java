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
public class JobDefinitionsRemoveContext extends AbstractJobDefinitionsContext {
    /**
     * True for enable, false for disable.
     */
    private final boolean stop;
    /**
     * Constructor.
     */
    public JobDefinitionsRemoveContext(JobDefinitionsRemoveContextBuilder b) {
        super(b);
        this.stop = b.stop;
    }
    /**
     * @return the state
     */
    public boolean isStop() {
        return stop;
    }
    /**
     * @return builder
     */
    public static JobDefinitionsRemoveContextBuilder builder() {
        return new JobDefinitionsRemoveContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Jul 4, 2021
     * Builder.
     */
    public static class JobDefinitionsRemoveContextBuilder extends AbstractJobDefinitionsContextBuilder<JobDefinitionsRemoveContextBuilder> {
        /**
         * True for enable, false for disable.
         */
        private boolean stop;
        /**
         * Constructor.
         */
        private JobDefinitionsRemoveContextBuilder() {
            super();
        }
        /**
         * Stops forcibly all currently running job instances of this definitions.
         * @param stop the state
         * @return self
         */
        public JobDefinitionsRemoveContextBuilder stop(boolean stop) {
            this.stop = stop;
            return this;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public JobDefinitionsRemoveContext build() {
            Objects.requireNonNull(this.jobDefinitionIds, "Job definition id(s) must not be null.");
            return new JobDefinitionsRemoveContext(this);
        }
    }
}
