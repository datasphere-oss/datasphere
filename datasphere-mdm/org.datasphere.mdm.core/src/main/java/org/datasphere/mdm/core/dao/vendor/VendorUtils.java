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

package org.datasphere.mdm.core.dao.vendor;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR_OF_ERA;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.WillClose;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.dao.tokenizer.CompositeValueTokenizer;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.postgresql.PGConnection;
import org.postgresql.copy.PGCopyOutputStream;
import org.datasphere.mdm.system.exception.PlatformFailureException;
import org.datasphere.mdm.system.util.ConvertUtils;
import org.datasphere.mdm.system.util.TimeBoundaryUtils;

/**
 * @author mikhail
 * DB vendor utils. Postgres centric so far.
 */
public class VendorUtils {
    /**
     * Type name 'varchar'.
     */
    public static final String VARCHAR_SQL_TYPE = "varchar";
    /**
     * Type name 'uuid'.
     */
    public static final String UUID_SQL_TYPE = "uuid";
    /**
     * Type name 'uuid'.
     */
    public static final String LONG_SQL_TYPE = "int8";
    /**
     * Type name 'double'.
     */
    public static final String DOUBLE_SQL_TYPE = "double";
    /**
     * Type name 'timestamptz'.
     */
    public static final String TIMESTAMPTZ_SQL_TYPE = "timestamptz";
    /**
     * Postgres epoch 01.01.2000'T'00:00:00.000.
     */
    private static final long POSTGRES_EPOCH_JDATE = 2451545L;
    /**
     * Unix/Java epoch 01.01.1970'T'00:00:00.000.
     */
    private static final long UNIX_EPOCH_JDATE = 2440588L;
    /**
     * Postgres adjustmeny offset in millis (PG epoch - UNIX epoch) * (milliseconds per day).
     */
    public static final long POSTGRES_OFFSET_MILLIS = (POSTGRES_EPOCH_JDATE - UNIX_EPOCH_JDATE) * (86400 * 1000);

    public static final DateTimeFormatter FMT_ERA_OUT =
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendValue(YEAR_OF_ERA, 4, 10, SignStyle.EXCEEDS_PAD)
                .parseLenient()
                .appendLiteral('-')
                .appendValue(MONTH_OF_YEAR, 2)
                .appendLiteral('-')
                .appendValue(DAY_OF_MONTH, 2)
                .appendLiteral(' ')
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .appendLiteral('.')
                .appendFraction(NANO_OF_SECOND, 0, 6, false)
                .appendOffsetId()
                .toFormatter()
                .withChronology(IsoChronology.INSTANCE);

    public static final DateTimeFormatter FMT_ERA_IN =
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendValue(YEAR_OF_ERA, 4, 10, SignStyle.EXCEEDS_PAD)
                .parseLenient()
                .appendLiteral('-')
                .appendValue(MONTH_OF_YEAR, 2)
                .appendLiteral('-')
                .appendValue(DAY_OF_MONTH, 2)
                .appendLiteral(' ')
                .append(ISO_LOCAL_TIME)
                .appendPattern("[xxxxx][xxx][x]")
                .optionalStart()
                .appendPattern(" GG")
                .optionalEnd()
                .toFormatter()
                .withChronology(IsoChronology.INSTANCE);
    /**
     * Constructor.
     */
    private VendorUtils() {
        super();
    }
    /**
     * Constructs text array from input.
     * @param elements the elements
     * @return array string
     */
    public static String textArray(Collection<? extends CharSequence> elements) {
        return typeArray(elements, "text");
    }
    /**
     * Constructs text array from input.
     * @param elements the elements
     * @param array value type
     * @return array string
     */
    public static String typeArray(Collection<? extends CharSequence> elements, String type) {
        return "array[" + (CollectionUtils.isEmpty(elements)
                ? ""
                : String.join(",",
                        elements.stream()
                            .map(CharSequence::toString)
                            .map(str -> StringUtils.wrapIfMissing(str, "'"))
                            .collect(Collectors.toList())
                        )) + "]::" + type + "[]";
    }
    /**
     * Colaesce from date.
     * @param ts the time stamp
     * @return timestamp
     */
    public static Timestamp coalesceFrom(Date ts) {
        return ts != null ? new Timestamp(ts.getTime()) : new Timestamp(TimeBoundaryUtils.TIME_NEGATIVE_INFINITY);
    }
    /**
     * Colaesce to date.
     * @param ts the time stamp
     * @return timestamp
     */
    public static Timestamp coalesceTo(Date ts) {
        return ts != null ? new Timestamp(ts.getTime()) : new Timestamp(TimeBoundaryUtils.TIME_POSITIVE_INFINITY);
    }
    /**
     * Colaesce a date.
     * @param ts the time stamp
     * @return timestamp
     */
    public static Timestamp coalesce(Date ts) {
        return ts != null ? new Timestamp(ts.getTime()) : new Timestamp(System.currentTimeMillis());
    }

    public static Date stringToTimestamp(String s) {

        String chomped = CompositeValueTokenizer.stripText(s);
        if (StringUtils.isBlank(chomped)) {
            return null;
        }

        TemporalAccessor ta = FMT_ERA_IN.parseBest(chomped, ZonedDateTime::from, OffsetDateTime::from, LocalDateTime::from);
        if (ta instanceof ZonedDateTime) {
            return ConvertUtils.zonedDateTime2Date((ZonedDateTime) ta);
        } else if (ta instanceof OffsetDateTime) {
            return ConvertUtils.offsetDateTime2Date((OffsetDateTime) ta);
        } else if (ta instanceof LocalDateTime) {
            return ConvertUtils.localDateTime2Date((LocalDateTime) ta);
        }

        return null;
    }

    public static String timestampToString(Date date) {

        if (Objects.isNull(date)) {
            return StringUtils.EMPTY;
        }

        return FMT_ERA_OUT.format(OffsetDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
    }
    /**
     * Appends row to bulk set.
     * @param stream the stream
     * @param types value types
     * @param values values
     */
    public static void bulkAppend(CopyDataOutputStream stream, VendorDataType[] types, Object[] values) {

        try {
            stream.writeShort(types.length);
            for (int i = 0; i < types.length; i++) {
                VendorUtils.bulkAppend(stream, types[i], values[i]);
            }
        } catch(Exception e) {
            final String message = "Append to bulk set failed.";
            throw new PlatformFailureException(message, e, CoreExceptionIds.EX_DATA_APPEND_BULK_SET_FAILED);
        }
    }
    /**
     * Bulk start wiht 'before' section.
     * @param connection a tomcat pooling connection
     * @param prolog prolog string
     * @param executeBefore befor SQL
     * @return stream
     */
    public static CopyDataOutputStream bulkStart(Connection connection, String prolog, Collection<String> executeBefore) {

        PGConnection pgConnection = connectionUnwrap(connection);
        if (CollectionUtils.isNotEmpty(executeBefore)) {
            @SuppressWarnings("resource")
            Connection standard = (Connection) pgConnection;
            try (Statement s = standard.createStatement()) {

                for (String beforeSQL : executeBefore) {
                    s.executeUpdate(beforeSQL);
                }

            } catch (SQLException e) {
                final String message = "Cannot execute BEFORE statements: ";
                throw new PlatformFailureException(message, e, CoreExceptionIds.EX_DATA_BEFORE_BULK_SET_FAILED);
            }
        }

        return bulkStartInternal(pgConnection, prolog);
    }
    /**
     * Starts a new bulk portion.
     * @param out the driver stream.
     * @return new data stream
     */
    public static CopyDataOutputStream bulkStart(Connection connection, String prolog) {
        return bulkStartInternal(connectionUnwrap(connection), prolog);
    }
    /**
     * Ends a bulk set.
     * @param buffer the buffer to finish.
     */
    public static void bulkFinish(final CopyDataOutputStream buffer) {
        bulkFinishInternal(buffer);
    }
    /**
     * Ends a bulk set.
     * @param buffer the buffer to finish.
     */
    public static void bulkFinish(final CopyDataOutputStream buffer, Collection<String> executeAfter) {
        buffer.executeAfter = executeAfter;
        bulkFinishInternal(buffer);
    }
    /**
     * Common part of bulk start.
     * @param pgConnection unwrapped native connection
     * @param prolog prolog string
     * @return copy stream
     */
    @WillClose
    private static CopyDataOutputStream bulkStartInternal(PGConnection pgConnection, String prolog) {

        CopyDataOutputStream buffer = null;
        try {

            buffer = new CopyDataOutputStream(new CopyBufferedOutputStream(new PGCopyOutputStream(pgConnection, prolog, 8192)));
            buffer.connection = pgConnection;

            // 11 bytes required header
            buffer.writeBytes("PGCOPY\n\377\r\n\0");
            // 32 bit integer indicating no OID
            buffer.writeInt(0);
            // 32 bit header extension area length
            buffer.writeInt(0);

        } catch(Exception e) {
            final String message = "Init bulk set failed.";
            throw new PlatformFailureException(message, e, CoreExceptionIds.EX_DATA_INIT_BULK_SET_FAILED);
        }

        return buffer;
    }
    /**
     * Does write end mark on the stream and calls flush.
     * @param buffer the stream
     */
    private static void bulkFinishInternal(CopyDataOutputStream buffer) {
        try {
            buffer.writeShort(-1);
            buffer.flush();
        } catch(Exception e) {
            final String message = "Finish bulk set failed.";
            throw new PlatformFailureException(message, e, CoreExceptionIds.EX_DATA_FINISH_BULK_SET_FAILED);
        }
    }
    /**
     * Vendor specific bulk append.
     * @param stream the stream to append to
     * @param type the vendor - specific type
     * @param obj the object
     * @throws IOException
     */
    private static void bulkAppend(DataOutputStream stream, VendorDataType type, Object obj) throws IOException {

        if (Objects.isNull(obj)) {
            stream.writeInt(-1);
            return;
        }

        switch (type) {
        case BOOLEAN:
            bulkAppendBoolean(stream, (Boolean) obj);
            break;
        case BYTEA:
            bulkAppendBytea(stream, (byte[]) obj);
            break;
        case CHAR:
        case TEXT:
            bulkAppendChars(stream, (String) obj);
            break;
        case DATE:
        case TIMESTAMP:
            bulkAppendTimestamp(stream, (Date) obj);
            break;
        case DOUBLE:
            bulkAppendDouble(stream, (Double) obj);
            break;
        case REAL:
            bulkAppendFloat(stream, (Float) obj);
            break;
        case INET4:
            bulkAppendInet4Address(stream, (Inet4Address) obj);
            break;
        case INET6:
            bulkAppendInet6Address(stream, (Inet6Address) obj);
            break;
        case INT2:
            bulkAppendShort(stream, (Short) obj);
            break;
        case INT4:
            bulkAppendInteger(stream, (Integer) obj);
            break;
        case INT8:
            bulkAppendLong(stream, (Long) obj);
            break;
        case JSONB:
            bulkAppendJsonb(stream, (String) obj);
            break;
        // case MAC_ADDRESS:
        //    break;
        // case MONEY:
        //    break;
        // case UNKNOWN:
        //    break;
        // case CASH:
        //    break;
        // case CIDR:
        //    break;
        case UUID:
            bulkAppendUUID(stream, (UUID) obj);
            break;
        default:
            break;
        }
    }
    /**
     * Writes boolean value.
     * @param stream the stream
     * @param value the value to write
     * @throws IOException
     */
    private static void bulkAppendBoolean(DataOutputStream stream, Boolean value) throws IOException {
        stream.writeInt(1);
        if (value.booleanValue()) {
            stream.writeByte(1);
        } else {
            stream.writeByte(0);
        }
    }
    /**
     * Writes byte array value.
     * @param stream the stream
     * @param value the value to write
     * @throws IOException
     */
    private static void bulkAppendBytea(DataOutputStream stream, byte[] value) throws IOException {
        stream.writeInt(value.length);
        stream.write(value);
    }
    /**
     * Writes character value.
     * @param stream the stream
     * @param value the value to write
     * @throws IOException
     */
    private static void bulkAppendChars(DataOutputStream stream, String value) throws IOException {
        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        stream.writeInt(bytes.length);
        stream.write(bytes);
    }
    /**
     * Writes JSONB value.
     * @param stream the stream
     * @param value the value to write
     * @throws IOException
     */
    private static void bulkAppendJsonb(DataOutputStream stream, String value) throws IOException {
        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        stream.writeInt(bytes.length + 1);
        stream.writeByte(1); // JSONB protocol version
        stream.write(bytes);
    }
    /**
     * Writes short range integer value.
     * @param stream the stream
     * @param value the value to write
     * @throws IOException
     */
    private static void bulkAppendShort(DataOutputStream stream, Short value) throws IOException {
        stream.writeInt(2);
        stream.writeShort(value);
    }
    /**
     * Writes int value.
     * @param stream the stream
     * @param value the value to write
     * @throws IOException
     */
    private static void bulkAppendInteger(DataOutputStream stream, Integer value) throws IOException {
        stream.writeInt(4);
        stream.writeInt(value);
    }
    /**
     * Writes long value.
     * @param stream the stream
     * @param value the value to write
     * @throws IOException
     */
    private static void bulkAppendLong(DataOutputStream stream, Long value) throws IOException {
        stream.writeInt(8);
        stream.writeLong(value);
    }
    /**
     * Writes FP4 value.
     * @param stream the stream
     * @param value the value to write
     * @throws IOException
     */
    private static void bulkAppendFloat(DataOutputStream stream, Float value) throws IOException {
        stream.writeInt(4);
        stream.writeFloat(value);
    }
    /**
     * Writes FP8 value.
     * @param stream the stream
     * @param value the value to write
     * @throws IOException
     */
    private static void bulkAppendDouble(DataOutputStream stream, Double value) throws IOException {
        stream.writeInt(8);
        stream.writeDouble(value);
    }
    /**
     * Writes INET4 address value.
     * @param stream the stream
     * @param value the value to write
     * @throws IOException
     */
    private static void bulkAppendInet4Address(DataOutputStream stream, Inet4Address value) throws IOException {
        stream.writeInt(8);
        stream.writeByte(2); // INET4
        stream.writeByte(32); // number of bits in the mask
        stream.writeByte(0); // Is CIDR or not? 0 - no

        byte[] inet4AddressBytes = value.getAddress();

        stream.writeByte(inet4AddressBytes.length);
        stream.write(inet4AddressBytes);
    }
    /**
     * Writes INET4 address value.
     * @param stream the stream
     * @param value the value to write
     * @throws IOException
     */
    private static void bulkAppendInet6Address(DataOutputStream stream, Inet6Address value) throws IOException {
        stream.writeInt(20);
        stream.writeByte(3); // INET6
        stream.writeByte(128); // number of bits in the mask
        stream.writeByte(0); // Is CIDR or not? 0 - no

        byte[] inet6AddressBytes = value.getAddress();

        stream.writeByte(inet6AddressBytes.length);
        stream.write(inet6AddressBytes);
    }
    /**
     * Writes date value.
     * @param stream the stream
     * @param value the value to write
     * @throws IOException
     */
    private static void bulkAppendTimestamp(DataOutputStream stream, Date value) throws IOException {

        long millis = value.getTime() - POSTGRES_OFFSET_MILLIS;

        // Julian/Greagorian calendar cutoff point
        if (millis < -13165977600000L) { // October 15, 1582 -> October 4, 1582
            millis -= 86400 * 10000;
            if (millis < -15773356800000L) { // 1500-03-01 -> 1500-02-28
                int years = (int) ((millis + 15773356800000L) / -3155823050000L);
                years++;
                years -= years / 4;
                millis += years * 86400;
            }
        }

        stream.writeInt(8);
        stream.writeLong(TimeUnit.MILLISECONDS.toMicros(millis));
    }
    /**
     * Writes UUID value.
     * @param stream the stream
     * @param value the value to write
     * @throws IOException
     */
    private static void bulkAppendUUID(DataOutputStream stream, UUID value) throws IOException {

        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(value.getMostSignificantBits());
        bb.putLong(value.getLeastSignificantBits());

        stream.writeInt(16);
        stream.writeInt(bb.getInt(0));
        stream.writeShort(bb.getShort(4));
        stream.writeShort(bb.getShort(6));
        stream.write(Arrays.copyOfRange(bb.array(), 8, 16));
    }
    /**
     * Tries to unwrap tomcat pooling connection to be a postgres native one.
     * @param connection the pooling connection
     * @return native
     */
    private static PGConnection connectionUnwrap(Connection connection) {
        try {
            return connection.unwrap(PGConnection.class);
        } catch (SQLException e) {
            final String message = "Cannot unwrap pooling connection to PGConnection.";
            throw new PlatformFailureException(message, e, CoreExceptionIds.EX_SYSTEM_CONNECTION_UNWRAP);
        }
    }
    /**
     * parse constraint name from postgres exception
     * db constraint name start from uq_
     *
     * @param message
     * @return
     */
    public static String parseUniqueException(String message) {
        Pattern pattern = Pattern.compile("(uq_[a-zA-Z_]+)");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find())
        {
            return matcher.group(1);
        }
        return StringUtils.EMPTY;
    }

    /**
     * parse details from postgres exception
     *
     * @param message
     * @return
     */
    public static String parseExceptionDetails(String message) {
        Pattern pattern = Pattern.compile("(Detail:.+;)");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find())
        {
            return matcher.group(1);
        }
        return StringUtils.EMPTY;
    }

    /**
     * @author Mikhail Mikhailov
     * Want to close the underlaying stream upon finish.
     */
    public static class CopyDataOutputStream extends DataOutputStream {
        /**
         * The underlaying connection.
         */
        private PGConnection connection;
        /**
         * Execute after (connection must be in 'finished' unblocked state).
         */
        private Collection<String> executeAfter;
        /**
         * {@inheritDoc}
         */
        public CopyDataOutputStream(@WillClose OutputStream out) {
            super(out);
        }
        /**
         * Gets the PG copy stream.
         * @return stream
         */
        public OutputStream getWriterStream() {
            return this.out;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void close() {
            try {
                this.out.close();
            } catch (Exception e) {
                final String message = "Cannot close the underlaying PG COPY output stream.";
                throw new PlatformFailureException(message, e, CoreExceptionIds.EX_DATA_COPY_STREAM_CLOSE_FAILED);
            }

            if (CollectionUtils.isNotEmpty(this.executeAfter)) {
                @SuppressWarnings("resource")
                Connection standard = (Connection) this.connection;
                try (Statement s = standard.createStatement()) {

                    for (String afterSQL : this.executeAfter) {
                        s.executeUpdate(afterSQL);
                    }

                } catch (SQLException e) {
                    final String message = "Cannot execute AFTER statements: ";
                    throw new PlatformFailureException(message, e, CoreExceptionIds.EX_DATA_AFTER_BULK_SET_FAILED);
                }
            }
        }
    }
    /**
     * @author Mikhail Mikhailov
     * Needs BOS to write signature.
     */
    private static class CopyBufferedOutputStream extends BufferedOutputStream {
        /**
         * Constructor.
         * @param out
         */
        public CopyBufferedOutputStream(@WillClose OutputStream out) {
            super(out, 8192);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void close() throws IOException {
            super.out.close();
        }
    }
}
