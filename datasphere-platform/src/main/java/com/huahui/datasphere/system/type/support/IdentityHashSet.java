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

package com.huahui.datasphere.system.type.support;

import java.util.AbstractSet;
import java.util.IdentityHashMap;
import java.util.Iterator;

/**
 * @author Mikhail Mikhailov
 * Simple identity hash set.
 */
public class IdentityHashSet<T> extends AbstractSet<T> {
    /**
     * The content.
     */
    private final IdentityHashMap<T, Boolean> content;
    /**
     * Constructor.
     */
    public IdentityHashSet() {
        super();
        content = new IdentityHashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        return content.keySet().iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return content.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        return content.containsKey(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(T o) {
        return content.put(o, Boolean.TRUE) == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        return content.remove(o) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        content.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return content.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o instanceof IdentityHashSet) {

            IdentityHashSet<?> other = (IdentityHashSet<?>) o;
            return other.content.equals(this.content);
        }

        return false;
    }
}
