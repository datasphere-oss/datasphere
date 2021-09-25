package com.huahui.datasphere.mdm.rest.system.module;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import com.huahui.datasphere.mdm.system.service.ModularPostProcessingRegistrar;
import com.huahui.datasphere.mdm.system.type.module.AbstractModule;
import com.huahui.datasphere.mdm.system.type.module.Dependency;

import com.huahui.datasphere.mdm.rest.system.service.ReceiveInInterceptor;

/**
 * @author theseusyang on Apr 24, 2020
 */
public class SystemRestModule extends AbstractModule {
    /**
     * This module ID.
     */
    public static final String MODULE_ID = "com.huahui.datasphere.mdm.rest.system";

    /**
     * Dependencies.
     */
    private static final Collection<Dependency> DEPENDENCIES
        = Arrays.asList(new Dependency("com.huahui.datasphere.mdm.system", "6.0"));

    @Autowired
    private ModularPostProcessingRegistrar modularPostProcessingRegistrar;

    @Autowired
    private ReceiveInInterceptor receiveInInterceptor;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return MODULE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return "6.0";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "System REST";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "System REST interfaces.";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Dependency> getDependencies() {
        return DEPENDENCIES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        modularPostProcessingRegistrar.registerBeanPostProcessor(receiveInInterceptor);
    }
}
