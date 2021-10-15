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
package org.datasphere.mdm.core.type.job;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.datasphere.mdm.system.type.touch.Touch;

/**
 * @author Mikhail Mikhailov on Jun 15, 2021
 * Job descriptor interface.
 */
public interface JobDescriptor {
    /**
     * Returns the JOB name.
     * This name is used for resolution and binding of the connected job.
     * This is important to give the right JOB bean name, because otherwise the job will not be found and this descriptor will not be bound.
     * @return the JOB name
     */
    String getJobName();
    /**
     * Gets display name of the job being described.
     * @return the job displayName
     */
    String getDisplayName();
    /**
     * Gets description of the job being described.
     * @return the job description
     */
    String getDescription();
    /**
     * Gets job's own parameter descriptors as collection.
     * @return job's own parameters
     */
    Collection<JobParameterDescriptor<?>> getParametersCollection();
    /**
     * Gets job's own parameter descriptors as map.
     * @return job's own parameters as map
     */
    Map<String, JobParameterDescriptor<?>> getParametersMap();
    /**
     * Gets job's own parameter descriptors + those, defined in fractions as map.
     * @return job's own parameters + those from descriptors as map
     */
    Map<String, JobParameterDescriptor<?>> getAllParameters();
    /**
     * Finds a parameter by name in either own parameters collection or in collected fractions.
     * @param name the parameter name
     * @return parameter descriptor or null
     */
    @Nullable
    JobParameterDescriptor<?> findParameter(String name);
    /**
     * Gets touch elements, that this job may export.
     * @return the touch elements
     */
    List<Touch<?>> getTouches();
    /**
     * Returns modular job fractions, that may exist for this job.
     * @return modular job fractions, that may exist for this job
     */
    List<JobFraction> getFractions();
    /**
     * Returns true, if this job supports modular execution.
     * @return true, if this job supports modular execution
     */
    boolean isModular();
    /**
     * System job indicator.
     * System jobs are not editable from UI (although visible).
     */
    boolean isSystem();
}