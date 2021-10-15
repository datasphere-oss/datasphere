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
package org.unidata.mdm.rest.system.converter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author Mikhail Mikhailov on Nov 11, 2020
 * Base converting stuff.
 * @param <I> a middle layer type
 * @param <O> a REST target type
 */
public class Converter<I, O> {
    /**
     * From UD to REST.
     */
    private final Function<I, O> to;
    /**
     * From REST to UD.
     */
    private final Function<O, I> from;
    /**
     * Constructor.
     */
    protected Converter(Function<I, O> to, Function<O, I> from) {
        super();
        this.to = to;
        this.from = from;
    }
    /**
     * To REST.
     * @param obj the object to convert.
     * @return converted
     */
    public O to(I obj) {

        if (Objects.isNull(obj)) {
            return null;
        }

        return this.to.apply(obj);
    }
    /**
     * To UD.
     * @param obj the object to convert.
     * @return converted
     */
    public I from(O obj) {

        if (Objects.isNull(obj)) {
            return null;
        }

        return this.from.apply(obj);
    }
    /**
     * To REST.
     * @param input the input collection
     * @return list
     */
    public List<O> to(Collection<I> input) {
        return CollectionUtils.isEmpty(input)
                ? Collections.emptyList()
                : input.stream()
                    .map(this::to)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
    }
    /**
     * To UD.
     * @param input the input collection
     * @return list
     */
    public List<I> from(Collection<O> input) {
        return CollectionUtils.isEmpty(input)
                ? Collections.emptyList()
                : input.stream()
                    .map(this::from)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
    }
}
