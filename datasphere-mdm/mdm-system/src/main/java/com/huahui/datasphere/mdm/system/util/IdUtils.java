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

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huahui.datasphere.mdm.system.configuration.SystemConfiguration;
import com.huahui.datasphere.mdm.system.service.PlatformIdentitySource;

/**
 * ID source.
 * @author theseusyang
 */
public class IdUtils {
    /**
     * Standard logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(IdUtils.class);
    /**
     * Configuration.
     */
    private static PlatformIdentitySource platformIdentitySource;
    /**
     * Constructor.
     */
    private IdUtils() {
        super();
    }
    /**
     * Convenient init method.
     * @param applicationContext
     */
    public static void init() {
        try {
            platformIdentitySource = SystemConfiguration.getBean(PlatformIdentitySource.class);
        } catch (Exception exc) {
            LOGGER.warn("Platform configuration bean GET. Exception caught.", exc);
        }
    }
    /**
     * Gets UUID v1 string.
     * @return string
     */
    public static String v1String() {
        return platformIdentitySource.v1IdString();
    }
    /**
     * Gets UUID v1.
     * @return {@link UUID}
     */
    public static UUID v1() {
        return platformIdentitySource.v1Id();
    }
    /**
     * Gets UUID v4 string.
     * @return string
     */
    public static String v4String() {
        return platformIdentitySource.v4IdString();
    }
    /**
     * Gets UUID v4.
     * @return {@link UUID}
     * @return
     */
    public static UUID v4() {
        return platformIdentitySource.v4Id();
    }
}
