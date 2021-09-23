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
 */
public final class SystemFieldValues extends CommonFieldValues {
    /**
     * Disabling constructor.
     */
    private SystemFieldValues() {
        super();
    }
    //=======================================================================================================================
    // Strings
    //=======================================================================================================================
    // Variables
    /**
     * Variables tag.
     */
    public static final String VARIABLES_TAG = "variables";
    /**
     * String variable tag.
     */
    public static final String STRING_VARIABLE_TAG = "stringVariable";
    /**
     * Integer variable tag.
     */
    public static final String INTEGER_VARIABLE_TAG = "intVariable";
    /**
     * Long variable tag.
     */
    public static final String LONG_VARIABLE_TAG = "longVariable";
    /**
     * Float variable tag.
     */
    public static final String FLOAT_VARIABLE_TAG = "floatVariable";
    /**
     * Double variable tag.
     */
    public static final String DOUBLE_VARIABLE_TAG = "doubleVariable";
    /**
     * Boolean variable tag.
     */
    public static final String BOOLEAN_VARIABLE_TAG = "booleanVariable";
    /**
     * Date variable tag.
     */
    public static final String DATE_VARIABLE_TAG = "dateVariable";
    /**
     * Time variable tag.
     */
    public static final String TIME_VARIABLE_TAG = "timeVariable";
    /**
     * Timestamp variable tag.
     */
    public static final String TIMESTAMP_VARIABLE_TAG = "timestampVariable";
    /**
     * Instant variable tag.
     */
    public static final String INSTANT_VARIABLE_TAG = "instantVariable";
    // Name etc.
    /**
     * Name field.
     */
    public static final String FIELD_NAME_TAG = "name";
    // Values
    /**
     * Int value.
     */
    public static final String INTEGER_VALUE_TAG = "intValue";
    /**
     * Long value.
     */
    public static final String LONG_VALUE_TAG = "longValue";
    /**
     * Float value.
     */
    public static final String FLOAT_VALUE_TAG = "floatValue";
    /**
     * Double value.
     */
    public static final String DOUBLE_VALUE_TAG = "doubleValue";
    /**
     * Boolean value.
     */
    public static final String BOOLEAN_VALUE_TAG = "booleanValue";
    /**
     * String value.
     */
    public static final String STRING_VALUE_TAG = "stringValue";
    /**
     * Date value.
     */
    public static final String DATE_VALUE_TAG = "dateValue";
    /**
     * Time value.
     */
    public static final String TIME_VALUE_TAG = "timeValue";
    /**
     * Timestamp value.
     */
    public static final String TIMESTAMP_VALUE_TAG = "timestampValue";
    /**
     * Instant value.
     */
    public static final String INSTANT_VALUE_TAG = "instantValue";
    //=======================================================================================================================
    // Integers
    //=======================================================================================================================
    // Variables
    /**
     * Variables val.
     */
    public static final int VARIABLES_VAL = 10001;
    /**
     * String variable tag.
     */
    public static final int STRING_VARIABLE_VAL = 10050;
    /**
     * Integer variable tag.
     */
    public static final int INTEGER_VARIABLE_VAL = 10051;
    /**
     * Long variable tag.
     */
    public static final int LONG_VARIABLE_VAL = 10052;
    /**
     * Float variable tag.
     */
    public static final int FLOAT_VARIABLE_VAL = 10053;
    /**
     * Double variable tag.
     */
    public static final int DOUBLE_VARIABLE_VAL = 10054;
    /**
     * Boolean simple attribute.
     */
    public static final int BOOLEAN_VARIABLE_VAL = 10055;
    /**
     * Date simple attribute.
     */
    public static final int DATE_VARIABLE_VAL = 10056;
    /**
     * Time simple attribute.
     */
    public static final int TIME_VARIABLE_VAL = 10057;
    /**
     * Timestamp simple attribute.
     */
    public static final int TIMESTAMP_VARIABLE_VAL = 10058;
    /**
     * Instant simple attribute.
     */
    public static final int INSTANT_VARIABLE_VAL = 10059;
    // Name etc.
    /**
     * Name field.
     */
    public static final int FIELD_NAME_VAL = 10250;
    // Values
    /**
     * Int value.
     */
    public static final int INTEGER_VALUE_VAL = 10300;
    /**
     * Long value.
     */
    public static final int LONG_VALUE_VAL = 10301;
    /**
     * Float value.
     */
    public static final int FLOAT_VALUE_VAL = 10302;
    /**
     * Double value.
     */
    public static final int DOUBLE_VALUE_VAL = 10303;
    /**
     * Boolean value.
     */
    public static final int BOOLEAN_VALUE_VAL = 10304;
    /**
     * String value.
     */
    public static final int STRING_VALUE_VAL = 10305;
    /**
     * Date value.
     */
    public static final int DATE_VALUE_VAL = 10306;
    /**
     * Time value.
     */
    public static final int TIME_VALUE_VAL = 10307;
    /**
     * Timestamp value.
     */
    public static final int TIMESTAMP_VALUE_VAL = 10308;
    /**
     * Instant value.
     */
    public static final int INSTANT_VALUE_VAL = 10309;
}
