

package com.huahui.datasphere.system.context;

/**
 * @author theseusyang
 * Storage ID marker.
 */
public interface StorageSpecificContext {
    /**
     * Returns specific storate id for operation.
     * @return storage id
     */
    String getStorageId();
}
