/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package org.unidata.mdm.rest.system.serializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.huahui.datasphere.rest.system.ro.SimpleDataType;

import org.unidata.mdm.system.util.ConvertUtils;

/**
 * @author Alexey Tsarapkin
 */
public abstract class AbstractJsonSerializer<T> extends JsonSerializer<T> {

    protected static final String REST_DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    /**
     * Date format without milliseconds.
     * Frontend specific.
     */
    private static final DateTimeFormatter DEFAULT_TIMESTAMP_NO_MS = DateTimeFormatter.ofPattern(REST_DATE_TIME_FORMAT_PATTERN);

    private static final String VALUE_FIELD = "value";

    protected void writeValue(SimpleDataType type, Object value, JsonGenerator jgen) throws IOException {

        switch (type) {
            case DATE:
                if (value == null) {
                    jgen.writeNullField(VALUE_FIELD);
                } else {
                    jgen.writeStringField(VALUE_FIELD, formatLocalDate((LocalDate) value));
                }
                break;
            case TIME:
                if (value == null) {
                    jgen.writeNullField(VALUE_FIELD);
                } else {
                    jgen.writeStringField(VALUE_FIELD, formatLocalTime((LocalTime) value));
                }
                break;
            case TIMESTAMP:
                if (value == null) {
                    jgen.writeNullField(VALUE_FIELD);
                } else {
                    jgen.writeStringField(VALUE_FIELD, formatLocalDateTime((LocalDateTime) value));
                }
                break;
            default:
                jgen.writeObjectField(VALUE_FIELD, value);
        }
    }

    /**
     * Format LocalTime to String
     * {@see DEFAULT_TIMESTAMP_NO_MS}.
     *
     * @param localDate
     *
     * @return parsed date.
     */
    protected String formatLocalDate(LocalDate localDate) {
        return ConvertUtils.localDate2LocalDateTime(localDate).format(DEFAULT_TIMESTAMP_NO_MS);
    }

    /**
     * Format LocalTime to String
     * {@see DEFAULT_TIMESTAMP_NO_MS}.
     *
     * @param localTime
     *
     * @return parsed date.
     */
    protected String formatLocalTime(LocalTime localTime) {
        return ConvertUtils.localTime2LocalDateTime(localTime).format(DEFAULT_TIMESTAMP_NO_MS);
    }

    /**
     * Format LocalDateTime to String
     * {@see DEFAULT_TIMESTAMP_NO_MS}.
     *
     * @param localDateTime
     *
     * @return parsed date.
     */
    protected String formatLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DEFAULT_TIMESTAMP_NO_MS);
    }


    @Override
    abstract public void serialize(T value, JsonGenerator gen, SerializerProvider serializers)throws IOException, JsonProcessingException;

}
