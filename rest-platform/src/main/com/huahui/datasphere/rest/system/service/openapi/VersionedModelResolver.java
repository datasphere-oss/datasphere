package com.huahui.datasphere.rest.system.service.openapi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.MoreObjects;
import com.huahui.datasphere.rest.system.ro.RestInputSource;
import com.huahui.datasphere.rest.system.ro.RestOutputSink;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unidata.mdm.system.service.AfterPlatformStartup;
import org.unidata.mdm.system.service.RenderingService;
import org.unidata.mdm.system.type.rendering.AnyType;
import org.unidata.mdm.system.type.rendering.FieldDef;
import org.unidata.mdm.system.type.rendering.FragmentDef;
import org.unidata.mdm.system.type.rendering.InputFragmentRenderer;
import org.unidata.mdm.system.type.rendering.OutputFragmentRenderer;

import static io.swagger.v3.core.util.RefUtils.constructRef;
import static java.util.Collections.emptyList;

/**
 * Custom openapi schema resolver
 *
 * @author Alexandr Serov
 * @since 16.09.2020
 **/
@Component
public class VersionedModelResolver extends ModelResolver implements AfterPlatformStartup {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionedModelResolver.class);

    private static final String OBJECT_TYPE_NAME = "object";

    private final Map<ClassVersion, List<FragmentDef>> extensions = new HashMap<>();

    @Autowired
    private RenderingService renderingService;

    private TypeFactory typeFactory;

    protected VersionedModelResolver(@Autowired ObjectMapper mapper) {
        super(mapper);
    }

    @PostConstruct
    private void init() {
        ModelConverters.getInstance().addConverter(this);
        VersionedModelConverters.getInstance().addConverter(this);
        typeFactory = _mapper.getTypeFactory();
    }

    @Override
    public Schema<?> resolve(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> chain) {
        JavaType javaType = toJavaType(annotatedType);
        String version = resolveObjectVersion(context);
        Schema<?> result;
        Class<?> rawClass;
        if (javaType != null && isClassRenderingSupport(rawClass = javaType.getRawClass())) {
            List<FragmentDef> fragments = extensions.getOrDefault(new ClassVersion(rawClass, version), Collections.emptyList());
            ObjectSchema baseSchema = createObjectSchema(annotatedType, context);
            baseSchema.setName(javaType.getTypeName());
            for (FragmentDef it : fragments) {
                final ObjectSchema propertySchema = new ObjectSchema();
                String targetFieldName = it.getObjectField();
                List<FieldDef> fields = it.getFields();
                for (FieldDef field : fields) {
                    String fieldName = field.getFieldName();
                    JavaType fieldJavaType = toJavaType(field.getFieldType());
                    Schema<?> property = createPropertySchema(context, propertySchema, annotatedType, fieldName, fieldJavaType, null);
                    if (property != null) {
                        if (StringUtils.isNotBlank(field.getDescription())) {
                            property.setDescription(field.getDescription());
                        }
                        propertySchema.addProperties(fieldName, property);
                    }
                }
                baseSchema.addProperties(targetFieldName, propertySchema);
            }
            result = baseSchema;
        } else {
            result = super.resolve(annotatedType, context, chain);
        }
        return result;
    }

    private String resolveObjectVersion(ModelConverterContext context) {
        String result;
        if (context instanceof VersionedConverterContext) {
            VersionedConverterContext versioned = (VersionedConverterContext) context;
            result = ObjectUtils.firstNonNull(versioned.getMethodApiVersion(), versioned.getClassApiVersion(), FragmentDef.CURRENT_VERSION);
        } else {
            result = FragmentDef.CURRENT_VERSION;
        }
        return result;
    }

    /**
     * Create base object schema
     *
     * @param annotatedType type
     * @param context context
     * @return object schema
     */
    private ObjectSchema createObjectSchema(AnnotatedType annotatedType, ModelConverterContext context) {
        Type type = annotatedType.getType();
        JavaType javaType = (type instanceof JavaType) ? (JavaType) type : _mapper.constructType(type);
        SerializationConfig config = _mapper.getSerializationConfig();
        final BeanDescription beanDesc = config.introspect(javaType);
        ObjectSchema model = new ObjectSchema();
        List<BeanPropertyDefinition> properties = beanDesc.findProperties();
        for (BeanPropertyDefinition def : properties) {
            String propName = def.getName();
            AnnotatedMember member = findAnnotatedMember(javaType, def);
            if (member != null) {
                AnnotationMap annotationMap = member.getAllAnnotations();
                Annotation[] annotations = null;
                if (annotationMap != null) {
                    Iterable<Annotation> iterable = annotationMap.annotations();
                    annotations = StreamSupport.stream(iterable.spliterator(), false).toArray(Annotation[]::new);
                }
                // 590
                Schema<?> prop = createPropertySchema(context, model, annotatedType, propName, member.getType(), annotations);
                if (prop != null) {
                    model.addProperties(prop.getName(), prop);
                }
            }
        }
        return model;
    }

    /**
     * Create property schema
     *
     * @param context context
     * @param parent property owner schema
     * @param parentType property owner type
     * @param propertyName property name
     * @param type property type
     * @param annotations property annotations
     * @return property schema or null
     */
    private Schema<?> createPropertySchema(ModelConverterContext context, Schema<?> parent,
                                           AnnotatedType parentType, String propertyName, JavaType type, Annotation[] annotations) {
        AnnotatedType aType = new AnnotatedType().type(type).ctxAnnotations(annotations)
            .name(propertyName).parent(parent)
            .resolveAsRef(parentType.isResolveAsRef())
            .jsonViewAnnotation(parentType.getJsonViewAnnotation())
            .skipSchemaName(true)
            .schemaProperty(true)
            .propertyName(propertyName);
        Schema<?> property = context.resolve(aType);
        if (property != null) {
            final BeanDescription propBeanDesc = _mapper.getSerializationConfig().introspect(type);
            if (property.get$ref() == null && (!OBJECT_TYPE_NAME.equals(property.getType()) || (property instanceof MapSchema))) {
                property = cloneSchema(property);
            }
            if (property != null && !type.isContainerType()) {
                Schema<?> refSchema = new Schema<>();
                if (OBJECT_TYPE_NAME.equals(property.getType())) {
                    // create a reference for the property
                    String pName = _typeName(type, propBeanDesc);
                    if (StringUtils.isNotBlank(property.getName())) {
                        pName = property.getName();
                    }
                    if (context.getDefinedModels().containsKey(pName)) {
                        property = refSchema.$ref(constructRef(pName));
                    }
                } else if (property.get$ref() != null) {
                    property = refSchema.$ref(StringUtils.isNotEmpty(property.get$ref()) ? property.get$ref() : property.getName());
                }
            }
            if (property != null) {
                if (annotations != null) {
                    applyBeanValidatorAnnotations(property, annotations, parent);
                }
                property.setName(propertyName);
            }
        }
        return property;
    }

    /**
     * Find object property accessor
     *
     * @param type - owner type
     * @param propDef - property definition
     * @return found member or null
     */
    private AnnotatedMember findAnnotatedMember(JavaType type, BeanPropertyDefinition propDef) {
        AnnotatedMember member = propDef.getPrimaryMember();
        if (member == null) {
            final BeanDescription desc = _mapper.getDeserializationConfig().introspect(type);
            List<BeanPropertyDefinition> properties = desc.findProperties();
            Iterator<BeanPropertyDefinition> iter = properties.iterator();
            while (member == null && iter.hasNext()) {
                BeanPropertyDefinition prop = iter.next();
                String internalName = prop.getInternalName();
                if (StringUtils.equals(internalName, propDef.getInternalName())) {
                    member = prop.getPrimaryMember();
                }
            }
        }
        return member;
    }

    private Schema<?> cloneSchema(Schema<?> schema) {
        try {
            return Json.mapper().readValue(Json.pretty(schema), Schema.class);
        } catch (Exception ex) {
            throw new IllegalStateException("Can't clone schema", ex);
        }
    }

    private static boolean isClassRenderingSupport(Class<?> rawClass) {
        return rawClass != null && (RestOutputSink.class.isAssignableFrom(rawClass) || RestInputSource.class.isAssignableFrom(rawClass));
    }

    private JavaType toJavaType(AnnotatedType annotatedType) {
        JavaType result = null;
        if (annotatedType != null) {
            Type type = annotatedType.getType();
            result = (type instanceof JavaType) ? (JavaType) type : _mapper.constructType(type);
        }
        return result;
    }

    private JavaType toJavaType(AnyType anyType) {
        int paramCount = anyType.getParamCount();
        JavaType javaType;
        if (paramCount > 0) {
            JavaType[] params = new JavaType[paramCount];
            for (int i = 0; i < paramCount; i++) {
                params[i] = toJavaType(anyType.getParameter(i));
            }
            javaType = typeFactory.constructParametricType(anyType.getJavaClass(), params);
        } else {
            javaType = typeFactory.constructType(anyType.getJavaClass());
        }
        return anyType.isArray() ? typeFactory.constructArrayType(javaType) : javaType;
    }


    public void withFragmentField(FragmentDef def) {
        Objects.requireNonNull(def, "FragmentDef field can't be null");
        String version = ObjectUtils.defaultIfNull(def.getVersion(), FragmentDef.CURRENT_VERSION);
        Class<?> objectClass = def.getObjectType();
        extensions.computeIfAbsent(new ClassVersion(objectClass, version), (c) -> new ArrayList<>()).add(def);
    }

    @Override
    public void afterPlatformStartup() {
        LOGGER.info("Init render extensions....");
        if (renderingService != null) {
            List<InputFragmentRenderer> inputs = MoreObjects.firstNonNull(renderingService.inputFragmentRenderers(), emptyList());
            List<OutputFragmentRenderer> outputs = MoreObjects.firstNonNull(renderingService.outputFragmentRenderers(), emptyList());
            inputs.stream().map(InputFragmentRenderer::fragmentFields).filter(Objects::nonNull)
                .flatMap(List::stream).forEach(this::withFragmentField);
            outputs.stream().map(OutputFragmentRenderer::fragmentFields).filter(Objects::nonNull)
                .flatMap(List::stream).forEach(this::withFragmentField);
        } else {
            LOGGER.warn("Rendering service is not define");
        }
    }

    public void setRenderingService(RenderingService renderingService) {
        this.renderingService = renderingService;
    }

    private static class ClassVersion {
        private final Class<?> objectClass;
        private final String version;

        private ClassVersion(Class<?> objectClass, String version) {
            this.objectClass = objectClass;
            this.version = version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ClassVersion that = (ClassVersion) o;
            return Objects.equals(objectClass, that.objectClass) &&
                Objects.equals(version, that.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(objectClass, version);
        }
    }


}
