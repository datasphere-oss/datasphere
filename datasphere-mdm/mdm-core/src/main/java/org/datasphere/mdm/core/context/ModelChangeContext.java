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

import javax.annotation.Nullable;

/**
 * @author theseusyang on Oct 8, 2020
 * Model change marker, the model descriptor operates on.
 */
public interface ModelChangeContext extends ModelIdentityContext {
    /**
     * Wait, until changes applied and the return.
     * @return true, if set to wait.
     */
    boolean waitForFinish();
    /**
     * The version to apply (current + 1 otherwise).
     * Upsert will fail if the version is older then current + 1 and 'force' is false.
     * @return specific version or null, if not set
     */
    @Nullable
    Integer getVersion();
    /**
     * Force upsert, even if supplied version is older then current + 1
     * or concurrent modifications were made during upsert.
     * @return
     */
    boolean force();
    /**
     * Model change type.
     */
    enum ModelChangeType {
        /**
         * One element update.
         * Former PARTIAL_UPDATE
         */
        PARTIAL,
        /**
         * Full replacement - model will be recreated.
         * Former FULLY_NEW.
         */
        FULL,
        /**
         * Current model and existed will be merged.
         * Former ADDITION
         */
        MERGE
    }
}
