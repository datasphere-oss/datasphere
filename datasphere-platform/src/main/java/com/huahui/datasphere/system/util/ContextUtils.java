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

package com.huahui.datasphere.system.util;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.unidata.mdm.system.configuration.SystemConfiguration;

import com.huahui.datasphere.system.service.ModuleService;

/**
 * @author Alexander Malyshev
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
