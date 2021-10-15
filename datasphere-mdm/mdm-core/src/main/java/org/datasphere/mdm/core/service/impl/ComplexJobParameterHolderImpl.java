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

package com.huahui.datasphere.mdm.core.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

import org.springframework.stereotype.Service;

import com.huahui.datasphere.mdm.core.service.job.ComplexJobParameterHolder;

/**
 * Object responsible fot containing complex parameters for job execution.
 */
@Service
public class ComplexJobParameterHolderImpl implements ComplexJobParameterHolder {

    /**
     * Map of complex parameters
     */
    private Map<String, Object> complexParameters = new ConcurrentHashMap<>();

    @Override
    public void putComplexParameter(@Nonnull String key, @Nonnull Object parameter) {
        complexParameters.put(key, parameter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getComplexParameter(@Nonnull String key) {
        return (T) complexParameters.get(key);
    }

    @Override
    public void removeComplexParameter(@Nonnull String key) {
        complexParameters.remove(key);
    }

    @Override
    public <T> T getComplexParameterAndRemove(@Nonnull String complexParameterKey) {
        T result = getComplexParameter(complexParameterKey);
        removeComplexParameter(complexParameterKey);
        return result;
    }
}
