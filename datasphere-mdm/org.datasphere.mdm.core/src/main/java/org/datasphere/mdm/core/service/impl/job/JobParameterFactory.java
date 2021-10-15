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
package org.datasphere.mdm.core.service.impl.job;

import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.service.job.CustomJobRegistry;
import org.datasphere.mdm.core.type.job.JobDescriptor;
import org.datasphere.mdm.core.type.job.JobParameterDefinition;
import org.datasphere.mdm.core.type.job.JobParameterDescriptor;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionBooleanParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionClobParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionDateParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionInstantParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionIntegerParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionNumberParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionStringParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionTimeParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionTimestampParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapBooleanParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapClobParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapDateParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapInstantParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapIntegerParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapNumberParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapStringParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapTimeParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapTimestampParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleBooleanParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleClobParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleDateParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleInstantParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleIntegerParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleNumberParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleStringParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleTimeParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleTimestampParameterDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.datasphere.mdm.system.util.ConvertUtils;
import org.datasphere.mdm.system.util.JsonUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

/**
 * @author Mikhail Mikhailov on Jul 9, 2021
 */
public class JobParameterFactory {
    /**
     * This logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JobParameterFactory.class);
    /**
     * The job name.
     */
    private static final String PARAMETER_FIELD_JOB_NAME = "jobName";
    /**
     * The parameter name.
     */
    private static final String PARAMETER_FIELD_PARAM_NAME = "paramName";
    /**
     * The parameter value.
     */
    private static final String PARAMETER_FIELD_VALUE = "value";
    /**
     * The JR.
     */
    private final CustomJobRegistry jobRegistry;
    /**
     * Constructor.
     */
    public JobParameterFactory(CustomJobRegistry jobRegistry) {
        super();
        this.jobRegistry = jobRegistry;
    }
    /**
     * Marshals parameter in SB format (with job name). Job name is required.
     * @param jobName the job name
     * @param param the parameter
     * @return string
     */
    @Nullable
    public String toString(String jobName, JobParameterDefinition<?> param) {

        if (Objects.isNull(param) || Objects.isNull(jobName)) {
            return null;
        }

        StringWriter sw = new StringWriter();
        try (JsonGenerator jg = JsonUtils.getMapper()
                .getFactory()
                .createGenerator(sw)) {

            jg.writeStartObject();

            jg.writeStringField(PARAMETER_FIELD_JOB_NAME, jobName);
            jg.writeStringField(PARAMETER_FIELD_PARAM_NAME, param.getName());

            jg.writeFieldName(PARAMETER_FIELD_VALUE);

            Object v = null;
            if (param.isCollection()) {
                v = param.collection();
            } else if (param.isSingle()) {
                v = param.single();
            } else if (param.isMap()) {
                v = param.map();
            } else if (param.isCustom()) {
                v = param.asCustom();
            }

            jg.writeTree(JsonUtils.writeNode(v));

            jg.writeEndObject();

        } catch (Exception e) {
            LOGGER.error("Unable to marshal JPD to string from [{}].", param, e);
            return null;
        }

        return sw.toString();
    }
    /**
     * Reads and restores (unmarshals) parameter value from string in SB format.
     * @param json the JSON text
     * @return parameter or null
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public<X> JobParameterDefinition<X> fromString(String json) {

        if (StringUtils.isBlank(json)) {
            return null;
        }

        try {

            JsonNode root = JsonUtils.getMapper().readTree(json);
            JsonNode jobName = root.get(PARAMETER_FIELD_JOB_NAME);
            if (Objects.isNull(jobName) || !jobName.isTextual()) {
                LOGGER.error("Unable to unmarshal string to JPD from [{}]. Job name not present.", json);
                return null;
            }

            JsonNode paramName = root.get(PARAMETER_FIELD_PARAM_NAME);
            if (Objects.isNull(paramName) || !paramName.isTextual()) {
                LOGGER.error("Unable to unmarshal string to JPD from [{}]. Param name not present.", json);
                return null;
            }

            JobDescriptor jd = jobRegistry.getDescriptor(StringUtils.trim(jobName.asText()));
            if (Objects.isNull(jd)) {
                LOGGER.error("Unable to unmarshal string to JPD from [{}]. Job descriptor not found.", json);
                return null;
            }

            JobParameterDescriptor<?> jpd = jd.findParameter(StringUtils.trim(paramName.asText()));
            if (Objects.isNull(jpd)) {
                LOGGER.error("Unable to unmarshal string to JPD from [{}]. Parameter descriptor not found.", json);
                return null;
            }

            JsonNode value = root.get(PARAMETER_FIELD_VALUE);
            return (JobParameterDefinition<X>) fromValue(jpd, fromNode(jpd, value));

        } catch (Exception e) {
            LOGGER.error("Unable to unmarshal string to JPD from [{}].", json, e);
        }

        return null;
    }
    /**
     * Extracts parameter's value from the {@link JsonNode} using the supplied parameter descriptor.
     * @param d the descriptor
     * @param value the value
     * @return value as object (single value, collection, map or custom value)
     */
    @Nullable
    public Object fromNode(JobParameterDescriptor<?> d, JsonNode value) {

        if (Objects.isNull(value) || value.isNull() || Objects.isNull(d)) {
            return null;
        }

        return readNode(value, d);
    }
    /**
     * Creates a new parameter definition using supplied value and parameter descriptor.
     * The values are expected to be strictly of the type and kind, declared by descriptor or null.
     * This is responsibility of either deserializer or parameter default value supplier.
     * The caller will get CCE, if this can not be ensured.
     * @param <X> the expected value type
     * @param d the descriptor
     * @param value the value to cast and turn into a {@link JobParameterDefinition}.
     * @return parameter definition or null
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public<X> JobParameterDefinition<X> fromValue(JobParameterDescriptor<X> d, Object value) {

        if (Objects.isNull(d)) {
            return null;
        }

        switch (d.getType()) {
        case BOOLEAN:
            return (JobParameterDefinition<X>) fromBoolean((JobParameterDescriptor<Boolean>) d, value);
        case CLOB:
            return (JobParameterDefinition<X>) fromClob((JobParameterDescriptor<String>) d, value);
        case CUSTOM:
            return (JobParameterDefinition<X>) fromCustom(d, value);
        case DATE:
            return (JobParameterDefinition<X>) fromDate((JobParameterDescriptor<LocalDate>) d, value);
        case INSTANT:
            return (JobParameterDefinition<X>) fromInstant((JobParameterDescriptor<Instant>) d, value);
        case INTEGER:
            return (JobParameterDefinition<X>) fromInteger((JobParameterDescriptor<Long>) d, value);
        case NUMBER:
            return (JobParameterDefinition<X>) fromNumber((JobParameterDescriptor<Double>) d, value);
        case STRING:
            return (JobParameterDefinition<X>) fromString((JobParameterDescriptor<String>) d, value);
        case TIME:
            return (JobParameterDefinition<X>) fromTime((JobParameterDescriptor<LocalTime>) d, value);
        case TIMESTAMP:
            return (JobParameterDefinition<X>) fromTimestamp((JobParameterDescriptor<LocalDateTime>) d, value);
        default:
            break;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private JobParameterDefinition<Boolean> fromBoolean(JobParameterDescriptor<Boolean> d, Object value) {

        if (d.isCollection()) {
            Collection<Boolean> v = (Collection<Boolean>) value;
            return new CollectionBooleanParameterDefinition(d.getName(), v);
        } else if (d.isMap()) {
            Map<String, Boolean> v = (Map<String, Boolean>) value;
            return new MapBooleanParameterDefinition(d.getName(), v);
        } else {
            Boolean v = (Boolean) value;
            return new SingleBooleanParameterDefinition(d.getName(), v);
        }
    }

    @SuppressWarnings("unchecked")
    private JobParameterDefinition<String> fromClob(JobParameterDescriptor<String> d, Object value) {

        if (d.isCollection()) {
            Collection<String> v = (Collection<String>) value;
            return new CollectionClobParameterDefinition(d.getName(), v);
        } else if (d.isMap()) {
            Map<String, String> v = (Map<String, String>) value;
            return new MapClobParameterDefinition(d.getName(), v);
        } else {
            String v = (String) value;
            return new SingleClobParameterDefinition(d.getName(), v);
        }
    }

    @SuppressWarnings("unchecked")
    private JobParameterDefinition<LocalDate> fromDate(JobParameterDescriptor<LocalDate> d, Object value) {

        if (d.isCollection()) {
            Collection<LocalDate> v = (Collection<LocalDate>) value;
            return new CollectionDateParameterDefinition(d.getName(), v);
        } else if (d.isMap()) {
            Map<String, LocalDate> v = (Map<String, LocalDate>) value;
            return new MapDateParameterDefinition(d.getName(), v);
        } else {
            LocalDate v = (LocalDate) value;
            return new SingleDateParameterDefinition(d.getName(), v);
        }
    }

    @SuppressWarnings("unchecked")
    private JobParameterDefinition<Instant> fromInstant(JobParameterDescriptor<Instant> d, Object value) {

        if (d.isCollection()) {
            Collection<Instant> v = (Collection<Instant>) value;
            return new CollectionInstantParameterDefinition(d.getName(), v);
        } else if (d.isMap()) {
            Map<String, Instant> v = (Map<String, Instant>) value;
            return new MapInstantParameterDefinition(d.getName(), v);
        } else {
            Instant v = (Instant) value;
            return new SingleInstantParameterDefinition(d.getName(), v);
        }
    }

    @SuppressWarnings("unchecked")
    private JobParameterDefinition<Long> fromInteger(JobParameterDescriptor<Long> d, Object value) {

        if (d.isCollection()) {
            Collection<Long> v = (Collection<Long>) value;
            return new CollectionIntegerParameterDefinition(d.getName(), v);
        } else if (d.isMap()) {
            Map<String, Long> v = (Map<String, Long>) value;
            return new MapIntegerParameterDefinition(d.getName(), v);
        } else {
            Long v = (Long) value;
            return new SingleIntegerParameterDefinition(d.getName(), v);
        }
    }

    @SuppressWarnings("unchecked")
    private JobParameterDefinition<Double> fromNumber(JobParameterDescriptor<Double> d, Object value) {

        if (d.isCollection()) {
            Collection<Double> v = (Collection<Double>) value;
            return new CollectionNumberParameterDefinition(d.getName(), v);
        } else if (d.isMap()) {
            Map<String, Double> v = (Map<String, Double>) value;
            return new MapNumberParameterDefinition(d.getName(), v);
        } else {
            Double v = (Double) value;
            return new SingleNumberParameterDefinition(d.getName(), v);
        }
    }

    @SuppressWarnings("unchecked")
    private JobParameterDefinition<String> fromString(JobParameterDescriptor<String> d, Object value) {

        if (d.isCollection()) {
            Collection<String> v = (Collection<String>) value;
            return new CollectionStringParameterDefinition(d.getName(), v);
        } else if (d.isMap()) {
            Map<String, String> v = (Map<String, String>) value;
            return new MapStringParameterDefinition(d.getName(), v);
        } else {
            String v = (String) value;
            return new SingleStringParameterDefinition(d.getName(), v);
        }
    }

    @SuppressWarnings("unchecked")
    private JobParameterDefinition<?> fromTime(JobParameterDescriptor<LocalTime> d, Object value) {

        if (d.isCollection()) {
            Collection<LocalTime> v = (Collection<LocalTime>) value;
            return new CollectionTimeParameterDefinition(d.getName(), v);
        } else if (d.isMap()) {
            Map<String, LocalTime> v = (Map<String, LocalTime>) value;
            return new MapTimeParameterDefinition(d.getName(), v);
        } else {
            LocalTime v = (LocalTime) value;
            return new SingleTimeParameterDefinition(d.getName(), v);
        }
    }

    @SuppressWarnings("unchecked")
    private JobParameterDefinition<LocalDateTime> fromTimestamp(JobParameterDescriptor<LocalDateTime> d, Object value) {

        if (d.isCollection()) {
            Collection<LocalDateTime> v = (Collection<LocalDateTime>) value;
            return new CollectionTimestampParameterDefinition(d.getName(), v);
        } else if (d.isMap()) {
            Map<String, LocalDateTime> v = (Map<String, LocalDateTime>) value;
            return new MapTimestampParameterDefinition(d.getName(), v);
        } else {
            LocalDateTime v = (LocalDateTime) value;
            return new SingleTimestampParameterDefinition(d.getName(), v);
        }
    }

    private JobParameterDefinition<?> fromCustom(JobParameterDescriptor<?> d, Object value) {
        return d.custom(d.getName(), value);
    }

    private Object readNode(JsonNode n, JobParameterDescriptor<?> pd) {

        if (pd.isMap()) {

            if (!n.isObject()) {
                logNodeTypeMismatch(pd.getName());
            } else {
                return readMap((ObjectNode) n, pd);
            }
        } else if (pd.isCollection()) {

            if (!n.isArray()) {
                logNodeTypeMismatch(pd.getName());
            } else {
                return readCollection((ArrayNode) n, pd);
            }
        } else if (pd.isSingle()) {

            if (!n.isValueNode()) {
                logNodeTypeMismatch(pd.getName());
            } else {
                return readSingle((ValueNode) n, pd);
            }
        } else if (pd.isCustom()) {
            return readCustomParameter(n, pd);
        }

        return null;
    }

    private Map<String, ?> readMap(ObjectNode n, JobParameterDescriptor<?> pd) {

        if (n.isEmpty()) {
            return Collections.emptyMap();
        }

        switch (pd.getType()) {
        case BOOLEAN:
            return JsonUtils.read(n, JsonUtils.BOOLEAN_MAP_TYPE_REFERENCE);
        case STRING:
        case CLOB:
            return JsonUtils.read(n, JsonUtils.STRING_MAP_TYPE_REFERENCE);
        case DATE:
            return JsonUtils.read(n, JsonUtils.LOCAL_DATE_MAP_TYPE_REFERENCE);
        case INSTANT:
            return JsonUtils.read(n, JsonUtils.INSTANT_MAP_TYPE_REFERENCE);
        case INTEGER:
            return JsonUtils.read(n, JsonUtils.LONG_MAP_TYPE_REFERENCE);
        case NUMBER:
            return JsonUtils.read(n, JsonUtils.DOUBLE_MAP_TYPE_REFERENCE);
        case TIME:
            return JsonUtils.read(n, JsonUtils.LOCAL_TIME_MAP_TYPE_REFERENCE);
        case TIMESTAMP:
            return JsonUtils.read(n, JsonUtils.LOCAL_TIMESTAMP_MAP_TYPE_REFERENCE);
        default:
            break;
        }

        return Collections.emptyMap();
    }

    private Collection<?> readCollection(ArrayNode n, JobParameterDescriptor<?> pd) {

        if (n.isEmpty()) {
            return Collections.emptyList();
        }

        switch (pd.getType()) {
        case BOOLEAN:
            return JsonUtils.read(n, JsonUtils.BOOLEAN_COLLECTION_TYPE_REFERENCE);
        case STRING:
        case CLOB:
            return JsonUtils.read(n, JsonUtils.STRING_COLLECTION_TYPE_REFERENCE);
        case DATE:
            return JsonUtils.read(n, JsonUtils.LOCAL_DATE_COLLECTION_TYPE_REFERENCE);
        case INSTANT:
            return JsonUtils.read(n, JsonUtils.INSTANT_COLLECTION_TYPE_REFERENCE);
        case INTEGER:
            return JsonUtils.read(n, JsonUtils.LONG_COLLECTION_TYPE_REFERENCE);
        case NUMBER:
            return JsonUtils.read(n, JsonUtils.DOUBLE_COLLECTION_TYPE_REFERENCE);
        case TIME:
            return JsonUtils.read(n, JsonUtils.LOCAL_TIME_COLLECTION_TYPE_REFERENCE);
        case TIMESTAMP:
            return JsonUtils.read(n, JsonUtils.LOCAL_TIMESTAMP_COLLECTION_TYPE_REFERENCE);
        default:
            break;
        }

        return Collections.emptyList();
    }

    private Object readSingle(ValueNode n, JobParameterDescriptor<?> pd) {

        switch (pd.getType()) {
        case BOOLEAN:
            if (!n.isBoolean()) {
                logNodeTypeMismatch(pd.getName());
            } else {
                return n.asBoolean();
            }
            break;
        case STRING:
        case CLOB:
            if (!n.isTextual()) {
                logNodeTypeMismatch(pd.getName());
            } else {
                return n.asText();
            }
            break;
        case DATE:
            if (!n.isTextual()) {
                logNodeTypeMismatch(pd.getName());
            } else {
                return ConvertUtils.string2LocalDate(StringUtils.trim(n.asText()));
            }
            break;
        case INSTANT:
            if (!n.isTextual()) {
                logNodeTypeMismatch(pd.getName());
            } else {
                return ConvertUtils.string2Instant(StringUtils.trim(n.asText()));
            }
            break;
        case INTEGER:
            if (!n.isIntegralNumber()) {
                logNodeTypeMismatch(pd.getName());
            } else {
                return n.asLong();
            }
            break;
        case NUMBER:
            if (!n.isFloatingPointNumber()) {
                logNodeTypeMismatch(pd.getName());
            } else {
                return n.asDouble();
            }
            break;
        case TIME:
            if (!n.isTextual()) {
                logNodeTypeMismatch(pd.getName());
            } else {
                return ConvertUtils.string2LocalTime(StringUtils.trim(n.asText()));
            }
            break;
        case TIMESTAMP:
            if (!n.isTextual()) {
                logNodeTypeMismatch(pd.getName());
            } else {
                return ConvertUtils.string2LocalDateTime(StringUtils.trim(n.asText()));
            }
            break;
        default:
            break;
        }

        return null;
    }

    private Object readCustomParameter(JsonNode n, JobParameterDescriptor<?> pd) {

        if (n.isEmpty()) {
            return null;
        }

        return JsonUtils.read(n, pd.getValueType());
    }

    private void logNodeTypeMismatch(String fieldId) {
        LOGGER.error("The value for parameter [{}] is not of the expected type.", fieldId);
    }
}
