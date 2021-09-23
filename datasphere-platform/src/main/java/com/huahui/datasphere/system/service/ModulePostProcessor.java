package com.huahui.datasphere.system.service;

import com.huahui.datasphere.system.type.module.Module;

/**
 * @author Mikhail Mikhailov on Jul 14, 2020
 */
public interface ModulePostProcessor {
    /**
     * Runs the procedure right after a module has been instantiated, but before it is initialized.
     * @param m the module to process
     */
    default void postProcessBeforeInitialization(Module m) {
        // Does nothing.
    }
    /**
     * Runs initialization procedure after a module has been initialized by the {@link ModuleService}.
     * @param m the module to process
     */
    default void postProcessAfterInitialization(Module m) {
        // Does nothing.
    }
}
