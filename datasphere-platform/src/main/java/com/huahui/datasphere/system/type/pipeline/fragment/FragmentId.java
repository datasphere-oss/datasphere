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

package com.huahui.datasphere.system.type.pipeline.fragment;

/**
 * Generic fragment ID.
 * @author Mikhail Mikhailov on Dec 11, 2019
 */
public class FragmentId<F extends Fragment<F>> {
    /**
     * The name of the ID.
     */
    private final String name;
    /**
     * Constructor.
     * @param name the name of the ID
     */
    public FragmentId(String name) {
        super();
        this.name = name;
    }
    /**
     * Gets the name if this fragment.
     * @return fragment name
     */
    public String getName() {
        return name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FragmentId<?> other = (FragmentId<?>) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringBuilder("FragmentId [").append(name).append("]").toString();
    }
}
