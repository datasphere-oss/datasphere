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
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.unidata.mdm.rest.system.exception.RestExceptionId;
import org.unidata.mdm.system.exception.PlatformFailureException;

/**
 * @author Alexey Tsarapkin
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
