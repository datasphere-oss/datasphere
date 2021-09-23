package com.huahui.datasphere.system.type.rendering;

/**
 * @author Mikhail Mikhailov on Jan 15, 2020
 */
public class VoidOutputSink implements OutputSink {

    public static final VoidOutputSink INSTANCE = new VoidOutputSink();

    private VoidOutputSink() {
        super();
    }
}
