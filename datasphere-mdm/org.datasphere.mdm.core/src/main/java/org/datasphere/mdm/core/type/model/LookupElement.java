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
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Code attribute holder element.
 * @author Mikhail Mikhailov on Nov 7, 2019
 */
public interface LookupElement extends EntityElement {
    /**
     * Gets the code attribute.
     *
     * @return the code attribute
     */
    @Nonnull
    AttributeElement getCodeAttribute();
    /**
     * Gets code alternative attributes.
     *
     * @return attributes collection
     */
    @Nonnull
    Collection<AttributeElement> getCodeAliases();
    /**
     * Gets lookups, referenced from this lookup alone with atributes.
     *
     * @return lookups map.
     */
    @Nonnull
    Map<LookupElement, Set<AttributeElement>> getReferencedLookups();
    /**
     * Gets lookups, referencing this lookup alone with atributes.
     *
     * @return lookups map.
     */
    @Nonnull
    Map<LookupElement, Set<AttributeElement>> getReferencingLookups();
    /**
     * Gets registers, referencing this lookup.
     *
     * @return registers with attributes
     */
    @Nonnull
    Map<RegisterElement, Set<AttributeElement>> getReferencingRegisters();
    /**
     * Gets nested elements, referencing this lookup.
     *
     * @return nested elements with attributes
     */
    @Nonnull
    Map<NestedElement, Set<AttributeElement>> getReferencingNesteds();
    /**
     * Gets relation elements, referencing this lookup.
     *
     * @return relation elements with their attributes
     */
    @Nonnull
    Map<RelationElement, Set<AttributeElement>> getReferencingRelations();
}
