package com.huahui.datasphere.system.serialization.json;

import java.util.Objects;

import com.huahui.datasphere.system.type.pipeline.connection.SinglePipelineConnection;
import com.huahui.datasphere.system.util.PipelineUtils;

/**
 * @author Mikhail Mikhailov on May 25, 2020
 */
public class ConnectorSegmentJS extends SegmentJS {
    /**
     * Connected start id.
     */
    private String connectedId;
    /**
     * Constructor.
     */
    public ConnectorSegmentJS() {
        super();
    }
    /**
     * Constructor.
     */
    public ConnectorSegmentJS(SinglePipelineConnection connection) {
        super();
        if (Objects.nonNull(connection) && connection.isConnected()) {
            this.connectedId = PipelineUtils.toSerializablePipelineId(connection.getPipeline());
        }
    }
    /**
     * @return the start
     */
    public String getConnectedId() {
        return connectedId;
    }
    /**
     * @param start the start to set
     */
    public void setConnectedId(String connectedId) {
        this.connectedId = connectedId;
    }
}
