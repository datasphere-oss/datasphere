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

import java.util.Date;
import java.util.UUID;

import org.datasphere.mdm.core.type.calculables.ModificationBoxKey;
import org.datasphere.mdm.core.type.data.RecordStatus;

/**
 * @author Mikhail Mikhailov
 * The origin key base.
 */
public abstract class OriginKey implements ModificationBoxKey {
    /**
     * Origin id.
     * FIXME: refactor sources to use UUID.
     */
    protected final String id;
    /**
     * Etalon id of the initial owner record.
     */
    protected final UUID initialOwner;
    /**
     * Origin source system.
     */
    protected final String sourceSystem;
    /**
     * Enrichment
     */
    protected final boolean enrichment;
    /**
     * This origin's current revision.
     */
    protected final int revision;
    /**
     * Origin status.
     */
    protected final RecordStatus status;
    /**
     * Record create date.
     */
    protected final Date createDate;
    /**
     * Record update date.
     */
    protected final Date updateDate;
    /**
     * Created by tag.
     */
    protected final String createdBy;
    /**
     * Updated by tag.
     */
    protected final String updatedBy;
    /**
     * Sonar S2055.
     * Constructor.
     */
    protected OriginKey() {
        super();
        this.id = null;
        this.initialOwner = null;
        this.sourceSystem = null;
        this.enrichment = false;
        this.revision = 0;
        this.status = null;
        this.createDate = null;
        this.createdBy = null;
        this.updateDate = null;
        this.updatedBy = null;
    }
    /**
     * Constructor.
     */
    protected OriginKey(OriginKeyBuilder<?> b) {
        super();
        this.id = b.id;
        this.initialOwner = b.initialOwner;
        this.sourceSystem = b.sourceSystem;
        this.enrichment = b.enrichment;
        this.revision = b.revision;
        this.status = b.status;
        this.createDate = b.createDate;
        this.createdBy = b.createdBy;
        this.updateDate = b.updateDate;
        this.updatedBy = b.updatedBy;
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @return the initialOwner
     */
    public UUID getInitialOwner() {
        return initialOwner;
    }
    /**
     * @return the sourceSystem
     */
    public String getSourceSystem() {
        return sourceSystem;
    }
    /**
     * @return the enrichment
     */
    public boolean isEnrichment() {
        return enrichment;
    }
    /**
     * @return the revision
     */
    public int getRevision() {
        return revision;
    }
    /**
     * @return the status
     */
    public RecordStatus getStatus() {
        return status;
    }
    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }
    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }
    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }
    /**
     * @return the updatedBy
     */
    public String getUpdatedBy() {
        return updatedBy;
    }
    /**
     * The origin key base builder.
     * @author Mikhail Mikhailov
     *
     * @param <X> subtype param
     */
    public abstract static class OriginKeyBuilder<X extends OriginKeyBuilder<X>> {
        /**
         * Origin id.
         */
        protected String id;
        /**
         * Etalon id of the initial owner record.
         */
        protected UUID initialOwner;
        /**
         * Origin source system.
         */
        protected String sourceSystem;
        /**
         * enrichment
         */
        protected boolean enrichment;
        /**
         * This origin's current revision.
         */
        protected int revision = 0;
        /**
         * Origin status.
         */
        protected RecordStatus status;
        /**
         * Record create date.
         */
        protected Date createDate;
        /**
         * Record update date.
         */
        protected Date updateDate;
        /**
         * Created by tag.
         */
        protected String createdBy;
        /**
         * Updated by tag.
         */
        protected String updatedBy;
        /**
         * Constructor.
         */
        protected OriginKeyBuilder() {
            super();
        }
        /**
         * Copy constructor.
         */
        protected OriginKeyBuilder(OriginKey other) {
            super();
            this.id = other.id;
            this.initialOwner = other.initialOwner;
            this.sourceSystem = other.sourceSystem;
            this.enrichment = other.enrichment;
            this.revision = other.revision;
            this.status = other.status;
            this.createDate = other.createDate;
            this.createdBy = other.createdBy;
            this.updateDate = other.updateDate;
            this.updatedBy = other.updatedBy;
        }
        /**
         * Sets the value of the id property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         */
        public X id(String value) {
            this.id = value;
            return self();
        }
        /**
         * Sets the value of the id property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         */
        public X initialOwner(UUID value) {
            this.initialOwner = value;
            return self();
        }
        /**
         * Sets the value of the sourceSystem property.
         *
         * @param sourceSystem
         *     allowed object is
         *     {@link String }
         */
        public X sourceSystem(String sourceSystem) {
            this.sourceSystem = sourceSystem;
            return self();
        }
        /**
         * Sets the value of the entityName property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         */
        public X enrichment(boolean value) {
            this.enrichment = value;
            return self();
        }
        /**
         * Sets the value of the revision property.
         *
         * @param value
         *     allowed object is
         *     {@link Integer }
         */
        public X revision(int value) {
            this.revision = value;
            return self();
        }
        /**
         * Sets the value of the status property.
         *
         * @param status
         *     allowed object is
         *     {@link RecordStatus }
         */
        public X status(RecordStatus status) {
            this.status = status;
            return self();
        }
        /**
         * @param createDate the createDate to set
         */
        public X createDate(Date createDate) {
            this.createDate = createDate;
            return self();
        }
        /**
         * @param updateDate the updateDate to set
         */
        public X updateDate(Date updateDate) {
            this.updateDate = updateDate;
            return self();
        }
        /**
         * @param createdBy the createdBy to set
         */
        public X createdBy(String createdBy) {
            this.createdBy = createdBy;
            return self();
        }
        /**
         * @param updatedBy the updatedBy to set
         */
        public X updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return self();
        }
        /**
         * This cast trick.
         * @return self
         */
        @SuppressWarnings("unchecked")
        protected X self() {
            return (X) this;
        }
        /**
         * Build the object.
         * @return object
         */
        public abstract OriginKey build();
    }
}
