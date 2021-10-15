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

import org.datasphere.mdm.core.context.DeleteLargeObjectContext;
import org.datasphere.mdm.core.context.FetchLargeObjectContext;
import org.datasphere.mdm.core.context.UpsertLargeObjectContext;
import org.datasphere.mdm.core.dto.LargeObjectResult;

/**
 * @author Dmitry Kopin on 26.12.2017.
 */
public interface LargeObjectsService {
    /**
     * Gets large object input stream according to context specification.
     * @param ctx the context
     * @return {@link LargeObjectResult} instance
     */
    LargeObjectResult fetchLargeObject(FetchLargeObjectContext ctx);
    /**
     * Saves large object data.
     * @param ctx the context
     * @return true if successful, false otherwise
     */
    LargeObjectResult saveLargeObject(UpsertLargeObjectContext ctx);
    /**
     * Deletes large object data.
     * @param ctx the context
     * @return true if successful, false otherwise
     */
    boolean deleteLargeObject(DeleteLargeObjectContext ctx);
    /**
     * Check large object exist
     * @param ctx context
     * @return return true if exist, else false
     */
    boolean checkExistLargeObject(FetchLargeObjectContext ctx);
}
