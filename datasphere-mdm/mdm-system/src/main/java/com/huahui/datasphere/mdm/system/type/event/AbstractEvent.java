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

import com.huahui.datasphere.mdm.system.util.IdUtils;

/**
 * @author theseusyang on Oct 28, 2019
 */
public abstract class AbstractEvent implements Event {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 6935407258627041109L;
    /**
     * The type name.
     */
    private final String typeName;
    /**
     * The id.
     */
    private final String id;
    /**
     * Sets internal replay flag.
     */
    private boolean withReplay;
    /**
     * Constructor.
     */
    protected AbstractEvent(String typeName, String id) {
        super();
        this.typeName = typeName;
        this.id = id;
    }
    /**
     * Constructor.
     */
    protected AbstractEvent(String typeName) {
        this(typeName, IdUtils.v1String());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTypeName() {
        return typeName;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }
    /**
     * @return the withReplay
     */
    @Override
    public boolean withReplay() {
        return withReplay;
    }
    /**
     * @param withReplay the withReplay to set
     */
    public void setReplay(boolean withReplay) {
        this.withReplay = withReplay;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Event: id [" + id + "], type [" + typeName + "], broadcast [" + getBroadcastType().name() + "], replay [" + withReplay + "]";
    }
}
