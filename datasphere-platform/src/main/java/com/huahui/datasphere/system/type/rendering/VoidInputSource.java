package com.huahui.datasphere.system.type.rendering;

/**
 * @author Mikhail Mikhailov on Jan 15, 2020
 */
public class VoidInputSource implements InputSource {

    public static final VoidInputSource INSTANCE = new VoidInputSource();

    private VoidInputSource() {
        super();
    }
}
