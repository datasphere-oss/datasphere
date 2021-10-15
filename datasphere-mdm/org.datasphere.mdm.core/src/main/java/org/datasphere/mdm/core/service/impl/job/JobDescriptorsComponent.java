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
package org.datasphere.mdm.core.service.impl.job;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.context.JobDescriptorsGetContext;
import org.datasphere.mdm.core.dto.job.JobDescriptorsGetResult;
import org.datasphere.mdm.core.service.job.CustomJobRegistry;
import org.datasphere.mdm.core.type.job.JobDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Mikhail Mikhailov on Jun 18, 2021
 * Job descriptors management.
 */
@Component
public class JobDescriptorsComponent {
    /**
     * The registry.
     */
    @Autowired
    private CustomJobRegistry jobRegistry;
    /**
     * Constructor.
     */
    public JobDescriptorsComponent() {
        super();
    }
    /**
     * Gets descriptors using supplied criteria.
     * @param ctx the context
     * @return result
     */
    public JobDescriptorsGetResult descriptors(JobDescriptorsGetContext ctx) {

        Collection<JobDescriptor> descriptors = Collections.emptyList();
        if (ctx.isAll()) {
            descriptors = jobRegistry.getDescriptorNames().stream()
                    .map(jobRegistry::getDescriptor)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else if (CollectionUtils.isNotEmpty(ctx.getNames())) {
            descriptors = ctx.getNames().stream()
                    .filter(Objects::nonNull)
                    .map(jobRegistry::getDescriptor)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        return new JobDescriptorsGetResult(descriptors);
    }
}
