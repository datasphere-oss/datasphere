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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.type.calculables.Calculable;
import org.datasphere.mdm.core.type.calculables.CalculableHolder;
import org.datasphere.mdm.core.type.timeline.impl.TimelineIterator.DateAdapter;
import org.datasphere.mdm.system.util.TimeBoundaryUtils;

/**
 * @author Mikhail Mikhailov
 * Versions slider backed by a map.
 */
public class RevisionSlider<V extends Calculable> {
    /**
     * Top latest comparator.
     */
    public static final Comparator<CalculableHolder<?>> POP_TOP_COMPARATOR =
            (CalculableHolder<?> ch1, CalculableHolder<?> ch2) -> {

                int diff = ch1.getLastUpdate().compareTo(ch2.getLastUpdate());
                if (diff == 0) {
                    return ch1.getRevision() - ch2.getRevision();
                }

                return diff;
            };

    /**
     * Backup map.
     */
    private final Map<String, NavigableSet<CalculableHolder<V>>> slides;
    /**
     * Constructor.
     */
    public RevisionSlider() {
        super();
        slides = new HashMap<>();
    }
    /**
     * Constructor.
     * @param expectedMaxSize the expected max size
     */
    public RevisionSlider(int expectedMaxSize) {
        super();
        slides = new HashMap<>(expectedMaxSize);
    }
    /**
     * Checks version set for being at least partially top-visible.
     * @param point the ts point to process
     * @param versions version set
     * @return true, if at least one version gets it to top in its modbox bucket.
     */
    public boolean visible(DateAdapter point, List<CalculableHolder<V>> versions) {

        for (CalculableHolder<V> ch : versions) {

            // Edges of enrichment versions are not visible.
            if (ch.isEnrichment()) {
                continue;
            }

            NavigableSet<CalculableHolder<V>> slide = slides.get(ch.toBoxKey());

            // Check 'from' (opening) point case
            if (point.isOpening()) {
                if (CollectionUtils.isEmpty(slide) || slide.higher(ch) == null) {
                    // Bucket is either empty or doen't exist
                    // or revision will be the highest and thus promoted to the top
                    return true;
                }
            // Check 'to' (closing) point case
            } else {
                if (CollectionUtils.isNotEmpty(slide) && slide.last() == ch) {
                    // The revision being removed, reside on top
                    // of the bucket for the given key
                    return true;
                }
            }
        }

        return false;
    }
    /**
     * Adds a point to timeline.
     * @param versions the versions to add
     */
    public void add(List<CalculableHolder<V>> versions) {

        for (CalculableHolder<V> ch : versions) {
            slides.computeIfAbsent(ch.toBoxKey(), key -> new TreeSet<>(POP_TOP_COMPARATOR))
                  .add(ch);
        }
    }
    /**
     * Removes a point from timeline.
     * @param versions the versions to remove
     */
    public void remove(List<CalculableHolder<V>> versions) {

        for (CalculableHolder<V> ch : versions) {

            NavigableSet<CalculableHolder<V>> slide = slides.get(ch.toBoxKey());
            if (CollectionUtils.isEmpty(slide)) {
                // Log this?
                continue;
            }

            Iterator<CalculableHolder<V>> it = slide.iterator();
            while (it.hasNext()) {

                CalculableHolder<V> current = it.next();
                if (current == ch) {
                    it.remove();
                    break;
                }
            }
        }
    }
    /**
     * Tells, whether the slider is empty.
     * @return true if so, false otherwise
     */
    public boolean isEmpty() {

        for (Entry<String, NavigableSet<CalculableHolder<V>>> entry : slides.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }

            return false;
        }

        return true;
    }
    /**
     * Returns content as collection.
     * @return content as collection
     */
    public Collection<CalculableHolder<V>> toCollection() {

        List<CalculableHolder<V>> versions = new ArrayList<>();
        for (Entry<String, NavigableSet<CalculableHolder<V>>> entry : slides.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }

            versions.add(entry.getValue().last());
        }

        return versions.isEmpty() ? Collections.emptyList() : versions;
    }
    /**
     * Returns content as collection suitable for merge operation.
     * @return content as a merge collection
     */
    public Collection<CalculableHolder<V>> toMergeCollection() {

        List<CalculableHolder<V>> collected = new ArrayList<>();
        for (Entry<String, NavigableSet<CalculableHolder<V>>> entry : slides.entrySet()) {

            List<CalculableHolder<V>> versions = new ArrayList<>(entry.getValue().size());
            CalculableHolder<V> current = entry.getValue().last();
            while (current != null) {

                boolean overlapped = false;
                for (CalculableHolder<V> added : versions) {
                    if (overlappedBy(current, added)) {
                        overlapped = true;
                        break;
                    }
                }

                if (!overlapped) {
                    versions.add(current);
                }

                current = entry.getValue().lower(current);
            }

            collected.addAll(versions);
        }

        return collected.isEmpty() ? Collections.emptyList() : collected;
    }

    private boolean overlappedBy(CalculableHolder<V> me, CalculableHolder<V> by) {
        return TimeBoundaryUtils.upperBoundCompareTo(by.getValidFrom(), me.getValidFrom()) <= 0
            && TimeBoundaryUtils.lowerBoundCompareTo(by.getValidTo(), me.getValidTo()) >= 0;
    }
}
