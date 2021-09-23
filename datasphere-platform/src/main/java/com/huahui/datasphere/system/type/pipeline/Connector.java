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

import com.huahui.datasphere.system.type.pipeline.fragment.InputFragmentContainer;

/**
 * @author Mikhail Mikhailov
 * Connector segment marker interface.
 */
public abstract class Connector<I extends PipelineInput, O extends PipelineOutput> extends Segment {
    /**
     * Constructor.
     * @param id the id
     * @param description the description
     */
    protected Connector(String id, String description) {
        super(id, description);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SegmentType getType() {
        return SegmentType.CONNECTOR;
    }
    /**
     * Default implementation, that just checks the input type for being a composite context.
     * Probably should almost always be overridden in subclasses.
     */
    @Override
    public boolean supports(Start<?, ?> start) {
        return InputFragmentContainer.class.isAssignableFrom(start.getInputTypeClass());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBatched() {
        return false;
    }
    /**
     * Performs actual execution of the segment's code.
     * @param ctx the context to execute the segment on
     */
    public abstract O connect(I ctx);
    /**
     * Performs actual execution of the segment's code but using the supplied pipeline.
     * @param ctx the context to execute the segment on
     * @param p the supplied pipeline
     */
    public abstract O connect(I ctx, Pipeline p);
}
