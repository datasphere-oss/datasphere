/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.huahui.datasphere.mdm.rest.system.ro;

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
