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

package org.datasphere.mdm.core.service.job;

import javax.annotation.Nonnull;

public interface JobComplexParameterHolder {
    /**
     * Put complex parameter to container.
     * @param key - key
     * @param parameter -  parameter
     */
    void putComplexParameter(@Nonnull String key, @Nonnull Object parameter);
    /**
     * Gets a parameter by key.
     * @param key - key
     * @param <T> -
     * @return complex parameter
     */
    <T> T getComplexParameter(@Nonnull String key);
    /**
     * Clean complex parameter
     * @param key -  key
     */
    void removeComplexParameter(@Nonnull String key);
    /**
     * Combination of methods getComplexParameter() and removeComplexParameter()
     * @param complexParameterKey
     * @param <T>
     * @return
     */
    <T> T getComplexParameterAndRemove(@Nonnull String complexParameterKey);
}
