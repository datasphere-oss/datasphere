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

package com.huahui.datasphere.mdm.system.type.configuration;

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
