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

import com.huahui.datasphere.mdm.core.migration.core.UN12296UninstallCoreModule;

import nl.myndocs.database.migrator.MigrationScript;

/**
 * storage migrations for uninstall core security
 *
 * @author maria.chistyakova
 */
public final class UninstallCoreSchemaMigrations {

    private static final MigrationScript[] MIGRATIONS = {
        new UN12296UninstallCoreModule()
    };
    /**
     * Constructor.
     */
    private UninstallCoreSchemaMigrations() {
        super();
    }

    /**
     * Makes SONAR happy.
     * @return migrations
     */
    public static MigrationScript[] migrations() {
        return MIGRATIONS;
    }


}
