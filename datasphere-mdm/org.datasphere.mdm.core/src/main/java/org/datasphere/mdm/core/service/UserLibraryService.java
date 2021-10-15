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

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.datasphere.mdm.core.context.UserLibraryGetContext;
import org.datasphere.mdm.core.context.UserLibraryQueryContext;
import org.datasphere.mdm.core.context.UserLibraryRemoveContext;
import org.datasphere.mdm.core.context.UserLibraryUpsertContext;
import org.datasphere.mdm.core.dto.UserLibraryResult;
import org.datasphere.mdm.core.type.libraries.UserLibrariesListener;

/**
 * @author Mikhail Mikhailov on Jan 29, 2021
 * A general purpose service to support user supplied, updatable, versioned, storage id bounded libraries
 * (primarilly jar files). Those can be uploaded at runtime and used without server restart.
 * The library content is save to DB and is published to HZ map, to make changes be immediately available in the cluster.
 */
public interface UserLibraryService {
    /**
     * Does either update or insert of a library to the library register.
     * @param ctx content as bytes
     */
    void upsert(UserLibraryUpsertContext ctx);
    /**
     * Gets info (optionally with )
     * @param ctx
     * @return
     */
    @Nonnull
    List<UserLibraryResult> query(UserLibraryQueryContext ctx);
    /**
     * Gets a version of a user library.
     * If storage Id is not set, current user's storage id is used.
     * If version is not set, the latest version (by create date) is returned.
     * @param ctx the context
     * @return result or null
     */
    @Nullable
    UserLibraryResult get(UserLibraryGetContext ctx);
    /**
     * Removes a version(s) of a user library.
     * If storage Id is not set, current user's storage id is used.
     * If version is not set, the latest version (by create date) is removed.
     * @param ctx the context
     */
    void remove(UserLibraryRemoveContext ctx);
    /**
     * Adds updates listener.
     * @param l the listener to add
     */
    void addListener(UserLibrariesListener l);
    /**
     * Removes updates listener.
     * @param l the listener to remove
     */
    void removeListener(UserLibrariesListener l);
    /**
     * Gets all registered library listeners.
     * @return collection of listeners
     */
    Collection<UserLibrariesListener> getListeners();
}
