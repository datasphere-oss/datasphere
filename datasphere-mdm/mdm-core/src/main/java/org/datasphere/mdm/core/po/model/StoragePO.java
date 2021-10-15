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

/**
 *
 */
package com.huahui.datasphere.mdm.core.po.model;

import com.huahui.datasphere.mdm.core.po.AbstractObjectPO;

/**
 * @author theseusyang
 * Storage PO.
 */
public class StoragePO extends AbstractObjectPO {
    /**
     * Table name.
     */
    public static final String TABLE_NAME = "storage";
    /**
     * ID.
     */
    public static final String FIELD_ID = "id";
    /**
     * Name.
     */
    public static final String FIELD_DECSRIPTION = "description";
    /**
     * ID.
     */
    protected String id;
    /**
     * Storage id.
     */
    protected String description;
    /**
     * Constructor.
     */
    public StoragePO() {
        super();
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
