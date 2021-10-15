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

import org.datasphere.mdm.core.type.lob.LargeObjectAcceptance;

/**
 * @author Mikhail Mikhailov
 * Large value type.
 */
public interface LargeValue {
    /**
     * @author Mikhail Mikhailov
     * Large value type.
     */
    public enum ValueType {
        /**
         * Binary large value.
         */
        BLOB,
        /**
         * Charcter large value.
         */
        CLOB
    }
    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    String getId();
    /**
     * Gets the possibly supplied file name.
     * @return name
     */
    String getFileName();
    /**
     * Gets the possibly supplied MIME type.
     * @return MIME type
     */
    String getMimeType();
    /**
     * Gets the size in bytes.
     * @return size in bytes
     */
    long getSize();
    /**
     * Gets LOB value acceptance state.
     * @return LOB value acceptance state
     */
    LargeObjectAcceptance getAcceptance();
    /**
     * Gets the value type.
     * @return type
     */
    ValueType getValueType();
}
