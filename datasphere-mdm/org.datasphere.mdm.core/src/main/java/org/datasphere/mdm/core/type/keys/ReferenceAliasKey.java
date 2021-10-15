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

package org.datasphere.mdm.core.type.keys;

/**
 * @author Mikhail Mikhailov
 * Key attribute alias key.
 */
public class ReferenceAliasKey {
    /**
     * Value.
     */
    private final String value;
    /**
     * Attribute name.
     */
    private final String entityAttributeName;
    /**
     * Constructor.
     * @param b builder
     */
    private ReferenceAliasKey(ReferenceAliasKeyBuilder b) {
        super();
        this.value = b.value;
        this.entityAttributeName = b.entityAttributeName;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the entityAttributeName
     */
    public String getEntityAttributeName() {
        return entityAttributeName;
    }

    @Override
	public String toString() {
		return new StringBuilder()
				.append("entityAttributeName = ")
				.append(entityAttributeName)
				.append("value = ")
				.append(value)
				.toString();
	}

	/**
     * @return new builder
     */
    public static ReferenceAliasKeyBuilder builder() {
        return new ReferenceAliasKeyBuilder();
    }
    /**
     * @author Mikhail Mikhailov
     * Builder.
     */
    public static class ReferenceAliasKeyBuilder {
        /**
         * Value.
         */
        private String value;
        /**
         * Attribute name.
         */
        private String entityAttributeName;

        /**
         * Constructor.
         */
        public ReferenceAliasKeyBuilder() {
            super();
        }

        /**
         * @param value the value to set
         */
        public ReferenceAliasKeyBuilder value(String value) {
            this.value = value;
            return this;
        }

        /**
         * @param entityAttributeName the entityAttributeName to set
         */
        public ReferenceAliasKeyBuilder entityAttributeName(String entityAttributeName) {
            this.entityAttributeName = entityAttributeName;
            return this;
        }

        /**
         * @return new alias key
         */
        public ReferenceAliasKey build() {
            return new ReferenceAliasKey(this);
        }
    }
}
