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

import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * @author Mikhail Mikhailov on Oct 16, 2020
 * Relation access interface.
 */
public interface RelationElement extends EntityElement {
    /**
     * True, if relation is required.
     * @return true, if required.
     */
    boolean isRequired();
    /**
     * Relation is of type REFERENCE.
     * @return true for REFERENCE
     */
    boolean isReference();
    /**
     * Relation is of type CONTAINS.
     * @return true for CONTAINS
     */
    boolean isContainment();
    /**
     * Relation is of type M2M.
     * @return true for M2M
     */
    boolean isManyToMany();
    /**
     * Gets left (from) entity.
     * @return the left (from) entity
     */
    @Nonnull
    RegisterElement getLeft();
    /**
     * Gets the left entity presentation rules.
     * @return left presentation rules
     */
    @Nonnull
    ReferencePresentationElement getLeftPresentation();
    /**
     * Gets right (to) entity.
     * @return the right (to) entity
     */
    @Nonnull
    RegisterElement getRight();
    /**
     * Gets the right entity presentation rules.
     * @return right presentation rules
     */
    @Nonnull
    ReferencePresentationElement getRightPresentation();
    /**
     * Gets lookups, referenced from this register alone with atributes.
     *
     * @return lookups map.
     */
    @Nonnull
    Map<LookupElement, Set<AttributeElement>> getReferencedLookups();
}
