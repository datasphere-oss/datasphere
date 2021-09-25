/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.huahui.datasphere.mdm.rest.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.huahui.datasphere.mdm.rest.system.ro.ErrorResponse;
import com.huahui.datasphere.mdm.rest.system.ro.RestResponse;
import com.huahui.datasphere.mdm.rest.system.ro.UpdateResponse;
import com.huahui.datasphere.mdm.rest.system.service.AbstractRestService;
import com.huahui.datasphere.mdm.system.configuration.SystemConfigurationConstants;
import com.huahui.datasphere.mdm.system.service.PipelineService;
import com.huahui.datasphere.mdm.system.type.pipeline.Pipeline;

import com.huahui.datasphere.mdm.rest.core.converter.PipelinesConverter;
import com.huahui.datasphere.mdm.rest.core.ro.pipeline.LoadAllPipelinesRO;
import com.huahui.datasphere.mdm.rest.core.ro.pipeline.PipelineIdRO;
import com.huahui.datasphere.mdm.rest.core.ro.pipeline.PipelineIdsRO;
import com.huahui.datasphere.mdm.rest.core.ro.pipeline.PipelineRO;
import com.huahui.datasphere.mdm.rest.core.ro.pipeline.SegmentsRO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * @author theseusyang on Nov 25, 2019
 */
@Path("/system/pipeline")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class PipelineRestService extends AbstractRestService {
    /**
     * All pipelines with their subjects.
     */
    public static final String PATH_ALL_PIPELINES = "all";
    /**
     * All start segments.
     */
    public static final String PATH_START_SEGMENTS = "start";
    /**
     * All segments, accepting given start segment.
     */
    public static final String PATH_ACCEPTING_SEGMENTS = "accepting";
    /**
     * All segments, compatible with the given outcome.
     */
    public static final String PATH_OUTCOME_SEGMENTS = "outcome-segments";
    /**
     * All pipeline IDs, compatible with the given outcome.
     */
    public static final String PATH_OUTCOME_PIPELINES = "outcome-pipelines";
    /**
     * Path param 'start segment ID'.
     */
    public static final String PARAM_START_ID = "startId";
    /**
     * Path param 'subject ID'.
     */
    public static final String PARAM_SUBJECT_ID = "subjectId";
    /**
     * Path param 'segment ID'.
     */
    public static final String PARAM_SEGMENT_ID = "segmentId";
    /**
     * Path param 'outcome ID'.
     */
    public static final String PARAM_OUTCOME_ID = "outcomeId";
    /**
     * The PS.
     */
    @Autowired
    private PipelineService pipelineService;
    /**
     * Loads all configured pipelines.
     * @return a possibly empty list of pipelines
     */
    @GET
    @Path("/" + PATH_ALL_PIPELINES)
    @Operation(
        description = "Load all active pipelines.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = LoadAllPipelinesRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response loadAllPipelines() {

        LoadAllPipelinesRO result = new LoadAllPipelinesRO();
        Map<String, Collection<Pipeline>> allWithSubjects = pipelineService.getAllWithSubjects();
        List<PipelineRO> converted = new ArrayList<>(allWithSubjects.size());

        for (Entry<String, Collection<Pipeline>> subject : allWithSubjects.entrySet()) {

            List<PipelineRO> ros = PipelinesConverter.toPipelines(subject.getValue());
            if (CollectionUtils.isNotEmpty(ros)) {
                ros.forEach(ro -> ro.setSubjectId(subject.getKey() == null ? LoadAllPipelinesRO.DEFAULT_SUBJECT : subject.getKey()));
            }

            converted.addAll(ros);
        }

        result.setPipelines(converted);
        return ok(new RestResponse<>(result));
    }

    /**
     * Loads all known start segments.
     * @return a possibly empty list of start segments
     */
    @GET
    @Path("/" + PATH_START_SEGMENTS)
    @Operation(
        description = "Load all known start segments.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = SegmentsRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response loadStartSegments() {

        SegmentsRO result = new SegmentsRO();
        result.setSegments(PipelinesConverter.toSegments(pipelineService.getStartSegments(), Pipeline.empty()));

        return ok(new RestResponse<>(result));
    }

    /**
     * Loads all segments, accepting given start segment.
     * @return a possibly empty list of segments
     */
    @GET
    @Path("/" + PATH_ACCEPTING_SEGMENTS + "/{" + PARAM_START_ID + "}")
    @Operation(
        description = "Load accepting segments for given start segment.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = SegmentsRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response loadAcceptingSegments(@Parameter(description = "Start segment ID", in = ParameterIn.PATH) @PathParam(PARAM_START_ID) String startId) {

        SegmentsRO result = new SegmentsRO();
        result.setSegments(PipelinesConverter.toSegments(pipelineService.getSegmentsForStart(startId), Pipeline.empty()));

        return ok(new RestResponse<>(result));
    }
    /**
     * Loads all segments, compatible with the given outcome.
     * @return a possibly empty list of segments
     */
    @GET
    @Path("/" + PATH_OUTCOME_SEGMENTS + "/{" + PARAM_SEGMENT_ID + "}/{" + PARAM_OUTCOME_ID + "}")
    @Operation(
        description = "Load all segments (including start segments), compatible with the given outcome.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = SegmentsRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response loadOutcomeSegments(
            @Parameter(description = "The segment ID", in = ParameterIn.PATH) @PathParam(PARAM_SEGMENT_ID) String segmentId,
            @Parameter(description = "The outcome ID", in = ParameterIn.PATH) @PathParam(PARAM_OUTCOME_ID) String outcomeId) {

        SegmentsRO result = new SegmentsRO();
        result.setSegments(PipelinesConverter.toSegments(pipelineService.getSegmentsForOutcome(segmentId, outcomeId), Pipeline.empty()));

        return ok(new RestResponse<>(result));
    }
    /**
     * Loads all pipeline IDs, compatible with the given outcome.
     * @return a possibly empty list of ids
     */
    @GET
    @Path("/" + PATH_OUTCOME_PIPELINES + "/{" + PARAM_SEGMENT_ID + "}/{" + PARAM_OUTCOME_ID + "}")
    @Operation(
        description = "Load all pipeline IDs, compatible with the given outcome.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = PipelineIdsRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response loadOutcomePipelines(
            @Parameter(description = "The segment ID", in = ParameterIn.PATH) @PathParam(PARAM_SEGMENT_ID) String segmentId,
            @Parameter(description = "The outcome ID", in = ParameterIn.PATH) @PathParam(PARAM_OUTCOME_ID) String outcomeId) {

        PipelineIdsRO result = new PipelineIdsRO();
        result.setPipelineIds(pipelineService.getPipelinesForOutcome(segmentId, outcomeId).stream()
                .map(Pipeline::getPipelineId)
                .map(PipelineIdRO::new)
                .collect(Collectors.toList()));

        return ok(new RestResponse<>(result));
    }
    /**
     * Saves a configured pipeline.
     * @param pipeline the pipeline to save
     * @return success/failure indicator
     */
    @POST
    @Operation(
        description = "Saves a configured pipeline.",
        method = "POST",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = PipelineRO.class))),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = SegmentsRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response savePipeline(PipelineRO pipeline) {

        pipelineService.save(PipelinesConverter.from(pipeline));
        return ok(new UpdateResponse(true, pipeline.getStartId()));
    }

    @DELETE
    @Path("{" + PARAM_START_ID + "}/{" + PARAM_SUBJECT_ID + "}")
    @Operation(
        description = "Deletes a configured pipeline.",
        method = "DELETE",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = UpdateResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response deletePipeline(
            @Parameter(description = "Start segment ID", in = ParameterIn.PATH) @PathParam(PARAM_START_ID) String startId,
            @Parameter(description = "Subject ID", in = ParameterIn.PATH, required = false) @PathParam(PARAM_SUBJECT_ID) String subjectId) {

        pipelineService.remove(startId, "-".equals(subjectId) || StringUtils.isBlank(subjectId) ? SystemConfigurationConstants.NON_SUBJECT : subjectId);
        return ok(new UpdateResponse(true, startId));
    }
}
