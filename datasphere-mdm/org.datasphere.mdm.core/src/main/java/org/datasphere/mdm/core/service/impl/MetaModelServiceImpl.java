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

package org.datasphere.mdm.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.context.ModelChangeContext;
import org.datasphere.mdm.core.context.ModelGetContext;
import org.datasphere.mdm.core.context.ModelRefreshContext;
import org.datasphere.mdm.core.context.ModelRemoveContext;
import org.datasphere.mdm.core.context.ModelSourceContext;
import org.datasphere.mdm.core.context.RefreshModelContext;
import org.datasphere.mdm.core.dao.StorageModelDAO;
import org.datasphere.mdm.core.dto.ModelGetResult;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.service.MetaModelService;
import org.datasphere.mdm.core.service.ModelIdentitySupport;
import org.datasphere.mdm.core.service.ModelRefreshListener;
import org.datasphere.mdm.core.service.impl.instance.StorageInstanceImpl;
import org.datasphere.mdm.core.type.event.RefreshModelEvent;
import org.datasphere.mdm.core.type.event.RefreshModelEvent.RefreshModelTuple;
import org.datasphere.mdm.core.type.model.ModelDescriptor;
import org.datasphere.mdm.core.type.model.ModelImplementation;
import org.datasphere.mdm.core.type.model.ModelInstance;
import org.datasphere.mdm.core.type.model.StorageInstance;
import org.datasphere.mdm.core.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.datasphere.mdm.system.context.DraftAwareContext;
import org.datasphere.mdm.system.exception.PlatformFailureException;
import org.datasphere.mdm.system.exception.ValidationResult;
import org.datasphere.mdm.system.service.EventService;
import org.datasphere.mdm.system.service.ExecutionService;
import org.datasphere.mdm.system.type.event.Event;
import org.datasphere.mdm.system.type.event.EventReceiver;

/**
 * The Class BaseMetaModelService.
 *
 * @author Michael Yashin. Created on 26.05.2015.
 */
@Service
public class MetaModelServiceImpl implements MetaModelService, EventReceiver, ModelIdentitySupport {
    /**
     * This class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MetaModelServiceImpl.class);
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
    private final Map<String, List<ModelRefreshListener>> refreshers = new HashMap<>();
    /**
     * The transaction template.
     */
    private final TransactionTemplate transactionTemplate;
    /**
     * Refresh executor.
     * Exists solely to prevent hoging of the HZ posting thread.
     */
    private final ExecutorService refresher = Executors.newSingleThreadExecutor(new CustomizableThreadFactory("model-refresh-thread-"));
    /**
     * The ES.
     */
    @Autowired
    private ExecutionService executionService;
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
    @Autowired
    public MetaModelServiceImpl(final PlatformTransactionManager manager) {
        super();
        this.transactionTemplate = new TransactionTemplate(manager);
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
                listener(l);
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void listener(ModelRefreshListener l) {
        if (Objects.nonNull(l)) {
            refreshers.computeIfAbsent(l.getTypeId(), k -> new ArrayList<>())
                      .add(l);
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
    public <I extends ModelInstance<?>> I instance(ModelDescriptor<I> descriptor, Long draftId, String storageId,
            String instanceId) {

        ModelImplementation<?> mi = implementors.get(descriptor.getModelTypeId());
        if (Objects.nonNull(mi)) {
            return (I) mi.instance(draftId, storageId, instanceId);
        }
        return null;
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <I extends ModelInstance<?>> I instance(ModelDescriptor<I> descriptor, String storageId, String instanceId) {
        ModelImplementation<?> mi = implementors.get(descriptor.getModelTypeId());
        if (Objects.nonNull(mi)) {
            return (I) mi.instance(storageId, instanceId);
        }
        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public <I extends ModelInstance<?>> I instance(ModelDescriptor<I> descriptor, String instanceId) {
        return instance(descriptor, SecurityUtils.getCurrentUserStorageId(), instanceId);
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
    public void upsert(ModelChangeContext change) {

        // 1. Exec ordinary pipeline in ransaction. Draft objects expected to be handled by implementations.
        transactionTemplate.executeWithoutResult(status -> executionService.execute(change));

        // 2. Run overall pipeline refresh for ordinary updates
        if ((change instanceof DraftAwareContext) && ((DraftAwareContext) change).isDraftOperation()) {
            return;
        }

        Callable<Boolean> refresh = () -> {

            Collection<RefreshModelTuple> collected = collectAsTuples(change);
            if (change.waitForFinish()) {
                if (!eventService.fireAndWait(new RefreshModelEvent(collected))) {
                    LOGGER.warn("Model with type ID [{}], instance ID [{}], storage ID [{}] was not published in time!",
                            change.getTypeId(), change.getInstanceId(), change.getStorageId());
                    return false;
                }
            } else {
                eventService.fire(new RefreshModelEvent(collected));
            }

            return true;
        };

        if (change.postponeRefresh()) {
            change.afterTransaction(refresh);
        } else {
            try { refresh.call(); } catch (Exception exc) { /* NOP.*/ }
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
                    for (int i = 0; i < listeners.size(); i++) {
                        listeners.get(i).refresh(refresh);
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
        transactionTemplate.executeWithoutResult(status -> executionService.execute(remove));
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