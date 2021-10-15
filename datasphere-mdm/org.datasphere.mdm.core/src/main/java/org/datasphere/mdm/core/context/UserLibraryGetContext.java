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

import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.system.context.StorageSpecificContext;

/**
 * @author Alexander Malyshev
 */
public class UserLibraryGetContext extends AbstractUserLibraryContext implements StorageSpecificContext {
    /**
     * Library's version
     */
    private final String version;
    /**
     * Fetch latest?
     */
    private final boolean latest;
    /**
     * Load current data view of the draft object.
     */
    private final boolean withData;
    /**
     * Constructor.
     * @param b the builder
     */
    private UserLibraryGetContext(final UserLibraryGetContextBuilder b) {
        super(b);
        this.version = b.version;
        this.latest = StringUtils.isBlank(b.version);
        this.withData = b.withData;
    }
    /**
     * Version field to filter for.
     * @return the version
     */
    public String getVersion() {
        return version;
    }
    /**
     * Filter just for latest version.
     * @return the latest
     */
    public boolean isLatest() {
        return latest;
    }
    /**
     * Return data (payload).
     * @return the withData
     */
    public boolean withData() {
        return withData;
    }
    /**
     * Builder method.
     * @return builder instance
     */
    public static UserLibraryGetContextBuilder builder() {
        return new UserLibraryGetContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Sep 16, 2020
     * Builder class.
     */
    public static class UserLibraryGetContextBuilder extends AbstractUserLibraryContextBuilder<UserLibraryGetContextBuilder> {
        /**
         * Library's version
         */
        private String version;
        /**
         * Load current data view of the draft object.
         */
        private boolean withData;
        /**
         * Constructor.
         */
        private UserLibraryGetContextBuilder() {
            super();
        }
        /**
         * The version.
         * @param version the version to filter for
         * @return self
         */
        public UserLibraryGetContextBuilder version(String version) {
            this.version = version;
            return self();
        }
        /**
         * Fetch data.
         * @param withData fetch data if true
         * @return self
         */
        public UserLibraryGetContextBuilder withData(boolean withData) {
            this.withData = withData;
            return self();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public UserLibraryGetContext build() {
            return new UserLibraryGetContext(this);
        }
    }
}
