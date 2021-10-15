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

package com.huahui.datasphere.mdm.core.type.data.impl;

import java.util.Iterator;
import java.util.Map.Entry;

import com.huahui.datasphere.mdm.core.type.data.Attribute;
import com.huahui.datasphere.mdm.core.type.data.AttributeIterator;

/**
 * @author theseusyang
 * Thin iterator wrapper to allow traverse -&gt; remove actions.
 */
public class AttributeIteratorImpl implements AttributeIterator {

    /**
     * The underlaying iterator.
     */
    private final Iterator<Entry<String, Attribute>> it;
    /**
     * Constructor.
     * @param it iterator.
     */
    AttributeIteratorImpl(Iterator<Entry<String, Attribute>> it) {
        this.it = it;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attribute next() {
        return it.next().getValue();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        it.remove();
    }
}
