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
package com.huahui.datasphere.mdm.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import com.huahui.datasphere.mdm.system.exception.ValidationResult;

import com.huahui.datasphere.mdm.core.type.model.CustomPropertyElement;
import com.huahui.datasphere.mdm.core.type.model.source.CustomProperty;

/**
 * @author theseusyang on Oct 9, 2020
 * Validates custom properties.
 */
public interface CustomPropertiesSupport {
    /**
     * Default validation pattern for names in model.
     */
    Pattern DEFAULT_PROPERTY_NAME_PATTERN = Pattern.compile("^[a-z][a-z0-9_-]*$", Pattern.CASE_INSENSITIVE);
    /**
     * @author theseusyang on Oct 9, 2020
     * Validation messages.
     */
    enum ValidationConstants {
        /**
         * Doesn't match against default pattern.
         */
        CUSTOM_PROPERTY_INVALID_NAMES("app.custom.property.invalid.names.on.object"),
        /**
         * Duplicates found.
         */
        CUSTOM_PROPERTY_DUPLICATE_NAMES("app.custom.property.duplicate.names.on.object");
        /**
         * Message code.
         */
        private final String value;
        /**
         * Constructor.
         * @param value the value
         */
        private ValidationConstants(String value) {
            this.value = value;
        }
        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }
    /**
     * Validate custom properties.
     *
     * @param objectName the object name
     * @param customProperties the custom properties
     * @return the list
     */
    default List<ValidationResult> validateCustomProperties(final String objectName,
                                                            final List<CustomProperty> customProperties) {
        if (CollectionUtils.isEmpty(customProperties)) {
            return Collections.emptyList();
        }

        final Set<String> invalidNames = new HashSet<>();
        final Set<String> duplicateNames = new HashSet<>();
        final Set<String> propertiesNames = new HashSet<>();

        for(CustomProperty property : customProperties) {

            final String propertyName = property.getName();
            if (!DEFAULT_PROPERTY_NAME_PATTERN.matcher(propertyName).matches()) {
                invalidNames.add(propertyName);
            }

            if (!propertiesNames.add(propertyName)) {
                duplicateNames.add(propertyName);
            }
        }

        final List<ValidationResult> validationResults = new ArrayList<>();
        if (!invalidNames.isEmpty()) {
            validationResults.add(new ValidationResult("Invalid properties names: " + invalidNames,
                    ValidationConstants.CUSTOM_PROPERTY_INVALID_NAMES.getValue(), invalidNames, objectName));
        }

        if (!duplicateNames.isEmpty()) {
            validationResults.add(new ValidationResult("Duplicated properties names: " + duplicateNames,
                    ValidationConstants.CUSTOM_PROPERTY_DUPLICATE_NAMES.getValue(), duplicateNames, objectName));
        }

        return validationResults;
    }

    default Collection<CustomProperty> assembleCustomProperties(Collection<CustomPropertyElement> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        return source.stream()
                .map(cpi -> new CustomProperty()
                        .withName(cpi.getName())
                        .withValue(cpi.getValue()))
                .collect(Collectors.toList());
    }
}
