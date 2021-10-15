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

/**
 * @author Mikhail Mikhailov
 *         Keys for opaque authentication system parameters.
 */
public enum AuthenticationSystemParameter {
    /**
     * User name.
     */
    PARAM_USER_NAME,
    /**
     * Token.
     */
    PARAM_USER_TOKEN,
    /**
     * External token.
     */
    PARAM_EXTERNAL_TOKEN,
    /**
     * User password.
     */
    PARAM_USER_PASSWORD,
    /**
     * User locale.
     */
    PARAM_USER_LOCALE,
    /**
     * Request
     */
    PARAM_HTTP_SERVLET_REQUEST,
    /**
     * Client ip
     */
    PARAM_CLIENT_IP,
    /**
     * Server ip
     */
    PARAM_SERVER_IP,
    /**
     * Endpoint
     */
    PARAM_ENDPOINT,
    /**
     * Details
     */
    PARAM_DETAILS
}
