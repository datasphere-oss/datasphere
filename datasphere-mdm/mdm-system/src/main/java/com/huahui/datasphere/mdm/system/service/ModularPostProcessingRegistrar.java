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

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author theseusyang
 */
public interface ModularPostProcessingRegistrar {
    /**
     * Registers a {@link BeanPostProcessor} to be used in child (modules) contexts.
     * @param beanPostProcessor the processor
     */
    void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor);
    /**
     * Registers a {@link BeanFactoryPostProcessor} to be used in child (modules) contexts.
     * @param beanFactoryPostProcessor the processor
     */
    void registerBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor);
    /**
     * Registers a {@link ModulePostProcessor}.
     * @param modulePostProcessor the processor
     */
    void registerModulePostProcessor(ModulePostProcessor modulePostProcessor);
}
