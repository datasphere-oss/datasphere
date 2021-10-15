/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.huahui.datasphere.mdm.core.service;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.huahui.datasphere.mdm.system.exception.ValidationResult;
import com.huahui.datasphere.mdm.system.service.AfterModuleStartup;

import com.huahui.datasphere.mdm.core.context.ModelChangeContext;
import com.huahui.datasphere.mdm.core.context.ModelGetContext;
import com.huahui.datasphere.mdm.core.context.ModelRefreshContext;
import com.huahui.datasphere.mdm.core.context.ModelRemoveContext;
import com.huahui.datasphere.mdm.core.context.ModelSourceContext;
import com.huahui.datasphere.mdm.core.dto.ModelGetResult;
import com.huahui.datasphere.mdm.core.type.model.ModelDescriptor;
import com.huahui.datasphere.mdm.core.type.model.ModelImplementation;
import com.huahui.datasphere.mdm.core.type.model.ModelInstance;
import com.huahui.datasphere.mdm.core.type.model.StorageInstance;

/**
 * The Interface MetaModelService.
 */
public interface MetaModelService extends AfterModuleStartup {
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
     * Registers a model implementation.
     * @param mi the implementation
     */
    void register(@Nonnull ModelImplementation<?> mi);
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
     * @param id the instance id
     * @return instance
     */
    <I extends ModelInstance<?>> I instance(@Nonnull ModelDescriptor<I> descriptor, String id);
    /**
     * Gets model instance by type descriptor using specific storage id and default instance id.
     * @param <I> source type
     * @param descriptor the descriptor
     * @param storageId the storage id
     * @param id the instance id
     * @return instance
     */
    <I extends ModelInstance<?>> I instance(@Nonnull ModelDescriptor<I> descriptor, String storageId, String id);
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
}
