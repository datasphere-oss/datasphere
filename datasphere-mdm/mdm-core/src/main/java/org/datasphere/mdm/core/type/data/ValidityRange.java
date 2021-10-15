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

/**
 *
 */
package com.huahui.datasphere.mdm.core.type.data;

import java.util.Date;

/**
 * @author theseusyang
 * The validity range.
 */
public interface ValidityRange {
    /**
     * From date. May be null.
     * @return Date or null
     */
    Date getValidFrom();
    /**
     * To date. May be null.
     * @return Date or null
     */
    Date getValidTo();
    /**
     * Returns true, if the range is of the same 'from' value as the 'other'.
     * @param other the 'from; boundary
     * @return true, if the range is of the same 'from' value as the 'other'.
     */
    default boolean isSameFrom(Date other) {
        return getValidFrom() == null
                ? other == null
                : getValidFrom().equals(other);
    }
    /**
     * Returns true, if the range is of the same 'to' value as the 'other'.
     * @param other the 'to'  boundary
     * @return true, if the range is of the same 'to' value as the 'other'.
     */
    default boolean isSameTo(Date other) {
        return getValidTo() == null
                ? other == null
                : getValidTo().equals(other);
    }
    /**
     * Returns true for the same dates boundaries.
     * @param from the from
     * @param to the to
     * @return true for the same dates boundaries.
     */
    default boolean isSameRange(Date from, Date to) {
        return isSameFrom(from) && isSameTo(to);
    }
}
