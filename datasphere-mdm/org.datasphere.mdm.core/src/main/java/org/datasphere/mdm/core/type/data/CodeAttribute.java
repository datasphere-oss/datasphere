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

package org.datasphere.mdm.core.type.data;

import java.util.List;

import org.datasphere.mdm.core.type.model.AttributeElement.AttributeValueType;
import org.datasphere.mdm.search.type.FieldType;

/**
 * @author Mikhail Mikhailov
 * Code attribute values.
 */
public interface CodeAttribute<T> extends SingleValueAttribute<T> {
    /**
     * @author Mikhail Mikhailov
     * Denotes type of the contained data.
     */
    enum CodeDataType {
        /**
         * The string type.
         */
        STRING,
        /**
         * The integer type (long 8 bytes).
         */
        INTEGER;
        /**
         * Converts self to search type.
         * @return self as search type
         */
        public FieldType toSearchType() {

            switch (this) {
            case STRING:
                return FieldType.STRING;
            case INTEGER:
                return FieldType.INTEGER;
            default:
                break;
            }

            return null;
        }
        /**
         * Converts self to model type.
         * @return self as search type
         */
        public AttributeValueType toModelType() {

            switch (this) {
            case STRING:
                return AttributeValueType.STRING;
            case INTEGER:
                return AttributeValueType.INTEGER;
            default:
                break;
            }

            return null;
        }
        /**
         * Converts model type to self .
         * @return model type as self
         */
        public static CodeDataType fromModelType(AttributeValueType type) {

            switch (type) {
            case STRING:
                return CodeDataType.STRING;
            case INTEGER:
                return CodeDataType.INTEGER;
            default:
                break;
            }

            return null;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    default AttributeType getAttributeType() {
        return AttributeType.CODE;
    }
    /**
     * Gets type of contained data.
     * @return type
     */
    CodeDataType getDataType();

    /**
     * Gets contained supplementary values.
     * @return values.
     */
    List<T> getSupplementary();
    /**
     * Gets contained supplementary values.
     * @return values.
     */
    @SuppressWarnings("unchecked")
    default<V> List<V> castSupplementary() {
        return (List<V>) getSupplementary();
    }
    /**
     * Sets the supplementary values.
     * @param value to set
     */
    void setSupplementary(List<T> value);
    /**
     * Tells, whether this attribute has supplementary values set.
     * @return true, if so, false otherwise
     */
    default boolean hasSupplementary() {
        return getSupplementary() != null && !getSupplementary().isEmpty();
    }
}
