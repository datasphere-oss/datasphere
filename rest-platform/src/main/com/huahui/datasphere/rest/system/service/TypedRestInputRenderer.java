package com.huahui.datasphere.rest.system.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.Lists;
import com.huahui.datasphere.rest.system.ro.RestInputSource;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidata.mdm.system.context.InputCollector;
import org.unidata.mdm.system.type.rendering.AnyType;
import org.unidata.mdm.system.type.rendering.FieldDef;
import org.unidata.mdm.system.type.rendering.FragmentDef;
import org.unidata.mdm.system.type.rendering.InputFragmentRenderer;
import org.unidata.mdm.system.type.rendering.InputSource;
import org.unidata.mdm.system.type.rendering.MapInputSource;
import org.unidata.mdm.system.util.JsonUtils;

/**
 * Typed input renderer for rest requests
 * Checks input arguments and unpacks properties needed for rendering
 *
 * @author Alexandr Serov
 * @since 20.09.2020
 **/
public abstract class TypedRestInputRenderer<T extends RestInputSource> implements InputFragmentRenderer {

    private static final int DEFAULT_ORDER = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(TypedRestInputRenderer.class);

    /**
     * Type of processed object
     */
    private final Class<T> type;

    /**
     * Fields to publish in schema
     */
    private final List<FragmentDef> fragments;

    /**
     * Rendering order
     */
    private final int order;


    public TypedRestInputRenderer(Class<T> type, List<FragmentDef> fragments, int order) {
        this.type = type;
        this.order = order;
        this.fragments = ObjectUtils.defaultIfNull(fragments, Collections.emptyList());
    }

    public TypedRestInputRenderer(Class<T> type, FragmentDef fragment, FragmentDef... fragments) {
        this(type, Lists.asList(fragment, fragments), DEFAULT_ORDER);
    }

    public TypedRestInputRenderer(Class<T> type, List<FieldDef> fields) {
        this(type, Collections.singletonList(FragmentDef.fragmentDef(type, fields)), DEFAULT_ORDER);
    }


    /**
     * Render object fields
     *
     * @param collector fragment collector
     * @param source source object
     * @param fieldValues resolved fragment fields
     */
    protected abstract void renderFields(FragmentDef fragmentDef, InputCollector collector, T source, MapInputSource fieldValues);

    @Override
    public void render(String version, InputCollector collector, InputSource source) {
        Objects.requireNonNull(collector, "Collector can't be null");
        Objects.requireNonNull(source, "Source can't be null");
        Class<?> sourceClass = source.getClass();
        if (type.isAssignableFrom(sourceClass)) {
            T typed = type.cast(source);
            List<FragmentDef> versionFragments = findFragmentsByVersion(version);
            if (versionFragments.isEmpty()) {
                LOGGER.debug("Skip unsupported version (empty version fragments): {}", version);
            } else {
                Map<String, JsonNode> renderedFields = ObjectUtils.defaultIfNull(typed.getAny(), Collections.emptyMap());
                ObjectMapper objectMapper = Objects.requireNonNull(JsonUtils.getMapper(), "ObjectMapper not preset in JsonUtils");
                TypeFactory typeFactory = objectMapper.getTypeFactory();
                versionFragments.forEach(it -> {
                    LOGGER.debug("Resolve fragment fields: {}", it);
                    MapInputSource fieldValues = new MapInputSource();
                    JsonNode fragmentNode = renderedFields.get(it.getObjectField());
                    if (fragmentNode != null && JsonToken.VALUE_NULL != fragmentNode.asToken()) {
                        List<FieldDef> fields = ObjectUtils.defaultIfNull(it.getFields(), Collections.emptyList());
                        for (FieldDef field : fields) {
                            JsonNode node = fragmentNode.get(field.getFieldName());
                            if (node != null && JsonToken.VALUE_NULL != node.asToken()) {
                                JavaType javaType = toJavaType(typeFactory, field.getFieldType());
                                fieldValues.put(field.getFieldName(), objectMapper.convertValue(node, javaType));
                            }
                        }
                    }
                    renderFields(it, collector, typed, fieldValues);
                });
            }
        } else {
            LOGGER.debug("Skip unexpected source type {}. Expected type {}", sourceClass, type);
        }
    }

    private JavaType toJavaType(TypeFactory typeFactory, AnyType anyType) {
        int paramCount = anyType.getParamCount();
        JavaType javaType;
        if (paramCount > 0) {
            JavaType[] params = new JavaType[paramCount];
            for (int i = 0; i < paramCount; i++) {
                params[i] = toJavaType(typeFactory, anyType.getParameter(i));
            }
            javaType = typeFactory.constructParametricType(anyType.getJavaClass(), params);
        } else {
            javaType = typeFactory.constructType(anyType.getJavaClass());
        }
        return anyType.isArray() ? typeFactory.constructArrayType(javaType) : javaType;
    }

    private List<FragmentDef> findFragmentsByVersion(String version) {
        return fragments.stream().filter(f -> Objects.equals(version, f.getVersion())).collect(Collectors.toList());
    }

    @Override
    public List<FragmentDef> fragmentFields() {
        return fragments;
    }

    @Override
    public int order() {
        return order;
    }
}
