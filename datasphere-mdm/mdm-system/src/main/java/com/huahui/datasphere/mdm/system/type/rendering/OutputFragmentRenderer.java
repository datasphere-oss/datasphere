package com.huahui.datasphere.mdm.system.type.rendering;

import java.util.Collections;
import java.util.List;

import com.huahui.datasphere.mdm.system.dto.OutputContainer;

/**
 * @author theseusyang on Jan 15, 2020
 */
public interface OutputFragmentRenderer {
    /**
     * Renders the output content to the given output sink.
     * @param version api version
     * @param container the container
     * @param sink the sink
     */
    void render(String version, OutputContainer container, OutputSink sink);
    /**
     * Gets the order of execution.
     * The lower is the order, the earlier will the renderer be executed.
     * @return preferred order of execution
     */
    default int order() {
        return 0;
    }

    /**
     * Returns the description of the fields of the fragment
     * @return fragment fields
     */
    default List<FragmentDef> fragmentFields() {
        return Collections.emptyList();
    }
}
