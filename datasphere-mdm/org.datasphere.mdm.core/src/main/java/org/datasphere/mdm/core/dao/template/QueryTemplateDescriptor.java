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

package org.datasphere.mdm.core.dao.template;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.system.exception.PlatformFailureException;

/**
 * @author Mikhail Mikhailov
 * The template descriptor.
 */
public interface QueryTemplateDescriptor {
    /**
     * The template's code.
     * @return code
     */
    String getCode();
    /**
     * Tells the caller, whether this decriptor denotes a distributed query or not
     * @return true for distributed queries, false otherwise
     */
    boolean isDistributed();

    static<T extends QueryTemplateDescriptor> QueryTemplates toTemplates(T[] values, Map<T, QueryTemplate> map, Properties p) {

        for (int i = 0; i < values.length; i++) {

            T drq = values[i];
            String source = p.getProperty(drq.getCode());
            if (Objects.isNull(source)) {
                throw new PlatformFailureException(
                        "No record query template found for given descriptor [{}]",
                        CoreExceptionIds.EX_DATA_STORAGE_NO_QUERY_TEMPLATE_FOR_DECSRIPTOR,
                        drq.getCode());
            }

            map.put(drq, new QueryTemplate(source));
        }

        return new QueryTemplates(map);
    }
}
