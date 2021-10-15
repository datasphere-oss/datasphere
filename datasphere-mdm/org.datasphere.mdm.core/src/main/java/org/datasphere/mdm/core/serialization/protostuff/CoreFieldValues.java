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

import org.datasphere.mdm.system.serialization.protostuff.CommonFieldValues;

/**
 * @author Mikhail Mikhailov
 *
 */
public final class CoreFieldValues extends CommonFieldValues {
    /**
     * Disabling constructor.
     */
    private CoreFieldValues() {
        super();
    }
    // Strings
    // 1 - 49. Record and fields.
    /**
     * Data record.
     */
    public static final String DATA_RECORD_TAG = "dataRecord";
    /**
     * Formless data bundle - data record with variables.
     */
    public static final String DATA_BUNDLE_TAG = "dataBundle";
    /**
     * Bundles array.
     */
    public static final String BUNDLES_ARRAY_TAG = "bundlesArray";
    // 50 - Simple attributes.
    /**
     * String simple attribute tag.
     */
    public static final String STRING_SIMPLE_ATTRIBUTE_TAG = "stringSimpleAttribute";
    /**
     * Integer simple attribute tag.
     */
    public static final String INTEGER_SIMPLE_ATTRIBUTE_TAG = "integerSimpleAttribute";
    /**
     * Number simple attribute tag.
     */
    public static final String NUMBER_SIMPLE_ATTRIBUTE_TAG = "numberSimpleAttribute";
    /**
     * Boolean simple attribute.
     */
    public static final String BOOLEAN_SIMPLE_ATTRIBUTE_TAG = "booleanSimpleAttribute";
    /**
     * Blob simple attribute.
     */
    public static final String BLOB_SIMPLE_ATTRIBUTE_TAG = "blobSimpleAttribute";
    /**
     * Clob simple attribute.
     */
    public static final String CLOB_SIMPLE_ATTRIBUTE_TAG = "clobSimpleAttribute";
    /**
     * Date simple attribute.
     */
    public static final String DATE_SIMPLE_ATTRIBUTE_TAG = "dateSimpleAttribute";
    /**
     * Time simple attribute.
     */
    public static final String TIME_SIMPLE_ATTRIBUTE_TAG = "timeSimpleAttribute";
    /**
     * Timestamp simple attribute.
     */
    public static final String TIMESTAMP_SIMPLE_ATTRIBUTE_TAG = "timestampSimpleAttribute";
    /**
     * Integer simple attribute.
     */
    public static final String MEASURED_SIMPLE_ATTRIBUTE_TAG = "measuredSimpleAttribute";

    // 100 - Code attributes.
    /**
     * String code attribute.
     */
    public static final String STRING_CODE_ATTRIBUTE_TAG = "stringCodeAttribute";
    /**
     * Integer code attribute.
     */
    public static final String INTEGER_CODE_ATTRIBUTE_TAG = "integerCodeAttribute";

    // 150 -Array attributes
    /**
     * String array.
     */
    public static final String STRING_ARRAY_ATTRIBUTE_TAG = "stringArrayAttribute";
    /**
     * Integer array.
     */
    public static final String INTEGER_ARRAY_ATTRIBUTE_TAG = "integerArrayAttribute";
    /**
     * Number array.
     */
    public static final String NUMBER_ARRAY_ATTRIBUTE_TAG = "numberArrayAttribute";
    /**
     * Date array.
     */
    public static final String DATE_ARRAY_ATTRIBUTE_TAG = "dateArrayAttribute";
    /**
     * Time array.
     */
    public static final String TIME_ARRAY_ATTRIBUTE_TAG = "timeArrayAttribute";
    /**
     * String array.
     */
    public static final String TIMESTAMP_ARRAY_ATTRIBUTE_TAG = "timestampArrayAttribute";

    // 200 - Complex
    /**
     * Complex.
     */
    public static final String COMPLEX_ATTRIBUTE_TAG = "complexAttribute";

    // 250 - Fields
    /**
     * Name field.
     */
    public static final String FIELD_NAME_TAG = "name";

    // 300 - Values
    /**
     * Long value.
     */
    public static final String INTEGER_VALUE_TAG = "intValue";
    /**
     * Double value.
     */
    public static final String NUMBER_VALUE_TAG = "numberValue";
    /**
     * Boolean value.
     */
    public static final String BOOLEAN_VALUE_TAG = "booleanValue";
    /**
     * String value.
     */
    public static final String STRING_VALUE_TAG = "stringValue";
    /**
     * Blob value.
     */
    public static final String BLOB_VALUE_TAG = "blobValue";
    /**
     * Clob value.
     */
    public static final String CLOB_VALUE_TAG = "clobValue";
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

    // 350 - Measured
    /**
     * Measured attribute value id.
     */
    public static final String MEASURED_CATEGORY_ID_TAG = "measuredValueId";
    /**
     * Measured attribute unit id.
     */
    public static final String MEASURED_UNIT_ID_TAG = "measuredUnitId";
    /**
     * The initial value (double).
     */
    public static final String MEASURED_INITIAL_TAG = "measuredInitial";

    // 400 - LOB value
    /**
     * Large value ID.
     */
    public static final String LARGE_VALUE_ID_TAG = "lobId";
    /**
     * Large value file name.
     */
    public static final String LARGE_VALUE_FILENAME_TAG = "lobFilename";
    /**
     * Large value size.
     */
    public static final String LARGE_VALUE_SIZE_TAG = "lobSize";
    /**
     * Large value mime type.
     */
    public static final String LARGE_VALUE_MIME_TYPE_TAG = "lobMimeType";
    /**
     * Large value acceptance state.
     */
    public static final String LARGE_VALUE_ACCEPTANCE_TAG = "lobAcceptance";

    // 450 - Array values
    /**
     * Long array value.
     */
    public static final String INTEGER_ARRAY_VALUE_TAG = "intArrayValue";
    /**
     * Double array value.
     */
    public static final String NUMBER_ARRAY_VALUE_TAG = "numberArrayValue";
    /**
     * String array value.
     */
    public static final String STRING_ARRAY_VALUE_TAG = "stringArrayValue";
    /**
     * Date array value.
     */
    public static final String DATE_ARRAY_VALUE_TAG = "dateArrayValue";
    /**
     * Time array value.
     */
    public static final String TIME_ARRAY_VALUE_TAG = "timeArrayValue";
    /**
     * Timestamp array value tag.
     */
    public static final String TIMESTAMP_ARRAY_VALUE_TAG = "timestampArrayValue";

    // Integers
    // 1 - 49 - Record and its fields.
    /**
     * Data record.
     */
    public static final int DATA_RECORD_VAL = 1;
    /**
     * Formless data bundle - data record with variables.
     */
    public static final int DATA_BUNDLE_VAL = 2;
    /**
     * Bundles array.
     */
    public static final int BUNDLES_ARRAY_VAL = 3;
    // 50 - Simple attributes.
    /**
     * String simple attribute tag.
     */
    public static final int STRING_SIMPLE_ATTRIBUTE_VAL = 50;
    /**
     * Integer simple attribute tag.
     */
    public static final int INTEGER_SIMPLE_ATTRIBUTE_VAL = 51;
    /**
     * Number simple attribute tag.
     */
    public static final int NUMBER_SIMPLE_ATTRIBUTE_VAL = 52;
    /**
     * Boolean simple attribute.
     */
    public static final int BOOLEAN_SIMPLE_ATTRIBUTE_VAL = 53;
    /**
     * Blob simple attribute.
     */
    public static final int BLOB_SIMPLE_ATTRIBUTE_VAL = 54;
    /**
     * Clob simple attribute.
     */
    public static final int CLOB_SIMPLE_ATTRIBUTE_VAL = 55;
    /**
     * Date simple attribute.
     */
    public static final int DATE_SIMPLE_ATTRIBUTE_VAL = 56;
    /**
     * Time simple attribute.
     */
    public static final int TIME_SIMPLE_ATTRIBUTE_VAL = 57;
    /**
     * Timestamp simple attribute.
     */
    public static final int TIMESTAMP_SIMPLE_ATTRIBUTE_VAL = 58;
    /**
     * Integer simple attribute.
     */
    public static final int MEASURED_SIMPLE_ATTRIBUTE_VAL = 59;

    // 100 - Code attributes.
    /**
     * String code attribute.
     */
    public static final int STRING_CODE_ATTRIBUTE_VAL = 100;
    /**
     * Integer code attribute.
     */
    public static final int INTEGER_CODE_ATTRIBUTE_VAL = 101;

    // 150 -Array attributes
    /**
     * String array.
     */
    public static final int STRING_ARRAY_ATTRIBUTE_VAL = 150;
    /**
     * Integer array.
     */
    public static final int INTEGER_ARRAY_ATTRIBUTE_VAL = 151;
    /**
     * Number array.
     */
    public static final int NUMBER_ARRAY_ATTRIBUTE_VAL = 152;
    /**
     * Date array.
     */
    public static final int DATE_ARRAY_ATTRIBUTE_VAL = 153;
    /**
     * Time array.
     */
    public static final int TIME_ARRAY_ATTRIBUTE_VAL = 154;
    /**
     * String array.
     */
    public static final int TIMESTAMP_ARRAY_ATTRIBUTE_VAL = 155;

    // 200 - Complex
    /**
     * Complex.
     */
    public static final int COMPLEX_ATTRIBUTE_VAL = 200;

    // 250 - Fields
    /**
     * Name field.
     */
    public static final int FIELD_NAME_VAL = 250;

    // 300 - Values
    /**
     * Long value.
     */
    public static final int INTEGER_VALUE_VAL = 300;
    /**
     * Double value.
     */
    public static final int NUMBER_VALUE_VAL = 301;
    /**
     * Boolean value.
     */
    public static final int BOOLEAN_VALUE_VAL = 302;
    /**
     * String value.
     */
    public static final int STRING_VALUE_VAL = 303;
    /**
     * Blob value.
     */
    public static final int BLOB_VALUE_VAL = 304;
    /**
     * Clob value.
     */
    public static final int CLOB_VALUE_VAL = 305;
    /**
     * Date value.
     */
    public static final int DATE_VALUE_VAL = 306;
    /**
     * Time value.
     */
    public static final int TIME_VALUE_VAL = 307;
    /**
     * Timestamp value.
     */
    public static final int TIMESTAMP_VALUE_VAL = 308;

    // 350 - Measured
    /**
     * Measured attribute value id.
     */
    public static final int MEASURED_CATEGORY_ID_VAL = 350;
    /**
     * Measured attribute unit id.
     */
    public static final int MEASURED_UNIT_ID_VAL = 351;
    /**
     * The "initial" value.
     */
    public static final int MEASURED_INITIAL_VAL = 352;

    // 400 - LOB value
    /**
     * Large value ID.
     */
    public static final int LARGE_VALUE_ID_VAL = 400;
    /**
     * Large value file name.
     */
    public static final int LARGE_VALUE_FILENAME_VAL = 401;
    /**
     * Large value size.
     */
    public static final int LARGE_VALUE_SIZE_VAL = 402;
    /**
     * Large value mime type.
     */
    public static final int LARGE_VALUE_MIME_TYPE_VAL = 403;
    /**
     * Large value acceptance state.
     */
    public static final int LARGE_VALUE_ACCEPTANCE_VAL = 404;

    // 450 - Array values
    /**
     * Long array value.
     */
    public static final int INTEGER_ARRAY_VALUE_VAL = 450;
    /**
     * Double array value.
     */
    public static final int NUMBER_ARRAY_VALUE_VAL = 451;
    /**
     * String array value.
     */
    public static final int STRING_ARRAY_VALUE_VAL = 452;
    /**
     * Date array value.
     */
    public static final int DATE_ARRAY_VALUE_VAL = 453;
    /**
     * Time array value.
     */
    public static final int TIME_ARRAY_VALUE_VAL = 454;
    /**
     * Timestamp array value tag.
     */
    public static final int TIMESTAMP_ARRAY_VALUE_VAL = 455;
}
