package org.datasphere.mdm.core.type.model;

import java.util.Collection;

import javax.annotation.Nullable;

/**
 * @author Mikhail Mikhailov on Oct 5, 2020
 */
public interface StorageInstance {
    /**
     * Gets all registered storage elements.
     * @return collection
     */
    Collection<StorageElement> getActive();
    /**
     * Gets element for current user.
     * @return element
     */
    StorageElement getStorageForCurrentUser();
    /**
     * Gets storage by id.
     * @param storageId the id
     * @return element or null
     */
    @Nullable
    StorageElement getStorageById(String storageId);
}
