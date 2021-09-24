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

package com.huahui.datasphere.mdm.system.service;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.huahui.datasphere.mdm.system.type.pipeline.Connector;
import com.huahui.datasphere.mdm.system.type.pipeline.Fallback;
import com.huahui.datasphere.mdm.system.type.pipeline.Finish;
import com.huahui.datasphere.mdm.system.type.pipeline.Pipeline;
import com.huahui.datasphere.mdm.system.type.pipeline.PipelineInput;
import com.huahui.datasphere.mdm.system.type.pipeline.PipelineOutput;
import com.huahui.datasphere.mdm.system.type.pipeline.Point;
import com.huahui.datasphere.mdm.system.type.pipeline.Segment;
import com.huahui.datasphere.mdm.system.type.pipeline.Splitter;
import com.huahui.datasphere.mdm.system.type.pipeline.Start;

/**
 * The service, responsible for pipeline management.
 * @author theseusyang
 */
public interface PipelineService {
    /**
     * Gets all pipelines, known to the service.
     * @return collection of pipelines
     */
    Collection<Pipeline> getAll();
    /**
     * Gets all pipelines, known to the service.
     * @return collection of pipelines
     */
    Map<String, Collection<Pipeline>> getAllWithSubjects();
    /**
     * Loads a cached or previously saved {@link Pipeline}, associated with the given id and having NO subject.
     * Note, pipeline's id is the ID of its start point if it is not overridden manually.
     * If it is not overridden, it can be autoselected by execution service.
     * @param id the pipeline id.
     * @return a pipeline instance or null, if nothing found
     */
    @Nullable
    Pipeline getById(String id);
    /**
     * Loads a cached or previously saved {@link Pipeline}, associated with the given id and particular subject.
     * Note, pipeline's id is the ID of its start point if it is not overridden manually.
     * If it is not overridden, it can be autoselected by execution service.
     * @param id the pipeline id
     * @param subject the subject to run the pipeline on
     * @return a pipeline instance or null, if nothing found
     */
    @Nullable
    Pipeline getByIdAndSubject(String id, String subject);
    /**
     * Loads cached or previously saved {@link Pipeline} instances, associated with particular subject.
     * @param subject the subject to run the pipeline on
     * @return a pipeline instance or null, if nothing found
     */
    Collection<Pipeline> getBySubject(String subject);
    /**
     * Gets all start segments, registered by the system.
     * @return collection of start segments
     */
    Collection<Segment> getStartSegments();
    /**
     * Gets all point segments, registered by the system.
     * @return collection of point segments
     */
    Collection<Segment> getPointSegments();
    /**
     * Gets all connector segments, registered by the system.
     * @return collection of connector segments
     */
    Collection<Segment> getConnectorSegments();
    /**
     * Gets all splitter segments, registered by the system.
     * @return collection of splitter segments
     */
    Collection<Segment> getSplitterSegments();
    /**
     * Gets all selector segments, registered by the system.
     * @return collection of selector segments
     */
    Collection<Segment> getSelectorSegments();
    /**
     * Gets all finish segments, registered by the system.
     * @return collection of finish segments
     */
    Collection<Segment> getFinishSegments();
    /**
     * Gets all fallback segments, registered on the system.
     * @return collectionf of fallback segments
     */
    Collection<Segment> getFallbackSegments();
    /**
     * Gets segments for the given start ID.
     * @param id the start segment ID
     * @return collection of segments
     */
    Collection<Segment> getSegmentsForStart(String id);
    /**
     * Gets segments for the given segment ID and outcome ID.
     * @param id the splitter/selector segment ID
     * @param outcome the outcome id
     * @return collection of segments
     */
    Collection<Segment> getSegmentsForOutcome(String id, String outcome);
    /**
     * Gets pipelines, which may potentially be executed for the given outcome,
     * i. e. which input and output types are conform to those of the outcome.
     * @param id outcome segment id
     * @param outcome the outcome id
     * @return collection of pipelines
     */
    Collection<Pipeline> getPipelinesForOutcome(String id, String outcome);
    /**
     * Saves and caches given pipeline with NO subject, associated with it.
     * @param pipeline the pipeline to save
     */
    void save(@Nonnull Pipeline pipeline);
    /**
     * Saves a serie of pipelines, with overwrite option.
     * @param pipelines the pipelines to save
     * @param overwrite if true, will overwrite existing pipelines with the same segment id and subject id
     */
    void save(Collection<Pipeline> pipelines, boolean overwrite);
    /**
     * Removes a pipeline.
     * @param startId the start ID
     * @param subjectId the subject ID, may be blank
     */
    void remove(@Nonnull String startId, @Nullable String subjectId);
    /**
     * Get uncpecified segment by id.
     * @param id the id
     * @return segment or null
     */
    @Nullable
    Segment segment(String id);
    /**
     * Get segment specifically as a start segment by id.
     * @param <C> the context type
     * @param id the id
     * @return start segment or null
     */
    @Nullable
    <C extends PipelineInput, O extends PipelineOutput> Start<C, O> start(String id);
    /**
     * Get segment specifically as a point segment by id.
     * @param <C> the context type
     * @param id the id
     * @return point segment or null
     */
    @Nullable
    <C extends PipelineInput> Point<C> point(String id);
    /**
     * Get segment specifically as a connector segment by id.
     * @param <C> the context type
     * @param <R> the result type
     * @param id the id
     * @return connector segment or null
     */
    @Nullable
    <C extends PipelineInput, R extends PipelineOutput> Connector<C, R> connector(String id);
    /**
     * Gets splitter segment by id.
     * @param <C> the segment input type
     * @param id the id
     * @return splitter segment or null
     */
    @Nullable
    <C extends PipelineInput, R extends PipelineOutput> Splitter<C, R> splitter(String id);
    /**
     * Get segment specifically as a finish segment by id.
     * @param <C> the context type
     * @param <R> the result type
     * @param id the id
     * @return finish segment or null
     */
    @Nullable
    <C extends PipelineInput, R extends PipelineOutput> Finish<C, R> finish(String id);
    /**
     * Gets a fallback segment by id.
     * @param <C> The input type
     * @param <T> The throwable type
     * @param id Segment id
     * @return fallback segment or null
     */
    @Nullable
    <C extends PipelineInput> Fallback<C> fallback(String id);
    /**
     * Loads execution pipelines during platform bootstrap.
     */
    void init();
}
