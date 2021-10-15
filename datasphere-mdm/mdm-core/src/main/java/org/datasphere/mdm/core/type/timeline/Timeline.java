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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import com.huahui.datasphere.mdm.core.type.calculables.Calculable;
import com.huahui.datasphere.mdm.core.type.calculables.CalculableHolder;
import com.huahui.datasphere.mdm.core.type.keys.Keys;

/**
 * @author theseusyang
 * Generalized timeline view.
 */
public interface Timeline <C extends Calculable> extends Iterable<TimeInterval<C>> {
    /**
     * Gets the keys of the object.
     * @return the keys
     */
    <V extends Keys<?, ?>> V getKeys();
    /**
     * Sets the keys of the object.
     * @param keys the keys to set
     */
    void setKeys(Keys<?, ?> keys);

    /**
     * Tells, whether the timeline was ever published.
     * @return the published
     */
    boolean isPublished();
    /**
     * Tells whether this timeline is empty.
     * @return true, if empty, false otherwise
     */
    boolean isEmpty();
    /**
     * Returns if at least one of the periods in this timeline is active.
     * Returns false otherwise.
     * @return true, if at least one period is active, false otherwise
     */
    boolean isActive();
    /**
     * Tells whether this timeline is not empty.
     * @return true, if empty, false otherwise
     */
    default boolean isNotEmpty() {
        return !isEmpty();
    }
    /**
     * Says 'yeah', if this timeline contains a single time interval.
     * @return true, if the timeline consists of only one interval
     */
    default boolean isSingleton() {
        return size() == 1;
    }
    /**
     * Returns contributors size.
     * @return number of elements
     */
    int size();
    /**
     * Gets the very first time interval of the timeline or null.
     * @return interval or null
     */
    TimeInterval<C> first();
    /**
     * Gets the very last time interval of this timeline or null.
     * @return interval or null
     */
    TimeInterval<C> last();
    /**
     * Gets a time interval object at index i.
     * @param i the index
     * @return interval
     */
    TimeInterval<C> get(int i);
    /**
     * Selects interval, which includes the given date.
     * @param asOf the date, null is treated as current timestamp.
     * @return
     */
    TimeInterval<C> selectAsOf(Date asOf);
    /**
     * Selects a timeline segment by given boundary.
     * @param from the from date
     * @param to the to date
     * @return sub segment
     */
    List<TimeInterval<C>> selectBy(Date from, Date to);
    /**
     * Gets all time intervals currently hold by this timeline.
     * @return all intervals or empty list. The list is unmodifiable.
     */
    List<TimeInterval<C>> getIntervals();
    /**
     * Gets all distinct calculables of this timeline.
     * @return list of calculables.
     */
    Collection<CalculableHolder<C>> getCalculables();
    /**
     * Check that timeline full covers given boundary, without holes
     * @param from left boundary
     * @param to right boundary
     * @param onlyActive boolean for only active or not
     * @return true, if covered, false else
     */
    boolean isFullCovered(Date from, Date to, boolean onlyActive);
    /**
     * Opens a stream of intervals.
     * @return stream
     */
    Stream<TimeInterval<C>> stream();
    /**
     * Unconditionally add an interval to the timeline.
     * Don't use without need.
     * @param interval the interval to add
     */
    void add(TimeInterval<C> interval);
    /**
     * Remove an interval at index i.
     * Don't use without need.
     * @param i the index of the period.
     */
    void remove(int i);
    /**
     * Clears timeline state.
     * Don't use without need.
     */
    void clear();
    /**
     * Produce new timeline from this timeline and overlay interval.
     * @param box the overlay interval
     * @return new timeline
     */
    Timeline<C> merge(TimeInterval<C> box);
    /**
     * Produce new timeline from this timeline and another timeline.
     * @param other the timeline to merge into this timeline
     * @return new timeline
     */
    Timeline<C> merge(Timeline<C> other);
    /**
     * Selects an interval for the given date and returns singleton timeline.
     * @param asOf the date
     * @return new timeline
     */
    Timeline<C> reduceAsOf(Date asOf);
    /**
     * Reduce this timeline by given time interval and return new reduced timeline.
     * @param from lower bound
     * @param to upper bound
     * @return new timeline
     */
    Timeline<C> reduceBy(Date from, Date to);
    /**
     * Returns earlier date of either 'from' date of the first period of this timeline or the argument date.
     * Returns argument date if this timeline is empty
     * @param d the date to check
     * @return date
     */
    default Date earliestFrom(Date d) {
        if (isEmpty()) {
            return d;
        }

        Date selected = d;
        for (int i = 0; i < size(); i++) {
            TimeInterval<C> ti = get(i);
            if (ti.isInRange(d)) {
                selected = ti.getValidFrom();
                break;
            }
        }

        return selected;
    }
    /**
     * Returns later date of either 'to' date of the last period of this timeline or the argument date.
     * Returns argument date if this timeline is empty
     * @param d the date to check
     * @return date
     */
    default Date latestTo(Date d) {
        if (isEmpty()) {
            return d;
        }

        Date selected = d;
        for (int i = (size() - 1); i >= 0; i--) {
            TimeInterval<C> ti = get(i);
            if (ti.isInRange(d)) {
                selected = ti.getValidTo();
                break;
            }
        }

        return selected;
    }

    @SuppressWarnings("unchecked")
    static<X extends Calculable> Timeline<X> emptyTimeline() {
        return (Timeline<X>) AbstractTimeline.EMPTY_TIMELINE;
    }
}
