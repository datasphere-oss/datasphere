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

import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * @author theseusyang on Oct 16, 2020
 * Relation access interface.
 */
public interface RelationElement extends EntityElement {
    /**
     * True, if relation is required.
     * @return true, if required.
     */
    boolean isRequired();
    /**
     * Relation is of type REFERENCE.
     * @return true for REFERENCE
     */
    boolean isReference();
    /**
     * Relation is of type CONTAINS.
     * @return true for CONTAINS
     */
    boolean isContainment();
    /**
     * Relation is of type M2M.
     * @return true for M2M
     */
    boolean isManyToMany();
    /**
     * Gets left (from) entity.
     * @return the left (from) entity
     */
    @Nonnull
    RegisterElement getLeft();
    /**
     * Gets the left entity presentation rules.
     * @return left presentation rules
     */
    @Nonnull
    ReferencePresentationElement getLeftPresentation();
    /**
     * Gets right (to) entity.
     * @return the right (to) entity
     */
    @Nonnull
    RegisterElement getRight();
    /**
     * Gets the right entity presentation rules.
     * @return right presentation rules
     */
    @Nonnull
    ReferencePresentationElement getRightPresentation();
    /**
     * Gets lookups, referenced from this register alone with atributes.
     *
     * @return lookups map.
     */
    @Nonnull
    Map<LookupElement, Set<AttributeElement>> getReferencedLookups();
}
