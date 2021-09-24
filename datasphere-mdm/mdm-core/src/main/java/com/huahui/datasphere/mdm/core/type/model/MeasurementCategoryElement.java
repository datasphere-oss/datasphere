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
package com.huahui.datasphere.mdm.core.type.model;

import java.util.Collection;

import javax.annotation.Nullable;

/**
 * @author theseusyang on Nov 5, 2020
 * The measurement category.
 */
public interface MeasurementCategoryElement extends IdentityElement, NamedDisplayableElement, CustomPropertiesElement {
    /**
     * Gets all unit values.
     * @return values
     */
    Collection<MeasurementUnitElement> getUnits();
    /**
     * Returns true, if category contains named element.
     * @return true, if category contains named element.
     */
    boolean exists(String name);
    /**
     * Gets unit value with given name.
     * @param name the name
     * @return value
     */
    @Nullable
    MeasurementUnitElement getUnit(String name);
    /**
     * Gets the base unit element.
     * @return base unit element
     */
    MeasurementUnitElement getBaseUnit();
}
