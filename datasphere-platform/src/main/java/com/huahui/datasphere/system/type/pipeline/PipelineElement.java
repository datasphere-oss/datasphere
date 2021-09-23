package com.huahui.datasphere.system.type.pipeline;

import com.huahui.datasphere.system.type.pipeline.connection.PipelineConnection;

/**
 * @author Mikhail Mikhailov on Dec 30, 2020
 * An abstract pipeline element.
 */
public interface PipelineElement {
    /**
     * Returns true, if this pipeline element is a segment.
     * @return true, if this pipeline element is a segment.
     */
    boolean isSegment();
    /**
     * Casts itself to segment.
     * @return segment or null
     */
    default Segment toSegment() {
        return isSegment() ? (Segment) this : null;
    }
    /**
     * Returns true, if this pipeline element is a connection.
     * @return true, if this pipeline element is a connection
     */
    boolean isConnection();
    /**
     * Casts itself to connection.
     * @return connection or null
     */
    default PipelineConnection toConnection() {
        return isConnection() ? (PipelineConnection) this : null;
    }
}
