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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author Mikhail Mikhailov on Oct 6, 2020
 */
public abstract class AbstractCustomPropertiesHolder<X extends AbstractCustomPropertiesHolder<X>> implements Serializable {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 2465578389809563969L;
    /**
     * The properties.
     */
    @JacksonXmlElementWrapper(localName = "properties", useWrapping = true)
    @JacksonXmlProperty(localName = "property")
    private List<CustomProperty> customProperties;
    /**
     * Constructor.
     */
    protected AbstractCustomPropertiesHolder() {
        super();
    }

    public List<CustomProperty> getCustomProperties() {
        if (customProperties == null) {
            customProperties = new ArrayList<>();
        }
        return customProperties;
    }

    public void setCustomProperties(List<CustomProperty> customProperties) {
        this.customProperties = customProperties;
    }

    public X withCustomProperties(CustomProperty... values) {
        if (ArrayUtils.isNotEmpty(values)) {
            return withCustomProperties(Arrays.asList(values));
        }
        return self();
    }

    public X withCustomProperties(Collection<CustomProperty> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            getCustomProperties().addAll(values);
        }
        return self();
    }

    @SuppressWarnings("unchecked")
    protected X self() {
        return (X) this;
    }
}
