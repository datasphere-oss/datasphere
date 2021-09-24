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

import java.util.UUID;

import com.huahui.datasphere.mdm.system.context.CommonRequestContext;

/**
 * @author theseusyang
 * Fetch large objects request context.
 */
public class FetchLargeObjectContext extends CommonRequestContext {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = -4782136881603082178L;
    /**
     * ID of the BLOB/CLOB record.
     */
    private final UUID largeObjectId;
    /**
     * Binary or character data.
     */
    private final boolean binary;
    /**
     * Gets the content directly from DB input stream without intermediate temp file.
     */
    private final boolean direct;
    /**
     * Constructor.
     */
    private FetchLargeObjectContext(FetchLargeObjectContextBuilder b) {
        super(b);
        this.largeObjectId = b.largeObjectId;
        this.binary = b.binary;
        this.direct = b.direct;
    }
    /**
     * @return the largeObjectId
     */
    public UUID getLargeObjectId() {
        return largeObjectId;
    }
    /**
     * @return the binary
     */
    public boolean isBinary() {
        return binary;
    }
    /**
     * @return the direct
     */
    public boolean isDirect() {
        return direct;
    }
    /**
     * Convenience builder method.
     * @return builder
     */
    public static FetchLargeObjectContextBuilder builder() {
        return new FetchLargeObjectContextBuilder();
    }
    /**
     * @author theseusyang
     * Builder class.
     */
    public static class FetchLargeObjectContextBuilder extends CommonRequestContextBuilder<FetchLargeObjectContextBuilder> {
        /**
         * ID of the BLOB/CLOB record.
         */
        private UUID largeObjectId;
        /**
         * Binary or character data.
         */
        private boolean binary;
        /**
         * Gets the content directly from DB input stream without intermediate temp file.
         */
        private boolean direct;
        /**
         * Constructor.
         */
        private FetchLargeObjectContextBuilder() {
            super();
        }
        /**
         * Sets ID of the BLOB/CLOB record..
         * @param largeObjectId the key
         * @return self
         */
        public FetchLargeObjectContextBuilder largeObjectId(String largeObjectId) {
            return largeObjectId(UUID.fromString(largeObjectId));
        }
        /**
         * Sets ID of the BLOB/CLOB record..
         * @param largeObjectId the key
         * @return self
         */
        public FetchLargeObjectContextBuilder largeObjectId(UUID largeObjectId) {
            this.largeObjectId = largeObjectId;
            return this;
        }
        /**
         * Sets flag to return binary (or character) data.
         * @param binary the flag
         * @return self
         */
        public FetchLargeObjectContextBuilder binary(boolean binary) {
            this.binary = binary;
            return this;
        }
        /**
         * Sets flag to return the content directly from DB input stream without intermediate temp file.
         * @param direct the flag
         * @return self
         */
        public FetchLargeObjectContextBuilder direct(boolean direct) {
            this.direct = direct;
            return this;
        }
        /**
         * Builds the context.
         * @return new context
         */
        @Override
        public FetchLargeObjectContext build() {
            return new FetchLargeObjectContext(this);
        }
    }
}
