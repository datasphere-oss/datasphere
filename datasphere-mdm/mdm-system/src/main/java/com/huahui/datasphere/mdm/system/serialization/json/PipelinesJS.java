package com.huahui.datasphere.mdm.system.serialization.json;

import java.util.List;

/**
 * @author theseusyang
 */
public class PipelinesJS {
    /**
     * The pipelines.
     */
    private List<PipelineJS> pipelines;
    /**
     * @return the pipelines
     */
    public List<PipelineJS> getPipelines() {
        return pipelines;
    }
    /**
     * @param pipelines the pipelines to set
     */
    public void setPipelines(List<PipelineJS> pipelines) {
        this.pipelines = pipelines;
    }
}
