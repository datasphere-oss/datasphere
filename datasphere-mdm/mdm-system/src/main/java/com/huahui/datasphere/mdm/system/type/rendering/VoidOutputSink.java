package com.huahui.datasphere.mdm.system.type.rendering;

/**
 * @author theseusyang on Jan 15, 2020
 */
public class VoidOutputSink implements OutputSink {

    public static final VoidOutputSink INSTANCE = new VoidOutputSink();

    private VoidOutputSink() {
        super();
    }
}
