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

import java.util.ArrayList;
import java.util.List;

import com.huahui.datasphere.mdm.core.type.data.Attribute;

/**
 * @author theseusyang
 * Simple upath result.
 */
public class UPathResult {
    /**
     * Collection mode.
     */
    private final UPathApplicationMode mode;
    /**
     * Attributes.
     */
    private final List<Attribute> attributes = new ArrayList<>(8);
    /**
     * Incomplete paths.
     */
    private final List<UPathIncompletePath> incomplete = new ArrayList<>(8);
    /**
     * Constructor.
     * @param mode the mode, which has been used while attribute collecting.
     */
    public UPathResult(UPathApplicationMode mode) {
        super();
        this.mode = mode;
    }
    /**
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }
    /**
     * @return the incomplete
     */
    public List<UPathIncompletePath> getIncomplete() {
        return incomplete;
    }
    /**
     * @return the mode
     */
    public UPathApplicationMode getMode() {
        return mode;
    }
    /**
     * Simple empty indicator.
     * @return true, if both records and attributes are empty
     */
    public boolean isEmpty() {
        return attributes.isEmpty() && incomplete.isEmpty();
    }
}
