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

package com.huahui.datasphere.mdm.core.configuration;

/**
 * TODO : 5.2 class, needs to be refactored and moved
 *
 * @author maria.chistyakova
 * @since 30.10.2019
 */
public class UserMessageConstants {

    /*
    STATISTIC_EXPORT_SUCCESS, DATA_EXPORT_FETCHED_RECORDS from backend and over

     */
    public static final String JOB_REINDEX_META_SUCCESS = "app.user.events.reindex.meta.jobs.success";
    public static final String JOB_REINDEX_META_FAIL = "app.user.events.reindex.meta.jobs.fail";
    public static final String DATA_IMPORT_METADATA_FAILED = "app.user.events.import.metadata.failed";
    public static final String DATA_IMPORT_METADATA_SUCCESS = "app.user.events.import.metadata.success";
}
