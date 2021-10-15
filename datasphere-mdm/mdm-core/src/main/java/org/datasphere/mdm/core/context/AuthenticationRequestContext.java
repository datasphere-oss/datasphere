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

package com.huahui.datasphere.mdm.core.context;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.huahui.datasphere.mdm.system.context.CommonRequestContext;

import com.huahui.datasphere.mdm.core.type.security.AuthenticationSystemParameter;

/**
 * @author theseusyang
 * Auth context. Simple wrapper to allow proptrty string queries.
 */
public class AuthenticationRequestContext extends CommonRequestContext {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = 4766147798188522307L;
    /**
     * Authentication params.
     */
    private final Map<AuthenticationSystemParameter, Object> params;
    /**
     * Constructor.
     */
    private AuthenticationRequestContext(AuthenticationRequestContextBuilder b) {
        super(b);
        this.params = Objects.nonNull(b.params) ? b.params : Collections.emptyMap();
    }
    /**
     * @return the params
     */
    public Map<AuthenticationSystemParameter, Object> getParams() {
        return params;
    }
    /**
     * Builder.
     * @param params
     * @return
     */
    public static AuthenticationRequestContextBuilder builder(Map<AuthenticationSystemParameter, Object> params) {
        return new AuthenticationRequestContextBuilder()
                .params(params);
    }
    /**
     * Shortcut.
     * @param params
     * @return
     */
    public static AuthenticationRequestContext of(Map<AuthenticationSystemParameter, Object> params) {
        return new AuthenticationRequestContextBuilder()
                .params(params)
                .build();
    }
    /**
     * @author theseusyang
     * Builder.
     */
    public static class AuthenticationRequestContextBuilder extends CommonRequestContextBuilder<AuthenticationRequestContextBuilder> {
        /**
         * Authentication params.
         */
        private Map<AuthenticationSystemParameter, Object> params;
        /**
         * Constructor.
         */
        private AuthenticationRequestContextBuilder() {
            super();
        }
        /**
         * Sets params.
         * @param params
         * @return self
         */
        public AuthenticationRequestContextBuilder params(Map<AuthenticationSystemParameter, Object> params) {
            this.params = params;
            return this;
        }
        /**
         * Build this.
         * @return context
         */
        @Override
        public AuthenticationRequestContext build() {
            return new AuthenticationRequestContext(this);
        }
    }
}
