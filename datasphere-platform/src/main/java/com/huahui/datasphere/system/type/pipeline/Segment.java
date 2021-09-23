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

/**
 * @author Mikhail Mikhailov
 * Segment (integration point)
 */
public abstract class Segment implements PipelineElement {
    /**
     * Segment id.
     */
    protected final String id;
    /**
     * Segment descripton.
     */
    protected final String description;
    /**
     * Constructor.
     * @param id the ID
     * @param description the description
     */
    protected Segment(String id, String description) {
        super();
        this.id = id;
        this.description = description;
    }
    /**
     * Gets the segment ID. Must be unique accross the system.
     * @return ID
     */
    public String getId() {
        return id;
    }
    /**
     * Gets type description.
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Gets the segment type.
     * @return type
     */
    public abstract SegmentType getType();
    /**
     * Check for supporting of pipelines, initiated by this starting point.
     * @param start the starting point
     * @return true, if supports, false otherwise
     */
    public abstract boolean supports(Start<?, ?> start);
    /**
     * Returns true, if this segment is an anonymous one
     * and should not be persisted to pipeline configuration.
     * @return true, if this segment is an anonymous one
     */
    public boolean isAnonymous() {
        return false;
    }
    /**
     * Marks a segment as a participant in a batched pipeline.
     * Pipelines, started by a start segment denoted as batched, become batched pipelines.
     * @return true, for batched, false otherwise
     */
    public abstract boolean isBatched();
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSegment() {
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnection() {
        return false;
    }
}
