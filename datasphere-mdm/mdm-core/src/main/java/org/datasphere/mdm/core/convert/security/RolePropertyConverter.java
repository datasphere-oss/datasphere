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

package com.huahui.datasphere.mdm.core.convert.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.huahui.datasphere.mdm.core.dto.RolePropertyDTO;
import com.huahui.datasphere.mdm.core.po.security.RolePropertyPO;
import com.huahui.datasphere.mdm.core.po.security.RolePropertyValuePO;
import com.huahui.datasphere.mdm.core.type.security.CustomProperty;

public class RolePropertyConverter {

    private RolePropertyConverter() {
        super();
    }

    private static RolePropertyDTO convertPropertyPoToDto(RolePropertyPO propertyPO) {
        if (propertyPO == null) {
            return null;
        }

        RolePropertyDTO dto = new RolePropertyDTO();

        dto.setId(propertyPO.getId());
        dto.setRequired(propertyPO.isRequired());
        dto.setName(propertyPO.getName());
        dto.setDisplayName(propertyPO.getDisplayName());
        dto.setReadOnly(propertyPO.isReadOnly());
        dto.setFieldType(propertyPO.getFieldType());

        return dto;
    }

    public static List<RolePropertyDTO> convertPropertiesPoToDto(List<RolePropertyPO> propertyPOs) {
        if (propertyPOs == null) {
            return new ArrayList<>();
        }
        final List<RolePropertyDTO> target = new ArrayList<>();
        propertyPOs.forEach(s -> target.add(convertPropertyPoToDto(s)));
        return target;
    }

    public static RolePropertyPO convertPropertyDtoToPo(RolePropertyDTO propertyDTO) {
        if (propertyDTO == null) {
            return null;
        }

        RolePropertyPO po = new RolePropertyPO();
        po.setId(propertyDTO.getId());
        po.setName(StringUtils.trim(propertyDTO.getName()));
        po.setRequired(propertyDTO.isRequired());
        po.setReadOnly(propertyDTO.isReadOnly());
        po.setDisplayName(StringUtils.trim(propertyDTO.getDisplayName()));
        po.setFieldType(StringUtils.trim(propertyDTO.getFieldType()));

        return po;
    }

    public static List<RolePropertyPO> convertPropertiesDtoToPo(List<RolePropertyDTO> propertyDTOs) {
        if (propertyDTOs == null) {
            return new ArrayList<>();
        }

        final List<RolePropertyPO> target = new ArrayList<>();
        propertyDTOs.forEach(s -> target.add(convertPropertyDtoToPo(s)));
        return target;
    }


    public static RolePropertyValuePO convertPropertyValueDtoToPo(CustomProperty valueDto) {

        if (valueDto == null) {
            return null;
        }

        RolePropertyValuePO valuePO = new RolePropertyValuePO();

        RolePropertyPO propertyPO = new RolePropertyPO();
        propertyPO.setId(valueDto instanceof RolePropertyDTO ? ((RolePropertyDTO) valueDto).getId() : Long.valueOf(0l));
        propertyPO.setName(valueDto.getName());
        propertyPO.setDisplayName(valueDto.getDisplayName());
        propertyPO.setFieldType(valueDto.getFieldType());

        if (valueDto instanceof RolePropertyDTO) {
            RolePropertyDTO rolePropertyDTO = (RolePropertyDTO) valueDto;

            // override ID
            propertyPO.setId(rolePropertyDTO.getId());

            propertyPO.setRequired(rolePropertyDTO.isRequired());
            propertyPO.setReadOnly(rolePropertyDTO.isReadOnly());
        }


        valuePO.setProperty(propertyPO);
        valuePO.setValue(valueDto.getValue());

        return valuePO;
    }

    public static List<RolePropertyValuePO> convertPropertyValuesDtoToPo(List<CustomProperty> source) {

        if (source == null) {
            return Collections.emptyList();
        }

        return source.stream()
                .map(RolePropertyConverter::convertPropertyValueDtoToPo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static List<CustomProperty> convertPropertyValues(List<RolePropertyValuePO> valuePOs) {

        if (valuePOs == null) {
            return Collections.emptyList();
        }

        return valuePOs.stream()
                .map(RolePropertyConverter::convertPropertyValue)
                .filter(Objects::nonNull)
                .map(v -> (CustomProperty) v)
                .collect(Collectors.toList());
    }

    /**
     * @param valuePO
     * @return
     */
    public static RolePropertyDTO convertPropertyValue(RolePropertyValuePO valuePO) {

        if (valuePO == null) {
            return null;
        }

        RolePropertyDTO dto = new RolePropertyDTO();
        if (valuePO.getProperty() != null) {
            dto.setId(valuePO.getProperty().getId());
            dto.setName(valuePO.getProperty().getName());
            dto.setDisplayName(valuePO.getProperty().getDisplayName());
            dto.setRequired(valuePO.getProperty().isRequired());
            dto.setReadOnly(valuePO.getProperty().isReadOnly());
        }

        dto.setValue(valuePO.getValue());
        return dto;
    }


}
