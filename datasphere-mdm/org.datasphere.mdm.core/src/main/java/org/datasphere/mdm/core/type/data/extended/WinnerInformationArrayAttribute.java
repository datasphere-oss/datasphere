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

package org.datasphere.mdm.core.type.data.extended;

import org.datasphere.mdm.core.type.data.ArrayAttribute;


/**
 * @author Dmitrii Kopin
 * Array attribute, which contains information about winner source.
 */
public interface WinnerInformationArrayAttribute<T> extends ArrayAttribute<T> {
    /**
     * get winner source system
     * @return winner source system
     */
    String getWinnerSourceSystem();

    /**
     * get winner external identificator
     * @return winner external identificator
     */
    String getWinnerExternalId();
}
