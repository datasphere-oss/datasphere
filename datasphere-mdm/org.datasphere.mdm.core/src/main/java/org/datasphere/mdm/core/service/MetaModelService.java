/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 *
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.service;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.datasphere.mdm.core.context.ModelChangeContext;
import org.datasphere.mdm.core.context.ModelGetContext;
import org.datasphere.mdm.core.context.ModelRefreshContext;
import org.datasphere.mdm.core.context.ModelRemoveContext;
import org.datasphere.mdm.core.context.ModelSourceContext;
import org.datasphere.mdm.core.dto.ModelGetResult;
import org.datasphere.mdm.core.type.model.ModelDescriptor;
import org.datasphere.mdm.core.type.model.ModelImplementation;
import org.datasphere.mdm.core.type.model.ModelInstance;
import org.datasphere.mdm.core.type.model.StorageInstance;
import org.datasphere.mdm.system.exception.ValidationResult;
import org.datasphere.mdm.system.service.AfterModuleStartup;

/**
 * The Interface MetaModelService.
 */
public interface MetaModelService extends AfterModuleStartup {
    /**
     * Registers a model implementation.
     * @param mi the implementation
     */
    void register(@Nonnull ModelImplementation<?> mi);
    /**
     * Cleanup and shutdown.
     */
    void shutdown();
    /**
     * Gets the sole storage info instance.
     * @return storage info instance
     */
    StorageInstance getStorageInstance();
    /**
     * Gets model implementation by model type id.
     * @param modelTypeId the type id
     * @return instance or null, if not found
     */
    @Nullable
    ModelImplementation<?> implementation(String modelTypeId);
    /**
     * Gets model instance by type descriptor using current user's storage id and default instance id.
     * @param <I> source type
     * @param descriptor the descriptor
     * @return instance
     */
    <I extends ModelInstance<?>> I instance(@Nonnull ModelDescriptor<I> descriptor);
    /**
     * Gets model instance by type descriptor using current user's storage id and given instance id.
     * @param <I> source type
     * @param descriptor the descriptor
     * @param instanceId the instance id
     * @return instance
     */
    <I extends ModelInstance<?>> I instance(@Nonnull ModelDescriptor<I> descriptor, String instanceId);
    /**
     * Gets model instance by type descriptor using specific storage id and default instance id.
     * @param <I> source type
     * @param descriptor the descriptor
     * @param storageId the storage id
     * @param instanceId the instance id
     * @return instance
     */
    <I extends ModelInstance<?>> I instance(@Nonnull ModelDescriptor<I> descriptor, String storageId, String instanceId);
    /**
     * Gets model instance by type descriptor using specific storage id and default instance id.
     * @param <I> source type
     * @param descriptor the descriptor
     * @param draftId the draft id
     * @param storageId the storage id
     * @param instanceId the instance id
     * @return instance
     */
    <I extends ModelInstance<?>> I instance(@Nonnull ModelDescriptor<I> descriptor, Long draftId, String storageId, String instanceId);
    /**
     * Performs change operation on the input.
     * @param change the change context
     */
    void upsert(@Nonnull ModelChangeContext change);
    /**
     * Performs refresh operation on the input.
     * @param refresh the refresh context
     */
    void refresh(@Nonnull ModelRefreshContext refresh);
    /**
     * Performs remove operation on the input.
     * @param remove the remove context
     */
    void remove(@Nonnull ModelRemoveContext remove);
    /**
     * Performs get operation on the input.
     * @param <X> the exact return type
     * @param get the get context
     * @return result
     */
    <X extends ModelGetResult> X get(@Nonnull ModelGetContext get);
    /**
     * Runs 'allow' on all model type handlers, except that of the supplied context.
     * @param change the change
     * @return validation results
     */
    @Nonnull
    Collection<ValidationResult> allow(@Nonnull ModelSourceContext<?> change);
    /**
     * Registers a model refresh listener.
     * @param l the listener
     */
    void listener(ModelRefreshListener l);
}
