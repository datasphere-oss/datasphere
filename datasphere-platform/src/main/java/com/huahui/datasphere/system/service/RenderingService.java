package com.huahui.datasphere.system.service;

import java.util.List;

import com.huahui.datasphere.system.context.InputCollector;
import com.huahui.datasphere.system.dto.OutputContainer;
import com.huahui.datasphere.system.exception.PlatformRuntimeException;
import com.huahui.datasphere.system.type.rendering.ErrorOutputFragmentRenderer;
import com.huahui.datasphere.system.type.rendering.FragmentDef;
import com.huahui.datasphere.system.type.rendering.InputFragmentRenderer;
import com.huahui.datasphere.system.type.rendering.InputRenderingAction;
import com.huahui.datasphere.system.type.rendering.InputSource;
import com.huahui.datasphere.system.type.rendering.OutputFragmentRenderer;
import com.huahui.datasphere.system.type.rendering.OutputRenderingAction;
import com.huahui.datasphere.system.type.rendering.OutputSink;

/**
 * @author Mikhail Mikhailov on Jan 15, 2020
 */
public interface RenderingService {
    /**
     * Renders input fragments for the given action.
     * @param version api version
     * @param action the action to render for
     * @param collector the collector for fragments
     * @param source the input data source
     */
    void renderInput(String version, InputRenderingAction action, InputCollector collector, InputSource source);

    /**
     * Renders input fragments for the given action.  With current api version
     * @param action the action to render for
     * @param collector the collector for fragments
     * @param source the input data source
     */
    default void renderInput(InputRenderingAction action, InputCollector collector, InputSource source) {
        renderInput(FragmentDef.CURRENT_VERSION, action, collector, source);
    }
    /**
     * Renders output fragments for the given action.
     * @param version api version
     * @param action the action
     * @param container the container
     * @param sink the sink
     */
    void renderOutput(String version, OutputRenderingAction action, OutputContainer container, OutputSink sink);

    /**
     * Renders output fragments for the given action. With current api version
     * @param action the action
     * @param container the container
     * @param sink the sink
     */
    default void renderOutput(OutputRenderingAction action, OutputContainer container, OutputSink sink) {
        renderOutput(FragmentDef.CURRENT_VERSION, action, container, sink);
    }
    /**
     * Renders output on error.
     * @param action the action
     * @param e the platform exception
     * @param sink the output sink
     * @return true - suppress exception
     */
    boolean renderOutput(OutputRenderingAction action, PlatformRuntimeException e, OutputSink sink);
    /**
     * Renders output with empty fragment container for the given action.
     * @param action the action
     * @param sink the sink
     */
    void renderOutput(OutputRenderingAction action, OutputSink sink);
    /**
     * Loads renderers once upon startup.
     */
    void init();

    /**
     * @return all registered input renderers
     */
    List<InputFragmentRenderer> inputFragmentRenderers();

    /**
     * @return all registered output renderers
     */
    List<OutputFragmentRenderer> outputFragmentRenderers();

    /**
     * @return all registered error renderers
     */
    List<ErrorOutputFragmentRenderer> errorFragmentRenderers();
}
