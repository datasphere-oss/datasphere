package com.huahui.datasphere.mdm.system.type.pipeline.connection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import com.huahui.datasphere.mdm.system.exception.PipelineException;
import com.huahui.datasphere.mdm.system.exception.SystemExceptionIds;
import com.huahui.datasphere.mdm.system.type.pipeline.Outcome;
import com.huahui.datasphere.mdm.system.type.pipeline.OutcomesSegment;
import com.huahui.datasphere.mdm.system.type.pipeline.Pipeline;
import com.huahui.datasphere.mdm.system.type.pipeline.PipelineElement;
import com.huahui.datasphere.mdm.system.type.pipeline.Segment;
import com.huahui.datasphere.mdm.system.util.PipelineUtils;

/**
 * @author theseusyang on May 26, 2020
 */
public class OutcomesPipelineConnection extends PipelineConnection {
    /**
     * Collected outcomes.
     */
    protected Map<Outcome, Pipeline> pipelines;
    /**
     * Links to resolve.
     */
    protected Map<String, PipelineId> ids;
    /**
     * Constructor.
     */
    protected OutcomesPipelineConnection(OutcomesPipelineConnectionBuilder b) {
        super(b);

        if (MapUtils.isEmpty(b.ids) && MapUtils.isEmpty(b.pipelines)) {
            throwOnEmptyInput();
        }

        if (MapUtils.isNotEmpty(b.pipelines) && MapUtils.isNotEmpty(b.ids)) {

            boolean disjoint = b.pipelines.keySet().stream().noneMatch(k -> b.ids.containsKey(k.getName()));
            if (!disjoint) {
                throwOnAmbiguousInput();
            }
        }

        // We don't set this to connected, since anonymous pipelines may reference other pipelines via connectors or similar,
        // and thus, they have to intercept connect call.
        this.pipelines = Objects.nonNull(b.pipelines) ? b.pipelines : new HashMap<>();
        this.ids = b.ids;
        this.connected = MapUtils.isEmpty(this.ids) && Objects.nonNull(this.segment);
    }
    /**
     * Gets a pipeline for the given outcome.
     * @return pipeline for the given outcome or null
     */
    @Nullable
    public Pipeline getPipeline(Outcome outcome) {
        return Objects.isNull(pipelines) ? null : pipelines.get(outcome);
    }
    /**
     * Gets all connected pipelines.
     * @return all connected pipelines
     */
    public Collection<Pipeline> getPipelines() {
        return Objects.isNull(pipelines) ? Collections.emptyList() : pipelines.values();
    }
    /**
     * Gets pipeline ids, supplied for resolution.
     * @return the ids
     */
    public Map<String, PipelineId> getIds() {
        return Objects.isNull(ids) ? Collections.emptyMap() : ids;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionType getType() {
        return ConnectionType.OUTCOME;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(Segment segment) {

        if (isConnected()) {
            return;
        }

        throwIfNotOutcomesSegment(segment);

        OutcomesSegment<?, ?> s = (OutcomesSegment<?, ?>) segment;
        Outcome[] out = s.getOutcomes();

        // 1. Resolve referenced pipelines by ids, if those were supplied.
        if (MapUtils.isNotEmpty(ids)) {

            Map<Outcome, Pipeline> collected = new HashMap<>(ids.size());

            ids.forEach((o, p) -> {

                Outcome found = Arrays.stream(out)
                        .filter(e -> e.getName().equals(o))
                        .findFirst()
                        .orElseThrow(() -> new PipelineException("Outcome [{}] not found by name.",
                                SystemExceptionIds.EX_PIPELINE_OUTCOME_NOT_FOUND_BY_NAME, o));

                Pipeline hit = PipelineUtils.findPipeline(p.getStartId(), p.getSubjectId());

                throwPipelineNotFoundIfNull(hit, p.getStartId(), p.getSubjectId());

                collected.put(found, hit);
            });

            pipelines.putAll(collected);
        }

        throwIfOutcomesAndPipelinesNotFullyCovered(s, pipelines);

        // 2. Set segment, if it is not already set
        this.segment = s;

        // 3. Call ready on anonymous pipelines
        pipelines.values().stream()
            .filter(Pipeline::isAnonymous)
            .forEach(Pipeline::ready);

        // 4. Verify I/O types correctness
        for (Outcome o : out) {

            Pipeline p = getPipeline(o);
            if (!p.getInputTypeClass().isAssignableFrom(o.getInputTypeClass())) {
                throw new PipelineException("Pipeline's input type [{}] is incompatible with [{}]'s outcome input type.",
                        SystemExceptionIds.EX_PIPELINE_OUTCOME_INPUT_TYPE_MISMATCH, p.getInputTypeClass().getCanonicalName(), o.getName());
            }

            if (!o.getOutputTypeClass().isAssignableFrom(p.getOutputTypeClass())) {
                throw new PipelineException("Pipeline's output type [{}] is incompatible with [{}]'s outcome output type.",
                        SystemExceptionIds.EX_PIPELINE_OUTCOME_OUTPUT_TYPE_MISMATCH, p.getOutputTypeClass().getCanonicalName(), o.getName());
            }
        }

        setConnected(true);
    }
    /**
     * Throws if the splitter and outcomes are unbrelated (i. e. have different return types, mapped to unknown outcome labels etc.)
     * @param s the outcomes segment instance
     * @param o the outcomes map
     */
    private void throwIfOutcomesAndPipelinesNotFullyCovered(OutcomesSegment<?, ?> s, Map<Outcome, Pipeline> o) {

        List<String> uncovered = Arrays.stream(s.getOutcomes())
                .filter(outcome -> !o.containsKey(outcome))
                .map(Outcome::getName)
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(uncovered)) {
            throw new PipelineException("Segment has some outcomes, which are not fully covered by the outcomes map [{}].",
                    SystemExceptionIds.EX_PIPELINE_OUTCOMES_NOT_FULLY_COVERED, uncovered);
        }
    }
    /**
     * Throws if the splitter and outcomes are unbrelated (i. e. have different return types, mapped to unknown outcome labels etc.)
     * @param s the splitter instance
     * @param o the outcomes map
     */
    private void throwIfNotOutcomesSegment(Segment s) {
        if (!(s instanceof OutcomesSegment)) {
            throw new PipelineException("Segment has some outcomes, which are not fully covered by the outcomes map [{}].",
                    SystemExceptionIds.EX_PIPELINE_NOT_AN_OUTCOMES_SEGMENT, Objects.nonNull(s) ? s.getId() : null);
        }
    }
    /**
     * Builder instance.
     * @return builder instance
     */
    public static OutcomesPipelineConnectionBuilder builder() {
        return new OutcomesPipelineConnectionBuilder();
    }
    /**
     * @author theseusyang on Dec 30, 2020
     * Outcomes builder.
     */
    public static class OutcomesPipelineConnectionBuilder extends PipelineConnectionBuilder<OutcomesPipelineConnectionBuilder> {
        /**
         * Collected outcomes.
         */
        protected Map<Outcome, Pipeline> pipelines;
        /**
         * Links to resolve.
         */
        protected Map<String, PipelineId> ids;
        /**
         * Constructor.
         */
        protected OutcomesPipelineConnectionBuilder() {
            super();
        }
        /**
         * From serialized pipeline id.
         * @param outcome the outcome to associate
         * @param id the id
         * @return self
         */
        public OutcomesPipelineConnectionBuilder link(@Nonnull String outcome, @Nonnull String id) {
            return id(outcome, PipelineUtils.fromSerializedPipelineId(id));
        }
        /**
         * From serialized pipeline id.
         * @param outcome the outcome to associate
         * @param id the id
         * @return self
         */
        public OutcomesPipelineConnectionBuilder links(Map<String, String> ids) {
            if (MapUtils.isNotEmpty(ids)) {
                for (Entry<String, String> entry : ids.entrySet()) {
                    link(entry.getKey(), entry.getValue());
                }
            }
            return self();
        }
        /**
         * From pipeline id.
         * @param outcome the outcome to associate
         * @param id the id
         * @return self
         */
        public OutcomesPipelineConnectionBuilder id(@Nonnull String outcome, @Nonnull PipelineId id) {

            Objects.requireNonNull(outcome, "Outcome name must not be null.");
            Objects.requireNonNull(id, "Pipeline id must not be null.");

            if (MapUtils.isNotEmpty(ids)) {
                ids = new HashMap<>();
            }

            ids.put(outcome, id);
            return self();
        }
        /**
         * From pipeline id.
         * @param outcome the outcome to associate
         * @param id the id
         * @return self
         */
        public OutcomesPipelineConnectionBuilder ids(Map<String, PipelineId> ids) {
            if (MapUtils.isNotEmpty(ids)) {
                for (Entry<String, PipelineId> entry : ids.entrySet()) {
                    id(entry.getKey(), entry.getValue());
                }
            }
            return self();
        }
        /**
         * From pipeline and outcome.
         * @param outcome the outcome to associate
         * @param pipeline the pipeline
         * @return self
         */
        public OutcomesPipelineConnectionBuilder pipeline(@Nonnull Outcome outcome, @Nonnull Pipeline pipeline) {

            Objects.requireNonNull(outcome, "Outcome instance must not be null.");
            Objects.requireNonNull(pipeline, "Pipeline must not be null.");

            if (Objects.isNull(pipelines)) {
                pipelines = new HashMap<>();
            }

            pipelines.put(outcome, pipeline);
            return self();
        }
        /**
         * From pipelines and outcomes.
         * @param pipelines the pipelines map
         * @return self
         */
        public OutcomesPipelineConnectionBuilder pipelines(Map<Outcome, Pipeline> pipelines) {
            if (MapUtils.isNotEmpty(pipelines)) {
                for (Entry<Outcome, Pipeline> entry : pipelines.entrySet()) {
                    pipeline(entry.getKey(), entry.getValue());
                }
            }
            return self();
        }
        /**
         * Anonymous pipeline from outcome and collection of segments (order must be preserved).
         * @param outcome the outcome to associate
         * @param pipeline the pipeline
         * @return self
         */
        public OutcomesPipelineConnectionBuilder pipeline(@Nonnull Outcome outcome, PipelineElement... pipeline) {
            pipeline(outcome, PipelineUtils.toAnonymous(pipeline));
            return self();
        }
        /**
         * Anonymous pipeline from outcome and collection of segments (order must be preserved).
         * @param outcome the outcome to associate
         * @param pipeline the pipeline
         * @return self
         */
        public OutcomesPipelineConnectionBuilder pipeline(@Nonnull Outcome outcome, List<PipelineElement> pipeline) {
            pipeline(outcome, PipelineUtils.toAnonymous(pipeline));
            return self();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public OutcomesPipelineConnection build() {
            return new OutcomesPipelineConnection(this);
        }
    }
}
