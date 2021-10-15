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
package org.datasphere.mdm.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.type.model.CustomPropertyElement;
import org.datasphere.mdm.core.type.model.source.CustomProperty;
import org.datasphere.mdm.system.exception.ValidationResult;

/**
 * @author Mikhail Mikhailov on Oct 9, 2020
 * Validates custom properties.
 */
public interface CustomPropertiesSupport {
    /**
     * Default validation pattern for names in model.
     */
    Pattern DEFAULT_PROPERTY_NAME_PATTERN = Pattern.compile("^[a-z][a-z0-9_-]*$", Pattern.CASE_INSENSITIVE);
    /**
     * @author Mikhail Mikhailov on Oct 9, 2020
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
