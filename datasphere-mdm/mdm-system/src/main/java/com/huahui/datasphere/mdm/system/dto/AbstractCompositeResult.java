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
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import com.huahui.datasphere.mdm.system.type.pipeline.fragment.FragmentId;
import com.huahui.datasphere.mdm.system.type.pipeline.fragment.OutputFragment;
import com.huahui.datasphere.mdm.system.type.pipeline.fragment.OutputFragmentCollector;
import com.huahui.datasphere.mdm.system.type.pipeline.fragment.OutputFragmentContainer;

/**
 * @author theseusyang
 */
@NotThreadSafe
public abstract class AbstractCompositeResult implements OutputFragmentContainer, OutputFragmentCollector<AbstractCompositeResult> {
    /**
     * Fragments map.
     */
    protected Map<FragmentId<? extends OutputFragment<?>>, OutputFragmentHolder> fragments;
    /**
     * Constructor.
     */
    public AbstractCompositeResult() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C extends OutputFragment<C>> C fragment(FragmentId<C> f) {

        if (MapUtils.isEmpty(fragments)) {
            return null;
        }

        OutputFragmentHolder h = fragments.get(f);
        if (Objects.isNull(h) || !h.isSingle()) {
            return null;
        }

        return (C) h.getSingle();
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C extends OutputFragment<C>> Collection<C> fragments(FragmentId<C> f) {

        if (MapUtils.isEmpty(fragments)) {
            return Collections.emptyList();
        }

        OutputFragmentHolder h = fragments.get(f);
        if (Objects.isNull(h) || !h.isMultiple()) {
            return Collections.emptyList();
        }

        return (Collection<C>) h.getMultiple();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractCompositeResult fragment(Supplier<? extends OutputFragment<?>> s) {

        OutputFragment<?> r = s.get();
        if (Objects.nonNull(r)) {
            if (Objects.isNull(fragments)) {
                fragments = new IdentityHashMap<>();
            }

            fragments.put(r.fragmentId(), OutputFragmentHolder.of(r));
        }

        return this;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractCompositeResult fragments(Supplier<Collection<? extends OutputFragment<?>>> s) {

        Collection<? extends OutputFragment<?>> fs = s.get();
        if (CollectionUtils.isNotEmpty(fs)) {
            if (Objects.isNull(fragments)) {
                fragments = new IdentityHashMap<>();
            }

            fragments.put(fs.iterator().next().fragmentId(), OutputFragmentHolder.of(fs));
        }

        return this;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractCompositeResult fragment(OutputFragment<?> f) {
        return fragment(() -> f);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractCompositeResult fragments(Collection<? extends OutputFragment<?>> f) {
        return fragments(() -> f);
    }
}
