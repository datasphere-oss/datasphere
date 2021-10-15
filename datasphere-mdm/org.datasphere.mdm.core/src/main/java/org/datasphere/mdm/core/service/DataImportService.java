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
package org.datasphere.mdm.core.service;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.datasphere.mdm.core.context.DataImportInputContext;
import org.datasphere.mdm.core.context.DataImportTemplateContext;
import org.datasphere.mdm.core.dto.DataImportTemplateResult;
import org.datasphere.mdm.core.type.load.DataImportHandler;
import org.datasphere.mdm.core.type.load.DataImportHandlerInfo;

/**
 * @author Mikhail Mikhailov on May 13, 2021
 * The data load service, responsible for loading various kinds of data in different formats.
 * The actual work of loading data is delegated to handlers, which are registered at runtime.
 */
public interface DataImportService {
    /**
     * Registers a data handler.
     * @param handler the handler to register
     */
    void register(@Nonnull DataImportHandler handler);
    /**
     * Loads data.
     * @param ctx the context
     */
    void handle(DataImportInputContext ctx);
    /**
     * Gets a template for import, if this is supported by the handler.
     * @param ctx the context
     * @return result
     */
    DataImportTemplateResult template(DataImportTemplateContext ctx);
    /**
     * returns info about curently registered handlers.
     * @return collection of info elements
     */
    Collection<DataImportHandlerInfo> handlers();
}
