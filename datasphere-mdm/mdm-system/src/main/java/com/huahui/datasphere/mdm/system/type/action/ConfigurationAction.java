package com.huahui.datasphere.mdm.system.type.action;

import javax.annotation.Nonnull;

/**
 * @author theseusyang on Nov 3, 2020
 * Cluster configuration action.
 */
public interface ConfigurationAction {
    /**
     * Run n times.
     * @return number of times to execute the action
     */
    int getTimes();
    /**
     * Gets action id.
     * @return id
     */
    @Nonnull
    String getId();
    /**
     * Runs action, returning success indicator.
     * Execution counter will be increased, if true.
     * @return true, for success and counter increase
     */
    boolean execute();
}
