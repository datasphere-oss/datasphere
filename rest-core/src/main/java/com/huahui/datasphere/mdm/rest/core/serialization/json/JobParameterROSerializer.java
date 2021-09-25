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
package com.huahui.datasphere.mdm.rest.core.serialization.json;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.huahui.datasphere.mdm.rest.core.ro.job.JobParameterRO;

/**
 * @author theseusyang
 */
public class JobParameterROSerializer extends JsonSerializer<JobParameterRO> {

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_VALUE = "value";
    public static final String FIELD_MULTISELECT = "multi_select";

    @Override
    public void serialize(final JobParameterRO value, final JsonGenerator generator, final SerializerProvider serializers) throws IOException {

        generator.writeStartObject();
        if (value.getId() != null) {
            generator.writeNumberField(FIELD_ID, value.getId());
        }

        generator.writeStringField(FIELD_NAME, value.getName());
        generator.writeBooleanField(FIELD_MULTISELECT, value.isMultiSelect());

        if (value.getValueSize() > 1) {
            generator.writeFieldName(FIELD_VALUE);
            generator.writeStartArray();
            switch (value.getType()) {
                case STRING:
                    for (final String s : (String[]) value.getArrayValue()) {
                        generator.writeString(s);
                    }
                    break;
                case DATE:
                    for (final ZonedDateTime dt : (ZonedDateTime[]) value.getArrayValue()) {
                        generator.writeString(dt.truncatedTo(ChronoUnit.SECONDS)
                                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                    }
                    break;
                case LONG:
                    for (final Long l : (Long[]) value.getArrayValue()) {
                        generator.writeNumber(l);
                    }
                    break;
                case DOUBLE:
                    for (final Double d : (Double[]) value.getArrayValue()) {
                        generator.writeNumber(d);
                    }
                    break;
                case BOOLEAN:
                    for (final Boolean b : (Boolean[]) value.getArrayValue()) {
                        generator.writeBoolean(b);
                    }
                    break;
            }
            generator.writeEndArray();
        } else {
            switch (value.getType()) {
                case STRING:
                    generator.writeStringField(FIELD_VALUE, value.getStringValue());
                    break;
                case DATE:
                    generator.writeStringField(FIELD_VALUE, value.getDateValue().truncatedTo(ChronoUnit.SECONDS)
                            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                    break;
                case LONG:
                    generator.writeNumberField(FIELD_VALUE, value.getLongValue());
                    break;
                case DOUBLE:
                    generator.writeNumberField(FIELD_VALUE, value.getDoubleValue());
                    break;
                case BOOLEAN:
                    generator.writeBooleanField(FIELD_VALUE, value.getBooleanValue());
                    break;
            }
        }
        generator.writeObjectField(FIELD_TYPE, value.getType());
        generator.writeEndObject();
    }
}
