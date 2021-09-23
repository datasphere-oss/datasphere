/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 *
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.huahui.datasphere.system.service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

import com.huahui.datasphere.system.dto.ModuleInfo;
import com.huahui.datasphere.system.type.module.Module;

/**
 * @author Alexander Malyshev
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
