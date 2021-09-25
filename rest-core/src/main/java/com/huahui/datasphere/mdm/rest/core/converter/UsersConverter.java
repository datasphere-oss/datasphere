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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import com.huahui.datasphere.mdm.core.dto.RoleDTO;
import com.huahui.datasphere.mdm.core.dto.SecurityLabelAttributeDTO;
import com.huahui.datasphere.mdm.core.dto.SecurityLabelDTO;
import com.huahui.datasphere.mdm.core.dto.UserDTO;
import com.huahui.datasphere.mdm.core.dto.UserEndpointDTO;
import com.huahui.datasphere.mdm.core.dto.UserPropertyDTO;
import com.huahui.datasphere.mdm.core.dto.UserWithPasswordDTO;
import com.huahui.datasphere.mdm.core.po.security.UserPropertyPO;
import com.huahui.datasphere.mdm.core.po.security.UserPropertyValuePO;
import com.huahui.datasphere.mdm.core.type.security.CustomProperty;
import com.huahui.datasphere.mdm.core.type.security.Endpoint;
import com.huahui.datasphere.mdm.core.type.security.Role;
import com.huahui.datasphere.mdm.core.type.security.SecurityDataSource;
import com.huahui.datasphere.mdm.core.type.security.SecurityLabel;
import com.huahui.datasphere.mdm.core.type.security.SecurityLabelAttribute;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;
import com.huahui.datasphere.mdm.rest.system.ro.security.SecurityLabelAttributeRO;
import com.huahui.datasphere.mdm.rest.system.ro.security.SecurityLabelRO;

import com.huahui.datasphere.mdm.rest.core.ro.SecurityDataSourceRO;
import com.huahui.datasphere.mdm.rest.core.ro.UserAPIRO;
import com.huahui.datasphere.mdm.rest.core.ro.UserPropertyRO;
import com.huahui.datasphere.mdm.rest.core.ro.UserRO;
import com.huahui.datasphere.mdm.rest.core.ro.UserWithPasswordRO;

/**
 * The Class UsersConverter.
 */
public class UsersConverter {

    private UsersConverter() {
    }

    /**
     * Convert user dto.
     *
     * @param source the source
     * @return the user ro
     */
    public static UserRO convertUserDTO(final UserDTO source) {

        if (source == null) {
            return null;
        }

        final UserRO target = new UserRO();
        target.setActive(source.isActive());
        target.setAdmin(source.isAdmin());
        target.setCreatedAt(source.getCreatedAt());
        target.setCreatedBy(source.getCreatedBy());
        target.setEmail(source.getEmail());
        target.setLocale(source.getLocale() != null ? source.getLocale().getLanguage() : null);
        target.setFirstName(source.getFirstName());
        target.setFullName(source.getFullName());
        target.setLastName(source.getLastName());
        target.setLogin(source.getLogin());
        target.setEndpoints(convertAPIsDtoToRo(source.getEndpoints()));
        target.setRoles(CollectionUtils.isEmpty(source.getRoles())
                ? Collections.emptyList()
                : source.getRoles().stream()
                .sorted(Comparator.comparing(o -> o.getDisplayName().toLowerCase()))
                .map(Role::getName)
                .collect(Collectors.toList()));
        target.setUpdatedAt(source.getUpdatedAt());
        target.setUpdatedBy(source.getUpdatedBy());
        target.setSecurityLabels(convertSecurityLabelDTOs(source.getSecurityLabels()));
        target.setProperties(convertPropertiesDtoToRo(source.getCustomProperties()));
        target.setExternal(source.isExternal());
        target.setSecurityDataSource(source.getSecurityDataSource());
        target.setEmailNotification(source.isEmailNotification());
        return target;
    }

    /**
     * Convert security label dt os.
     *
     * @param source the source
     * @return the list
     */
    public static List<SecurityLabelRO> convertSecurityLabelDTOs(final Collection<SecurityLabel> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        final List<SecurityLabelRO> target = new ArrayList<>();
        source.forEach(s -> target.add(convertSecurityLabelDTO(s)));
        return target;
    }

    /**
     * Convert security label dto.
     *
     * @param source the source
     * @return the security label ro
     */
    private static SecurityLabelRO convertSecurityLabelDTO(SecurityLabel source) {
        if (source == null) {
            return null;
        }
        SecurityLabelRO target = new SecurityLabelRO();
        target.setDisplayName(source.getDisplayName());
        target.setName(source.getName());
        target.setAttributes(convertAttributeDTOs(source.getAttributes()));
        return target;
    }

    /**
     * Convert attribute dt os.
     *
     * @param source the source
     * @return the list
     */
    private static List<SecurityLabelAttributeRO> convertAttributeDTOs(final List<SecurityLabelAttribute> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        final List<SecurityLabelAttributeRO> target = new ArrayList<>();
        source.forEach(s -> target.add(SecurityLabelAttributeConverter.convertSecurityLabelAttributeDTO(s)));
        return target;
    }


    /**
     * Convert user ro.
     *
     * @param source the source
     * @return the user with password dto
     */
    public static UserWithPasswordDTO convertUserRO(final UserWithPasswordRO source) {
        if (source == null) {
            return null;
        }
        final UserWithPasswordDTO target = new UserWithPasswordDTO();
        target.setActive(source.isActive());
        target.setAdmin(source.isAdmin());
        target.setEmail(source.getEmail());
        if (source.getLocale() != null) {
            target.setLocale(new Locale(source.getLocale()));
        }
        target.setFirstName(source.getFirstName());
        target.setFullName(source.getFullName());
        target.setLastName(source.getLastName());
        target.setLogin(source.getLogin());
        target.setPassword(source.getPassword());
        target.setRoles(convertRoles(source.getRoles()));
        target.setEnpoints(convertAPIRoToDtos(source.getEndpoints()));
        target.setSecurityLabels(convertLabels(source.getSecurityLabels()));
        target.setProperties(convertPropertiesRoToDto(source.getProperties()));
        target.setExternal(source.isExternal());
        target.setSecurityDataSource(Objects.isNull(source.getSecurityDataSource())
                ? SecurityUtils.UNIDATA_SECURITY_DATA_SOURCE
                : source.getSecurityDataSource());
        target.setEmailNotification(source.isEmailNotification());

        return target;
    }

    /**
     * Convert API ro to dtos.
     *
     * @param source the source
     * @return the list
     */
    private static List<Endpoint> convertAPIRoToDtos(List<UserAPIRO> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        List<Endpoint> target = new ArrayList<>();
        source.forEach(a -> target.add(convertAPIRoToDto(a)));
        return target;
    }

    /**
     * Convert API ro to dto.
     *
     * @param source the source
     * @return the user APIDTO
     */
    private static UserEndpointDTO convertAPIRoToDto(UserAPIRO source) {
        if (source == null) {
            return null;
        }
        UserEndpointDTO target = new UserEndpointDTO();
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setDisplayName(source.getDisplayName());
        return target;
    }

    /**
     * Convert roles.
     *
     * @param source the source
     * @return the list
     */
    private static List<Role> convertRoles(List<String> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        List<Role> target = new ArrayList<>();
        source.forEach(name -> {
            RoleDTO r = new RoleDTO();
            r.setName(name);
            target.add(r);
        });

        return target;
    }
    /**
     * Convert AP is dto to ro.
     *
     * @param source the source
     * @return the list
     */
    public static List<UserAPIRO> convertAPIsDtoToRo(final List<? extends Endpoint> source) {

        if (source == null) {
            return new ArrayList<>();
        }

        final List<UserAPIRO> target = new ArrayList<>();
        source.forEach(s -> target.add(convertAPIDtoToRo(s)));
        return target;
    }

    /**
     * Convert API dto to ro.
     *
     * @param source the source
     * @return the user APIRO
     */
    private static UserAPIRO convertAPIDtoToRo(Endpoint source) {
        if (source == null) {
            return null;
        }
        UserAPIRO target = new UserAPIRO();
        target.setDescription(source.getDescription());
        target.setDisplayName(source.getDisplayName());
        target.setName(source.getName());
        return target;
    }

    /**
     * Convert properties.
     *
     * @param source the source
     * @return the list
     */
    public static List<UserPropertyRO> convertPropertiesDtoToRo(final List<? extends CustomProperty> source) {

        if (source == null) {
            return new ArrayList<>();
        }

        final List<UserPropertyRO> target = new ArrayList<>();
        source.forEach(s -> target.add(convertPropertyDtoToRo(s)));
        return target;
    }

    /**
     * Convert security data source.
     *
     * @param source the source
     * @return the security data source RO
     */
    private static SecurityDataSourceRO convertSecurityDataSource(SecurityDataSource source) {

        if (Objects.isNull(source)) {
            return null;
        }

        SecurityDataSourceRO target = new SecurityDataSourceRO();
        target.setName(source.getName());
        target.setDescription(source.getDescription());

        return target;
    }

    /**
     * Convert security data sources.
     *
     * @param source the source
     * @return the list
     */
    public static List<SecurityDataSourceRO> convertSecurityDataSources(Collection<SecurityDataSource> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        List<SecurityDataSourceRO> target = new ArrayList<>();
        source.forEach(s -> target.add(convertSecurityDataSource(s)));
        return target;
    }

    /**
     * Convert property.
     *
     * @param source the source
     * @return the user property
     */
    public static UserPropertyRO convertPropertyDtoToRo(final CustomProperty source) {

        if (source == null) {
            return null;
        }

        final UserPropertyRO target = new UserPropertyRO();
        target.setName(source.getName());
        target.setDisplayName(source.getDisplayName());
        target.setRequired(source instanceof UserPropertyDTO && ((UserPropertyDTO) source).isRequired());
        target.setValue(source.getValue());
        target.setId(source instanceof UserPropertyDTO ? ((UserPropertyDTO) source).getId() : 0);
        return target;
    }

    /**
     * Convert properties.
     *
     * @param source the source
     * @return the list
     */
    private static List<UserPropertyDTO> convertPropertiesRoToDto(final List<UserPropertyRO> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        return source.stream()
                .map(UsersConverter::convertPropertyRoToDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Convert property.
     *
     * @param source the source
     * @return the user property
     */
    public static UserPropertyDTO convertPropertyRoToDto(final UserPropertyRO source) {

        if (source == null) {
            return null;
        }

        final UserPropertyDTO target = new UserPropertyDTO();
        target.setName(source.getName());
        target.setRequired(source.isRequired());
        target.setDisplayName(source.getDisplayName());
        target.setValue(source.getValue());
        target.setId(source.getId());
        return target;
    }

    /**
     * Convert labels.
     *
     * @param source the source
     * @return the list
     */
    private static List<SecurityLabel> convertLabels(List<SecurityLabelRO> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        final List<SecurityLabel> target = new ArrayList<>();
        source.forEach(s -> target.add(convertLabel(s)));
        return target;
    }

    /**
     * Convert label.
     *
     * @param source the source
     * @return the security label dto
     */
    private static SecurityLabel convertLabel(SecurityLabelRO source) {
        if (source == null) {
            return null;
        }
        final SecurityLabelDTO target = new SecurityLabelDTO();
        target.setName(source.getName());
        target.setDisplayName(source.getDisplayName());
        target.setAttributes(convertAttributes(source.getAttributes()));
        return target;
    }

    /**
     * Convert attributes.
     *
     * @param source the source
     * @return the list
     */
    private static List<SecurityLabelAttribute> convertAttributes(List<SecurityLabelAttributeRO> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        List<SecurityLabelAttribute> target = new ArrayList<>();
        source.forEach(s -> target.add(convertAttribute(s)));
        return target;
    }

    /**
     * Convert attribute.
     *
     * @param source the source
     * @return the security label attribute dto
     */
    private static SecurityLabelAttributeDTO convertAttribute(SecurityLabelAttributeRO source) {
        if (source == null) {
            return null;
        }
        SecurityLabelAttributeDTO target = new SecurityLabelAttributeDTO();
        target.setName(source.getName());
        target.setValue(source.getValue());
        return target;
    }

    /**
     * Convert user dt os.
     *
     * @param source the source
     * @return the list
     */
    public static List<UserRO> convertUserDTOs(final List<UserDTO> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        final List<UserRO> target = new ArrayList<>();
        source.forEach(s -> target.add(convertUserDTO(s)));
        return target;
    }

    /**
     * Convert property po to dto.
     *
     * @param propertyPO the property PO
     * @return the user property DTO
     */
    private static UserPropertyDTO convertPropertyPoToDto(UserPropertyPO propertyPO) {
        if (propertyPO == null) {
            return null;
        }

        UserPropertyDTO dto = new UserPropertyDTO();

        dto.setId(propertyPO.getId());
        dto.setRequired(propertyPO.isRequired());
        dto.setName(propertyPO.getName());
        dto.setDisplayName(propertyPO.getDisplayName());
        dto.setReadOnly(propertyPO.isReadOnly());
        dto.setFieldType(propertyPO.getFieldType());

        return dto;
    }

    /**
     * Convert properties po to dto.
     *
     * @param propertyPOs the property P os
     * @return the list
     */
    public static List<UserPropertyDTO> convertPropertiesPoToDto(List<UserPropertyPO> propertyPOs) {
        if (propertyPOs == null) {
            return new ArrayList<>();
        }
        final List<UserPropertyDTO> target = new ArrayList<>();
        propertyPOs.forEach(s -> target.add(convertPropertyPoToDto(s)));
        return target;
    }

    /**
     * Convert property dto to po.
     *
     * @param propertyDTO the property DTO
     * @return the user property PO
     */
    public static UserPropertyPO convertPropertyDtoToPo(UserPropertyDTO propertyDTO) {
        if (propertyDTO == null) {
            return null;
        }

        UserPropertyPO po = new UserPropertyPO();
        po.setId(propertyDTO.getId());
        po.setRequired(propertyDTO.isRequired());
        po.setReadOnly(propertyDTO.isReadOnly());
        po.setName(StringUtils.trim(propertyDTO.getName()));
        po.setDisplayName(StringUtils.trim(propertyDTO.getDisplayName()));
        po.setFieldType(propertyDTO.getFieldType());

        return po;
    }

    /**
     * Convert property value po to dto.
     *
     * @param valuePO the value PO
     * @return the user property DTO
     */
    private static UserPropertyDTO convertPropertyValuePoToDto(UserPropertyValuePO valuePO) {

        if (valuePO == null) {
            return null;
        }

        UserPropertyDTO dto = new UserPropertyDTO();
        if (valuePO.getProperty() != null) {
            dto.setId(valuePO.getProperty().getId());
            dto.setName(valuePO.getProperty().getName());
            dto.setReadOnly(valuePO.getProperty().isReadOnly());
            dto.setRequired(valuePO.getProperty().isRequired());
            dto.setDisplayName(valuePO.getProperty().getDisplayName());
            dto.setFieldType(valuePO.getProperty().getFieldType());
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
    public static List<UserPropertyDTO> convertPropertyValuesPoToDto(List<UserPropertyValuePO> valuePOs) {

        if (CollectionUtils.isEmpty(valuePOs)) {
            return Collections.emptyList();
        }

        return valuePOs.stream()
                .map(UsersConverter::convertPropertyValuePoToDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Convert property value dto to po.
     *
     * @param valueDto the value dto
     * @return the user property value PO
     */
    private static UserPropertyValuePO convertPropertyValueDtoToPo(final UserPropertyDTO valueDto) {

        if (valueDto == null) {
            return null;
        }

        final UserPropertyPO propertyPO = new UserPropertyPO();
        propertyPO.setName(valueDto.getName());
        propertyPO.setDisplayName(valueDto.getDisplayName());
        propertyPO.setReadOnly(valueDto.isReadOnly());
        propertyPO.setRequired(valueDto.isRequired());
        propertyPO.setId(valueDto.getId());
        propertyPO.setFieldType(valueDto.getFieldType());


        final UserPropertyValuePO valuePO = new UserPropertyValuePO();
        valuePO.setId(valueDto.getId());
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
    public static List<UserPropertyValuePO> convertPropertyValuesDtoToPo(final List<UserPropertyDTO> valueDtos) {

        if (CollectionUtils.isEmpty(valueDtos)) {
            return Collections.emptyList();
        }

        return valueDtos.stream()
                .map(UsersConverter::convertPropertyValueDtoToPo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
