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

package com.huahui.datasphere.system.serialization.protostuff;

/**
 * @author Mikhail Mikhailov
 * Schema fields.
 */
public enum SystemSchemaFields {
    /**
     * Variables container.
     */
    VARIABLES(SystemFieldValues.VARIABLES_VAL, SystemFieldValues.VARIABLES_TAG),
    /**
     * String simple attribute.
     */
    STRING_VARIABLE(SystemFieldValues.STRING_VARIABLE_VAL, SystemFieldValues.STRING_VARIABLE_TAG),
    /**
     * Integer simple attribute.
     */
    INTEGER_VARIABLE(SystemFieldValues.INTEGER_VARIABLE_VAL, SystemFieldValues.INTEGER_VARIABLE_TAG),
    /**
     * Blob simple attribute.
     */
    LONG_VARIABLE(SystemFieldValues.LONG_VARIABLE_VAL, SystemFieldValues.LONG_VARIABLE_TAG),
    /**
     * Number simple attribute.
     */
    FLOAT_VARIABLE(SystemFieldValues.FLOAT_VARIABLE_VAL, SystemFieldValues.FLOAT_VARIABLE_TAG),
    /**
     * Number simple attribute.
     */
    DOUBLE_VARIABLE(SystemFieldValues.DOUBLE_VARIABLE_VAL, SystemFieldValues.DOUBLE_VARIABLE_TAG),
    /**
     * Boolean simple attribute.
     */
    BOOLEAN_VARIABLE(SystemFieldValues.BOOLEAN_VARIABLE_VAL, SystemFieldValues.BOOLEAN_VARIABLE_TAG),
    /**
     * Date simple attribute.
     */
    DATE_VARIABLE(SystemFieldValues.DATE_VARIABLE_VAL, SystemFieldValues.DATE_VARIABLE_TAG),
    /**
     * Time simple attribute.
     */
    TIME_VARIABLE(SystemFieldValues.TIME_VARIABLE_VAL, SystemFieldValues.TIME_VARIABLE_TAG),
    /**
     * Timestamp simple attribute.
     */
    TIMESTAMP_VARIABLE(SystemFieldValues.TIMESTAMP_VARIABLE_VAL, SystemFieldValues.TIMESTAMP_VARIABLE_TAG),
    /**
     * Instant simple attribute.
     */
    INSTANT_VARIABLE(SystemFieldValues.INSTANT_VARIABLE_VAL, SystemFieldValues.INSTANT_VARIABLE_TAG),
    // 100 - Fields
    /**
     * Name field.
     */
    FIELD_NAME(SystemFieldValues.FIELD_NAME_VAL, SystemFieldValues.FIELD_NAME_TAG),
    // 120 - Values
    /**
     * Long value.
     */
    INTEGER_VALUE(SystemFieldValues.INTEGER_VALUE_VAL, SystemFieldValues.INTEGER_VALUE_TAG),
    /**
     * Long value.
     */
    LONG_VALUE(SystemFieldValues.LONG_VALUE_VAL, SystemFieldValues.LONG_VALUE_TAG),
    /**
     * Double value.
     */
    FLOAT_VALUE(SystemFieldValues.FLOAT_VALUE_VAL, SystemFieldValues.FLOAT_VALUE_TAG),
    /**
     * Blob value.
     */
    DOUBLE_VALUE(SystemFieldValues.DOUBLE_VALUE_VAL, SystemFieldValues.DOUBLE_VALUE_TAG),
    /**
     * Boolean value.
     */
    BOOLEAN_VALUE(SystemFieldValues.BOOLEAN_VALUE_VAL, SystemFieldValues.BOOLEAN_VALUE_TAG),
    /**
     * String value.
     */
    STRING_VALUE(SystemFieldValues.STRING_VALUE_VAL, SystemFieldValues.STRING_VALUE_TAG),
    /**
     * Date value.
     */
    DATE_VALUE(SystemFieldValues.DATE_VALUE_VAL, SystemFieldValues.DATE_VALUE_TAG),
    /**
     * Time value.
     */
    TIME_VALUE(SystemFieldValues.TIME_VALUE_VAL, SystemFieldValues.TIME_VALUE_TAG),
    /**
     * Timestamp value.
     */
    TIMESTAMP_VALUE(SystemFieldValues.TIMESTAMP_VALUE_VAL, SystemFieldValues.TIMESTAMP_VALUE_TAG),
    /**
     * Instant value.
     */
    INSTANT_VALUE(SystemFieldValues.INSTANT_VALUE_VAL, SystemFieldValues.INSTANT_VALUE_TAG);
    /**
     * Constructor.
     * @param field name of the field
     */
    private SystemSchemaFields(int value, String field) {
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
        case SystemFieldValues.VARIABLES_TAG:
            return VARIABLES.getValue();

        // 50 - Simple attributes.
        case SystemFieldValues.STRING_VARIABLE_TAG:
            return STRING_VARIABLE.getValue();
        case SystemFieldValues.INTEGER_VARIABLE_TAG:
            return INTEGER_VARIABLE.getValue();
        case SystemFieldValues.LONG_VARIABLE_TAG:
            return LONG_VARIABLE.getValue();
        case SystemFieldValues.FLOAT_VARIABLE_TAG:
            return FLOAT_VARIABLE.getValue();
        case SystemFieldValues.DOUBLE_VARIABLE_TAG:
            return DOUBLE_VARIABLE.getValue();
        case SystemFieldValues.BOOLEAN_VARIABLE_TAG:
            return BOOLEAN_VARIABLE.getValue();
        case SystemFieldValues.DATE_VARIABLE_TAG:
            return DATE_VARIABLE.getValue();
        case SystemFieldValues.TIME_VARIABLE_TAG:
            return TIME_VARIABLE.getValue();
        case SystemFieldValues.TIMESTAMP_VARIABLE_TAG:
            return TIMESTAMP_VARIABLE.getValue();
        case SystemFieldValues.INSTANT_VARIABLE_TAG:
            return INSTANT_VARIABLE.getValue();

        // 250 - Fields
        case SystemFieldValues.FIELD_NAME_TAG:
            return FIELD_NAME.getValue();

        // 300 - Values
        case SystemFieldValues.INTEGER_VALUE_TAG:
            return INTEGER_VALUE.getValue();
        case SystemFieldValues.LONG_VALUE_TAG:
            return LONG_VALUE.getValue();
        case SystemFieldValues.FLOAT_VALUE_TAG:
            return FLOAT_VALUE.getValue();
        case SystemFieldValues.DOUBLE_VALUE_TAG:
            return DOUBLE_VALUE.getValue();
        case SystemFieldValues.BOOLEAN_VALUE_TAG:
            return BOOLEAN_VALUE.getValue();
        case SystemFieldValues.STRING_VALUE_TAG:
            return STRING_VALUE.getValue();
        case SystemFieldValues.DATE_VALUE_TAG:
            return DATE_VALUE.getValue();
        case SystemFieldValues.TIME_VALUE_TAG:
            return TIME_VALUE.getValue();
        case SystemFieldValues.TIMESTAMP_VALUE_TAG:
            return TIMESTAMP_VALUE.getValue();
        case SystemFieldValues.INSTANT_VALUE_TAG:
            return INSTANT_VALUE.getValue();

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
        case SystemFieldValues.VARIABLES_VAL:
            return VARIABLES.getField();

        case SystemFieldValues.STRING_VARIABLE_VAL:
            return STRING_VARIABLE.getField();
        case SystemFieldValues.INTEGER_VARIABLE_VAL:
            return INTEGER_VARIABLE.getField();
        case SystemFieldValues.LONG_VARIABLE_VAL:
            return LONG_VARIABLE.getField();
        case SystemFieldValues.FLOAT_VARIABLE_VAL:
            return FLOAT_VARIABLE.getField();
        case SystemFieldValues.DOUBLE_VARIABLE_VAL:
            return DOUBLE_VARIABLE.getField();
        case SystemFieldValues.BOOLEAN_VARIABLE_VAL:
            return BOOLEAN_VARIABLE.getField();
        case SystemFieldValues.DATE_VARIABLE_VAL:
            return DATE_VARIABLE.getField();
        case SystemFieldValues.TIME_VARIABLE_VAL:
            return TIME_VARIABLE.getField();
        case SystemFieldValues.TIMESTAMP_VARIABLE_VAL:
            return TIMESTAMP_VARIABLE.getField();
        case SystemFieldValues.INSTANT_VARIABLE_VAL:
            return INSTANT_VARIABLE.getField();

        // 250 - Fields
        case SystemFieldValues.FIELD_NAME_VAL:
            return FIELD_NAME.getField();

        // 300 - Values
        case SystemFieldValues.INTEGER_VALUE_VAL:
            return INTEGER_VALUE.getField();
        case SystemFieldValues.LONG_VALUE_VAL:
            return LONG_VALUE.getField();
        case SystemFieldValues.FLOAT_VALUE_VAL:
            return FLOAT_VALUE.getField();
        case SystemFieldValues.DOUBLE_VALUE_VAL:
            return DOUBLE_VALUE.getField();
        case SystemFieldValues.BOOLEAN_VALUE_VAL:
            return BOOLEAN_VALUE.getField();
        case SystemFieldValues.STRING_VALUE_VAL:
            return STRING_VALUE.getField();
        case SystemFieldValues.DATE_VALUE_VAL:
            return DATE_VALUE.getField();
        case SystemFieldValues.TIME_VALUE_VAL:
            return TIME_VALUE.getField();
        case SystemFieldValues.TIMESTAMP_VALUE_VAL:
            return TIMESTAMP_VALUE.getField();
        case SystemFieldValues.INSTANT_VALUE_VAL:
            return INSTANT_VALUE.getField();

        default:
            break;
        }

        return null;
    }
}
