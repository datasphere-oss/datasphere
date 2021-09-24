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

package com.huahui.datasphere.mdm.system.migration;

import nl.myndocs.database.migrator.MigrationScript;
import nl.myndocs.database.migrator.definition.Migration;

/**
 * UN-12000
 * @author theseusyang on Nov 26, 2019
 */
public class UN12000InitPipelines implements MigrationScript  {

    @Override
    public String author() {
        return "mikhail";
    }

    @Override
    public void migrate(Migration m) {
        m.raw()
            .sql("create table if not exists pipelines_info (\n" +
                 "    start_id text not null,\n" +
                 "    subject text,\n" +
                 "    content text not null,\n" +
                 "    constraint pk_pipelines_info_start_id_subject primary key(start_id, subject)\n" +
                 ")")
            .save();
    }

    @Override
    public String migrationId() {
        return "UN-12000-pipelines";
    }
}
