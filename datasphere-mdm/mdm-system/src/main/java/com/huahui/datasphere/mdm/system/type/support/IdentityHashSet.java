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

package com.huahui.datasphere.mdm.system.type.support;

import java.util.AbstractSet;
import java.util.IdentityHashMap;
import java.util.Iterator;

/**
 * @author theseusyang
 * Simple identity hash set.
 */
public class IdentityHashSet<T> extends AbstractSet<T> {
    /**
     * The content.
     */
    private final IdentityHashMap<T, Boolean> content;
    /**
     * Constructor.
     */
    public IdentityHashSet() {
        super();
        content = new IdentityHashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        return content.keySet().iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return content.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        return content.containsKey(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(T o) {
        return content.put(o, Boolean.TRUE) == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        return content.remove(o) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        content.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return content.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o instanceof IdentityHashSet) {

            IdentityHashSet<?> other = (IdentityHashSet<?>) o;
            return other.content.equals(this.content);
        }

        return false;
    }
}
