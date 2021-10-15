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

package org.datasphere.mdm.core.service;

import java.util.List;

import org.datasphere.mdm.core.dto.CustomStorageRecordDTO;

/**
 * @author Dmitry Kopin on 28.08.2017.
 * Service for work with custom settings
 */
public interface CustomStorageService {
    /**
     * Create list of records in custom storage
     * @param customStorageRecords list of {@link CustomStorageRecordDTO}
     */
    boolean createRecords(List<CustomStorageRecordDTO> customStorageRecords);
    /**
     * Update list of records in custom storage
     * @param customStorageRecords list of {@link CustomStorageRecordDTO}
     */
    boolean updateRecords(List<CustomStorageRecordDTO> customStorageRecords);
    /**
     * Delete list of records in custom storage
     * @param customStorageRecords list of {@link CustomStorageRecordDTO}
     */
    boolean deleteRecords(List<CustomStorageRecordDTO> customStorageRecords);
    /**
     * Delete list of record in custom storage by key
     * @param key key
     */
    boolean deleteRecordsByKey(String key);
    /**
     * Delete list of record in custom storage by user name
     * @param userName user name
     */
    boolean deleteRecordsByUserName(String userName);
    /**
     * Get list of records from custom storage by user name
     * @param userName user name
     * @return list of records
     */
    List<CustomStorageRecordDTO> getRecordsByUserName(String userName);
    /**
     * Get list of records from custom storage by key
     * @param key key
     * @return list of records
     */
    List<CustomStorageRecordDTO> getRecordsByKey(String key);
    /**
     * Get list of records from custom storage by user name and key
     * @param userName user name
     * @param key key
     * @return list of records
     */
    List<CustomStorageRecordDTO> getRecordsByKeyAndUser(String key, String userName);
}
