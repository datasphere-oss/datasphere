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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.system.context.CommonRequestContext;

/**
 * @author Mikhail Mikhailov
 * Deletes LOB.
 */
public class DeleteLargeObjectContext extends CommonRequestContext {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = 5088228957783645486L;
    /**
     * ID of the BLOB/CLOB record.
     */
    private final UUID largeObjectId;
    /**
     * The subject ID.
     */
    private final String subjectId;
    /**
     * Binary or character data.
     */
    private final boolean binary;
    /**
     * An arbitrary set of searchable tags.
     */
    private final Set<String> tags;
    /**
     * Constructor.
     */
    private DeleteLargeObjectContext(DeleteLargeObjectContextBuilder b) {
        super(b);
        this.largeObjectId = b.largeObjectId;
        this.subjectId = b.subjectId;
        this.binary = b.binary;
        this.tags = b.tags;
    }
    /**
     * @return the largeObjectId
     */
    public UUID getLargeObjectId() {
        return largeObjectId;
    }
    /**
     * @return the subjectId
     */
    public String getSubjectId() {
        return subjectId;
    }
    /**
     * @return the binary
     */
    public boolean isBinary() {
        return binary;
    }
    /**
     * An arbitrary set of searchable tags.
     * @return set of tags
     */
    public Set<String> getTags() {
        return Objects.isNull(tags) ? Collections.emptySet() : tags;
    }
    /**
     * Returns tags as strings array.
     * NOTE: if no tags specified, it will return null so it has to be checked!
     * @return tags as string array or null, if no tags specified
     */
    @Nullable
    public String[] getTagsAsArray() {
        return CollectionUtils.isEmpty(tags) ? null : tags.toArray(String[]::new);
    }
    /**
     * The builder.
     * @return builder
     */
    public static DeleteLargeObjectContextBuilder builder() {
        return new DeleteLargeObjectContextBuilder();
    }
    /**
     * Builder class.
     */
    public static class DeleteLargeObjectContextBuilder extends CommonRequestContextBuilder<DeleteLargeObjectContextBuilder> {
        /**
         * ID of the BLOB/CLOB record.
         */
        private UUID largeObjectId;
        /**
         * The subject ID.
         */
        private String subjectId;
        /**
         * Binary or character data.
         */
        private boolean binary;
        /**
         * An arbitrary set of searchable tags.
         */
        private Set<String> tags;
        /**
         * Constructor.
         */
        private DeleteLargeObjectContextBuilder() {
            super();
        }
        /**
         * Sets ID of the BLOB/CLOB record..
         * @param largeObjectId the key
         * @return self
         */
        public DeleteLargeObjectContextBuilder largeObjectId(String largeObjectId) {
            if (StringUtils.isNotBlank(largeObjectId)) {
                return largeObjectId(UUID.fromString(largeObjectId));
            }
            return self();
        }
        /**
         * Sets ID of the BLOB/CLOB record..
         * @param largeObjectId the key
         * @return self
         */
        public DeleteLargeObjectContextBuilder largeObjectId(UUID largeObjectId) {
            this.largeObjectId = largeObjectId;
            return this;
        }
        /**
         * Sets LOB subject ID.
         * @param subjectId the key
         * @return self
         */
        public DeleteLargeObjectContextBuilder subjectId(String subjectId) {
            this.subjectId = subjectId;
            return self();
        }
        /**
         * Sets flag to return binary (or character) data.
         * @param binary the flag
         * @return self
         */
        public DeleteLargeObjectContextBuilder binary(boolean binary) {
            this.binary = binary;
            return this;
        }
        /**
         * Adds tags.
         * @param tags the tags
         * @return self
         */
        public DeleteLargeObjectContextBuilder tags(String... tags) {
            if (ArrayUtils.isNotEmpty(tags)) {
                return tags(Arrays.asList(tags));
            }
            return self();
        }
        /**
         * Adds tags.
         * @param tags the tags
         * @return self
         */
        public DeleteLargeObjectContextBuilder tags(Collection<String> tags) {
            if (CollectionUtils.isNotEmpty(tags)) {
                for (String t : tags) {

                    if (Objects.isNull(t)) {
                        continue;
                    }

                    if (Objects.isNull(this.tags)) {
                        this.tags = new HashSet<>();
                    }

                    this.tags.add(t);
                }
            }
            return self();
        }
        /**
         * Builds the context.
         * @return new context
         */
        @Override
        public DeleteLargeObjectContext build() {
            return new DeleteLargeObjectContext(this);
        }
    }
}
