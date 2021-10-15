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

package org.datasphere.mdm.core.service;

import java.util.List;

import org.datasphere.mdm.core.context.JobDefinitionSchedulingContext;
import org.datasphere.mdm.core.context.JobDefinitionUpsertContext;
import org.datasphere.mdm.core.context.JobDefinitionsEnableContext;
import org.datasphere.mdm.core.context.JobDefinitionsMarkContext;
import org.datasphere.mdm.core.context.JobDefinitionsQueryContext;
import org.datasphere.mdm.core.context.JobDefinitionsRemoveContext;
import org.datasphere.mdm.core.context.JobDescriptorsGetContext;
import org.datasphere.mdm.core.context.JobExecutionQueryContext;
import org.datasphere.mdm.core.context.JobExecutionStartContext;
import org.datasphere.mdm.core.context.JobExecutionStatusContext;
import org.datasphere.mdm.core.context.JobExecutionStopContext;
import org.datasphere.mdm.core.context.StepExecutionQueryContext;
import org.datasphere.mdm.core.dto.job.JobDefinitionUpsertResult;
import org.datasphere.mdm.core.dto.job.JobDefinitionsQueryResult;
import org.datasphere.mdm.core.dto.job.JobDescriptorsGetResult;
import org.datasphere.mdm.core.dto.job.JobExecutionStartResult;
import org.datasphere.mdm.core.dto.job.JobExecutionStatusResult;
import org.datasphere.mdm.core.dto.job.JobExecutionsQueryResult;
import org.datasphere.mdm.core.dto.job.StepExecutionQueryResult;

/**
 * The service, implementing functionality around batch jobs.
 * The following types belong to the jobs subsystem:<br>
 * <ul>
 * <li> JobDescriptor - a required entity, describing a job, its parameters, fractions, etc. System jobs also must have a descriptor.</li>
 * <li> JobDefinition - a set of settings, once set up and saved for a job. Once saved, a JobDefinition can be run by id.</li>
 * <li> JobInstance - a Spring Batch entity, implementing a single run of a job. Objects of this type are used, but not managed by the platform.</li>
 * <li> JobExecution - a Spring Batch entity, implementing state of a JobInstance during execution of a job. Objects of this type are used, but not managed by the platform.</li>
 * </ul>
 */
public interface JobService {
    /**
     * Loads job descriptors, registered on the system.
     * @return job descriptors, registered on the system
     */
    JobDescriptorsGetResult descriptors(JobDescriptorsGetContext ctx);
    /**
     * Gets job definitions by supplied IDs or using supplied filtering criteria.
     * @param ctx selection criteria
     * @return result
     */
    JobDefinitionsQueryResult definitions(JobDefinitionsQueryContext ctx);
    /**
     * Does upsert (insert or update) of a job definition.
     * @param ctx the context
     * @return result (updated definition)
     */
    JobDefinitionUpsertResult upsert(JobDefinitionUpsertContext ctx);
    /**
     * Enables or disables a job definition.
     * @param ctx the context
     */
    void enable(JobDefinitionsEnableContext ctx);
    /**
     * Marks a definition as being in error (or not in error) state.
     * @param ctx the context
     */
    void mark(JobDefinitionsMarkContext ctx);
    /**
     * Removes a job definition from the system.
     * @param ctx the context
     */
    void remove(JobDefinitionsRemoveContext ctx);
    /**
     * Schedule unidata job definition.
     *
     * @param ctx the scheduling context.
     */
    void schedule(JobDefinitionSchedulingContext ctx);
    /**
     * UN-schedule unidata job definition.
     * @param ctx the scheduling context.
     */
    void unschedule(JobDefinitionSchedulingContext ctx);
    /**
     * Starts a new job execution, according to supplied parameters.
     * @param ctx the context
     * @return result
     */
    JobExecutionStartResult start(JobExecutionStartContext ctx);
    /**
     * Attempts to stop a job be either definitionId, or jobName or execution id.
     * @param ctx the stop context
     */
    void stop(JobExecutionStopContext ctx);
    /**
     * Loads executions (possibly filtered).
     * @param ctx the query context
     * @return result
     */
    JobExecutionsQueryResult executions(JobExecutionQueryContext ctx);
    /**
     * Loads step executions according to request context.
     * @param ctx the context
     * @return result
     */
    StepExecutionQueryResult steps(StepExecutionQueryContext ctx);
    /**
     * Gets a status of a single execution by either
     * definition id (latest is taken), name (for system jobs, latest is taken) or execution id. Also progress percentage is returned.
     * @param ctx the context
     * @return result
     */
    JobExecutionStatusResult status(JobExecutionStatusContext ctx);
    /**
     * @param complexParameter - any object which you want to pass to job
     * @return key in storage which help find complex parameter in partitioner
     */
    String putComplexParameter(Object complexParameter);
    /**
     * Loads all available tags.
     * @return tags without duplicates
     */
    List<String> tags();
}
