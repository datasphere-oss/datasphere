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
package org.datasphere.mdm.core.type.load;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.datasphere.mdm.core.context.DataImportInputContext;
import org.datasphere.mdm.core.context.DataImportTemplateContext;
import org.datasphere.mdm.core.dto.DataImportTemplateResult;

/**
 * @author Mikhail Mikhailov on May 13, 2021
 * A load handler, capable to load data from supplied input stream.
 */
public interface DataImportHandler {
    /**
     * Gets the unique load handler name.
     * @return handler id
     */
    @Nonnull
    String getId();
    /**
     * Gets translated handler description.
     * @return description
     */
    @Nonnull
    String getDescription();
    /**
     * Returns import formats, supported by this load handler.
     * @return import formats, supported by this load handler
     */
    Collection<DataImportFormat> getSupported();
    /**
     * Loads the supplied data.
     * @param ctx the context
     */
    void handle(DataImportInputContext ctx);
    /**
     * Gets
     * @param ctx
     * @return
     */
    default DataImportTemplateResult template(DataImportTemplateContext ctx) {
        throw new UnsupportedOperationException(getId() + " handler does not support templates.");
    }
}
