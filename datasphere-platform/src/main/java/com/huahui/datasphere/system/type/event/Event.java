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

import java.io.Serializable;

public interface Event extends Serializable {
    /**
     * This might be useful for listeners / subscribers, that do not filetr on the event type.
     * @return type name
     */
    String getTypeName();
    /**
     * Gets the id.
     * @return the ebent id
     */
    String getId();
    /**
     * Gets the type of broadcasting.
     * @return type
     */
    BroadcastType getBroadcastType();
    /**
     * Returns true, if the event should be replied.
     * @return true for awaited replay, false otherwise
     */
    default boolean withReplay() {
        return false;
    }
    /**
     * Returns true, if sender and receiver are on the same node.
     * In other words
     * <ul>
     * <li> always true for {@link AbstractLocalEvent}</li>
     * <li> always false for {@link AbstractForeignEvent}</li>
     * <li> may be true or false for {@link AbstractMulticastEvent}</li>
     * </ul>
     * @return true, if sender and receiver are on the same node.
     */
    boolean isLocal();
}
