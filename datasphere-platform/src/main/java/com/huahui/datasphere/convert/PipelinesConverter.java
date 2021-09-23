/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 *
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.huahui.datasphere.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.huahui.datasphere.system.exception.PipelineException;
import com.huahui.datasphere.system.exception.SystemExceptionIds;
import com.huahui.datasphere.system.serialization.json.ConnectorSegmentJS;
import com.huahui.datasphere.system.serialization.json.FallbackSegmentJS;
import com.huahui.datasphere.system.serialization.json.FinishSegmentJS;
import com.huahui.datasphere.system.serialization.json.PipelineJS;
import com.huahui.datasphere.system.serialization.json.PointSegmentJS;
import com.huahui.datasphere.system.serialization.json.SegmentJS;
import com.huahui.datasphere.system.serialization.json.SelectorSegmentJS;
import com.huahui.datasphere.system.serialization.json.SplitterSegmentJS;
import com.huahui.datasphere.system.serialization.json.StartSegmentJS;
import com.huahui.datasphere.system.type.pipeline.Connector;
import com.huahui.datasphere.system.type.pipeline.Fallback;
import com.huahui.datasphere.system.type.pipeline.Finish;
import com.huahui.datasphere.system.type.pipeline.Outcome;
import com.huahui.datasphere.system.type.pipeline.OutcomesSegment;
import com.huahui.datasphere.system.type.pipeline.Pipeline;
import com.huahui.datasphere.system.type.pipeline.PipelineElement;
import com.huahui.datasphere.system.type.pipeline.Point;
import com.huahui.datasphere.system.type.pipeline.Segment;
import com.huahui.datasphere.system.type.pipeline.SegmentType;
import com.huahui.datasphere.system.type.pipeline.Start;
import com.huahui.datasphere.system.type.pipeline.connection.PipelineConnection;
import com.huahui.datasphere.system.util.PipelineUtils;

/**
 * @author Mikhail Mikhailov on Nov 25, 2019
 */
public class PipelinesConverter {

    private PipelinesConverter() {
        super();
    }

    public static List<PipelineJS> toPipelines(Collection<Pipeline> pipelines) {

        if (CollectionUtils.isEmpty(pipelines)) {
            return Collections.emptyList();
        }

        List<PipelineJS> result = new ArrayList<>(pipelines.size());
        for (Pipeline p : pipelines) {

            PipelineJS pro = to(p);
            if (Objects.isNull(pro)) {
                continue;
            }

            result.add(pro);
        }

        return result;
    }

    public static PipelineJS to (Pipeline p) {

        if (Objects.isNull(p)) {
            return null;
        }

        PipelineJS result = new PipelineJS();
        result.setStartId(p.getStartId());
        result.setSubjectId(p.getSubjectId());
        result.setDescription(p.getDescription());
        result.setSegments(toSegments(Stream.concat(p.getSegments().stream(), p.getFallbacks().stream()).collect(Collectors.toList()), p));

        return result;
    }

    public static List<SegmentJS> toSegments(Collection<Segment> segments, Pipeline p) {

        if (CollectionUtils.isEmpty(segments)) {
            return Collections.emptyList();
        }

        List<SegmentJS> result = new ArrayList<>(segments.size());
        for (Segment s : segments) {

            SegmentJS sro = to(s, p);
            if (Objects.isNull(sro)) {
                continue;
            }

            result.add(sro);
        }

        return result;
    }

    public static SegmentJS to(Segment s, Pipeline p) {

        if (Objects.isNull(s) || s.isAnonymous()) {
            return null;
        }

        SegmentJS result = null;
        if (s.getType() == SegmentType.SPLITTER) {
            result = new SplitterSegmentJS(p.getConnection(s));
        } else if (s.getType() == SegmentType.SELECTOR) {
            result = new SelectorSegmentJS(p.getConnection(s));
        } else if (s.getType() == SegmentType.CONNECTOR) {
            result = new ConnectorSegmentJS(p.getConnection(s));
        } else if (s.getType() == SegmentType.FALLBACK) {
            result = new FallbackSegmentJS();
        } else if (s.getType() == SegmentType.FINISH) {
            result = new FinishSegmentJS();
        } else if (s.getType() == SegmentType.POINT) {
            result = new PointSegmentJS();
        } else if (s.getType() == SegmentType.START) {
            result = new StartSegmentJS();
        }

        Objects.requireNonNull(result, "Trying to marshal a segment of unknown type.");

        result.setId(s.getId());
        result.setSegmentType(s.getType().name());

        return result;
    }

    public static Pipeline from(PipelineJS js) {

        if (Objects.isNull(js)) {
            return null;
        }

        // Gather segments
        List<PipelineElement> gathered = fromSegments(js.getSegments());

        Pipeline p = Pipeline.start((Start<?, ?>) gathered.get(0), js.getSubjectId(), js.getDescription());
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

    public static List<PipelineElement> fromSegments(List<SegmentJS> ros) {

        if (CollectionUtils.isEmpty(ros)) {
            return Collections.emptyList();
        }

        List<PipelineElement> result = new ArrayList<>(ros.size());
        for (SegmentJS ro : ros) {
            result.add(from(ro));
        }

        return result;
    }

    public static PipelineElement from(SegmentJS js) {

        if (Objects.isNull(js)) {
            return null;
        }

        Segment hit = PipelineUtils.findSegment(js.getId());
        if (Objects.isNull(hit)) {
            throw new PipelineException("Segment not found by id [{}].",
                    SystemExceptionIds.EX_PIPELINE_SEGMENT_NOT_FOUND_BY_ID,
                    js.getId());
        } else if (!hit.getType().name().equals(js.getSegmentType())) {
            throw new PipelineException("Segment found by ID, but is of different type [{}].",
                    SystemExceptionIds.EX_PIPELINE_SEGMENT_OF_WRONG_TYPE, js.getSegmentType());
        }

        if (hit.getType() == SegmentType.CONNECTOR) {

            ConnectorSegmentJS csjs = (ConnectorSegmentJS) js;
            return Objects.isNull(csjs.getConnectedId()) ? hit : PipelineConnection.single()
                    .segment(hit)
                    .id(csjs.getConnectedId())
                    .build();

        } else if (hit.getType() == SegmentType.SPLITTER) {

            SplitterSegmentJS ssjs = (SplitterSegmentJS) js;
            return PipelineConnection.outcome()
                    .pipelines(PipelinesConverter.from((OutcomesSegment<?, ?>) hit, ssjs.getOutcomeSegments()))
                    .links(ssjs.getOutcomeLinks())
                    .segment(hit)
                    .build();

        } else if (hit.getType() == SegmentType.SELECTOR) {

            SelectorSegmentJS ssjs = (SelectorSegmentJS) js;
            return PipelineConnection.outcome()
                    .pipelines(PipelinesConverter.from((OutcomesSegment<?, ?>) hit, ssjs.getOutcomeSegments()))
                    .links(ssjs.getOutcomeLinks())
                    .segment(hit)
                    .build();
        }

        return hit;
    }

    private static Map<Outcome, Pipeline> from(OutcomesSegment<?, ?> os, Map<String, List<SegmentJS>> input) {

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
}
