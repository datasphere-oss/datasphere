

package com.huahui.datasphere.system.context;

/**
 * @author theseusyang
 * Object id for transient values in a context.
 */
public final class StorageId {
    /**
     * The name of the ID.
     */
    private final String name;
    /**
     * Constructor.
     * @param name the name of the ID
     */
    public StorageId(String name) {
        this.name = name;
    }
    /**
     * Gets the tag of this ID.
     * @return TAG/name of this id
     */
    String getName() {
        return name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }
}
