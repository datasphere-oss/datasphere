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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;

import com.huahui.datasphere.mdm.core.type.data.Attribute;
import com.huahui.datasphere.mdm.core.type.data.TypeOfChange;

/**
 * The attribute diff.
 * @author theseusyang on Nov 1, 2019
 */
public class SimpleAttributesDiff {
    /**
     * The diff table.
     */
    private final Map<TypeOfChange, Map<String, Attribute>> table;
    /**
     * Constructor.
     */
    public SimpleAttributesDiff(Map<TypeOfChange, Map<String, Attribute>> table) {
        super();
        this.table = table;
    }
    /**
     * Empty check.
     * @return true, if empty, false otherwise
     */
    public boolean isEmpty() {
        return MapUtils.isEmpty(table);
    }
    /**
     * Returns diff as table.
     * @return map
     */
    public Map<String, Map<TypeOfChange, Attribute>> asAttributesTable() {
        return invert();
    }
    /**
     * Returns diff as table.
     * @return map
     */
    public Map<TypeOfChange, Map<String, Attribute>> asChangesTable() {
        return MapUtils.isEmpty(table) ? Collections.emptyMap() : table;
    }
    /**
     * Inverts diff map.
    *
    * @param map
    *            the map to invert
    * @return inverted map
    */
   private Map<String, Map<TypeOfChange, Attribute>> invert() {

       if (MapUtils.isEmpty(table)) {
           return Collections.emptyMap();
       }

       Map<String, Map<TypeOfChange, Attribute>> result = new HashMap<>();
       for (Entry<TypeOfChange, Map<String, Attribute>> row : table.entrySet()) {
           for (Entry<String, Attribute> column : row.getValue().entrySet()) {
               result.put(column.getKey(), Collections.singletonMap(row.getKey(), column.getValue()));
           }
       }

       return result;
   }
}
