/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.huahui.datasphere.mdm.core.type.model.source;

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
 * @author theseusyang on Oct 6, 2020
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
