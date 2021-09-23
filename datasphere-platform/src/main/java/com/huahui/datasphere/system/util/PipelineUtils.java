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

package com.huahui.datasphere.system.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.unidata.mdm.system.configuration.SystemConfiguration;

import com.huahui.datasphere.system.exception.PipelineException;
import com.huahui.datasphere.system.exception.SystemExceptionIds;
import com.huahui.datasphere.system.service.PipelineService;
import com.huahui.datasphere.system.type.pipeline.Fallback;
import com.huahui.datasphere.system.type.pipeline.Finish;
import com.huahui.datasphere.system.type.pipeline.Pipeline;
import com.huahui.datasphere.system.type.pipeline.PipelineElement;
import com.huahui.datasphere.system.type.pipeline.PipelineInput;
import com.huahui.datasphere.system.type.pipeline.PipelineOutput;
import com.huahui.datasphere.system.type.pipeline.Point;
import com.huahui.datasphere.system.type.pipeline.Segment;
import com.huahui.datasphere.system.type.pipeline.SegmentType;
import com.huahui.datasphere.system.type.pipeline.Start;
import com.huahui.datasphere.system.type.pipeline.connection.PipelineConnection;
import com.huahui.datasphere.system.type.pipeline.connection.PipelineId;
import com.huahui.datasphere.system.type.pipeline.defaults.NullReturnFinish;
import com.huahui.datasphere.system.type.pipeline.defaults.PassThroughStart;

/**
 * @author Mikhail Mikhailov on Nov 25, 2019
 */
public class PipelineUtils {
    /**
     * The PS.
     */
    private static PipelineService pipelineService;
    /**
     * Disabling instantiation constructor.
     */
    private PipelineUtils() {
        super();
    }
    /**
     * Semi-private initializer. Called by the system.
     */
    public static void init() {
        pipelineService = SystemConfiguration.getBean(PipelineService.class);
    }
    /**
     * Finds a segment by given id.
     * @param id segment id
     * @return segment or null, if not found
     */
    @Nullable
    public static Segment findSegment(String id) {
        return pipelineService.segment(id);
    }
    /**
     * Finds a pipeline by given identity.
     * @param id the start id
     * @param subject the subject id
     * @return pipeline instance or null, if not found
     */
    @Nullable
    public static Pipeline findPipeline(String id, String subject) {
        return Objects.isNull(subject) ? pipelineService.getById(id) : pipelineService.getByIdAndSubject(id, subject);
    }
    /**
     * Serializes pipeline id to string.
     * @param p the pipeline
     * @return string
     */
    public static String toSerializablePipelineId(@Nullable Pipeline p) {
        return PipelineId.toSerializable(p);
    }
    /**
     * Gets pipeline id out of serialized form or null, if malformed.
     * @param id the pipeline id in serializable form
     * @return {@link PipelineId} instance or null
     */
    @Nullable
    public static PipelineId fromSerializedPipelineId(String id) {
        return PipelineId.fromSerializable(id);
    }
    /**
     * Builds an anonymous pipeline out of input.
     * @param elements segments or {@link PipelineConnection} elements as list
     * @return anonymous pipeline or empty pipeline on empty input
     */
    public static Pipeline toAnonymous(PipelineElement... elements) {
        return toAnonymous(null, null, elements);
    }
    /**
     * Builds an anonymous pipeline out of input.
     * @param in input type
     * @param out output type
     * @param elements segments or {@link PipelineConnection} elements as list
     * @return anonymous pipeline or empty pipeline on empty input
     */
    public static Pipeline toAnonymous(Class<? extends PipelineInput> in, Class<? extends PipelineOutput> out, PipelineElement... elements) {

        if (ArrayUtils.isEmpty(elements)) {
            return Pipeline.empty();
        }

        return toAnonymous(in, out, Arrays.asList(elements));
    }
    /**
     * Builds an anonymous pipeline out of input.
     * @param elements segments or {@link PipelineConnection} elements as list
     * @return anonymous pipeline or empty pipeline on empty input
     */
    public static Pipeline toAnonymous(List<PipelineElement> elements) {
        return toAnonymous(null, null, elements);
    }

    /**
     * Builds an anonymous pipeline out of input.
     * @param in input type
     * @param out output type
     * @param elements segments or {@link PipelineConnection} elements as list
     * @return anonymous pipeline or empty pipeline on empty input
     */
    public static Pipeline toAnonymous(Class<? extends PipelineInput> in, Class<? extends PipelineOutput> out, List<PipelineElement> elements) {

        if (CollectionUtils.isEmpty(elements)) {
            return Pipeline.empty();
        }

        PipelineElement el = elements.get(0);

        int i;
        Start<?, ?> start;
        if (el.isSegment() && el.toSegment().getType() == SegmentType.START) {
            start = (Start<?, ?>) el.toSegment();
            i = 1;
        } else {

            if (Objects.isNull(in) || Objects.isNull(out)) {
                throw new PipelineException(
                        "Anonymous pipeline neither defines a starting point, nor supplies valid input and output types for PASS THROUGH.",
                        SystemExceptionIds.EX_PIPELINE_NO_START_AND_TYPES_FOR_ANONYMOUS);
            }

            start = new PassThroughStart<>(in, out);
            i = 0;
        }

        Pipeline p = Pipeline.start(start, true);
        for (; i < elements.size(); i++) {

            el = elements.get(i);
            if (el.isConnection()) {
                p.with(el.toConnection());
                continue;
            }

            switch (el.toSegment().getType()) {
            case POINT:
                p.with((Point<?>) el.toSegment());
                break;
            case FALLBACK:
                p.with((Fallback<?>) el.toSegment());
                break;
            case FINISH:
                p.end((Finish<?, ?>) el.toSegment());
                break;
            default:
                break;
            }
        }

        Segment finish = p.getSegments().get(p.getSegments().size() - 1);
        if (finish.getType() != SegmentType.FINISH) {

            if (Objects.isNull(out)) {
                throw new PipelineException(
                        "Anonymous pipeline neither defines a finish point, nor supplies valid output type for PASS THROUGH.",
                        SystemExceptionIds.EX_PIPELINE_NO_FINISH_AND_TYPES_FOR_ANONYMOUS);
            }

            p.end(new NullReturnFinish<>(out));
        }

        return p;
    }
}
