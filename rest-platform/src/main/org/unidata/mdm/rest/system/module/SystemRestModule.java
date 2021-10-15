package org.unidata.mdm.rest.system.module;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.unidata.mdm.system.service.ModularPostProcessingRegistrar;
import org.unidata.mdm.system.type.module.AbstractModule;
import org.unidata.mdm.system.type.module.Dependency;

import com.huahui.datasphere.rest.system.service.ReceiveInInterceptor;

/**
 * @author Mikhail Mikhailov on Apr 24, 2020
 */
public class SystemRestModule extends AbstractModule {
    /**
     * This module ID.
     */
    public static final String MODULE_ID = "org.unidata.mdm.rest.system";

    /**
     * Dependencies.
     */
    private static final Collection<Dependency> DEPENDENCIES
        = Arrays.asList(new Dependency("org.unidata.mdm.system", "6.0"));

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
