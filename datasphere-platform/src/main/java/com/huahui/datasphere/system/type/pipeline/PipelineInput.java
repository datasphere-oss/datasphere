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

package com.huahui.datasphere.system.type.pipeline;

/**
 * Base type for pipeline parameter types
 * @author Mikhail Mikhailov on Dec 11, 2019
 */
public interface PipelineInput {
    /**
     * Gets the pipeline start segment type this context works with.
     * @return pipeline start type
     */
    String getStartTypeId();
    /**
     * Narrow self to particular type.
     * Dangerous! The ability must be checked obligatory via 'supports' call.
     * @param <T> the target type
     * @return narrowed object
     */
    @SuppressWarnings("unchecked")
    default <T> T narrow() {
        return (T) this;
    }
}
