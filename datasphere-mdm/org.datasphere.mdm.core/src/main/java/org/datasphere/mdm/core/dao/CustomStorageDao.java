/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 * 
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.dao;

import javax.annotation.Nonnull;

import org.datasphere.mdm.core.po.CustomStorageRecordPO;

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
