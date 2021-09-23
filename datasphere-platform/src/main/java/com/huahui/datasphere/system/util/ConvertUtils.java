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

package com.huahui.datasphere.system.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.extra.chrono.JulianChronology;
import org.threeten.extra.chrono.JulianDate;

/**
 * @author Mikhail Mikhailov
 * Various conversions.
 */
public class ConvertUtils {
    /**
     * This logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertUtils.class);
    /**
     * No TZ offset date formatter.
     */
    private static final DateTimeFormatter DEFAULT_LOCAL_DATE_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * No TZ offset timestamp formatter.
     */
    private static final DateTimeFormatter DEFAULT_LOCAL_TIMESTAMP_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    /**
     * Zero offset formatter.
     */
    public static final DateTimeFormatter ZULU_TIMESTAMP_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    private static final FastDateFormat DATE_TIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS");

    /**
     * Start of epoch as local date.
     */
    private static final LocalDate START_OF_EPOCH = LocalDate.of(1970, 1, 1);
    /**
     * Fix UTC ZID here.
     */
    public static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");
    /**
     * The point at which the Gregorian calendar rules are used, measured in
     * milliseconds from the standard epoch.  Default is October 15, 1582
     * (Gregorian) 00:00:00 UTC or -12219292800000L.  For this value, October 4,
     * 1582 (Julian) is followed by October 15, 1582 (Gregorian).  This
     * corresponds to Julian day number 2299161.
     *
     * @serial
     */
    private static final long DEFAULT_GREGORIAN_CUTOVER = -12219292800000L;

    /**
     * Constructor.
     */
    private ConvertUtils() {
        super();
    }

    /**
     * Date 2 LocalDate.
     *
     * @param d the date
     * @return local date
     */
    public static LocalDate date2LocalDate(Date d) {

        if (d != null && d.getTime() < DEFAULT_GREGORIAN_CUTOVER) {
            JulianDate julianDate = JulianChronology.INSTANCE.date(d.toInstant().atZone(ZoneId.systemDefault()));
            return LocalDate.of(
                    julianDate.get(ChronoField.YEAR_OF_ERA),
                    julianDate.get(ChronoField.MONTH_OF_YEAR),
                    julianDate.get(ChronoField.DAY_OF_MONTH));
        }

        return d == null ? null : new Date(d.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date 2 LocalTime.
     *
     * @param d the date
     * @return local time
     */
    public static LocalTime date2LocalTime(Date d) {
        return d == null ? null : new Date(d.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * Date 2 LocalDateTime.
     *
     * @param d the date
     * @return local date time
     */
    public static LocalDateTime date2LocalDateTime(Date d) {

        LocalDateTime ldt = d == null ? null : d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        if (d != null && d.getTime() < DEFAULT_GREGORIAN_CUTOVER) {
            JulianDate julianDate = JulianChronology.INSTANCE.date(d.toInstant().atZone(ZoneId.systemDefault()));
            return LocalDateTime.of(
                    julianDate.get(ChronoField.YEAR_OF_ERA),
                    julianDate.get(ChronoField.MONTH_OF_YEAR),
                    julianDate.get(ChronoField.DAY_OF_MONTH),
                    ldt.getHour(),
                    ldt.getMinute(),
                    ldt.getSecond(),
                    ldt.getNano());
        }

        return ldt;
    }

    /**
     * LocalDate 2 Date.
     *
     * @param d the local date
     * @return date
     */
    public static Date localDate2Date(LocalDate d) {
        return d == null ? null : Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalTime 2 Date.
     *
     * @param d the local time
     * @return date
     */
    public static Date localTime2Date(LocalTime d) {
        return d == null ? null : Date.from(d.atDate(START_OF_EPOCH).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDate 2 Date.
     * crazy formatter localdate to date. Date.from(Instance(1111-11-11))  lying
     *
     * @param d the local time
     * @return date
     */
    public static Date localDate2DateUsingParser(LocalDate d) {
        try {
            return d == null ? null : DATE_FORMAT.parse(d.format(DEFAULT_LOCAL_DATE_FORMATTER));
        } catch (ParseException e) {
            // TODO : UN-11834
            return null;
            /*
            throw new BusinessException("cannot parse date " + d.toString(),
                    ExceptionId.EX_CLASSIFIER_NODE_ATTRIBUTE_CONVERSION_ERROR,
                    d.toString());
            */
        }
    }

    /**
     * LocalDateTime 2 Date.
     * crazy formatter LocalDateTime to date. Date.from(Instance(1111-11-11))  lying
     *
     * @param d the local time
     * @return date
     */
    public static Date localDateTime2DateUsingParser(LocalDateTime d) {
        try {
            return d == null ? null : DATE_TIME_FORMAT.parse(d.format(DEFAULT_LOCAL_TIMESTAMP_FORMATTER));
        } catch (ParseException e) {
            // TODO : UN-11834
            return null;
            /*
            throw new BusinessException("cannot parse datetime " + d.toString(),
                    ExceptionId.EX_CLASSIFIER_NODE_ATTRIBUTE_CONVERSION_ERROR,
                    d.toString());
            */
        }
    }
    /**
     * LocalDateTime 2 Date.
     *
     * @param d the local date time
     * @return date
     */
    public static Date localDateTime2Date(LocalDateTime d) {
        return d == null ? null : Date.from(d.atZone(ZoneId.systemDefault()).toInstant());
    }
    /**
     * LocalDateTime 2 Date.
     *
     * @param d the local date time
     * @return date
     */
    public static Date localDateTime2ZonedDateTime(LocalDateTime d) {

        return d == null ? null : Date.from(d.atZone(ZoneId.systemDefault()).toInstant());
    }
    /**
     * LocalDateTime 2 Date.
     *
     * @param d the local date time
     * @return date
     */
    public static OffsetDateTime localDateTime2OffsetDateTime(LocalDateTime ldt, ZoneId zoneId) {
        return ldt == null ? null : OffsetDateTime.ofInstant(ldt.atZone(ZoneId.systemDefault()).toInstant(), zoneId);
    }
    /**
     * Converts {@link OffsetDateTime} to {@link Date}.
     * @param z the offset date time
     * @return date
     */
    public static Date offsetDateTime2Date(OffsetDateTime z){
        return z == null ? null : Date.from(z.toInstant());
    }
    /**
     * @param z the zoned date time
     * @return date
     */
    public static Date zonedDateTime2Date(ZonedDateTime z) {
        return z == null ? null : Date.from(z.toInstant());
    }

    /**
     * Converts to zoned DT from {@linkplain Date}.
     * @param date the {@link Date}
     * @return ZDT
     */
    public static ZonedDateTime zonedDateTimeFromDate(final Date date) {
        return date == null ? null : ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalTime 2 LocalDateTime.
     *
     * @param d the local time
     * @return date
     */
    public static LocalDateTime localTime2LocalDateTime(LocalTime d) {
        return d == null ? null : LocalDateTime.of(START_OF_EPOCH, d);
    }

    /**
     * LocalDate 2 LocalDateTime.
     *
     * @param d the local date
     * @return date
     */
    public static LocalDateTime localDate2LocalDateTime(LocalDate d) {
        return d == null ? null : LocalDateTime.of(d, LocalTime.MIN);
    }

    public static LocalDate date2LocalDateWithoutOffset(final Date d) {
        if (d != null && d.getTime() < DEFAULT_GREGORIAN_CUTOVER) {
            return new Timestamp(d.getTime()).toLocalDateTime().toLocalDate();
        }

        return d == null ? null : new Date(d.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime date2LocalDateTimeWithoutOffset(Date d) {
        LocalDateTime ldt = d == null ? null : d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        if (d != null && d.getTime() < DEFAULT_GREGORIAN_CUTOVER) {
            return new Timestamp(d.getTime()).toLocalDateTime();
        }

        return ldt;
    }
    public static String localDateTime2UTCAndFormat(LocalDateTime ldt) {
        return ldt == null ? null : ZULU_TIMESTAMP_FORMATTER.format(localDateTime2OffsetDateTime(ldt, ConvertUtils.UTC_ZONE_ID));
    }

    /**
     * Parses string representation of date according to date format from
     * {@see DEFAULT_LOCAL_TIMESTAMP_FORMATTER}.
     *
     * @param param
     *            string representation of date.
     * @return parsed date.
     */
    public static Date string2Date(String param) {

        try {
            if (StringUtils.isNotBlank(param)) {
                return localDateTime2Date(LocalDateTime.parse(param, DEFAULT_LOCAL_TIMESTAMP_FORMATTER));
            }
        } catch (Exception e) {
            LOGGER.warn("Cannot parse validity period boundary {}.", param, e);
        }

        return null;
    }

    public static LocalDateTime string2LocalDateTime(String param) {

        try {
            if (StringUtils.isNotBlank(param)) {
                return LocalDateTime.parse(param, DEFAULT_LOCAL_TIMESTAMP_FORMATTER);
            }
        } catch (Exception e) {
            LOGGER.warn("Cannot parse validity period boundary {}.", param, e);
        }

        return null;
    }

    /**
     * Reads boundary date.
     * @param param the timestamp as {@linkplain Date} object
     * @return date string or null
     */
    public static String date2String(Date param) {

        try {
            return Objects.isNull(param)
                    ? null
                    : DEFAULT_LOCAL_TIMESTAMP_FORMATTER.format(ConvertUtils.date2LocalDateTime(param));
        } catch (Exception e) {
            LOGGER.warn("Cannot unparse date [{}] to string.", param, e);
        }

        return null;
    }
}
