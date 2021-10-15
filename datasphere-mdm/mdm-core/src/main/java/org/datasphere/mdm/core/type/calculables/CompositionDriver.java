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

package com.huahui.datasphere.mdm.core.type.calculables;

import com.huahui.datasphere.mdm.core.type.data.DataRecord;

/**
 * @author mikhail
 * Type of calculable, held by the holder.
 */
public interface CompositionDriver<T extends Calculable, A extends BvrCalculationInfo<T>, B extends BvtCalculationInfo<T>> {
    /**
     * Gets the tag name of this type.
     * @return the type name
     */
    String getName();
    /**
     * Gets the basic description of this type.
     * @return decsription
     */
    String getDescription();
    /**
     * Tells whether the given calculable set denotes an active interval (validity range).
     * @param info calculables info
     * @return true, if active, false otherwise
     */
    boolean hasActiveBVR(A info);
    /**
     * Performs BVR calculation on this type
     * @param info the calculables info
     * @return BVR winner
     */
    T toBVR(A info);
    /**
     * Performs BVT calculation on this type
     * @param info the calculables info
     * @return BVT winner record
     */
    DataRecord toBVT(B info);
}
