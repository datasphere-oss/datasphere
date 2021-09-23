package com.huahui.datasphere.system.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huahui.datasphere.system.context.InputCollector;
import com.huahui.datasphere.system.dto.OutputContainer;
import com.huahui.datasphere.system.exception.PlatformRuntimeException;
import com.huahui.datasphere.system.service.ModuleService;
import com.huahui.datasphere.system.service.RenderingService;
import com.huahui.datasphere.system.type.module.Module;
import com.huahui.datasphere.system.type.rendering.ErrorOutputFragmentRenderer;
import com.huahui.datasphere.system.type.rendering.InputFragmentRenderer;
import com.huahui.datasphere.system.type.rendering.InputRenderingAction;
import com.huahui.datasphere.system.type.rendering.InputSource;
import com.huahui.datasphere.system.type.rendering.OutputFragmentRenderer;
import com.huahui.datasphere.system.type.rendering.OutputRenderingAction;
import com.huahui.datasphere.system.type.rendering.OutputSink;
import com.huahui.datasphere.system.type.rendering.RenderingAction;
import com.huahui.datasphere.system.type.rendering.RenderingProvider;

/**
 * @author Mikhail Mikhailov on Jan 15, 2020
 */
@Service
public class RenderingServiceImpl implements RenderingService {
    /**
     * Empty output fragment container
     */
    private static final OutputContainer EMPTY_OUTPUT_CONTAINER = new OutputContainer(){};
    /**
     * Input renderers.
     */
    private final Map<InputRenderingAction, List<InputFragmentRenderer>> inputRenderers = new IdentityHashMap<>();
    /**
     * Output renderers.
     */
    private final Map<OutputRenderingAction, List<OutputFragmentRenderer>> outputRenderers = new IdentityHashMap<>();
    /**
     * Error output renderers.
     */
    private final Map<OutputRenderingAction, List<ErrorOutputFragmentRenderer>> errorOutputRenderers = new IdentityHashMap<>();
    /**
     * The module service.
     */
    @Autowired
    private ModuleService moduleService;
    /**
     * {@inheritDoc}
     */
    @Override
    public void renderInput(String version, InputRenderingAction action, InputCollector collector, InputSource source) {

        List<InputFragmentRenderer> renderers = inputRenderers.get(action);
        if (CollectionUtils.isNotEmpty(renderers)) {
            for (InputFragmentRenderer ifr : renderers) {
                ifr.render(version, collector, source);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderOutput(String version, OutputRenderingAction action, OutputContainer container, OutputSink sink) {

        List<OutputFragmentRenderer> renderers = outputRenderers.get(action);
        if (CollectionUtils.isNotEmpty(renderers)) {
            for (OutputFragmentRenderer ifr : renderers) {
                ifr.render(version, container, sink);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderOutput(OutputRenderingAction action, OutputSink sink) {
        renderOutput(action, EMPTY_OUTPUT_CONTAINER, sink);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean renderOutput(OutputRenderingAction action, PlatformRuntimeException e, OutputSink sink) {

        List<ErrorOutputFragmentRenderer> renderers = errorOutputRenderers.get(action);
        boolean result = false;
        if (CollectionUtils.isEmpty(renderers)) {
            return result;
        }

        for (ErrorOutputFragmentRenderer ifr : renderers) {
            if (ifr.onError(EMPTY_OUTPUT_CONTAINER, e, sink)) {
                result = true;
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {

        List<InputRenderingAction> ira = new ArrayList<>();
        List<OutputRenderingAction> ora = new ArrayList<>();
        List<RenderingProvider> collected = new ArrayList<>();

        for (Module m : moduleService.getModules()) {
            Collection<RenderingProvider> providers = m.getRenderingProviders();
            if (CollectionUtils.isNotEmpty(providers)) {
                collected.addAll(providers);
            }

            for (RenderingAction ra : m.getRenderingActions()) {
                switch (ra.actionType()) {
                case INPUT:
                    ira.add((InputRenderingAction) ra);
                    break;
                case OUTPUT:
                    ora.add((OutputRenderingAction) ra);
                    break;
                default:
                    break;
                }
            }
        }

        for (InputRenderingAction ir : ira) {

            List<InputFragmentRenderer> renderers = collected.stream()
                .map(r -> r.get(ir))
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparingInt(InputFragmentRenderer::order))
                .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(renderers)) {
                inputRenderers.put(ir, renderers);
            }
        }

        for (OutputRenderingAction or : ora) {

            List<OutputFragmentRenderer> renderers = collected.stream()
                .map(r -> r.get(or))
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparingInt(OutputFragmentRenderer::order)).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(renderers)) {
                outputRenderers.put(or, renderers);
            }
        }

        for (OutputRenderingAction or : ora) {

            List<ErrorOutputFragmentRenderer> renderers = collected.stream()
                    .map(r -> r.onError(or))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(renderers)) {
                errorOutputRenderers.put(or, renderers);
            }
        }
    }

    @Override
    public List<InputFragmentRenderer> inputFragmentRenderers() {
        return inputRenderers.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public List<OutputFragmentRenderer> outputFragmentRenderers() {
        return outputRenderers.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public List<ErrorOutputFragmentRenderer> errorFragmentRenderers() {
        return errorOutputRenderers.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }
}
