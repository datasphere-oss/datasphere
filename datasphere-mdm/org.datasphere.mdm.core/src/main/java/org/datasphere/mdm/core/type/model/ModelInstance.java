package org.datasphere.mdm.core.type.model;

import java.time.OffsetDateTime;

/**
 * @author Mikhail Mikhailov on Oct 2, 2020
 * An instance of a model of particular type.
 */
public interface ModelInstance<X extends ModelSource> extends NamedDisplayableElement, CustomPropertiesElement, VersionedElement {
    /**
     * Gets the storage id.
     * @return storage id
     */
    String getStorageId();
    /**
     * Gets the model instance id, the source is for.
     * This is either real instance name/id (i. e. classifier name) for models that support multiple instances,
     * or just DEFAULT or null for singleton instances, such as DATA, SOURCE_SYSTEMS, ENUMERATIONS, MEASUREMENT_UNITS etc.
     * @return model instance id
     */
    String getInstanceId();
    /**
     * Returns the model type id from configuration (the id supplied with model type descriptor for this model type).
     * @return model type id
     */
    String getTypeId();
    /**
     * Create date.
     */
    OffsetDateTime getCreateDate();
    /**
     * Gets author's name.
     * @return version author's user name
     */
    String getCreatedBy();
    /**
     * Assembles itself to source from current state.
     * @return self as source
     */
    X toSource();
    /**
     * Executed before adding to the system cache.
     */
    default void beforeAdd() {
        // Nothing since not all model types support this.
    }
    /**
     * Executed before removal from the system cache.
     */
    default void beforeRemove() {
        // Nothing since not all model types support this.
    }
    /**
     * Returns true if no items are registered.
     * @return true, if empty
     */
    boolean isEmpty();
}
