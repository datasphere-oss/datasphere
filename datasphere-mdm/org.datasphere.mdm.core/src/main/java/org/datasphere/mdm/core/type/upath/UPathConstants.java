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
 * Some UPath constants.
 */
public final class UPathConstants {
    /**
     * Constructor.
     */
    private UPathConstants() {
        super();
    }
    /**
     * Internal name of the root element
     */
    public static final String UPATH_ROOT_NAME = "{}";
    /**
     * The 'record' constant, used in expression evaluations.
     */
    public static final String UPATH_RECORD_NAME = "record";
    /**
     * Path separator char.
     */
    public static final char UPATH_SEPARATOR_CHAR = '.';
    /**
     * Start of the UPath filtering expression.
     */
    public static final char UPATH_EXPRESSION_START = '{';
    /**
     * End of the UPath filtering expression.
     */
    public static final char UPATH_EXPRESSION_END = '}';
    /**
     * Mid of the UPath filtering expression.
     */
    public static final char UPATH_EXPRESSION_MID = ':';
    /**
     * Start of the UPath subscript expression.
     */
    public static final char UPATH_SUBSCRIPT_START = '[';
    /**
     * End of the UPath subscript expression.
     */
    public static final char UPATH_SUBSCRIPT_END = ']';
    /**
     * UPath escape symbol.
     */
    public static final char UPATH_ESCAPE_CHAR = '\\';
}
