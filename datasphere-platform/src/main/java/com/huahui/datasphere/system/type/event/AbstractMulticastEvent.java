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

/**
 * @author Mikhail Mikhailov on Oct 28, 2019
 */
public abstract class AbstractMulticastEvent extends AbstractEvent {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = -3807384836300688428L;
    /**
     *  Is source of event local
     */
    private boolean local;
    /**
     * Constructor.
     * @param typeName
     * @param id
     */
    protected AbstractMulticastEvent(String typeName, String id) {
        super(typeName, id);
    }
    /**
     * Constructor.
     * @param typeName
     */
    protected AbstractMulticastEvent(String typeName) {
        super(typeName);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public BroadcastType getBroadcastType() {
        return BroadcastType.BOTH;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLocal() {
        return local;
    }
    /**
     * Sets the event to be local or foreign source.
     * @param local
     */
    public void setLocal(boolean local) {
        this.local = local;
    }
}
