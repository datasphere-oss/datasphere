package com.huahui.datasphere.mdm.system.service;

import com.huahui.datasphere.mdm.system.type.module.Module;

/**
 * @author theseusyang
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
