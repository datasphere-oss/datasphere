package com.huahui.datasphere.mdm.system.service;

/**
 * Runs methods after {@link com.huahui.datasphere.mdm.system.type.module.Module#start()}.
 * @author theseusyang
 */
public interface AfterModuleStartup {
    /**
     * Runs method after {@link com.huahui.datasphere.mdm.system.type.module.Module#start()}.
     */
    void afterModuleStartup();
}
