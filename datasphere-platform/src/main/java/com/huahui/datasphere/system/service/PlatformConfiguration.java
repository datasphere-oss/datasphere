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

import com.huahui.datasphere.system.type.format.DumpTargetFormat;

/**
 * @author Mikhail Mikhailov
 * Some system-wide visible configuration fields.
 */
public interface PlatformConfiguration extends PlatformIdentitySource {
    /**
     * Major number.
     */
    int getPlatformMajor();
    /**
     * Minor number.
     */
    int getPlatformMinor();
    /**
     * Patch number.
     */
    int getPlatformPatch();
    /**
     * Revision number.
     */
    int getPlatformRevision();
    /**
     * Node ID.
     */
    String getNodeId();
    /**
     * The default dump target format.
     */
    DumpTargetFormat getDumpTargetFormat();
    /**
     * Tells the caller, whether the platform runs in developer mode.
     * @return true, if so, false otherwise
     */
    boolean isDeveloperMode();
}
