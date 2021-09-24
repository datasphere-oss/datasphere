

package com.huahui.datasphere.mdm.system.context;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A context type, which is capable to store something in its internal storage.
 */
public interface StorageCapableContext {
    /**
     * Puts an object to the storage.
     * @param <T> the type of the object to store
     * @param <R> the type of an extending context
     * @param id the storage id
     * @param t the object to store
     * @return self
     */
    <T, R extends StorageCapableContext> R putToStorage(@Nonnull StorageId id, T t);
    /**
     * Gets a stored object from the context storage.
     * @param <T> the object's type
     * @param id the id
     * @return object or null
     */
    @Nullable
    <T> T getFromStorage(@Nonnull StorageId id);
}
