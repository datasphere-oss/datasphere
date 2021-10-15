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

package org.datasphere.mdm.core.type.timeline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.type.calculables.Calculable;
import org.datasphere.mdm.core.type.calculables.CalculableHolder;
import org.datasphere.mdm.core.type.keys.EtalonKey;
import org.datasphere.mdm.core.type.keys.Keys;
import org.datasphere.mdm.core.type.keys.OriginKey;
import org.datasphere.mdm.core.type.timeline.impl.RevisionSlider;
import org.datasphere.mdm.core.type.timeline.impl.TimelineIterator;
import org.datasphere.mdm.system.type.support.IdentityHashSet;

/**
 * @author Mikhail Mikhailov
 * Common functionality for timeline.
 */
public abstract class AbstractTimeline<C extends Calculable> implements Timeline<C> {
    /**
     * Published state
     */
    protected Keys<?, ?> keys;
    /**
     * Intervals.
     */
    protected final List<TimeInterval<C>> intervals = new ArrayList<>(8);
    /**
     * Empty TL member.
     */
    static final Timeline<Calculable> EMPTY_TIMELINE = new AbstractTimeline<Calculable>((Keys<EtalonKey, OriginKey>) null) {

        @Override
        protected AbstractTimeline<Calculable> of(Keys<?, ?> keys) {
            return this;
        }

        @Override
        protected AbstractTimeline<Calculable> of(Keys<?, ?> keys, List<TimeInterval<Calculable>> intervals) {
            return this;
        }

        @Override
        protected AbstractTimeline<Calculable> of(Keys<?, ?> keys, Collection<CalculableHolder<Calculable>> input) {
            return this;
        }

        @Override
        protected TimeIntervalFactory<Calculable> factory() {
            return null;
        }};
    /**
     * Constructor.
     * @param keys the keys to hold
     */
    protected AbstractTimeline(Keys<?, ?> keys) {
        super();
        this.keys = keys;
    }
    /**
     * Constructor.
     */
    protected AbstractTimeline(Keys<?, ?> keys, List<TimeInterval<C>> input) {
        this(keys);
        this.intervals.addAll(input);
    }
    /**
     * Constructor.
     */
    protected AbstractTimeline(Keys<?, ?> keys, Collection<CalculableHolder<C>> input) {
        this(keys);
        this.input(input);
    }
    /**
     * @return the keys
     */
    @Override
    @SuppressWarnings("unchecked")
    public<V extends Keys<?, ?>> V getKeys() {
        return (V) keys;
    }
    /**
     * @param keys the keys to set
     */
    @Override
    public void setKeys(Keys<?, ?> keys) {
        this.keys = keys;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPublished() {
        return getKeys() != null && getKeys().isPublished();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive() {
        return intervals.stream().anyMatch(TimeInterval::isActive);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public TimeInterval<C> first() {
        return isEmpty() ? null : get(0);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public TimeInterval<C> last() {
        return isEmpty() ? null : get(size() - 1);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public TimeInterval<C> get(int i) {

        // Suppress AIOOB
        if (i >= intervals.size()) {
            return null;
        }

        return intervals.get(i);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void add(TimeInterval<C> interval) {
        intervals.add(interval);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(int i) {
        // Suppress AIOOB
        if (i >= intervals.size()) {
            return;
        }

        intervals.remove(i);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        intervals.clear();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public TimeInterval<C> selectAsOf(Date asOf) {

        if (isEmpty()) {
            return null;
        }

        Date point = asOf == null ? new Date() : asOf;
        for (TimeInterval<C> interval : intervals) {
            if (interval.isInRange(point)) {
                return interval;
            }
        }

        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<TimeInterval<C>> selectBy(Date from, Date to) {

        if (CollectionUtils.isEmpty(intervals)) {
            return Collections.emptyList();
        } else if (from == null && to == null) {
            return new ArrayList<>(intervals);
        }

        List<TimeInterval<C>> result = null;
        for (int i = 0; i < size(); i++) {

            TimeInterval<C> interval = get(i);
            boolean left = from == null || interval.getValidTo() == null || from.compareTo(interval.getValidTo()) < 0;
            boolean right = to == null || interval.getValidFrom() == null || to.compareTo(interval.getValidFrom()) > 0;

            if (left && right) {

                if (Objects.isNull(result)) {
                    result = new ArrayList<>();
                }

                result.add(interval);
            }
        }

        return Objects.isNull(result) ? Collections.emptyList() : result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TimeInterval<C>> getIntervals() {
        return Collections.unmodifiableList(intervals);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<CalculableHolder<C>> getCalculables() {
        return stream()
            .flatMap(TimeInterval::stream)
            .collect(Collectors.toCollection(IdentityHashSet::new));
    }

    @Override
    public boolean isFullCovered(Date from, Date to, boolean onlyActive) {
        boolean covered = false;
        List<TimeInterval<C>> periods = new ArrayList<>(intervals.stream()
                .filter(interval -> !onlyActive || interval.isActive())
                .collect(Collectors.toList()));

        periods.sort((o1, o2) -> {

            if (o1.getValidFrom() == null && o2.getValidFrom() == null) {
                return 0;
            } else if (o1.getValidFrom() == null) {
                return -1;
            } else if (o2.getValidFrom() == null) {
                return 1;
            }

            return o1.getValidFrom().compareTo(o2.getValidFrom());
        });

        Date checkFrom = from;
        Date checkTo = to;
        long delta = TimeUnit.MILLISECONDS.toMillis(1);

        for (TimeInterval<C> period : periods) {
            if (period.getValidFrom() != null
                    && (checkFrom == null || period.getValidFrom().getTime() > checkFrom.getTime() + delta)) {
                break;
            }
            if (period.getValidTo() == null
                    || (checkTo != null && checkTo.getTime() - delta < period.getValidTo().getTime())) {
                covered = true;
                break;
            }
            if (checkFrom == null || checkFrom.getTime() < period.getValidTo().getTime())
                checkFrom = period.getValidTo();
        }

        return covered;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return intervals.size();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<TimeInterval<C>> iterator() {
        return intervals.listIterator();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<TimeInterval<C>> stream() {
        return intervals.stream();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Timeline<C> merge(TimeInterval<C> box) {

        if (Objects.isNull(box) || box.isEmpty()) {
            return this;
        }

        AbstractTimeline<C> result = of(getKeys());
        result.add(box);
        return merge(result);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Timeline<C> merge(Timeline<C> other) {

        if (Objects.isNull(other) || other.isEmpty()) {
            return this;
        }

        RevisionSlider<C> slider = new RevisionSlider<>(this.size() + other.size());
        TimeInterval<C> it;
        for (int i = 0; i < this.size(); i++) {

            it = this.get(i);
            if (it instanceof MutableTimeInterval) {
                slider.add(((MutableTimeInterval<C>) it).toContent());
            } else {
                slider.add(it.toList());
            }
        }

        for (int i = 0; i < other.size(); i++) {

            it = other.get(i);
            if (it instanceof MutableTimeInterval) {
                slider.add(((MutableTimeInterval<C>) it).toContent());
            } else {
                slider.add(it.toList());
            }
        }

        return of(getKeys(), slider.toMergeCollection());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timeline<C> reduceBy(Date from, Date to) {

        AbstractTimeline<C> result = of(getKeys());

        List<TimeInterval<C>> reduced = selectBy(from, to);
        if (CollectionUtils.isNotEmpty(reduced)) {
            result.intervals.addAll(reduced);
        }

        return result;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Timeline<C> reduceAsOf(Date asOf) {

        AbstractTimeline<C> result = of(getKeys());

        TimeInterval<C> selected = selectAsOf(asOf);
        if (Objects.nonNull(selected)) {
            result.intervals.add(selected);
        }

        return result;
    }
    /**
     * Adds intervals to timeline using the given input.
     * @param values the input to process
     */
    protected void input(Collection<CalculableHolder<C>> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            for (TimelineIterator<C> ti = new TimelineIterator<>(factory(), values); ti.hasNext(); ) {
                this.intervals.add(ti.next());
            }
        }
    }
    /**
     * Creates a new timeline instance of a particular type.
     * @param keys the keys to use
     * @return new timeline
     */
    protected abstract AbstractTimeline<C> of(Keys<?, ?> keys);
    /**
     * Creates a new timeline instance of a particular type.
     * @param keys the keys to use
     * @param intervals the intervals to hold
     * @return new timeline
     */
    protected abstract AbstractTimeline<C> of(Keys<?, ?> keys, List<TimeInterval<C>> intervals);
    /**
     * Creates a new timeline instance of a particular type.
     * @param keys the keys to use
     * @param input the input to hold
     * @return new timeline
     */
    protected abstract AbstractTimeline<C> of(Keys<?, ?> keys, Collection<CalculableHolder<C>> input);
    /**
     * Gets the time interval factory for this calculable type.
     * @return {@linkplain TimeIntervalFactory}
     */
    protected abstract TimeIntervalFactory<C> factory();
}
