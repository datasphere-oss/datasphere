package com.huahui.datasphere.system.type.pipeline.batch;

import com.huahui.datasphere.system.type.pipeline.PipelineOutput;

/**
 * @author Mikhail Mikhailov on Jan 15, 2020
 * Just a marker to divide inheritance trees.
 */
public interface BatchedPipelineOutput extends PipelineOutput {
    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isBatched() {
        return true;
    }
}
