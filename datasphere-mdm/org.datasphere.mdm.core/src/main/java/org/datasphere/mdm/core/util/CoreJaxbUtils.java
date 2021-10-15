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

package org.datasphere.mdm.core.util;

import org.datasphere.mdm.system.util.AbstractJaxbUtils;

/**
 * @author Mikhail Mikhailov on Oct 6, 2019
 * Basic JAXB stuff.
 */
public final class CoreJaxbUtils extends AbstractJaxbUtils {

    private static final com.datasphere.mdm.security.ObjectFactory SECURITY_FACTORY =
            new com.datasphere.mdm.security.ObjectFactory();

    /**
     * Constructor.
     */
    private CoreJaxbUtils() {
        super();
    }

    public static com.datasphere.mdm.security.ObjectFactory getSecurityFactory() {
        return SECURITY_FACTORY;
    }
}
