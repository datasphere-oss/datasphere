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
package org.datasphere.mdm.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Mikhail Mikhailov
 * Deletes the temp file upon close.
 */
public class AutodeleteTempFileInputStream extends FileInputStream {
    /**
     * Temp file.
     */
    private File file;
    /**
     * Constructor.
     * @param name
     * @throws FileNotFoundException
     */
    public AutodeleteTempFileInputStream(String name) throws FileNotFoundException {
        this(new File(name));
    }
    /**
     * Constructor.
     * @param file
     * @throws FileNotFoundException
     */
    public AutodeleteTempFileInputStream(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (file != null) {
                Files.delete(file.toPath());
            }
        }
    }
}
