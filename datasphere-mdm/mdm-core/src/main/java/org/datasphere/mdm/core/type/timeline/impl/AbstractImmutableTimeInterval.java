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

package com.huahui.datasphere.mdm.core.type.timeline.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.huahui.datasphere.mdm.system.exception.PlatformFailureException;

import com.huahui.datasphere.mdm.core.exception.CoreExceptionIds;
import com.huahui.datasphere.mdm.core.type.calculables.Calculable;
import com.huahui.datasphere.mdm.core.type.calculables.CalculableHolder;
import com.huahui.datasphere.mdm.core.type.timeline.AbstractTimeInterval;
import com.huahui.datasphere.mdm.core.type.timeline.MutableTimeInterval;

/**
 * @author theseusyang
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
