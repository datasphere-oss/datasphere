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
package com.huahui.datasphere.mdm.core.type.libraries;

/**
 * @author theseusyang on Feb 1, 2021
 * UL updates listener.
 */
public interface UserLibrariesListener {
    /**
     * Library with the given properties was either updated or inserted.
     * @param storage the storage id
     * @param name the name
     * @param version the version
     */
    void libraryUpserted(String storage, String name, String version);
    /**
     * Library with the given properties was removed.
     * @param storage the storage id
     * @param name the name
     * @param version the version
     */
    void libraryRemoved(String storage, String name, String version);
}
