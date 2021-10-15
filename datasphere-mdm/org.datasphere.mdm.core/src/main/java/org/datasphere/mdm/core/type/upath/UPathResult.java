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

import java.util.ArrayList;
import java.util.List;

import org.datasphere.mdm.core.type.data.Attribute;

/**
 * @author Mikhail Mikhailov
 * Simple upath result.
 */
public class UPathResult {
    /**
     * Collection mode.
     */
    private final UPathApplicationMode mode;
    /**
     * Attributes.
     */
    private final List<Attribute> attributes = new ArrayList<>(8);
    /**
     * Incomplete paths.
     */
    private final List<UPathIncompletePath> incomplete = new ArrayList<>(8);
    /**
     * Constructor.
     * @param mode the mode, which has been used while attribute collecting.
     */
    public UPathResult(UPathApplicationMode mode) {
        super();
        this.mode = mode;
    }
    /**
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }
    /**
     * @return the incomplete
     */
    public List<UPathIncompletePath> getIncomplete() {
        return incomplete;
    }
    /**
     * @return the mode
     */
    public UPathApplicationMode getMode() {
        return mode;
    }
    /**
     * Simple empty indicator.
     * @return true, if both records and attributes are empty
     */
    public boolean isEmpty() {
        return attributes.isEmpty() && incomplete.isEmpty();
    }
}
