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

import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huahui.datasphere.mdm.core.po.ObjectPO;

/**
 * @author theseusyang
 * Row tokenization helper utility.
 */
public abstract class AbstractRowTokenizer<T extends ObjectPO> {
    /**
     * The logger.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractRowTokenizer.class);
    /**
     * Constructor.
     */
    public AbstractRowTokenizer() {
        super();
    }
    /**
     * Processes PGObjet internal data.
     * @param val the PGObject instance
     * @return result
     */
    public T process(PGobject val) {

        if (val != null
         && val.getValue() != null
         && val.getValue().length() > 0) {
            return process(CompositeValueTokenizer.structTokenizer(val.getValue()));
        }

        return null;
    }
    /**
     * Number of columns in a record.
     * @return number of columns
     */
    protected int size() {
        return 0;
    }
    /**
     * Process line at whole.
     * @param fields the line
     * @return new object
     */
    protected abstract T process(CompositeValueTokenizer fields);
    /**
     * Process part of a line.
     * @param fields the line
     * @param from the from index
     * @param to the to index
     * @param t
     */
    protected abstract void process(CompositeValueIterator tk, T t);
    /**
     * Reads a portion from tokenizer iterator into the target T.
     * @param rti the tokenizer
     * @param t the target
     * @param fields the fields to read
     */
    protected void process(CompositeValueIterator rti, T t, RowTokenizerField<T>[] fields) {

        for (int i = 0; i < fields.length; i++) {

            String token = rti.next();
            if (StringUtils.isBlank(token)) {
                continue;
            }

            fields[i].accept(token, t);
        }
    }
}
