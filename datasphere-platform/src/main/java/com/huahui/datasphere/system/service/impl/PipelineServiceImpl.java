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

package com.huahui.datasphere.system.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unidata.mdm.system.configuration.SystemConfigurationConstants;
import org.unidata.mdm.system.configuration.SystemConfigurationProperty;

import com.huahui.datasphere.convert.PipelinesConverter;
import com.huahui.datasphere.system.dao.PipelinesDAO;
import com.huahui.datasphere.system.exception.PipelineException;
import com.huahui.datasphere.system.exception.SystemExceptionIds;
import com.huahui.datasphere.system.po.PipelinePO;
import com.huahui.datasphere.system.serialization.json.PipelineJS;
import com.huahui.datasphere.system.serialization.json.PipelinesJS;
import com.huahui.datasphere.system.service.EventService;
import com.huahui.datasphere.system.service.ModuleService;
import com.huahui.datasphere.system.service.PipelineService;
import com.huahui.datasphere.system.service.RuntimePropertiesService;
import com.huahui.datasphere.system.type.configuration.ConfigurationValue;
import com.huahui.datasphere.system.type.event.Event;
import com.huahui.datasphere.system.type.event.EventReceiver;
import com.huahui.datasphere.system.type.event.impl.PipelineUpdate;
import com.huahui.datasphere.system.type.event.impl.PipelineUpdate.PipelineUpdateType;
import com.huahui.datasphere.system.type.module.Module;
import com.huahui.datasphere.system.type.pipeline.Connector;
import com.huahui.datasphere.system.type.pipeline.Fallback;
import com.huahui.datasphere.system.type.pipeline.Finish;
import com.huahui.datasphere.system.type.pipeline.Outcome;
import com.huahui.datasphere.system.type.pipeline.OutcomesSegment;
import com.huahui.datasphere.system.type.pipeline.Pipeline;
import com.huahui.datasphere.system.type.pipeline.PipelineInput;
import com.huahui.datasphere.system.type.pipeline.PipelineOutput;
import com.huahui.datasphere.system.type.pipeline.Point;
import com.huahui.datasphere.system.type.pipeline.Segment;
import com.huahui.datasphere.system.type.pipeline.SegmentType;
import com.huahui.datasphere.system.type.pipeline.Splitter;
import com.huahui.datasphere.system.type.pipeline.Start;
import com.huahui.datasphere.system.type.pipeline.defaults.PassThroughStart;
import com.huahui.datasphere.system.util.JsonUtils;
import com.huahui.datasphere.system.util.ResourceUtils;

/**
 * {@link PipelineService} implementation.
 * @author Mikhail Mikhailov on Nov 1, 2019
 */
@Service("pipelineService")
public class PipelineServiceImpl implements PipelineService, EventReceiver {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PipelineServiceImpl.class);
    /**
     * Pipelines by subjects.
     */
    private final ConcurrentMap<String, AdjacencyInfo> subjects = new ConcurrentHashMap<>();
    /**
     * Registered segments.
     */
    private final ConcurrentMap<String, Segment> segments = new ConcurrentHashMap<>();
    /**
     * ES instance.
     */
    @Autowired
    private EventService eventService;
    /**
     * Module registry.
     */
    @Autowired
    private ModuleService moduleService;
    /**
     * The RPS.
     */
    @Autowired
    private RuntimePropertiesService runtimePropertiesService;
    /**
     * The repo.
     */
    @Autowired
    private PipelinesDAO pipelinesDAO;
    /**
     * Constructor.
     */
    public PipelineServiceImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {

        // 1. Collect points
        for (Module m : moduleService.getModules()) {

            Collection<Segment> exported = m.getSegments();
            if (CollectionUtils.isNotEmpty(exported)) {
                segments.putAll(exported.stream().collect(Collectors.toMap(Segment::getId, Function.identity())));
            }
        }

        // 2. Read DB state
        cacheLoad();

        // 3. Register self as event consumer
        eventService.register(this, PipelineUpdate.class);

        // 4. Load possibly preconfigured initial pipelines
        ConfigurationValue<String> initialPipelines
            = runtimePropertiesService.getByProperty(SystemConfigurationProperty.SYSTEM_INITIAL_PIPELINES);

        if (initialPipelines.hasValue()) {

            try (InputStream is = ResourceUtils.asInputStream(initialPipelines.getValue())) {

                PipelinesJS pipelines = JsonUtils.read(is, PipelinesJS.class);
                if (Objects.isNull(pipelines) || CollectionUtils.isEmpty(pipelines.getPipelines())) {
                    return;
                }

                save(pipelines.getPipelines().stream()
                        .map(PipelinesConverter::from)
                        .collect(Collectors.toList()), false);

            } catch (IOException e) {
                LOGGER.error("Failed to load initial pipelines!", e);
            }
        }
    }

    private void cacheLoad() {

        List<PipelinePO> pos = pipelinesDAO.loadAll();
        List<Pipeline> defaults = new ArrayList<>();
        for (PipelinePO po : pos) {

            String startId = po.getStartId();
            String subjectId = po.getSubject();

            Start<?, ?> s = start(startId);
            if (Objects.isNull(s)) {
                LOGGER.warn("Start id [{}] does not exists. Skipping.", startId);
            } else {

                Pipeline result;
                try {
                    result = PipelinesConverter.from(JsonUtils.read(po.getContent(), PipelineJS.class));
                } catch (PipelineException e) {
                    LOGGER.warn("Pipeline exception caught. The pipeline with start id [{}], subject [{}] will not be loaded.",
                                startId, subjectId, e);
                    continue;
                }

                if (StringUtils.isNotBlank(subjectId)) {
                    subjects.put(subjectId, AdjacencyInfo.of(result));
                } else {
                    defaults.add(result);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(defaults)) {
            subjects.put(SystemConfigurationConstants.NON_SUBJECT, AdjacencyInfo.of(defaults));
        }

        subjects.forEach((k, v) -> {

            if (v.isSingleton()) {
                v.singleton.ready();
            } else if (v.isMultiple()) {
                v.multiple.values().forEach(Pipeline::ready);
            }
        });
    }

    private void cachePut(Pipeline pipeline) {

        if (StringUtils.isBlank(pipeline.getSubjectId())) {
            subjects.compute(SystemConfigurationConstants.NON_SUBJECT, (k, v) -> {
                if (Objects.isNull(v)) {
                    return AdjacencyInfo.of(Collections.singletonList(pipeline));
                }
                v.multiple.put(pipeline.getStartId(), pipeline);
                return v;
            });
        } else {
            subjects.compute(pipeline.getSubjectId(), (k, v) -> {
                if (Objects.isNull(v)) {
                    return AdjacencyInfo.of(pipeline);
                }
                v.singleton = pipeline;
                return v;
            });
        }

        pipeline.ready();
    }

    private void cacheRemove(String startId, String subjectId) {
        if (StringUtils.isBlank(subjectId)) {
            subjects.compute(SystemConfigurationConstants.NON_SUBJECT, (k, v) -> {
                if (Objects.isNull(v)) {
                    return null;
                }
                v.multiple.remove(startId);
                return v;
            });
        } else {
            subjects.compute(subjectId, (k, v) -> {
                if (Objects.isNull(v)) {
                    return null;
                }
                v.singleton = null;
                return v;
            });
        }
    }

    private void throwIfStartSegmentNotFound(Segment s, String id) {
        if (Objects.isNull(s) || s.getType() != SegmentType.START) {
            throw new PipelineException("Start segment not found by id [{}].",
                    SystemExceptionIds.EX_PIPELINE_START_SEGMENT_NOT_FOUND_BY_ID, id);
        }
    }

    private void throwIfOutcomeSegmentNotFound(Segment s, String id) {
        if (Objects.isNull(s) || (s.getType() != SegmentType.SPLITTER && s.getType() != SegmentType.SELECTOR)) {
            throw new PipelineException("Outcome segment not found by id [{}].",
                    SystemExceptionIds.EX_PIPELINE_OUTCOME_SEGMENT_NOT_FOUND_BY_ID, id);
        }
    }

    private void throwIfOutcomeNotFound(Outcome o, String name) {
        if (Objects.isNull(o)) {
            throw new PipelineException("Outcome [{}] not found by name.",
                    SystemExceptionIds.EX_PIPELINE_OUTCOME_NOT_FOUND_BY_NAME, name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pipeline getById(String id) {

        if (Objects.isNull(id)) {
            return null;
        }

        AdjacencyInfo info = subjects.get(SystemConfigurationConstants.NON_SUBJECT);
        return Objects.isNull(info) ? null : info.get(id);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Pipeline getByIdAndSubject(String id, String subject) {

        if (Objects.isNull(subject) || Objects.isNull(id)) {
            return null;
        }

        AdjacencyInfo info = subjects.get(subject);
        return Objects.isNull(info) ? null : info.get(id);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Pipeline> getAll() {

        Map<String, Pipeline> collected = new HashMap<>();
        for (Entry<String, AdjacencyInfo> entry : subjects.entrySet()) {
            entry.getValue().getAll().forEach(p -> collected.put(p.getStartId(), p));
        }

        return collected.values();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Collection<Pipeline>> getAllWithSubjects() {

        Map<String, Collection<Pipeline>> collected = new HashMap<>();
        for (Entry<String, AdjacencyInfo> entry : subjects.entrySet()) {
            if (SystemConfigurationConstants.NON_SUBJECT.equals(entry.getKey())) {
                collected.put(null, entry.getValue().getAll());
            } else {
                collected.put(entry.getKey(), entry.getValue().getAll());
            }
        }

        return collected;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Pipeline> getBySubject(String subject) {

        if (Objects.isNull(subject)) {
            return Collections.emptyList();
        }

        AdjacencyInfo info = subjects.get(subject);
        return Objects.isNull(info) ? Collections.emptyList() : info.getAll();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Segment> getStartSegments() {
        return segments.values().stream()
                .filter(s -> s.getType() == SegmentType.START)
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Segment> getPointSegments() {
        return segments.values().stream()
                .filter(s -> s.getType() == SegmentType.POINT)
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Segment> getConnectorSegments() {
        return segments.values().stream()
                .filter(s -> s.getType() == SegmentType.CONNECTOR)
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Segment> getSplitterSegments() {
        return segments.values().stream()
                .filter(s -> s.getType() == SegmentType.SPLITTER)
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Segment> getSelectorSegments() {
        return segments.values().stream()
                .filter(s -> s.getType() == SegmentType.SELECTOR)
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Segment> getFinishSegments() {
        return segments.values().stream()
                .filter(s -> s.getType() == SegmentType.FINISH)
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Segment> getFallbackSegments() {
        return segments.values().stream()
                .filter(s -> s.getType() == SegmentType.FALLBACK)
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Segment> getSegmentsForStart(String id) {

        Segment s = segments.get(id);

        throwIfStartSegmentNotFound(s, id);

        Start<?, ?> start = (Start<?, ?>) s;
        return segments.values().stream()
                .filter(segment -> segment.getType() != SegmentType.START && s.isBatched() == segment.isBatched() && segment.supports(start))
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Segment> getSegmentsForOutcome(String id, String outcome) {

        Segment s = segments.get(id);

        throwIfOutcomeSegmentNotFound(s, id);

        OutcomesSegment<?, ?> os = (OutcomesSegment<?, ?>) s;
        Outcome out = Stream.of(os.getOutcomes())
                .filter(o -> StringUtils.equals(o.getName(), outcome))
                .findFirst()
                .orElse(null);

        throwIfOutcomeNotFound(out, outcome);

        PassThroughStart<?, ?> check
            = new PassThroughStart<>(out.getInputTypeClass(), out.getOutputTypeClass());

        return segments.values().stream()
                .filter(segment -> {
                    switch (segment.getType()) {
                    case START:
                        return ((Start<?, ?>) segment).getInputTypeClass().isAssignableFrom(out.getInputTypeClass())
                            && out.getOutputTypeClass().isAssignableFrom(((Start<?, ?>) segment).getOutputTypeClass());
                    case FINISH:
                        return out.getOutputTypeClass().isAssignableFrom(((Finish<?, ?>) segment).getOutputTypeClass());
                    default:
                        return segment.supports(check);
                    }
                })
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Pipeline> getPipelinesForOutcome(String id, String outcome) {

        Segment s = segments.get(id);

        throwIfOutcomeSegmentNotFound(s, id);

        OutcomesSegment<?, ?> os = (OutcomesSegment<?, ?>) s;
        Outcome out = Stream.of(os.getOutcomes())
                .filter(o -> StringUtils.equals(o.getName(), outcome))
                .findFirst()
                .orElse(null);

        throwIfOutcomeNotFound(out, outcome);

        return getAll().stream()
                .filter(p -> p.getInputTypeClass().isAssignableFrom(out.getInputTypeClass())
                          && out.getOutputTypeClass().isAssignableFrom(p.getOutputTypeClass()))
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Pipeline pipeline) {

        if (Objects.isNull(pipeline)) {
            return;
        }

        PipelinePO po = new PipelinePO();
        po.setStartId(pipeline.getStartId());
        po.setSubject(pipeline.getSubjectId());
        po.setContent(JsonUtils.write(PipelinesConverter.to(pipeline)));

        pipelinesDAO.save(po);

        cachePut(pipeline);

        PipelineUpdate evt = new PipelineUpdate();
        evt.setStartId(pipeline.getStartId());
        evt.setSubjectId(pipeline.getSubjectId());
        evt.setUpdateType(PipelineUpdateType.UPSERT);

        eventService.fire(evt);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Collection<Pipeline> pipelines, boolean overwrite) {

        if (CollectionUtils.isEmpty(pipelines)) {
            return;
        }

        for (Pipeline p : pipelines) {

            Pipeline existing;
            if (StringUtils.isNotBlank(p.getSubjectId())) {
                existing = getByIdAndSubject(p.getStartId(), p.getSubjectId());
            } else {
                existing = getById(p.getStartId());
            }

            if (Objects.nonNull(existing) && !overwrite) {
                LOGGER.info("Pipeline '{}:{}' already exists and won't be overwritten. Skipping.",
                        existing.getStartId(),
                        existing.getSubjectId());
                continue;
            }

            LOGGER.info("Saving Pipeline '{}:{}' as default {}overwriting an existing one.",
                    p.getStartId(),
                    p.getSubjectId(),
                    Objects.isNull(existing) ? "NOT " : StringUtils.EMPTY);

            try {
                save(p);
            } catch (Exception e) {
                LOGGER.warn("Failed to save pipeline '{}:{}'. Skipping.",
                        p.getStartId(),
                        p.getSubjectId(),
                        e);
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(String startId, String subjectId) {

        Objects.requireNonNull(startId, "Start id must not be null");

        pipelinesDAO.delete(startId,
                StringUtils.isBlank(subjectId)
                    ? SystemConfigurationConstants.NON_SUBJECT
                    : subjectId);

        cacheRemove(startId, subjectId);

        PipelineUpdate evt = new PipelineUpdate();
        evt.setStartId(startId);
        evt.setSubjectId(subjectId);
        evt.setUpdateType(PipelineUpdateType.REMOVAL);

        eventService.fire(evt);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Segment segment(String id) {
        return segments.get(id);
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C extends PipelineInput, O extends PipelineOutput> Start<C, O> start(String id) {

        Segment s = segments.get(id);
        if (Objects.nonNull(s) && s.getType() == SegmentType.START) {
            return (Start<C, O>) s;
        }

        return null;
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C extends PipelineInput> Point<C> point(String id) {

        Segment s = segments.get(id);
        if (Objects.nonNull(s) && s.getType() == SegmentType.POINT) {
            return (Point<C>) s;
        }

        return null;
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C extends PipelineInput, R extends PipelineOutput> Connector<C, R> connector(String id) {

        Segment s = segments.get(id);
        if (Objects.nonNull(s) && s.getType() == SegmentType.CONNECTOR) {
            return (Connector<C, R>) s;
        }

        return null;
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C extends PipelineInput, R extends PipelineOutput> Splitter<C, R> splitter(String id) {

        Segment s = segments.get(id);
        if (Objects.nonNull(s) && s.getType() == SegmentType.SPLITTER) {
            return (Splitter<C, R>) s;
        }

        return null;
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C extends PipelineInput> Fallback<C> fallback(String id) {

        Segment s = segments.get(id);
        if (Objects.nonNull(s) && s.getType() == SegmentType.FALLBACK) {
            return (Fallback<C>) s;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C extends PipelineInput, R extends PipelineOutput> Finish<C, R> finish(String id) {

        Segment s = segments.get(id);
        if (Objects.nonNull(s) && s.getType() == SegmentType.FINISH) {
            return (Finish<C, R>) s;
        }

        return null;
    }
    @Override
    public void receive(Event event) {

        PipelineUpdate pu = (PipelineUpdate) event;

        if (pu.getUpdateType() == PipelineUpdateType.UPSERT) {

            PipelinePO po = pipelinesDAO.load(pu.getStartId(),
                    StringUtils.isBlank(pu.getSubjectId())
                        ? SystemConfigurationConstants.NON_SUBJECT
                        : pu.getSubjectId());

            if (Objects.isNull(po)) {
                LOGGER.warn("Recived Pipeline update event for start ID [{}], subject ID [{}], but the pipeline is missing in the DB!",
                        pu.getStartId(), pu.getSubjectId());
                return;
            }

            cachePut(PipelinesConverter.from(JsonUtils.read(po.getContent(), PipelineJS.class)));
        } else if (pu.getUpdateType() == PipelineUpdateType.REMOVAL) {

            cacheRemove(pu.getStartId(),
                    StringUtils.isBlank(pu.getSubjectId())
                        ? SystemConfigurationConstants.NON_SUBJECT
                        : pu.getSubjectId());
        }
    }

    /**
     * Pipelines for a particular subject.
     * @author Mikhail Mikhailov on Nov 22, 2019
     */
    private static class AdjacencyInfo {
        /**
         * Connected pipelines, keyed by ID.
         */
        private Map<String, Pipeline> multiple;
        /**
         * Singleton pipeline on a real subject.
         */
        private Pipeline singleton;
        /**
         * Constructor.
         */
        public AdjacencyInfo() {
            super();
        }
        /**
         * Gets a pipeline by its id.
         * @param id the pipeline id
         * @return pipeline instance or null
         */
        @Nullable
        public Pipeline get(String id) {

            if (isSingleton()) {
                return singleton.getStartId().equals(id) ? singleton : null;
            } else if (isMultiple()) {
                return multiple.get(id);
            }

            return null;
        }
        /**
         * Gets all currently hold pipelines.
         * @return collection of pipelines
         */
        public Collection<Pipeline> getAll() {

            if (isSingleton()) {
                return Collections.singletonList(singleton);
            } else if (isMultiple()) {
                return multiple.values();
            }

            return Collections.emptyList();
        }
        /**
         * Checks for singleton condition.
         * @return true for singleton, false otherwise
         */
        public boolean isSingleton() {
            return this.singleton != null && this.multiple == null;
        }
        /**
         * Checks for multiple condition.
         * @return true for multiple, false otherwise
         */
        public boolean isMultiple() {
            return this.singleton == null && this.multiple != null;
        }
        /**
         * Creates info instance.
         * @param p the pipeline
         * @return info
         */
        public static AdjacencyInfo of(@Nonnull Pipeline p) {
            Objects.requireNonNull(p, "Pipeline cannot be null.");
            Objects.requireNonNull(p.getStartId(), "Pipeline's ID cannot be null.");
            AdjacencyInfo info = new AdjacencyInfo();
            info.singleton = p;
            return info;
        }
        /**
         * Creates info instance.
         * @param p the pipeline
         * @return info
         */
        public static AdjacencyInfo of(@Nonnull Collection<Pipeline> p) {
            Objects.requireNonNull(p, "Pipeline cannot be null.");
            AdjacencyInfo info = new AdjacencyInfo();
            info.multiple = new HashMap<>();
            p.forEach(pipeline -> info.multiple.put(pipeline.getStartId(), pipeline));
            return info;
        }
    }
}
