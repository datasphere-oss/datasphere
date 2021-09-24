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

package com.huahui.datasphere.mdm.system.type.pipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.huahui.datasphere.mdm.system.configuration.SystemConfigurationConstants;
import com.huahui.datasphere.mdm.system.exception.PipelineException;
import com.huahui.datasphere.mdm.system.exception.SystemExceptionIds;
import com.huahui.datasphere.mdm.system.type.pipeline.connection.PipelineConnection;
import com.huahui.datasphere.mdm.system.type.pipeline.connection.PipelineId;

/**
 * @author theseusyang
 * Pipeline instance object.
 */
public final class Pipeline {
    /**
     * Empty pipeline instance.
     */
    private static final Pipeline EMPTY = new Pipeline(null, null, null, false, false);
    /**
     * The start segment id.
     */
    private final String startId;
    /**
     * The subject id. May be null/empty string.
     */
    private final String subjectId;
    /**
     * The description, either supplied or generated.
     */
    private final String description;
    /**
     * Marks a pipeline as batched pipeline.
     */
    private final boolean batched;
    /**
     * Marks a pipeline as anonymous pipeline.
     */
    private final boolean anonymous;
    /**
     * Just the indicator that .end has already been called and the PL is closed.
     */
    private boolean finished;
    /**
     * Collected segments.
     */
    private final List<Segment> segments = new ArrayList<>();
    /**
     * Fallbacks.
     */
    private final List<Segment> fallbacks = new ArrayList<>();
    /**
     * Connected pipelines.
     */
    private Map<Segment, PipelineConnection> connections;
    /**
     * Constructor.
     * @param startId the start segment id.
     * @param subjectId the subject id. May be null/blank.
     * @param description the description.
     * @param batched the batched mark
     */
    private Pipeline(String startId, String subjectId, String description, boolean batched, boolean anonymous) {
        super();
        this.startId = startId;
        this.subjectId = subjectId;
        this.description = description;
        this.batched = batched;
        this.anonymous = anonymous;
    }
    /**
     * Gets the pipeline id for this pipeline.
     * @return pipeline id for this pipeline
     */
    public PipelineId getPipelineId() {
        return new PipelineId(startId, subjectId);
    }
    /**
     * Gets pipeline ID. Must be unique accross the system.
     * @return ID
     */
    public String getStartId() {
        return startId;
    }
    /**
     * @return the subjectId
     */
    public String getSubjectId() {
        return subjectId;
    }
    /**
     * Gets type description.
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Gets the participating segments collection.
     * @return segments
     */
    public List<Segment> getSegments() {
        return segments;
    }
    /**
     * Gets the pipeline fallback functions.
     * @return fallbacks
     */
    public List<Segment> getFallbacks() {
        return fallbacks;
    }
    /**
     * Gets pipeline connection associated with the given segment or null.
     * @param <C> the precise connection type
     * @param s the segment
     * @return connection or null, if not associated
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public<C extends PipelineConnection> C getConnection(Segment s) {
        return (C) getConnections().get(s);
    }
    /**
     * Gets the output type class of this pipeline.
     * @return class or null, if the pipeline was not started (is empty)
     */
    @Nullable
    public Class<?> getOutputTypeClass() {

        if (isEmpty()) {
            return null;
        }

        return ((Start<?, ?>) segments.get(0)).getOutputTypeClass();
    }
    /**
     * Gets the input type class of this pipeline.
     * @return class or null, if the pipeline was not started (is empty)
     */
    @Nullable
    public Class<?> getInputTypeClass() {

        if (isEmpty()) {
            return null;
        }

        return ((Start<?, ?>) segments.get(0)).getInputTypeClass();
    }
    /**
     * Returns true, if the segment is connected with another pipeline(s).
     * @param s the segment
     * @return true, if the segment is connected with another pipeline(s)
     */
    public boolean isConnected(Segment s) {
        return getConnections().containsKey(s);
    }
    /**
     * Returns finished state.
     * @return finished state
     */
    public boolean isFinished() {
        return finished;
    }
    /**
     * Returns batched state.
     * @return batched state
     */
    public boolean isBatched() {
        return batched;
    }
    /**
     * Tells whether this pipeline is an anonymous one.
     * @return true, if anonymous, false otherwise
     */
    public boolean isAnonymous() {
        return anonymous;
    }
    /**
     * Tells whether this pipeline is empty.
     * @return true, if empty, false otherwise
     */
    public boolean isEmpty() {
        return segments.isEmpty();
    }
    /**
     * Puts this pipeline to ready state.
     */
    public void ready() {
        getConnections().forEach((k, v) -> v.connect(k));
    }
    /**
     * Adds a point to this pipeline.
     * @param p the point
     * @return self
     */
    public Pipeline with(@Nonnull Point<? extends PipelineInput> p) {

        Objects.requireNonNull(p, "Point segment is null");

        throwIfPipelineClosed();
        throwIfPipelineBatchedMismatch(p);

        segments.add(p);
        return this;
    }
    /**
     * Adds a connector with pipeline selection at runtime.
     * @param c the connector
     * @return self
     */
    public Pipeline with(@Nonnull Connector<? extends PipelineInput, ? extends PipelineOutput> c) {

        Objects.requireNonNull(c, "Connector segment is null");

        throwIfPipelineClosed();
        throwIfPipelineBatchedMismatch(c);

        segments.add(c);
        return this;
    }
    /**
     * Adds a connector with pipeline selection at runtime.
     * @param c the connector
     * @param p the connected pipeline
     * @return self
     */
    public Pipeline with(@Nonnull Connector<? extends PipelineInput, ? extends PipelineOutput> c, @Nonnull PipelineConnection p) {

        Objects.requireNonNull(c, "Connector segment is null");
        Objects.requireNonNull(p, "Connector pipeline connection is null");

        throwIfPipelineClosed();
        throwIfPipelineBatchedMismatch(c);

        segments.add(c);

        ensureConnections().put(c, p);
        return this;
    }
    /**
     * Adds a splitter with pipeline selection at runtime.
     * @param s a {@link Splitter} instance
     * @param p a supplier, returning splitting outcomes
     * @return self
     */
    public Pipeline with(@Nonnull Splitter<? extends PipelineInput, ? extends PipelineOutput> s, PipelineConnection p) {

        Objects.requireNonNull(s, "Splitter segment is null");
        Objects.requireNonNull(p, "Splitter outcomes connection is null");

        throwIfPipelineClosed();
        throwIfPipelineBatchedMismatch(s);

        segments.add(s);

        ensureConnections().put(s, p);
        return this;
    }
    /**
     * Adds a selector with pipeline selection at runtime.
     * @param s a {@link Selector} instance
     * @param p a supplier, returning splitting outcomes
     * @return self
     */
    public Pipeline with(@Nonnull Selector<? extends PipelineInput, ? extends PipelineOutput> s, PipelineConnection p) {

        Objects.requireNonNull(s, "Selector segment is null");
        Objects.requireNonNull(p, "Selector outcomes connection is null");

        throwIfPipelineClosed();
        throwIfPipelineBatchedMismatch(s);

        segments.add(s);

        ensureConnections().put(s, p);
        return this;
    }
    /**
     * Connects using the supplied connection.
     * Connected segment must not be null!
     * @param p the connection
     * @return self
     */
    public Pipeline with(@Nonnull PipelineConnection p) {

        Objects.requireNonNull(p, "Pipeline connection is null");

        Segment segment = p.getSegment();
        Objects.requireNonNull(segment, "Connected segment is null");

        switch (segment.getType()) {
        case CONNECTOR:
            return with((Connector<?, ?>) segment, p);
        case SELECTOR:
            return with((Selector<?, ?>) segment, p);
        case SPLITTER:
            return with((Splitter<?, ?>) segment, p);
        default:
            break;
        }

        return this;
    }
    /**
     * Add a fallback function to pipeline.
     * @param fallback the fallback function
     * @return self
     */
    public Pipeline with(@Nonnull Fallback<? extends PipelineInput> fallback) {

        Objects.requireNonNull(fallback, "Fallback segment is null");

        throwIfPipelineBatchedMismatch(fallback);

        fallbacks.add(fallback);
        return this;
    }
    /**
     * Adds a connector with pipeline selection at runtime.
     * @param f the finish segment
     * @return self
     */
    public Pipeline end(@Nonnull Finish<? extends PipelineInput, ? extends PipelineOutput> f) {

        Objects.requireNonNull(f, "Finish segment is null");

        throwIfPipelineClosed();
        throwIfPipelineBatchedMismatch(f);

        segments.add(f);

        verify();

        finished = true;
        return this;
    }

    private void verify() {

        // 1. Every pipeline must support starting point input type in each segment
        Start<?, ?> start = (Start<?, ?>) segments.get(0);
        for (Segment s : segments) {

            Class<?> outputType = null;
            switch (s.getType()) {
            case SELECTOR:
                outputType = ((Selector<?, ?>) s).getOutputTypeClass();
                break;
            case SPLITTER:
                outputType = ((Splitter<?, ?>) s).getOutputTypeClass();
                break;
            case FINISH:
                outputType = ((Finish<?, ?>) s).getOutputTypeClass();
                break;
            default:
                break;
            }

            if (!s.supports(start)) {
                throw new PipelineException("Segment [{}] does not support pipeline's starting point [{}].",
                        SystemExceptionIds.EX_PIPELINE_START_SEGMENT_NOT_SUPPORTED, s.getId(), start.getId());
            }

            if (Objects.nonNull(outputType) && !start.getOutputTypeClass().isAssignableFrom(outputType)) {
                throw new PipelineException("[{}] output type does not match pipeline's output type [{}].",
                        SystemExceptionIds.EX_PIPELINE_OUTPUT_TYPES_DONT_MATCH, s.getId(), start.getOutputTypeClass().getCanonicalName());
            }
        }
    }

    private Map<Segment, PipelineConnection> ensureConnections() {

        if (Objects.isNull(connections)) {
            connections = new IdentityHashMap<>();
        }

        return connections;
    }

    private Map<Segment, PipelineConnection> getConnections() {
        return Objects.isNull(connections) ? Collections.emptyMap() : connections;
    }

    /**
     * Throws if this PL is already closed.
     */
    private void throwIfPipelineClosed() {
        if (finished) {
            throw new PipelineException("This pipeline is already finished.", SystemExceptionIds.EX_PIPELINE_ALREADY_FINISHED);
        }
    }
    /**
     * Throws if this PL is already closed.
     */
    private void throwIfPipelineBatchedMismatch(Segment s) {
        if (s.isBatched() != batched) {
            throw new PipelineException("Attempt to add a non batched segment to a batched pipeline or vice versa.",
                    SystemExceptionIds.EX_PIPELINE_BATCHED_MISMATCH);
        }
    }
    /**
     * Returns the empty pipeline singleton.
     * @return empty pipeline
     */
    public static Pipeline empty() {
        return EMPTY;
    }
    /**
     * Returns a pipeline instance.
     * @param s the starting point
     * @param anonymous build an anonymous pipeline, if true
     * @return pipeline
     */
    public static Pipeline start(@Nonnull Start<? extends PipelineInput, ? extends PipelineOutput> s, boolean anonymous) {

        Objects.requireNonNull(s, "Start segment is null");
        if (anonymous) {
            Pipeline p = new Pipeline(s.getId(), null, null, s.isBatched(), true);
            p.segments.add(s);
            return p;
        }

        return start(s);
    }
    /**
     * Starts a pipeline from starting point with no subject.
     * Description will be taken from the starting point.
     * @param s the starting point
     * @return a pipeline instance
     */
    public static Pipeline start(@Nonnull Start<? extends PipelineInput, ? extends PipelineOutput> s) {
        return start(s, null, null);
    }
    /**
     * Starts a pipeline from starting point and subject.
     * Description will be taken from the starting point.
     * @param s the starting point
     * @param subjectId the subject id, on which this pipeline overrides the default one
     * @return a pipeline instance
     */
    public static Pipeline start(@Nonnull Start<? extends PipelineInput, ? extends PipelineOutput> s, String subjectId) {
        return start(s, subjectId, null);
    }
    /**
     * Starts a named pipeline from starting point, using subject and description.
     * @param s the starting point
     * @param subjectId the subject id, on which this pipeline overrides the default one
     * @param description the description
     * @return a pipeline instance
     */
    public static Pipeline start(@Nonnull Start<? extends PipelineInput, ? extends PipelineOutput> s, String subjectId, String description) {
        Objects.requireNonNull(s, "Start segment is null");
        Pipeline p = new Pipeline(
                s.getId(),
                StringUtils.isBlank(subjectId) ? SystemConfigurationConstants.NON_SUBJECT : subjectId,
                StringUtils.isBlank(description) ? s.getDescription() : description,
                s.isBatched(),
                false);
        p.segments.add(s);
        return p;
    }
}
