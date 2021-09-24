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

import java.util.Collection;

import org.springframework.stereotype.Service;
import com.huahui.datasphere.mdm.system.type.annotation.ConfigurationRef;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationValue;

import com.huahui.datasphere.mdm.core.configuration.CoreConfigurationConstants;
import com.huahui.datasphere.mdm.core.context.AuditEventWriteContext;
import com.huahui.datasphere.mdm.core.dao.AuditDao;
import com.huahui.datasphere.mdm.core.service.AuditStorageService;

/**
 * @author theseusyang
 */
@Service("databaseAuditStorageService")
public class DatabaseAuditStorageService implements AuditStorageService {

    private final AuditDao auditDao;

    @ConfigurationRef(CoreConfigurationConstants.PROPERTY_AUDIT_ENABLED)
    private ConfigurationValue<Boolean> auditEnabled;

    @ConfigurationRef(CoreConfigurationConstants.PROPERTY_AUDIT_ENABLED_STORAGES)
    private ConfigurationValue<AuditStorageType[]> enabledStorages;

    public DatabaseAuditStorageService(final AuditDao auditDao) {
        this.auditDao = auditDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(Collection<AuditEventWriteContext> auditEventWriteContext) {

        // Audit is either disabled entirely or just the DB is disabled
        if (!auditEnabled.getValue().booleanValue()
          || enabledStorages.getValue()[AuditStorageType.DATABASE.ordinal()] == null) {
            return;
        }

        auditDao.insert(auditEventWriteContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        // NOP for DB
    }
}
