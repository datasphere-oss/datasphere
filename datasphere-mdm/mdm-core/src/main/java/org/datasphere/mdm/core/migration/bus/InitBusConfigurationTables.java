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

package com.huahui.datasphere.mdm.core.migration.bus;

import nl.myndocs.database.migrator.MigrationScript;
import nl.myndocs.database.migrator.definition.Column;
import nl.myndocs.database.migrator.definition.Migration;

/**
 * @author theseusyang
 */
public class InitBusConfigurationTables implements MigrationScript {

    @Override
    public String migrationId() {
        return "InitBusConfigurationTables";
    }

    @Override
    public String author() {
        return "Alexander Malyshev";
    }

    @Override
    public void migrate(Migration migration) {
        migration.table("bus_routes_definitions")
                .addColumn("routes_definition_id", Column.TYPE.VARCHAR, cb -> cb.primary(true).notNull(true).size(255))
                .addColumn("routes_definition", Column.TYPE.TEXT, cb -> cb.notNull(true))
                .save();
    }
}
