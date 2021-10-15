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
import org.springframework.util.CollectionUtils;

import com.huahui.datasphere.mdm.core.dto.UserPropertyDTO;
import com.huahui.datasphere.mdm.core.po.security.UserPropertyPO;
import com.huahui.datasphere.mdm.core.po.security.UserPropertyValuePO;

/**
 * @author theseusyang on Sep 24, 2019
 */
public class UserPropertyConverter {

    /**
     * Constructor.
     */
    private UserPropertyConverter() {
        super();
    }

    /**
     * Convert property po to dto.
     *
     * @param propertyPO the property PO
     * @return the user property DTO
     */
    public static UserPropertyDTO convert(UserPropertyPO propertyPO) {

        if (propertyPO == null) {
            return null;
        }

        UserPropertyDTO dto = new UserPropertyDTO();

        dto.setId(propertyPO.getId());
        dto.setRequired(propertyPO.isRequired());
        dto.setName(propertyPO.getName());
        dto.setDisplayName(propertyPO.getDisplayName());

        return dto;
    }

    /**
     * Convert properties po to dto.
     *
     * @param propertyPOs the property P os
     * @return the list
     */
    public static List<UserPropertyDTO> convertPropertyPOs(List<UserPropertyPO> propertyPOs) {
        if (propertyPOs == null) {
            return new ArrayList<>();
        }
        final List<UserPropertyDTO> target = new ArrayList<>();
        propertyPOs.forEach(s -> target.add(convert(s)));
        return target;
    }

    /**
     * Convert property dto to po.
     *
     * @param propertyDTO the property DTO
     * @return the user property PO
     */
    public static UserPropertyPO convert(UserPropertyDTO propertyDTO) {
        if (propertyDTO == null) {
            return null;
        }

        UserPropertyPO po = new UserPropertyPO();
        po.setId(propertyDTO.getId());
        po.setRequired(propertyDTO.isRequired());
        po.setName(StringUtils.trim(propertyDTO.getName()));
        po.setDisplayName(StringUtils.trim(propertyDTO.getDisplayName()));

        return po;
    }

    /**
     * Convert property value po to dto.
     *
     * @param valuePO the value PO
     * @return the user property DTO
     */
    public static UserPropertyDTO convert(UserPropertyValuePO valuePO) {

        if (valuePO == null) {
            return null;
        }

        UserPropertyDTO dto = new UserPropertyDTO();
        if (valuePO.getProperty() != null) {
            dto.setId(valuePO.getProperty().getId());
            dto.setName(valuePO.getProperty().getName());
            dto.setDisplayName(valuePO.getProperty().getDisplayName());
        }

        dto.setValue(valuePO.getValue());
        return dto;
    }

    /**
     * Convert property values po to dto.
     *
     * @param valuePOs the value P os
     * @return the list
     */
    public static List<UserPropertyDTO> convertValuePOs(List<UserPropertyValuePO> valuePOs) {

        if (CollectionUtils.isEmpty(valuePOs)) {
            return Collections.emptyList();
        }

        return valuePOs.stream()
                .map(UserPropertyConverter::convert)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Convert property value dto to po.
     *
     * @param valueDto the value dto
     * @return the user property value PO
     */
    public static UserPropertyValuePO convertValueDTO(final UserPropertyDTO valueDto) {

        if (valueDto == null) {
            return null;
        }

        final UserPropertyPO propertyPO = new UserPropertyPO();
        propertyPO.setName(valueDto.getName());
        propertyPO.setDisplayName(valueDto.getDisplayName());
        propertyPO.setId(valueDto.getId());

        final UserPropertyValuePO valuePO = new UserPropertyValuePO();
        valuePO.setProperty(propertyPO);
        valuePO.setValue(valueDto.getValue());

        return valuePO;
    }

    /**
     * Convert property values dto to po.
     *
     * @param valueDtos the value dtos
     * @return the list
     */
    public static List<UserPropertyValuePO> convertPropertyDTOs(final List<UserPropertyDTO> valueDtos) {

        if (CollectionUtils.isEmpty(valueDtos)) {
            return Collections.emptyList();
        }

        return valueDtos.stream()
                .map(UserPropertyConverter::convertValueDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
