package com.huahui.datasphere.mdm.system.serialization.json;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.huahui.datasphere.mdm.system.convert.PipelinesConverter;
import com.huahui.datasphere.mdm.system.type.pipeline.Outcome;
import com.huahui.datasphere.mdm.system.type.pipeline.OutcomesSegment;
import com.huahui.datasphere.mdm.system.type.pipeline.Pipeline;
import com.huahui.datasphere.mdm.system.type.pipeline.connection.OutcomesPipelineConnection;
import com.huahui.datasphere.mdm.system.util.PipelineUtils;

public abstract class AbstractOutcomeSegmentJS extends SegmentJS {
    /**
     * Outcome to collection of segments map.
     */
    private Map<String, List<SegmentJS>> outcomeSegments;
    /**
     * Outcome to pipeline map.
     */
    private Map<String, String> outcomeLinks;
    /**
     * Constructor.
     */
    protected AbstractOutcomeSegmentJS() {
        super();
    }
    /**
     * Constructor.
     * @param connection the connection
     */
    protected AbstractOutcomeSegmentJS(OutcomesPipelineConnection connection) {
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
    }
    /**
     * @return the outcomeSegments
     */
    public Map<String, List<SegmentJS>> getOutcomeSegments() {
        return Objects.isNull(outcomeSegments) ? Collections.emptyMap() : outcomeSegments;
    }
    /**
     * @param outcomeSegments the outcomeSegments to set
     */
    public void setOutcomeSegments(Map<String, List<SegmentJS>> outcomes) {
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
}
