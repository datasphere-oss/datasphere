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

package com.huahui.datasphere.mdm.core.type.data;

import org.apache.commons.lang3.StringUtils;

/**
 * @author theseusyang
 * An attribute, which is a link to a code attribute.
 */
public interface CodeLinkValue {
    /**
     * Gets the link etalon id.
     * @return etalon id
     */
    String getLinkEtalonId();
    /**
     * Sets the link id.
     * @param id the id to set
     */
    void setLinkEtalonId(String id);
    /**
     * Tells, whether this attribute has link etalon id.
     * @return true, if so, false otherwise
     */
    default boolean hasLinkEtalonId() {
        return StringUtils.isNoneBlank(getLinkEtalonId());
    }
}
