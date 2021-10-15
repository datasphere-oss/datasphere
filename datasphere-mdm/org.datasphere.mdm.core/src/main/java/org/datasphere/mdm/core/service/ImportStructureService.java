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
package org.datasphere.mdm.core.service;

import java.nio.file.Path;
import java.util.List;

/**
 * @author maria.chistyakova
 * @since 26.04.2020
 */
public interface ImportStructureService<T> {

    /**
     * validate structure
     *
     * @param rootFolder
     * @param fileName
     * @return validation error messages
     */
    List<String> validateImportedStructure(Path rootFolder, String fileName);

    T prepareUploadData(Path rootFolder, Path pathToZipFile, boolean isOverride, String fileName, String id);

}
