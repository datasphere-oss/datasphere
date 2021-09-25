package com.huahui.datasphere.mdm.rest.core.ro.pipeline;

import java.util.Objects;

import com.huahui.datasphere.mdm.system.type.pipeline.connection.PipelineId;

/**
 * @author theseusyang on Jan 15, 2021
 */
public class PipelineIdRO {
    /**
     * Start segment id.
     */
    private String startId;
    /**
     * Subject name/id.
     */
    private String subjectId;
    /**
     * Constructor.
     */
    public PipelineIdRO() {
        super();
    }
    /**
     * Constructor.
     */
    public PipelineIdRO(PipelineId id) {
        super();
        Objects.requireNonNull(id, "Pipeline ID cannot be null.");
        this.startId = id.getStartId();
        this.subjectId = id.getSubjectId();
    }
    /**
     * @return the startId
     */
    public String getStartId() {
        return startId;
    }
    /**
     * @param startId the startId to set
     */
    public void setStartId(String startId) {
        this.startId = startId;
    }
    /**
     * @return the subjectId
     */
    public String getSubjectId() {
        return subjectId;
    }
    /**
     * @param subjectId the subjectId to set
     */
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
