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

package org.datasphere.mdm.core.dao.vendor;

/**
 * Pg data types.
 * @author Mikhail Mikhailov
 */
public enum VendorDataType {

    BOOLEAN(16),
    BYTEA(17),
    CHAR(18),
    INT8(20),
    INT2(21),
    INT4(23),
    TEXT(25),
    JSONB(114),
    REAL(700), // Float, 4 bytes
    DOUBLE(701),
    INET4(869),
    INET6(869),
    // CIDR(650),
    // UNKNOWN(705),
    // CASH(790),
    // MONEY(791),
    // MAC_ADDRESS(829),
    DATE(1082),
    TIMESTAMP(1114),
    UUID(2950)
    // POINT,
    // BOX,
    // LINE,
    // LINESEGMENT,
    // CIRCLE,
    // PATH,
    // POLYGON,
    // HSTORE,
    // NUMERIC
    ;
    /**
     * Type Oid,
     */
    private final int id;
    /**
     * Constructor.
     * @param id type id
     */
    private VendorDataType(int id) {
        this.id = id;
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
}
