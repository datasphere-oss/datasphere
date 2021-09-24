

package com.huahui.datasphere.mdm.system.context;

/**
 * Storage ID marker.
 */
public interface StorageSpecificContext {
    /**
     * Returns specific storate id for operation.
     * @return storage id
     */
    String getStorageId();
}
