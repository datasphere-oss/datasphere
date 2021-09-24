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

package com.huahui.datasphere.mdm.system.type.event;

/**
 * @author theseusyang on Oct 28, 2019
 */
public abstract class AbstractForeignEvent extends AbstractEvent {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 3508410130810320797L;
    /**
     * Constructor.
     * @param typeName
     * @param id
     */
    protected AbstractForeignEvent(String typeName, String id) {
        super(typeName, id);
    }
    /**
     * Constructor.
     * @param typeName
     */
    protected AbstractForeignEvent(String typeName) {
        super(typeName);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public BroadcastType getBroadcastType() {
        return BroadcastType.FOREIGN;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLocal() {
        return false;
    }
}
