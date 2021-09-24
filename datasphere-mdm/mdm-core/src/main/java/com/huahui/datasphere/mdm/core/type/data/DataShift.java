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

package com.huahui.datasphere.mdm.core.type.data;

/**
 * @author theseusyang
 * Data shift. 
 * Versions, saved prior to any modifications which may be carried out by DQ, UE, etc. are marked PRISTINE.
 * Versions, saved after some modifications done by DQ, UE, etc. are marked REVISED.
 */
public enum DataShift {
    /**
     * Versions, saved prior to any modifications which may be carried out by DQ, UE, etc. are marked PRISTINE.
     */
    PRISTINE,
    /**
     * Versions, saved after some modifications done by DQ, UE, etc. are marked REVISED.
     */
    REVISED;
    /**
     * Convenient value method.
     * @return value / name.
     */
    public String value() {
        return name();
    }
    /**
     * Covenient from value wmethod.
     * @param v value / name
     * @return parsed value
     */
    public static DataShift fromValue(String v) {
        return valueOf(v);
    }
}
