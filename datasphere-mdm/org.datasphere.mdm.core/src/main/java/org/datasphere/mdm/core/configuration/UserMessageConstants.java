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

package org.datasphere.mdm.core.configuration;

/**
 * TODO : 5.2 class, needs to be refactored and moved
 *
 * @author maria.chistyakova
 * @since 30.10.2019
 */
public class UserMessageConstants {

    private UserMessageConstants() {
        super();
    }

    public static final String JOB_REINDEX_META_SUCCESS = "app.user.events.reindex.meta.jobs.success";
    public static final String JOB_REINDEX_META_FAIL = "app.user.events.reindex.meta.jobs.fail";
    public static final String DATA_IMPORT_METADATA_FAILED = "app.user.events.import.metadata.failed";
    public static final String DATA_IMPORT_METADATA_SUCCESS = "app.user.events.import.metadata.success";
    public static final String DATA_IMPORT_UNSUCCESS = "app.user.events.import.data.unsuccess";
}
