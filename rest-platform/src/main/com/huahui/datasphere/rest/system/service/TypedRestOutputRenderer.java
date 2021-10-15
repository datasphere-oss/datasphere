package com.huahui.datasphere.rest.system.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huahui.datasphere.rest.system.ro.RestOutputSink;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidata.mdm.system.dto.OutputContainer;
import org.unidata.mdm.system.type.rendering.FieldDef;
import org.unidata.mdm.system.type.rendering.FragmentDef;
import org.unidata.mdm.system.type.rendering.OutputFragmentRenderer;
import org.unidata.mdm.system.type.rendering.OutputSink;
import org.unidata.mdm.system.util.JsonUtils;

/**
 * Typed output render
 *
 * @author Alexandr Serov
 * @since 05.10.2020
 **/
public abstract class TypedRestOutputRenderer<T extends RestOutputSink, C extends OutputContainer> implements OutputFragmentRenderer {

    private static final int DEFAULT_ORDER = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(TypedRestOutputRenderer.class);

    /**
     * Type of processed object
     */
    private final Class<T> type;
    /**
     * Type of processed container
     */
    private final Class<C> containerType;


    /**
     * Fields to publish in schema
     */
    private final List<FragmentDef> fragments;


    /**
     * Rendering order
     */
    private final int order;

    public TypedRestOutputRenderer(Class<T> type, Class<C> containerType, List<FragmentDef> fragments, int order) {
        this.type = type;
        this.containerType = containerType;
        this.order = order;
        this.fragments = ObjectUtils.defaultIfNull(fragments, Collections.emptyList());
    }

    public TypedRestOutputRenderer(Class<T> type, Class<C> containerType, List<FieldDef> fields) {
        this(type, containerType, Collections.singletonList(FragmentDef.fragmentDef(type, fields)), DEFAULT_ORDER);
    }


    protected abstract Map<String, Object> renderFragmentFields(FragmentDef fragmentDef, C container);

    @Override
    public void render(String version, OutputContainer container, OutputSink sink) {
        Objects.requireNonNull(container, "OutputContainer can't be null");
        Objects.requireNonNull(sink, "OutputSink can't be null");
        Class<?> containerClass = container.getClass();
        if (containerType.isAssignableFrom(containerClass)) {
            Class<?> sinkClass = sink.getClass();
            if (type.isAssignableFrom(sinkClass)) {
                List<FragmentDef> versionFragments = findFragmentsByVersion(version);
                if (versionFragments.isEmpty()) {
                    LOGGER.debug("Skip unsupported version (empty version fragments): {}", version);
                } else {
                    ObjectMapper objectMapper = Objects.requireNonNull(JsonUtils.getMapper(), "ObjectMapper not preset in JsonUtils");
                    T typeSink = type.cast(sink);
                    C typedContainer = containerType.cast(container);
                    Map<String, Map<String, Object>> fragments = new HashMap<>();
                    for (FragmentDef fragment : versionFragments) {
                        Map<String, Object> renderedFields = renderFragmentFields(fragment, typedContainer);
                        if (renderedFields != null && !renderedFields.isEmpty()) {
                            Map<String, Object> fragmentFields = fragments.computeIfAbsent(fragment.getObjectField(), (fn) -> new HashMap<>());
                            fragmentFields.putAll(renderedFields);
                        }
                    }
                    fragments.forEach((fieldName, fieldValue) -> typeSink.setAny(fieldName, objectMapper.valueToTree(fieldValue)));
                }
            } else {
                LOGGER.debug("Skip unexpected sink type {}. Expected type {}", sinkClass, type);
            }
        } else {
            LOGGER.debug("Skip unexpected container type {}. Expected type {}", containerClass, type);
        }
    }

    private List<FragmentDef> findFragmentsByVersion(String version) {
        return fragments.stream().filter(f -> Objects.equals(version, f.getVersion())).collect(Collectors.toList());
    }

    /**
     * @return rendered fragments
     */
    @Override
    public List<FragmentDef> fragmentFields() {
        return fragments;
    }


    /**
     * @return order
     */
    @Override
    public int order() {
        return order;
    }
}
