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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.datasphere.mdm.core.type.job.JobExecutionStatus;

/**
 * @author Mikhail Mikhailov on Jun 18, 2021
 */
public class JobDefinitionsQueryContext extends AbstractJobDefinitionsContext {
    /**
     * Fetch only active definitions.
     */
    private final boolean activeOnly;
    /**
     * Fetch only inactive definitions.
     */
    private final boolean inactiveOnly;
    /**
     * The SB job names to select definitions for.
     */
    private final Set<String> jobNames;
    /**
     * The job definition names to select definitions for.
     */
    private final Set<String> definitionNames;
    /**
     * The job tags to select definitions for.
     */
    private final Set<String> tags;
    /**
     * Created by.
     */
    private final String createdBy;
    /**
     * Last finished with.
     */
    private final JobExecutionStatus lastFinishedWith;
    /**
     * Start id.
     */
    private final Long from;
    /**
     * Count
     */
    private final Integer count;
    /**
     * Sort settings, where key is the field name, and the value is "ASC" or "DESC".
     */
    private final Pair<String, String> sorting;
    /**
     * Load all available definitions.
     */
    private final boolean all;
    /**
     * Constructor.
     */
    private JobDefinitionsQueryContext(JobDescriptorsQueryContextBuilder b) {
        super(b);
        this.activeOnly = b.activeOnly;
        this.inactiveOnly = b.inactiveOnly;
        this.all = b.all;
        this.jobNames = Objects.isNull(b.jobNames) ? Collections.emptySet() : b.jobNames;
        this.definitionNames = Objects.isNull(b.definitionNames) ? Collections.emptySet() : b.definitionNames;
        this.tags = Objects.isNull(b.tags) ? Collections.emptySet() : b.tags;
        this.createdBy = b.createdBy;
        this.lastFinishedWith = b.lastFinishedWith;
        this.from = b.from;
        this.count = b.count;
        this.sorting = Objects.isNull(b.sorting) ? ImmutablePair.nullPair() : b.sorting;
    }
    /**
     * @return the activeOnly
     */
    public boolean isActiveOnly() {
        return activeOnly;
    }
    /**
     * @return the inactiveOnly
     */
    public boolean isInactiveOnly() {
        return inactiveOnly;
    }
    /**
     * @return the all
     */
    public boolean isAll() {
        return all;
    }
    /**
     * @return the jobNames
     */
    public Set<String> getJobNames() {
        return jobNames;
    }
    /**
     * @return the definitionNames
     */
    public Set<String> getDefinitionNames() {
        return definitionNames;
    }
    /**
     * @return the tags
     */
    public Set<String> getTags() {
        return tags;
    }
    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }
    /**
     * @return the lastFinishedWith
     */
    public JobExecutionStatus getLastFinishedWith() {
        return lastFinishedWith;
    }
    /**
     * @return the from
     */
    public Long getFrom() {
        return from;
    }
    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }
    /**
     * @return the sorting
     */
    public Pair<String, String> getSorting() {
        return sorting;
    }
    /**
     * @return builder
     */
    public static JobDescriptorsQueryContextBuilder builder() {
        return new JobDescriptorsQueryContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Jun 18, 2021
     * Builder class.
     */
    public static class JobDescriptorsQueryContextBuilder extends AbstractJobDefinitionsContextBuilder<JobDescriptorsQueryContextBuilder> {
        /**
         * Fetch only active definitions.
         */
        private boolean activeOnly;
        /**
         * Fetch only inactive definitions.
         */
        private boolean inactiveOnly;
        /**
         * The SB job names to select definitions for.
         */
        private Set<String> jobNames;
        /**
         * The job definition names to select definitions for.
         */
        private Set<String> definitionNames;
        /**
         * The job tags to select definitions for.
         */
        private Set<String> tags;
        /**
         * Created by.
         */
        private String createdBy;
        /**
         * Start id.
         */
        private Long from;
        /**
         * Count
         */
        private Integer count;
        /**
         * Last finished with.
         */
        private JobExecutionStatus lastFinishedWith;
        /**
         * Sort settings, where key is the field name, and the value is "ASC" or "DESC".
         */
        private Pair<String, String> sorting;
        /**
         * Load all available definitions.
         */
        private boolean all;
        /**
         * Constructor.
         */
        private JobDescriptorsQueryContextBuilder() {
            super();
        }
        /**
         * Select definition, which last run was finished with.
         * @param value the mark
         * @return self
         */
        public JobDescriptorsQueryContextBuilder lastFinishedWith(JobExecutionStatus value) {
            this.lastFinishedWith = value;
            return this;
        }
        /**
         * Sets fetch active only mark.
         * @param value the mark
         * @return self
         */
        public JobDescriptorsQueryContextBuilder activeOnly(boolean value) {
            this.activeOnly = value;
            return this;
        }
        /**
         * Sets fetch inactive only mark.
         * @param value the mark
         * @return self
         */
        public JobDescriptorsQueryContextBuilder inactiveOnly(boolean value) {
            this.inactiveOnly = value;
            return this;
        }
        /**
         * Sets createdBy fetch criteria.
         * @param value the criteria
         * @return self
         */
        public JobDescriptorsQueryContextBuilder createdBy(String value) {
            this.createdBy = value;
            return this;
        }
        /**
         * Sets fech all mark.
         * @param value the mark
         * @return self
         */
        public JobDescriptorsQueryContextBuilder all(boolean value) {
            this.all = value;
            return this;
        }
        /**
         * Sets fech all mark.
         * @param value the mark
         * @return self
         */
        public JobDescriptorsQueryContextBuilder definitionName(String name) {
            if (Objects.nonNull(name)) {
                if (definitionNames == null) {
                    definitionNames = new HashSet<>();
                }

                definitionNames.add(name);
            }
            return this;
        }
        /**
         * Sets fech all mark.
         * @param value the mark
         * @return self
         */
        public JobDescriptorsQueryContextBuilder definitionNames(Collection<String> names) {
            if (CollectionUtils.isNotEmpty(names)) {
                for (String name : names) {
                    definitionName(name);
                }
            }
            return this;
        }
        /**
         * Select by SB job name.
         * @param value the mark
         * @return self
         */
        public JobDescriptorsQueryContextBuilder jobName(String jobName) {
            if (Objects.nonNull(jobName)) {
                if (jobNames == null) {
                    jobNames = new HashSet<>();
                }

                jobNames.add(jobName);
            }
            return this;
        }
        /**
         * Select by SB job name.
         * @param value the mark
         * @return self
         */
        public JobDescriptorsQueryContextBuilder jobNames(Collection<String> jobNames) {
            if (CollectionUtils.isNotEmpty(jobNames)) {
                for (String name : jobNames) {
                    jobName(name);
                }
            }
            return this;
        }
        /**
         * Sets tags.
         * @param value the mark
         * @return self
         */
        public JobDescriptorsQueryContextBuilder tag(String tag) {
            if (Objects.nonNull(tag)) {
                if (tags == null) {
                    tags = new HashSet<>();
                }

                tags.add(tag);
            }
            return this;
        }
        /**
         * Sets tags.
         * @param value the mark
         * @return self
         */
        public JobDescriptorsQueryContextBuilder tags(Collection<String> tags) {
            if (CollectionUtils.isNotEmpty(tags)) {
                for (String name : tags) {
                    tag(name);
                }
            }
            return this;
        }
        /**
         * Sets sorting.
         * @param value the mark
         * @return self
         */
        public JobDescriptorsQueryContextBuilder sortBy(String field, String order) {
            if (Objects.nonNull(field)) {
                sorting = Pair.of(field, order);
            }
            return this;
        }
        /**
         * Return starting from the given id.
         * @param value the value
         * @return self
         */
        public JobDescriptorsQueryContextBuilder from(Long value) {
            this.from = value;
            return this;
        }
        /**
         * Return the given number of elements.
         * @param value the value
         * @return self
         */
        public JobDescriptorsQueryContextBuilder count(Integer value) {
            this.count = value;
            return this;
        }
        /**
         * Builder method.
         * @return context
         */
        @Override
        public JobDefinitionsQueryContext build() {
            return new JobDefinitionsQueryContext(this);
        }
    }
}
