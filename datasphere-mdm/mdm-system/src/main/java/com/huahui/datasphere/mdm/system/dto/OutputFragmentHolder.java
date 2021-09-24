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

package com.huahui.datasphere.mdm.system.dto;

import java.util.Collection;

import com.huahui.datasphere.mdm.system.type.pipeline.fragment.OutputFragment;

/**
 * @author theseusyang
 * The fragments holder.
 */
final class OutputFragmentHolder {
    /**
     * Single context was supplied for id.
     */
    private final OutputFragment<?> single;
    /**
     * Multiple contexts were supplied for id.
     */
    private final Collection<? extends OutputFragment<?>> multiple;
    /**
     * Constructor.
     * @param single the fragment
     */
    private OutputFragmentHolder(OutputFragment<?> single) {
        super();
        this.single = single;
        this.multiple = null;
    }
    /**
     * Constructor.
     * @param multiple contexts
     */
    private OutputFragmentHolder(Collection<? extends OutputFragment<?>> multiple) {
        super();
        this.single = null;
        this.multiple = multiple;
    }
    /**
     * @return the single
     */
    public OutputFragment<?> getSingle() {
        return single;
    }
    /**
     * @return the multiple
     */
    public Collection<? extends OutputFragment<?>> getMultiple() {
        return multiple;
    }
    /**
     * Check for having a single fragment for id.
     * @return true for single, false otherwise
     */
    public boolean isSingle() {
        return single != null && multiple == null;
    }
    /**
     * Check for having multiple fragments for id.
     * @return true for multiple, false otherwise
     */
    public boolean isMultiple() {
        return single == null && multiple != null;
    }
    /**
     * Creates holder instance.
     * @param single the fragment
     * @return holder
     */
    public static OutputFragmentHolder of(OutputFragment<?> single) {
        return new OutputFragmentHolder(single);
    }
    /**
     * Creates holder instance.
     * @param multiple fragments
     * @return holder
     */
    public static OutputFragmentHolder of(Collection<? extends OutputFragment<?>> multiple) {
        return new OutputFragmentHolder(multiple);
    }
}