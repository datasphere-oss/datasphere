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

package com.huahui.datasphere.mdm.core.type.keys;

import com.huahui.datasphere.mdm.core.type.data.RecordStatus;

/**
 * @author theseusyang
 * Base class for various etalon keys.
 */
public abstract class EtalonKey {
    /**
     * The id.
     * FIXME: refactor keys to use UUID.
     */
    protected final String id;
    /**
     * Global sequence number.
     */
    protected final Long lsn;
    /**
     * Etalon status.
     */
    protected final RecordStatus status;

    /**
     * Sonar rule S2055.
     * Constructor.
     */
    protected EtalonKey() {
        super();
        this.id = null;
        this.lsn = null;
        this.status = null;
    }
    /**
     * Constructor.
     */
    protected EtalonKey(EtalonKeyBuilder<?> b) {
        super();
        this.id = b.id;
        this.lsn = b.lsn;
        this.status = b.status;
    }
    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getId() {
        return id;
    }
    /**
     * Gets the value of the lsn property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     */
    public Long getLsn() {
        return lsn;
    }
    /**
     * Gets the value of the etalon status.
     *
     * @return
     *     possible object is
     *     {@link RecordStatus }
     */
    public RecordStatus getStatus() {
        return status;
    }


    /**
     * Activity mark.
     * @return true, if inactive, false otherwise
     */
    public boolean isActive() {
        return status == RecordStatus.ACTIVE;
    }
    /**
     * @author theseusyang
     * Builder class.
     */
    public abstract static class EtalonKeyBuilder<X extends EtalonKeyBuilder<X>> {
        /**
         * The id.
         */
        protected String id;
        /**
         * Global sequence number.
         */
        protected Long lsn;
        /**
         * Etalon status.
         */
        protected RecordStatus status;
        /**
         * Constructor.
         */
        protected EtalonKeyBuilder() {
            super();
        }
        /**
         * Copy constructor.
         * @param other
         */
        protected EtalonKeyBuilder(EtalonKey other) {
            super();
            this.id = other.id;
            this.lsn = other.lsn;
            this.status = other.status;
        }
        /**
         * Sets the value of the id property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         * @return builder
         */
        public X id(String value) {
            this.id = value;
            return self();
        }
        /**
         * Sets the value of the lsn property.
         *
         * @param value
         *     allowed object is
         *     {@link Long }
         *
         * @return builder
         */
        public X lsn(Long value) {
            this.lsn = value;
            return self();
        }
        /**
         * Sets the value of the status property.
         *
         * @param status
         *     allowed object is
         *     {@link RecordStatus }
         *
         * @return builder
         */
        public X status(RecordStatus status) {
            this.status = status;
            return self();
        }

        /**
         * Build.
         * @return key
         */
        public abstract EtalonKey build();
        /**
         * This suppresser.
         * @return self
         */
        @SuppressWarnings("unchecked")
        protected final X self() {
            return (X) this;
        }
    }
}
