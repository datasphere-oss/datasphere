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

import java.util.Collection;

import org.datasphere.mdm.core.configuration.CoreConfigurationConstants;
import org.datasphere.mdm.core.context.AuditEventWriteContext;
import org.datasphere.mdm.core.dao.AuditDao;
import org.datasphere.mdm.core.service.AuditStorageService;
import org.springframework.stereotype.Service;
import org.datasphere.mdm.system.type.annotation.ConfigurationRef;
import org.datasphere.mdm.system.type.configuration.ConfigurationValue;

/**
 * @author Alexander Malyshev
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
