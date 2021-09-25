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
package com.huahui.datasphere.mdm.rest.system.converter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author theseusyang on Feb 10, 2021
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
