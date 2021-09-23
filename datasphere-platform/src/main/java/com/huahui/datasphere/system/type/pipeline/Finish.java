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
 * @author Mikhail Mikhailov on Oct 2, 2019
 */
public abstract class Finish<C extends PipelineInput, R extends PipelineOutput> extends Segment {
    /**
     * The exact output type class.
     */
    private final Class<R> outputTypeClass;
    /**
     * Constructor.
     * @param id
     * @param description
     */
    protected Finish(String id, String description, Class<R> outputTypeClass) {
        super(id, description);
        this.outputTypeClass = outputTypeClass;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SegmentType getType() {
        return SegmentType.FINISH;
    }
    /**
     * @return the outputTypeClass
     */
    public Class<R> getOutputTypeClass() {
        return outputTypeClass;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBatched() {
        return false;
    }
    /**
     * Preformes the last pipeline step, converting context state to result.
     * @param ctx the context
     * @return result
     */
    public abstract R finish(C ctx);
}
