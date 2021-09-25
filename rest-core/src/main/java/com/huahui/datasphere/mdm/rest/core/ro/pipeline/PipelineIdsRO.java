package com.huahui.datasphere.mdm.rest.core.ro.pipeline;

import java.util.List;

/**
 * @author theseusyang on Jan 15, 2021
 * List of pipeline ids.
 */
public class PipelineIdsRO {

    private List<PipelineIdRO> pipelineIds;

    public PipelineIdsRO() {
        super();
    }
    /**
     * @return the pipelineIds
     */
    public List<PipelineIdRO> getPipelineIds() {
        return pipelineIds;
    }
    /**
     * @param pipelineIds the pipelineIds to set
     */
    public void setPipelineIds(List<PipelineIdRO> pipelineIds) {
        this.pipelineIds = pipelineIds;
    }
}
