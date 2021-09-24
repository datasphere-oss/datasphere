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

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

import com.huahui.datasphere.mdm.system.dto.ModuleInfo;
import com.huahui.datasphere.mdm.system.type.module.Module;

/**
 * @author theseusyang
 * Basic functionality around business modules.
 */
public interface ModuleService extends BeanPostProcessor, ModularPostProcessingRegistrar {
    /**
     * Sets current context.
     * @param applicationContext the context to set
     */
    void setRootContext(ApplicationContext applicationContext);
    /**
     * Loads modules during platform startup.
     * @param modulesClasses set of module classes to load
     */
    void loadModules(Set<Class<Module>> modulesClasses);
    /**
     * Gets running modules info.
     * @return modules info
     */
    Collection<ModuleInfo> modulesInfo();
    /**
     * Finds module by module id.
     * @param moduleId the id
     * @return module or null, if not found
     */
    Optional<Module> findModuleById(String moduleId);
    /**
     * Gets a collection of registered modules.
     * @return collection
     */
    Collection<Module> getModules();
    /**
     * Creates singleton beans, that can inject beans from any context after 'ready' was called.
     * The bean is put to {@link BeanFactory}.
     * @param <T> the bean type
     * @param klass the class
     * @return bean
     */
    <T> T createRuntimeSingleton(Class<T> klass);
    /**
     * Creates singleton beans, that can inject beans from any context after 'ready' was called.
     * The bean is put to {@link BeanFactory}.
     * @param <T> the bean type
     * @param beanName alternative bean name
     * @param klass the class
     * @return bean
     */
    <T> T createRuntimeSingleton(String beanName, Class<T> klass);
    /**
     * Does injection into the given bean, runs all registered postprocessors and invokes lifecycle callbacks,
     * BUT does not append the bean to {@link BeanFactory} (does not create BF definition).
     * @param <T> the bean type
     * @param bean the bean instance
     * @return a possibly enhanced bean instance
     */
    <T> T initOrphanBean(@Nonnull T bean);
    /**
     * Does injection into the given bean, runs all registered postprocessors and invokes lifecycle callbacks,
     * BUT does not append the bean to {@link BeanFactory} (does not create BF definition).
     * Uses the given bean name in {@code setBeanName}
     * @param <T> the bean type
     * @param beanName the name to use in {@code setBeanName}
     * @param bean the bean instance
     * @return a possibly enhanced bean instance
     */
    <T> T initOrphanBean(String beanName, @Nonnull T bean);
    /**
     * Does just injection into the given bean and runs all registered postprocessors.
     * Does NOT append the bean to {@link BeanFactory} (does not create BF definition).
     * @param <T> the bean type
     * @param bean the bean instance
     * @return a possibly enhanced bean instance
     */
    <T> void autowireOrphanBean(@Nonnull T bean);
    /**
     * Does destruction of the given bean, runs all registered destruction postprocessors and invokes lifecycle callbacks.
     * @param <T> the bean type
     * @param bean the bean instance
     * @return a possibly enhanced bean instance
     */
    <T> void releaseOrphanBean(@Nonnull T bean);
}
