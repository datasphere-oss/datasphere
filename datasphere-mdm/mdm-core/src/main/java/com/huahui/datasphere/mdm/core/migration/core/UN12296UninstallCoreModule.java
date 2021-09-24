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

package com.huahui.datasphere.mdm.core.migration.core;

import nl.myndocs.database.migrator.MigrationScript;
import nl.myndocs.database.migrator.definition.Migration;

/**
 * cleanup core security schema migration
 *
 * @author maria.chistyakova
 * @since 11.10.2019
 */
public class UN12296UninstallCoreModule implements MigrationScript {

    /**
     * Constructor.
     */
    public UN12296UninstallCoreModule() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String author() {
        return "maria.chistyakova";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String migrationId() {
        return "UN-12296__UninstallCoreModule";
    }

    /**
    /**
     * drop tables, not schema
     *
     * {@inheritDoc}
     */
    @Override
    public void migrate(Migration migration) {
        migration.raw().sql("select 'drop table if exists \"' || tablename || '\" cascade;' \n" +
                "  from pg_tables\n" +
                " where schemaname = 'org_unidata_mdm_core'; ").save();
    }
}
