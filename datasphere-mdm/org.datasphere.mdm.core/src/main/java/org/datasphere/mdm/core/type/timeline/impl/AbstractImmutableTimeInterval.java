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

package org.datasphere.mdm.core.type.timeline.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.type.calculables.Calculable;
import org.datasphere.mdm.core.type.calculables.CalculableHolder;
import org.datasphere.mdm.core.type.timeline.AbstractTimeInterval;
import org.datasphere.mdm.core.type.timeline.MutableTimeInterval;
import org.datasphere.mdm.system.exception.PlatformFailureException;

/**
 * @author Mikhail Mikhailov
 * Immutable view of a time interval.
 */
public abstract class AbstractImmutableTimeInterval<T extends Calculable> extends AbstractTimeInterval<T> {
    /**
     * Contributors.
     */
    protected final List<CalculableHolder<T>> contributors = new ArrayList<>(8);
    /**
     * Constructor.
     */
    protected AbstractImmutableTimeInterval() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return contributors.size();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<CalculableHolder<T>> toList() {
        return isEmpty() ? Collections.emptyList() : new ArrayList<>(contributors);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> toValueList() {
        return isEmpty()
                ? Collections.emptyList()
                : contributors.stream()
                    .map(CalculableHolder::getValue)
                    .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<CalculableHolder<T>> iterator() {
        return contributors.listIterator();
    }
    /**
     * Opens contributors stream.
     * @return stream
     */
    @Override
    public Stream<CalculableHolder<T>> stream() {
        return contributors.stream();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public MutableTimeInterval<T> unlock() {
        throw new PlatformFailureException("Interval type does not support unlock.",
                CoreExceptionIds.EX_DATA_TIMELINE_INTERVAL_READ_ONLY);
    }
}
