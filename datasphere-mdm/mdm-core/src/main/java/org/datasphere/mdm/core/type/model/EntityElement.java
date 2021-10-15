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

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

/**
 * @author theseusyang
 * Marks an abstract entity holder.
 */
public interface EntityElement extends IdentityElement, NamedDisplayableElement, AttributedElement, CustomPropertiesElement {
    /**
     * Tells, whether this element is a top level register entity.
     * @return true, for register entities, false otherwise
     */
    default boolean isRegister() {
        return false;
    }
    /**
     * Gets register element, if this is a register element.
     * @return register element or null
     */
    default RegisterElement getRegister() {
        return null;
    }
    /**
     * Tells, whether this element is a top level lookup.
     * @return true, for lookups, false otherwise
     */
    default boolean isLookup() {
        return false;
    }
    /**
     * Gets code attributed element, if this is one (i. e. {@link #isLookup()} returns true) or null
     * @return code attributed or null
     */
    @Nullable
    default LookupElement getLookup() {
        return null;
    }
    /**
     * Tells, whether this element is a top level relation.
     * @return true, for relations, false otherwise
     */
    default boolean isRelation() {
        return false;
    }
    /**
     * Gets relational view, if this entity is a relation one.
     * @return relational view
     */
    @Nullable
    default RelationElement getRelation() {
        return null;
    }
    /**
     * Tells, whether this element is a nested entity.
     * @return true, for nested, false otherwise
     */
    default boolean isNested() {
        return false;
    }
    /**
     * Gets the nested entity view, if this is a nested entity.
     * @return nested entity or null
     */
    default NestedElement getNested() {
        return null;
    }
    /**
     * Tells, whether this element is BVT capable.
     * @return true, for BVT capable, false otherwise
     */
    default boolean isBvtCapable() {
        return false;
    }
    /**
     * Returns the BVT element.
     * @return BVT element
     */
    @Nullable
    default BvtMapElement getBvt() {
        return null;
    }
    /**
     * Tells, whether this elements is indexed one.
     * @return true, if indexed, false otherwise
     */
    default boolean isIndexed() {
        return false;
    }
    /**
     * Gets the indexed element or null, if not supported.
     * @return element or null
     */
    @Nullable
    default IndexedElement getIndexed() {
        return null;
    }
    /**
     * Returns true, if the code attribute is a generating one (i. e. its values come from a supplied generating strategy).
     * @return true for attributes with value generation support, false otherwise
     */
    default boolean isGenerating() {
        return false;
    }
    /**
     * Gets generating element, if this entity is an EXTERNAL ID generating one.
     * @return generating element
     */
    @Nullable
    default GeneratingElement getGenerating() {
        return null;
    }
    /**
     * Returns true if this entity can have validity period settings.
     * @return true if this entity can have validity period settings.
     */
    default boolean isValidityPeriod() {
        return false;
    }
    /**
     * Gets validity period settings for this entity.
     * @return settings
     */
    default ValidityPeriodElement getValidityPeriod() {
        return null;
    }
    /**
     * Returns true, if this entity has generating attributes.
     * @return true, if has, false otherwise
     */
    default boolean hasGeneratingAttributes() {
        return false;
    }
    /**
     * Gets filtered value generating attributes.
     * @return list of value generating attributes
     */
    default List<AttributeElement> getGeneratingAttributes() {
        return Collections.emptyList();
    }
    /**
     * Returns true, if this element supports grouping.
     * @return true, if this element supports grouping.
     */
    default boolean isGrouped() {
        return false;
    }
    /**
     * Gets grouped view.
     * @return grouped view
     */
    default GroupedElement getGrouped() {
        return null;
    }
    /**
     * Returns true, if this element supports presentation settings.
     * @return true, if this element supports presentation settings.
     */
    default boolean hasPresentation() {
        return false;
    }
    /**
     * Returns presentation view.
     * @return presentation view
     */
    default PresentationElement getPresentation() {
        return null;
    }
    /**
     * Returns true, if this element is a searchable model element.
     * @return true, if this element is a searchable model element.
     */
    default boolean isSearchable() {
        return false;
    }
    /**
     * Returns searchable info (that will be indexed in model index) about this model element if this element is a searchable one.
     * @return searchable info (that will be indexed in model index) about this model element if this element is a searchable one.
     */
    default SearchableElement getSearchable() {
        return null;
    }
}
