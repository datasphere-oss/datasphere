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

import java.util.Map;
import java.util.Map.Entry;

/**
 * @author theseusyang
 * The templates holder.
 */
public class QueryTemplates {
    /**
     * The queries.
     */
    private Map<? extends QueryTemplateDescriptor, QueryTemplate> queries;
    /**
     * Constructor.
     */
    public QueryTemplates(Map<? extends QueryTemplateDescriptor, QueryTemplate> queries) {
        super();
        this.queries = queries;
    }

    public String getQuery(QueryTemplateDescriptor d) {
        // NPE or wrong, i. e. distributed query may fail the request here
        QueryTemplate qt = queries.get(d);
        return qt.toSourceQuery();
    }

    public String getQuery(QueryTemplateDescriptor d, int shard) {
        // NPE or wrong, not distributed query may fail the request here
        QueryTemplate qt = queries.get(d);
        return qt.toQuery(shard);
    }

    public void init(int shards) {
        for (Entry<? extends QueryTemplateDescriptor, QueryTemplate> entry : queries.entrySet()) {
            if (entry.getKey().isDistributed()) {
                entry.getValue().init(shards);
            }
        }
    }
}
