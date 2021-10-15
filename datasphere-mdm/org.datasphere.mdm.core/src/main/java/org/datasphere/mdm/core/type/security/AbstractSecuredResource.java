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

package org.datasphere.mdm.core.type.security;

import java.io.Serializable;

/**
 * @author Denis Kostovarov
 */
public abstract class AbstractSecuredResource implements SecuredResource, Serializable {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = -4784151607289806225L;
    /**
     * Name.
     */
    private String name;
    /**
     * Display name.
     */
    private String displayName;
    /**
     * Type.
     */
    private SecuredResourceType type;
    /**
     * Category.
     */
    private String category;
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SecuredResourceType getType() {
        return type;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getCategory() {
        return category;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }
    /**
     * Sets name.
     * @param name the name
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Sets display name.
     * @param displayName the display name
     */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    /**
     * Sets type.
     * @param securedResourceType the type to set
     */
    public void setType(final SecuredResourceType securedResourceType) {
        this.type = securedResourceType;
    }
    /**
     * Sets a category.
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }
}
