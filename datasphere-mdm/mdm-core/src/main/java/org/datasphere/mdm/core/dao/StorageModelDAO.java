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

import java.util.List;

import com.huahui.datasphere.mdm.core.po.model.StoragePO;

/**
 * @author theseusyang on Oct 2, 2020
 * Simple storage DAO.
 */
public interface StorageModelDAO {
    /**
     * Loads all available storage ids.
     * @return list of ids
     */
    List<StoragePO> loadAll();
    /**
     * Loads object by id.
     * @param id the id
     * @return storage object
     */
    StoragePO loadById(String id);
    /**
     * Creates a new meta storage entry.
     * @param pothe object to update
     */
    void create(StoragePO po);
    /**
     * Updates a meta storage entry.
     * @param pothe object to update
     */
    void update(StoragePO po);
    /**
     * Removes a storage object.
     * @param id the object's id
     */
    void remove(String id);
}
