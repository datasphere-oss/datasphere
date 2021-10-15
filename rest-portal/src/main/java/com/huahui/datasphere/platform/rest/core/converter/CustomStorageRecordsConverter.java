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

import org.unidata.mdm.core.dto.CustomStorageRecordDTO;

import com.huahui.datasphere.platform.rest.core.ro.settings.CustomStorageRecordRO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CustomStorageRecordsConverter {

    public static List<CustomStorageRecordRO> convertToRO(List<CustomStorageRecordDTO> source) {
        if(source == null){
            return Collections.emptyList();
        }
        return source.stream().map(CustomStorageRecordsConverter::convertToRO).collect(Collectors.toList());
    }

    public static List<CustomStorageRecordDTO> convertToDTO(List<CustomStorageRecordRO> source) {
        if(source == null){
            return Collections.emptyList();
        }
        return source.stream().map(CustomStorageRecordsConverter::convertToDTO).collect(Collectors.toList());
    }

    public static CustomStorageRecordRO convertToRO(CustomStorageRecordDTO source) {
        CustomStorageRecordRO target = new CustomStorageRecordRO();
        target.setKey(source.getKey());
        target.setUser(source.getUser());
        target.setUpdateDate(source.getUpdateDate());
        target.setValue(source.getValue());
        return target;
    }

    public static CustomStorageRecordDTO convertToDTO(CustomStorageRecordRO source) {
        CustomStorageRecordDTO target = new CustomStorageRecordDTO();
        target.setKey(source.getKey());
        target.setUser(source.getUser());
        target.setUpdateDate(source.getUpdateDate());
        target.setValue(source.getValue());
        return target;
    }

}
