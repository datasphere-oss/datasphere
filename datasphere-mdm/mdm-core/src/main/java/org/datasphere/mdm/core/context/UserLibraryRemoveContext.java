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

/**
 * @author theseusyang
 */
public class UserLibraryRemoveContext extends AbstractUserLibraryContext implements StorageSpecificContext {
    /**
     * Library's version
     */
    private final String version;
    /**
     * Constructor.
     * @param b the builder
     */
    private UserLibraryRemoveContext(final UserLibraryRemoveContextBuilder b) {
        super(b);
        this.version = b.version;
    }
    /**
     * Version field to filter for.
     * @return the version
     */
    public String getVersion() {
        return version;
    }
    /**
     * Builder method.
     * @return builder instance
     */
    public static UserLibraryRemoveContextBuilder builder() {
        return new UserLibraryRemoveContextBuilder();
    }
    /**
     * @author theseusyang on Sep 16, 2020
     * Builder class.
     */
    public static class UserLibraryRemoveContextBuilder extends AbstractUserLibraryContextBuilder<UserLibraryRemoveContextBuilder> {
        /**
         * Library's version
         */
        private String version;
        /**
         * Constructor.
         */
        private UserLibraryRemoveContextBuilder() {
            super();
        }
        /**
         * The version.
         * @param version the version to filter for
         * @return self
         */
        public UserLibraryRemoveContextBuilder version(String version) {
            this.version = version;
            return self();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public UserLibraryRemoveContext build() {
            return new UserLibraryRemoveContext(this);
        }
    }
}
