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

package org.datasphere.mdm.core.type.timeline;

import java.util.Collection;
import java.util.Date;

import org.datasphere.mdm.core.type.calculables.Calculable;
import org.datasphere.mdm.core.type.calculables.CalculableHolder;

/**
 * @author Mikhail Mikhailov
 * The factory interface, responsible for creation of new interval instancies.
 */
@FunctionalInterface
public interface TimeIntervalFactory<C extends Calculable> {
    /**
     * full constructor method to implement.
     * @return new time interval
     */
    TimeInterval<C> newInstance(Date validFrom, Date validTo, Collection<CalculableHolder<C>> contributors);
}
