package com.huahui.datasphere.system.type.rendering;

import com.huahui.datasphere.system.dto.OutputContainer;
import com.huahui.datasphere.system.exception.PlatformRuntimeException;

/**
 * Runs rendering actions upon exceptional executions.
 * @author Alexey Tsarapkin
 */
public interface ErrorOutputFragmentRenderer {
    /**
     * Runs rendering action.
     * @param container the container
     * @param e the exception
     * @param sink the sink
     * @return true, if successful, false otherwise
     */
    boolean onError(OutputContainer container, PlatformRuntimeException e, OutputSink sink);
}
