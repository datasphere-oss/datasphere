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
import java.util.Collections;
import java.util.Objects;

/**
 * @author Mikhail Mikhailov on May 13, 2021
 * Handler info.
 */
public class DataImportHandlerInfo {
    /**
     * Handler's id.
     */
    private String id;
    /**
     * Handler's description.
     */
    private String description;
    /**
     * Supported formats.
     */
    private Collection<DataImportFormat> formats;
    /**
     * Constructor.
     */
    public DataImportHandlerInfo() {
        super();
    }
    /**
     * Constructor.
     */
    public DataImportHandlerInfo(DataImportHandler h) {
        super();
        this.id = h.getId();
        this.description = h.getDescription();
        this.formats = h.getSupported();
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the formats
     */
    public Collection<DataImportFormat> getFormats() {
        return Objects.isNull(formats) ? Collections.emptyList() : formats;
    }
    /**
     * @param formats the formats to set
     */
    public void setFormats(Collection<DataImportFormat> formats) {
        this.formats = formats;
    }
}
