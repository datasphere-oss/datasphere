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

package org.datasphere.mdm.core.type.data.impl;

import org.datasphere.mdm.core.type.data.Attribute;
import org.datasphere.mdm.core.type.data.DataRecord;

/**
 * @author Mikhail Mikhailov
 * Abstract attribute name holder.
 */
public abstract class AbstractAttribute implements Attribute {
    /**
     * Attribute name.
     */
    private String name;
    /**
     * Record link.
     */
    private DataRecord record;
    /**
     * Special serialization constructor. Schould not be used otherwise.
     */
    protected AbstractAttribute() {
        super();
    }
    /**
     * Constructor.
     * @param name the name of the attribute.
     */
    protected AbstractAttribute(String name) {
        super();
        this.name = name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }
    /**
     * Sets the attribute name. Used for serialization. Should not be used otherwise.
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord getRecord() {
        return record;
    }
    /**
     * @param record the record to set
     */
    @Override
    public void setRecord(DataRecord record) {
        this.record = record;
    }
}
