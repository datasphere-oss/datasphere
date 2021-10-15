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

package org.datasphere.mdm.core.service.impl.upath;

import org.datasphere.mdm.core.type.upath.UPathElement;
import org.datasphere.mdm.core.type.upath.UPathElementType;

/**
 * @author Mikhail Mikhailov
 * UPath element base.
 */
public abstract class AbstractElementImpl implements UPathElement {
    /**
     * Path element.
     */
    private final String element;
    /**
     * Type of element.
     */
    private final UPathElementType type;
    /**
     * Constructor.
     * @param element the original element
     * @param type element type {@link UPathElementType}.
     */
    protected AbstractElementImpl(String element, UPathElementType type) {
        super();
        this.element = element;
        this.type = type;
    }
    /**
     * @return the element
     */
    @Override
    public String getElement() {
        return element;
    }
    /**
     * @return the type
     */
    @Override
    public UPathElementType getType() {
        return type;
    }
}