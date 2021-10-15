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

package com.huahui.datasphere.mdm.core.type.calculables.impl.attribute;

import java.util.Date;

import com.huahui.datasphere.mdm.core.type.calculables.CalculableHolder;
import com.huahui.datasphere.mdm.core.type.calculables.impl.AbstractCalculableHolder;
import com.huahui.datasphere.mdm.core.type.data.Attribute;
import com.huahui.datasphere.mdm.core.type.data.RecordStatus;
import com.huahui.datasphere.mdm.core.type.keys.OriginKey;

/**
 * @author theseusyang
 * Single attribute holder.
 */
public class RecordAttributeHolder extends AbstractCalculableHolder<Attribute> {
    /**
     * The value.
     */
    private final Attribute value;
    /**
     * Attribute name.
     */
    private final String typeName;
    /**
     * Source system.
     */
    private final String sourceSystem;
    /**
     * External id of the record.
     */
    private final String externalId;
    /**
     * LUD.
     */
    private final Date lastUpdate;
    /**
     * The revision.
     */
    private final int revision;
    /**
     * Constructor.
     * @param value the value to hold
     * @param typeName the type name (attribute path)
     * @param source the source record
     */
    public RecordAttributeHolder(Attribute value, String typeName, CalculableHolder<?> source) {
        this(value, typeName, source.getSourceSystem(), source.getExternalId(), source.getLastUpdate(), source.getRevision());
    }
    /**
     * Constructor.
     * @param value attribute value
     * @param typeName record's type name
     * @param sourceSystem record's source system
     * @param externalId record's external id
     * @param lastUpdate the last update
     */
    public RecordAttributeHolder(Attribute value, String typeName, String sourceSystem, String externalId, Date lastUpdate, int revision) {
        super();
        this.value = value;
        this.typeName = typeName;
        this.sourceSystem = sourceSystem;
        this.externalId = externalId;
        this.lastUpdate = lastUpdate;
        this.revision = revision;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attribute getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTypeName() {
        return typeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSourceSystem() {
        return sourceSystem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExternalId() {
        return externalId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RecordStatus getStatus() {
        return RecordStatus.ACTIVE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastUpdate() {
        return lastUpdate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRevision() {
        return revision;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getValidFrom() {
        // Does not apply
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getValidTo() {
        // Does not apply
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnrichment() {
        // Does not apply
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OriginKey getOriginKey() {
        // Does not apply.
        return null;
    }
}
