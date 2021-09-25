package com.huahui.datasphere.mdm.rest.core.ro.pipeline;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import com.huahui.datasphere.mdm.system.type.pipeline.Outcome;
import com.huahui.datasphere.mdm.system.type.pipeline.OutcomesSegment;
import com.huahui.datasphere.mdm.system.type.pipeline.Pipeline;
import com.huahui.datasphere.mdm.system.type.pipeline.connection.OutcomesPipelineConnection;
import com.huahui.datasphere.mdm.system.util.PipelineUtils;

import com.huahui.datasphere.mdm.rest.core.converter.PipelinesConverter;

public abstract class AbstractOutcomeSegmentRO extends SegmentRO {
    /**
     * Outcome to collection of segments map.
     */
    private Map<String, List<SegmentRO>> outcomeSegments;
    /**
     * Outcome to pipeline map.
     */
    private Map<String, String> outcomeLinks;
    /**
     * Outcome names.
     */
    private List<String> outcomeNames;
    /**
     * Constructor.
     */
    protected AbstractOutcomeSegmentRO() {
        super();
    }
    /**
     * Constructor.
     */
    protected AbstractOutcomeSegmentRO(OutcomesSegment<?, ?> segment) {
        super();
        Outcome[] outcomes = segment.getOutcomes();
        outcomeNames = ArrayUtils.isEmpty(outcomes)
                ? Collections.emptyList()
                : Stream.of(outcomes).map(Outcome::getName).collect(Collectors.toList());
    }
    /**
     * Constructor.
     * @param connection the connection
     */
    protected AbstractOutcomeSegmentRO(OutcomesPipelineConnection connection) {
        this();

        outcomeSegments = new HashMap<>();
        outcomeLinks = new HashMap<>();

        if (Objects.isNull(connection) || !connection.isConnected()) {
            return;
        }

        OutcomesSegment<?, ?> segment = (OutcomesSegment<?, ?>) connection.getSegment();
        Outcome[] outcomes = segment.getOutcomes();

        for (int i = 0; ArrayUtils.isNotEmpty(outcomes) && i < outcomes.length; i++) {

            Outcome o = outcomes[i];
            Pipeline pl = connection.getPipeline(o);

            if (Objects.isNull(pl)) {
                continue;
            }

            if (pl.isAnonymous()) {
                outcomeSegments.put(o.getName(), PipelinesConverter.toSegments(ListUtils.union(pl.getSegments(), pl.getFallbacks()), pl));
            } else {
                outcomeLinks.put(o.getName(), PipelineUtils.toSerializablePipelineId(pl));
            }
        }

        outcomeNames = ArrayUtils.isEmpty(outcomes)
                ? Collections.emptyList()
                : Stream.of(outcomes).map(Outcome::getName).collect(Collectors.toList());
    }
    /**
     * @return the outcomeSegments
     */
    public Map<String, List<SegmentRO>> getOutcomeSegments() {
        return Objects.isNull(outcomeSegments) ? Collections.emptyMap() : outcomeSegments;
    }
    /**
     * @param outcomeSegments the outcomeSegments to set
     */
    public void setOutcomeSegments(Map<String, List<SegmentRO>> outcomes) {
        this.outcomeSegments = outcomes;
    }
    /**
     * @return the outcomeLinks
     */
    public Map<String, String> getOutcomeLinks() {
        return Objects.isNull(outcomeLinks) ? Collections.emptyMap() : outcomeLinks;
    }
    /**
     * @param outcomeLinks the outcomeLinks to set
     */
    public void setOutcomeLinks(Map<String, String> links) {
        this.outcomeLinks = links;
    }
    /**
     * @return the outcomeNames
     */
    public List<String> getOutcomeNames() {
        return outcomeNames;
    }
    /**
     * @param outcomeNames the outcomeNames to set
     */
    public void setOutcomeNames(List<String> outcomeNames) {
        this.outcomeNames = outcomeNames;
    }
}
