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

/**
 * @author theseusyang
 * Short info about top level elements.
 */
public interface NamedDisplayableElement {
    /**
     * Gets the entity's name.
     * @return name
     */
    String getName();
    /**
     * Gets the entity's display name.
     * @return display name
     */
    String getDisplayName();
    /**
     * Gets a possibly defined description of the element.
     * @return description or null
     */
    String getDescription();
}
