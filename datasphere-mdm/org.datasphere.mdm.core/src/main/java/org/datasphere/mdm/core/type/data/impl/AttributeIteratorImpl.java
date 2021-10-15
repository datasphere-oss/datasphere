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

package org.datasphere.mdm.core.type.data.impl;

import java.util.Iterator;
import java.util.Map.Entry;

import org.datasphere.mdm.core.type.data.Attribute;
import org.datasphere.mdm.core.type.data.AttributeIterator;

/**
 * @author Mikhail Mikhailov
 * Thin iterator wrapper to allow traverse -&gt; remove actions.
 */
public class AttributeIteratorImpl implements AttributeIterator {

    /**
     * The underlaying iterator.
     */
    private final Iterator<Entry<String, Attribute>> it;
    /**
     * Constructor.
     * @param it iterator.
     */
    AttributeIteratorImpl(Iterator<Entry<String, Attribute>> it) {
        this.it = it;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attribute next() {
        return it.next().getValue();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        it.remove();
    }
}
