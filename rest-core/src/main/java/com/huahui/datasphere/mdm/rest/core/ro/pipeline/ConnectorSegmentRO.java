package com.huahui.datasphere.mdm.rest.core.ro.pipeline;

import java.util.Objects;

import com.huahui.datasphere.mdm.system.type.pipeline.connection.PipelineId;
import com.huahui.datasphere.mdm.system.type.pipeline.connection.SinglePipelineConnection;

/**
 * @author theseusyang on May 25, 2020
 */
public class ConnectorSegmentRO extends SegmentRO {
    /**
     * Connected start id.
     */
    private String startId;
    /**
     * Connected subject id.
     */
    private String subjectId;
    /**
     * Constructor.
     */
    public ConnectorSegmentRO() {
        super();
    }
    /**
     * Constructor.
     */
    public ConnectorSegmentRO(String startId, String subjectId) {
        this();
        this.startId = startId;
        this.subjectId = subjectId;
    }
    /**
     * Constructor.
     */
    public ConnectorSegmentRO(SinglePipelineConnection connection) {
        this();
        if (Objects.nonNull(connection) && connection.isConnected()) {
            PipelineId id = connection.getPipeline().getPipelineId();
            this.startId = id.getStartId();
            this.subjectId = id.getSubjectId();
        }
    }
    /**
     * @return the start
     */
    public String getStartId() {
        return startId;
    }
    /**
     * @param start the start to set
     */
    public void setStartId(String connectedId) {
        this.startId = connectedId;
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
