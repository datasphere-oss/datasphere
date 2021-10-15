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

import com.huahui.datasphere.platform.rest.core.ro.SecurityDataSourceRO;
import com.huahui.datasphere.platform.rest.core.ro.UserAPIRO;
import com.huahui.datasphere.platform.rest.core.ro.UserPropertyRO;
import com.huahui.datasphere.platform.rest.core.ro.UserRO;
import com.huahui.datasphere.platform.rest.core.ro.UserWithPasswordRO;
import com.huahui.datasphere.portal.dto.RoleDTO;
import com.huahui.datasphere.portal.dto.SecurityLabelAttributeDTO;
import com.huahui.datasphere.portal.dto.SecurityLabelDTO;
import com.huahui.datasphere.portal.dto.UserDTO;
import com.huahui.datasphere.portal.dto.UserEndpointDTO;
import com.huahui.datasphere.portal.dto.UserPropertyDTO;
import com.huahui.datasphere.portal.dto.UserWithPasswordDTO;
import com.huahui.datasphere.portal.security.po.Role;
import com.huahui.datasphere.portal.security.po.UserProperty;
import com.huahui.datasphere.portal.security.po.UserPropertyValue;
import com.huahui.datasphere.portal.type.security.CustomProperty;
import com.huahui.datasphere.portal.type.security.EndpointInf;
import com.huahui.datasphere.portal.type.security.RoleInf;
import com.huahui.datasphere.portal.type.security.SecurityDataSource;
import com.huahui.datasphere.portal.type.security.SecurityLabelAttribute;
import com.huahui.datasphere.portal.type.security.SecurityLabelInf;
import com.huahui.datasphere.portal.util.SecurityUtils;
import com.huahui.datasphere.rest.system.ro.security.SecurityLabelAttributeRO;
import com.huahui.datasphere.rest.system.ro.security.SecurityLabelRO;

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
        
        target.setFullName(source.getFullName());
        
        target.setLogin(source.getLogin());
        target.setEndpoints(convertAPIsDtoToRo(source.getEndpoints()));
        target.setRoles(CollectionUtils.isEmpty(source.getRoles())
                ? Collections.emptyList()
                : source.getRoles().stream()
                .sorted(Comparator.comparing(o -> o.getDisplayName().toLowerCase()))
                .map(RoleInf::getName)
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
    public static List<SecurityLabelRO> convertSecurityLabelDTOs(final Collection<SecurityLabelInf> source) {
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
    private static SecurityLabelRO convertSecurityLabelDTO(SecurityLabelInf source) {
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
//        target.setFirstName(source.getFirstName());
        target.setFullName(source.getFullName());
//        target.setLastName(source.getLastName());
        target.setLogin(source.getLogin());
        target.setPassword(source.getPassword());
        target.setRoles(convertRoles(source.getRoles()));
        target.setEnpoints(convertAPIRoToDtos(source.getEndpoints()));
        target.setSecurityLabels(convertLabels(source.getSecurityLabels()));
        target.setProperties(convertPropertiesRoToDto(source.getProperties()));
        target.setExternal(source.isExternal());
        target.setSecurityDataSource(Objects.isNull(source.getSecurityDataSource())
                ? SecurityUtils.DATASPHERE_SECURITY_DATA_SOURCE
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
    private static List<EndpointInf> convertAPIRoToDtos(List<UserAPIRO> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        List<EndpointInf> target = new ArrayList<>();
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
    private static List<RoleInf> convertRoles(List<String> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        List<RoleInf> target = new ArrayList<>();
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
    public static List<UserAPIRO> convertAPIsDtoToRo(final List<? extends EndpointInf> source) {

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
    private static UserAPIRO convertAPIDtoToRo(EndpointInf source) {
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
    private static List<SecurityLabelInf> convertLabels(List<SecurityLabelRO> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        final List<SecurityLabelInf> target = new ArrayList<>();
        source.forEach(s -> target.add(convertLabel(s)));
        return target;
    }

    /**
     * Convert label.
     *
     * @param source the source
     * @return the security label dto
     */
    private static SecurityLabelInf convertLabel(SecurityLabelRO source) {
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
    private static UserPropertyDTO convertPropertyPoToDto(UserProperty propertyPO) {
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
    public static List<UserPropertyDTO> convertPropertiesPoToDto(List<UserProperty> propertyPOs) {
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
    public static UserProperty convertPropertyDtoToPo(UserPropertyDTO propertyDTO) {
        if (propertyDTO == null) {
            return null;
        }

        UserProperty po = new UserProperty();
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
    private static UserPropertyDTO convertPropertyValuePoToDto(UserPropertyValue valuePO) {

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
    public static List<UserPropertyDTO> convertPropertyValuesPoToDto(List<UserPropertyValue> valuePOs) {

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
    private static UserPropertyValue convertPropertyValueDtoToPo(final UserPropertyDTO valueDto) {

        if (valueDto == null) {
            return null;
        }

        final UserProperty propertyPO = new UserProperty();
        propertyPO.setName(valueDto.getName());
        propertyPO.setDisplayName(valueDto.getDisplayName());
        propertyPO.setReadOnly(valueDto.isReadOnly());
        propertyPO.setRequired(valueDto.isRequired());
        propertyPO.setId(valueDto.getId());
        propertyPO.setFieldType(valueDto.getFieldType());


        final UserPropertyValue valuePO = new UserPropertyValue();
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
    public static List<UserPropertyValue> convertPropertyValuesDtoToPo(final List<UserPropertyDTO> valueDtos) {

        if (CollectionUtils.isEmpty(valueDtos)) {
            return Collections.emptyList();
        }

        return valueDtos.stream()
                .map(UsersConverter::convertPropertyValueDtoToPo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
