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
public class AbstractUserLibraryContext implements StorageSpecificContext {
    /**
     * The storage id.
     */
    private final String storageId;
    /**
     * File name.
     */
    private final String filename;
    /**
     * Constructor.
     * @param b the builder
     */
    protected AbstractUserLibraryContext(final AbstractUserLibraryContextBuilder<?> b) {
        super();
        this.storageId = b.storageId;
        this.filename = b.filename;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getStorageId() {
        return storageId;
    }
    /**
     * Filename field to filter for.
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }
    /**
     * @author theseusyang on Sep 16, 2020
     * Builder class.
     */
    public abstract static class AbstractUserLibraryContextBuilder<X extends AbstractUserLibraryContextBuilder<X>> {
        /**
         * The storage id.
         */
        private String storageId;
        /**
         * File name.
         */
        private String filename;
        /**
         * Constructor.
         */
        protected AbstractUserLibraryContextBuilder() {
            super();
        }
        /**
         * The storageId to use.
         * @param storageId the storage id to use
         * @return self
         */
        public X storageId(String storageId) {
            this.storageId = storageId;
            return self();
        }
        /**
         * The filename.
         * @param filename the filename
         * @return self
         */
        public X filename(String filename) {
            this.filename = filename;
            return self();
        }
        /**
         * Self to X cast.
         * @return self
         */
        @SuppressWarnings("unchecked")
        protected X self() {
            return (X) this;
        }
        /**
         * {@inheritDoc}
         */
        public abstract AbstractUserLibraryContext build();
    }
}
