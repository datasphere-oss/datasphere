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

package com.huahui.datasphere.system.type.pipeline;

import java.util.Objects;

/**
 * The segment id type.
 * @author Mikhail Mikhailov on Nov 13, 2019
 */
public final class SegmentId<T extends Segment> {
    /**
     * This segment string id.
     */
    private final String id;
    /**
     * Segment description.
     */
    private final String description;
    /**
     * The link to segment.
     */
    private final T segment;
    /**
     * Constructor.
     */
    public SegmentId(String id, String description, T segment) {
        super();

        Objects.requireNonNull(id, "Segment id must not be null.");
        Objects.requireNonNull(description, "Segment description must not be null.");
        Objects.requireNonNull(segment, "Segment instance must not be null.");

        this.id = id;
        this.description = description;
        this.segment = segment;
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @return the segment
     */
    public T getSegment() {
        return segment;
    }
}
