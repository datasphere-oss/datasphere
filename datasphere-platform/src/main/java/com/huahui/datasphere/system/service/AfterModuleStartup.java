package com.huahui.datasphere.system.service;

/**
 * Runs methods after {@link com.huahui.datasphere.system.type.module.Module#start()}.
 * @author Mikhail Mikhailov on Apr 1, 2020
 */
public interface AfterModuleStartup {
    /**
     * Runs method after {@link com.huahui.datasphere.system.type.module.Module#start()}.
     */
    void afterModuleStartup();
}
