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

import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.BooleanUtils;
/**
 * Default input format validators.
 * @author Alexander Malyshev on Apr 17, 2020
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
