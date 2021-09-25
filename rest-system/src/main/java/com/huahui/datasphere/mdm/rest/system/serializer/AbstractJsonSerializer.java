/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.huahui.datasphere.mdm.rest.system.serializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.huahui.datasphere.mdm.rest.system.ro.SimpleDataType;

import com.huahui.datasphere.mdm.system.util.ConvertUtils;

/**
 * @author theseusyang
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
