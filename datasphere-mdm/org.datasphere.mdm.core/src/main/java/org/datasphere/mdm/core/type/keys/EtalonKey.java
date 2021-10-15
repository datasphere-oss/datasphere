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

package org.datasphere.mdm.core.type.keys;

import org.datasphere.mdm.core.type.data.RecordStatus;

/**
 * @author Mikhail Mikhailov
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
     * @author Mikhail Mikhailov
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
