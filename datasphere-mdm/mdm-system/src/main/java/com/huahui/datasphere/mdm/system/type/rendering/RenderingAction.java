package com.huahui.datasphere.mdm.system.type.rendering;

import java.util.Objects;

/**
 * @author theseusyang on Jan 16, 2020
 */
public interface RenderingAction {

    enum RenderingActionType {
        INPUT,
        OUTPUT,
        ERROR
    }

    default boolean isOneOf(RenderingAction... actions) {

        if (Objects.nonNull(actions)) {
            for (RenderingAction action : actions) {
                if (action == this) {
                    return true;
                }
            }
        }

        return false;
    }

    String name();

    RenderingActionType actionType();
}
