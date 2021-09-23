package com.huahui.datasphere.system.context;

/**
 * @author theseusyang
 * Marks a context as capable to reset start type id.
 */
public interface StartTypeIdResettingContext {
    /**
     * Overrides or sets the start type id.
     * @param startTypeId the id
     */
    void setStartTypeId(String startTypeId);
}
