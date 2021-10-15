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

package org.datasphere.mdm.core.type.upath;

/**
 * @author Mikhail Mikhailov
 * UPath element.
 */
public interface UPathElement {
    /**
     * Gets source element (input string).
     * @return the source element (input string)
     */
    String getElement();
    /**
     * Gets exact element type.
     * @return the exact element type
     */
    UPathElementType getType();
    /**
     * Gets true if filtering, false for collecting
     * @return true if filtering, false for collecting
     */
    default boolean isFiltering() {
        return false;
    }
    /**
     * Gets filter element view or null, if this is not a filtering element.
     * @return filter element view or null, if this is not a filtering element
     */
    default UPathFilterElement getFilter() {
        return null;
    }
    /**
     * Gets true if collecting, false for filtering
     * @return true if collecting, false for filtering
     */
    default boolean isCollecting() {
        return false;
    }
    /**
     * Gets collector element view or null, if this is not a filtering element.
     * @return collector element view or null, if this is not a filtering element
     */
    default UPathCollectorElement getCollector() {
        return null;
    }
}