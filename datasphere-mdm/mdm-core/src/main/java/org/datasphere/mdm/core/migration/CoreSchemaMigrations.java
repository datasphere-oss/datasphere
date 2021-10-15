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

package com.huahui.datasphere.mdm.core.migration;

import com.huahui.datasphere.mdm.system.migration.Migrations;
import com.huahui.datasphere.mdm.system.util.ResourceUtils;

import com.huahui.datasphere.mdm.core.migration.audit.UN11979InitAuditTables;
import com.huahui.datasphere.mdm.core.migration.bus.InitBusConfigurationTables;
import com.huahui.datasphere.mdm.core.migration.event.meta.UN12296InitializationEventCoreSchema;
import com.huahui.datasphere.mdm.core.migration.job.meta.InitializationQuartzJobSchema;
import com.huahui.datasphere.mdm.core.migration.job.meta.UN12296InitializationJobCoreSchema;
import com.huahui.datasphere.mdm.core.migration.security.data.UN12296InsertSecurityDefaultData;
import com.huahui.datasphere.mdm.core.migration.security.meta.UN12296InitializationSecuritySchema;
import com.huahui.datasphere.mdm.core.migration.security.meta.UN13491DropColumn;

import nl.myndocs.database.migrator.MigrationScript;

/**
 * storage migrations to install security meta + admin login + resource
 *
 *
 * @author maria.chistyakova
 */
public final class CoreSchemaMigrations {

    private static final MigrationScript[] MIGRATIONS = {
            new UN12296InitializationSecuritySchema(),
            new UN12296InsertSecurityDefaultData(),
            new UN12296InitializationJobCoreSchema(),
            new UN12296InitializationEventCoreSchema(),
            new UN11979InitAuditTables(),
            new InitializationQuartzJobSchema(),
            new InitBusConfigurationTables(),
            new UN13491DropColumn(),
            Migrations.of("UN-15627__MoveStorageToCore", "mikhail.mikhailov", ResourceUtils.asStrings("classpath:/migration/UN-15627-move-storage-to-core.sql")),
            Migrations.of("UN-15627__UserLibraries", "mikhail.mikhailov", ResourceUtils.asStrings("classpath:/migration/UN-15627-user-libraries.sql")),
            Migrations.of("UN-15627__UserLibraries_EditableField", "mikhail.mikhailov", "alter table libraries add column if not exists editable boolean default true"),
            Migrations.of("UN-15755__BinaryCharacterData", "mikhail.mikhailov", ResourceUtils.asStrings("classpath:/migration/UN-15755-binary-character-data.sql"))
    };

    /**
     * Constructor.
     */
    private CoreSchemaMigrations() {
        super();
    }

    /**
     * Makes SONAR happy.
     *
     * @return migrations
     */
    public static MigrationScript[] migrations() {
        return MIGRATIONS;
    }

}
