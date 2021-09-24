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
package com.huahui.datasphere.mdm.core.type.upath;

import com.huahui.datasphere.mdm.core.type.data.DataRecord;

/**
 * @author theseusyang on Feb 25, 2021
 * Filtering element interface.
 */
public interface UPathFilterElement extends UPathElement {
    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isFiltering() {
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    default UPathFilterElement getFilter() {
        return this;
    }
    /**
     * Returns true, if the given record matches filtering condition.
     * @param record the record to check
     * @return true, if the given record matches filtering condition
     */
    boolean matches(DataRecord record);
}
