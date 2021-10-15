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

package org.datasphere.mdm.core.serialization.protostuff;

/**
 * @author Mikhail Mikhailov
 * Schema fields. New enum fields must be added to the end ONLY! Gap is 20.
 */
public enum CoreSchemaFields {
    // 10 - Simple attributes.
    /**
     * Data record.
     */
    DATA_RECORD(CoreFieldValues.DATA_RECORD_VAL, CoreFieldValues.DATA_RECORD_TAG),
    /**
     * DataBundle.
     */
    DATA_BUNDLE(CoreFieldValues.DATA_BUNDLE_VAL, CoreFieldValues.DATA_BUNDLE_TAG),
    /**
     * Bundles array.
     */
    BUNDLES_ARRAY(CoreFieldValues.BUNDLES_ARRAY_VAL, CoreFieldValues.BUNDLES_ARRAY_TAG),
    /**
     * String simple attribute.
     */
    STRING_SIMPLE_ATTRIBUTE(CoreFieldValues.STRING_SIMPLE_ATTRIBUTE_VAL, CoreFieldValues.STRING_SIMPLE_ATTRIBUTE_TAG),
    /**
     * Integer simple attribute.
     */
    INTEGER_SIMPLE_ATTRIBUTE(CoreFieldValues.INTEGER_SIMPLE_ATTRIBUTE_VAL, CoreFieldValues.INTEGER_SIMPLE_ATTRIBUTE_TAG),
    /**
     * Number simple attribute.
     */
    NUMBER_SIMPLE_ATTRIBUTE(CoreFieldValues.NUMBER_SIMPLE_ATTRIBUTE_VAL, CoreFieldValues.NUMBER_SIMPLE_ATTRIBUTE_TAG),
    /**
     * Boolean simple attribute.
     */
    BOOLEAN_SIMPLE_ATTRIBUTE(CoreFieldValues.BOOLEAN_SIMPLE_ATTRIBUTE_VAL, CoreFieldValues.BOOLEAN_SIMPLE_ATTRIBUTE_TAG),
    /**
     * Blob simple attribute.
     */
    BLOB_SIMPLE_ATTRIBUTE(CoreFieldValues.BLOB_SIMPLE_ATTRIBUTE_VAL, CoreFieldValues.BLOB_SIMPLE_ATTRIBUTE_TAG),
    /**
     * Clob simple attribute.
     */
    CLOB_SIMPLE_ATTRIBUTE(CoreFieldValues.CLOB_SIMPLE_ATTRIBUTE_VAL, CoreFieldValues.CLOB_SIMPLE_ATTRIBUTE_TAG),
    /**
     * Date simple attribute.
     */
    DATE_SIMPLE_ATTRIBUTE(CoreFieldValues.DATE_SIMPLE_ATTRIBUTE_VAL, CoreFieldValues.DATE_SIMPLE_ATTRIBUTE_TAG),
    /**
     * Time simple attribute.
     */
    TIME_SIMPLE_ATTRIBUTE(CoreFieldValues.TIME_SIMPLE_ATTRIBUTE_VAL, CoreFieldValues.TIME_SIMPLE_ATTRIBUTE_TAG),
    /**
     * Timestamp simple attribute.
     */
    TIMESTAMP_SIMPLE_ATTRIBUTE(CoreFieldValues.TIMESTAMP_SIMPLE_ATTRIBUTE_VAL, CoreFieldValues.TIMESTAMP_SIMPLE_ATTRIBUTE_TAG),
    /**
     * Integer simple attribute.
     */
    MEASURED_SIMPLE_ATTRIBUTE(CoreFieldValues.MEASURED_SIMPLE_ATTRIBUTE_VAL, CoreFieldValues.MEASURED_SIMPLE_ATTRIBUTE_TAG),

    // 40 - Code attributes.
    /**
     * String code attribute.
     */
    STRING_CODE_ATTRIBUTE(CoreFieldValues.STRING_CODE_ATTRIBUTE_VAL, CoreFieldValues.STRING_CODE_ATTRIBUTE_TAG),
    /**
     * Integer code attribute.
     */
    INTEGER_CODE_ATTRIBUTE(CoreFieldValues.INTEGER_CODE_ATTRIBUTE_VAL, CoreFieldValues.INTEGER_CODE_ATTRIBUTE_TAG),

    // 60 -Array attributes
    /**
     * String array.
     */
    STRING_ARRAY_ATTRIBUTE(CoreFieldValues.STRING_ARRAY_ATTRIBUTE_VAL, CoreFieldValues.STRING_ARRAY_ATTRIBUTE_TAG),
    /**
     * Integer array.
     */
    INTEGER_ARRAY_ATTRIBUTE(CoreFieldValues.INTEGER_ARRAY_ATTRIBUTE_VAL, CoreFieldValues.INTEGER_ARRAY_ATTRIBUTE_TAG),
    /**
     * Number array.
     */
    NUMBER_ARRAY_ATTRIBUTE(CoreFieldValues.NUMBER_ARRAY_ATTRIBUTE_VAL, CoreFieldValues.NUMBER_ARRAY_ATTRIBUTE_TAG),
    /**
     * Date array.
     */
    DATE_ARRAY_ATTRIBUTE(CoreFieldValues.DATE_ARRAY_ATTRIBUTE_VAL, CoreFieldValues.DATE_ARRAY_ATTRIBUTE_TAG),
    /**
     * Time array.
     */
    TIME_ARRAY_ATTRIBUTE(CoreFieldValues.TIME_ARRAY_ATTRIBUTE_VAL, CoreFieldValues.TIME_ARRAY_ATTRIBUTE_TAG),
    /**
     * String array.
     */
    TIMESTAMP_ARRAY_ATTRIBUTE(CoreFieldValues.TIMESTAMP_ARRAY_ATTRIBUTE_VAL, CoreFieldValues.TIMESTAMP_ARRAY_ATTRIBUTE_TAG),

    // 80 - Complex
    /**
     * Complex.
     */
    COMPLEX_ATTRIBUTE(CoreFieldValues.COMPLEX_ATTRIBUTE_VAL, CoreFieldValues.COMPLEX_ATTRIBUTE_TAG),

    // 100 - Fields
    /**
     * Name field.
     */
    FIELD_NAME(CoreFieldValues.FIELD_NAME_VAL, CoreFieldValues.FIELD_NAME_TAG),

    // 120 - Values
    /**
     * Long value.
     */
    INTEGER_VALUE(CoreFieldValues.INTEGER_VALUE_VAL, CoreFieldValues.INTEGER_VALUE_TAG),
    /**
     * Double value.
     */
    NUMBER_VALUE(CoreFieldValues.NUMBER_VALUE_VAL, CoreFieldValues.NUMBER_VALUE_TAG),
    /**
     * Boolean value.
     */
    BOOLEAN_VALUE(CoreFieldValues.BOOLEAN_VALUE_VAL, CoreFieldValues.BOOLEAN_VALUE_TAG),
    /**
     * String value.
     */
    STRING_VALUE(CoreFieldValues.STRING_VALUE_VAL, CoreFieldValues.STRING_VALUE_TAG),
    /**
     * Blob value.
     */
    BLOB_VALUE(CoreFieldValues.BLOB_VALUE_VAL, CoreFieldValues.BLOB_VALUE_TAG),
    /**
     * Clob value.
     */
    CLOB_VALUE(CoreFieldValues.CLOB_VALUE_VAL, CoreFieldValues.CLOB_VALUE_TAG),
    /**
     * Date value.
     */
    DATE_VALUE(CoreFieldValues.DATE_VALUE_VAL, CoreFieldValues.DATE_VALUE_TAG),
    /**
     * Time value.
     */
    TIME_VALUE(CoreFieldValues.TIME_VALUE_VAL, CoreFieldValues.TIME_VALUE_TAG),
    /**
     * Timestamp value.
     */
    TIMESTAMP_VALUE(CoreFieldValues.TIMESTAMP_VALUE_VAL, CoreFieldValues.TIMESTAMP_VALUE_TAG),

    // 150 - Measured
    /**
     * Measured attribute category id.
     */
    MEASURED_CATEGORY_ID(CoreFieldValues.MEASURED_CATEGORY_ID_VAL, CoreFieldValues.MEASURED_CATEGORY_ID_TAG),
    /**
     * Measured attribute unit id.
     */
    MEASURED_UNIT_ID(CoreFieldValues.MEASURED_UNIT_ID_VAL, CoreFieldValues.MEASURED_UNIT_ID_TAG),
    /**
     * The initial value mark.
     */
    MEASURED_INITIAL(CoreFieldValues.MEASURED_INITIAL_VAL, CoreFieldValues.MEASURED_INITIAL_TAG),

    // 170 - LOB value
    /**
     * Large value ID.
     */
    LARGE_VALUE_ID(CoreFieldValues.LARGE_VALUE_ID_VAL, CoreFieldValues.LARGE_VALUE_ID_TAG),
    /**
     * Large value file name.
     */
    LARGE_VALUE_FILENAME(CoreFieldValues.LARGE_VALUE_FILENAME_VAL, CoreFieldValues.LARGE_VALUE_FILENAME_TAG),
    /**
     * Large value size.
     */
    LARGE_VALUE_SIZE(CoreFieldValues.LARGE_VALUE_SIZE_VAL, CoreFieldValues.LARGE_VALUE_SIZE_TAG),
    /**
     * Large value mime type.
     */
    LARGE_VALUE_MIME_TYPE(CoreFieldValues.LARGE_VALUE_MIME_TYPE_VAL, CoreFieldValues.LARGE_VALUE_MIME_TYPE_TAG),
    /**
     * Large value acceptance state.
     */
    LARGE_VALUE_ACCEPTANCE(CoreFieldValues.LARGE_VALUE_ACCEPTANCE_VAL, CoreFieldValues.LARGE_VALUE_ACCEPTANCE_TAG),

    // 190 - Array values
    /**
     * Long array value.
     */
    INTEGER_ARRAY_VALUE(CoreFieldValues.INTEGER_ARRAY_VALUE_VAL, CoreFieldValues.INTEGER_ARRAY_VALUE_TAG),
    /**
     * Double array value.
     */
    NUMBER_ARRAY_VALUE(CoreFieldValues.NUMBER_ARRAY_VALUE_VAL, CoreFieldValues.NUMBER_ARRAY_VALUE_TAG),
    /**
     * String array value.
     */
    STRING_ARRAY_VALUE(CoreFieldValues.STRING_ARRAY_VALUE_VAL, CoreFieldValues.STRING_ARRAY_VALUE_TAG),
    /**
     * Date array value.
     */
    DATE_ARRAY_VALUE(CoreFieldValues.DATE_ARRAY_VALUE_VAL, CoreFieldValues.DATE_ARRAY_VALUE_TAG),
    /**
     * Time array value.
     */
    TIME_ARRAY_VALUE(CoreFieldValues.TIME_ARRAY_VALUE_VAL, CoreFieldValues.TIME_ARRAY_VALUE_TAG),
    /**
     * Timestamp array value.
     */
    TIMESTAMP_ARRAY_VALUE(CoreFieldValues.TIMESTAMP_ARRAY_VALUE_VAL, CoreFieldValues.TIMESTAMP_ARRAY_VALUE_TAG);

    /**
     * Constructor.
     * @param field name of the field
     */
    private CoreSchemaFields(int value, String field) {
        this.field = field;
        this.value = value;
    }
    /**
     * Name of the field.
     */
    private final String field;
    /**
     * The numeric
     */
    private final int value;
    /**
     * @return the field
     */
    public String getField() {
        return field;
    }
    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }
    /**
     * String to int method.
     * @param s the string
     * @return integer
     */
    public static int stringToInt(String s) {

        switch (s) {
        // 1 - 49. Record.
        case CoreFieldValues.DATA_RECORD_TAG:
            return DATA_RECORD.getValue();
        case CoreFieldValues.DATA_BUNDLE_TAG:
            return DATA_BUNDLE.getValue();
        case CoreFieldValues.BUNDLES_ARRAY_TAG:
            return BUNDLES_ARRAY.getValue();

        // 50 - Simple attributes.
        case CoreFieldValues.STRING_SIMPLE_ATTRIBUTE_TAG:
            return STRING_SIMPLE_ATTRIBUTE.getValue();
        case CoreFieldValues.INTEGER_SIMPLE_ATTRIBUTE_TAG:
            return INTEGER_SIMPLE_ATTRIBUTE.getValue();
        case CoreFieldValues.NUMBER_SIMPLE_ATTRIBUTE_TAG:
            return NUMBER_SIMPLE_ATTRIBUTE.getValue();
        case CoreFieldValues.BOOLEAN_SIMPLE_ATTRIBUTE_TAG:
            return BOOLEAN_SIMPLE_ATTRIBUTE.getValue();
        case CoreFieldValues.BLOB_SIMPLE_ATTRIBUTE_TAG:
            return BLOB_SIMPLE_ATTRIBUTE.getValue();
        case CoreFieldValues.CLOB_SIMPLE_ATTRIBUTE_TAG:
            return CLOB_SIMPLE_ATTRIBUTE.getValue();
        case CoreFieldValues.DATE_SIMPLE_ATTRIBUTE_TAG:
            return DATE_SIMPLE_ATTRIBUTE.getValue();
        case CoreFieldValues.TIME_SIMPLE_ATTRIBUTE_TAG:
            return TIME_SIMPLE_ATTRIBUTE.getValue();
        case CoreFieldValues.TIMESTAMP_SIMPLE_ATTRIBUTE_TAG:
            return TIMESTAMP_SIMPLE_ATTRIBUTE.getValue();
        case CoreFieldValues.MEASURED_SIMPLE_ATTRIBUTE_TAG:
            return MEASURED_SIMPLE_ATTRIBUTE.getValue();

        // 100 - Code attributes.
        case CoreFieldValues.STRING_CODE_ATTRIBUTE_TAG:
            return STRING_CODE_ATTRIBUTE.getValue();
        case CoreFieldValues.INTEGER_CODE_ATTRIBUTE_TAG:
            return INTEGER_CODE_ATTRIBUTE.getValue();

        // 150 -Array attributes
        case CoreFieldValues.STRING_ARRAY_ATTRIBUTE_TAG:
            return STRING_ARRAY_ATTRIBUTE.getValue();
        case CoreFieldValues.INTEGER_ARRAY_ATTRIBUTE_TAG:
            return INTEGER_ARRAY_ATTRIBUTE.getValue();
        case CoreFieldValues.NUMBER_ARRAY_ATTRIBUTE_TAG:
            return NUMBER_ARRAY_ATTRIBUTE.getValue();
        case CoreFieldValues.DATE_ARRAY_ATTRIBUTE_TAG:
            return DATE_ARRAY_ATTRIBUTE.getValue();
        case CoreFieldValues.TIME_ARRAY_ATTRIBUTE_TAG:
            return TIME_ARRAY_ATTRIBUTE.getValue();
        case CoreFieldValues.TIMESTAMP_ARRAY_ATTRIBUTE_TAG:
            return TIMESTAMP_ARRAY_ATTRIBUTE.getValue();

        // 200 - Complex
        case CoreFieldValues.COMPLEX_ATTRIBUTE_TAG:
            return COMPLEX_ATTRIBUTE.getValue();

        // 250 - Fields
        case CoreFieldValues.FIELD_NAME_TAG:
            return FIELD_NAME.getValue();

        // 300 - Values
        case CoreFieldValues.INTEGER_VALUE_TAG:
            return INTEGER_VALUE.getValue();
        case CoreFieldValues.NUMBER_VALUE_TAG:
            return NUMBER_VALUE.getValue();
        case CoreFieldValues.BOOLEAN_VALUE_TAG:
            return BOOLEAN_VALUE.getValue();
        case CoreFieldValues.STRING_VALUE_TAG:
            return STRING_VALUE.getValue();
        case CoreFieldValues.BLOB_VALUE_TAG:
            return BLOB_VALUE.getValue();
        case CoreFieldValues.CLOB_VALUE_TAG:
            return CLOB_VALUE.getValue();
        case CoreFieldValues.DATE_VALUE_TAG:
            return DATE_VALUE.getValue();
        case CoreFieldValues.TIME_VALUE_TAG:
            return TIME_VALUE.getValue();
        case CoreFieldValues.TIMESTAMP_VALUE_TAG:
            return TIMESTAMP_VALUE.getValue();

        // 350 - Measured
        case CoreFieldValues.MEASURED_CATEGORY_ID_TAG:
            return MEASURED_CATEGORY_ID.getValue();
        case CoreFieldValues.MEASURED_UNIT_ID_TAG:
            return MEASURED_UNIT_ID.getValue();
        case CoreFieldValues.MEASURED_INITIAL_TAG:
            return MEASURED_INITIAL.getValue();

        // 400 - LOB value
        case CoreFieldValues.LARGE_VALUE_ID_TAG:
            return LARGE_VALUE_ID.getValue();
        case CoreFieldValues.LARGE_VALUE_FILENAME_TAG:
            return LARGE_VALUE_FILENAME.getValue();
        case CoreFieldValues.LARGE_VALUE_SIZE_TAG:
            return LARGE_VALUE_SIZE.getValue();
        case CoreFieldValues.LARGE_VALUE_MIME_TYPE_TAG:
            return LARGE_VALUE_MIME_TYPE.getValue();
        case CoreFieldValues.LARGE_VALUE_ACCEPTANCE_TAG:
            return LARGE_VALUE_ACCEPTANCE.getValue();

        // 450 - Array values
        case CoreFieldValues.INTEGER_ARRAY_VALUE_TAG:
            return INTEGER_ARRAY_VALUE.getValue();
        case CoreFieldValues.NUMBER_ARRAY_VALUE_TAG:
            return NUMBER_ARRAY_VALUE.getValue();
        case CoreFieldValues.STRING_ARRAY_VALUE_TAG:
            return STRING_ARRAY_VALUE.getValue();
        case CoreFieldValues.DATE_ARRAY_VALUE_TAG:
            return DATE_ARRAY_VALUE.getValue();
        case CoreFieldValues.TIME_ARRAY_VALUE_TAG:
            return TIME_ARRAY_VALUE.getValue();
        case CoreFieldValues.TIMESTAMP_ARRAY_VALUE_TAG:
            return TIMESTAMP_ARRAY_VALUE.getValue();
        default:
            break;
        }

        return 0;
    }

    /**
     * Int to string method.
     * @param s the it value
     * @return string
     */
    public static String intToString(int i) {

        switch (i) {
        // 1 - 49 - Record.
        case CoreFieldValues.DATA_RECORD_VAL:
            return DATA_RECORD.getField();
        case CoreFieldValues.DATA_BUNDLE_VAL:
            return DATA_BUNDLE.getField();
        case CoreFieldValues.BUNDLES_ARRAY_VAL:
            return BUNDLES_ARRAY.getField();

        // 50 - Simple attributes.
        case CoreFieldValues.STRING_SIMPLE_ATTRIBUTE_VAL:
            return STRING_SIMPLE_ATTRIBUTE.getField();
        case CoreFieldValues.INTEGER_SIMPLE_ATTRIBUTE_VAL:
            return INTEGER_SIMPLE_ATTRIBUTE.getField();
        case CoreFieldValues.NUMBER_SIMPLE_ATTRIBUTE_VAL:
            return NUMBER_SIMPLE_ATTRIBUTE.getField();
        case CoreFieldValues.BOOLEAN_SIMPLE_ATTRIBUTE_VAL:
            return BOOLEAN_SIMPLE_ATTRIBUTE.getField();
        case CoreFieldValues.BLOB_SIMPLE_ATTRIBUTE_VAL:
            return BLOB_SIMPLE_ATTRIBUTE.getField();
        case CoreFieldValues.CLOB_SIMPLE_ATTRIBUTE_VAL:
            return CLOB_SIMPLE_ATTRIBUTE.getField();
        case CoreFieldValues.DATE_SIMPLE_ATTRIBUTE_VAL:
            return DATE_SIMPLE_ATTRIBUTE.getField();
        case CoreFieldValues.TIME_SIMPLE_ATTRIBUTE_VAL:
            return TIME_SIMPLE_ATTRIBUTE.getField();
        case CoreFieldValues.TIMESTAMP_SIMPLE_ATTRIBUTE_VAL:
            return TIMESTAMP_SIMPLE_ATTRIBUTE.getField();
        case CoreFieldValues.MEASURED_SIMPLE_ATTRIBUTE_VAL:
            return MEASURED_SIMPLE_ATTRIBUTE.getField();

        // 100 - Code attributes.
        case CoreFieldValues.STRING_CODE_ATTRIBUTE_VAL:
            return STRING_CODE_ATTRIBUTE.getField();
        case CoreFieldValues.INTEGER_CODE_ATTRIBUTE_VAL:
            return INTEGER_CODE_ATTRIBUTE.getField();

        // 150 -Array attributes
        case CoreFieldValues.STRING_ARRAY_ATTRIBUTE_VAL:
            return STRING_ARRAY_ATTRIBUTE.getField();
        case CoreFieldValues.INTEGER_ARRAY_ATTRIBUTE_VAL:
            return INTEGER_ARRAY_ATTRIBUTE.getField();
        case CoreFieldValues.NUMBER_ARRAY_ATTRIBUTE_VAL:
            return NUMBER_ARRAY_ATTRIBUTE.getField();
        case CoreFieldValues.DATE_ARRAY_ATTRIBUTE_VAL:
            return DATE_ARRAY_ATTRIBUTE.getField();
        case CoreFieldValues.TIME_ARRAY_ATTRIBUTE_VAL:
            return TIME_ARRAY_ATTRIBUTE.getField();
        case CoreFieldValues.TIMESTAMP_ARRAY_ATTRIBUTE_VAL:
            return TIMESTAMP_ARRAY_ATTRIBUTE.getField();

        // 200 - Complex
        case CoreFieldValues.COMPLEX_ATTRIBUTE_VAL:
            return COMPLEX_ATTRIBUTE.getField();

        // 250 - Fields
        case CoreFieldValues.FIELD_NAME_VAL:
            return FIELD_NAME.getField();

        // 300 - Values
        case CoreFieldValues.INTEGER_VALUE_VAL:
            return INTEGER_VALUE.getField();
        case CoreFieldValues.NUMBER_VALUE_VAL:
            return NUMBER_VALUE.getField();
        case CoreFieldValues.BOOLEAN_VALUE_VAL:
            return BOOLEAN_VALUE.getField();
        case CoreFieldValues.STRING_VALUE_VAL:
            return STRING_VALUE.getField();
        case CoreFieldValues.BLOB_VALUE_VAL:
            return BLOB_VALUE.getField();
        case CoreFieldValues.CLOB_VALUE_VAL:
            return CLOB_VALUE.getField();
        case CoreFieldValues.DATE_VALUE_VAL:
            return DATE_VALUE.getField();
        case CoreFieldValues.TIME_VALUE_VAL:
            return TIME_VALUE.getField();
        case CoreFieldValues.TIMESTAMP_VALUE_VAL:
            return TIMESTAMP_VALUE.getField();

        // 350 - Measured
        case CoreFieldValues.MEASURED_CATEGORY_ID_VAL:
            return MEASURED_CATEGORY_ID.getField();
        case CoreFieldValues.MEASURED_UNIT_ID_VAL:
            return MEASURED_UNIT_ID.getField();
        case CoreFieldValues.MEASURED_INITIAL_VAL:
            return MEASURED_INITIAL.getField();

        // 400 - LOB value
        case CoreFieldValues.LARGE_VALUE_ID_VAL:
            return LARGE_VALUE_ID.getField();
        case CoreFieldValues.LARGE_VALUE_FILENAME_VAL:
            return LARGE_VALUE_FILENAME.getField();
        case CoreFieldValues.LARGE_VALUE_SIZE_VAL:
            return LARGE_VALUE_SIZE.getField();
        case CoreFieldValues.LARGE_VALUE_MIME_TYPE_VAL:
            return LARGE_VALUE_MIME_TYPE.getField();
        case CoreFieldValues.LARGE_VALUE_ACCEPTANCE_VAL:
            return LARGE_VALUE_ACCEPTANCE.getField();

        // 450 - Array values
        case CoreFieldValues.INTEGER_ARRAY_VALUE_VAL:
            return INTEGER_ARRAY_VALUE.getField();
        case CoreFieldValues.NUMBER_ARRAY_VALUE_VAL:
            return NUMBER_ARRAY_VALUE.getField();
        case CoreFieldValues.STRING_ARRAY_VALUE_VAL:
            return STRING_ARRAY_VALUE.getField();
        case CoreFieldValues.DATE_ARRAY_VALUE_VAL:
            return DATE_ARRAY_VALUE.getField();
        case CoreFieldValues.TIME_ARRAY_VALUE_VAL:
            return TIME_ARRAY_VALUE.getField();
        case CoreFieldValues.TIMESTAMP_ARRAY_VALUE_VAL:
            return TIMESTAMP_ARRAY_VALUE.getField();
        default:
            break;
        }

        return null;
    }
}
