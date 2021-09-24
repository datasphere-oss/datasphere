

package com.huahui.datasphere.mdm.system.context;

/**
 * _Lots_ of boolean flags - interface to internal bitset.
 */
public interface BooleanFlagsContext {
    /**
     * Sets a known flag to true.
     * @param flag the flag to set
     */
    void setFlag(int flag);
    /**
     * Sets a known flag true or false, according to given state.
     * @param flag the flag to set
     * @param state the state
     */
    default void setFlag(int flag, boolean state) {
        if (state) {
            setFlag(flag);
        } else {
            clearFlag(flag);
        }
    }
    /**
     * Sets a known flag to false.
     * @param flag the flag to clear
     */
    void clearFlag(int flag);
    /**
     * Returns the value of the given flag.
     * @param flag the flag id
     * @return boolean value
     */
    boolean getFlag(int flag);
}
