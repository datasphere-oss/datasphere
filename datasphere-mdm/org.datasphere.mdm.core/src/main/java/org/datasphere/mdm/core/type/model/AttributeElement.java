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

package org.datasphere.mdm.core.type.model;

import java.util.List;

import org.datasphere.mdm.search.type.FieldType;
import org.datasphere.mdm.search.type.IndexField;

/**
 * @author Mikhail Mikhailov
 * Holds attributes information.
 */
public interface AttributeElement extends NamedDisplayableElement, CustomPropertiesElement, OrderedElement  {
    /**
     * @author Mikhail Mikhailov
     * Type of data, which an attribute - simple, code or array so far - can hold.
     */
    public enum AttributeValueType {
        /**
         * Local date.
         */
        DATE("Date"),
        /**
         * Local time.
         */
        TIME("Time"),
        /**
         * Local TS.
         */
        TIMESTAMP("Timestamp"),
        /**
         * String.
         */
        STRING("String"),
        /**
         * Integer.
         */
        INTEGER("Integer"),
        /**
         * FP number.
         */
        NUMBER("Number"),
        /**
         * Boolean.
         */
        BOOLEAN("Boolean"),
        /**
         * Blob.
         */
        BLOB("Blob"),
        /**
         * Clob.
         */
        CLOB("Clob"),
        /**
         * Measured attribute init value.
         */
        MEASURED("Measured"),
        /**
         * Any of the above (used by CF).
         */
        ANY("Any"),
        /**
         * Special value, meaning "this attribute holds no data", as it is the case by complex attribute.
         */
        NONE("None");
        /**
         * Constructor.
         * @param value the JAXB friendly value
         */
        private AttributeValueType(String value) {
            this.value = value;
        }
        /**
         * JAXB friendly value.
         */
        private final String value;
        /**
         * Gets the JAXB friendly value.
         * @return
         */
        public String value() {
            return value;
        }
        /**
         * Creates instance from JAXB friendly value
         * @param v the value
         * @return enum instamce
         */
        public static AttributeValueType fromValue(String v) {

            for (AttributeValueType c: AttributeValueType.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }

            throw new IllegalArgumentException(v);
        }
        /**
         * Converts self to search type.
         * @return self as search type
         */
        public FieldType toSearchType() {

            switch (this) {
            case BLOB:
            case CLOB:
            case STRING:
                return FieldType.STRING;
            case BOOLEAN:
                return FieldType.BOOLEAN;
            case DATE:
                return FieldType.DATE;
            case TIME:
                return FieldType.TIME;
            case TIMESTAMP:
                return FieldType.TIMESTAMP;
            case INTEGER:
                return FieldType.INTEGER;
            case MEASURED:
            case NUMBER:
                return FieldType.NUMBER;
            case ANY:
                return FieldType.ANY;
            default:
                break;
            }

            return null;
        }
    }
    /**
     * Gets the value type, hold by this attribute.
     * @return value type
     */
    AttributeValueType getValueType();
    /**
     * Checks for being a measured attribute.
     * @return true for measured, false otherwise
     */
    boolean isMeasured();
    /**
     * Gets measure settings view of the attribute data.
     * @return settings or null, if the attribute is not the measured one
     */
    MeasuredValueElement getMeasured();
    /**
     * Checks for being a complex attribute.
     * @return true for complex, false otherwise
     */
    boolean isComplex();
    /**
     * Gets the complex view of the attribute data.
     * @return complex or null, if the attribute is not the complex one
     */
    ComplexElement getComplex();
    /**
     * Returns true, if the code attribute is a generating one (i. e. its values come from a supplied generating strategy).
     * @return true for attributes with value generation support, false otherwise
     */
    boolean isGenerating();
    /**
     * Gets generating element, if this attribute is a generating one
     * @return generating element
     */
    GeneratingElement getGenerating();
    /**
     * Tells whether this attribute is an indexed one.
     * If true, a call to {@link #getIndexed()} returns basic indexing information about this attribute.
     * @return true, if indexed, false otherwise
     */
    boolean isIndexed();
    /**
     * If this attribute element is an indexed one, returns basic indexing information
     * about this attribute. Otherwise returns null.
     * @return basic indexing info or null
     */
    IndexField getIndexed();
    /**
     * Returns true, if the attribute has indexing params.
     * @return true, if has indexing params, false otherwise
     */
    boolean hasIndexingParams();
    /**
     * Gets the indexing params view, if the attribute {@linkplain #hasIndexingParams()}.
     * @return indexing params view or null
     */
    IndexingParamsElement getIndexingParams();
    /**
     * Checks for being a lookup link attribute.
     * @return true for lookup link, false otherwise
     */
    boolean isLookupLink();
    /**
     * Gets the lookup link view for this attribute
     * @return lookup link view, if this element is a lookup link, null otherwise
     */
    LookupLinkElement getLookupLink();
    /**
     * Checks for being a enum attribute.
     * @return true for enum, false otherwise
     */
    boolean isEnumValue();
    /**
     * Gets the enum name.
     * @return enum name
     */
    String getEnumName();
    /**
     * Checks for being a template attribute.
     * @return true for template, false otherwise
     */
    boolean isLinkTemplate();
    /**
     * Gets the template value.
     * @return template value
     */
    String getLinkTemplate();
    /**
     * Gets exchange separator.
     * @return separator
     */
    String getExchangeSeparator();
    /**
     * Gets the full path of this attribute.
     * @return the path
     */
    String getPath();
    /**
     * @return the entity
     */
    NamedDisplayableElement getContainer();
    /**
     * Gets the parent complex attribute if it exists.
     * @return the parent
     */
    AttributeElement getParent();
    /**
     * Gets its children attributes, if this attribute is a complex one.
     * @return the children
     */
    List<AttributeElement> getChildren();
    /**
     * Has parent or not.
     * @return true, if has
     */
    boolean hasParent();
    /**
     * Has children or not.
     * @return true, if has
     */
    boolean hasChildren();
    /**
     * Returns the type name
     * @return the type name
     */
    String getTypeName();
    /**
     * Gets the hierarchie level this attribute is on. Starts with 0.
     * @return the level
     */
    int getLevel();
    /**
     * Checks for being a simple attribute.
     * @return true for simple, false otherwise
     */
    boolean isSimple();
    /**
     * Checks for being a code attribute.
     * @return true for code, false otherwise
     */
    boolean isCode();
    /**
     * Checks for being a code alternative attribute.
     * @return true for codeAlternative, false otherwise
     */
    boolean isCodeAlternative();
    /**
     * Checks for being an array attribute.
     * @return true for array, false otherwise
     */
    boolean isArray();
    /**
     * Checks for being a dictionary attribute.
     * @return true for dictionary, false otherwise
     */
    boolean isDictionary();
    /**
     * Checks for being a BLOB attribute.
     * @return true for BLOB, false otherwise
     */
    boolean isBlob();
    /**
     * Checks for being a CLOB attribute.
     * @return true for CLOB, false otherwise
     */
    boolean isClob();
    /**
     * Checks for being a temporal attribute.
     * @return true for temporal, false otherwise
     */
    boolean isDate();
    /**
     * Checks for being a unique attribute.
     * @return true for unique, false otherwise
     */
    boolean isUnique();
    /**
     * Checks for being a nullable attribute.
     * @return true for nullable, false otherwise
     */
    boolean isNullable();
    /**
     * True for read - only attributes.
     * @return true for read - only attributes.
     */
    boolean isReadOnly();
    /**
     * True for hidden attributes.
     * @return true for hidden attributes.
     */
    boolean isHidden();
    /**
     * Checks for being a displayable attribute.
     * @return true, if displayable, false otherwise
     */
    boolean isDisplayable();
    /**
     * Checks for being a main displayable attribute.
     * @return true if main displayable, false otherwise
     */
    boolean isMainDisplayable();
    /**
     * Checks for participation in a path.
     * @return true if participates, false otherwise
     */
    boolean isOfPath(String path);
}