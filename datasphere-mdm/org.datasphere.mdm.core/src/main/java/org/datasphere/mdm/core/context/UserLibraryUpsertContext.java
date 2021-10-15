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
package org.datasphere.mdm.core.context;

import java.io.InputStream;
import java.util.Objects;
import java.util.function.Supplier;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * @author Mikhail Mikhailov
 */
@NotThreadSafe
public class UserLibraryUpsertContext extends AbstractUserLibraryContext {
    /**
     * Library's version
     */
    private final String version;
    /**
     * MIME type.
     */
    private final String mimeType;
    /**
     * Description.
     */
    private final String description;
    /**
     * Editable mark.
     */
    private final boolean editable;
    /**
     * Input stream.
     */
    private final Supplier<InputStream> input;
    /**
     * Force upsert to overcome 'not updatable' state.
     */
    private final boolean force;
    /**
     * Constructor.
     */
    private UserLibraryUpsertContext(UserLibraryUpsertContextBuilder b) {
        super(b);
        this.version = b.version;
        this.description = b.description;
        this.editable = b.editable;
        this.input = Objects.nonNull(b.inputSupplier) ? b.inputSupplier : () -> b.inputStream;
        this.mimeType = b.mimeType;
        this.force = b.force;
    }
    /**
     * Version field to filter for.
     * @return the version
     */
    public String getVersion() {
        return version;
    }
    /**
     * @return the largeObjectId
     */
    public String getDescription() {
        return description;
    }
    /**
     * @return the inputStream
     */
    public Supplier<InputStream> getInput() {
        return input;
    }
    /**
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }
    /**
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }
    /**
     * @return the force
     */
    public boolean isForce() {
        return force;
    }
    /**
     * Symmetrically to all other contexts.
     * @return builder instance
     */
    public static UserLibraryUpsertContextBuilder builder() {
        return new UserLibraryUpsertContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov
     * Builder class.
     */
    public static class UserLibraryUpsertContextBuilder extends AbstractUserLibraryContextBuilder<UserLibraryUpsertContextBuilder> {
        /**
         * Library's version
         */
        private String version;
        /**
         * Mime type.
         */
        private String mimeType;
        /**
         * Description.
         */
        private String description;
        /**
         * Editable mark.
         * True by default.
         */
        private boolean editable = true;
        /**
         * Input stream supplier.
         */
        private Supplier<InputStream> inputSupplier;
        /**
         * Input stream.
         */
        private InputStream inputStream;
        /**
         * Force upsert to overcome 'not updatable' state.
         */
        private boolean force;
        /**
         * The version.
         * @param version the version to filter for
         * @return self
         */
        public UserLibraryUpsertContextBuilder version(String version) {
            this.version = version;
            return self();
        }
        /**
         * Sets the MIME type.
         * @param mimeType the mime type
         * @return self
         */
        public UserLibraryUpsertContextBuilder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }
        /**
         * Sets description.
         * @param description the description
         * @return self
         */
        public UserLibraryUpsertContextBuilder description(String description) {
            this.description = description;
            return this;
        }
        /**
         * Sets the editable flag.
         * @param editable the editable flag
         * @return self
         */
        public UserLibraryUpsertContextBuilder editable(boolean editable) {
            this.editable = editable;
            return this;
        }
        /**
         * Sets the input stream.
         * @param inputStream the input stream
         * @return self
         */
        public UserLibraryUpsertContextBuilder input(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }
        /**
         * Sets the force flag.
         * @param force the force flag
         * @return self
         */
        public UserLibraryUpsertContextBuilder force(boolean force) {
            this.force = force;
            return this;
        }
        /**
         * Sets an input stream supplier,
         * which may do some recovery in case of failure or run other activities,
         * related to underlying input.
         * @param inputStream the input stream
         * @return self
         */
        public UserLibraryUpsertContextBuilder input(Supplier<InputStream> inputStream) {
            this.inputSupplier = inputStream;
            return this;
        }
        /**
         * Builds the context.
         * @return new context
         */
        @Override
        public UserLibraryUpsertContext build() {
            return new UserLibraryUpsertContext(this);
        }
    }

}
