/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package com.huahui.datasphere.platform.rest.core.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.unidata.mdm.system.exception.PipelineException;
import org.unidata.mdm.system.exception.SystemExceptionIds;
import org.unidata.mdm.system.type.pipeline.Connector;
import org.unidata.mdm.system.type.pipeline.Fallback;
import org.unidata.mdm.system.type.pipeline.Finish;
import org.unidata.mdm.system.type.pipeline.Outcome;
import org.unidata.mdm.system.type.pipeline.OutcomesSegment;
import org.unidata.mdm.system.type.pipeline.Pipeline;
import org.unidata.mdm.system.type.pipeline.PipelineElement;
import org.unidata.mdm.system.type.pipeline.Point;
import org.unidata.mdm.system.type.pipeline.Segment;
import org.unidata.mdm.system.type.pipeline.SegmentType;
import org.unidata.mdm.system.type.pipeline.Selector;
import org.unidata.mdm.system.type.pipeline.Splitter;
import org.unidata.mdm.system.type.pipeline.Start;
import org.unidata.mdm.system.type.pipeline.connection.PipelineConnection;
import org.unidata.mdm.system.type.pipeline.connection.PipelineId;
import org.unidata.mdm.system.util.PipelineUtils;

import com.huahui.datasphere.platform.rest.core.ro.pipeline.ConnectorSegmentRO;
import com.huahui.datasphere.platform.rest.core.ro.pipeline.FallbackSegmentRO;
import com.huahui.datasphere.platform.rest.core.ro.pipeline.FinishSegmentRO;
import com.huahui.datasphere.platform.rest.core.ro.pipeline.PipelineRO;
import com.huahui.datasphere.platform.rest.core.ro.pipeline.PointSegmentRO;
import com.huahui.datasphere.platform.rest.core.ro.pipeline.SegmentRO;
import com.huahui.datasphere.platform.rest.core.ro.pipeline.SelectorSegmentRO;
import com.huahui.datasphere.platform.rest.core.ro.pipeline.SplitterSegmentRO;
import com.huahui.datasphere.platform.rest.core.ro.pipeline.StartSegmentRO;

/**
 * @author Mikhail Mikhailov on Nov 25, 2019
 */
public class PipelinesConverter {

    private PipelinesConverter() {
        super();
    }

    public static List<PipelineRO> toPipelines(Collection<Pipeline> pipelines) {

        if (CollectionUtils.isEmpty(pipelines)) {
            return Collections.emptyList();
        }

        List<PipelineRO> result = new ArrayList<>(pipelines.size());
        for (Pipeline p : pipelines) {

            PipelineRO pro = to(p);
            if (Objects.isNull(pro)) {
                continue;
            }

            result.add(pro);
        }

        return result;
    }

    public static PipelineRO to (Pipeline p) {

        if (Objects.isNull(p)) {
            return null;
        }

        // Order segments according to expectations from UI
        List<Segment> view = new ArrayList<>(p.getSegments().size() + p.getFallbacks().size());
        view.addAll(p.getSegments().subList(0, p.getSegments().size() - 1));
        view.addAll(p.getFallbacks());
        view.add(p.getSegments().get(p.getSegments().size() - 1));

        PipelineRO result = new PipelineRO();
        result.setStartId(p.getStartId());
        result.setSubjectId(p.getSubjectId());
        result.setDescription(p.getDescription());
        result.setSegments(toSegments(view, p));

        return result;
    }

    public static List<SegmentRO> toSegments(Collection<Segment> segments, Pipeline p) {

        if (CollectionUtils.isEmpty(segments)) {
            return Collections.emptyList();
        }

        List<SegmentRO> result = new ArrayList<>(segments.size());
        for (Segment s : segments) {

            SegmentRO sro = to(s, p);
            if (Objects.isNull(sro)) {
                continue;
            }

            result.add(sro);
        }

        return result;
    }

    public static SegmentRO to(Segment s, Pipeline p) {

        if (Objects.isNull(s) || s.isAnonymous()) {
            return null;
        }

        SegmentRO result = null;
        switch (s.getType()) {
        case START:
            result = new StartSegmentRO();
            break;
        case FALLBACK:
            result = new FallbackSegmentRO();
            break;
        case FINISH:
            result = new FinishSegmentRO();
            break;
        case POINT:
            result = new PointSegmentRO();
            break;
        case CONNECTOR:
            result = new ConnectorSegmentRO(p.getConnection(s));
            break;
        case SPLITTER:
            result = p.isEmpty()
                ? new SplitterSegmentRO((Splitter<?, ?>) s)
                : new SplitterSegmentRO(p.getConnection(s));
            break;
        case SELECTOR:
            result = p.isEmpty()
                ? new SelectorSegmentRO((Selector<?, ?>) s)
                : new SelectorSegmentRO(p.getConnection(s));
            break;
        default:
            break;
        }

        Objects.requireNonNull(result, "Trying to marshal segment of unknown type.");

        result.setId(s.getId());
        result.setDescription(s.getDescription());
        result.setSegmentType(s.getType().name());

        return result;
    }

    public static Pipeline from(PipelineRO ro) {

        if (Objects.isNull(ro)) {
            return null;
        }

        // Gather segments
        List<PipelineElement> gathered = fromSegments(ro.getSegments());

        throwOnWrongPipelineBounds(gathered);

        Pipeline p = Pipeline.start((Start<?, ?>) gathered.get(0).toSegment(), ro.getSubjectId(), ro.getDescription());
        for (int i = 1; i < gathered.size(); i++) {

            PipelineElement element = gathered.get(i);
            if (element.isConnection()) {
                p.with(element.toConnection());
            } else {
                Segment s = element.toSegment();
                if (s.getType() == SegmentType.POINT) {
                    p.with((Point<?>) s);
                } else if (s.getType() == SegmentType.FALLBACK) {
                    p.with((Fallback<?>) s);
                } else if (s.getType() == SegmentType.CONNECTOR) {
                    p.with((Connector<?, ?>) s);
                } else if (s.getType() == SegmentType.FINISH) {
                    p.end((Finish<?, ?>) s);
                }
            }
        }

        return p;
    }

    public static List<PipelineElement> fromSegments(List<SegmentRO> ros) {

        if (CollectionUtils.isEmpty(ros)) {
            return Collections.emptyList();
        }

        List<PipelineElement> result = new ArrayList<>(ros.size());
        for (SegmentRO ro : ros) {
            result.add(from(ro));
        }

        return result;
    }

    public static PipelineElement from(SegmentRO ro) {

        if (Objects.isNull(ro)) {
            return null;
        }

        Segment hit = PipelineUtils.findSegment(ro.getId());
        if (Objects.isNull(hit) || !hit.getType().name().equals(ro.getSegmentType())) {
            throw new PipelineException("Segment not found by id or is of wrong type [{}].",
                    SystemExceptionIds.EX_PIPELINE_SEGMENT_NOT_FOUND_BY_ID,
                    ro.getId());
        }

        if (hit.getType() == SegmentType.CONNECTOR) {
            ConnectorSegmentRO csro = (ConnectorSegmentRO) ro;
            return Objects.isNull(csro.getStartId()) ? hit : PipelineConnection.single()
                    .segment(hit)
                    .id(new PipelineId(csro.getStartId(), csro.getSubjectId()))
                    .build();
        } else if (hit.getType() == SegmentType.SPLITTER) {
            SplitterSegmentRO ssro = (SplitterSegmentRO) ro;
            return PipelineConnection.outcome()
                    .pipelines(PipelinesConverter.from((OutcomesSegment<?, ?>) hit, ssro.getOutcomeSegments()))
                    .links(ssro.getOutcomeLinks())
                    .segment(hit)
                    .build();
        } else if (hit.getType() == SegmentType.SELECTOR) {
            SelectorSegmentRO ssro = (SelectorSegmentRO) ro;
            return PipelineConnection.outcome()
                    .pipelines(PipelinesConverter.from((OutcomesSegment<?, ?>) hit, ssro.getOutcomeSegments()))
                    .links(ssro.getOutcomeLinks())
                    .segment(hit)
                    .build();
        }

        return hit;
    }

    private static Map<Outcome, Pipeline> from(OutcomesSegment<?, ?> os, Map<String, List<SegmentRO>> input) {

        if (MapUtils.isEmpty(input)) {
            return Collections.emptyMap();
        }

        Map<Outcome, Pipeline> result = new HashMap<>();
        input.forEach((outcomeName, segments) -> {

            Outcome found = Stream.of(os.getOutcomes())
                    .filter(Objects::nonNull)
                    .filter(o -> StringUtils.equals(outcomeName, o.getName()))
                    .findFirst()
                    .orElseThrow(() -> new PipelineException("Outcome not found by id [{}] in [{}].",
                            SystemExceptionIds.EX_PIPELINE_OUTCOME_NOT_FOUND_BY_ID,
                            outcomeName, os.getId()));

            result.put(found, PipelineUtils.toAnonymous(found.getInputTypeClass(), found.getOutputTypeClass(), fromSegments(segments)));
        });

        return result;
    }

    private static void throwOnWrongPipelineBounds(List<PipelineElement> gathered) {
        if (gathered.size() < 2) {
            throw new PipelineException("Invalid number of segments. A pipeline must contain at least 2 points of type 'start' and 'finish'.",
                    SystemExceptionIds.EX_PIPELINE_INVALID_NUMBER_OF_SEGMENTS);
        } else if (!(gathered.get(0).isSegment() && gathered.get(0).toSegment().getType() == SegmentType.START)
                || !(gathered.get(gathered.size() - 1).isSegment() && gathered.get(gathered.size() - 1).toSegment().getType() != SegmentType.FINISH)) {
            throw new PipelineException("Invalid pipeline layout. A pipeline must start with a point of type 'start' and end with a point of type 'finish'.",
                    SystemExceptionIds.EX_PIPELINE_HAS_NO_START_OR_FINISH_OR_BOTH);
        }
    }
}
