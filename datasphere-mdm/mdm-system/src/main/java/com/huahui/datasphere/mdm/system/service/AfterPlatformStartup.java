package com.huahui.datasphere.mdm.system.service;

/**
 * Runs methods after {@link com.huahui.datasphere.mdm.system.type.module.Module#ready()}.
 * @author theseusyang
 */
public interface AfterPlatformStartup {
    /**
     * Runs method after {@link com.huahui.datasphere.mdm.system.type.module.Module#ready()}.
     */
    void afterPlatformStartup();
}
