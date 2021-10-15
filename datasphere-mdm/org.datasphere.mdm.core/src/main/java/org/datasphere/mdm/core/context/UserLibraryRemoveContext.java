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
public class UserLibraryRemoveContext extends AbstractUserLibraryContext implements StorageSpecificContext {
    /**
     * Library's version
     */
    private final String version;
    /**
     * Constructor.
     * @param b the builder
     */
    private UserLibraryRemoveContext(final UserLibraryRemoveContextBuilder b) {
        super(b);
        this.version = b.version;
    }
    /**
     * Version field to filter for.
     * @return the version
     */
    public String getVersion() {
        return version;
    }
    /**
     * Builder method.
     * @return builder instance
     */
    public static UserLibraryRemoveContextBuilder builder() {
        return new UserLibraryRemoveContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Sep 16, 2020
     * Builder class.
     */
    public static class UserLibraryRemoveContextBuilder extends AbstractUserLibraryContextBuilder<UserLibraryRemoveContextBuilder> {
        /**
         * Library's version
         */
        private String version;
        /**
         * Constructor.
         */
        private UserLibraryRemoveContextBuilder() {
            super();
        }
        /**
         * The version.
         * @param version the version to filter for
         * @return self
         */
        public UserLibraryRemoveContextBuilder version(String version) {
            this.version = version;
            return self();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public UserLibraryRemoveContext build() {
            return new UserLibraryRemoveContext(this);
        }
    }
}
