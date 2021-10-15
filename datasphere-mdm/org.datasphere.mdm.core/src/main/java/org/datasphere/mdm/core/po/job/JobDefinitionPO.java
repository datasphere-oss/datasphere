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

package org.datasphere.mdm.core.po.job;

import java.util.List;

import org.datasphere.mdm.core.po.AbstractObjectPO;

/**
 * Job PO from/to database.
 * @author Denis Kostovarov
 */
public class JobDefinitionPO extends AbstractObjectPO {
    /**
     * The table name.
     */
    public static final String TABLE_NAME = "job";
    /**
     * The job definition ID.
     */
    public static final String FIELD_ID = "id";
    /**
     * Name of the definition.
     */
    public static final String FIELD_NAME = "name";
    /**
     * Enabled mark.
     */
    public static final String FIELD_ENABLED = "enabled";
    /**
     * Error mark.
     */
    public static final String FIELD_ERROR = "error";
    /**
     * Cron expression.
     */
    public static final String FIELD_CRON_EXPRESSION = "cron_expression";
    /**
     * The SB job name.
     */
    public static final String FIELD_JOB_NAME = "job_name";
    /**
     * Description field.
     */
    public static final String FIELD_DESCRIPTION = "description";
    /**
     * Tags field.
     */
    public static final String FIELD_TAGS = "tags";
    /**
     * Parameters as JSON object.
     */
    public static final String FIELD_PARAMETERS = "parameters";
    /**
     * Parameters as JSON object.
     */
    public static final String FIELD_STATUS = "status";
    /**
     * This definition ID.
     */
    private Long id;
    /**
     * The real job name.
     */
    private String jobName;
    /**
     * This definition name (not the job name).
     */
    private String name;
    /**
     * This definition description.
     */
    private String description;
    /**
     * Enabled or not.
     */
    private boolean enabled;
    /**
     * In error state or not.
     */
    private boolean error;
    /**
     * CRON expression or null.
     */
    private String cronExpression;
    /**
     * Tags, that could be applied to the definition.
     */
    private List<String> tags;
    /**
     * Marshaled parameters as string.
     */
    private String parametersAsString;

    public JobDefinitionPO() {
        super();
    }

    public JobDefinitionPO(
            final Long id,
            final String name,
            final boolean enabled,
            final String cronExpression,
            final String jobNameReference,
            final String description) {

        this();
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.cronExpression = cronExpression;
        this.jobName = jobNameReference;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobNameReference) {
        this.jobName = jobNameReference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public List<String> getTags() {
    	return tags;
    }

    public void setTags(List<String> tags) {
    	this.tags = tags;
    }

    /**
     * @return the parametersAsString
     */
    public String getParametersAsString() {
        return parametersAsString;
    }

    /**
     * @param parametersAsString the parametersAsString to set
     */
    public void setParametersAsString(String parametersAsString) {
        this.parametersAsString = parametersAsString;
    }
}
