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

import org.datasphere.mdm.core.type.calculables.Calculable;

/**
 * @author Mikhail Mikhailov
 * Simple next and previous timeline change view.
 */
public final class TimelineSnapshot <T extends Calculable> {
    /**
     * Previous TL state.
     */
    private final Timeline<T> previous;
    /**
     * Next TL state.
     */
    private final Timeline<T> next;
    /**
     * Constructor.
     * @param previous the former state
     * @param next the next state
     */
    public TimelineSnapshot(Timeline<T> previous, Timeline<T> next) {
        super();
        this.previous = previous;
        this.next = next;
    }
    /**
     * @return the previous
     */
    public Timeline<T> getPrevious() {
        return previous;
    }
    /**
     * @return the next
     */
    public Timeline<T> getNext() {
        return next;
    }

}
