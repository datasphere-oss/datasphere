package com.huahui.datasphere.system.type.rendering;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Mikhail Mikhailov on Jan 15, 2020
 */
public interface RenderingProvider {
    /**
     * Gets renderer for an action or null
     * @param action the action
     * @return renderer or null
     */
    @Nullable
    Collection<InputFragmentRenderer> get(@Nonnull InputRenderingAction action);
    /**
     * Gets renderer for an action or null
     * @param action the action
     * @return renderer or null
     */
    @Nullable
    Collection<OutputFragmentRenderer> get(@Nonnull OutputRenderingAction action);
    /**
     * Gets renderer for errors or null
     * @param action the action
     * @return renderer or null
     */
    @Nullable
    default ErrorOutputFragmentRenderer onError(@Nonnull OutputRenderingAction action) {
        return null;
    }
}
