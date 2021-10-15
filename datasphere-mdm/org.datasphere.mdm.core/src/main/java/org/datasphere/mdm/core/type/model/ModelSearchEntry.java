/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 *
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.type.model;

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
