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
package org.datasphere.mdm.core.service.impl.upath;

import java.util.Objects;

import org.datasphere.mdm.core.type.data.DataRecord;
import org.datasphere.mdm.core.type.upath.UPathElementType;
import org.datasphere.mdm.core.type.upath.UPathFilterElement;

/**
 * @author Mikhail Mikhailov on Feb 26, 2021
 * Subscript filter.
 */
public class SubscriptElementImpl extends AbstractElementImpl implements UPathFilterElement {
    /**
     * The record's ordinal (subscript).
     */
    private final int ordinal;
    /**
     * Constructor.
     * @param element the source element
     * @param ordinal the record's ordinal
     */
    public SubscriptElementImpl(String element, int ordinal) {
        super(element, UPathElementType.SUBSCRIPT);
        this.ordinal = ordinal;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(DataRecord record) {
        return Objects.nonNull(record) && record.getOrdinal() == ordinal;
    }
}
