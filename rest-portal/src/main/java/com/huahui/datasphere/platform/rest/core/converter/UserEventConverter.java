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
/**
 *
 */
package com.huahui.datasphere.platform.rest.core.converter;

import java.util.List;

import org.unidata.mdm.core.dto.UserEventDTO;

import com.huahui.datasphere.platform.rest.core.ro.UserEventRO;

/**
 * @author Mikhail Mikhailov
 */
public class UserEventConverter {

    /**
     * Constructor.
     */
    private UserEventConverter() {
        super();
    }

    /**
     * To REST from system.
     *
     * @param source system
     * @return REST
     */
    public static UserEventRO to(UserEventDTO source) {
        if (source == null) {
            return null;
        }

        UserEventRO target = new UserEventRO();

        target.setId(source.getId());
        target.setBinaryDataId(source.getBinaryDataId());
        target.setCharacterDataId(source.getCharacterDataId());
        target.setCreateDate(source.getCreateDate());
        target.setCreatedBy(source.getCreatedBy());
        target.setType(source.getType());
        target.setContent(source.getContent());
        target.setDetails(source.getDetails());

        return target;
    }

    /**
     * To REST from system.
     *
     * @param source system
     * @param target REST
     */
    public static void to(List<UserEventDTO> source, List<UserEventRO> target) {
        if (source == null || source.isEmpty()) {
            return;
        }

        for (UserEventDTO d : source) {
            target.add(to(d));
        }
    }
}
