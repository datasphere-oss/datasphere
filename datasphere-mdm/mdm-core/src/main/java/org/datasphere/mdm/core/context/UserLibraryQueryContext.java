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
package com.huahui.datasphere.mdm.core.context;

import com.huahui.datasphere.mdm.system.context.StorageSpecificContext;

import com.huahui.datasphere.mdm.core.type.libraries.LibraryMimeType;

/**
 * @author theseusyang
 */
public class UserLibraryQueryContext extends AbstractUserLibraryContext implements StorageSpecificContext {
    /**
     * Fetch postional from.
     */
    private final Integer start;
    /**
     * Fetch postional count.
     */
    private final Integer limit;
    /**
     * Load current data view of the draft object.
     */
    private final boolean withData;
    /**
     * Filter by MIME type.
     */
    private final LibraryMimeType mimeType;
    /**
     * Fetch latest?
     */
    private final boolean latest;
    /**
     * Constructor.
     * @param b the builder
     */
    private UserLibraryQueryContext(final UserLibraryQueryContextBuilder b) {
        super(b);
        this.start = b.start;
        this.limit = b.limit;
        this.withData = b.withData;
        this.latest = b.latest;
        this.mimeType = b.mimeType;
    }
    /**
     * Gets postional from.
     * @return from position
     */
    public Integer getStart() {
        return start;
    }
    /**
     * Gets count to return.
     * @return count
     */
    public Integer getLimit() {
        return limit;
    }
    /**
     * Return data (payload).
     * @return the withData
     */
    public boolean withData() {
        return withData;
    }
    /**
     * @return the mimeType
     */
    public LibraryMimeType getMimeType() {
        return mimeType;
    }
    /**
     * Filter just for latest version.
     * @return the latest
     */
    public boolean isLatest() {
        return latest;
    }
    /**
     * Builder method.
     * @return builder instance
     */
    public static UserLibraryQueryContextBuilder builder() {
        return new UserLibraryQueryContextBuilder();
    }
    /**
     * @author theseusyang on Sep 16, 2020
     * Builder class.
     */
    public static class UserLibraryQueryContextBuilder extends AbstractUserLibraryContextBuilder<UserLibraryQueryContextBuilder> {
        /**
         * Filter by MIME type.
         */
        private LibraryMimeType mimeType;
        /**
         * Fetch postional from.
         */
        private Integer start;
        /**
         * Fetch postional count.
         */
        private Integer limit;
        /**
         * Load current data view of the draft object.
         */
        private boolean withData;
        /**
         * Fetch latest?
         */
        private boolean latest;
        /**
         * Constructor.
         */
        private UserLibraryQueryContextBuilder() {
            super();
        }
        /**
         * Start portion.
         * @param start the start point
         * @return self
         */
        public UserLibraryQueryContextBuilder start(Integer start) {
            this.start = start;
            return this;
        }
        /**
         * The count to return.
         * @param limit count to return
         * @return self
         */
        public UserLibraryQueryContextBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        /**
         * Fetch data.
         * @param withData fetch data if true
         * @return self
         */
        public UserLibraryQueryContextBuilder withData(boolean withData) {
            this.withData = withData;
            return this;
        }
        /**
         * Fetch latest.
         * @param latest fetch latest records if true
         * @return self
         */
        public UserLibraryQueryContextBuilder latest(boolean latest) {
            this.latest = latest;
            return this;
        }
        /**
         * The mimeType.
         * @param mimeType the mimeType to filter for
         * @return self
         */
        public UserLibraryQueryContextBuilder mimeType(LibraryMimeType mimeType) {
            this.mimeType = mimeType;
            return this;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public UserLibraryQueryContext build() {
            return new UserLibraryQueryContext(this);
        }
    }
}
