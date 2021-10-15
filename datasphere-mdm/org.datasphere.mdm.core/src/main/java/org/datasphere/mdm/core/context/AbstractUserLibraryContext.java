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
package org.datasphere.mdm.core.context;

import org.datasphere.mdm.system.context.StorageSpecificContext;

/**
 * @author Alexander Malyshev
 */
public class AbstractUserLibraryContext implements StorageSpecificContext {
    /**
     * The storage id.
     */
    private final String storageId;
    /**
     * File name.
     */
    private final String filename;
    /**
     * Constructor.
     * @param b the builder
     */
    protected AbstractUserLibraryContext(final AbstractUserLibraryContextBuilder<?> b) {
        super();
        this.storageId = b.storageId;
        this.filename = b.filename;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getStorageId() {
        return storageId;
    }
    /**
     * Filename field to filter for.
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }
    /**
     * @author Mikhail Mikhailov on Sep 16, 2020
     * Builder class.
     */
    public abstract static class AbstractUserLibraryContextBuilder<X extends AbstractUserLibraryContextBuilder<X>> {
        /**
         * The storage id.
         */
        private String storageId;
        /**
         * File name.
         */
        private String filename;
        /**
         * Constructor.
         */
        protected AbstractUserLibraryContextBuilder() {
            super();
        }
        /**
         * The storageId to use.
         * @param storageId the storage id to use
         * @return self
         */
        public X storageId(String storageId) {
            this.storageId = storageId;
            return self();
        }
        /**
         * The filename.
         * @param filename the filename
         * @return self
         */
        public X filename(String filename) {
            this.filename = filename;
            return self();
        }
        /**
         * Self to X cast.
         * @return self
         */
        @SuppressWarnings("unchecked")
        protected X self() {
            return (X) this;
        }
        /**
         * {@inheritDoc}
         */
        public abstract AbstractUserLibraryContext build();
    }
}
