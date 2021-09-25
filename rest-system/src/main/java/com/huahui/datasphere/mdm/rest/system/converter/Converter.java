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
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author theseusyang on Nov 11, 2020
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
