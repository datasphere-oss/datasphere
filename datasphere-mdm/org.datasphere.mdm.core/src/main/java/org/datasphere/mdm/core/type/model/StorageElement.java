package org.datasphere.mdm.core.type.model;

import java.time.Instant;

public interface StorageElement {
    /**
     * Gets the storage id.
     * @return id
     */
    String getStorageId();
    /**
     * Gets the description.
     * @return decsription
     */
    String getDescription();
    /**
     * Gets created by.
     * @return created by
     */
    String getCreatedBy();
    /**
     * Gets create date.
     * @return create date
     */
    Instant getCreateDate();
    /**
     * Gets updated by.
     * @return updated by
     */
    String getUpdatedBy();
    /**
     * Gets update date.
     * @return update date
     */
    Instant getUpdateDate();
}
