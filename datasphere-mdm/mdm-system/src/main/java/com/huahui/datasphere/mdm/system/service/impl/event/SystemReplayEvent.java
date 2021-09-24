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

package com.huahui.datasphere.mdm.system.service.impl.event;

import com.huahui.datasphere.mdm.system.type.event.AbstractMulticastEvent;

/**
 * @author theseusyang on Oct 29, 2019
 */
public class SystemReplayEvent extends AbstractMulticastEvent {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = -6947329119790339516L;
    /**
     * This type name.
     */
    private static final String TYPE_NAME = "SYSTEM_REPLAY_EVENT";
    /**
     * The number of receivers to receive the original message.
     */
    private final int receiverCount;
    /**
     * Constructor.
     * @param typeName
     * @param id
     */
    public SystemReplayEvent(String id, int receiverCount) {
        super(TYPE_NAME, id);
        this.receiverCount = receiverCount;
    }
    /**
     * @return the receiverCount
     */
    public int getReceiverCount() {
        return receiverCount;
    }
}
