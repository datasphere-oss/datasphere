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

/**
 *
 */
package org.datasphere.mdm.core.type.data;

import java.util.Date;

/**
 * @author Mikhail Mikhailov
 * The validity range.
 */
public interface ValidityRange {
    /**
     * From date. May be null.
     * @return Date or null
     */
    Date getValidFrom();
    /**
     * To date. May be null.
     * @return Date or null
     */
    Date getValidTo();
    /**
     * Returns true, if the range is of the same 'from' value as the 'other'.
     * @param other the 'from; boundary
     * @return true, if the range is of the same 'from' value as the 'other'.
     */
    default boolean isSameFrom(Date other) {
        return getValidFrom() == null
                ? other == null
                : getValidFrom().equals(other);
    }
    /**
     * Returns true, if the range is of the same 'to' value as the 'other'.
     * @param other the 'to'  boundary
     * @return true, if the range is of the same 'to' value as the 'other'.
     */
    default boolean isSameTo(Date other) {
        return getValidTo() == null
                ? other == null
                : getValidTo().equals(other);
    }
    /**
     * Returns true for the same dates boundaries.
     * @param from the from
     * @param to the to
     * @return true for the same dates boundaries.
     */
    default boolean isSameRange(Date from, Date to) {
        return isSameFrom(from) && isSameTo(to);
    }
}
