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
 * Code attribute holder element.
 * @author theseusyang on Nov 7, 2019
 */
public interface LookupElement extends EntityElement {
    /**
     * Gets the code attribute.
     *
     * @return the code attribute
     */
    @Nonnull
    AttributeElement getCodeAttribute();
    /**
     * Gets code alternative attributes.
     *
     * @return attributes collection
     */
    @Nonnull
    Collection<AttributeElement> getCodeAliases();
    /**
     * Gets lookups, referenced from this lookup alone with atributes.
     *
     * @return lookups map.
     */
    @Nonnull
    Map<LookupElement, Set<AttributeElement>> getReferencedLookups();
    /**
     * Gets lookups, referencing this lookup alone with atributes.
     *
     * @return lookups map.
     */
    @Nonnull
    Map<LookupElement, Set<AttributeElement>> getReferencingLookups();
    /**
     * Gets registers, referencing this lookup.
     *
     * @return registers with attributes
     */
    @Nonnull
    Map<RegisterElement, Set<AttributeElement>> getReferencingRegisters();
    /**
     * Gets nested elements, referencing this lookup.
     *
     * @return nested elements with attributes
     */
    @Nonnull
    Map<NestedElement, Set<AttributeElement>> getReferencingNesteds();
    /**
     * Gets relation elements, referencing this lookup.
     *
     * @return relation elements with their attributes
     */
    @Nonnull
    Map<RelationElement, Set<AttributeElement>> getReferencingRelations();
}
