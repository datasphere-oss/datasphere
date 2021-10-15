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
package org.datasphere.mdm.core.type.libraries;

/**
 * @author Mikhail Mikhailov on Feb 1, 2021
 * UL updates listener.
 */
public interface UserLibrariesListener {
    /**
     * Library with the given properties was either updated or inserted.
     * @param storage the storage id
     * @param name the name
     * @param version the version
     */
    void libraryUpserted(String storage, String name, String version);
    /**
     * Library with the given properties was removed.
     * @param storage the storage id
     * @param name the name
     * @param version the version
     */
    void libraryRemoved(String storage, String name, String version);
}
