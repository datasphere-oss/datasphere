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

package com.huahui.datasphere.mdm.core.dao.vendor;

/**
 * Pg data types.
 * @author theseusyang
 */
public enum VendorDataType {

    BOOLEAN(16),
    BYTEA(17),
    CHAR(18),
    INT8(20),
    INT2(21),
    INT4(23),
    TEXT(25),
    JSONB(114),
    REAL(700), // Float, 4 bytes
    DOUBLE(701),
    INET4(869),
    INET6(869),
    // CIDR(650),
    // UNKNOWN(705),
    // CASH(790),
    // MONEY(791),
    // MAC_ADDRESS(829),
    DATE(1082),
    TIMESTAMP(1114),
    UUID(2950)
    // POINT,
    // BOX,
    // LINE,
    // LINESEGMENT,
    // CIRCLE,
    // PATH,
    // POLYGON,
    // HSTORE,
    // NUMERIC
    ;
    /**
     * Type Oid,
     */
    private final int id;
    /**
     * Constructor.
     * @param id type id
     */
    private VendorDataType(int id) {
        this.id = id;
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
}
