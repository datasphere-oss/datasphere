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

package com.huahui.datasphere.mdm.system.service.impl.module;

import java.util.Collection;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.huahui.datasphere.mdm.system.configuration.SystemConfiguration;
import com.huahui.datasphere.mdm.system.type.module.Module;

/**
 * Parent context +.
 * @author theseusyang on Oct 25, 2019
 */
public class ModularContextBuilder {
    /**
     * The last "sentinel" context name.
     */
    public static final String SENTINEL_CONTEXT_NAME = "SENTINEL_CONTEXT_NAME";
    /**
     * The parent context to use.
     */
    private AbstractApplicationContext parent;
    /**
     * Custom class loader, if needed.
     */
    private ClassLoader customClassLoader;
    /**
     * The module, being currentlu initialized.
     */
    private Module module;
    /**
     * Non-default post processors to run on children contexts.
     */
    private Collection<BeanPostProcessor> postProcessors;
    /**
     * BF post-processors.
     */
    private Collection<BeanFactoryPostProcessor> beanFactoryPostProcessors;
    /**
     * Create sentinel context.
     */
    private boolean sentinel;
    /**
     * Builder method.
     * @param parent the parent (system) context
     * @return builder
     */
    public static ModularContextBuilder builder(AbstractApplicationContext parent) {
        Objects.requireNonNull(parent, "Parent context must not be null!");
        return new ModularContextBuilder(parent);
    }
    /**
     * Constructor.
     */
    private ModularContextBuilder(AbstractApplicationContext parent) {
        super();
        this.parent = parent;
    }
    /**
     * Sets a custom classloader.
     * @param customClassLoader the class loader to set
     * @return self
     */
    public ModularContextBuilder customClassLoader(ClassLoader customClassLoader) {
        this.customClassLoader = customClassLoader;
        return this;
    }
    /**
     * Sets this module state.
     * @param module the module being initialized
     * @return self
     */
    public ModularContextBuilder module(Module module) {
        this.module = module;
        return this;
    }
    /**
     * Sets non-default post processors to run on children contexts.
     * @param postProcessors the post processors
     * @return self
     */
    public ModularContextBuilder beanPostProcessors(Collection<BeanPostProcessor> postProcessors) {
        this.postProcessors = postProcessors;
        return this;
    }
    /**
     * Creates sentinel context.
     * @param sentinel the sentinel flag
     * @return self
     */
    public ModularContextBuilder sentinel(boolean sentinel) {
        this.sentinel = sentinel;
        return this;
    }
    /**
     * Sets non-default bean factory post processors to run on children contexts.
     * @param beanFactoryPostProcessors the post processors
     * @return self
     */
    public ModularContextBuilder beanFactoryPostProcessors(Collection<BeanFactoryPostProcessor> beanFactoryPostProcessors) {
        this.beanFactoryPostProcessors = beanFactoryPostProcessors;
        return this;
    }
    /**
     * Builds a child context.
     * @return a new context
     */
    public AnnotationConfigApplicationContext build() {

        // 0. Validate state
        if (!this.sentinel) {
            Objects.requireNonNull(this.module, "Module must not be null");
        }

        ModularAnnotationConfigApplicationContext result
            = new ModularAnnotationConfigApplicationContext(
                    this, customClassLoader, new ModularListableBeanFactory(this));

        result.setParent(parent);
        if (!this.sentinel) {
            // 2. Set parent and subcontext ID
            result.setId(module.getId());

            // 3. Scan this module package and its subpackages
            result.scan(module.getId());

            // 4. This module doesn't use spring
            if (result.getBeanDefinitionCount() == 0) {
                return null;
            }
        } else {
            result.setId(SENTINEL_CONTEXT_NAME);
        }

        // 5. Refresh
        result.refresh();

        // 6. Add resource basenames to parent MS, if those are defined
        if (!this.sentinel && ArrayUtils.isNotEmpty(module.getResourceBundleBasenames())) {
            SystemConfiguration configuration = parent.getBean(SystemConfiguration.class);
            ResourceBundleMessageSource rbms = configuration.getSystemMessageSource();
            rbms.addBasenames(module.getResourceBundleBasenames());
        }

        // 7. And return
        return result;
    }
    /**
     * The bean factory.
     * @author theseusyang on Oct 25, 2019
     */
    private class ModularListableBeanFactory extends DefaultListableBeanFactory {
        /**
         * Constructor.
         * @param parentBeanFactory
         */
        public ModularListableBeanFactory(ModularContextBuilder builder) {
            super();
            // Non-default global post-processors
            if (CollectionUtils.isNotEmpty(builder.postProcessors)) {
                getBeanPostProcessors().addAll(builder.postProcessors);
            }
        }
    }
    /**
     * Our own context.
     * @author theseusyang on Oct 25, 2019
     */
    private class ModularAnnotationConfigApplicationContext extends AnnotationConfigApplicationContext {
        /**
         * Constructor.
         * @param beanFactory
         */
        public ModularAnnotationConfigApplicationContext(ModularContextBuilder builder, ClassLoader customClassLoader, DefaultListableBeanFactory beanFactory) {
            super(beanFactory);
            if (Objects.nonNull(customClassLoader)) {
                setClassLoader(customClassLoader);
            }

            // Non-default global post-processors
            if (CollectionUtils.isNotEmpty(builder.beanFactoryPostProcessors)) {
                getBeanFactoryPostProcessors().addAll(builder.beanFactoryPostProcessors);
            }
        }
    }
}
