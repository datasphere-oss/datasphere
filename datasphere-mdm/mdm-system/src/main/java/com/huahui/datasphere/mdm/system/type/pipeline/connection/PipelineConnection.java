package com.huahui.datasphere.mdm.system.type.pipeline.connection;

import java.util.Objects;

import com.huahui.datasphere.mdm.system.exception.PipelineException;
import com.huahui.datasphere.mdm.system.exception.SystemExceptionIds;
import com.huahui.datasphere.mdm.system.type.pipeline.Pipeline;
import com.huahui.datasphere.mdm.system.type.pipeline.PipelineElement;
import com.huahui.datasphere.mdm.system.type.pipeline.Segment;
import com.huahui.datasphere.mdm.system.type.pipeline.connection.MultiPipelineConnection.MultiPipelineConnectionBuilder;
import com.huahui.datasphere.mdm.system.type.pipeline.connection.OutcomesPipelineConnection.OutcomesPipelineConnectionBuilder;
import com.huahui.datasphere.mdm.system.type.pipeline.connection.SinglePipelineConnection.SinglePipelineConnectionBuilder;

/**
 * @author theseusyang on May 25, 2020
 */
public abstract class PipelineConnection implements PipelineElement {
    /**
     * Type of connection.
     */
    public enum ConnectionType {
        SINGLE,
        MULTIPLE,
        OUTCOME
    }
    /**
     * Connection mark.
     */
    protected boolean connected;
    /**
     * The connected segment. Optional.
     */
    protected Segment segment;
    /**
     * Constructor.
     */
    protected PipelineConnection(PipelineConnectionBuilder<?> b) {
        super();
        this.segment = b.segment;
    }
    /**
     * @param connected the connected to set
     */
    protected void setConnected(boolean connected) {
        this.connected = connected;
    }
    /**
     * @return the connected
     */
    public boolean isConnected() {
        return connected;
    }
    /**
     * Tries to activate this connection if it is not active and can be activated (requires post-processing).
     * @param segment the connecting segment
     */
    public abstract void connect(Segment segment);
    /**
     * Gets type of this connection.
     * @return type of connection
     */
    public abstract ConnectionType getType();
    /**
     * Gets the connected segment.
     * @return the segment
     */
    public Segment getSegment() {
        return segment;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSegment() {
        return false;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnection() {
        return true;
    }
    /**
     * Narrows connection to a precise type.
     * @param <T> the type to narrow to
     */
    @SuppressWarnings("unchecked")
    public <T extends PipelineConnection> T narrow() {
        return (T) this;
    }
    /**
     * Throws if this PL is already closed.
     */
    protected void throwPipelineNotFoundIfNull(Pipeline p, String start, String subject) {
        if (Objects.isNull(p)) {
            throw new PipelineException("Pipeline not found by id [{}], subject [{}].",
                    SystemExceptionIds.EX_PIPELINE_NOT_FOUND_BY_ID_AND_SUBJECT, start, subject);
        }
    }
    /**
     * Throw on ambiguous input.
     */
    protected void throwOnAmbiguousInput() {
        throw new PipelineException("Pipeline connection has ambiguous input.",
                SystemExceptionIds.EX_PIPELINE_BUILDER_AMBIGUOUS_INPUT);
    }
    /**
     * Throws on empty input.
     */
    protected void throwOnEmptyInput() {
        throw new PipelineException("Pipeline connection has empty input.",
                SystemExceptionIds.EX_PIPELINE_BUILDER_EMPTY_INPUT);
    }
    /**
     * Single connection builder instance.
     * @return builder instance
     */
    public static SinglePipelineConnectionBuilder single() {
        return SinglePipelineConnection.builder();
    }
    /**
     * Multi connection builder instance.
     * @return builder instance
     */
    public static MultiPipelineConnectionBuilder multi() {
        return MultiPipelineConnection.builder();
    }
    /**
     * Outcomes connection builder instance.
     * @return builder instance
     */
    public static OutcomesPipelineConnectionBuilder outcome() {
        return OutcomesPipelineConnection.builder();
    }
    /**
     * @author theseusyang on Dec 29, 2020
     * Base builder.
     */
    public abstract static class PipelineConnectionBuilder<X extends PipelineConnectionBuilder<X>> {
        /**
         * Connected segment, if we know it. Optional.
         */
        protected Segment segment;
        /**
         * Constructor.
         */
        protected PipelineConnectionBuilder() {
            super();
        }
        /**
         * Sets connected segment, if known.
         * @param segment the segment
         * @return self
         */
        public X segment(Segment segment) {
            this.segment = segment;
            return self();
        }
        /**
         * Build target.
         * @return target
         */
        public abstract PipelineConnection build();
        /**
         * Self cast to target.
         * @return self
         */
        @SuppressWarnings("unchecked")
        protected X self() {
            return (X) this;
        }
    }
}
