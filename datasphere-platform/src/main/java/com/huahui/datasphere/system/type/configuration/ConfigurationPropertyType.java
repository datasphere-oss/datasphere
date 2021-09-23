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

package com.huahui.datasphere.system.type.configuration;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

public enum ConfigurationPropertyType {

    STRING("String", ValueValidators.STRING_VALIDATOR, Function.identity(), Function.identity()),
    INTEGER("Integer", ValueValidators.INTEGER_VALIDATOR, s -> Objects.isNull(s) ? null : Long.valueOf(s), (Long i) -> Objects.isNull(i) ? null : i.toString()),
    NUMBER("Number", ValueValidators.NUMBER_VALIDATOR, s -> Objects.isNull(s) ? null : Double.valueOf(s), (Double i) -> Objects.isNull(i) ? null : i.toString()),
    BOOLEAN("Boolean", ValueValidators.BOOLEAN_VALIDATOR, s -> Objects.isNull(s) ? null : Boolean.valueOf(s), (Boolean i) -> Objects.isNull(i) ? null : i.toString()),
    CUSTOM("Custom", ValueValidators.ANY_VALID, v -> null, v -> null);

    private final String value;

    private final Predicate<Optional<String>> validator;

    private final Function<String, ?> deserializer;

    private final Function<?, String> serializer;

    ConfigurationPropertyType(
            final String value,
            final Predicate<Optional<String>> validator,
            final Function<String, ?> deserializer,
            final Function<?, String> serializer) {
        this.value = value;
        this.validator = validator;
        this.deserializer = deserializer;
        this.serializer = serializer;
    }

    public String value() {
        return value;
    }

    public Predicate<Optional<String>> getValidator() {
        return validator;
    }

    public Function<String, ?> getDeserializer() {
        return deserializer;
    }

    public Function<?, String> getSerializer() {
        return serializer;
    }

    public static ConfigurationPropertyType fromValue(String v) {
        for (ConfigurationPropertyType c : ConfigurationPropertyType.values()) {
            if (StringUtils.equalsIgnoreCase(v, c.value())) {
                return c;
            }
        }
        return null;
    }
}
