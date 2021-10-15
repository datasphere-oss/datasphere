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
import java.util.function.Predicate;

import org.datasphere.mdm.core.type.data.DataRecord;
import org.datasphere.mdm.core.type.upath.UPathElementType;
import org.datasphere.mdm.core.type.upath.UPathFilterElement;

/**
 * @author Mikhail Mikhailov on Feb 26, 2021
 * Generic predicate filter.
 */
public class PredicateElementImpl extends AbstractElementImpl implements UPathFilterElement {
    /**
     * The presicate.
     */
    private final Predicate<DataRecord> predicate;
    /**
     * Constructor.
     * @param element the source element
     * @param predicate filtering predicate
     */
    public PredicateElementImpl(String element, Predicate<DataRecord> predicate) {
        super(element, UPathElementType.EXPRESSION);
        this.predicate = predicate;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(DataRecord record) {
        return Objects.nonNull(record) && predicate.test(record);
    }
}
