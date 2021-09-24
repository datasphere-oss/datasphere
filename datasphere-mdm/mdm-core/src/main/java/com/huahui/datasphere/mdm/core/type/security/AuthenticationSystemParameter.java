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

package com.huahui.datasphere.mdm.core.type.security;

/**
 * @author theseusyang
 *         Keys for opaque authentication system parameters.
 */
public enum AuthenticationSystemParameter {
    /**
     * User name.
     */
    PARAM_USER_NAME,
    /**
     * Token.
     */
    PARAM_USER_TOKEN,
    /**
     * External token.
     */
    PARAM_EXTERNAL_TOKEN,
    /**
     * User password.
     */
    PARAM_USER_PASSWORD,
    /**
     * User locale.
     */
    PARAM_USER_LOCALE,
    /**
     * Request
     */
    PARAM_HTTP_SERVLET_REQUEST,
    /**
     * Client ip
     */
    PARAM_CLIENT_IP,
    /**
     * Server ip
     */
    PARAM_SERVER_IP,
    /**
     * Endpoint
     */
    PARAM_ENDPOINT,
    /**
     * Details
     */
    PARAM_DETAILS
}
