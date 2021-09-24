
package com.huahui.datasphere.mdm.system.context;

import java.util.Objects;

/**
 * Contexts, that potentially participate in draft operations, can implement this interface and components,
 * serving those contexts can decide whether to start a draft operation or do it in some other way.
 */
public interface DraftAwareContext {
    /**
     * Gets draft id.
     * @return draft id
     */
    Long getDraftId();
    /**
     * Gets draft parent id.
     * @return the parent draft id
     */
    Long getParentDraftId();
    /**
     * Returns true, if this context defines a valid draftId field,
     * @return true, if this context defines a valid draftId field,
     */
    default boolean hasDraftId() {
        return Objects.nonNull(getDraftId()) && getDraftId() > 0;
    }
    /**
     * Returns true, if this context defines a valid parent draftId field,
     * @return true, if this context defines a valid parent draftId field,
     */
    default boolean hasParentDraftId() {
        return Objects.nonNull(getParentDraftId()) && getParentDraftId() > 0;
    }
    /**
     * Returns true, if one of the draft ids is set,
     * marking this context as capable for a draft operation.
     * @return true, if one of the draft ids is set
     */
    default boolean isDraftOperation() {
        return hasDraftId() || hasParentDraftId();
    }
}
