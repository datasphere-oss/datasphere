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
package com.huahui.datasphere.system.type.messaging;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * The header. Can be added to a {@link MessageType}.
 * Headers from {@link SystemHeaders} are added to each message automatically.
 * Hash code and equals are calculated using the name field only.
 * Thus, for a message type, you won't be able to override a registered header with a header with the same name but a different type.
 * @author Mikhail Mikhailov on Jul 6, 2020
 */
public class Header {
    /**
     * Type of the header's value.
     * @author Mikhail Mikhailov on Jul 6, 2020
     */
    public enum HeaderType {
        STRING,
        INTEGER,
        BOOLEAN,
        INSTANT,
        OBJECT
    }
    /**
     * The header name.
     */
    private final String name;
    /**
     * The header type.
     */
    private final HeaderType type;
    /**
     * Constructor.
     * @param name the header name
     * @param type the header type
     */
    public Header(@Nonnull String name, @Nonnull HeaderType type) {
        super();

        Objects.requireNonNull(name, "Headers name must not be null.");
        Objects.requireNonNull(type, "Headers type must not be null.");
        this.name = name;
        this.type = type;
    }
    /**
     * Gets the name of the header.
     * @return the name of the header
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the type of the header's value.
     * @return type of the value
     */
    public HeaderType getType() {
        return type;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return name.hashCode();
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
        Header other = (Header) obj;
        if (!name.equals(other.name))
            return false;
        return true;
    }
}
