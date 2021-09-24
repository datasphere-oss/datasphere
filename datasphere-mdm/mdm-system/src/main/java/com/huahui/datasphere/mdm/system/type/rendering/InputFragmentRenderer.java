package com.huahui.datasphere.mdm.system.type.rendering;

import java.util.Collections;
import java.util.List;

import com.huahui.datasphere.mdm.system.context.InputCollector;

/**
 * @author theseusyang on Jan 15, 2020
 */
@FunctionalInterface
public interface InputFragmentRenderer {
    /**
     * Renders a portion (or the whole) source input to the collector.
     * @param version api version
     * @param collector the collector
     * @param source the input source
     */
    void render(String version, InputCollector collector, InputSource source);
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
