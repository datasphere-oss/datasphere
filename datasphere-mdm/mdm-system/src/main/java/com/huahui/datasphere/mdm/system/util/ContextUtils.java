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

package com.huahui.datasphere.mdm.system.util;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import com.huahui.datasphere.mdm.system.configuration.SystemConfiguration;
import com.huahui.datasphere.mdm.system.service.ModuleService;

/**
 * @author theseusyang
 */
public final class ContextUtils {
    /**
     * The MS.
     */
    private static ModuleService moduleService;
    /**
     * Constructor.
     */
    private ContextUtils() {
        super();
    }
    /**
     * Public "internal" init method.
     */
    public static void init() {
        moduleService = SystemConfiguration.getBean(ModuleService.class);
    }
    /**
     * Creates properties bean by path.
     * @param path the path
     * @return bean
     */
    public static PropertiesFactoryBean classpathPropertiesFactoryBean(final String path) {
        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource(path));
        return propertiesFactoryBean;
    }
    /**
     * Initializes an "orphan" bean (the one, which is not in the context).
     * @param <T> bean type
     * @param bean the bean to process
     * @return bean (may be proxied by CGLib)
     */
    public static<T> T initOrphanBean(@Nonnull T bean) {
        return moduleService.initOrphanBean(bean);
    }
    /**
     * Initializes an "orphan" bean (the one, which is not in the context).
     * @param <T> bean type
     * @param beanName the bean name
     * @param bean the bean to process
     * @return bean (may be proxied by CGLib)
     */
    public static<T> T initOrphanBean(String beanName, @Nonnull T bean) {
        return moduleService.initOrphanBean(beanName, bean);
    }
    /**
     * Doas just injection of the fields into the bean.
     * @param bean the bean to process.
     */
    public static void autowireOrphanBean(Object bean) {
        moduleService.autowireOrphanBean(bean);
    }
}
