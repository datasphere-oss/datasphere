package com.huahui.datasphere.rest.system.service.openapi;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;

/**
 * Converter Keeper
 * Required to access the converters from the schema reader (open api)
 *
 * @author Alexandr Serov
 * @since 29.09.2020
 **/
public class VersionedModelConverters {

    private static final VersionedModelConverters INSTANCE = new VersionedModelConverters();

    private final List<ModelConverter> converters;

    private VersionedModelConverters() {
        converters = new CopyOnWriteArrayList<>();
        converters.add(new ModelResolver(Json.mapper()));
    }

    public static VersionedModelConverters getInstance() {
        return INSTANCE;
    }

    public void addConverter(ModelConverter converter) {
        converters.add(0, converter);
    }

    public void removeConverter(ModelConverter converter) {
        converters.remove(converter);
    }

    public List<ModelConverter> getConverters() {
        return converters;
    }
}
