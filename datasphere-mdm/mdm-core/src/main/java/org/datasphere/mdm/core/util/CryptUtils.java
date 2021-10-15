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

package com.huahui.datasphere.mdm.core.util;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * @author theseusyang
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
