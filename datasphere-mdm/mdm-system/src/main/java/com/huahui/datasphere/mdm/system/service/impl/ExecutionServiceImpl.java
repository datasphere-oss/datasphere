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

package com.huahui.datasphere.mdm.system.service.impl;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huahui.datasphere.mdm.system.context.ThrowableCauseAwareContext;
import com.huahui.datasphere.mdm.system.exception.PipelineException;
import com.huahui.datasphere.mdm.system.exception.SystemExceptionIds;
import com.huahui.datasphere.mdm.system.service.ExecutionService;
import com.huahui.datasphere.mdm.system.service.PipelineService;
import com.huahui.datasphere.mdm.system.type.pipeline.Connector;
import com.huahui.datasphere.mdm.system.type.pipeline.Fallback;
import com.huahui.datasphere.mdm.system.type.pipeline.Finish;
import com.huahui.datasphere.mdm.system.type.pipeline.Outcome;
import com.huahui.datasphere.mdm.system.type.pipeline.Pipeline;
import com.huahui.datasphere.mdm.system.type.pipeline.PipelineInput;
import com.huahui.datasphere.mdm.system.type.pipeline.PipelineOutput;
import com.huahui.datasphere.mdm.system.type.pipeline.Point;
import com.huahui.datasphere.mdm.system.type.pipeline.Segment;
import com.huahui.datasphere.mdm.system.type.pipeline.Selector;
import com.huahui.datasphere.mdm.system.type.pipeline.Splitter;
import com.huahui.datasphere.mdm.system.type.pipeline.Start;
import com.huahui.datasphere.mdm.system.type.pipeline.connection.OutcomesPipelineConnection;
import com.huahui.datasphere.mdm.system.type.pipeline.connection.SinglePipelineConnection;
import com.huahui.datasphere.mdm.system.type.pipeline.fragment.FragmentId;
import com.huahui.datasphere.mdm.system.type.pipeline.fragment.OutputFragment;
import com.huahui.datasphere.mdm.system.type.pipeline.fragment.OutputFragmentCollector;

/**
 * @author theseusyang
 */
@Service
public class ExecutionServiceImpl implements ExecutionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionServiceImpl.class);
    /**
     * Pipeline servivce.
     */
    @Autowired
    private PipelineService pipelineService;
    /**
     * Constructor.
     */
    public ExecutionServiceImpl() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public <C extends PipelineInput, R extends PipelineOutput> R execute(C ctx) {

        if (Objects.isNull(ctx)) {
            return null;
        }

        String id = ctx.getStartTypeId();
        Start<C, R> s = pipelineService.start(id);
        if (Objects.isNull(s)) {
            throw new PipelineException("Pipeline start type [{}] not found.",
                    SystemExceptionIds.EX_PIPELINE_START_TYPE_NOT_FOUND,
                    id);
        }

        String subject = s.subject(ctx);

        Pipeline p = null;
        if (Objects.nonNull(subject)) {
            p = pipelineService.getByIdAndSubject(id, subject);
        }

        if (Objects.isNull(p)) {
            p = pipelineService.getById(id);
        }

        if (Objects.isNull(p)) {
            throw new PipelineException("Pipeline not found by id [{}], subject [{}].",
                    SystemExceptionIds.EX_PIPELINE_NOT_FOUND_BY_ID_AND_SUBJECT,
                    id, subject);
        }

        return execute(p, ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C extends PipelineInput, R extends PipelineOutput> R execute(Pipeline p, C ctx) {

        if (Objects.isNull(p) || p.isEmpty() || Objects.isNull(ctx)) {
            return null;
        }

        if (!p.isFinished()) {
            throw new PipelineException("Cannot execute the pipeline, the pipeline is not finished yet.",
                    SystemExceptionIds.EX_PIPELINE_IS_NOT_FINISHED);
        }

        ExecutionState<R> state = new ExecutionState<>(p);
        for (int i = 0; i < p.getSegments().size(); i++) {

            Segment s = p.getSegments().get(i);
            try {

                execSegment(state, s, ctx);

                // Stop on first non-null result
                if (state.hasResult()) {
                    return state.toResult();
                }

            } catch (Exception e) {

                if (ctx instanceof ThrowableCauseAwareContext) {
                    ((ThrowableCauseAwareContext) ctx).throwableCause(e);
                }

                execFallbacks(ctx, e, p.getFallbacks());
                throw new PipelineException("Execution of the pipeline [{} / {}] failed.", e,
                        SystemExceptionIds.EX_PIPELINE_EXECUTION_FAILED, p.getStartId(), p.getSubjectId());
            }
        }

        return null;
    }

    private <C extends PipelineInput, R extends PipelineOutput> void execSegment(ExecutionState<R> state, Segment s, C ctx) {

        switch (s.getType()) {
            case START:
                execStart(s, ctx);
                break;
            case POINT:
                execPoint(s, ctx);
                break;
            case CONNECTOR:
                state.afterConnector(execConnector(s, ctx, state.getPipeline()));
                break;
            case SPLITTER:
                state.afterFinish(execSplitter(s, ctx, state.getPipeline()));
                break;
            case SELECTOR:
                state.afterFinish(execSelector(s, ctx, state.getPipeline()));
                break;
            case FINISH:
                state.afterFinish(execFinish(s, ctx));
                break;
            default:
                break;
        }
    }

    /**
     * Executes start segment.
     * @param <C> the context type
     * @param s the start segment
     * @param ctx the context
     */
    @SuppressWarnings("unchecked")
    private <C extends PipelineInput, O extends PipelineOutput> void execStart(Segment s, C ctx) {
        ((Start<C, O>) s).start(ctx);
    }
    /**
     * Executes point segment.
     * @param <C> the context type
     * @param s the point segment
     * @param ctx the context
     */
    @SuppressWarnings("unchecked")
    private <C extends PipelineInput> void execPoint(Segment s, C ctx) {
        ((Point<C>) s).point(ctx);
    }
    /**
     * Executes connector segment.
     * @param <C> the context type
     * @param s the connector segment
     * @param ctx the context
     * @param p the pipeline, being executed
     * @return result fragment or null
     */
    @SuppressWarnings("unchecked")
    private <C extends PipelineInput> PipelineOutput execConnector(Segment s, C ctx, Pipeline p) {

        Connector<C, ? extends PipelineOutput> c = (Connector<C, ? extends PipelineOutput>) s;
        SinglePipelineConnection connection = p.getConnection(c);

        if (Objects.nonNull(connection) && connection.isConnected()) {
            return c.connect(ctx, connection.getPipeline());
        }

        return c.connect(ctx);
    }
    /**
     * Executes connector segment.
     * @param <C> the context type
     * @param s the connector segment
     * @param ctx the context
     * @param p the pipeline, being executed
     * @return result fragment or null
     */
    @SuppressWarnings("unchecked")
    private <C extends PipelineInput, R extends PipelineOutput> R execSplitter(Segment s, C ctx, Pipeline p) {

        Splitter<C, R> c = (Splitter<C, R>) s;

        Outcome[] outcomes = c.split(ctx);
        if (ArrayUtils.isNotEmpty(outcomes)) {

            OutcomesPipelineConnection connection = p.getConnection(c);
            if (Objects.nonNull(connection) && connection.isConnected()) {

                Map<Outcome, PipelineOutput> collected = new HashMap<>();
                for (Outcome outcome : outcomes) {

                    Pipeline connected = connection.getPipeline(outcome);
                    Objects.requireNonNull(connected, "Splitter split must not return unknown/unmapped outcomes.");

                    PipelineOutput out = execute(connected, c.in(ctx, outcome));
                    collected.put(outcome, out);
                }

                return c.join(collected);
            }
        }

        return null;
    }
    /**
     * Executes selector segment.
     * @param <C> the context type
     * @param s the selector segment
     * @param ctx the context
     * @param p the pipeline, currently being executed
     * @return result or null
     */
    @SuppressWarnings("unchecked")
    private <C extends PipelineInput, R extends PipelineOutput> R execSelector(Segment s, C ctx, Pipeline p) {

        Selector<C, R> c = (Selector<C, R>) s;

        Outcome outcome = c.select(ctx);
        if (Objects.nonNull(outcome)) {

            OutcomesPipelineConnection connection = p.getConnection(c);
            if (Objects.nonNull(connection) && connection.isConnected()) {

                Pipeline connected = connection.getPipeline(outcome);
                Objects.requireNonNull(connected, "Selector's select method must not return unknown/unmapped outcomes.");

                return c.out(execute(connected, c.in(ctx, outcome)), outcome);
            }
        }

        return null;
    }
    /**
     * Executes finishing segment.
     * @param <C> the input type
     * @param <R> the output type
     * @param s the finish segment
     * @param ctx the context
     * @return result or null
     */
    @SuppressWarnings("unchecked")
    private <C extends PipelineInput, R extends PipelineOutput> R execFinish(Segment s, C ctx) {
        return ((Finish<C, R>) s).finish(ctx);
    }
    /**
     * Executes fallback segments.
     * @param <C> the input type
     * @param c the input
     * @param t the throwable, caused the execution
     * @param fallbacks list of fallbacks
     */
    @SuppressWarnings("unchecked")
    private <C extends PipelineInput> void execFallbacks(final C c, final Throwable t, final List<Segment> fallbacks) {
        for (int i = 0; i < fallbacks.size(); i++) {
            try {
                ((Fallback<C>) fallbacks.get(i)).accept(c, t);
            } catch (Exception e) {
                LOGGER.error("Error while executing fallback.", e);
            }
        }
    }
    /**
     * @author theseusyang on Dec 26, 2020
     * A SINGLE pipeline execution state.
     */
    private class ExecutionState <R extends PipelineOutput> {

        private final Pipeline pipeline;

        private R result = null;

        private Map<FragmentId<?>, OutputFragment<?>> collected = null;

        public ExecutionState(Pipeline pipeline) {
            super();
            this.pipeline = pipeline;
        }

        /**
         * @return the pipeline
         */
        public Pipeline getPipeline() {
            return pipeline;
        }

        public void afterConnector(PipelineOutput intermediate) {

            // Return if the pipeline is not supposed to return composite result or returned null.
            if (intermediate == null || !OutputFragment.class.isAssignableFrom(intermediate.getClass())) {
                return;
            }

            if (Objects.isNull(collected)) {
                collected = new IdentityHashMap<>();
            }

            OutputFragment<?> fragment = (OutputFragment<?>) intermediate;
            collected.put(fragment.fragmentId(), fragment);
        }

        public void afterFinish(R result) {
            this.result = result;
        }

        public boolean hasResult() {
            return Objects.nonNull(result);
        }

        public R toResult() {

            if (result instanceof OutputFragmentCollector && MapUtils.isNotEmpty(collected)) {
                OutputFragmentCollector<?> composite = (OutputFragmentCollector<?>) result;
                collected.values().forEach(composite::fragment);
            }

            return result;
        }
    }
}
