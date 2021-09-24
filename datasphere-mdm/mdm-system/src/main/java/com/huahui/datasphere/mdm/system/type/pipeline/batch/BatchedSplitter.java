package com.huahui.datasphere.mdm.system.type.pipeline.batch;

import com.huahui.datasphere.mdm.system.type.pipeline.Outcome;
import com.huahui.datasphere.mdm.system.type.pipeline.Splitter;

/**
 * @author theseusyang on May 25, 2020
 */
public abstract class BatchedSplitter<I extends BatchedPipelineInput, O extends BatchedPipelineOutput> extends Splitter<I, O> {
    /**
     * Constructor.
     * @param id
     * @param description
     * @param outputTypeClass
     * @param outcomes
     */
    protected BatchedSplitter(String id, String description, Class<O> outputTypeClass, Outcome... outcomes) {
        super(id, description, outputTypeClass, outcomes);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBatched() {
        return true;
    }
}
