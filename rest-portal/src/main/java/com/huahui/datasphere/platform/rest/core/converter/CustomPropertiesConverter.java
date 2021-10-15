/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package com.huahui.datasphere.platform.rest.core.converter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.unidata.mdm.core.type.model.CustomPropertyElement;
import org.unidata.mdm.core.type.model.source.CustomProperty;

import com.huahui.datasphere.platform.rest.core.ro.CustomPropertyRO;

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
