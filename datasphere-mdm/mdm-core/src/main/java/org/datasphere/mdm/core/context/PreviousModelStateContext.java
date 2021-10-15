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
package com.huahui.datasphere.mdm.core.context;

import com.huahui.datasphere.mdm.system.context.StorageCapableContext;
import com.huahui.datasphere.mdm.system.context.StorageId;

import com.huahui.datasphere.mdm.core.type.model.ModelInstance;

/**
 * @author theseusyang on Nov 2, 2020
 * Previous model state holder.
 */
public interface PreviousModelStateContext extends StorageCapableContext {
    /**
     * The notification id SID.
     */
    StorageId SID_PREVIOUS_MODEL_STATE = new StorageId("PREVIOUS_MODEL_STATE");
    /**
     * Gets state.
     * @param <M> the expected model type
     * @return state or null
     */
    default <M extends ModelInstance<?>> M prevousState() {
        return getFromStorage(SID_PREVIOUS_MODEL_STATE);
    }
    /**
     * Puts state.
     * @param <M> the expected model type
     * @param m the state
     */
    default <M extends ModelInstance<?>> void prevousState(M m) {
        putToStorage(SID_PREVIOUS_MODEL_STATE, m);
    }
}
