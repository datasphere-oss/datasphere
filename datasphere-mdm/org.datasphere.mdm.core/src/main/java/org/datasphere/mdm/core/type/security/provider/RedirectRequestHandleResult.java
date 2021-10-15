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

package org.datasphere.mdm.core.type.security.provider;

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
