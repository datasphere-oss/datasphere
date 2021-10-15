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
package org.datasphere.mdm.core.type.model.source;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * General purpose custom property.
 */
public class CustomProperty implements Serializable {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = -2004657676497629391L;

    @JacksonXmlProperty(isAttribute = true)
    protected String name;

    @JacksonXmlProperty(isAttribute = true)
    protected String value;

    @Override
    public String toString() {
        return "CustomProperty{" + "name='" + name + "', value='" + value + "'}";
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CustomProperty withName(String value) {
        setName(value);
        return this;
    }

    public CustomProperty withValue(String value) {
        setValue(value);
        return this;
    }
}
