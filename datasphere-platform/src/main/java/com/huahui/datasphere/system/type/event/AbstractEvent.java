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

package com.huahui.datasphere.system.type.event;

import com.huahui.datasphere.system.util.IdUtils;

/**
 * @author Mikhail Mikhailov on Oct 28, 2019
 */
public abstract class AbstractEvent implements Event {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 6935407258627041109L;
    /**
     * The type name.
     */
    private final String typeName;
    /**
     * The id.
     */
    private final String id;
    /**
     * Sets internal replay flag.
     */
    private boolean withReplay;
    /**
     * Constructor.
     */
    protected AbstractEvent(String typeName, String id) {
        super();
        this.typeName = typeName;
        this.id = id;
    }
    /**
     * Constructor.
     */
    protected AbstractEvent(String typeName) {
        this(typeName, IdUtils.v1String());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTypeName() {
        return typeName;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }
    /**
     * @return the withReplay
     */
    @Override
    public boolean withReplay() {
        return withReplay;
    }
    /**
     * @param withReplay the withReplay to set
     */
    public void setReplay(boolean withReplay) {
        this.withReplay = withReplay;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Event: id [" + id + "], type [" + typeName + "], broadcast [" + getBroadcastType().name() + "], replay [" + withReplay + "]";
    }
}
