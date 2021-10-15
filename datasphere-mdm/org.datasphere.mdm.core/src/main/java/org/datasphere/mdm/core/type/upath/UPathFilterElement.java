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
package org.datasphere.mdm.core.type.upath;

import org.datasphere.mdm.core.type.data.DataRecord;

/**
 * @author Mikhail Mikhailov on Feb 25, 2021
 * Filtering element interface.
 */
public interface UPathFilterElement extends UPathElement {
    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isFiltering() {
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    default UPathFilterElement getFilter() {
        return this;
    }
    /**
     * Returns true, if the given record matches filtering condition.
     * @param record the record to check
     * @return true, if the given record matches filtering condition
     */
    boolean matches(DataRecord record);
}
