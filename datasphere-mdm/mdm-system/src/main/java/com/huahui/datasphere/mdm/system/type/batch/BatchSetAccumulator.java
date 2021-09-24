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

package com.huahui.datasphere.mdm.system.type.batch;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.huahui.datasphere.mdm.system.context.CommonRequestContext;
import com.huahui.datasphere.mdm.system.dto.ExecutionResult;
import com.huahui.datasphere.mdm.system.type.pipeline.Pipeline;
import com.huahui.datasphere.mdm.system.type.pipeline.batch.BatchedPipelineInput;

/**
 * @author theseusyang
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
