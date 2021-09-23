package com.huahui.datasphere.system.type.pipeline.fragment;

import com.huahui.datasphere.system.type.pipeline.batch.BatchedPipelineOutput;

/**
 * Boxing 'void' return method value for PipelineExecutionResult
 *
 * @author maria.chistyakova
 * @since  08.12.2019
 */
public class VoidBatchedPipelineOutput implements BatchedPipelineOutput {

    public static final VoidBatchedPipelineOutput INSTANCE = new VoidBatchedPipelineOutput();

    private VoidBatchedPipelineOutput(){}
}
