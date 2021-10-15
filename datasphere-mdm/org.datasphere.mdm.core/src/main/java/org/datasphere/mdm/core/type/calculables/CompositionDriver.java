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

package org.datasphere.mdm.core.type.calculables;

import org.datasphere.mdm.core.type.data.DataRecord;

/**
 * @author mikhail
 * Type of calculable, held by the holder.
 */
public interface CompositionDriver<T extends Calculable, A extends BvrCalculationInfo<T>, B extends BvtCalculationInfo<T>> {
    /**
     * Gets the tag name of this type.
     * @return the type name
     */
    String getName();
    /**
     * Gets the basic description of this type.
     * @return decsription
     */
    String getDescription();
    /**
     * Tells whether the given calculable set denotes an active interval (validity range).
     * @param info calculables info
     * @return true, if active, false otherwise
     */
    boolean hasActiveBVR(A info);
    /**
     * Performs BVR calculation on this type
     * @param info the calculables info
     * @return BVR winner
     */
    T toBVR(A info);
    /**
     * Performs BVT calculation on this type
     * @param info the calculables info
     * @return BVT winner record
     */
    DataRecord toBVT(B info);
}
