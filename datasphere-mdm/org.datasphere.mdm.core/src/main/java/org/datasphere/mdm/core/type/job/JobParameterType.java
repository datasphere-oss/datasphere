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

package org.datasphere.mdm.core.type.job;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Denis Kostovarov
 * Supported value types.
 */
public enum JobParameterType {
    /**
     * Plain string.
     */
    STRING,
    /**
     * Same as string, but indicates potentially large volume, that should be displayed in a different control.
     */
    CLOB,
    /**
     * Boolean.
     */
    BOOLEAN,
    /**
     * 64 bit signed integer (long).
     */
    INTEGER,
    /**
     * 64 bit signed fp number (double).
     */
    NUMBER,
    /**
     * LocalDate.
     */
    DATE,
    /**
     * LocalTime.
     */
    TIME,
    /**
     * LocalDateTime.
     */
    TIMESTAMP,
    /**
     * Instant.
     */
    INSTANT,
    /**
     * A custom type, serialized to string.
     */
    CUSTOM;
    /**
     * Non-throwing, case insensetive valueOf(String).
     * @param v the string
     * @return {@linkplain JobParameterType} or null
     */
    @Nullable
    public static JobParameterType fromValue(final String v) {

        for (int i = 0; i < JobParameterType.values().length; i++) {

            final JobParameterType c = JobParameterType.values()[i];
            if (StringUtils.equalsIgnoreCase(v, c.name())) {
                return c;
            }

        }

        return null;
    }
}
