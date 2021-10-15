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

import java.util.Collection;

/**
 * @author Mikhail Mikhailov on Oct 21, 2020
 * Relations presentation info.
 */
public interface RelationPresentationBlockElement extends PresentationBlockElement {

    enum RelationTypeInfo {
        /**
         * "References".
         */
        REFERENCES,
        /**
         * "Contains"
         */
        CONTAINS,
        /**
         * "ManyToMany"
         */
        MANY_TO_MANY
    }

    /**
     * Gets relation names.
     * @return relation names
     */
    Collection<String> getRelations();
    /**
     * Gets type info.
     * @return type info
     */
    RelationTypeInfo getTypeInfo();
}
