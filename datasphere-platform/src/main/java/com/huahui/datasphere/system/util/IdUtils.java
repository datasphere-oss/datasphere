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
package com.huahui.datasphere.system.util;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidata.mdm.system.configuration.SystemConfiguration;

import com.huahui.datasphere.system.service.PlatformIdentitySource;

/**
 * ID source.
 * @author Mikhail Mikhailov
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
