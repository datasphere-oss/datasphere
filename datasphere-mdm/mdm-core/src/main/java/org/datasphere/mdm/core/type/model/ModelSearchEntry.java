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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Model object holding search info for a particular element.
 */
public class ModelSearchEntry {
    /**
     * Type of the subject element.
     */
    private final String subjectType;
    /**
     * Type of the subject element.
     */
    private final String subjectName;
    /**
     * Type of the entry element.
     */
    private final String entryType;
    /**
     * Name of model element
     */
    private final String entryName;
    /**
     * Display name of model element
     */
    private String entryDisplayName;
    /**
     * Display name of model element
     */
    private String entryDescription;
    /**
     * Collection things which should be available for searching
     */
    private final Map<String, List<String>> details = new HashMap<>();

    public ModelSearchEntry(String subjectType, String subjectName, String entryType, String entryName) {

        Objects.requireNonNull(subjectType, "Subject type must not be null.");
        Objects.requireNonNull(subjectName, "Subject name must not be null.");
        Objects.requireNonNull(entryType, "Entry type must not be null.");
        Objects.requireNonNull(entryName, "Entry name must not be null.");

        this.subjectType = subjectType;
        this.subjectName = subjectName;
        this.entryType = entryType;
        this.entryName = entryName;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getEntryType() {
        return entryType;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryDisplayName(String entryDisplayName) {
        this.entryDisplayName = entryDisplayName;
    }

    public String getEntryDisplayName() {
        return entryDisplayName;
    }

    public String getEntryDescription() {
        return entryDescription;
    }

    public void setEntryDescription(String entryDescription) {
        this.entryDescription = entryDescription;
    }

    public Map<String, List<String>> getDetails() {
        return details;
    }

    public ModelSearchEntry withEntryDisplayName(String entryDisplayName) {
        setEntryDisplayName(entryDisplayName);
        return this;
    }

    public ModelSearchEntry withEntryDescription(String entryDescription) {
        setEntryDescription(entryDescription);
        return this;
    }

    public ModelSearchEntry withDetail(String name, String value) {
        details.computeIfAbsent(name, k -> new ArrayList<>())
               .add(value);
        return this;
    }

    public ModelSearchEntry withDetails(String name, Collection<String> values) {
        details.computeIfAbsent(name, k -> new ArrayList<>())
               .addAll(values);
        return this;
    }
}
