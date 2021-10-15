/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package com.huahui.datasphere.rest.system.ro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

/**
 * Supported simple data types.
 */
public enum SimpleDataType {

    /**
     * Date, as defined in XSD.
     */
    DATE("Date"),
    /**
     * Time, as defined in XSD.
     */
    TIME("Time"),
    /**
     * Date-time, as defined in XSD.
     */
    TIMESTAMP("Timestamp"),
    /**
     * String, as defined in XSD.
     */
    STRING("String"),
    /**
     * Dictionary, as defined in XSD.
     */
    DICTIONARY("Dictionary"),
    /**
     * Integer, as defined in XSD.
     */
    INTEGER("Integer"),
    /**
     * FP number, as defined in XSD.
     */
    NUMBER("Number"),
    /**
     * Boolean, as defined in XSD.
     */
    BOOLEAN("Boolean"),
    /**
     * BLOB as defined in XSD.
     */
    BLOB("Blob"),
    /**
     * CLOB as defined in XSD.
     */
    CLOB("Clob"),
    /**
     * CLOB as defined in XSD.
     */
    ANY("Any");
    /**
     * The value, really used for marshaling / unmarshaling.
     */
    private final String value;

    private SimpleDataType(String v) {
        value = v;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static SimpleDataType fromValue(String v) {

        if("Measured".equalsIgnoreCase(v)){
            return NUMBER;
        }

        for (SimpleDataType c: SimpleDataType.values()) {
            if (StringUtils.equalsIgnoreCase(v, c.value())) {
                return c;
            }
        }
        return null;
    }

}
