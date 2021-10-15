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
package org.datasphere.mdm.core.context;

import org.datasphere.mdm.core.type.model.ModelInstance;
import org.datasphere.mdm.system.context.StorageCapableContext;
import org.datasphere.mdm.system.context.StorageId;

/**
 * @author Mikhail Mikhailov on Nov 2, 2020
 * Previous model state holder.
 */
public interface PreviousModelStateContext extends StorageCapableContext {
    /**
     * The notification id SID.
     */
    StorageId SID_PREVIOUS_MODEL_STATE = new StorageId("PREVIOUS_MODEL_STATE");
    /**
     * Gets state.
     * @param <M> the expected model type
     * @return state or null
     */
    default <M extends ModelInstance<?>> M prevousState() {
        return getFromStorage(SID_PREVIOUS_MODEL_STATE);
    }
    /**
     * Puts state.
     * @param <M> the expected model type
     * @param m the state
     */
    default <M extends ModelInstance<?>> void prevousState(M m) {
        putToStorage(SID_PREVIOUS_MODEL_STATE, m);
    }
}
