package com.huahui.datasphere.mdm.system.serialization;

/**
 * @author theseusyang
 */
public interface SerializedVariable {
    /**
     * Tells, whether this variable is valid.
     * @return true, if valid, false otherwise
     */
    boolean isValid();
    /**
     * Sets the variable's name.
     * @param name the name to set
     */
    void setName(String name);
}
