package com.huahui.datasphere.mdm.system.type.pipeline.defaults;

import com.huahui.datasphere.mdm.system.module.SystemModule;
import com.huahui.datasphere.mdm.system.type.pipeline.PipelineInput;
import com.huahui.datasphere.mdm.system.type.pipeline.PipelineOutput;
import com.huahui.datasphere.mdm.system.type.pipeline.Start;

/**
 * @author theseusyang on Dec 28, 2020
 */
public final class PassThroughStart<X extends PipelineInput, Y extends PipelineOutput> extends Start<X, Y> {
    /**
     * This segment ID.
     */
    public static final String SEGMENT_ID = SystemModule.MODULE_ID + "[PASS_THROUGH_START]";
    /**
     * Localized message code.
     */
    public static final String SEGMENT_DESCRIPTION = SystemModule.MODULE_ID + ".pass.through.start.description";
    /**
     * Constructor.
     */
    public PassThroughStart(Class<X> inputType, Class<Y> outputType) {
        super(SEGMENT_ID, SEGMENT_DESCRIPTION, inputType, outputType);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void start(PipelineInput ctx) {
        // Does nothing, since this is the only purpose of this point
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String subject(PipelineInput ctx) {
        return null;
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
    public boolean isAnonymous() {
        return true;
    }
}
