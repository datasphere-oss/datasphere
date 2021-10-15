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

package org.datasphere.mdm.core.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.type.data.ArrayAttribute;
import org.datasphere.mdm.core.type.data.CodeAttribute;
import org.datasphere.mdm.core.type.data.MeasuredValue;
import org.datasphere.mdm.core.type.data.SimpleAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.datasphere.mdm.system.exception.PlatformFailureException;

public final class AttributeUtils {
    /**
     * The AttributeUtils log.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeUtils.class);

    private AttributeUtils() {
        super();
    }
    /**
     * Array of available for system formaters
     */
    private static final DateTimeFormatter[] FORMATTERS = new DateTimeFormatter[] {
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ISO_DATE,
            DateTimeFormatter.ISO_TIME,
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS")
    };

    /**
     * Sets the value to the simple attribute.
     *
     * @param valueAttr data simple attribute
     * @param value     the value to set
     */
    public static void processCodeAttributeValue(@Nullable CodeAttribute<?> valueAttr, @Nullable Object value) {

        if (Objects.isNull(valueAttr)) {
            return;
        }

        // Overwrite or set the value
        switch (valueAttr.getDataType()) {
        case INTEGER:
            valueAttr.castValue(toLong(value));
            return;
        case STRING:
            valueAttr.castValue(toString(value));
            return;
        default:
            return;
        }
    }

    /**
     * Sets the value to the simple attribute.
     *
     * @param valueAttr data simple attribute
     * @param value     the value to set
     */
    public static void processSimpleAttributeValue(@Nullable SimpleAttribute<?> valueAttr, @Nullable Object value) {

        if (Objects.isNull(valueAttr)) {
            return;
        }

        // Overwrite or set the value
        try{
            switch (valueAttr.getDataType()) {
            case BOOLEAN:
                valueAttr.castValue(toBoolean(value));
                return;
            case INTEGER:
                valueAttr.castValue(toLong(value));
                return;
            case NUMBER:
                valueAttr.castValue(toDouble(value));
                return;
            case MEASURED:
                MeasuredValue v = valueAttr.castValue();
                v.setInitialValue(toDouble(value));
                return;
            case STRING:
            case ENUM:
            case DICTIONARY:
                valueAttr.castValue(toString(value));
                return;
            case DATE:
                valueAttr.castValue(toDate(value));
                return;
            case TIME:
                valueAttr.castValue(toTime(value));
                return;
            case TIMESTAMP:
                valueAttr.castValue(toTimestamp(value));
                return;
            case BLOB:
            case CLOB:
            default:
                return;
            }
        } catch (Exception e){
            String message = "Unable to instantiate an array variable. Value [{}]. Attribute name [{}]. Type [{}].";
            LOGGER.error(message, value, valueAttr.getName(), valueAttr.getDataType(), e);
            throw new PlatformFailureException(message, CoreExceptionIds.EX_DATA_IMPORT_IMPOSSIBLE_CONVERT_TO_TYPE,
                    value, valueAttr.getName(), valueAttr.getDataType());
        }
    }

    /**
     * Sets the value to the simple attribute.
     *
     * @param valueAttr data simple attribute
     * @param value     the value to set
     */
    public static void processArrayAttributeValue(@Nullable ArrayAttribute<?> valueAttr, @Nullable Object value, String exchangeSeparator) {

        if (Objects.isNull(valueAttr)) {
            return;
        }

        List<?> result = null;
        String[] values = splitArrayValues(value, exchangeSeparator);
        if (ArrayUtils.isNotEmpty(values)) {

            try {
                switch (valueAttr.getDataType()) {
                case INTEGER:
                    result = Arrays.stream(values).map(AttributeUtils::toLong).filter(Objects::nonNull).collect(Collectors.toList());
                    break;
                case NUMBER:
                    result = Arrays.stream(values).map(AttributeUtils::toDouble).filter(Objects::nonNull).collect(Collectors.toList());
                    break;
                case STRING:
                    result = Arrays.stream(values).map(AttributeUtils::toString).filter(Objects::nonNull).collect(Collectors.toList());
                    break;
                case DATE:
                    result = Arrays.stream(values).map(AttributeUtils::toDate).filter(Objects::nonNull).collect(Collectors.toList());
                    break;
                case TIME:
                    result = Arrays.stream(values).map(AttributeUtils::toTime).filter(Objects::nonNull).collect(Collectors.toList());
                    break;
                case TIMESTAMP:
                    result = Arrays.stream(values).map(AttributeUtils::toTimestamp).filter(Objects::nonNull).collect(Collectors.toList());
                    break;
                default:
                    break;
                }
            } catch (Exception e) {
                String message =
                        "Unable to instantiate an array variable.Value[" + value + "]. AttrName ["
                                + valueAttr.getName() + "]. Array type [" + valueAttr.getDataType() + "]";
                LOGGER.error(message, e);
                throw new PlatformFailureException(message, CoreExceptionIds.EX_DATA_IMPORT_IMPOSSIBLE_CONVERT_TO_TYPE,
                        value, valueAttr.getName(), "Массив " + valueAttr.getDataType());
            }
        }

        valueAttr.castValue(result);
    }

    /**
     * Splits array values using specific separator.
     * @param value the value
     * @param exchangeSeparator the separator
     * @return array or null
     */
    public static String[] splitArrayValues(Object value, String exchangeSeparator) {

        if (Objects.isNull(value)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        return StringUtils.split(value.toString(), StringUtils.isBlank(exchangeSeparator) ? "|" : StringUtils.trim(exchangeSeparator));
    }

    /**
     * Joins array values using specific separator.
     * @param values the values
     * @param exchangeSeparator the separator
     * @return array or null
     */
    public static String joinArrayValues(Object[] values, String exchangeSeparator) {

        if (Objects.isNull(values)) {
            return null;
        }

        return StringUtils.join(Arrays.stream(values)
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .map(StringUtils::trim)
                    .filter(StringUtils::isNotBlank).toArray(),
                StringUtils.isBlank(exchangeSeparator) ? "|" : StringUtils.trim(exchangeSeparator));
    }

    /**
     * Object to boolean, if required.
     *
     * @param o object
     * @return the boolean
     */
    public static Boolean toBoolean(Object o) {

        if (o == null) {
            return Boolean.FALSE;
        } else if (Boolean.class == o.getClass()) {
            return (Boolean) o;
        }

        return Boolean.class.isAssignableFrom(o.getClass()) ? (Boolean) o : BooleanUtils.toBooleanObject(o.toString());
    }

    /**
     * Object to long, if required.
     *
     * @param o object
     * @return the long
     */
    public static Long toLong(Object o) {

        if (o == null) {
            return null;
        } else if (Long.class == o.getClass()) {
            return (Long) o;
        }

        return Number.class.isAssignableFrom(o.getClass()) ? ((Number) o).longValue() : Long.valueOf(o.toString());
    }

    /**
     * Object to boolean, if required.
     *
     * @param o object
     * @return the double
     */
    public static Double toDouble(Object o) {

        if (o == null) {
            return null;
        } else if (Double.class == o.getClass()) {
            return (Double) o;
        }

        return Number.class.isAssignableFrom(o.getClass()) ? ((Number) o).doubleValue() : Double.valueOf(o.toString());
    }

    /**
     * Object to boolean, if required.
     *
     * @param o object
     * @return the string
     */
    public static String toString(Object o) {

        if (o == null) {
            return null;
        } else if (String.class == o.getClass()) {
            return (String) o;
        }

        return String.class.isAssignableFrom(o.getClass()) ? (String) o : o.toString();
    }

    /**
     * Object to boolean, if required.
     *
     * @param o object
     * @return the XML gregorian calendar
     */
    public static LocalDate toDate(Object o) {
        if (o == null) {
            return null;
        } else if (LocalDate.class == o.getClass()) {
            return (LocalDate) o;
        } else if (Calendar.class.isAssignableFrom(o.getClass())) {
            return ((Calendar) o).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else if (java.sql.Date.class.isAssignableFrom(o.getClass())) {
            return ((java.sql.Date) o).toLocalDate();
        } else if (Date.class.isAssignableFrom(o.getClass())) {
            return new Date(((Date) o).getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else if (String.class.isAssignableFrom(o.getClass())) {
            TemporalAccessor result = parseStringToDate(o.toString());
            if (result != null) {
                return result.query(LocalDate::from);
            } else {
                String message = "Cannot convert value [{}] to LocalDate.";
                LOGGER.error(message, o);
                throw new PlatformFailureException(message, CoreExceptionIds.EX_DATA_IMPORT_INCORRECT_STRING_TIME_FORMAT, o);
            }
        } else {
            String message = "Cannot convert value of type [{}] to LocalDate.";
            LOGGER.error(message, o.getClass().getName());
            throw new PlatformFailureException(message, CoreExceptionIds.EX_DATA_IMPORT_UNSUPPORTED_TIME_FORMAT, o.getClass().getName());
        }
    }

    /**
     * Object to boolean, if required.
     *
     * @param o object
     * @return the XML gregorian calendar
     */
    public static LocalTime toTime(Object o) {
        if (o == null) {
            return null;
        } else if (LocalTime.class == o.getClass()) {
            return (LocalTime) o;
        } else if (Calendar.class.isAssignableFrom(o.getClass())) {
            return ((Calendar) o).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        } else if (Time.class.isAssignableFrom(o.getClass())) {
            return ((Time) o).toLocalTime();
        } else if (Date.class.isAssignableFrom(o.getClass())) {
            return new Date(((Date) o).getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        } else if (String.class.isAssignableFrom(o.getClass())) {
            TemporalAccessor result = parseStringToDate(o.toString());
            if (result != null) {
                return result.query(LocalTime::from);
            } else {
                String message = "Cannot convert value [{}] to LocalTime.";
                LOGGER.error(message, o);
                throw new PlatformFailureException(message, CoreExceptionIds.EX_DATA_IMPORT_INCORRECT_STRING_TIME_FORMAT, o);
            }
        } else {
            String message = "Cannot convert value of type [{}] to LocalTime.";
            LOGGER.error(message);
            throw new PlatformFailureException(message, CoreExceptionIds.EX_DATA_IMPORT_UNSUPPORTED_TIME_FORMAT, o.getClass().getName());
        }
    }

    /**
     * Object to boolean, if required.
     *
     * @param o object
     * @return the XML gregorian calendar
     */
    public static LocalDateTime toTimestamp(Object o) {
        if (o == null) {
            return null;
        } else if (LocalDateTime.class == o.getClass()) {
            return (LocalDateTime) o;
        } else if (Calendar.class.isAssignableFrom(o.getClass())) {
            return ((Calendar) o).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else if (Timestamp.class.isAssignableFrom(o.getClass())) {
            return ((Timestamp) o).toLocalDateTime();
        } else if (Date.class.isAssignableFrom(o.getClass())) {
            return new Date(((Date) o).getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else if (String.class.isAssignableFrom(o.getClass())) {
            TemporalAccessor result = parseStringToDate(o.toString());
            if (result != null) {
                return result.query(LocalDateTime::from);
            } else {
                String message = "Cannot convert value [{}] to LocalDateTime.";
                LOGGER.error(message, o);
                throw new PlatformFailureException(message, CoreExceptionIds.EX_DATA_IMPORT_INCORRECT_STRING_TIME_FORMAT, o);
            }
        } else {
            String message = "Cannot convert value of type [{}] to LocalDateTime.";
            LOGGER.error(message);
            throw new PlatformFailureException(message, CoreExceptionIds.EX_DATA_IMPORT_UNSUPPORTED_TIME_FORMAT, o.getClass().getName());
        }
    }

    private static TemporalAccessor parseStringToDate(String date) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return formatter.parse(date);
            } catch (Exception e) {
                LOGGER.trace("Cannot format string [{}] to date using [{}]", date, formatter);
            }
        }
        LOGGER.trace("Cannot format string [{}] to date ", date);
        return null;
    }
}
