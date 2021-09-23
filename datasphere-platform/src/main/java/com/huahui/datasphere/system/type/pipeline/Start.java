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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Mikhail Mikhailov
 * The pipeline starting segment.
 */
public abstract class Start<C extends PipelineInput, O extends PipelineOutput> extends Segment {
    /**
     * The input type class.
     */
    private final Class<C> inputTypeClass;
    /**
     * The output type class.
     */
    private final Class<O> outputTypeClass;
    /**
     * Constructor.
     * @param id the id
     * @param description the description
     * @param inputTypeClass the input type class
     * @param outputTypeClass the output type class
     */
    protected Start(String id, String description, Class<C> inputTypeClass, Class<O> outputTypeClass) {
        super(id, description);
        this.inputTypeClass = inputTypeClass;
        this.outputTypeClass = outputTypeClass;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SegmentType getType() {
        return SegmentType.START;
    }
    /**
     * @return the inputTypeClass
     */
    public Class<C> getInputTypeClass() {
        return inputTypeClass;
    }
    /**
     * @return the outputTypeClass
     */
    public Class<O> getOutputTypeClass() {
        return outputTypeClass;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Start<?, ?> start) {
        return start == this;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBatched() {
        return false;
    }
    /**
     * Performs the step.
     * @param ctx the context
     */
    public abstract void start(@Nonnull C ctx);
    /**
     * Selects execution subject for the supplied context, if possible.
     * @param ctx the context to use
     * @return execution subject or null
     */
    @Nullable
    public abstract String subject(C ctx);
}
