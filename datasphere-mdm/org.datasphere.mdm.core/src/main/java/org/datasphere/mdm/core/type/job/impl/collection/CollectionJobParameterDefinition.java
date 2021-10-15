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
package org.datasphere.mdm.core.type.job.impl.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.type.job.JobParameterDefinition;
import org.datasphere.mdm.core.type.job.JobParameterKind;
import org.datasphere.mdm.core.type.job.JobParameterType;

/**
 * @author Mikhail Mikhailov on Jun 21, 2021
 */
public abstract class CollectionJobParameterDefinition<X> extends JobParameterDefinition<X> {
    /**
     * The values.
     */
    protected Collection<X> values;
    /**
     * Constructor.
     * @param name
     * @param type
     */
    protected CollectionJobParameterDefinition(String name, JobParameterType type) {
        super(name, type, JobParameterKind.COLLECTION);
    }
    /**
     * Constructor.
     * @param name
     * @param type
     */
    protected CollectionJobParameterDefinition(String name, JobParameterType type, Collection<X> values) {
        this(name, type);
        this.values = values;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCollection() {
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<X> collection() {
        return Objects.isNull(values) ? Collections.emptyList() : values;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(values);
    }
}
