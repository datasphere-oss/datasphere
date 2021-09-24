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
package com.huahui.datasphere.mdm.core.service.impl.upath;

import java.util.Objects;
import java.util.function.Predicate;

import com.huahui.datasphere.mdm.core.type.data.DataRecord;
import com.huahui.datasphere.mdm.core.type.upath.UPathElementType;
import com.huahui.datasphere.mdm.core.type.upath.UPathFilterElement;

/**
 * @author theseusyang on Feb 26, 2021
 * Generic predicate filter.
 */
public class PredicateElementImpl extends AbstractElementImpl implements UPathFilterElement {
    /**
     * The presicate.
     */
    private final Predicate<DataRecord> predicate;
    /**
     * Constructor.
     * @param element the source element
     * @param predicate filtering predicate
     */
    public PredicateElementImpl(String element, Predicate<DataRecord> predicate) {
        super(element, UPathElementType.EXPRESSION);
        this.predicate = predicate;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(DataRecord record) {
        return Objects.nonNull(record) && predicate.test(record);
    }
}
