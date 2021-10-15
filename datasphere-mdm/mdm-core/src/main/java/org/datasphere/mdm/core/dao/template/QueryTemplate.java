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

package com.huahui.datasphere.mdm.core.dao.template;

import org.apache.commons.lang3.StringUtils;

/**
 * @author theseusyang
 * Distributed query generator.
 */
public class QueryTemplate {
    /**
     * Partition mark string.
     */
    public static final String PARTITION_MARK = "${#}";
    /**
     * The template query source.
     */
    private final String source;
    /**
     * "Narrowed" queries, created from the template above.
     */
    private String[] narrowed;
    /**
     * Constructor.
     */
    public QueryTemplate(String source) {
        super();
        this.source = source;
        this.narrowed = new String[0];
    }
    /**
     * (Re-) initializes templated queries.
     * @param shards the number of shards to initialize
     */
    public void init(int shards) {
        narrowed = new String[shards];
        for (int i = 0; i < narrowed.length; i++) {
            narrowed[i] = StringUtils.replace(source, PARTITION_MARK, Integer.toString(i));
        }
    }
    /**
     * Simple here.
     * @param shard the shard number.
     * @return query string
     */
    public String toQuery(int shard) {
        return narrowed[shard];
    }
    /**
     * Not distributed. Return source as is.
     * @return source query
     */
    public String toSourceQuery() {
        return source;
    }
}
