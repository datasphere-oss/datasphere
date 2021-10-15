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

import java.util.UUID;

import org.datasphere.mdm.system.context.CommonRequestContext;

/**
 * @author Mikhail Mikhailov
 * Fetch large objects request context.
 */
public class FetchLargeObjectContext extends CommonRequestContext {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = -4782136881603082178L;
    /**
     * ID of the BLOB/CLOB record.
     */
    private final UUID largeObjectId;
    /**
     * Binary or character data.
     */
    private final boolean binary;
    /**
     * Gets the content directly from DB input stream without intermediate temp file.
     */
    private final boolean direct;
    /**
     * Constructor.
     */
    private FetchLargeObjectContext(FetchLargeObjectContextBuilder b) {
        super(b);
        this.largeObjectId = b.largeObjectId;
        this.binary = b.binary;
        this.direct = b.direct;
    }
    /**
     * @return the largeObjectId
     */
    public UUID getLargeObjectId() {
        return largeObjectId;
    }
    /**
     * @return the binary
     */
    public boolean isBinary() {
        return binary;
    }
    /**
     * @return the direct
     */
    public boolean isDirect() {
        return direct;
    }
    /**
     * Convenience builder method.
     * @return builder
     */
    public static FetchLargeObjectContextBuilder builder() {
        return new FetchLargeObjectContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov
     * Builder class.
     */
    public static class FetchLargeObjectContextBuilder extends CommonRequestContextBuilder<FetchLargeObjectContextBuilder> {
        /**
         * ID of the BLOB/CLOB record.
         */
        private UUID largeObjectId;
        /**
         * Binary or character data.
         */
        private boolean binary;
        /**
         * Gets the content directly from DB input stream without intermediate temp file.
         */
        private boolean direct;
        /**
         * Constructor.
         */
        private FetchLargeObjectContextBuilder() {
            super();
        }
        /**
         * Sets ID of the BLOB/CLOB record..
         * @param largeObjectId the key
         * @return self
         */
        public FetchLargeObjectContextBuilder largeObjectId(String largeObjectId) {
            return largeObjectId(UUID.fromString(largeObjectId));
        }
        /**
         * Sets ID of the BLOB/CLOB record..
         * @param largeObjectId the key
         * @return self
         */
        public FetchLargeObjectContextBuilder largeObjectId(UUID largeObjectId) {
            this.largeObjectId = largeObjectId;
            return this;
        }
        /**
         * Sets flag to return binary (or character) data.
         * @param binary the flag
         * @return self
         */
        public FetchLargeObjectContextBuilder binary(boolean binary) {
            this.binary = binary;
            return this;
        }
        /**
         * Sets flag to return the content directly from DB input stream without intermediate temp file.
         * @param direct the flag
         * @return self
         */
        public FetchLargeObjectContextBuilder direct(boolean direct) {
            this.direct = direct;
            return this;
        }
        /**
         * Builds the context.
         * @return new context
         */
        @Override
        public FetchLargeObjectContext build() {
            return new FetchLargeObjectContext(this);
        }
    }
}
