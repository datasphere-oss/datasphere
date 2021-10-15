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

import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.type.data.DataRecord;

/**
 * @author Mikhail Mikhailov
 * Incomplete path - parent record with the missing attribute.
 */
public class UPathIncompletePath {
    /**
     * Parent record.
     */
    private final DataRecord parent;
    /**
     * Link to missing {@link UPathElement}.
     */
    private final UPathElement element;
    /**
     * Constructor.
     */
    public UPathIncompletePath(DataRecord parent, UPathElement element) {
        super();
        this.parent = parent;
        this.element = element;
    }
    /**
     * @return the parent
     */
    public DataRecord getParent() {
        return parent;
    }
    /**
     * @return the element
     */
    public UPathElement getElement() {
        return element;
    }
    /**
     * Generates local path from content.
     * @return local path
     */
    public String toLocalPath() {

        String parentPath = parent.toLocalPath();
        return StringUtils.isBlank(parentPath)
                ? element.getElement()
                : new StringBuilder()
                    .append(parentPath)
                    .append('.')
                    .append(element.getElement())
                    .toString();
    }
}
