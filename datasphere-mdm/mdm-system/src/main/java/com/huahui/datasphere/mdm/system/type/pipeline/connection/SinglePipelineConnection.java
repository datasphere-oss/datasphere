package com.huahui.datasphere.mdm.system.type.pipeline.connection;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.huahui.datasphere.mdm.system.type.pipeline.Pipeline;
import com.huahui.datasphere.mdm.system.type.pipeline.Segment;
import com.huahui.datasphere.mdm.system.util.PipelineUtils;

/**
 * @author theseusyang on May 26, 2020
 */
public class SinglePipelineConnection extends PipelineConnection {
    /**
     * Resolved pipeline.
     */
    protected Pipeline pipeline;
    /**
     * Pipeline id to resolve.
     */
    protected PipelineId id;
    /**
     * Constructor.
     */
    protected SinglePipelineConnection(SinglePipelineConnectionBuilder b) {
        super(b);

        if (Objects.isNull(b.id) && Objects.isNull(b.pipeline)) {
            throwOnEmptyInput();
        }

        if (Objects.nonNull(b.id) && Objects.nonNull(b.pipeline)) {
            throwOnAmbiguousInput();
        }

        this.id = b.id;
        this.pipeline = b.pipeline;
        this.connected = Objects.nonNull(this.pipeline) && Objects.nonNull(b.segment);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionType getType() {
        return ConnectionType.SINGLE;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(Segment segment) {

        if (isConnected()) {
            return;
        }

        // 1. Resolve, if not already resolved
        if (Objects.nonNull(this.id)) {
            this.pipeline = PipelineUtils.findPipeline(this.id.getStartId(), this.id.getSubjectId());
            throwPipelineNotFoundIfNull(this.pipeline, this.id.getStartId(), this.id.getSubjectId());
        }

        // 2. Set segment, if missing
        this.segment = segment;

        // 3. Call ready on anonymous pipelines
        if (this.pipeline.isAnonymous()) {
            this.pipeline.ready();
        }

        setConnected(true);
    }
    /**
     * @return the pipeline
     */
    public Pipeline getPipeline() {
        return pipeline;
    }
    /**
     * Builder instance.
     * @return builder instance
     */
    public static SinglePipelineConnectionBuilder builder() {
        return new SinglePipelineConnectionBuilder();
    }
    /**
     * @author theseusyang on Dec 29, 2020
     * Single connection builder.
     */
    public static class SinglePipelineConnectionBuilder extends PipelineConnectionBuilder<SinglePipelineConnectionBuilder> {
        /**
         * Resolved pipeline.
         */
        protected Pipeline pipeline;
        /**
         * Pipeline id to resolve.
         */
        protected PipelineId id;
        /**
         * Constructor.
         */
        protected SinglePipelineConnectionBuilder() {
            super();
        }
        /**
         * From serialized id string.
         * @param id the serialized id string
         * @return self
         */
        public SinglePipelineConnectionBuilder id(@Nonnull String id) {
            Objects.requireNonNull(id, "Pipeline Id must not be null.");
            this.id = PipelineUtils.fromSerializedPipelineId(id);
            return this;
        }
        /**
         * From existing start id and subject.
         * @param id the start id
         * @param subject the subject
         * @return self
         */
        public SinglePipelineConnectionBuilder id(@Nonnull String id, String subject) {
            Objects.requireNonNull(id, "Start Id must not be null.");
            this.id = new PipelineId(id, subject);
            return this;
        }
        /**
         * From already instantiated pipeline id.
         * @param id the instantiated pipeline id
         * @return self
         */
        public SinglePipelineConnectionBuilder id(@Nonnull PipelineId id) {
            Objects.requireNonNull(id, "Pipeline Id must not be null.");
            this.id = id;
            return this;
        }
        /**
         * From existing pipeline.
         * @param p the existing pipeline.
         * @return self
         */
        public SinglePipelineConnectionBuilder pipeline(@Nonnull Pipeline p) {
            Objects.requireNonNull(p, "Pipeline instance must not be null.");
            this.pipeline = p;
            return this;
        }
        /**
         * Build.
         * @return connection or throws
         */
        @Override
        public SinglePipelineConnection build() {
            return new SinglePipelineConnection(this);
        }
    }
}
