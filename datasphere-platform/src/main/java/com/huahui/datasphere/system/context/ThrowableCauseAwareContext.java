

package com.huahui.datasphere.system.context;

import java.util.Objects;

import io.micrometer.core.lang.Nullable;

/**
 * @author theseusyang
 * Throwable cause indicator.
 */
public interface ThrowableCauseAwareContext extends StorageCapableContext {
    /**
     * The throwable cause.
     */
    StorageId SID_THROWABLE_CAUSE = new StorageId("THROWABLE_CAUSE");
    /**
     * Returns true, if this context has throwable cause set and, thus, has performed an operation,
     * that caused a throw.
     * @return true, if has throwable set
     */
    default boolean hasThrown() {
        return Objects.nonNull(throwableCause());
    }
    /**
     * Get throwable cause
     * @return throwable or null
     */
    @Nullable
    default Throwable throwableCause() {
        return getFromStorage(SID_THROWABLE_CAUSE);
    }
    /**
     * Put throwable cause to the context
     * @param throwableCause the cause
     */
    default void throwableCause(Throwable throwableCause) {
        putToStorage(SID_THROWABLE_CAUSE, throwableCause);
    }
}
