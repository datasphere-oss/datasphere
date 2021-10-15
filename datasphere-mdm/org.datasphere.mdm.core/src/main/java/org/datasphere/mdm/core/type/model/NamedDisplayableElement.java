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

package org.datasphere.mdm.core.type.model;

/**
 * @author Mikhail Mikhailov
 * Short info about top level elements.
 */
public interface NamedDisplayableElement {
    /**
     * Gets the entity's name.
     * @return name
     */
    String getName();
    /**
     * Gets the entity's display name.
     * @return display name
     */
    String getDisplayName();
    /**
     * Gets a possibly defined description of the element.
     * @return description or null
     */
    String getDescription();
}
