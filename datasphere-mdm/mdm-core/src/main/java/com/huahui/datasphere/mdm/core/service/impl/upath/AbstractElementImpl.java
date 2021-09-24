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

package com.huahui.datasphere.mdm.core.service.impl.upath;

import com.huahui.datasphere.mdm.core.type.upath.UPathElement;
import com.huahui.datasphere.mdm.core.type.upath.UPathElementType;

/**
 * @author theseusyang
 * UPath element base.
 */
public abstract class AbstractElementImpl implements UPathElement {
    /**
     * Path element.
     */
    private final String element;
    /**
     * Type of element.
     */
    private final UPathElementType type;
    /**
     * Constructor.
     * @param element the original element
     * @param type element type {@link UPathElementType}.
     */
    protected AbstractElementImpl(String element, UPathElementType type) {
        super();
        this.element = element;
        this.type = type;
    }
    /**
     * @return the element
     */
    @Override
    public String getElement() {
        return element;
    }
    /**
     * @return the type
     */
    @Override
    public UPathElementType getType() {
        return type;
    }
}