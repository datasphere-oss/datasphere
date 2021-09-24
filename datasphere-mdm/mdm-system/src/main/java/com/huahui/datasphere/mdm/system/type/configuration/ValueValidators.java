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

import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.BooleanUtils;
/**
 * Default input format validators.
 * @author theseusyang on Apr 17, 2020
 */
public final class ValueValidators {
    /**
     * Constructor.
     */
    private ValueValidators() {}
    /**
     * Integer input validator.
     */
    public static final Predicate<Optional<String>> INTEGER_VALIDATOR =
            value -> value.isPresent() && value.get().matches("\\d+");
    /**
     * FP numbers input validator.
     */
    public static final Predicate<Optional<String>> NUMBER_VALIDATOR =
            value -> value.isPresent() && value.get().matches("\\d+.\\d+");
    /**
     * String input validator.
     */
    public static final Predicate<Optional<String>> STRING_VALIDATOR = Optional::isPresent;
    /**
     * Boolean input validator.
     */
    public static final Predicate<Optional<String>> BOOLEAN_VALIDATOR =
            value -> value.isPresent() && BooleanUtils.toBooleanObject(value.get()) != null;
    /**
     * All enabling validator.
     */
    public static final Predicate<Optional<String>> ANY_VALID = value -> true;
}
