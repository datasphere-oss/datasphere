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

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * @author Mikhail Mikhailov
 * Collect crypto stuff in a single place.
 */
public class CryptUtils {

    private static final HashFunction MURMUR3_128 = Hashing.murmur3_128();

    private CryptUtils() {
        super();
    }

    public static String toMurmurString(String val) {
        return Objects.isNull(val) ? null : MURMUR3_128.hashString(val, StandardCharsets.UTF_8).toString();
    }

    public static String toMurmurString(Long val) {
        return Objects.isNull(val) ? null : MURMUR3_128.hashLong(val).toString();
    }

    public static String toMurmurString(byte[] val) {
        return Objects.isNull(val) ? null : MURMUR3_128.hashBytes(val).toString();
    }
}
