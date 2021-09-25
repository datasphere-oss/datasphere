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
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.huahui.datasphere.mdm.rest.system.exception.RestExceptionId;

import com.huahui.datasphere.mdm.system.exception.PlatformFailureException;

/**
 * @author theseusyang
 */
public abstract class AbstractJsonDeserializer<T> extends JsonDeserializer<T> {
    /**
     * Date format without milliseconds.
     * Frontend specific.
     */
    public static final DateTimeFormatter DEFAULT_TIMESTAMP_NO_MS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    /**
     * Parses string representation of date according to LocalDate from
     * {@see DEFAULT_TIMESTAMP_NO_MS}.
     *
     * @param dateAsString string representation of date.
     * @return parsed date.
     */
    protected LocalDate parseToLocalDate(String dateAsString) {
        try {
            return LocalDate.parse(dateAsString, DEFAULT_TIMESTAMP_NO_MS);
        } catch (DateTimeParseException e) {
            throw new PlatformFailureException(
                    "Incorrect date format found, unable to parse date string!",
                    e,
                    RestExceptionId.EX_DATA_CANNOT_PARSE_DATE,
                    dateAsString
            );
        }
    }

    /**
     * Parses string representation of date according to LocalTime from
     * {@see DEFAULT_TIMESTAMP_NO_MS}.
     *
     * @param dateAsString string representation of date.
     * @return parsed date.
     */
    protected LocalTime parseToLocalTime(String dateAsString) {
        try {
            return LocalTime.parse(dateAsString, DEFAULT_TIMESTAMP_NO_MS);
        } catch (DateTimeParseException e) {
            throw new PlatformFailureException(
                    "Incorrect date format found, unable to parse date string!",
                    e,
                    RestExceptionId.EX_DATA_CANNOT_PARSE_DATE,
                    dateAsString
            );
        }
    }

    /**
     * Parses string representation of date according to LocalDateTime from
     * {@see DEFAULT_TIMESTAMP_NO_MS}.
     *
     * @param dateAsString string representation of date.
     * @return parsed date.
     */
    protected LocalDateTime parseToLocalDateTime(String dateAsString) {
        try {
            return LocalDateTime.parse(dateAsString, DEFAULT_TIMESTAMP_NO_MS);
        } catch (DateTimeParseException e) {
            throw new PlatformFailureException(
                    "Incorrect date format found, unable to parse date string!",
                    e,
                    RestExceptionId.EX_DATA_CANNOT_PARSE_DATE,
                    dateAsString
            );
        }
    }

    @Override
    abstract public T deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException;

}
