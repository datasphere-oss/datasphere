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

import java.util.Date;

import org.datasphere.mdm.core.type.data.RecordStatus;
import org.datasphere.mdm.core.type.keys.OriginKey;

/**
 * @author Mikhail Mikhailov
 * Holder for calculation objects.
 */
public interface CalculableHolder<T> extends ModificationBoxKey {
    /**
     * @return the relation
     */
    T getValue();
    /**
     * @return the name
     */
    String getTypeName();
    /**
     * @return the sourceSystem
     */
    String getSourceSystem();
    /**
     * @return the external id (if present)
     */
    String getExternalId();
    /**
     * @return the status
     */
    RecordStatus getStatus();

    /**
     * @return the last update date
     */
    Date getLastUpdate();
    /**
     * Gets the revision of the object hold, if applicable.
     * @return revision (&gt; 0), -1 if not applicable or 0 for new objects
     */
    int getRevision();
    /**
     * Validity period from.
     * @return from
     */
    Date getValidFrom();
    /**
     * Validity period to.
     * @return to
     */
    Date getValidTo();
    /**
     * Tells whether this calculable is an enrichment.
     * @return true, if so, false otherwise
     */
    boolean isEnrichment();
    /**
     * Returns true, if this calculable as a modification to timeline (has revision 0).
     * @return true, if this calculable as a modification to timeline (has revision 0), false otherwise
     */
    default boolean isModification() {
        return getRevision() == 0;
    }
    /**
     * Gets the origin key of the value.
     * This might be useful in situation with lowered types visibility.
     * Returns null for attributes.
     * @return origin key
     */
    OriginKey getOriginKey();
}
