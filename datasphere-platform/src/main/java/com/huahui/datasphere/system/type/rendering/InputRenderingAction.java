package com.huahui.datasphere.system.type.rendering;

/**
 * @author Mikhail Mikhailov on Jan 15, 2020
 */
public interface InputRenderingAction extends RenderingAction {
    @Override
    default RenderingActionType actionType() {
        return RenderingActionType.INPUT;
    }
}
