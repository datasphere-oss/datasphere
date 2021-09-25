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
package com.huahui.datasphere.mdm.rest.core.converter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import com.huahui.datasphere.mdm.core.type.model.CustomPropertyElement;
import com.huahui.datasphere.mdm.core.type.model.source.CustomProperty;

import com.huahui.datasphere.mdm.rest.core.ro.CustomPropertyRO;

import static java.util.stream.Collectors.toList;

public class CustomPropertiesConverter {

    private CustomPropertiesConverter() {
        super();
    }

    public static List<CustomPropertyRO> to(Collection<CustomPropertyElement> cp) {

        if (CollectionUtils.isEmpty(cp)) {
            return Collections.emptyList();
        }

        return cp.stream()
                .map(CustomPropertiesConverter::to)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static CustomPropertyRO to(CustomPropertyElement cpe) {

        if (Objects.isNull(cpe)) {
            return null;
        }

        return new CustomPropertyRO(cpe.getName(), cpe.getValue());
    }

    public static List<CustomPropertyRO> to(List<CustomProperty> cp) {

        if (CollectionUtils.isEmpty(cp)) {
            return Collections.emptyList();
        }

        return cp.stream()
                .map(CustomPropertiesConverter::to)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static CustomPropertyRO to(CustomProperty cpe) {

        if (Objects.isNull(cpe)) {
            return null;
        }

        return new CustomPropertyRO(cpe.getName(), cpe.getValue());
    }

    public static List<CustomProperty> from(final Collection<CustomPropertyRO> cp) {

        if (CollectionUtils.isEmpty(cp)) {
            return Collections.emptyList();
        }

        return cp.stream()
                .map(CustomPropertiesConverter::from)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    public static CustomProperty from(final CustomPropertyRO cp) {

        if (Objects.isNull(cp)) {
            return null;
        }

        return new CustomProperty()
                .withName(cp.getName())
                .withValue(cp.getValue());
    }

}
