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
import com.huahui.datasphere.mdm.system.util.TimeBoundaryUtils;

import com.huahui.datasphere.mdm.core.type.calculables.Calculable;
import com.huahui.datasphere.mdm.core.type.calculables.CalculableHolder;
import com.huahui.datasphere.mdm.core.type.timeline.impl.TimelineIterator.DateAdapter;

/**
 * @author theseusyang
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
