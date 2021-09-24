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

package com.huahui.datasphere.mdm.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.huahui.datasphere.mdm.system.context.DraftAwareContext;
import com.huahui.datasphere.mdm.system.exception.PlatformFailureException;
import com.huahui.datasphere.mdm.system.exception.ValidationResult;
import com.huahui.datasphere.mdm.system.service.EventService;
import com.huahui.datasphere.mdm.system.service.ExecutionService;
import com.huahui.datasphere.mdm.system.type.event.Event;
import com.huahui.datasphere.mdm.system.type.event.EventReceiver;

import com.huahui.datasphere.mdm.core.context.ModelChangeContext;
import com.huahui.datasphere.mdm.core.context.ModelGetContext;
import com.huahui.datasphere.mdm.core.context.ModelRefreshContext;
import com.huahui.datasphere.mdm.core.context.ModelRemoveContext;
import com.huahui.datasphere.mdm.core.context.ModelSourceContext;
import com.huahui.datasphere.mdm.core.context.RefreshModelContext;
import com.huahui.datasphere.mdm.core.dao.StorageModelDAO;
import com.huahui.datasphere.mdm.core.dto.ModelGetResult;
import com.huahui.datasphere.mdm.core.exception.CoreExceptionIds;
import com.huahui.datasphere.mdm.core.service.MetaModelService;
import com.huahui.datasphere.mdm.core.service.ModelIdentitySupport;
import com.huahui.datasphere.mdm.core.service.ModelRefreshListener;
import com.huahui.datasphere.mdm.core.service.impl.instance.StorageInstanceImpl;
import com.huahui.datasphere.mdm.core.type.event.RefreshModelEvent;
import com.huahui.datasphere.mdm.core.type.event.RefreshModelEvent.RefreshModelTuple;
import com.huahui.datasphere.mdm.core.type.model.ModelDescriptor;
import com.huahui.datasphere.mdm.core.type.model.ModelImplementation;
import com.huahui.datasphere.mdm.core.type.model.ModelInstance;
import com.huahui.datasphere.mdm.core.type.model.StorageInstance;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;

/**
 * The Class BaseMetaModelService.
 *
 * @author Michael Yashin. Created on 26.05.2015.
 */
@Service
public class MetaModelServiceImpl implements MetaModelService, EventReceiver, ModelIdentitySupport {
    /**
     * Storage info set.
     */
    private final AtomicReference<StorageInstance> storage = new AtomicReference<>();
    /**
     * Model type implementors.
     */
    private final Map<String, ModelImplementation<?>> implementors = new HashMap<>();
    /**
     * Refresh listeners.
     */
    private Map<String, List<ModelRefreshListener>> refreshers = new HashMap<>();
    /**
     * The ES.
     */
    @Autowired
    private ExecutionService executionService;
    /**
     * Refresh executor.
     * Exists solely to prevent hoging of the HZ posting thread.
     */
    private final ExecutorService refresher = Executors.newSingleThreadExecutor(new CustomizableThreadFactory("model-refresh-thread-"));
    /**
     * STorage DAO.
     */
    @Autowired
    private StorageModelDAO storageDAO;
    /**
     * Event service - 'update model', 'reload model' are sent.
     */
    @Autowired
    private EventService eventService;
    /**
     * Constructor.
     */
    public MetaModelServiceImpl() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        refresher.shutdown();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public StorageInstance getStorageInstance() {
        return storage.get();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void register(ModelImplementation<?> mi) {
        Objects.requireNonNull(mi, "Model implementor must not be null.");
        Objects.requireNonNull(mi.descriptor(), "Model implementor's descriptor must not be null.");
        implementors.put(mi.descriptor().getModelTypeId(), mi);

        Collection<ModelRefreshListener> listeners = mi.listeners();
        if (CollectionUtils.isNotEmpty(listeners)) {
            for (ModelRefreshListener l : listeners) {
                refreshers.computeIfAbsent(l.getTypeId(), k -> new ArrayList<>())
                          .add(l);
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ModelImplementation<?> implementation(String modelTypeId) {
        return implementors.get(modelTypeId);
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <I extends ModelInstance<?>> I instance(ModelDescriptor<I> descriptor, String storageId, String id) {
        ModelImplementation<?> mi = implementors.get(descriptor.getModelTypeId());
        if (Objects.nonNull(mi)) {
            return (I) mi.instance(storageId, id);
        }
        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public <I extends ModelInstance<?>> I instance(ModelDescriptor<I> descriptor, String id) {
        return instance(descriptor, SecurityUtils.getCurrentUserStorageId(), id);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public <I extends ModelInstance<?>> I instance(ModelDescriptor<I> descriptor) {
        return instance(descriptor, null);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void upsert(ModelChangeContext change) {

        // 1. Exec ordinary pipeline. Draft objects expected to be handled by implementations.
        executionService.execute(change);

        // 2. Run overall pipeline refresh for ordinary updates
        boolean isDraft = (change instanceof DraftAwareContext) && ((DraftAwareContext) change).isDraftOperation();
        if (!isDraft) {

            Collection<RefreshModelTuple> collected = collectAsTuples(change);
            if (change.waitForFinish()) {
                eventService.fireAndWait(new RefreshModelEvent(collected));
            } else {
                eventService.fire(new RefreshModelEvent(collected));
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void refresh(ModelRefreshContext refresh) {

        executionService.execute(refresh);
        if (refresh.isLocal()) {

            List<ModelRefreshListener> listeners = refreshers.get(refresh.getTypeId());
            if (CollectionUtils.isNotEmpty(listeners)) {
                refresher.execute(() -> {
                    for (ModelRefreshListener l : listeners) {
                        l.refresh(refresh);
                    }
                });
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(ModelRemoveContext remove) {
        executionService.execute(remove);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public <X extends ModelGetResult> X get(ModelGetContext get) {
        // 2. Exec ordinary pipeline
        return executionService.execute(get);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ValidationResult> allow(ModelSourceContext<?> change) {

        List<ValidationResult> collected = new ArrayList<>();
        String thisTypeId = change.getTypeId();
        for (Entry<String, ModelImplementation<?>> i : implementors.entrySet()) {

            if (StringUtils.equals(i.getKey(), thisTypeId)) {
                continue;
            }

            Collection<ValidationResult> result = i.getValue().allow(change);
            if (CollectionUtils.isNotEmpty(result)) {
                collected.addAll(result);
            }
        }

        return collected;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void receive(Event event) {

        if (event instanceof RefreshModelEvent) {
            RefreshModelEvent rme = (RefreshModelEvent) event;
            rme.getPayload().forEach(tuple ->
                refresh(RefreshModelContext.builder()
                    .typeId(tuple.getTypeId())
                    .instanceId(tuple.getInstanceId())
                    .storageId(tuple.getStorageId())
                    .local(rme.isLocal())
                    .build()));
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterModuleStartup() {

        try {

            storage.set(new StorageInstanceImpl(storageDAO.loadAll()));
            // Subscribe for distributed model changes
            eventService.register(this, RefreshModelEvent.class);

        } catch (Exception e) {
            final String message = "Metadata service failed to initialize.";
            throw new PlatformFailureException(message, e, CoreExceptionIds.EX_META_INIT_METADATA_FAILED);
        }
    }
}