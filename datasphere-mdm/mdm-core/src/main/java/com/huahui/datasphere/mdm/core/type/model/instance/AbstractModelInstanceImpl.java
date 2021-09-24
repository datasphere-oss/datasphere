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
package com.huahui.datasphere.mdm.core.type.model.instance;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.huahui.datasphere.mdm.core.type.model.CustomPropertyElement;
import com.huahui.datasphere.mdm.core.type.model.ModelInstance;
import com.huahui.datasphere.mdm.core.type.model.ModelSource;
import com.huahui.datasphere.mdm.core.type.model.VersionedElement;
import com.huahui.datasphere.mdm.core.type.model.source.AbstractModelSource;
import com.huahui.datasphere.mdm.core.type.model.source.CustomProperty;

/**
 * @author theseusyang on Oct 7, 2020
 * Version info.
 */
public abstract class AbstractModelInstanceImpl<X extends ModelSource>
    extends AbstractNamedDisplayableCustomPropertiesImpl
    implements ModelInstance<X>, VersionedElement {
    /**
     * Current version.
     */
    private final int version;
    /**
     * Create date.
     */
    private final OffsetDateTime createDate;
    /**
     * Created by.
     */
    private final String createdBy;
    /**
     * The storage id.
     */
    private final String storageId;
    /**
     * Constructor.
     * @param m the object
     */
    protected AbstractModelInstanceImpl(AbstractModelSource<?> m) {
        super(m.getName(), m.getDisplayName(), m.getDescription(), m.getCustomProperties());
        this.version = m.getVersion();
        this.createDate = m.getCreateDate();
        this.createdBy = m.getCreatedBy();
        this.storageId = m.getStorageId();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getVersion() {
        return version;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public OffsetDateTime getCreateDate() {
        return createDate;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getCreatedBy() {
        return createdBy;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getStorageId() {
        return storageId;
    }
    /**
     * Restores CP source view.
     * @return CP source view
     */
    protected Collection<CustomProperty> getSourceCustomProperties() {

        Collection<CustomPropertyElement> cpe = getCustomProperties();
        if (CollectionUtils.isEmpty(cpe)) {
            return Collections.emptyList();
        }

        return getCustomProperties().stream()
                .map(pe -> new CustomProperty()
                        .withName(pe.getName())
                        .withValue(pe.getValue()))
                .collect(Collectors.toList());
    }
}
