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

import java.util.List;
import java.util.Map;

public final class AuthAndRedirectRequestHandleResult extends AuthRequestHandleResult {

    private final RedirectRequestHandleResult redirectTokenCheckResult;

    private AuthAndRedirectRequestHandleResult(
            final RedirectRequestHandleResult redirectTokenCheckResult
    ) {
        this.redirectTokenCheckResult = redirectTokenCheckResult;
    }

    public static AuthAndRedirectRequestHandleResult get(
            final String location
    ) {
        return new AuthAndRedirectRequestHandleResult(RedirectRequestHandleResult.get(location));
    }

    public static AuthAndRedirectRequestHandleResult get(
            final String location,
            final Map<String, List<String>> headers
    ) {
        return new AuthAndRedirectRequestHandleResult(RedirectRequestHandleResult.get(location, headers));
    }

    public RedirectRequestHandleResult getRedirectTokenCheckResult() {
        return redirectTokenCheckResult;
    }
}
