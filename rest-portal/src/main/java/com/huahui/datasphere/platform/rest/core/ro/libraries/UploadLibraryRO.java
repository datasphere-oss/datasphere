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
package com.huahui.datasphere.platform.rest.core.ro.libraries;

import com.huahui.datasphere.rest.system.ro.DetailedOutputRO;

/**
 * @author Mikhail Mikhailov on Feb 8, 2021
 */
public class UploadLibraryRO extends DetailedOutputRO {

    /**
     * Success indicator.
     */
    private boolean success;

    /**
     * Constructor.
     */
    public UploadLibraryRO() {
        super();
    }

    /**
     * Constructor.
     */
    public UploadLibraryRO(boolean success) {
        super();
        this.success = success;
    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

}
