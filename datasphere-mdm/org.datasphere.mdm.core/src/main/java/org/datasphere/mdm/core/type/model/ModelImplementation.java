package org.datasphere.mdm.core.type.model;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.datasphere.mdm.core.context.ModelChangeContext;
import org.datasphere.mdm.core.context.ModelGetContext;
import org.datasphere.mdm.core.context.ModelRefreshContext;
import org.datasphere.mdm.core.context.ModelRemoveContext;
import org.datasphere.mdm.core.context.ModelSourceContext;
import org.datasphere.mdm.core.dto.ModelGetResult;
import org.datasphere.mdm.core.service.ModelRefreshListener;
import org.datasphere.mdm.system.exception.ValidationResult;

/**
 * @author Mikhail Mikhailov on Oct 7, 2020
 * <br>
 * Meta models in UD are served by this interface.
 * Meta data exists in such an implementation in two flavors
 * <ul>
 * <li> Immutable, prepared, fast links runtime data. It can be queried via {@link ModelImplementation#instance(String, String)} method.</li>
 * <li> Mutable 'source' format. Changes to model are accepted in this format via {@link ModelImplementation#upsert(ModelChangeContext)}. {@link #get(ModelGetContext)} returns data also in this format, which is historically used by REST.</li>
 * </ul>
 */
public interface ModelImplementation<I extends ModelInstance<?>> {
    /**
     * Returns the model descriptor for this implementation.
     * @return descriptor instance
     */
    ModelDescriptor<I> descriptor();
    /**
     * Gets a particular model instance with the given id.
     * @param storageId the storage id
     * @param id the instance id
     * @return instance
     */
    I instance(String storageId, String id);
    /**
     * Gets a particular model instance with the given id and draft id.
     * Method is 'default', since not all model types support draft functionality.
     * @param draftId the draft id
     * @param storageId the storage id
     * @param id the instance id
     * @return instance or null
     */
    @Nullable
    default I instance(Long draftId, String storageId, String id) {
        return null;
    }
    /**
     * Gets particular or whole content of the model in a fashion specific to this model.
     * @param get the input context
     * @return result
     */
    ModelGetResult get(ModelGetContext get);
    /**
     * Does combine steps from
     * <ul>
     * <li> {@link #assemble(ModelChangeContext)}</li>
     * <li> {@link #validate(ModelSource)}</li>
     * <li> {@link #put(String, ModelSource)}</li>
     * </ul>
     * into a single step, if the implementation supports it.
     * Implementations using draft as an intermediate step for model edition should throw from this method.
     * @param change the change
     */
    void upsert(@Nonnull ModelChangeContext change);
    /**
     * Transforms Model changes to a new model source.
     * @param change the change
     * @return new source
     */
    ModelSource assemble(@Nonnull ModelChangeContext change);
    /**
     * Validates state of the model source.
     * @param source the model source
     * @return empty collection if the given source is valid
     */
    Collection<ValidationResult> validate(@Nonnull ModelSource source);
    /**
     * Allow changes as defined by supplied source.
     * The input source can be examined by other model types
     * to allow or deny requested changes (i. e. source system deletion may be disallowed, if the SS is used by data merge settings).
     * @param source a foreign model as it will be published/upserted. Type should be examined and cast accordingly.
     * @return empty collection if the given change is allowed
     */
    default Collection<ValidationResult> allow(@Nonnull ModelSourceContext<?> source) {
        // Some model types are self-contained.
        return Collections.emptyList();
    }
    /**
     * Can be used to invalidate the cache, if implementation caches values locally.
     * @param refresh the refresh info
     */
    void refresh(ModelRefreshContext refresh);
    /**
     * Remove a model from persistent store.
     * @param remove the remove info
     */
    void remove(ModelRemoveContext remove);
    /**
     * Returns collection of refresh listeners.
     * This way foreign models can intercept refresh events, they are interested in.
     * @return collection of refresh listeners
     */
    default Collection<ModelRefreshListener> listeners() {
        return Collections.emptyList();
    }
}
