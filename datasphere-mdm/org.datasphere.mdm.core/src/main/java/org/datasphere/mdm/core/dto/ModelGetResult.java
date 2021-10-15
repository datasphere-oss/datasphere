package org.datasphere.mdm.core.dto;

import org.datasphere.mdm.system.dto.ExecutionResult;
import org.datasphere.mdm.system.type.pipeline.PipelineOutput;

/**
 * @author Mikhail Mikhailov on Oct 21, 2020
 */
public interface ModelGetResult extends PipelineOutput, ExecutionResult {
    /**
     * Gets the model id, the change is for.
     * @return model id
     */
    String getInstanceId();
    /**
     * Returns the model type id (the id supplied with model type descriptor for this model type).
     * @return model type id
     */
    String getTypeId();
}
