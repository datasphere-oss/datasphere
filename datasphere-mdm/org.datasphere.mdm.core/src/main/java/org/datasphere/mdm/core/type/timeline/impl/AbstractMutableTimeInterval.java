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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.type.calculables.Calculable;
import org.datasphere.mdm.core.type.calculables.CalculableHolder;
import org.datasphere.mdm.core.type.data.DataRecord;
import org.datasphere.mdm.core.type.timeline.AbstractTimeInterval;
import org.datasphere.mdm.core.type.timeline.MutableTimeInterval;

/**
 * @author Mikhail Mikhailov
 * Mutable view of a time interval (modification box).
 * Internally, the data for a source system + ext id is organized as follows:
 * -----------------------------------
 * |          modX (at tail)         |
 * -----------------------------------
 * |          mod2                   |
 * -----------------------------------
 * |          mod1                   |
 * -----------------------------------
 * |          base (if any at head)  |
 * -----------------------------------
 */
public abstract class AbstractMutableTimeInterval<T extends Calculable> extends AbstractTimeInterval<T> implements MutableTimeInterval<T> {
    /**
     * Modifications.
     */
    protected final Map<String, BoxKeyAdapter<T>> box = new HashMap<>();
    /**
     * The cause origin if any.
     */
    protected DataRecord calculationState;
    /**
     * Constructor.
     */
    protected AbstractMutableTimeInterval() {
        super();
    }
    /**
     * @return the calculationCause
     */
    @Override
    public DataRecord getCalculationState() {
        return calculationState;
    }
    /**
     * @param calculationState the calculationCause to set
     */
    @Override
    public void setCalculationState(DataRecord calculationState) {
        this.calculationState = calculationState;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> toBoxKeys() {
        return box.keySet();
    }
    /**
     * Gets the calculation base for this box (revisions already persisted to storage and thus having revisions).
     * The map is not modifyable.
     */
    @Override
    public List<CalculableHolder<T>> toCalculationBase() {

        List<CalculableHolder<T>> result = new ArrayList<>(box.size());
        Iterator<Entry<String, BoxKeyAdapter<T>>> it = box.entrySet().iterator();
        while (it.hasNext()) {

            Entry<String, BoxKeyAdapter<T>> entry = it.next();
            CalculableHolder<T> ch = entry.getValue().peekFirst();
            if (Objects.nonNull(ch) && ch.getRevision() > 0) {
                result.add(ch);
            }
        }

        return result;
    }
    /**
     * Gets all the calculation modifications of this box.
     * The elements will be returned in order from first (head) to last (tail).
     * The map is not modifyable.
     */
    @Override
    public Map<String, List<CalculableHolder<T>>> toModifications() {

        Map<String, List<CalculableHolder<T>>> result = new HashMap<>();
        Iterator<Entry<String, BoxKeyAdapter<T>>> it = box.entrySet().iterator();
        while (it.hasNext()) {

            Entry<String, BoxKeyAdapter<T>> entry = it.next();
            for (CalculableHolder<T> ch : entry.getValue()) {
                if (ch.getRevision() == 0) {
                    result.computeIfAbsent(ch.toBoxKey(), key -> new ArrayList<>())
                        .add(ch);
                }
            }
        }

        return result;
    }
    /**
     * Returns collection of top most claculables for all source systems.
     * @return collection
     */
    @Override
    public List<CalculableHolder<T>> toCalculables() {

        List<CalculableHolder<T>> result = new ArrayList<>(box.size());
        Iterator<Entry<String, BoxKeyAdapter<T>>> it = box.entrySet().iterator();
        while (it.hasNext()) {

            Entry<String, BoxKeyAdapter<T>> entry = it.next();
            CalculableHolder<T> ch = entry.getValue().peekLast();
            if (Objects.nonNull(ch)) {
                result.add(ch);
            }
        }

        return result;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<CalculableHolder<T>> toContent() {
        return box.values().stream()
                .map(BoxKeyAdapter::content)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
    /**
     * Tells whether this box has some modifications / new revisions (is dirty).
     * @return true, if so, false otherwise
     */
    @Override
    public boolean isDirty() {

        Iterator<Entry<String, BoxKeyAdapter<T>>> it = box.entrySet().iterator();
        while (it.hasNext()) {

            Entry<String, BoxKeyAdapter<T>> entry = it.next();
            CalculableHolder<T> ch = entry.getValue().peekLast();
            if (Objects.nonNull(ch) && ch.getRevision() == 0) {
                return true;
            }
        }

        return false;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void push(Collection<CalculableHolder<T>> records) {
        if (CollectionUtils.isNotEmpty(records)) {
            records.forEach(this::push);
        }
    }
    /**
     * Pushes a calculable to the source system stack.
     * @param record calculable
     */
    @Override
    public void push(CalculableHolder<T> record) {
        box.computeIfAbsent(record.toBoxKey(), key -> new BoxKeyAdapter<>())
           .addLast(record);
    }
    /**
     * Peeks the last added calculable by the source system.
     * @param boxKey the source system name
     * @return calculable or null
     */
    @Override
    public CalculableHolder<T> peek(String boxKey) {
        BoxKeyAdapter<T> stack = box.get(boxKey);
        return stack != null ? stack.peekLast() : null;
    }
    /**
     * Pops the last added calculable by the source system.
     * @param boxKey the source system name
     * @return calculable or null
     */
    @Override
    public CalculableHolder<T> pop(String boxKey) {
        BoxKeyAdapter<T> stack = box.get(boxKey);
        return stack != null ? stack.pollLast() : null;
    }
    /**
     * Gets the number of versions by source system and external id.
     * @param boxKey the source system name
     * @return number of versions
     */
    @Override
    public int count(String boxKey) {
        BoxKeyAdapter<T> stack = box.get(boxKey);
        return stack != null ? stack.size() : 0;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int modifications(String boxKey) {
        BoxKeyAdapter<T> stack = box.get(boxKey);
        return stack != null ? stack.modifications() : 0;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int total() {
        return box.values().stream().mapToInt(BoxKeyAdapter::size).sum();
    }
    /**
     * Resets versions to calculation base by source system and external id.
     * @param boxKey the source system name
     * @return number of versions
     */
    @Override
    public List<CalculableHolder<T>> reset(String boxKey) {

        BoxKeyAdapter<T> stack = box.get(boxKey);
        List<CalculableHolder<T>> result = new ArrayList<>(stack != null ? stack.size() : 0);
        if (stack != null) {

            for (Iterator<CalculableHolder<T>> i = stack.iterator(); i.hasNext(); ) {

                CalculableHolder<T> ch = i.next();
                if (ch.getRevision() > 0) {
                    continue;
                }

                i.remove();
                result.add(ch);
            }
        }

        return result;
    }
    /**
     * Resets latest versions by given count, source system and external id.
     * @param boxKey the source system name
     * @return number of versions
     */
    @Override
    public List<CalculableHolder<T>> resetBy(String boxKey, int count) {

        BoxKeyAdapter<T> stack = box.get(boxKey);
        List<CalculableHolder<T>> result = new ArrayList<>(stack != null ? stack.size() : 0);
        if (stack != null && count <= stack.size()) {

            int i = 0;
            int offset = stack.size() - count;
            for (Iterator<CalculableHolder<T>> ci = stack.iterator(); ci.hasNext(); ) {

                CalculableHolder<T> ch = ci.next();
                if (i++ < offset) {
                    continue;
                }

                ci.remove();
                result.add(ch);
            }
        }

        return result;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return total();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<CalculableHolder<T>> toList() {
        return toCalculables();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> toValueList() {
        return toCalculables().stream().map(CalculableHolder::getValue).collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<CalculableHolder<T>> stream() {
        return toCalculables().stream();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<CalculableHolder<T>> iterator() {
        return toCalculables().iterator();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public MutableTimeInterval<T> unlock() {
        return this;
    }
    /**
     * Box key adapter, holding mod. deque, modifications count and other stuff.
     * @author Mikhail Mikhailov
     *
     */
    class BoxKeyAdapter <V extends Calculable> implements Iterable<CalculableHolder<V>> {
        /**
         * The revision stack.
         */
        private Deque<CalculableHolder<V>> stack = new ArrayDeque<>(4);
        /**
         * Mod. count. Only 0 revisions are counted.
         */
        private int modificationsCount;
        /**
         * @see {@link Deque#peekFirst()}.
         */
        public CalculableHolder<V> peekFirst() {
            return stack.peekFirst();
        }
        /**
         * @see {@link Deque#peekLast()}.
         */
        public CalculableHolder<V> peekLast() {
            return stack.peekLast();
        }
        /**
         * @see {@link Deque#pollFirst()}.
         */
        public CalculableHolder<V> pollFirst() {
            return stack.pollFirst();
        }
        /**
         * @see {@link Deque#pollLast()}.
         */
        public CalculableHolder<V> pollLast() {
            return stack.pollLast();
        }
        /**
         * @see {@link Deque#addLast(Object)}.
         */
        public void addLast(CalculableHolder<V> ch) {
            stack.addLast(ch);
            if (ch.getRevision() == 0) {
                modificationsCount++;
            }
        }
        /**
         * @see {@link Deque#size()}.
         */
        public int size() {
            return stack.size();
        }
        /**
         * @see {@link Iterable}.
         */
        @Override
        public Iterator<CalculableHolder<V>> iterator() {
            return new BoxKeyAdapterIterator<>(this);
        }
        /**
         * @return the modificationsCount
         */
        public int modifications() {
            return modificationsCount;
        }
        /**
         * @return the content
         */
        public Collection<CalculableHolder<V>> content() {
            return stack;
        }
        /**
         * Simple iterator wrapper.
         * @author Mikhail Mikhailov
         * @param <I> CH type
         */
        private class BoxKeyAdapterIterator<I extends Calculable> implements Iterator<CalculableHolder<I>> {
            /**
             * Element at current position.
             */
            private CalculableHolder<I> current;
            /**
             * Real stack iterator.
             */
            private Iterator<CalculableHolder<I>> stackIterator;
            /**
             * Constructor.
             * @param box the box.
             */
            BoxKeyAdapterIterator(BoxKeyAdapter<I> box) {
                super();
                stackIterator = box.stack.iterator();
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean hasNext() {
                return stackIterator.hasNext();
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public CalculableHolder<I> next() {
                current = stackIterator.next();
                return current;
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void remove() {
                stackIterator.remove();
                if (Objects.nonNull(current) && current.getRevision() == 0) {
                    modificationsCount--;
                }
            }
        }
    }
}
