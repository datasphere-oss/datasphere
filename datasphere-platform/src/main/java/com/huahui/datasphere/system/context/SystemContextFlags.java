package com.huahui.datasphere.system.context;

/**
 * @author theseusyang
 * System boolean flags.
 */
public final class SystemContextFlags {
    /**
     * Context setup hint.
     */
    public static final int FLAG_IS_SETUP = CommonRequestContext.FLAG_ID_PROVIDER.getAndIncrement();
    /**
     * Constructor.
     */
    private SystemContextFlags() {
        super();
    }
}
