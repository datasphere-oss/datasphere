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

package com.huahui.datasphere.mdm.system.service.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Service;

import com.huahui.datasphere.mdm.system.configuration.SystemConfiguration;
import com.huahui.datasphere.mdm.system.dao.ModuleDAO;
import com.huahui.datasphere.mdm.system.dto.ModuleInfo;
import com.huahui.datasphere.mdm.system.exception.PlatformFailureException;
import com.huahui.datasphere.mdm.system.exception.SystemExceptionIds;
import com.huahui.datasphere.mdm.system.module.SystemModule;
import com.huahui.datasphere.mdm.system.service.AfterModuleStartup;
import com.huahui.datasphere.mdm.system.service.AfterPlatformStartup;
import com.huahui.datasphere.mdm.system.service.MessagingService;
import com.huahui.datasphere.mdm.system.service.ModulePostProcessor;
import com.huahui.datasphere.mdm.system.service.ModuleService;
import com.huahui.datasphere.mdm.system.service.RuntimePropertiesService;
import com.huahui.datasphere.mdm.system.service.impl.module.ModularAnnotationConfigWebApplicationContext;
import com.huahui.datasphere.mdm.system.service.impl.module.ModularContextBuilder;
import com.huahui.datasphere.mdm.system.type.annotation.ModuleRef;
import com.huahui.datasphere.mdm.system.type.module.Dependency;
import com.huahui.datasphere.mdm.system.type.module.Module;
import com.huahui.datasphere.mdm.system.type.support.IdentityHashSet;

/**
 * @author theseusyang
 * Simple implementation of the module service.
 */
@Service
public class ModuleServiceImpl implements ModuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleServiceImpl.class);

    /**
     * Needed for inclusion of parent post processor classes.
     */
    private static final Class<?>[] INITIAL_BEAN_FACTORY_POST_PROCESSORS = {
        PropertySourcesPlaceholderConfigurer.class,
        CustomEditorConfigurer.class
    };

    private final RuntimePropertiesService runtimePropertiesService;

    private final MessagingService messagingService;

    private final ModuleDAO moduleDao;

    private final List<ModuleInfo> modulesInfo = new ArrayList<>();

    private final Map<String, Module> startedModules = new HashMap<>();

    private final Map<String, Module> readyModules = new HashMap<>();

    private final Set<String> readyFailedModules = new HashSet<>();
    // Only singletones are actually accepted
    private final Set<BeanPostProcessor> beanPostProcessors = new IdentityHashSet<>();
    // Only singletones are actually accepted
    private final Set<BeanFactoryPostProcessor> beanFactoryPostProcessors = new IdentityHashSet<>();
    // Only singletones are actually accepted
    private final Set<ModulePostProcessor> modulePostProcessors = new IdentityHashSet<>();

    private ModularAnnotationConfigWebApplicationContext modularRootContext;

    private AbstractApplicationContext currentContext;

    public ModuleServiceImpl(
            final RuntimePropertiesService runtimePropertiesService,
            final MessagingService messagingService,
            final ModuleDAO moduleDao) {
        this.runtimePropertiesService = runtimePropertiesService;
        this.messagingService = messagingService;
        this.moduleDao = moduleDao;
    }

    @Override
    public void loadModules(final Set<Class<Module>> modulesClasses) {

        Objects.requireNonNull(modularRootContext, "Current application context must be set");
        final Map<String, ModuleInfo> installedModules = moduleDao.fetchModulesInfo().stream()
                        .collect(Collectors.toMap(mi -> mi.getModule().getId(), Function.identity()));

        // 1. Prepare post-processors
        for (Class<?> ppc : INITIAL_BEAN_FACTORY_POST_PROCESSORS) {
            try {
                BeanFactoryPostProcessor bfpp = (BeanFactoryPostProcessor) modularRootContext.getBean(ppc);
                beanFactoryPostProcessors.add(bfpp);
            } catch (BeansException e) {
                // Nothing
            }
        }

        beanPostProcessors.add(this);
        beanPostProcessors.add(runtimePropertiesService);
        beanPostProcessors.add(messagingService);

        modulePostProcessors.add(runtimePropertiesService);
        modulePostProcessors.add(messagingService);

        // 2. Load stuff
        LOGGER.info("Starting loading modules...");
        final Collection<ModuleInfo> loadedModulesInfo = loadModules(modulesClasses, installedModules);
        moduleDao.saveModulesInfo(loadedModulesInfo);
        this.modulesInfo.addAll(loadedModulesInfo);

        // 3. Create sentinel context for "foreign' beans
        AnnotationConfigApplicationContext result = ModularContextBuilder.builder(currentContext)
                .customClassLoader(null) // Just for a possible future use
                .sentinel(true)
                .beanPostProcessors(beanPostProcessors)
                .beanFactoryPostProcessors(beanFactoryPostProcessors)
                .build();

        // Add last sentinel context to stack
        modularRootContext.addChild(result);

        // 4. Set root context ready state and call ready on modules
        modularRootContext.setReady(true);

        callReady();
        LOGGER.info("Modules loaded.");
    }

    private void callReady() {
        startedModules.values().forEach(this::callReady);
    }

    private void callReady(Module module) {

        final String moduleId = module.getId();
        if (readyModules.containsKey(moduleId) || readyFailedModules.contains(moduleId)) {
            return;
        }

        if (module.getDependencies() != null) {
            module.getDependencies().forEach(d -> callReady(startedModules.get(d.getModuleId())));
            if (module.getDependencies().stream().anyMatch(d -> readyFailedModules.contains(d.getModuleId()))) {
                readyFailedModules.add(moduleId);
                return;
            }
        }

        try {

            module.ready();

            // Call afterPlatformStartup on found beans, but only on this context (without ancestors).
            AbstractApplicationContext context = modularRootContext.getChild(module.getId());
            Map<String, AfterPlatformStartup> beans
                = context.getBeanFactory().getBeansOfType(AfterPlatformStartup.class, true, false);
            if (MapUtils.isNotEmpty(beans)) {
                beans.values().forEach(AfterPlatformStartup::afterPlatformStartup);
            }

            readyModules.put(moduleId, module);
        } catch (Exception e) {
            LOGGER.error("Error happened while call ready() on {}", moduleId, e);
            readyFailedModules.add(moduleId);
        }
    }

    private Collection<ModuleInfo> loadModules(
            final Set<Class<Module>> modulesClasses,
            final Map<String, ModuleInfo> installedModules) {

        final Map<String, ModuleInfo> modules = loadModulesInfo(modulesClasses);
        modules.values().forEach(moduleInfo -> loadModule(moduleInfo, modules, installedModules));

        return modules.values();
    }

    private Map<String, ModuleInfo> loadModulesInfo(Set<Class<Module>> modulesClasses) {
        return modulesClasses.stream()
                .map(moduleClass -> {
                    final Module module = loadModuleObject(moduleClass);
                    if (module == null) {
                        return null;
                    }
                    return new ModuleInfo(module);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(moduleInfo -> moduleInfo.getModule().getId(), Function.identity()));
    }

    private void loadModule(
            final ModuleInfo moduleInfo,
            final Map<String, ModuleInfo> modules,
            final Map<String, ModuleInfo> installedModules) {

        final String moduleId = moduleInfo.getModule().getId();
        LOGGER.info("Starting loading module {}", moduleId);
        if (moduleInfo.getModuleStatus() == ModuleInfo.ModuleStatus.LOADED
                || moduleInfo.getModuleStatus() == ModuleInfo.ModuleStatus.FAILED
                || moduleInfo.getModuleStatus() == ModuleInfo.ModuleStatus.INSTALLATION_FAILED
                || moduleInfo.getModuleStatus() == ModuleInfo.ModuleStatus.START_FAILED) {
            LOGGER.info("Module {} was loaded early", moduleId);
            return;
        }

        moduleInfo.setModuleStatus(ModuleInfo.ModuleStatus.LOADING);
        if (!loadModuleDependencies(moduleInfo, modules, installedModules)) {
            return;
        }

        final Module module = moduleInfo.getModule();

        // Load context, if needed.
        if (SystemModule.MODULE_ID.equals(moduleId)) {

            // Load module resources
            loadModuleMessages(module);
            // Save context
            initModulePostProcess(module, currentContext);
            // Add current context to stack
            modularRootContext.addChild(currentContext);
        } else {

            // Run module pre processing.
            initModulePreProcess(module);

            // Create context, if applicable.
            final AnnotationConfigApplicationContext result = initModuleSpringContext(module);

            // Post process
            initModulePostProcess(module, result);
            if (result != null) {

                currentContext = result;
                // Add current context to stack
                modularRootContext.addChild(currentContext);
            }
        }

        if (wasNotInstalled(moduleId, installedModules, moduleInfo)
         && moduleInfo.getModuleStatus() != ModuleInfo.ModuleStatus.FAILED) {
            try {
                LOGGER.info("Installing module {}", moduleId);
                module.install();
                LOGGER.info("Module {} was installing", moduleId);
            } catch (Exception e) {
                moduleInfo.setModuleStatus(ModuleInfo.ModuleStatus.INSTALLATION_FAILED);
                moduleInfo.setError("Module " + moduleId + " installation failed");
                LOGGER.error("Installation of module {} failed", moduleId, e);
                return;
            }
        }

        try {

            LOGGER.info("Starting module {}", moduleId);

            module.start();

            // Call afterModuleStartup on found beans, but only on this context (without ancestors).
            AbstractApplicationContext context = modularRootContext.getLast();
            Map<String, AfterModuleStartup> beans = context
                    .getBeanFactory()
                    .getBeansOfType(AfterModuleStartup.class, true, false);

            if (MapUtils.isNotEmpty(beans)) {
                beans.values().forEach(AfterModuleStartup::afterModuleStartup);
            }

            LOGGER.info("Module {} was started", moduleId);
        } catch (Exception e) {
            moduleInfo.setModuleStatus(ModuleInfo.ModuleStatus.START_FAILED);
            moduleInfo.setError("Module starting failed");
            LOGGER.error("Starting of module {} failed", moduleId, e);
            return;
        }

        moduleInfo.setModuleStatus(ModuleInfo.ModuleStatus.LOADED);
        startedModules.put(moduleId, module);
        LOGGER.info("Module {} was loaded", moduleId);
    }

    private boolean wasNotInstalled(String moduleId, Map<String, ModuleInfo> installedModules, ModuleInfo moduleInfo) {
        final ModuleInfo mi = installedModules.get(moduleId);
        return !installedModules.containsKey(moduleId)
                || !Objects.equals(mi.getModule().getVersion(), moduleInfo.getModule().getVersion())
                || mi.getModuleStatus() == ModuleInfo.ModuleStatus.FAILED
                || mi.getModuleStatus() == ModuleInfo.ModuleStatus.INSTALLATION_FAILED;
    }

    private boolean loadModuleDependencies(
            final ModuleInfo moduleInfo,
            final Map<String, ModuleInfo> modules,
            final Map<String, ModuleInfo> installedModules) {

        final Collection<Dependency> dependencies = moduleInfo.getModule().getDependencies();
        if (dependencies == null || dependencies.isEmpty()) {
            return true;
        }

        final String moduleId = moduleInfo.getModule().getId();
        for (final Dependency dependency : dependencies) {

            final ModuleInfo dependencyModuleInfo = modules.get(dependency.getModuleId());
            if (dependencyModuleInfo == null) {
                moduleInfo.setModuleStatus(ModuleInfo.ModuleStatus.FAILED);
                moduleInfo.setError("Unknown dependency " + dependency);
                LOGGER.error(
                        "Module {} has unknown dependency {}:{}~{}",
                        moduleId,
                        dependency.getModuleId(),
                        dependency.getVersion(),
                        dependency.getTag()
                );
                return false;
            }

            if (!Objects.equals(dependencyModuleInfo.getModule().getVersion(), dependency.getVersion())) {
                moduleInfo.setModuleStatus(ModuleInfo.ModuleStatus.FAILED);
                moduleInfo.setError("Unknown dependency version " + dependency);
                LOGGER.error("Module {} has unknown dependency version {}", moduleId, dependency);
                return false;
            }

            if (dependencyModuleInfo.getModuleStatus() == ModuleInfo.ModuleStatus.LOADING) {
                moduleInfo.setModuleStatus(ModuleInfo.ModuleStatus.FAILED);
                moduleInfo.setError("Cyclic loading module " + moduleId);
                LOGGER.error("Module {} has cyclic dependencies", moduleId);
                return false;
            }

            if (dependencyModuleInfo.getModuleStatus() == ModuleInfo.ModuleStatus.NOT_LOADED) {
                loadModule(dependencyModuleInfo, modules, installedModules);
            }

            if (dependencyModuleInfo.getModuleStatus() == ModuleInfo.ModuleStatus.FAILED
                    || dependencyModuleInfo.getModuleStatus() == ModuleInfo.ModuleStatus.START_FAILED) {
                moduleInfo.setModuleStatus(ModuleInfo.ModuleStatus.FAILED);
                moduleInfo.setError("Dependency has failed status " + dependency);
                LOGGER.error("Module {} has failed dependency {}", moduleId, dependency.getModuleId());
                return false;
            }
        }

        return true;
    }

    private Module loadModuleObject(final Class<Module> moduleClass) {
        try {
            final Constructor<?> constructor = moduleClass.getConstructor();
            return (Module) constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            LOGGER.error("Error while creating module {} object", moduleClass.getName(), e);
            return null;
        }
    }

    private void initModulePreProcess(Module m) {
        // Possibly read interesting things from a module
        modulePostProcessors.forEach(mpp -> mpp.postProcessBeforeInitialization(m));
    }

    private AnnotationConfigApplicationContext initModuleSpringContext(Module module) {

        try {
            // Create context
            return ModularContextBuilder.builder(currentContext)
                .customClassLoader(null) // Just for a possible future use
                .module(module)
                .beanPostProcessors(beanPostProcessors)
                .beanFactoryPostProcessors(beanFactoryPostProcessors)
                .build();

        } catch (Exception e) {
            final String message = "Spring context for module id '{}' failed to load.";
            throw new PlatformFailureException(message, e, SystemExceptionIds.EX_SYSTEM_CONTAINER_INIT_FAILED, module.getId());
        }
    }

    private void initModulePostProcess(Module module, AbstractApplicationContext context) {

        // Possibly post process module.
        if (Objects.nonNull(context)) {
            // Inject stuff, but don't call any callbacks such as @PostConstruct etc.
            context.getAutowireCapableBeanFactory().autowireBean(module);
        }

        // Autowire configuration vars and other post processing stuff
        modulePostProcessors.forEach(mpp -> mpp.postProcessAfterInitialization(module));
    }

    private void loadModuleMessages(Module module) {
        String [] bundles = module.getResourceBundleBasenames();
        if (ArrayUtils.isNotEmpty(bundles)) {
            currentContext.getBean(SystemConfiguration.class).getSystemMessageSource().addBasenames(bundles);
        }
    }

    @Override
    public Collection<ModuleInfo> modulesInfo() {
        return modulesInfo;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Module> getModules() {
        return startedModules.values();
    }

    @Override
    public Optional<Module> findModuleById(String moduleId) {
        return Optional.ofNullable(startedModules.get(moduleId));
    }

    @PreDestroy
    public void stop() {
        startedModules.values().forEach(Module::stop);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {

        try {

            List<Field> fields = FieldUtils.getFieldsListWithAnnotation(bean.getClass(), ModuleRef.class);
            for (Field f : fields) {

                boolean accessible = f.isAccessible();
                f.setAccessible(true);

                Class<?> fieldType = f.getType();
                Module hit = null;
                // 1. We need the id
                if (fieldType == Module.class) {
                    String requestedId = f.getAnnotation(ModuleRef.class).value();
                    hit = startedModules.get(requestedId);
                // 2. Exact type was given. Select one.
                } else {
                    for (Module i : startedModules.values()) {
                        if (i.getClass() == fieldType) {
                            hit = i;
                            break;
                        }
                    }
                }

                f.set(bean, hit);
                f.setAccessible(accessible);
            }
        } catch (IllegalAccessException e) {
            LOGGER.warn("IAE caught while post-processing.", e);
        }

        return bean;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.add(beanPostProcessor);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor) {
        beanFactoryPostProcessors.add(beanFactoryPostProcessor);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerModulePostProcessor(ModulePostProcessor modulePostProcessor) {
        modulePostProcessors.add(modulePostProcessor);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setRootContext(ApplicationContext applicationContext) {
        this.modularRootContext = (ModularAnnotationConfigWebApplicationContext) applicationContext;
        this.currentContext = (AbstractApplicationContext) applicationContext;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T createRuntimeSingleton(Class<T> klass) {
        return createRuntimeSingleton(klass.getName(), klass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T createRuntimeSingleton(String beanName, Class<T> klass) {

        if (!modularRootContext.isReady()) {
            LOGGER.warn("Platform is not in ready state. Aborting.");
            return null;
        }

        AbstractApplicationContext aac = modularRootContext.getLast();
        AutowireCapableBeanFactory acbf = aac.getAutowireCapableBeanFactory();
        BeanDefinitionRegistry bdr = (BeanDefinitionRegistry) acbf;

        GenericBeanDefinition myBeanDefinition = new GenericBeanDefinition();
        myBeanDefinition.setBeanClass(klass);
        myBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

        bdr.registerBeanDefinition(beanName, myBeanDefinition);

        return aac.getBean(beanName, klass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T initOrphanBean(T bean) {
        Objects.requireNonNull(bean, "Bean must not be null.");
        return initOrphanBean(bean.getClass().getName(), bean);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T initOrphanBean(String beanName, T bean) {
        Objects.requireNonNull(bean, "Bean must not be null.");

        AbstractApplicationContext aac = modularRootContext.getLast();
        AutowireCapableBeanFactory acbf = aac.getAutowireCapableBeanFactory();

        acbf.autowireBean(bean);
        return (T) acbf.initializeBean(bean, beanName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void autowireOrphanBean(T bean) {
        Objects.requireNonNull(bean, "Bean must not be null.");

        AbstractApplicationContext aac = modularRootContext.getLast();
        AutowireCapableBeanFactory acbf = aac.getAutowireCapableBeanFactory();

        acbf.autowireBean(bean);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void releaseOrphanBean(T bean) {

        Objects.requireNonNull(bean, "Bean must not be null.");

        AbstractApplicationContext aac = modularRootContext.getLast();
        AutowireCapableBeanFactory acbf = aac.getAutowireCapableBeanFactory();

        acbf.destroyBean(bean);
    }
}
