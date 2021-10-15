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
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author Mikhail Mikhailov on Feb 10, 2021
 * Composite, four way converter.
 */
public class QuadConverter<A, B, X, Y> {

    private final Converter<A, B> to;

    private final Converter<X, Y> from;
    /**
     * Constructor.
     */
    public QuadConverter(Converter<A, B> to, Converter<X, Y> from) {
        super();
        this.to = to;
        this.from = from;
    }
    /**
     * Gets the underlaying TO side converter.
     * @return the underlaying TO side converter.
     */
    public Converter<A, B> to() {
        return this.to;
    }
    /**
     * To REST.
     * @param obj the object to convert.
     * @return converted
     */
    public B to(A i) {

        if (Objects.isNull(i)) {
            return null;
        }

        return this.to.to(i);
    }
    /**
     * Gets the underlaying FROM side converter.
     * @return the underlaying TO side converter.
     */
    public Converter<X, Y> from() {
        return this.from;
    }
    /**
     * To UD.
     * @param obj the object to convert.
     * @return converted
     */
    public Y from (X i) {

        if (Objects.isNull(i)) {
            return null;
        }

        return this.from.to(i);
    }
    /**
     * To REST.
     * @param input the input collection
     * @return list
     */
    public List<B> to(Collection<A> input) {
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
    public List<Y> from(Collection<X> input) {
        return CollectionUtils.isEmpty(input)
                ? Collections.emptyList()
                : input.stream()
                    .map(this::from)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
    }
}
