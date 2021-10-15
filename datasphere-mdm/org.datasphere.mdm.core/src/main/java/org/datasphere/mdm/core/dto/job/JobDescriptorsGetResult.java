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
package org.datasphere.mdm.core.dto.job;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.datasphere.mdm.core.type.job.JobDescriptor;

/**
 * @author Mikhail Mikhailov on Jun 18, 2021
 */
public class JobDescriptorsGetResult {
    /**
     * The payload.
     */
    private Collection<JobDescriptor> descriptors;
    /**
     * Constructor.
     */
    public JobDescriptorsGetResult() {
        super();
    }
    /**
     * Constructor.
     */
    public JobDescriptorsGetResult(Collection<JobDescriptor> descriptors) {
        this();
        this.descriptors = descriptors;
    }
    /**
     * @return the descriptors
     */
    public Collection<JobDescriptor> getDescriptors() {
        return Objects.isNull(descriptors) ? Collections.emptyList() : descriptors;
    }
    /**
     * @param descriptors the descriptors to set
     */
    public void setDescriptors(Collection<JobDescriptor> descriptors) {
        this.descriptors = descriptors;
    }
}
