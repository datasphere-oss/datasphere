package com.huahui.datasphere.mdm.system.type.rendering;

/**
 * @author theseusyang on Jan 15, 2020
 */
public interface InputRenderingAction extends RenderingAction {
    @Override
    default RenderingActionType actionType() {
        return RenderingActionType.INPUT;
    }
}
