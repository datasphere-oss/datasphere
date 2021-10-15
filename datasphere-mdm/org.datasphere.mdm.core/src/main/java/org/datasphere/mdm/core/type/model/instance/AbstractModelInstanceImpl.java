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
package org.datasphere.mdm.core.type.model.instance;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.type.model.CustomPropertyElement;
import org.datasphere.mdm.core.type.model.ModelInstance;
import org.datasphere.mdm.core.type.model.ModelSource;
import org.datasphere.mdm.core.type.model.VersionedElement;
import org.datasphere.mdm.core.type.model.source.AbstractModelSource;
import org.datasphere.mdm.core.type.model.source.CustomProperty;

/**
 * @author Mikhail Mikhailov on Oct 7, 2020
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
