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

/**
 *
 */
package org.datasphere.mdm.core.type.security;

import java.util.Map;

/**
 * @author Mikhail Mikhailov
 * Authentication token.
 */
public interface AuthenticationToken {
    /**
     * @author mikhail
     * Additional params field names.
     */
    public enum SecurityParam {
        /**
         * Roles map, keyed with role name.
         */
        ROLES_MAP,
        /**
         * Rights map, keyed with resource names.
         */
        RIGHTS_MAP,
        /**
         * Labels map, keyed by resource name.
         */
        LABELS_MAP
    }
    /**
     * Gets user name.
     * @return user name
     */
    String getUserName();
    /**
     * Gets current authentication token.
     * @return the token
     */
    String getCurrentToken();
    /**
     * Gets additional parameters.
     * @return parameter map
     */
    Map<SecurityParam, Object> getSecurityParams();
    /**
     * Gets the user details.
     *
     * @return details
     */
    User getUserDetails();
}
