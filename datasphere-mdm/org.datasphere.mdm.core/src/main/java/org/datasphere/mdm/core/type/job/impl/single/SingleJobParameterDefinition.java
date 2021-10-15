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
package org.datasphere.mdm.core.type.job.impl.single;

import java.util.Objects;

import org.datasphere.mdm.core.type.job.JobParameterDefinition;
import org.datasphere.mdm.core.type.job.JobParameterKind;
import org.datasphere.mdm.core.type.job.JobParameterType;

/**
 * @author Mikhail Mikhailov on Jun 20, 2021
 */
public abstract class SingleJobParameterDefinition<X> extends JobParameterDefinition<X> {
    /**
     * The value.
     */
    protected X value;
    /**
     * Constructor.
     */
    protected SingleJobParameterDefinition(String name, JobParameterType type) {
        super(name, type, JobParameterKind.SINGLE);
    }
    /**
     * Constructor.
     */
    protected SingleJobParameterDefinition(String name, JobParameterType type, X value) {
        this(name, type);
        this.value = value;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingle() {
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public X single() {
        return value;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return Objects.isNull(value);
    }
}
