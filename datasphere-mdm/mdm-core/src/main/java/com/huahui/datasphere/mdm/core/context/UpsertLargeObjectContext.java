/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/**
 *
 */
package com.huahui.datasphere.mdm.core.context;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import com.huahui.datasphere.mdm.system.context.CommonRequestContext;

import com.huahui.datasphere.mdm.core.type.lob.LargeObjectAcceptance;

/**
 * @author theseusyang
 * Saves LOB.
 */
@NotThreadSafe
public class UpsertLargeObjectContext extends CommonRequestContext {
    /**
     * Generated SVUID.
     */
    private static final long serialVersionUID = -6915756538080064206L;
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
     * Input stream.
     */
    private final transient Supplier<InputStream> input;
    /**
     * File name.
     */
    private final String filename;
    /**
     * MIME type.
     */
    private final String mimeType;
    /**
     * An arbitrary set of searchable tags.
     */
    private final Set<String> tags;
    /**
     * LOB acceptance state. 'PENDING' is the default.
     */
    private final LargeObjectAcceptance acceptance;
    /**
     * Constructor.
     */
    private UpsertLargeObjectContext(UpsertLargeObjectContextBuilder b) {
        super(b);
        this.largeObjectId = b.largeObjectId;
        this.subjectId = b.subjectId;
        this.binary = b.binary;
        this.input = Objects.nonNull(b.inputSupplier) ? b.inputSupplier : () -> b.inputStream;
        this.filename = b.filename;
        this.mimeType = b.mimeType;
        this.tags = b.tags;
        this.acceptance = b.acceptance;
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
     * @return the inputStream
     */
    public Supplier<InputStream> getInput() {
        return input;
    }
    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }
    /**
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
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
     * Retuirns accpetance state.
     * @return the acceptance state
     */
    public LargeObjectAcceptance getAcceptance() {
        return acceptance;
    }
    /**
     * Symmetrically to all other contexts.
     * @return builder instance
     */
    public static UpsertLargeObjectContextBuilder builder() {
        return new UpsertLargeObjectContextBuilder();
    }
    /**
     * @author theseusyang
     * Builder class.
     */
    public static class UpsertLargeObjectContextBuilder extends CommonRequestContextBuilder<UpsertLargeObjectContextBuilder> {
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
         * Input stream supplier.
         */
        private Supplier<InputStream> inputSupplier;
        /**
         * Input stream.
         */
        private InputStream inputStream;
        /**
         * File name.
         */
        private String filename;
        /**
         * Mime type.
         */
        private String mimeType;
        /**
         * An arbitrary set of searchable tags.
         */
        private Set<String> tags;
        /**
         * LOB acceptance state. 'PENDING' is the default.
         */
        private LargeObjectAcceptance acceptance;
        /**
         * Constructor.
         */
        private UpsertLargeObjectContextBuilder() {
            super();
        }
        /**
         * Sets LOB object ID.
         * @param largeObjectId the key
         * @return self
         */
        public UpsertLargeObjectContextBuilder largeObjectId(String largeObjectId) {
            if (Objects.nonNull(largeObjectId)) {
                this.largeObjectId = UUID.fromString(largeObjectId);
            }
            return self();
        }
        /**
         * Sets LOB object ID.
         * @param largeObjectId the key
         * @return self
         */
        public UpsertLargeObjectContextBuilder largeObjectId(UUID largeObjectId) {
            this.largeObjectId = largeObjectId;
            return self();
        }
        /**
         * Sets LOB subject ID.
         * @param subjectId the key
         * @return self
         */
        public UpsertLargeObjectContextBuilder subjectId(String subjectId) {
            this.subjectId = subjectId;
            return self();
        }
        /**
         * Sets flag to return binary (or character) data.
         * @param binary the flag
         * @return self
         */
        public UpsertLargeObjectContextBuilder binary(boolean binary) {
            this.binary = binary;
            return self();
        }
        /**
         * Sets the input stream.
         * @param inputStream the input stream
         * @return self
         */
        public UpsertLargeObjectContextBuilder input(InputStream inputStream) {
            this.inputStream = inputStream;
            return self();
        }
        /**
         * Sets an input stream supplier,
         * which may do some recovery in case of failure or run other activities,
         * related to underlying input.
         * @param inputStream the input stream
         * @return self
         */
        public UpsertLargeObjectContextBuilder input(Supplier<InputStream> inputStream) {
            this.inputSupplier = inputStream;
            return self();
        }
        /**
         * Sets the file name.
         * @param filename the file name
         * @return self
         */
        public UpsertLargeObjectContextBuilder filename(String filename) {
            this.filename = filename;
            return self();
        }
        /**
         * Sets the MIME type.
         * @param mimeType the mime type
         * @return self
         */
        public UpsertLargeObjectContextBuilder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return self();
        }
        /**
         * Adds tags.
         * @param tags the tags
         * @return self
         */
        public UpsertLargeObjectContextBuilder tags(String... tags) {
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
        public UpsertLargeObjectContextBuilder tags(Collection<String> tags) {
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
         * Sets the acceptance state.
         * @param acceptance the acceptance state
         * @return self
         */
        public UpsertLargeObjectContextBuilder acceptance(LargeObjectAcceptance acceptance) {
            this.acceptance = acceptance;
            return self();
        }
        /**
         * Builds the context.
         * @return new context
         */
        @Override
        public UpsertLargeObjectContext build() {
            return new UpsertLargeObjectContext(this);
        }
    }
}
