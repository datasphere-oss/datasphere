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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.huahui.datasphere.mdm.system.configuration.SystemConfigurationConstants;
import com.huahui.datasphere.mdm.system.exception.PlatformFailureException;
import com.huahui.datasphere.mdm.system.exception.PlatformValidationException;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.huahui.datasphere.mdm.core.configuration.CoreConfigurationConstants;
import com.huahui.datasphere.mdm.core.context.UserLibraryGetContext;
import com.huahui.datasphere.mdm.core.context.UserLibraryQueryContext;
import com.huahui.datasphere.mdm.core.context.UserLibraryRemoveContext;
import com.huahui.datasphere.mdm.core.context.UserLibraryUpsertContext;
import com.huahui.datasphere.mdm.core.dao.LibrariesDAO;
import com.huahui.datasphere.mdm.core.dto.UserLibraryResult;
import com.huahui.datasphere.mdm.core.exception.CoreExceptionIds;
import com.huahui.datasphere.mdm.core.po.LibraryPO;
import com.huahui.datasphere.mdm.core.service.UserLibraryService;
import com.huahui.datasphere.mdm.core.type.libraries.LibraryMimeType;
import com.huahui.datasphere.mdm.core.type.libraries.UserLibrariesListener;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;
import com.huahui.datasphere.mdm.core.util.TransactionUtils;

/**
 * @author theseusyang on Jan 29, 2021
 * ULS implementation.
 */
@Service
public class UserLibrariesServiceImpl implements UserLibraryService {
    /**
     * The libraries DAO.
     */
    @Autowired
    private LibrariesDAO librariesDAO;
    /**
     * HZ executor service.
     */
    private IExecutorService executorService;
    /**
     * Listeners collection.
     */
    private Set<UserLibrariesListener> listeners = new HashSet<>();
    /**
     * The service is singleton. Allow access to its internals for distributed actions.
     */
    private static final AtomicReference<UserLibrariesServiceImpl> INSTANCE = new AtomicReference<>();
    /**
     * Constructor.
     */
    @Autowired
    public UserLibrariesServiceImpl(final HazelcastInstance hazelcastInstance) {
        super();
        this.executorService = hazelcastInstance.getExecutorService(SystemConfigurationConstants.SYSTEM_EXECUTOR_SERVICE_NAME);
        INSTANCE.set(this);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(UserLibrariesListener l) {
        listeners.add(l);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(UserLibrariesListener l) {
        listeners.remove(l);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<UserLibrariesListener> getListeners() {
        return listeners;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void upsert(UserLibraryUpsertContext ctx) {

        Objects.requireNonNull(ctx.getFilename(), "Library file name must not be null.");

        // 1. Ensure MIME type
        LibraryMimeType mimeType = LibraryMimeType.fromCode(ctx.getMimeType());
        if (Objects.isNull(mimeType)) {
            throw new PlatformValidationException("Library MIME type [{}] is either null ot not supported.",
                    CoreExceptionIds.EX_LIBRARIES_MIME_TYPE_MISSING,
                    Collections.emptyList(), ctx.getMimeType());
        }

        // 2. Select content. We don't expect GiG-large files to be uploaded as libraries,
        // so we just read the whole content to buf in memory.
        InputStream is = ctx.getInput().get();
        Objects.requireNonNull(is, "Payload is null.");

        // 2. Select version and payload
        String version;
        byte[] payload;
        long size;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            size = IOUtils.copyLarge(is, baos);
            payload = baos.toByteArray();
            version = StringUtils.trimToNull(ctx.getVersion());
            if (Objects.isNull(version) && mimeType == LibraryMimeType.JAR_FILE) {

                JarInputStream jis = new JarInputStream(new ByteArrayInputStream(payload));
                Manifest m = jis.getManifest();

                Attributes attrs = m.getMainAttributes();
                version = StringUtils.trimToNull(attrs.getValue(CoreConfigurationConstants.CORE_LIBRARIES_VERSION_PROPERTY));
            }

        } catch (IOException ioe) {
            throw new PlatformFailureException("IO interaction failed.", ioe,
                    CoreExceptionIds.EX_LIBRARIES_IO_INTERACTION_FAILED);
        }

        if (Objects.isNull(version)) {
            throw new PlatformValidationException("Library version missing.",
                    CoreExceptionIds.EX_LIBRARIES_VERSION_MISSING,
                    Collections.emptyList());
        }

        LibraryPO po = new LibraryPO();
        po.setContent(payload);
        po.setSize(size);
        po.setCreatedBy(SecurityUtils.getCurrentUserName());
        po.setDescription(ctx.getDescription());
        po.setName(ctx.getFilename());
        po.setMimeType(mimeType.getCode());
        po.setEditable(ctx.isEditable());
        po.setStorageId(SecurityUtils.getStorageId(ctx));
        po.setVersion(version);

        librariesDAO.save(po, ctx.isForce());

        SyncAction sync = new SyncAction(true, po.getStorageId(), po.getName(), po.getVersion());
        TransactionUtils.executeAfterCommitAction(() -> executorService.executeOnAllMembers(sync));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserLibraryResult> query(UserLibraryQueryContext ctx) {

        return librariesDAO.load(
                SecurityUtils.getStorageId(ctx),
                ctx.getFilename(),
                Objects.nonNull(ctx.getMimeType()) ? ctx.getMimeType().getCode() : null,
                ctx.isLatest(), ctx.withData())
            .stream()
                .map(po -> {

                    UserLibraryResult result = new UserLibraryResult();

                    result.setFilename(po.getName());
                    result.setMimeType(LibraryMimeType.fromCode(po.getMimeType()));
                    result.setEditable(po.isEditable());
                    result.setDescription(po.getDescription());
                    result.setPayload(po.getContent());
                    result.setSize(po.getSize());
                    result.setStorageId(po.getStorageId());
                    result.setVersion(po.getVersion());
                    result.setCreatedBy(po.getCreatedBy());
                    result.setCreateDate(OffsetDateTime.ofInstant(po.getCreateDate().toInstant(), ZoneId.systemDefault()));

                    return result;
                })
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public UserLibraryResult get(UserLibraryGetContext ctx) {

        Objects.requireNonNull(ctx.getFilename(), "Library name must not be null.");
        LibraryPO po = ctx.isLatest()
                ? librariesDAO.load(SecurityUtils.getStorageId(ctx), ctx.getFilename(), ctx.withData())
                : librariesDAO.load(SecurityUtils.getStorageId(ctx), ctx.getFilename(), ctx.getVersion(), ctx.withData());

        if (Objects.isNull(po)) {
            return null;
        }

        UserLibraryResult result = new UserLibraryResult();

        result.setFilename(po.getName());
        result.setMimeType(LibraryMimeType.fromCode(po.getMimeType()));
        result.setEditable(po.isEditable());
        result.setDescription(po.getDescription());
        result.setPayload(po.getContent());
        result.setSize(po.getSize());
        result.setStorageId(po.getStorageId());
        result.setVersion(po.getVersion());
        result.setCreatedBy(po.getCreatedBy());
        result.setCreateDate(OffsetDateTime.ofInstant(po.getCreateDate().toInstant(), ZoneId.systemDefault()));

        return result;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void remove(UserLibraryRemoveContext ctx) {

        Objects.requireNonNull(ctx.getFilename(), "Library name must not be null.");

        final String storageId = SecurityUtils.getStorageId(ctx);
        final String name = ctx.getFilename();
        final String version = ctx.getVersion();

        librariesDAO.remove(storageId, name, version);

        SyncAction sync = new SyncAction(false, storageId, name, version);
        TransactionUtils.executeAfterCommitAction(() -> executorService.executeOnAllMembers(sync));
    }
    /**
     * @author theseusyang on Feb 1, 2021
     * Sync upsert type.
     */
    static class SyncAction implements Serializable, Runnable {
        /**
         * GSVUID.
         */
        private static final long serialVersionUID = 321341119737944333L;
        /**
         * Upsert/Remove indicator.
         */
        private boolean upsert;
        /**
         * Storage id.
         */
        private String storage;
        /**
         * Library name.
         */
        private String name;
        /**
         * The version.
         */
        private String version;
        /**
         * Constructor.
         * @param upsert the operation indicator
         * @param storage the storage
         * @param name the name
         * @param version the version
         */
        public SyncAction(boolean upsert, String storage, String name, String version) {
            super();
            this.upsert = upsert;
            this.storage = storage;
            this.name = name;
            this.version = version;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {

            UserLibrariesServiceImpl instance = INSTANCE.get();
            for (UserLibrariesListener l : instance.listeners) {
                if (upsert) {
                    l.libraryUpserted(storage, name, version);
                } else {
                    l.libraryRemoved(storage, name, version);
                }
            }
        }
    }
}
