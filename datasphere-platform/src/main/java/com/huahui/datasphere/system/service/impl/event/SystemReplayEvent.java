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

package com.huahui.datasphere.system.service.impl.event;

import com.huahui.datasphere.system.type.event.AbstractMulticastEvent;

/**
 * @author Mikhail Mikhailov on Oct 29, 2019
 */
public class SystemReplayEvent extends AbstractMulticastEvent {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = -6947329119790339516L;
    /**
     * This type name.
     */
    private static final String TYPE_NAME = "SYSTEM_REPLAY_EVENT";
    /**
     * The number of receivers to receive the original message.
     */
    private final int receiverCount;
    /**
     * Constructor.
     * @param typeName
     * @param id
     */
    public SystemReplayEvent(String id, int receiverCount) {
        super(TYPE_NAME, id);
        this.receiverCount = receiverCount;
    }
    /**
     * @return the receiverCount
     */
    public int getReceiverCount() {
        return receiverCount;
    }
}
