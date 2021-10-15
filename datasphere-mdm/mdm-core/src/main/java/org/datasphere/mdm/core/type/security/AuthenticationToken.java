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

/**
 *
 */
package com.huahui.datasphere.mdm.core.type.security;

import java.util.Map;

/**
 * @author theseusyang
 * Authentication token.
 */
public interface AuthenticationToken {
    /**
     * @author mikhail
     * Additional params field names.
     */
    public enum SecurityParam {
        /**
         * Roles map, keyed with role name.
         */
        ROLES_MAP,
        /**
         * Rights map, keyed with resource names.
         */
        RIGHTS_MAP,
        /**
         * Labels map, keyed by resource name.
         */
        LABELS_MAP
    }
    /**
     * Gets user name.
     * @return user name
     */
    String getUserName();
    /**
     * Gets current authentication token.
     * @return the token
     */
    String getCurrentToken();
    /**
     * Gets additional parameters.
     * @return parameter map
     */
    Map<SecurityParam, Object> getSecurityParams();
    /**
     * Gets the user details.
     *
     * @return details
     */
    User getUserDetails();
}
