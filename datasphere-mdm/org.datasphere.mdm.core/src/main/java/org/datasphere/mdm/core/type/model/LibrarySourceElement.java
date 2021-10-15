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
 * @author Mikhail Mikhailov on Feb 16, 2021
 * Marks implementation as a library sourced element.
 */
public interface LibrarySourceElement {
    /**
     * Returns the name of the jar/zip/whatever file, that contains the implementing class.
     * @return the name of the jar/zip/whatever file, that contains the implementing class.
     */
    String getLibrary();
    /**
     * Version of the library.
     * @return version of the library.
     */
    String getVersion();
}
