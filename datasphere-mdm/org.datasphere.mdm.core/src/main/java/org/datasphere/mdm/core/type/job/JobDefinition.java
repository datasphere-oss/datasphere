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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.batch.core.Job;

/**
 * @author Denis Kostovarov
 */
public class JobDefinition {
    /**
     * The job definition ID.
     */
    private Long id;
    /**
     * The SB job bean (and the whole job) name.
     */
    private String jobName;
    /**
     * The name of the definition (job settings with parameters).
     * Do not confuse this with job name, that is used in {@link Job} and {@link JobDescriptor}.
     */
    private String displayName;
    /**
     * Definition's description.
     */
    private String description;
    /**
     * Definition is allowed to execute if true.
     */
    private boolean enabled;
    /**
     * Definition is in error state.
     */
    private boolean error;
    /**
     * Definition is executed periodically as cron job.
     */
    private String cronExpression;
    /**
     * The job definition is marked by the following tags.
     */
    private List<String> tags;
    /**
     * Parameters.
     */
    private Map<String, JobParameterDefinition<?>> parameters;
    /**
     * Date, at what the last edition was created.
     */
    private Date updateDate;
    /**
     * Date, at what the draft and first edition was created.
     */
    private Date createDate;
    /**
     * The creator's user name.
     */
    private String createdBy;
    /**
     * The updater's user name.
     */
    private String updatedBy;
    /**
     * Constructor.
     */
    public JobDefinition() {
        super();
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
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

    public Collection<JobParameterDefinition<?>> getParametersCollection() {
        return Objects.isNull(parameters) ? Collections.emptySet() : parameters.values();
    }

    public Map<String, JobParameterDefinition<?>> getParametersMap() {
        return Objects.isNull(parameters) ? Collections.emptyMap() : parameters;
    }

    public void setParameters(Map<String, JobParameterDefinition<?>> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(JobParameterDefinition<?> param) {

        if (Objects.nonNull(param)) {

            if (parameters == null) {
                parameters = new HashMap<>();
            }

            parameters.put(param.getName(), param);
        }
    }

    /**
     * Gets the create date.
     * @return date
     */
    public Date getCreateDate() {
        return createDate;
    }
    /**
     * Sets create date.
     * @param createDate the date
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * Gets the name of the user, who created the definition.
     * @return name
     */
    public String getCreatedBy() {
        return createdBy;
    }
    /**
     * Sets the name of the user, who created the definition.
     * @param createdBy the name
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /**
     * Gets the name of the user, who updated the definition.
     * @return name
     */
    public String getUpdatedBy() {
        return updatedBy;
    }
    /**
     * Sets the name of the user, who updated the definition.
     * @param updatedBy the name
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    /**
     * Gets the update date
     * @return date
     */
    public Date getUpdateDate() {
        return updateDate;
    }
    /**
     * Sets the update date
     * @param updateDate the date
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
