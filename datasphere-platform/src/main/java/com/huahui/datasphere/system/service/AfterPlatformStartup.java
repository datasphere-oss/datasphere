package com.huahui.datasphere.system.service;

/**
 * Runs methods after {@link com.huahui.datasphere.system.type.module.Module#ready()}.
 * @author Mikhail Mikhailov on Apr 1, 2020
 */
public interface AfterPlatformStartup {
    /**
     * Runs method after {@link com.huahui.datasphere.system.type.module.Module#ready()}.
     */
    void afterPlatformStartup();
}
