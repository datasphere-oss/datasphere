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

import java.util.UUID;

/**
 * @author theseusyang
 * Id generation routines.
 */
public interface PlatformIdentitySource {
    /**
     * Gets UUID v1 string.
     * @return string
     */
    String v1IdString();
    /**
     * Gets UUID v1.
     * @return {@link UUID}
     */
    UUID v1Id();
    /**
     * Gets UUID v4 string.
     * @return string
     */
    String v4IdString();
    /**
     * Gets UUID v4.
     * @return {@link UUID}
     * @return
     */
    UUID v4Id();
}
