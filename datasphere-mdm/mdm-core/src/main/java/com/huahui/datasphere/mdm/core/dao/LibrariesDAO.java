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

import com.huahui.datasphere.mdm.core.po.LibraryPO;

/**
 * @author theseusyang on Oct 2, 2020
 * DQ model DAO.
 */
public interface LibrariesDAO {
    /**
     * Loads current versions of libraries set for a storage id.
     * @param storageId the storage id
     * @param name the name
     * @param mimeType the MIME type
     * @param latest only latest versions, if true, all otherwise
     * @param withData load data too
     * @return libraries set or empty list.
     */
    List<LibraryPO> load(String storageId, String name, String mimeType, boolean latest, boolean withData);
    /**
     * Loads current (latest by create date) version of a specific library.
     * @param storageId the storage id
     * @param name the name of a particular library
     * @param withData load also data (or not)
     * @return library or null.
     */
    LibraryPO load(String storageId, String name, boolean withData);
    /**
     * Loads specific version of a specific library.
     * @param storageId the storage id
     * @param name the name of a particular library
     * @param version specific version
     * @param withData  load also data (or not)
     * @return library or null.
     */
    LibraryPO load(String storageId, String name, String version, boolean withData);
    /**
     * Saves a new version of DQ model.
     * @param po the PO
     * @param force force update for 'not editable' libraries
     * @return true if successfully saved
     */
    boolean save(LibraryPO po, boolean force);
    /**
     * Deletes a DQ model revision.
     * @param storageId the storage id
     * @param name the name of the library
     * @param version the version
     */
    void remove(String storageId, String name, String version);
}
