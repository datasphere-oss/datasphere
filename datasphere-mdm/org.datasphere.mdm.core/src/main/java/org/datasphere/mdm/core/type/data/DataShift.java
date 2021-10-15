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

package org.datasphere.mdm.core.type.data;

/**
 * @author Mikhail Mikhailov
 * Data shift. 
 * Versions, saved prior to any modifications which may be carried out by DQ, UE, etc. are marked PRISTINE.
 * Versions, saved after some modifications done by DQ, UE, etc. are marked REVISED.
 */
public enum DataShift {
    /**
     * Versions, saved prior to any modifications which may be carried out by DQ, UE, etc. are marked PRISTINE.
     */
    PRISTINE,
    /**
     * Versions, saved after some modifications done by DQ, UE, etc. are marked REVISED.
     */
    REVISED;
    /**
     * Convenient value method.
     * @return value / name.
     */
    public String value() {
        return name();
    }
    /**
     * Covenient from value wmethod.
     * @param v value / name
     * @return parsed value
     */
    public static DataShift fromValue(String v) {
        return valueOf(v);
    }
}
