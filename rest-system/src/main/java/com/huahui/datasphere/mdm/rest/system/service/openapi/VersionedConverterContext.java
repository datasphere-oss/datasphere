package com.huahui.datasphere.mdm.rest.system.service.openapi;

import java.util.List;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContextImpl;

/**
 * Versioned schema processing context.
 * Unlike the original, it has information about the api version for which the scheme is being formed
 *
 * @author theseusyang
 * @since 29.09.2020
 **/
public class VersionedConverterContext extends ModelConverterContextImpl {

    private final String classApiVersion;
    private final String methodApiVersion;

    public VersionedConverterContext(String classApiVersion, String methodApiVersion, List<ModelConverter> converters) {
        super(converters);
        this.classApiVersion = classApiVersion;
        this.methodApiVersion = methodApiVersion;
    }

    public VersionedConverterContext(String classApiVersion, String methodApiVersion, String apiVersion, ModelConverter converter) {
        super(converter);
        this.classApiVersion = classApiVersion;
        this.methodApiVersion = methodApiVersion;
    }

    public String getClassApiVersion() {
        return classApiVersion;
    }

    public String getMethodApiVersion() {
        return methodApiVersion;
    }
}
