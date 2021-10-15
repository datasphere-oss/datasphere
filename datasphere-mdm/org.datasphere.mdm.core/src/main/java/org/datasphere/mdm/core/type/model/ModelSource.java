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
package org.datasphere.mdm.core.type.model;

/**
 * @author Mikhail Mikhailov on Oct 8, 2020
 * Model source marker, the model descriptor operates on.
 */
public interface ModelSource {
    /**
     * Gets the model instance id, the source is for.
     * This is either real instance name/id (i. e. classifier name) for models that support multiple instances,
     * or just DEFAULT or null for singleton instances, such as DATA, SOURCE_SYSTEMS, ENUMERATIONS, MEASURE_UNITS etc.
     * @return model instance id
     */
    String getInstanceId();
    /**
     * Returns the model type id from configuration (the id supplied with model type descriptor for this model type).
     * @return model type id
     */
    String getTypeId();
}
