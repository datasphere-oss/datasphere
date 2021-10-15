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

package com.huahui.datasphere.mdm.core.type.timeline;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.huahui.datasphere.mdm.system.util.TimeBoundaryUtils;

import com.huahui.datasphere.mdm.core.type.calculables.Calculable;
import com.huahui.datasphere.mdm.core.type.calculables.CalculableHolder;
import com.huahui.datasphere.mdm.core.type.calculables.CalculationResult;
import com.huahui.datasphere.mdm.core.type.data.ValidityRange;

/**
 * @author theseusyang
 * The basic time interval abstraction.
 */
public interface TimeInterval<T extends Calculable> extends Iterable<CalculableHolder<T>>, ValidityRange {
    /**
     * Gets calculation result, based on the content of this interval.
     * Calculations aren't performed automatically.
     * The result must be set by some other code.
     * @return the calculation result
     */
    <V extends CalculationResult<T>> V getCalculationResult();
    /**
     * Sets current calculation result.
     * @param result the result to set
     */
    void setCalculationResult(CalculationResult<T> result);
    /**
     * Tells whether this interval is active or not.
     * @return true, if active, false otherwise
     */
    boolean isActive();
    /**
     * Sets this interval active.
     * @param active the activity mark
     */
    void setActive(boolean active);

    /**
     * Tells whether the given date is within range of this time interval.
     * @param asOf the date to check interval against. Must not be null
     * @return true, if included, false otherwise
     */
    default boolean isInRange(Date asOf) {
        boolean left = getValidFrom() == null || (asOf != null && (getValidFrom().before(asOf) || getValidFrom().getTime() == asOf.getTime()));
        boolean right = getValidTo() == null || (asOf != null && (getValidTo().after(asOf) || getValidTo().getTime() == asOf.getTime()));
        return left && right;
    }
    /**
     * Tells whether the given date is within range of this time interval.
     * @param from the left date to check interval against.
     * @param to the right date to check interval against.
     * @return true, if included, false otherwise
     */
    default boolean isExact(Date from, Date to) {
        boolean left = (getValidFrom() == null && from == null) || (getValidFrom() != null && from != null && getValidFrom().getTime() == from.getTime());
        boolean right = (getValidTo() == null && to == null) || (getValidTo() != null && to != null && getValidTo().getTime() == to.getTime());
        return left && right;
    }
    /**
     * Returns true, if this interval contains no contributors.
     * @return true, if this interval contains no contributors, false otherwise
     */
    default boolean isEmpty() {
        return size() == 0;
    }
    /**
     * Returns true, if this interval contains contributors.
     * @return true, if this interval contains contributors, false otherwise
     */
    default boolean isNotEmpty() {
        return !isEmpty();
    }
    /**
     * Gets the period id.
     * @return the period id
     */
    default long getPeriodId() {
        return Objects.isNull(getValidTo()) ? TimeBoundaryUtils.TIME_POSITIVE_INFINITY : getValidTo().getTime();
    }
    /**
     * Returns contributors size.
     * @return number of elements
     */
    int size();
    /**
     * Returns calculables array
     * @return array
     */
    CalculableHolder<T>[] toArray();
    /**
     * Returns value array
     * @return array
     */
    T[] toValueArray();
    /**
     * Returns unattended calculables list.
     * @return list
     */
    List<CalculableHolder<T>> toList();
    /**
     * Returns the underlaying value list.
     * @return list
     */
    List<T> toValueList();
    /**
     * Opens a stream of calculables.
     * @return stream
     */
    Stream<CalculableHolder<T>> stream();
    /**
     * Returns mutable view of this interval or throws,
     * if mutability is not supported.
     * @return mutable view
     */
    MutableTimeInterval<T> unlock();
}
