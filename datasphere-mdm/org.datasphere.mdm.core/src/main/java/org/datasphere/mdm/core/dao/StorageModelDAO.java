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

import java.util.List;

import org.datasphere.mdm.core.po.model.StoragePO;

/**
 * @author Mikhail Mikhailov on Oct 2, 2020
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
