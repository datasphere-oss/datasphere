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


import com.huahui.datasphere.portal.type.security.SecurityLabelAttribute;
import com.huahui.datasphere.rest.system.ro.security.SecurityLabelAttributeRO;

/**
 * replaced SecurityLabelAttribute conversion method from duplicates
 *
 * @author maria.chistyakova
 * @since 21.05.2019
 */
class SecurityLabelAttributeConverter {

    private SecurityLabelAttributeConverter() {
    }

    /**
     * Convert security label attribute dto.
     *
     * @param source the source
     * @return the security label attribute ro
     */
    static SecurityLabelAttributeRO convertSecurityLabelAttributeDTO(SecurityLabelAttribute source) {
        if (source == null) {
            return null;
        }
        SecurityLabelAttributeRO target = new SecurityLabelAttributeRO();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setValue(source.getValue());
        target.setPath(source.getPath());
        return target;
    }
}
