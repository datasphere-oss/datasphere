package com.huahui.datasphere.system.type.pipeline.defaults;

import org.unidata.mdm.system.module.SystemModule;

import com.huahui.datasphere.system.type.pipeline.Finish;
import com.huahui.datasphere.system.type.pipeline.PipelineInput;
import com.huahui.datasphere.system.type.pipeline.PipelineOutput;
import com.huahui.datasphere.system.type.pipeline.Start;

/**
 * @author Mikhail Mikhailov on Dec 28, 2020
 */
public final class NullReturnFinish<X extends PipelineOutput> extends Finish<PipelineInput, X> {
    /**
     * This segment ID.
     */
    public static final String SEGMENT_ID = SystemModule.MODULE_ID + "[NULL_RETURN_FINISH]";
    /**
     * Localized message code.
     */
    public static final String SEGMENT_DESCRIPTION = SystemModule.MODULE_ID + ".null.return.finish.description";
    /**
     * Constructor.
     */
    public NullReturnFinish(Class<X> outputType) {
        super(SEGMENT_ID, SEGMENT_DESCRIPTION, outputType);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public X finish(PipelineInput ctx) {
        // Intentionally returns null, thus, not stopping a sub-pipeline,
        // if it is executed as a part of sub-pipeline.
        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Start<?, ?> start) {
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAnonymous() {
        return true;
    }
}
