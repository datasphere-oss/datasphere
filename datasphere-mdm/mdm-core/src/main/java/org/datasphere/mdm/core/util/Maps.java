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

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Immutable map builder class with null values support
 *
 * @author theseusyang
 */
public final class Maps {
    private Maps() {
    }
    public static <K, V> Map<K, V> of(K k1, V v1) {
        return buildMap(Pair.of(k1, v1));
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        return buildMap(Pair.of(k1, v1), Pair.of(k2, v2));
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return buildMap(Pair.of(k1, v1), Pair.of(k2, v2), Pair.of(k3, v3));
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return buildMap(Pair.of(k1, v1), Pair.of(k2, v2), Pair.of(k3, v3), Pair.of(k4, v4));
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return buildMap(Pair.of(k1, v1), Pair.of(k2, v2), Pair.of(k3, v3), Pair.of(k4, v4), Pair.of(k5, v5));
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return buildMap(
                Pair.of(k1, v1),
                Pair.of(k2, v2),
                Pair.of(k3, v3),
                Pair.of(k4, v4),
                Pair.of(k5, v5),
                Pair.of(k6, v6)
        );
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return buildMap(
                Pair.of(k1, v1),
                Pair.of(k2, v2),
                Pair.of(k3, v3),
                Pair.of(k4, v4),
                Pair.of(k5, v5),
                Pair.of(k6, v6),
                Pair.of(k7, v7)
        );
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return buildMap(
                Pair.of(k1, v1),
                Pair.of(k2, v2),
                Pair.of(k3, v3),
                Pair.of(k4, v4),
                Pair.of(k5, v5),
                Pair.of(k6, v6),
                Pair.of(k7, v7),
                Pair.of(k8, v8)
        );
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return buildMap(
                Pair.of(k1, v1),
                Pair.of(k2, v2),
                Pair.of(k3, v3),
                Pair.of(k4, v4),
                Pair.of(k5, v5),
                Pair.of(k6, v6),
                Pair.of(k7, v7),
                Pair.of(k8, v8),
                Pair.of(k9, v9)
        );
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return buildMap(
                Pair.of(k1, v1),
                Pair.of(k2, v2),
                Pair.of(k3, v3),
                Pair.of(k4, v4),
                Pair.of(k5, v5),
                Pair.of(k6, v6),
                Pair.of(k7, v7),
                Pair.of(k8, v8),
                Pair.of(k9, v9),
                Pair.of(k10, v10)
        );
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11) {
        return buildMap(
                Pair.of(k1, v1),
                Pair.of(k2, v2),
                Pair.of(k3, v3),
                Pair.of(k4, v4),
                Pair.of(k5, v5),
                Pair.of(k6, v6),
                Pair.of(k7, v7),
                Pair.of(k8, v8),
                Pair.of(k9, v9),
                Pair.of(k10, v10),
                Pair.of(k11, v11)
        );
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12) {
        return buildMap(
                Pair.of(k1, v1),
                Pair.of(k2, v2),
                Pair.of(k3, v3),
                Pair.of(k4, v4),
                Pair.of(k5, v5),
                Pair.of(k6, v6),
                Pair.of(k7, v7),
                Pair.of(k8, v8),
                Pair.of(k9, v9),
                Pair.of(k10, v10),
                Pair.of(k11, v11),
                Pair.of(k12, v12)
        );
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13) {
        return buildMap(
                Pair.of(k1, v1),
                Pair.of(k2, v2),
                Pair.of(k3, v3),
                Pair.of(k4, v4),
                Pair.of(k5, v5),
                Pair.of(k6, v6),
                Pair.of(k7, v7),
                Pair.of(k8, v8),
                Pair.of(k9, v9),
                Pair.of(k10, v10),
                Pair.of(k11, v11),
                Pair.of(k12, v12),
                Pair.of(k13, v13)
        );
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13, K k14, V v14) {
        return buildMap(
                Pair.of(k1, v1),
                Pair.of(k2, v2),
                Pair.of(k3, v3),
                Pair.of(k4, v4),
                Pair.of(k5, v5),
                Pair.of(k6, v6),
                Pair.of(k7, v7),
                Pair.of(k8, v8),
                Pair.of(k9, v9),
                Pair.of(k10, v10),
                Pair.of(k11, v11),
                Pair.of(k12, v12),
                Pair.of(k13, v13),
                Pair.of(k14, v14)
        );
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13, K k14, V v14, K k15, V v15) {
        return buildMap(
                Pair.of(k1, v1),
                Pair.of(k2, v2),
                Pair.of(k3, v3),
                Pair.of(k4, v4),
                Pair.of(k5, v5),
                Pair.of(k6, v6),
                Pair.of(k7, v7),
                Pair.of(k8, v8),
                Pair.of(k9, v9),
                Pair.of(k10, v10),
                Pair.of(k11, v11),
                Pair.of(k12, v12),
                Pair.of(k13, v13),
                Pair.of(k14, v14),
                Pair.of(k15, v15)
        );
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13, K k14, V v14, K k15, V v15, K k16, V v16) {
        return buildMap(
                Pair.of(k1, v1),
                Pair.of(k2, v2),
                Pair.of(k3, v3),
                Pair.of(k4, v4),
                Pair.of(k5, v5),
                Pair.of(k6, v6),
                Pair.of(k7, v7),
                Pair.of(k8, v8),
                Pair.of(k9, v9),
                Pair.of(k10, v10),
                Pair.of(k11, v11),
                Pair.of(k12, v12),
                Pair.of(k13, v13),
                Pair.of(k14, v14),
                Pair.of(k15, v15),
                Pair.of(k16, v16)
        );
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13, K k14, V v14, K k15, V v15, K k16, V v16, K k17, V v17) {
        return buildMap(
                Pair.of(k1, v1),
                Pair.of(k2, v2),
                Pair.of(k3, v3),
                Pair.of(k4, v4),
                Pair.of(k5, v5),
                Pair.of(k6, v6),
                Pair.of(k7, v7),
                Pair.of(k8, v8),
                Pair.of(k9, v9),
                Pair.of(k10, v10),
                Pair.of(k11, v11),
                Pair.of(k12, v12),
                Pair.of(k13, v13),
                Pair.of(k14, v14),
                Pair.of(k15, v15),
                Pair.of(k16, v16),
                Pair.of(k17, v17)
        );
    }

    @SafeVarargs
    private static <K, V> Map<K, V> buildMap(Pair<K, V> ... kvs) {
        final Map<K, V> map = new HashMap<>();
        for (Pair<K, V> kv : kvs) {
            map.put(kv.getKey(), kv.getValue());
        }
        return Collections.unmodifiableMap(map);
    }

    public static <U, K, V> U fold(Map<K, V> map, U identity, BiFunction<U, Map.Entry<K, V>, U> folder) {
        U result = identity;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            result = folder.apply(result, entry);
        }
        return result;
    }
}
