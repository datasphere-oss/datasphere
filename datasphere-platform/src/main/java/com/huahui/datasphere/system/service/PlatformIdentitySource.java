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

package com.huahui.datasphere.system.service;

import java.util.UUID;

/**
 * @author Mikhail Mikhailov
 * Id generation routines.
 */
public interface PlatformIdentitySource {
    /**
     * Gets UUID v1 string.
     * @return string
     */
    String v1IdString();
    /**
     * Gets UUID v1.
     * @return {@link UUID}
     */
    UUID v1Id();
    /**
     * Gets UUID v4 string.
     * @return string
     */
    String v4IdString();
    /**
     * Gets UUID v4.
     * @return {@link UUID}
     * @return
     */
    UUID v4Id();
}
