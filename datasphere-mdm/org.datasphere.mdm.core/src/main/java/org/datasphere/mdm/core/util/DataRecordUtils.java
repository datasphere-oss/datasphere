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
package org.datasphere.mdm.core.util;

import java.util.Objects;

import org.datasphere.mdm.core.type.data.Attribute;
import org.datasphere.mdm.core.type.data.DataRecord;

/**
 * @author Mikhail Mikhailov on Jun 7, 2021
 */
public final class DataRecordUtils {

    /**
     * Constructor.
     */
    private DataRecordUtils() {
        super();
    }

    public static DataRecord topLevelFromAttribute(Attribute attr) {

        if (Objects.isNull(attr)) {
            return null;
        }

        return topLevelFromRecord(attr.getRecord());
    }

    public static DataRecord topLevelFromRecord(DataRecord dr) {

        if (Objects.isNull(dr)) {
            return null;
        }

        while (Objects.nonNull(dr) && !dr.isTopLevel()) {
            dr = dr.getParentRecord();
        }

        return dr;
    }
}
