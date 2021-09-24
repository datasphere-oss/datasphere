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

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.huahui.datasphere.mdm.core.po.lob.LargeObjectPO;

/**
 * @author theseusyang
 * Large objects DAO.
 */
public interface LargeObjectsDAO {
    /**
     * Fetch large object.
     * @param id the id
     * @param isBinary character or binary
     * @return object or null
     */
    LargeObjectPO loadLargeObject(UUID id, boolean isBinary);
    /**
     * Upsert large object.
     * @param lob the object
     * @return true if successful, false otherwise
     * @throws IOException
     */
    boolean upsertLargeObject(LargeObjectPO lob) throws IOException;
    /**
     * Sets a record active (submits an attachment).
     * @param spec activation spec (objects)
     * @return true if successful, false otherwise
     */
    boolean submitLargeObjects(Collection<LargeObjectPO> spec);
    /**
     * Deletes a large object.
     * @param id the id
     * @param isBinary character or binary
     * @return true if successful, false otherwise
     */
    boolean wipeLargeObject(UUID id, boolean isBinary);
    /**
     * Deletes a large object.
     * @param subject the subject id
     * @param tags the tags
     * @param isBinary character or binary
     * @return true if successful, false otherwise
     */
    boolean wipeLargeObjects(String subject, String[] tags, boolean isBinary);
    /**
     * Returns true, if character or binary object with the given ID exists.
     * @param id the ID to check
     * @param isBinary type indicator
     * @return true, if character or binary object with the given ID exists
     */
    boolean checkLargeObject(UUID id, boolean isBinary);
    /**
     * Clean unused binary data
     * @param maxLifetime max lifetime for binary data
     * @return count of removed messages
     */
    long cleanUnusedBinaryData(long maxLifetime);
    /**
     * Drops both character and binary data for the given subject IDs (record origin IDs, event IDs, etc.)
     * @param subjects the subject IDs
     * @return count of deleted records
     */
    long cleanForSubjectIds(List<String> subjects);
}
