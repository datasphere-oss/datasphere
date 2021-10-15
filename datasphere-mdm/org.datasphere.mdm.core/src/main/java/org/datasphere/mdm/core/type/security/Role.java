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
import java.util.List;

/**
 * @author Denis Kostovarov
 * A security role.
 */
public interface Role extends Serializable {
    /**
     * Gets the name of the role.
     * @return name
     */
    String getName();
    /**
     * Gets the type of the role (system or user defined).
     * @return type
     */
    RoleType getRoleType();
    /**
     * Gets rights, defined for this role.
     * @return rights
     */
    List<Right> getRights();
    /**
     * Gets the display name of the role.
     * @return display name
     */
    String getDisplayName();
    /**
     * Gets the security labels, defined for this role.
     * @return security labels
     */
    List<SecurityLabel> getSecurityLabels();
    /**
     * Gets collection of the custom properties, defined for this user.
     * @return collection of custom properties
     */
    List<CustomProperty> getProperties();

}
