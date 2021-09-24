

package com.huahui.datasphere.mdm.system.context;

/**
 * Set up aware context.
 */
public interface SetupAwareContext extends BooleanFlagsContext {
    /**
     * Adds setup awareness to a context.
     * @return true for set up context, false otherwise
     */
    default boolean setUp() {
        return getFlag(SystemContextFlags.FLAG_IS_SETUP);
    }
    /**
     * Sets a context to set up state.
     * @param value the value to set
     */
    default void setUp(boolean value) {
        setFlag(SystemContextFlags.FLAG_IS_SETUP, value);
    }
}
