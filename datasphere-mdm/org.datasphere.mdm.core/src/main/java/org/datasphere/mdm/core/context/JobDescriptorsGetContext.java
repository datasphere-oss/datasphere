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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author Mikhail Mikhailov on Jun 18, 2021
 */
public class JobDescriptorsGetContext {
    /**
     * Fetch all descriptors.
     */
    private final boolean all;
    /**
     * The job names to select fescriptors for.
     */
    private final Set<String> names;
    /**
     * Constructor.
     */
    private JobDescriptorsGetContext(JobDescriptorsGetContextBuilder b) {
        super();
        this.all = b.all;
        this.names = Objects.isNull(b.names) ? Collections.emptySet() : b.names;
    }
    /**
     * @return the all
     */
    public boolean isAll() {
        return all;
    }
    /**
     * @return the names
     */
    public Set<String> getNames() {
        return names;
    }
    /**
     * @return builder
     */
    public static JobDescriptorsGetContextBuilder builder() {
        return new JobDescriptorsGetContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Jun 18, 2021
     * Builder class.
     */
    public static class JobDescriptorsGetContextBuilder {
        /**
         * Fetch all descriptors.
         */
        private boolean all;
        /**
         * The job names to select fescriptors for.
         */
        private Set<String> names;
        /**
         * Constructor.
         */
        private JobDescriptorsGetContextBuilder() {
            super();
        }
        /**
         * Sets fech all mark.
         * @param value the mark
         * @return self
         */
        public JobDescriptorsGetContextBuilder all(boolean value) {
            this.all = value;
            return this;
        }
        /**
         * Sets fech all mark.
         * @param value the mark
         * @return self
         */
        public JobDescriptorsGetContextBuilder name(String name) {
            if (Objects.nonNull(name)) {
                if (names == null) {
                    names = new HashSet<>();
                }

                names.add(name);
            }
            return this;
        }
        /**
         * Sets fech all mark.
         * @param value the mark
         * @return self
         */
        public JobDescriptorsGetContextBuilder names(Collection<String> names) {
            if (CollectionUtils.isNotEmpty(names)) {
                for (String name : names) {
                    name(name);
                }
            }
            return this;
        }
        /**
         * Builder method.
         * @return context
         */
        public JobDescriptorsGetContext build() {
            return new JobDescriptorsGetContext(this);
        }
    }
}
