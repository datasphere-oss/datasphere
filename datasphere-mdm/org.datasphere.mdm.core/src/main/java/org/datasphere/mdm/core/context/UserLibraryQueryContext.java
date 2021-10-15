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

import org.datasphere.mdm.core.type.libraries.LibraryMimeType;
import org.datasphere.mdm.system.context.StorageSpecificContext;

/**
 * @author Alexander Malyshev
 */
public class UserLibraryQueryContext extends AbstractUserLibraryContext implements StorageSpecificContext {
    /**
     * Fetch postional from.
     */
    private final Integer start;
    /**
     * Fetch postional count.
     */
    private final Integer limit;
    /**
     * Load current data view of the draft object.
     */
    private final boolean withData;
    /**
     * Filter by MIME type.
     */
    private final LibraryMimeType mimeType;
    /**
     * Fetch latest?
     */
    private final boolean latest;
    /**
     * Constructor.
     * @param b the builder
     */
    private UserLibraryQueryContext(final UserLibraryQueryContextBuilder b) {
        super(b);
        this.start = b.start;
        this.limit = b.limit;
        this.withData = b.withData;
        this.latest = b.latest;
        this.mimeType = b.mimeType;
    }
    /**
     * Gets postional from.
     * @return from position
     */
    public Integer getStart() {
        return start;
    }
    /**
     * Gets count to return.
     * @return count
     */
    public Integer getLimit() {
        return limit;
    }
    /**
     * Return data (payload).
     * @return the withData
     */
    public boolean withData() {
        return withData;
    }
    /**
     * @return the mimeType
     */
    public LibraryMimeType getMimeType() {
        return mimeType;
    }
    /**
     * Filter just for latest version.
     * @return the latest
     */
    public boolean isLatest() {
        return latest;
    }
    /**
     * Builder method.
     * @return builder instance
     */
    public static UserLibraryQueryContextBuilder builder() {
        return new UserLibraryQueryContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on Sep 16, 2020
     * Builder class.
     */
    public static class UserLibraryQueryContextBuilder extends AbstractUserLibraryContextBuilder<UserLibraryQueryContextBuilder> {
        /**
         * Filter by MIME type.
         */
        private LibraryMimeType mimeType;
        /**
         * Fetch postional from.
         */
        private Integer start;
        /**
         * Fetch postional count.
         */
        private Integer limit;
        /**
         * Load current data view of the draft object.
         */
        private boolean withData;
        /**
         * Fetch latest?
         */
        private boolean latest;
        /**
         * Constructor.
         */
        private UserLibraryQueryContextBuilder() {
            super();
        }
        /**
         * Start portion.
         * @param start the start point
         * @return self
         */
        public UserLibraryQueryContextBuilder start(Integer start) {
            this.start = start;
            return this;
        }
        /**
         * The count to return.
         * @param limit count to return
         * @return self
         */
        public UserLibraryQueryContextBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        /**
         * Fetch data.
         * @param withData fetch data if true
         * @return self
         */
        public UserLibraryQueryContextBuilder withData(boolean withData) {
            this.withData = withData;
            return this;
        }
        /**
         * Fetch latest.
         * @param latest fetch latest records if true
         * @return self
         */
        public UserLibraryQueryContextBuilder latest(boolean latest) {
            this.latest = latest;
            return this;
        }
        /**
         * The mimeType.
         * @param mimeType the mimeType to filter for
         * @return self
         */
        public UserLibraryQueryContextBuilder mimeType(LibraryMimeType mimeType) {
            this.mimeType = mimeType;
            return this;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public UserLibraryQueryContext build() {
            return new UserLibraryQueryContext(this);
        }
    }
}
