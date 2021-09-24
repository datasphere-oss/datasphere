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

package com.huahui.datasphere.mdm.system.type.pipeline.fragment;

import java.util.Collection;
import java.util.function.Supplier;

import com.huahui.datasphere.mdm.system.dto.OutputCollector;
/**
 * Gets input fragments.
 * @author theseusyang on Dec 11, 2019
 */
public interface OutputFragmentCollector <X extends OutputFragmentCollector<X>> extends OutputCollector {
    /**
     * Adds a fragment from this composite.
     * @param r the fragment DTO
     */
    X fragment(OutputFragment<?> f);
    /**
     * Adds multiple fragments of the same type from this composite.
     * @param r fragments
     */
    X fragments(Collection<? extends OutputFragment<?>> f);
    /**
     * Adds a fragment from this composite using a supplier.
     * @param r the fragment DTO
     */
    X fragment(Supplier<? extends OutputFragment<?>> f);
    /**
     * Adds multiple fragments of the same type from this composite using a supplier.
     * @param r fragments
     */
    X fragments(Supplier<Collection<? extends OutputFragment<?>>> f);
}
