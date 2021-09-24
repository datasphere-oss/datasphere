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
 * @author theseusyang on Oct 6, 2020
 * Enumeration view.
 */
public interface EnumerationElement extends IdentityElement, NamedDisplayableElement, CustomPropertiesElement {
    /**
     * Gets all enumeration values.
     * @return values
     */
    Collection<NamedDisplayableElement> getEnumerationValues();
    /**
     * Returns true, if enumeration contains named element.
     * @return true, if enumeration contains named element.
     */
    boolean valueExists(String name);
    /**
     * Gets enumeration value with given name.
     * @param name the name
     * @return value
     */
    @Nullable
    NamedDisplayableElement getEnumerationValue(String name);
}
