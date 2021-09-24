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

package com.huahui.datasphere.mdm.core.dao.tokenizer;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author theseusyang
 * Simple forward iterator for {@link PGtokenizer}.
 */
public class CompositeValueIterator implements Iterator<String> {
    /**
     * The tokenizer.
     */
    private CompositeValueTokenizer cvt;
    /**
     * Current position.
     */
    private int cursor = 0;
    /**
     * Constructor.
     */
    public CompositeValueIterator(CompositeValueTokenizer cvt) {
        super();
        this.cvt = cvt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return cvt.getSize() > cursor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String next() {

        if (!hasNext()) {
            throw new NoSuchElementException("no (more) tokens in CompositeValueTokenizer");
        }

        return cvt.getToken(cursor++);
    }
}
