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

import java.io.Serializable;

public interface Event extends Serializable {
    /**
     * This might be useful for listeners / subscribers, that do not filetr on the event type.
     * @return type name
     */
    String getTypeName();
    /**
     * Gets the id.
     * @return the ebent id
     */
    String getId();
    /**
     * Gets the type of broadcasting.
     * @return type
     */
    BroadcastType getBroadcastType();
    /**
     * Returns true, if the event should be replied.
     * @return true for awaited replay, false otherwise
     */
    default boolean withReplay() {
        return false;
    }
    /**
     * Returns true, if sender and receiver are on the same node.
     * In other words
     * <ul>
     * <li> always true for {@link AbstractLocalEvent}</li>
     * <li> always false for {@link AbstractForeignEvent}</li>
     * <li> may be true or false for {@link AbstractMulticastEvent}</li>
     * </ul>
     * @return true, if sender and receiver are on the same node.
     */
    boolean isLocal();
}
