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

package com.huahui.datasphere.mdm.core.type.security.provider;

import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class RedirectRequestHandleResult implements RequestHandleResult {
    private final String location;
    private final Map<String, List<String>> headers = new HashMap<>();

    private RedirectRequestHandleResult(final String location) {
        this.location = location;
    }

    private RedirectRequestHandleResult(final String location, final Map<String, List<String>> headers) {
        this.location = location;
        if (MapUtils.isNotEmpty(headers)) {
            this.headers.putAll(headers);
        }
    }

    public static RedirectRequestHandleResult get(final String location) {
        return new RedirectRequestHandleResult(location);
    }

    public static RedirectRequestHandleResult get(final String location, final Map<String, List<String>> headers) {
        return new RedirectRequestHandleResult(location, headers);
    }

    public String getLocation() {
        return location;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RedirectRequestHandleResult that = (RedirectRequestHandleResult) o;
        return Objects.equals(location, that.location) &&
                Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, headers);
    }
}
