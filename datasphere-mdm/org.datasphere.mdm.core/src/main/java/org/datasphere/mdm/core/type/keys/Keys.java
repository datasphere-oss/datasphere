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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.type.calculables.ModificationBox;
import org.datasphere.mdm.core.type.data.RecordStatus;

/**
 * @author Mikhail Mikhailov
 * Keys of data of some sort.
 */
public abstract class Keys<E extends EtalonKey, O extends OriginKey> {
    /**
     * The etalon key.
     */
    protected final E etalonKey;
    /**
     * The origin key.
     */
    protected final O originKey;
    /**
     * The supplementary keys.
     */
    protected final Map<String, O> supplementaryKeys;
    /**
     * Shard number.
     */
    protected final int shard;
    /**
     * Node number.
     */
    protected final int node;
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
     * Whether this record was published (i. e. has at least one version).
     */
    protected final boolean published;
    /**
     * SQUID S2055.
     * Constructor.
     */
    protected Keys() {
        super();
        this.createDate = null;
        this.createdBy = null;
        this.updateDate = null;
        this.updatedBy = null;
        this.etalonKey = null;
        this.originKey = null;
        this.node = 0;
        this.shard = 0;
        this.published = false;
        this.supplementaryKeys = Collections.emptyMap();
    }
    /**
     * Constructor.
     * @param b the builder.
     */
    protected Keys(KeysBuilder<?, E, O> b)  {
        super();
        this.createDate = b.createDate;
        this.createdBy = b.createdBy;
        this.updateDate = b.updateDate;
        this.updatedBy = b.updatedBy;
        this.etalonKey = b.etalonKey;
        this.originKey = b.originKey;
        this.node = b.node;
        this.shard = b.shard;
        this.published = b.published;
        this.supplementaryKeys = b.supplementaryKeys == null || b.supplementaryKeys.isEmpty() ? Collections.emptyMap(): b.supplementaryKeys;
    }
    /**
     * @return the etalonKey
     */
    public E getEtalonKey() {
        return etalonKey;
    }
    /**
     * @return the originKey
     */
    public O getOriginKey() {
        return originKey;
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
     * Returns {@link ModificationBox} capable keys (i. e. ext_id_source_system),
     * which are used in box calculations.
     * @return key set
     */
    public Set<String> getBoxKeys() {
        return supplementaryKeys.keySet();
    }
    /**
     * @return supplementary keys
     */
    public List<O> getSupplementaryKeys() {
        return new ArrayList<>(supplementaryKeys.values());
    }
    /**
     * Returns map view of supplementary keys, keyed by {@link ModificationBox} capable keys (i. e. ext_id_source_system),
     * which are used in box calculations.
     * @return
     */
    public Map<String, O> getSupplementaryKeysByBoxKey() {
        return supplementaryKeys;
    }
    /**
     * @return only not enriched supplementary keys
     */
    public List<O> getSupplementaryKeysWithoutEnrichments() {
        return supplementaryKeys.values().stream().filter(key -> !key.isEnrichment()).collect(Collectors.toList());
    }
    /**
     * Finds an origin key by its {@link ModificationBox} key.
     * @param boxKey the key to use
     * @return origin key or null
     */
    public O findByBoxKey(String boxKey) {
        return supplementaryKeys.get(boxKey);
    }
    /**
     * Finds an origin key by external id.
     * @param sourceSystem the source system to match
     * @return key or null, if not found
     */
    public O findBySourceSystem(String sourceSystem){
        for (O ok : supplementaryKeys.values()) {
            if (ok.getSourceSystem().equals(sourceSystem)) {
                return ok;
            }
        }
        return null;
    }
    /**
     * Finds an origin key by source system exclusive enrichment origins.
     * @param sourceSystem the source system to match
     * @return key or null, if not found
     */
    public O findBySourceSystemWithoutEnrichments(String sourceSystem){
        for (O ok : supplementaryKeys.values()) {
            if (ok.getSourceSystem().equals(sourceSystem) && !ok.isEnrichment()) {
                return ok;
            }
        }
        return null;
    }
    /**
     * Finds an origin key by origin id.
     * @param originId the origin id to match
     * @return key or null, if not found
     */
    public O findByOriginId(String originId){
        for (O ok : supplementaryKeys.values()) {
            if (ok.getId().equals(originId)) {
                return ok;
            }
        }
        return null;
    }
    /**
     * Tells, whether this key is active. A node without a valid etalon key will answer false.
     * @return true, if active, false otherwise
     */
    public boolean isActive() {
        return etalonKey != null && etalonKey.getStatus() == RecordStatus.ACTIVE;
    }

    /**
     * Tells, whether this key (and record) was published. A node without a valid origin key will answer false.
     * @return true, if published, false otherwise
     */
    public boolean isPublished() {
        return published;
    }
    /**
     * Tells whether this keys are in the "new" state (i. e. all origins have no valid revisons).
     * @return true, for "new" state keys, false otherwise
     */
    public boolean isNew() {

        if (supplementaryKeys.isEmpty()) {
            return false;
        }

        for (Entry<String, O> key : supplementaryKeys.entrySet()) {
            if (key.getValue().getRevision() != 0) {
                return false;
            }
        }

        return true;
    }
    /**
     * Gets the shard number, where this record reside together with its keys and data.
     * @return the shard number
     */
    public int getShard() {
        return shard;
    }
    /**
     * Gets the node number, where this record reside together with its keys and data.
     * @return the node number
     */
    public int getNode() {
        return node;
    }
    /**
     * Gets LSN as object.
     * @return lsn as object
     */
    public LSN getLsnAsObject() {
        return LSN.of(getShard(), etalonKey == null ? -1 : etalonKey.getLsn());
    }
    /**
     * @author Mikhail Mikhailov
     * Builder class.
     */
    public abstract static class KeysBuilder<X extends KeysBuilder<X, E, O>, E extends EtalonKey, O extends OriginKey> {
        /**
         * The etalon key.
         */
        protected E etalonKey;
        /**
         * The origin key.
         */
        protected O originKey;
        /**
         * The supplementary keys.
         */
        protected Map<String, O> supplementaryKeys = new HashMap<>();
        /**
         * Shard number.
         */
        protected int shard;
        /**
         * Node number.
         */
        protected int node;
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
         * Whether this record was published.
         */
        protected boolean published;
        /**
         * Constructor.
         */
        protected KeysBuilder() {
            super();
        }
        /**
         * Copy constructor.
         * @param other
         */
        protected KeysBuilder(Keys<E, O> b) {
            super();
            this.createDate = b.createDate;
            this.createdBy = b.createdBy;
            this.updateDate = b.updateDate;
            this.updatedBy = b.updatedBy;
            this.etalonKey = b.etalonKey;
            this.originKey = b.originKey;
            this.node = b.node;
            this.shard = b.shard;
            this.published = b.published;
            this.supplementaryKeys.putAll(b.supplementaryKeys == null || b.supplementaryKeys.isEmpty() ? Collections.emptyMap(): b.supplementaryKeys);
        }
        /**
         * @param etalonKey the etalonKey to set
         */
        public X etalonKey(E etalonKey) {
            this.etalonKey = etalonKey;
            return self();
        }
        /**
         * @param originKey the originKey to set
         */
        public X originKey(O originKey) {
            this.originKey = originKey;
            this.supplementaryKeys.put(originKey.toBoxKey(), originKey);
            return self();
        }
        /**
         * @param published the published to set
         */
        public X published(boolean published) {
            this.published = published;
            return self();
        }
        /**
         * @param supplementaryKeys
         */
        public X supplementaryKeys(Collection<O> supplementaryKeys) {
            if (CollectionUtils.isNotEmpty(supplementaryKeys)) {
                for (O key : supplementaryKeys) {
                    this.supplementaryKeys.put(key.toBoxKey(), key);
                }
            }
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
         * Sets number of a particular shard, where the key is residing.
         *
         * @param shard the shard number
         * @return builder
         */
        public X shard(int shard) {
            this.shard = shard;
            return self();
        }
        /**
         * Sets number of a particular node, where the key is residing.
         *
         * @param no the node number
         * @return builder
         */
        public X node(int no) {
            this.node = no;
            return self();
        }
        /**
         * Build.
         * @return key
         */
        public abstract Keys<E, O> build();
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
