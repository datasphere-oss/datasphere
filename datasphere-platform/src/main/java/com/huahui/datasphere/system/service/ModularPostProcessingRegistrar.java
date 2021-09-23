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

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author Mikhail Mikhailov on Dec 27, 2019
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
