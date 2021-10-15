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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.datasphere.mdm.core.type.data.DataRecord;
import org.datasphere.mdm.core.type.data.ValidityRange;

/**
 * @author Mikhail Mikhailov
 * Modification box for record periods.
 */
public interface ModificationBox<T extends Calculable> extends ValidityRange {
    /**
     * Returns all box keys, currently hold by the box.
     * @return keys
     */
    Collection<String> toBoxKeys();
    /**
     * Gets the calculation base for this box.
     * The map is not modifyable.
     */
    List<CalculableHolder<T>> toCalculationBase();
    /**
     * Gets the calculation modifications of this box.
     * The map is not modifyable.
     */
    Map<String, List<CalculableHolder<T>>> toModifications();
    /**
     * Returns collection of top most claculables for all source systems.
     * @return collection
     */
    List<CalculableHolder<T>> toCalculables();
    /**
     * Gets all revisions, currently hold by box.
     * @return whole content
     */
    List<CalculableHolder<T>> toContent();
    /**
     * Tells whether this box has modifications (is dirty).
     * @return true, if so, false otherwise
     */
    boolean isDirty();
    /**
     * Pushes a calculable to the source system stack.
     * @param record calculable
     */
    void push(CalculableHolder<T> record);
    /**
     * Pushes a collection of calculables to the source system stack.
     * @param records calculables
     */
    void push(Collection<CalculableHolder<T>> records);
    /**
     * Peeks the last added calculable by the source system.
     * @param boxKey the source system name
     * @return calculable or null
     */
    CalculableHolder<T> peek(String boxKey);
    /**
     * Gets the number of versions by source system and external id.
     * @param boxKey the source system name
     * @return number of versions
     */
    int count(String boxKey);
    /**
     * Gets the number of new versions (above base with revision == 0) by source system and external id.
     * @param boxKey the source system name
     * @return number of new versions
     */
    int modifications(String boxKey);
    /**
     * Returns total number of revisions (of all keys), hold currently by the box.
     * @return total number of revisions
     */
    int total();
    /**
     * Resets versions to calculation base by source system and external id.
     * @param boxKey the source system name
     * @return number of versions
     */
    List<CalculableHolder<T>> reset(String boxKey);
    /**
     * Resets latest versions by given count, source system and external id.
     * @param boxKey the source system name
     * @return number of versions
     */
    List<CalculableHolder<T>> resetBy(String boxKey, int count);
    /**
     * Pops the last added calculable by the source system.
     * @param boxKey the source system name
     * @return calculable or null
     */
    CalculableHolder<T> pop(String boxKey);
    /**
     * Gets the modification cause.
     * @return initial record
     */
    DataRecord getCalculationState();
    /**
     * Sets the modification cause.
     * @param cause initial record
     */
    void setCalculationState(DataRecord cause);
}
