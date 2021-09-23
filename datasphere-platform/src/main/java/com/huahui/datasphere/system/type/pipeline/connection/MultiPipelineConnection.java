package com.huahui.datasphere.system.type.pipeline.connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.huahui.datasphere.system.type.pipeline.Pipeline;
import com.huahui.datasphere.system.type.pipeline.Segment;
import com.huahui.datasphere.system.util.PipelineUtils;

/**
 * @author Mikhail Mikhailov on May 26, 2020
 */
public class MultiPipelineConnection extends PipelineConnection {
    /**
     * Resolved pipelines.
     */
    protected List<Pipeline> pipelines;
    /**
     * Ids to resolve.
     */
    protected List<PipelineId> ids;
    /**
     * Constructor.
     * @param pipelineId serialized pipeline id
     */
    protected MultiPipelineConnection(MultiPipelineConnectionBuilder b) {
        super(b);

        if (CollectionUtils.isEmpty(b.ids) && CollectionUtils.isEmpty(b.pipelines)) {
            throwOnEmptyInput();
        }

        if (CollectionUtils.isNotEmpty(b.pipelines) && CollectionUtils.isNotEmpty(b.ids)) {

            Set<PipelineId> prebuilt = b.pipelines.stream()
                    .map(Pipeline::getPipelineId)
                    .collect(Collectors.toCollection(HashSet::new));

            boolean disjoint = b.ids.stream().noneMatch(prebuilt::contains);
            if (!disjoint) {
                throwOnAmbiguousInput();
            }
        }

        this.ids = b.ids;
        this.pipelines = b.pipelines;
        this.connected = CollectionUtils.isEmpty(this.ids) && Objects.nonNull(this.segment);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionType getType() {
        return ConnectionType.MULTIPLE;
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
        if (CollectionUtils.isNotEmpty(this.ids)) {

            List<Pipeline> collected = new ArrayList<>(ids.size());
            for (PipelineId id : ids) {

                Pipeline p = PipelineUtils.findPipeline(id.getStartId(), id.getSubjectId());
                throwPipelineNotFoundIfNull(p, id.getStartId(), id.getSubjectId());
                collected.add(p);
            }

            if (CollectionUtils.isEmpty(pipelines)) {
                pipelines = collected;
            } else {
                pipelines.addAll(collected);
            }
        }

        // 2. Set segment, if it is not already set
        this.segment = segment;

        // 3. Call ready on anonymous pipelines
        pipelines.stream()
            .filter(Pipeline::isAnonymous)
            .forEach(Pipeline::ready);

        setConnected(true);
    }
    /**
     * Returns all pipelines hold.
     * @return the pipelines
     */
    public List<Pipeline> getPipelines() {
        return Objects.isNull(pipelines) ? Collections.emptyList() : pipelines;
    }
    /**
     * Returns pipeline ids, supplied for resolution.
     * @return the pipeline ids
     */
    public List<PipelineId> getIds() {
        return Objects.isNull(ids) ? Collections.emptyList() : ids;
    }
    /**
     * Builder method.
     * @return builder
     */
    public static MultiPipelineConnectionBuilder builder() {
        return new MultiPipelineConnectionBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Dec 29, 2020
     * The builder.
     */
    public static class MultiPipelineConnectionBuilder extends PipelineConnectionBuilder<MultiPipelineConnectionBuilder> {
        /**
         * Resolved pipelines.
         */
        protected List<Pipeline> pipelines;
        /**
         * Ids to resolve.
         */
        protected List<PipelineId> ids;
        /**
         * Constructor.
         */
        protected MultiPipelineConnectionBuilder() {
            super();
        }
        /**
         * From serialized ids.
         * @param ids the ids
         * @return self
         */
        public MultiPipelineConnectionBuilder ids(Collection<String> ids) {

            if (CollectionUtils.isNotEmpty(ids)) {

                List<PipelineId> converted = ids.stream()
                    .map(PipelineUtils::fromSerializedPipelineId)
                    .collect(Collectors.toList());

                if (Objects.isNull(this.ids)) {
                    this.ids = converted;
                } else {
                    this.ids.addAll(converted);
                }
            }

            return self();
        }
        /**
         * From serialized ids.
         * @param ids the ids
         * @return self
         */
        public MultiPipelineConnectionBuilder ids(String... ids) {

            if (ArrayUtils.isNotEmpty(ids)) {

                List<PipelineId> converted = Arrays.stream(ids)
                    .map(PipelineUtils::fromSerializedPipelineId)
                    .collect(Collectors.toList());

                if (Objects.isNull(this.ids)) {
                    this.ids = converted;
                } else {
                    this.ids.addAll(converted);
                }
            }

            return self();
        }
        /**
         * From complete pipelines.
         * @param pipelines the ids
         * @return self
         */
        public MultiPipelineConnectionBuilder pipelines(Collection<Pipeline> pipelines) {

            if (CollectionUtils.isNotEmpty(pipelines)) {

                if (Objects.isNull(this.pipelines)) {
                    this.pipelines = new ArrayList<>(pipelines);
                } else {
                    this.pipelines.addAll(pipelines);
                }
            }

            return self();
        }
        /**
         * From complete pipelines.
         * @param pipelines the pipelines
         * @return self
         */
        public MultiPipelineConnectionBuilder pipelines(Pipeline... pipelines) {

            if (ArrayUtils.isNotEmpty(pipelines)) {
                if (Objects.isNull(this.pipelines)) {
                    this.pipelines = new ArrayList<>(Arrays.asList(pipelines));
                } else {
                    this.pipelines.addAll(Arrays.asList(pipelines));
                }
            }

            return self();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public PipelineConnection build() {
            return new MultiPipelineConnection(this);
        }
    }
}
