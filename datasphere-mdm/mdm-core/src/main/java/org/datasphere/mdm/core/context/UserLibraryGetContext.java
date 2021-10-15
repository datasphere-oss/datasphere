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

import org.apache.commons.lang3.StringUtils;
import com.huahui.datasphere.mdm.system.context.StorageSpecificContext;

/**
 * @author theseusyang
 */
public class UserLibraryGetContext extends AbstractUserLibraryContext implements StorageSpecificContext {
    /**
     * Library's version
     */
    private final String version;
    /**
     * Fetch latest?
     */
    private final boolean latest;
    /**
     * Load current data view of the draft object.
     */
    private final boolean withData;
    /**
     * Constructor.
     * @param b the builder
     */
    private UserLibraryGetContext(final UserLibraryGetContextBuilder b) {
        super(b);
        this.version = b.version;
        this.latest = StringUtils.isBlank(b.version);
        this.withData = b.withData;
    }
    /**
     * Version field to filter for.
     * @return the version
     */
    public String getVersion() {
        return version;
    }
    /**
     * Filter just for latest version.
     * @return the latest
     */
    public boolean isLatest() {
        return latest;
    }
    /**
     * Return data (payload).
     * @return the withData
     */
    public boolean withData() {
        return withData;
    }
    /**
     * Builder method.
     * @return builder instance
     */
    public static UserLibraryGetContextBuilder builder() {
        return new UserLibraryGetContextBuilder();
    }
    /**
     * @author theseusyang on Sep 16, 2020
     * Builder class.
     */
    public static class UserLibraryGetContextBuilder extends AbstractUserLibraryContextBuilder<UserLibraryGetContextBuilder> {
        /**
         * Library's version
         */
        private String version;
        /**
         * Load current data view of the draft object.
         */
        private boolean withData;
        /**
         * Constructor.
         */
        private UserLibraryGetContextBuilder() {
            super();
        }
        /**
         * The version.
         * @param version the version to filter for
         * @return self
         */
        public UserLibraryGetContextBuilder version(String version) {
            this.version = version;
            return self();
        }
        /**
         * Fetch data.
         * @param withData fetch data if true
         * @return self
         */
        public UserLibraryGetContextBuilder withData(boolean withData) {
            this.withData = withData;
            return self();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public UserLibraryGetContext build() {
            return new UserLibraryGetContext(this);
        }
    }
}
