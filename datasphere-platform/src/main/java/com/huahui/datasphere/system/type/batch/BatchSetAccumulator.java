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

package com.huahui.datasphere.system.type.batch;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.huahui.datasphere.system.context.CommonRequestContext;
import com.huahui.datasphere.system.dto.ExecutionResult;
import com.huahui.datasphere.system.type.pipeline.Pipeline;
import com.huahui.datasphere.system.type.pipeline.batch.BatchedPipelineInput;

/**
 * @author Mikhail Mikhailov
 * Simple batch set accumulator.
 * If accumulator is used in a job for processing and modifying of multiversion record sets,
 * it *must* exist during the lifetime of the step, since it holds id cache for multiversion batches.
 */
public interface BatchSetAccumulator<I extends CommonRequestContext, O extends ExecutionResult> extends BatchedPipelineInput {
    /**
     * Charge with new block.
     * @param charge the payload
     */
    void charge(Collection<I> charge);
    /**
     * Clear state.
     */
    void discharge();
    /**
     * Get iterator of the underlaying working copy.
     * @return iterator
     */
    BatchIterator<I> iterator();
    /**
     * Gets the working copy.
     * @return list
     */
    List<I> workingCopy();
    /**
     * Accumulates a processed item from the working copy.
     * @param item the item to accumulate
     */
    void accumulate(I item);
    /**
     * Gets the size of the batch set.
     * @return {@link BatchSetSize}
     */
    BatchSetSize getBatchSetSize();
    /**
     * stop processing, if exception was occurred.
     * @return flag 'abourtOnFailure'
     */
    boolean isAbortOnFailure();
    /**
     * Gets a pipeline, possibly supplied for this batch set.
     * This pipeline is intended for execution on input objects, not on the {@link BatchSetAccumulator}.
     * @return pipeline or null
     */
    @Nullable
    Pipeline pipeline();
    /**
     * Returns statistics for this run.
     * Statistics exist until {@link #discharge()} is called.
     * @param <S> the exact statistic type
     * @return statistics
     */
    @Nullable
    <S extends BatchSetStatistics<O>> S statistics();
}
