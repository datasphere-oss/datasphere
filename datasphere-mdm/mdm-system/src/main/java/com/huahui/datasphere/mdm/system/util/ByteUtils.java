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

package com.huahui.datasphere.mdm.system.util;

import java.nio.ByteOrder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author theseusyang
 * Low level bits manipulations.
 */
public final class ByteUtils {
    /**
     * Constructor.
     */
    private ByteUtils() {
        super();
    }
    /**
     * Append int to buffer.
     * @param buf the buffer to append to
     * @param data the data
     * @param offset the offset in buffer
     */
    public static void packInt(byte[] buf, int data, int offset) {

        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            buf[offset] = (byte) (data >> 24);
            buf[offset + 1] = (byte) (data >> 16);
            buf[offset + 2] = (byte) (data >> 8);
            buf[offset + 3] = (byte) (data);
        } else {
            buf[offset] = (byte) (data);
            buf[offset + 1] = (byte) (data >> 8);
            buf[offset + 2] = (byte) (data >> 16);
            buf[offset + 3] = (byte) (data >> 24);
        }
    }
    /**
     * Append long to buffer.
     * @param buf the buffer to append to
     * @param data the data
     * @param offset the offset in buffer
     */
    public static void packLong(byte[] buf, long data, int offset) {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            buf[offset] = (byte) (data >> 56);
            buf[offset + 1] = (byte) (data >> 48);
            buf[offset + 2] = (byte) (data >> 40);
            buf[offset + 3] = (byte) (data >> 32);
            buf[offset + 4] = (byte) (data >> 24);
            buf[offset + 5] = (byte) (data >> 16);
            buf[offset + 6] = (byte) (data >> 8);
            buf[offset + 7] = (byte) (data);
        } else {
            buf[offset] = (byte) (data);
            buf[offset + 1] = (byte) (data >> 8);
            buf[offset + 2] = (byte) (data >> 16);
            buf[offset + 3] = (byte) (data >> 24);
            buf[offset + 4] = (byte) (data >> 32);
            buf[offset + 5] = (byte) (data >> 40);
            buf[offset + 6] = (byte) (data >> 48);
            buf[offset + 7] = (byte) (data >> 56);
        }
    }
    /**
     * Pack to byte buffer.
     * @param values integers to pack
     * @return byte array
     */
    public static byte[] packIntArray(int... values) {

        byte[] result = new byte[4 * values.length];
        for (int i = 0; i < values.length; i++) {
            packInt(result, values[i], i * 4);
        }

        return result;
    }
    /**
     * Pack to byte buffer.
     * @param values integers to pack
     * @return byte array
     */
    public static byte[] packLongArray(long... values) {

        byte[] result = new byte[8 * values.length];
        for (int i = 0; i < values.length; i++) {
            packLong(result, values[i], i * 8);
        }

        return result;
    }
    /**
     * Pack to byte buffer.
     * @param values integers to pack
     * @return byte array
     */
    public static byte[] packLocalDate(LocalDate date) {

        // Year + 2 octets for month and day
        byte[] result = new byte[6];
        packInt(result, date.getYear(), 0);

        result[4] = (byte) date.getMonthValue();
        result[5] = (byte) date.getDayOfMonth();

        return result;
    }
    /**
     * Pack to byte buffer.
     * @param values integers to pack
     * @return byte array
     */
    public static byte[] packLocalTime(LocalTime time) {

        // 3 octets for hours, minutes, seconds + nano of seconds
        byte[] result = new byte[7];
        packInt(result, time.getNano(), 0);

        result[4] = (byte) time.getHour();
        result[5] = (byte) time.getMinute();
        result[6] = (byte) time.getSecond();

        return result;
    }
    /**
     * Pack to byte buffer.
     * @param values integers to pack
     * @return byte array
     */
    public static byte[] packLocalDateTime(LocalDateTime timestamp) {

        // Year + nano of seconds + 2 octets for month and day + 3 octets for hours, minutes, seconds
        byte[] result = new byte[13];
        packInt(result, timestamp.getYear(), 0);
        packInt(result, timestamp.getNano(), 4);

        result[8] = (byte) timestamp.getMonthValue();
        result[9] = (byte) timestamp.getDayOfMonth();
        result[10] = (byte) timestamp.getHour();
        result[11] = (byte) timestamp.getMinute();
        result[12] = (byte) timestamp.getSecond();

        return result;
    }
    /**
     * Pack {@linkplain Instant} to BA.
     * @param instant the instant to pack
     * @return byte array
     */
    public static byte[] packInstant(Instant instant) {

        // seconds (b64) + nano (b32)
        byte[] result = new byte[12];

        packLong(result, instant.getEpochSecond(), 0);
        packInt(result, instant.getNano(), 8);

        return result;
    }
    /**
     * Extract integer from byte array.
     * @param buf the buffer
     * @param offset the offset
     * @return integer
     */
    public static int unpackInt(byte[] buf, int offset) {
        // Provoke AIOOB, in case of wrong pointer arithmetic.
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            return ((buf[offset] & 0xff) << 24) |
                   ((buf[offset + 1] & 0xff) << 16) |
                   ((buf[offset + 2] & 0xff) << 8) |
                    (buf[offset + 3] & 0xff);
        } else {
            return (buf[offset] & 0xff) |
                  ((buf[offset + 1]  & 0xff) << 8) |
                  ((buf[offset + 2]  & 0xff) << 16) |
                  ((buf[offset + 3]  & 0xff) << 24);
        }
    }
    /**
     * Extract integer from byte array.
     * @param buf the buffer
     * @param offset the offset
     * @return integer
     */
    public static long unpackLong(byte[] buf, int offset) {
        // Provoke AIOOB, in case of wrong pointer arithmetic.
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            return  ((long) (buf[offset] & 0xff) << 56) |
                    ((long) (buf[offset + 1] & 0xff) << 48) |
                    ((long) (buf[offset + 2] & 0xff) << 40) |
                    ((long) (buf[offset + 3] & 0xff) << 32) |
                    ((long) (buf[offset + 4] & 0xff) << 24) |
                    ((long) (buf[offset + 5] & 0xff) << 16) |
                    ((long) (buf[offset + 6] & 0xff) << 8) |
                    (buf[offset + 7]  & 0xff);
        } else {
            return  ((long) buf[offset] & 0xff) |
                    ((long) (buf[offset + 1] & 0xff) << 8) |
                    ((long) (buf[offset + 2] & 0xff) << 16) |
                    ((long) (buf[offset + 3] & 0xff) << 24) |
                    ((long) (buf[offset + 4] & 0xff) << 32) |
                    ((long) (buf[offset + 5] & 0xff) << 40) |
                    ((long) (buf[offset + 6] & 0xff) << 48) |
                    ((long) (buf[offset + 7] & 0xff) << 56);
        }
    }
    /**
     * Unpack a numer of integers from byte array.
     * @param buf the buffer
     * @param count count of packed integers
     * @return integer array
     */
    public static int[] unpackIntArray(byte[] buf, int count) {

        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            result[i] = unpackInt(buf, i * 4);
        }

        return result;
    }
    /**
     * Unpack a numer of long integers from byte array.
     * @param buf the buffer
     * @param count count of packed long integers
     * @return long integer array
     */
    public static long[] unpackLongArray(byte[] buf, int count) {

        long[] result = new long[count];
        for (int i = 0; i < count; i++) {
            result[i] = unpackLong(buf, i * 8);
        }

        return result;
    }
    /**
     * Unpack a local date from byte array.
     * @param buf the buffer
     * @return integer array
     */
    public static LocalDate unpackLocalDate(byte[] buf) {

        int year = unpackInt(buf, 0);
        int month = (buf[4] & 0xff);
        int day = (buf[5] & 0xff);

        return LocalDate.of(year, month, day);
    }
    /**
     * Unpack a local date from byte array.
     * @param buf the buffer
     * @return integer array
     */
    public static LocalTime unpackLocalTime(byte[] buf) {

        int nanos = unpackInt(buf, 0);
        int hour = (buf[4] & 0xff);
        int minute = (buf[5] & 0xff);
        int second = (buf[6] & 0xff);

        return LocalTime.of(hour, minute, second, nanos);
    }
    /**
     * Unpack a local date from byte array.
     * @param buf the buffer
     * @return integer array
     */
    public static LocalDateTime unpackLocalDateTime(byte[] buf) {

        int year = unpackInt(buf, 0);
        int nanos = unpackInt(buf, 4);
        int month = (buf[8] & 0xff);
        int day = (buf[9] & 0xff);
        int hour = (buf[10] & 0xff);
        int minute = (buf[11] & 0xff);
        int second = (buf[12] & 0xff);

        return LocalDateTime.of(year, month, day, hour, minute, second, nanos);
    }
    /**
     * Unpack an instant from byte array.
     * @param buf the buffer
     * @return instant
     */
    public static Instant unpackInstant(byte[] buf) {

        long seconds = unpackLong(buf, 0);
        long nanos = unpackInt(buf, 8);

        return Instant.ofEpochSecond(seconds, nanos);
    }
}
