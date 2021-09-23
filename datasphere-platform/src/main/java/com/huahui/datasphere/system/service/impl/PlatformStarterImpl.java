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
package com.huahui.datasphere.system.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.huahui.datasphere.system.exception.PlatformFailureException;
import com.huahui.datasphere.system.service.ModuleService;
import com.huahui.datasphere.system.service.PlatformStarter;
import com.huahui.datasphere.system.service.impl.module.ModularAnnotationConfigWebApplicationContext;
import com.huahui.datasphere.system.type.module.Module;

@Service
public class PlatformStarterImpl implements PlatformStarter, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformStarterImpl.class);

    private static final String MODULE_CLASS_ATTRIBUTE = "Unidata-Module-Class";

    private final ModuleService moduleService;

    public PlatformStarterImpl(
            final ModuleService moduleService
    ) {
        this.moduleService = moduleService;
    }

    @Override
    public void onApplicationEvent(@Nonnull ContextRefreshedEvent event) {
        try {

            // Spring fires refresh events for every context, put to hierarchy, thus making a stop here each time.
            // Run once. Set parent once.
            if (event.getApplicationContext() instanceof ModularAnnotationConfigWebApplicationContext) {

                moduleService.setRootContext(event.getApplicationContext());

                final Set<Class<Module>> classes = loadModuleClasses();
                classes.forEach(c -> preload(c, event.getApplicationContext()));
                moduleService.loadModules(classes);
            }

        } catch (Exception e) {
            LOGGER.error("Failed to start platform.", e);
            ((ConfigurableApplicationContext) event.getApplicationContext()).close();
        }
    }

    @Nonnull
    private Set<Class<Module>> loadModuleClasses() {
        try {
            final Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
            final Set<Class<Module>> modulesClasses = new HashSet<>();
            while (resources.hasMoreElements()) {
                final Manifest manifest = new Manifest(resources.nextElement().openStream());
                final Attributes attributes = manifest.getMainAttributes();
                final String moduleClass = StringUtils.trimToNull(attributes.getValue(MODULE_CLASS_ATTRIBUTE));
                if (moduleClass != null) {
                    final Class<Module> aClass = loadClass(moduleClass);
                    if (aClass != null) {
                        modulesClasses.add(aClass);
                    }
                }
            }
            return modulesClasses;
        } catch (IOException e) {
            LOGGER.error("Error while loading modules", e);
            throw new PlatformFailureException("Can't load modules", e, null);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<Module> loadClass(final String moduleClassName) {
        try {
            return (Class<Module>) getClass().getClassLoader().loadClass(moduleClassName);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Module class not found", e);
            return null;
        }
    }

    private void preload(
            @Nonnull final Class<Module> moduleClass,
            final ApplicationContext applicationContext
    ) {
        try {
            final Method preloadMethod = moduleClass.getMethod("preload", ApplicationContext.class);
            if (preloadMethod != null) {
                preloadMethod.invoke(moduleClass, applicationContext);
            }
        } catch (NoSuchMethodException ignore) {
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("Preload method invocation error", e);
            throw new PlatformFailureException("Can't invoke preload method", e, null);
        }
    }
}
