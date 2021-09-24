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

package com.huahui.datasphere.mdm.core.service.job;

import javax.annotation.Nonnull;

public interface ComplexJobParameterHolder {
    /**
     * Put complex parameter to container.
     * @param key - key
     * @param parameter -  parameter
     */
    void putComplexParameter(@Nonnull String key, @Nonnull Object parameter);
    /**
     * Gets a parameter by key.
     * @param key - key
     * @param <T> -
     * @return complex parameter
     */
    <T> T getComplexParameter(@Nonnull String key);
    /**
     * Clean complex parameter
     * @param key -  key
     */
    void removeComplexParameter(@Nonnull String key);
    /**
     * Combination of methods getComplexParameter() and removeComplexParameter()
     * @param complexParameterKey
     * @param <T>
     * @return
     */
    <T> T getComplexParameterAndRemove(@Nonnull String complexParameterKey);
}
