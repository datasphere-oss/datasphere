package com.huahui.datasphere.mdm.rest.system.service.openapi;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.huahui.datasphere.mdm.rest.system.annotations.ApiVersion;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ParameterProcessor;
import io.swagger.v3.core.util.PathUtils;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.jaxrs2.OperationParser;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.ResolvedParameter;
import io.swagger.v3.jaxrs2.SecurityParser;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.v3.jaxrs2.util.ReaderUtils;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Scheme reader.
 * Unfortunately, the original reader passes only the type to the converters,
 * which makes it impossible to dynamically form the description of objects based on the parameters of a method or class.
 * This class overrides some of the methods of the original reader to pass this information to the handler
 *
 * @author theseusyang
 * @since 29.09.2020
 **/
public class VersionedOpenApiReader extends Reader {

    public static final String DEFAULT_DESCRIPTION = "default response";

    private static final String GET_METHOD = "get";
    private static final String POST_METHOD = "post";
    private static final String PUT_METHOD = "put";
    private static final String DELETE_METHOD = "delete";
    private static final String PATCH_METHOD = "patch";
    private static final String TRACE_METHOD = "trace";
    private static final String HEAD_METHOD = "head";
    private static final String OPTIONS_METHOD = "options";

    private final Set<Tag> openApiTags = new HashSet<>();
    private final Paths paths = new Paths();

    @Override
    public OpenAPI read(Class<?> cls, String parentPath, String parentMethod, boolean isSubresource, RequestBody parentRequestBody,
                        ApiResponses parentResponses, Set<String> parentTags, List<Parameter> parentParameters, Set<Class<?>> scannedResources) {
        final OpenAPI openAPI = Objects.requireNonNull(getOpenAPI(), "Reader not configured (getOpenAPI() == null)");
        final Components components = Objects.requireNonNull(openAPI.getComponents(), "Reader not configured (getOpenAPI.components() == null)");
        if (!isHiddenElement(cls)) {
            final var apiPath = ReflectionUtils.getAnnotation(cls, javax.ws.rs.Path.class);
            final var classConsumes = ReflectionUtils.getAnnotation(cls, javax.ws.rs.Consumes.class);
            final var classProduces = ReflectionUtils.getAnnotation(cls, javax.ws.rs.Produces.class);
            io.swagger.v3.oas.annotations.tags.Tag[] apiTags = ReflectionUtils.getRepeatableAnnotationsArray(cls, io.swagger.v3.oas.annotations.tags.Tag.class);
            setupApiSecuritySchemeIfPreset(openAPI, cls);
            setupOpenAPIDefinitionIfPreset(openAPI, cls);
            var apiSecurityRequirements = ReflectionUtils.getRepeatableAnnotations(cls, io.swagger.v3.oas.annotations.security.SecurityRequirement.class);
            List<SecurityRequirement> classSecurityRequirements = Optional.ofNullable(ReflectionUtils.getRepeatableAnnotations(cls, io.swagger.v3.oas.annotations.security.SecurityRequirement.class))
                .flatMap(sr -> SecurityParser.getSecurityRequirements(apiSecurityRequirements.toArray(new io.swagger.v3.oas.annotations.security.SecurityRequirement[0])))
                .orElseGet(ArrayList::new);
            final List<Parameter> globalParameters = new ArrayList<>();
            // look for constructor-level annotated properties
            globalParameters.addAll(ReaderUtils.collectConstructorParameters(cls, components, classConsumes, null));
            // look for field-level annotated properties
            globalParameters.addAll(ReaderUtils.collectFieldParameters(cls, components, classConsumes, null));

            String classApiVersion = resolveApiVersion(cls);
            Method[] methods = cls.getMethods();
            JavaType classType = TypeFactory.defaultInstance().constructType(cls);
            BeanDescription bd = Json.mapper().getSerializationConfig().introspect(classType);
            final Set<String> classTags = new LinkedHashSet<>();
            for (Method method : methods) {
                String httpMethod = ReaderUtils.extractOperationMethod(method, OpenAPIExtensions.chain());
                if (httpMethod == null && isSubresource) {
                    httpMethod = parentMethod;
                }
                final Class<?> subResource = getSubResourceWithJaxRsSubresourceLocatorSpecs(method);
                if (!isOperationHidden(method) && !ReflectionUtils.isOverriddenMethod(method, cls)) { // && httpMethod != null
                    javax.ws.rs.Path methodPath = ReflectionUtils.getAnnotation(method, javax.ws.rs.Path.class);
                    String operationPath = ReaderUtils.getPath(apiPath, methodPath, parentPath, false);
                    String parsedOperationPath = operationPath = PathUtils.parsePath(operationPath, new LinkedHashMap<>());
                    if (parsedOperationPath != null && !ReaderUtils.isIgnored(operationPath, config)) {
                        final MethodOperationContext ctx = new MethodOperationContext(classApiVersion, method,
                            bd.findMethod(method.getName(), method.getParameterTypes()), classConsumes, classProduces, classSecurityRequirements, globalParameters);
                        Operation operation = createOperationFromMethod(ctx);
                        List<Parameter> operationParameters = new ArrayList<>();
                        List<Parameter> formParameters = new ArrayList<>();
                        Annotation[][] paramAnnotations = ReflectionUtils.getParameterAnnotations(method);
                        Type[] types = ctx.resolveParameterTypes(cls);
                        for (int i = 0, n = types.length; i < n; i++) {
                            Type type = types[i];
                            var paramAnnotation = AnnotationsUtils.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class, paramAnnotations[i]);
                            Type paramType = ParameterProcessor.getParameterType(paramAnnotation, true);
                            if (!(paramType instanceof Class)) {
                                paramType = type;
                            }
                            ResolvedParameter resolvedParameter = getParameters(paramType, Arrays.asList(paramAnnotations[i]), operation,
                                ctx.classConsumes, ctx.consumes, ctx.jsonViewAnnotation);
                            operationParameters.addAll(resolvedParameter.parameters);
//                                // collect params to use together as request Body
                            formParameters.addAll(resolvedParameter.formParameters);
                            if (resolvedParameter.requestBody != null) {
                                processRequestBody(resolvedParameter.requestBody, operation, ctx.consumes,
                                    ctx.classConsumes, operationParameters, paramAnnotations[i], type, ctx.jsonViewAnnotationForRequestBody);
                            }
                        }
                        // 253
                        // if we have form parameters, need to merge them into single schema and use as request body..
                        if (!formParameters.isEmpty()) {
                            Schema<?> mergedSchema = new ObjectSchema();
                            for (Parameter formParam : formParameters) {
                                mergedSchema.addProperties(formParam.getName(), formParam.getSchema());
                            }
                            Parameter merged = new Parameter().schema(mergedSchema);
                            processRequestBody(merged, operation, ctx.consumes, classConsumes, operationParameters,
                                new Annotation[0], null, ctx.jsonViewAnnotationForRequestBody);

                        }
                        for (Parameter operationParameter : operationParameters) {
                            operation.addParametersItem(operationParameter);
                        }
                        // if subresource, merge parent parameters
                        if (parentParameters != null) {
                            for (Parameter parentParameter : parentParameters) {
                                operation.addParametersItem(parentParameter);
                            }
                        }
                        // don't proceed with root resource operation, as it's handled by subresource
                        if (subResource != null && !scannedResources.contains(subResource)) {
                            scannedResources.add(subResource);
                            read(subResource, operationPath, httpMethod, true, operation.getRequestBody(), operation.getResponses(), classTags, operation.getParameters(), scannedResources);
                            scannedResources.remove(subResource);
                        } else {
                            final Iterator<OpenAPIExtension> chain = OpenAPIExtensions.chain();
                            if (chain.hasNext()) {
                                final OpenAPIExtension extension = chain.next();
                                extension.decorateOperation(operation, method, chain);
                            }
                            PathItem pathItemObject;
                            if (openAPI.getPaths() != null && openAPI.getPaths().get(operationPath) != null) {
                                pathItemObject = openAPI.getPaths().get(operationPath);
                            } else {
                                pathItemObject = new PathItem();
                            }
                            if (StringUtils.isNotBlank(httpMethod)) {
                                setPathItemOperation(pathItemObject, httpMethod, operation);
                                paths.addPathItem(operationPath, pathItemObject);
                                if (openAPI.getPaths() != null) {
                                    paths.putAll(openAPI.getPaths());
                                }
                                openAPI.setPaths(paths);
                            }
                        }
                    }
                }
            }
            // add tags from class to definition tags
            AnnotationsUtils.getTags(apiTags, true).ifPresent(openApiTags::addAll);
            if (!openApiTags.isEmpty()) {
                Set<Tag> tagsSet = new LinkedHashSet<>();
                if (openAPI.getTags() != null) {
                    for (Tag tag : openAPI.getTags()) {
                        if (tagsSet.stream().noneMatch(t -> t.getName().equals(tag.getName()))) {
                            tagsSet.add(tag);
                        }
                    }
                }
                for (Tag tag : openApiTags) {
                    if (tagsSet.stream().noneMatch(t -> t.getName().equals(tag.getName()))) {
                        tagsSet.add(tag);
                    }
                }
                openAPI.setTags(new ArrayList<>(tagsSet));
            }
            // if no components object is defined in openApi instance passed by client, set openAPI.components to resolved components (if not empty)
            if (!isEmptyComponents(components) && openAPI.getComponents() == null) {
                openAPI.setComponents(components);
            }
        }
        return openAPI;
    }

    /**
     * Copied from parent class
     * {@inheritDoc}
     */
    private Operation createOperationFromMethod(MethodOperationContext ctx) {
        OpenAPI openAPI = Objects.requireNonNull(getOpenAPI(), "OpenAPI == null");
        Components components = Objects.requireNonNull(openAPI.getComponents(), "OpenAPI.components == null");
        final Operation operation = new Operation();
        ctx.classSecurityRequirements.forEach(operation::addSecurityItem);
        Optional.ofNullable(ctx.apiSecurity)
            .flatMap(s -> SecurityParser.getSecurityRequirements(s.toArray(new io.swagger.v3.oas.annotations.security.SecurityRequirement[0])))
            .ifPresent(ro -> ro.stream().filter(r -> operation.getSecurity() == null || !operation.getSecurity().contains(r)).forEach(operation::addSecurityItem));
        Optional.ofNullable(ReflectionUtils.getAnnotation(ctx.method, ExternalDocumentation.class))
            .flatMap(AnnotationsUtils::getExternalDocumentation)
            .ifPresent(operation::setExternalDocs);
        ApiResponses apiResponses = createApiResponses(ctx);
        if (!apiResponses.isEmpty()) {
            operation.setResponses(apiResponses);
        }

        if (ctx.apiServers != null) {
            AnnotationsUtils.getServers(ctx.apiServers.toArray(new Server[0])).ifPresent(servers -> servers.forEach(operation::addServersItem));
        }
        // callbacks
        Map<String, Callback> callbacks = new LinkedHashMap<>();
        if (ctx.apiCallbacks != null) {
            for (io.swagger.v3.oas.annotations.callbacks.Callback methodCallback : ctx.apiCallbacks) {
                Map<String, Callback> currentCallbacks = getCallbacks(methodCallback, ctx);
                callbacks.putAll(currentCallbacks);
            }
        }
        if (callbacks.size() > 0) {
            operation.setCallbacks(callbacks);
        }
        // method tags
        if (ctx.apiTags != null) {
            ctx.apiTags.stream().filter(t -> operation.getTags() == null || (operation.getTags() != null && !operation.getTags().contains(t.name())))
                .map(io.swagger.v3.oas.annotations.tags.Tag::name)
                .forEach(operation::addTagsItem);
            AnnotationsUtils.getTags(ctx.apiTags.toArray(new io.swagger.v3.oas.annotations.tags.Tag[0]), true).ifPresent(openApiTags::addAll);
        }

        if (ctx.apiOperation != null) {
            setOperationObjectFromApiOperationAnnotation(operation, ctx);
        }

        // parameters
        if (ctx.globalParameters != null) {
            for (Parameter globalParameter : ctx.globalParameters) {
                operation.addParametersItem(globalParameter);
            }
        }

        if (ctx.apiParameters != null) {
            getParametersListFromAnnotation(ctx.apiParameters.toArray(new io.swagger.v3.oas.annotations.Parameter[0]), ctx.classConsumes, ctx.consumes, operation, ctx.jsonViewAnnotation)
                .ifPresent(p -> p.forEach(operation::addParametersItem));
        }
        // RequestBody in Method
        if (ctx.apiRequestBody != null && operation.getRequestBody() == null) {
            OperationParser.getRequestBody(ctx.apiRequestBody, ctx.classConsumes, ctx.consumes, components, ctx.jsonViewAnnotation)
                .ifPresent(operation::setRequestBody);
        }
        // operation id
        if (StringUtils.isBlank(operation.getOperationId())) {
            operation.setOperationId(getOperationId(ctx.method.getName()));
        }
        final Class<?> subResource = getSubResourceWithJaxRsSubresourceLocatorSpecs(ctx.method);
        if (!shouldIgnoreClass(ctx.returnType.getTypeName()) && !ctx.method.getGenericReturnType().equals(subResource)) {
            VersionedModelConverters converters = VersionedModelConverters.getInstance();
            VersionedConverterContext converterContext = new VersionedConverterContext(ctx.classApiVersion, ctx.methodApiVersion, converters.getConverters());
            ResolvedSchema resolvedSchema = new ResolvedSchema();
            resolvedSchema.schema = converterContext.resolve(new AnnotatedType(ctx.returnType)
                .resolveAsRef(true)
                .jsonViewAnnotation(ctx.jsonViewAnnotation));
            resolvedSchema.referencedSchemas = converterContext.getDefinedModels();
            if (resolvedSchema.schema != null) {
                Schema<?> returnTypeSchema = resolvedSchema.schema;
                Content content = new Content();
                MediaType mediaType = new MediaType().schema(returnTypeSchema);
                AnnotationsUtils.applyTypes(ctx.classProducesAsArray(), ctx.producesAsArray(), content, mediaType);
                if (operation.getResponses() == null) {
                    operation.responses(new ApiResponses()._default(new ApiResponse().description(DEFAULT_DESCRIPTION).content(content)));
                }
                ApiResponse defaultApiResponse = operation.getResponses().getDefault();
                if (defaultApiResponse != null && StringUtils.isBlank(defaultApiResponse.get$ref())) {
                    if (defaultApiResponse.getContent() == null) {
                        defaultApiResponse.content(content);
                    } else {
                        defaultApiResponse.getContent().values().stream()
                            .filter(it -> Objects.isNull(it.getSchema()))
                            .forEach(mt -> mt.setSchema(returnTypeSchema));
                    }
                    if (resolvedSchema.referencedSchemas != null) {
                        resolvedSchema.referencedSchemas.forEach(components::addSchemas);
                    }
                }
            }
        }
        return operation;
    }

    private ApiResponses createApiResponses(MethodOperationContext ctx) {
        var apiResponses = ReflectionUtils.getRepeatableAnnotations(ctx.method, io.swagger.v3.oas.annotations.responses.ApiResponse.class);
        ApiResponses result = new ApiResponses();
        OpenAPI openAPI = getOpenAPI();
        Components components = openAPI.getComponents();
        if (apiResponses != null && !apiResponses.isEmpty()) {
            for (var resp : apiResponses) {
                String ref = resp.ref();
                ApiResponse apiResponseObject = new ApiResponse();
                String responseCode = resp.responseCode();
                if (StringUtils.isNotBlank(ref)) {
                    apiResponseObject.$ref(ref);
                    if (StringUtils.isNotBlank(responseCode)) {
                        result.addApiResponse(responseCode, apiResponseObject);
                    } else {
                        result._default(apiResponseObject);
                    }
                } else {
                    apiResponseObject.setDescription(StringUtils.trimToNull(resp.description()));
                    Map<String, Object> extensions = AnnotationsUtils.getExtensions(resp.extensions());
                    extensions.forEach(apiResponseObject::addExtension);
                    AnnotationsUtils.getContent(resp.content(), ctx.classProducesAsArray(), ctx.producesAsArray(), null, components, ctx.jsonViewAnnotation)
                        .ifPresent(apiResponseObject::content);
                    AnnotationsUtils.getHeaders(resp.headers(), ctx.jsonViewAnnotation).ifPresent(apiResponseObject::headers);
                    if (StringUtils.isNotBlank(apiResponseObject.getDescription()) || apiResponseObject.getContent() != null || apiResponseObject.getHeaders() != null) {
                        Map<String, Link> links = AnnotationsUtils.getLinks(resp.links());
                        if (!links.isEmpty()) {
                            apiResponseObject.setLinks(links);
                        }
                        if (StringUtils.isNotBlank(responseCode)) {
                            result.addApiResponse(responseCode, apiResponseObject);
                        } else {
                            result._default(apiResponseObject);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Copied from parent class
     * {@inheritDoc}
     */
    private boolean shouldIgnoreClass(String className) {
        if (StringUtils.isBlank(className)) {
            return true;
        }
        boolean ignore;
        String rawClassName = className;
        if (rawClassName.startsWith("[")) { // jackson JavaType
            rawClassName = className.replace("[simple type, class ", "");
            rawClassName = rawClassName.substring(0, rawClassName.length() - 1);
        }
        ignore = rawClassName.startsWith("javax.ws.rs.");
        ignore = ignore || rawClassName.equalsIgnoreCase("void");
        ignore = ignore || ModelConverters.getInstance().isRegisteredAsSkippedClass(rawClassName);
        return ignore;
    }

    /**
     * Copied from parent class
     * {@inheritDoc}
     */
    private void setPathItemOperation(PathItem pathItemObject, String method, Operation operation) {
        switch (method) {
            case POST_METHOD:
                pathItemObject.post(operation);
                break;
            case GET_METHOD:
                pathItemObject.get(operation);
                break;
            case DELETE_METHOD:
                pathItemObject.delete(operation);
                break;
            case PUT_METHOD:
                pathItemObject.put(operation);
                break;
            case PATCH_METHOD:
                pathItemObject.patch(operation);
                break;
            case TRACE_METHOD:
                pathItemObject.trace(operation);
                break;
            case HEAD_METHOD:
                pathItemObject.head(operation);
                break;
            case OPTIONS_METHOD:
                pathItemObject.options(operation);
                break;
        }
    }

    /**
     * Copied from parent class
     * {@inheritDoc}
     */
    private boolean isEmptyComponents(Components components) {
        if (components == null) {
            return true;
        }
        if (components.getSchemas() != null && components.getSchemas().size() > 0) {
            return false;
        }
        if (components.getSecuritySchemes() != null && components.getSecuritySchemes().size() > 0) {
            return false;
        }
        if (components.getCallbacks() != null && components.getCallbacks().size() > 0) {
            return false;
        }
        if (components.getExamples() != null && components.getExamples().size() > 0) {
            return false;
        }
        if (components.getExtensions() != null && components.getExtensions().size() > 0) {
            return false;
        }
        if (components.getHeaders() != null && components.getHeaders().size() > 0) {
            return false;
        }
        if (components.getLinks() != null && components.getLinks().size() > 0) {
            return false;
        }
        if (components.getParameters() != null && components.getParameters().size() > 0) {
            return false;
        }
        if (components.getRequestBodies() != null && components.getRequestBodies().size() > 0) {
            return false;
        }
        return components.getResponses() == null || components.getResponses().size() <= 0;
    }

    /**
     * Copied from parent class
     * {@inheritDoc}
     */
    private void setOperationObjectFromApiOperationAnnotation(Operation operation, MethodOperationContext ctx) {
        OpenAPI openAPI = Objects.requireNonNull(getOpenAPI(), "OpenAPI == null");
        Components components = Objects.requireNonNull(openAPI.getComponents(), "OpenAPI.components == null");
        operation.setSummary(ctx.apiOperation.summary());
        operation.setDescription(ctx.apiOperation.description());
        operation.setOperationId(ctx.apiOperation.operationId());
        operation.setDeprecated(ctx.apiOperation.deprecated());
        ReaderUtils.getStringListFromStringArray(ctx.apiOperation.tags())
            .ifPresent(tags -> tags.stream()
                .filter(t -> operation.getTags() == null || (operation.getTags() != null && !operation.getTags().contains(t)))
                .forEach(operation::addTagsItem));

        if (operation.getExternalDocs() == null) { // if not set in root annotation
            AnnotationsUtils.getExternalDocumentation(ctx.apiOperation.externalDocs()).ifPresent(operation::setExternalDocs);
        }

        OperationParser.getApiResponses(ctx.apiOperation.responses(), ctx.classProduces, ctx.produces, components, ctx.jsonViewAnnotation).ifPresent(responses -> {
            if (operation.getResponses() == null) {
                operation.setResponses(responses);
            } else {
                responses.forEach(operation.getResponses()::addApiResponse);
            }
        });
        AnnotationsUtils.getServers(ctx.apiOperation.servers()).ifPresent(servers -> servers.forEach(operation::addServersItem));

        getParametersListFromAnnotation(ctx.apiOperation.parameters(), ctx.classConsumes, ctx.consumes, operation, ctx.jsonViewAnnotation)
            .ifPresent(p -> p.forEach(operation::addParametersItem));

        // security
        Optional<List<SecurityRequirement>> requirementsObject = SecurityParser.getSecurityRequirements(ctx.apiOperation.security());
        requirementsObject.ifPresent(securityRequirements -> securityRequirements.stream()
            .filter(r -> operation.getSecurity() == null || !operation.getSecurity().contains(r))
            .forEach(operation::addSecurityItem));

        // RequestBody in Operation
        if (operation.getRequestBody() == null) {
            OperationParser.getRequestBody(ctx.apiOperation.requestBody(), ctx.classConsumes, ctx.consumes, components, ctx.jsonViewAnnotation)
                .ifPresent(operation::setRequestBody);
        }

        // Extensions in Operation
        if (ctx.apiOperation.extensions().length > 0) {
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(ctx.apiOperation.extensions());
            for (String ext : extensions.keySet()) {
                operation.addExtension(ext, extensions.get(ext));
            }
        }
    }

    /**
     * Copied from parent class
     * {@inheritDoc}
     */
    private Map<String, Callback> getCallbacks(io.swagger.v3.oas.annotations.callbacks.Callback apiCallback, MethodOperationContext ctx) {
        Map<String, Callback> callbackMap = new HashMap<>();
        if (apiCallback == null) {
            return callbackMap;
        }

        Callback callbackObject = new Callback();
        if (StringUtils.isNotBlank(apiCallback.ref())) {
            callbackObject.set$ref(apiCallback.ref());
            callbackMap.put(apiCallback.name(), callbackObject);
            return callbackMap;
        }
        PathItem pathItemObject = new PathItem();
        for (io.swagger.v3.oas.annotations.Operation callbackOperation : apiCallback.operation()) {
            Operation callbackNewOperation = new Operation();
            setOperationObjectFromApiOperationAnnotation(callbackNewOperation, ctx);
            setPathItemOperation(pathItemObject, callbackOperation.method(), callbackNewOperation);
        }
        callbackObject.addPathItem(apiCallback.callbackUrlExpression(), pathItemObject);
        callbackMap.put(apiCallback.name(), callbackObject);
        return callbackMap;
    }

    private void setupApiSecuritySchemeIfPreset(OpenAPI openAPI, Class<?> cls) {
        var apiSecurityScheme = ReflectionUtils.getRepeatableAnnotations(cls, io.swagger.v3.oas.annotations.security.SecurityScheme.class);
        if (apiSecurityScheme != null && !apiSecurityScheme.isEmpty()) {
            apiSecurityScheme.stream()
                .map(SecurityParser::getSecurityScheme)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(sec -> StringUtils.isNotBlank(sec.key))
                .forEach(sec -> {
                    var components = openAPI.getComponents();
                    Map<String, SecurityScheme> securitySchemeMap = new HashMap<>();
                    securitySchemeMap.put(sec.key, sec.securityScheme);
                    Map<String, SecurityScheme> schemes = components.getSecuritySchemes();
                    if (schemes != null && !schemes.isEmpty()) {
                        schemes.putAll(securitySchemeMap);
                    } else {
                        components.setSecuritySchemes(securitySchemeMap);
                    }
                });
        }
    }

    private void setupOpenAPIDefinitionIfPreset(OpenAPI openAPI, Class<?> cls) {
        OpenAPIDefinition openAPIDefinition = ReflectionUtils.getAnnotation(cls, OpenAPIDefinition.class);
        if (openAPIDefinition != null) {
            // info
            AnnotationsUtils.getInfo(openAPIDefinition.info()).ifPresent(openAPI::setInfo);
            // OpenApiDefinition security requirements
            SecurityParser.getSecurityRequirements(openAPIDefinition.security()).ifPresent(openAPI::setSecurity);
            // OpenApiDefinition external docs
            AnnotationsUtils.getExternalDocumentation(openAPIDefinition.externalDocs()).ifPresent(openAPI::setExternalDocs);
            // OpenApiDefinition tags
            AnnotationsUtils.getTags(openAPIDefinition.tags(), false).ifPresent(openApiTags::addAll);
            // OpenApiDefinition servers
            AnnotationsUtils.getServers(openAPIDefinition.servers()).ifPresent(openAPI::setServers);
            // OpenApiDefinition extensions
            if (openAPIDefinition.extensions().length > 0) {
                openAPI.setExtensions(AnnotationsUtils.getExtensions(openAPIDefinition.extensions()));
            }
        }
    }


    private boolean isHiddenElement(AnnotatedElement element) {
        return element.getAnnotation(Hidden.class) != null;
    }

    /**
     * Determine the version of the api item
     *
     * @param element versioned element
     * @return element version or latest
     */
    private String resolveApiVersion(AnnotatedElement element) {
        ApiVersion apiVersion = element.getAnnotation(ApiVersion.class);
        String result = null;
        if (apiVersion != null) {
            result = apiVersion.value();
        }
        return result;
    }

    private class MethodOperationContext {
        private final String classApiVersion;
        private final String methodApiVersion;
        private final Method method;
        private final AnnotatedMethod annotatedMethod;
        private final Consumes consumes;
        private final Produces produces;
        private final Consumes classConsumes;
        private final Produces classProduces;
        private final io.swagger.v3.oas.annotations.Operation apiOperation;
        private final io.swagger.v3.oas.annotations.parameters.RequestBody apiRequestBody;
        private final List<io.swagger.v3.oas.annotations.Parameter> apiParameters;
        private final List<Server> apiServers;
        private final List<SecurityRequirement> classSecurityRequirements;
        private final List<io.swagger.v3.oas.annotations.security.SecurityRequirement> apiSecurity;
        private final List<io.swagger.v3.oas.annotations.tags.Tag> apiTags;
        private final List<Parameter> globalParameters;
        private final List<io.swagger.v3.oas.annotations.callbacks.Callback> apiCallbacks;
        private JsonView jsonViewAnnotation;
        private JsonView jsonViewAnnotationForRequestBody;
        private Type returnType;

        private MethodOperationContext(String classApiVersion, Method method, AnnotatedMethod annotatedMethod,
                                       Consumes classConsumes, Produces classProduces, List<SecurityRequirement> classSecurityRequirements, List<Parameter> globalParameters) {
            this.classApiVersion = classApiVersion;
            this.methodApiVersion = resolveApiVersion(method);
            this.method = method;
            this.annotatedMethod = annotatedMethod;
            this.classConsumes = classConsumes;
            this.classProduces = classProduces;
            this.classSecurityRequirements = classSecurityRequirements;
            this.apiSecurity = ReflectionUtils.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.security.SecurityRequirement.class);
            apiOperation = ReflectionUtils.getAnnotation(method, io.swagger.v3.oas.annotations.Operation.class);
            apiRequestBody = ReflectionUtils.getAnnotation(method, io.swagger.v3.oas.annotations.parameters.RequestBody.class);
            apiParameters = ReflectionUtils.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.Parameter.class);
            consumes = ReflectionUtils.getAnnotation(method, javax.ws.rs.Consumes.class);
            produces = ReflectionUtils.getAnnotation(method, javax.ws.rs.Produces.class);
            apiServers = ReflectionUtils.getRepeatableAnnotations(method, Server.class);
            apiTags = ReflectionUtils.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.tags.Tag.class);
            apiCallbacks = ReflectionUtils.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.callbacks.Callback.class);
            returnType = method.getGenericReturnType();
            this.globalParameters = globalParameters;
            if (annotatedMethod != null && annotatedMethod.getType() != null) {
                returnType = annotatedMethod.getType();
            }
            if (apiOperation != null && !apiOperation.ignoreJsonView()) {
                jsonViewAnnotation = ReflectionUtils.getAnnotation(method, JsonView.class);
                jsonViewAnnotationForRequestBody = Arrays.stream(ReflectionUtils.getParameterAnnotations(method))
                    .filter(an -> Arrays.stream(an).anyMatch(a -> io.swagger.v3.oas.annotations.parameters.RequestBody.class.equals(a.annotationType())))
                    .flatMap(Arrays::stream)
                    .filter(an -> JsonView.class.equals(an.annotationType()))
                    .map(an -> (JsonView) an)
                    .findFirst()
                    .orElse(jsonViewAnnotation);
            }
        }

        private Type[] resolveParameterTypes(Class<?> cls) {
            Type[] types;
            if (annotatedMethod != null) {
                int paramCount = annotatedMethod.getParameterCount();
                types = new Type[paramCount];
                for (int i = 0; i < paramCount; i++) {
                    AnnotatedParameter param = annotatedMethod.getParameter(i);
                    types[i] = TypeFactory.defaultInstance().constructType(param.getParameterType(), cls);
                }
            } else {
                types = Arrays.stream(method.getGenericParameterTypes())
                    .map(t -> TypeFactory.defaultInstance().constructType(t, cls)).toArray(Type[]::new);
            }
            return types;
        }

        private String[] producesAsArray() {
            return produces == null ? ArrayUtils.EMPTY_STRING_ARRAY : produces.value();
        }

        private String[] classProducesAsArray() {
            return classProduces == null ? ArrayUtils.EMPTY_STRING_ARRAY : classProduces.value();
        }

    }

}
