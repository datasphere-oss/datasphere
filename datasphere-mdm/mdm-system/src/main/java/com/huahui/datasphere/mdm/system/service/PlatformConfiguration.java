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

package com.huahui.datasphere.mdm.system.service;

import com.huahui.datasphere.mdm.system.type.format.DumpTargetFormat;

/**
 * @author theseusyang
 * Some system-wide visible configuration fields.
 */
public interface PlatformConfiguration extends PlatformIdentitySource {
    /**
     * Major number.
     */
    int getPlatformMajor();
    /**
     * Minor number.
     */
    int getPlatformMinor();
    /**
     * Patch number.
     */
    int getPlatformPatch();
    /**
     * Revision number.
     */
    int getPlatformRevision();
    /**
     * Node ID.
     */
    String getNodeId();
    /**
     * The default dump target format.
     */
    DumpTargetFormat getDumpTargetFormat();
    /**
     * Tells the caller, whether the platform runs in developer mode.
     * @return true, if so, false otherwise
     */
    boolean isDeveloperMode();
}
