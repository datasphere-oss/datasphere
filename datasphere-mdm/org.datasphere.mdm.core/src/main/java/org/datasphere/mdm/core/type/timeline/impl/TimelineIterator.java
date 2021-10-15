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
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TreeMap;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.type.calculables.Calculable;
import org.datasphere.mdm.core.type.calculables.CalculableHolder;
import org.datasphere.mdm.core.type.timeline.TimeInterval;
import org.datasphere.mdm.core.type.timeline.TimeIntervalFactory;
import org.datasphere.mdm.system.util.TimeBoundaryUtils;

/**
 * @author Mikhail Mikhailov
 * Transforms timeline versions to time scale and iterates through intervals.
 */
public class TimelineIterator<V extends Calculable> implements Iterator<TimeInterval<V>> {
    /**
     * Practical lower bound, used instead of null in some places.
     */
    private static final Date TIMELINE_LOWER_BOUND = new Date(TimeBoundaryUtils.TIME_NEGATIVE_INFINITY);
    /**
     * Practical upper bound, used instead of null in some places.
     */
    private static final Date TIMELINE_UPPER_BOUND = new Date(TimeBoundaryUtils.TIME_POSITIVE_INFINITY);
    /**
     * Versions, currently active.
     */
    private final RevisionSlider<V> slider;
    /**
     * From / to points as a scale.
     */
    private final NavigableMap<DateAdapter, List<CalculableHolder<V>>> points;
    /**
     * Type.
     */
    private final TimeIntervalFactory<V> factory;
    /**
     * Current pointer. Cannot be null, while iterating.
     */
    private DateAdapter pointer = null;
    /**
     * Constructor.
     */
    public TimelineIterator(TimeIntervalFactory<V> factory, Collection<CalculableHolder<V>> input) {

        super();

        this.factory = factory;
        this.slider = new RevisionSlider<>(CollectionUtils.isEmpty(input) ? 0 : input.size());
        this.points = new TreeMap<>();

        for (CalculableHolder<V> v : input) {

            Date validFrom = v.getValidFrom();
            Date validTo = v.getValidTo();

            if (Objects.isNull(validFrom)) {
                validFrom = TIMELINE_LOWER_BOUND;
            } else {
                // SQL <-> platform types violate compareTo contract
                validFrom = new Date(validFrom.getTime());
            }

            if (Objects.isNull(validTo)) {
                validTo = TIMELINE_UPPER_BOUND;
            } else {
                // SQL <-> platform types violate compareTo contract
                validTo = new Date(validTo.getTime());
            }

            DateAdapter from = new DateAdapter(validFrom, false);
            DateAdapter to = new DateAdapter(validTo, true);

            points.computeIfAbsent(from, key -> new ArrayList<>()).add(v);
            points.computeIfAbsent(to, key -> new ArrayList<>()).add(v);
        }
    }

    public RevisionSlider<V> slider() {
        return slider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {

        if (pointer == null) {
            return points.firstKey() != null;
        }

        return points.higherKey(pointer) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TimeInterval<V> next() {

        while (true) {

            // 1. Find visible bounds
            Entry<DateAdapter, List<CalculableHolder<V>>> start = nextStart();
            Entry<DateAdapter, List<CalculableHolder<V>>> end = nextEnd();

            if (Objects.isNull(start) || Objects.isNull(end)) {
                throw new NoSuchElementException("No such element. "
                        + (start == null ? "'Start'" : "'End'")
                        + " bound is null.");
            }

            // 2. Skip cycle if needed
            boolean skipCycle = false;

            // Handle direct edge (diff is 1 milli only)
            if ((end.getKey().getValue().getTime() - start.getKey().getValue().getTime()) == 1) {
                pointer = start.getKey();
                skipCycle = true;
            // Check slider for being empty (empty space).
            // Handle this. Do one more cycle.
            } else if (slider.isEmpty()) {
                skipCycle = true;
            }

            if (skipCycle) {
                continue;
            }

            // 4. Reset pointer for the next cycle
            // Subtract 1 milli for next cycle, if current 'end' is not the last one.
            // Leave it as is for last key, to cause hasNext() return false.
            pointer = points.lastKey().equals(end.getKey())
                    ? end.getKey()
                    : new DateAdapter(new Date(end.getKey().getValue().getTime() - 1), false);

            // 5. Return result
            return toInterval(start.getKey(), end.getKey());
        }
    }
    /**
     * Start can not be hidden, since it is either first point or last visible point.
     * @return period start entry
     */
    private Entry<DateAdapter, List<CalculableHolder<V>>> nextStart() {

        Entry<DateAdapter, List<CalculableHolder<V>>> start = null;
        boolean isVisible = false;
        while (!isVisible) {

            // 1. Start
            if (pointer == null) {
                start = points.firstEntry();
            } else {
                start = points.higherEntry(pointer);
            }

            // 2. End of scale / empty timeline
            if (Objects.isNull(start)) {
                return null;
            }

            // 3. Check visibility
            isVisible = slider.visible(start.getKey(), start.getValue());

            // 4. Update slider
            // Remove contributors for 'to' start.
            // Add contributors for 'from' start.
            if (start.getKey().isOpening()) {
                slider.add(start.getValue());
            } else if (start.getKey().isClosing()) {
                slider.remove(start.getValue());
            }

            // 5. Move pointer
            pointer = start.getKey();
        }

        return start;
    }
    /**
     * Process end until next visible point.
     * @return next visible entry
     */
    private Entry<DateAdapter, List<CalculableHolder<V>>> nextEnd() {

        Entry<DateAdapter, List<CalculableHolder<V>>> end = null;
        boolean isVisible = false;
        while (!isVisible) {

            // 1. Obtain next point
            end = points.higherEntry(pointer);
            if (Objects.isNull(end)) {
                return null;
            }

            // 2. Check visibility
            isVisible = slider.visible(end.getKey(), end.getValue());
            if (!isVisible) {

                // 2.1. Update slider
                // Remove contributors for 'to' start.
                // Add contributors for ''from' start.
                if (end.getKey().isOpening()) {
                    slider.add(end.getValue());
                } else if (end.getKey().isClosing()) {
                    slider.remove(end.getValue());
                }

                pointer = end.getKey();
            }
        }

        return end;
    }
    /**
     * Dumps state to time interval.
     * @param start left boundary
     * @param end right boundary
     *
     * @return interval
     */
    private TimeInterval<V> toInterval(DateAdapter start, DateAdapter end) {

        Date from = null;
        Date to = null;

        if (start.getValue() != TIMELINE_LOWER_BOUND) {
            from = start.isClosing() ? new Date(start.getValue().getTime() + 1) : start.getValue();
        }

        if (end.getValue() != TIMELINE_UPPER_BOUND) {
            to = end.isOpening() ? new Date(end.getValue().getTime() - 1) : end.getValue();
        }

        return factory.newInstance(from, to, slider.toCollection());
    }

    /**
     * @author Mikhail Mikhailov
     * Simple date adapter with 'from'/'to' origin mark.
     */
    public static class DateAdapter implements Comparable<DateAdapter> {
        /**
         * The value.
         */
        private final Date value;
        /**
         * 'From' or 'to' mark.
         */
        private final boolean to;
        /**
         * Constructor.
         * @param value the value to adapt
         * @param to the origin mark
         */
        public DateAdapter(Date value, boolean to) {
            super();
            this.value = value;
            this.to = to;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(DateAdapter o) {
            return value.compareTo(o.getValue());
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {

            if (obj == this) {
                return true;
            }

            if (obj instanceof TimelineIterator.DateAdapter) {
                return value.equals(((DateAdapter) obj).getValue());
            }

            return false;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return value.hashCode();
        }
        /**
         * @return the value
         */
        public Date getValue() {
            return value;
        }
        /**
         * @return the to
         */
        public boolean isClosing() {
            return to;
        }
        /**
         * @return the to
         */
        public boolean isOpening() {
            return !to;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return (isOpening() ? "opening: " : "closing: ") + value.toString();
        }
    }
}
