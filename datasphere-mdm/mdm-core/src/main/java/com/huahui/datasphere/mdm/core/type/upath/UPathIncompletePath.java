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

import org.apache.commons.lang3.StringUtils;

import com.huahui.datasphere.mdm.core.type.data.DataRecord;

/**
 * @author theseusyang
 * Incomplete path - parent record with the missing attribute.
 */
public class UPathIncompletePath {
    /**
     * Parent record.
     */
    private final DataRecord parent;
    /**
     * Link to missing {@link UPathElement}.
     */
    private final UPathElement element;
    /**
     * Constructor.
     */
    public UPathIncompletePath(DataRecord parent, UPathElement element) {
        super();
        this.parent = parent;
        this.element = element;
    }
    /**
     * @return the parent
     */
    public DataRecord getParent() {
        return parent;
    }
    /**
     * @return the element
     */
    public UPathElement getElement() {
        return element;
    }
    /**
     * Generates local path from content.
     * @return local path
     */
    public String toLocalPath() {

        String parentPath = parent.toLocalPath();
        return StringUtils.isBlank(parentPath)
                ? element.getElement()
                : new StringBuilder()
                    .append(parentPath)
                    .append('.')
                    .append(element.getElement())
                    .toString();
    }
}
