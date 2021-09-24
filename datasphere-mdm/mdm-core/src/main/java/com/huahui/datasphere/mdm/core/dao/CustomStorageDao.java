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

package com.huahui.datasphere.mdm.core.dao;

import javax.annotation.Nonnull;

import com.huahui.datasphere.mdm.core.po.CustomStorageRecordPO;

import java.util.List;

/**
 * @author Dmitry Kopin on 25.08.2017.
 *         Dao for work with custom storage
 */
public interface CustomStorageDao {

    /**
     * Create list of records in custom storage
     * @param customStorageRecords list of {@link CustomStorageRecordPO}
     */
    void createRecords(@Nonnull List<CustomStorageRecordPO> customStorageRecords);
    /**
     * Update list of records in custom storage
     * @param customStorageRecords list of {@link CustomStorageRecordPO}
     */
    void updateRecords(@Nonnull List<CustomStorageRecordPO> customStorageRecords);
    /**
     * Delete list of records in custom storage
     * @param customStorageRecords list of {@link CustomStorageRecordPO}
     */
    void deleteRecords(List<CustomStorageRecordPO> customStorageRecords);
    /**
     * Delete list of record in custom storage by key
     * @param key key
     */
    void deleteRecordsByKey(String key);
    /**
     * Delete list of record in custom storage by user name
     * @param userName user name
     */
    void deleteRecordsByUserName(String userName);
    /**
     * Get list of records from custom storage by user name
     * @param userName user name
     * @return list of records
     */
    List<CustomStorageRecordPO> getRecordsByUserName(String userName);
    /**
     * Get list of records from custom storage by key
     * @param key key
     * @return list of records
     */
    List<CustomStorageRecordPO> getRecordsByKey(String key);
    /**
     * Get list of records from custom storage by user name and key
     * @param userName user name
     * @param key key
     * @return list of records
     */
    List<CustomStorageRecordPO> getRecordsByKeyAndUser(String key, String userName);

}
