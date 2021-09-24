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
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * @author theseusyang on Oct 17, 2020
 * Nested element.
 */
public interface NestedElement extends EntityElement {
    /**
     * Gets registers, referencing this nested element.
     *
     * @return collection
     */
    @Nonnull
    Collection<RegisterElement> getReferencingRegisters();
    /**
     * Gets nested elements, referencing this nested element.
     *
     * @return collection of nested elements
     */
    @Nonnull
    Collection<NestedElement> getReferencingNesteds();
    /**
     * Gets lookups, referenced from this register alone with atributes.
     *
     * @return lookups map.
     */
    @Nonnull
    Map<LookupElement, Set<AttributeElement>> getReferencedLookups();
    /**
     * Gets all nested elements referenced by this nested element.
     *
     * @return collection of nested elements
     */
    @Nonnull
    Collection<NestedElement> getReferencedNesteds();
}
