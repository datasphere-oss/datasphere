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
package com.huahui.datasphere.mdm.core.type.model;

/**
 * @author theseusyang on Feb 16, 2021
 * Marks implementation as a library sourced element.
 */
public interface LibrarySourceElement {
    /**
     * Returns the name of the jar/zip/whatever file, that contains the implementing class.
     * @return the name of the jar/zip/whatever file, that contains the implementing class.
     */
    String getLibrary();
    /**
     * Version of the library.
     * @return version of the library.
     */
    String getVersion();
}
