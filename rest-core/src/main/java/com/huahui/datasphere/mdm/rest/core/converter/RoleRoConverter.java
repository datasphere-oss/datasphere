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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;
import com.huahui.datasphere.mdm.core.dto.ResourceSpecificRightDTO;
import com.huahui.datasphere.mdm.core.dto.RightDTO;
import com.huahui.datasphere.mdm.core.dto.RoleDTO;
import com.huahui.datasphere.mdm.core.dto.RolePropertyDTO;
import com.huahui.datasphere.mdm.core.dto.SecuredResourceDTO;
import com.huahui.datasphere.mdm.core.dto.SecurityLabelAttributeDTO;
import com.huahui.datasphere.mdm.core.dto.SecurityLabelDTO;
import com.huahui.datasphere.mdm.core.type.security.CustomProperty;
import com.huahui.datasphere.mdm.core.type.security.Right;
import com.huahui.datasphere.mdm.core.type.security.Role;
import com.huahui.datasphere.mdm.core.type.security.RoleType;
import com.huahui.datasphere.mdm.core.type.security.SecuredResource;
import com.huahui.datasphere.mdm.core.type.security.SecuredResourceCategory;
import com.huahui.datasphere.mdm.core.type.security.SecuredResourceType;
import com.huahui.datasphere.mdm.core.type.security.SecurityLabel;
import com.huahui.datasphere.mdm.core.type.security.SecurityLabelAttribute;
import com.huahui.datasphere.mdm.rest.system.ro.security.RightRO;
import com.huahui.datasphere.mdm.rest.system.ro.security.RolePropertyRO;
import com.huahui.datasphere.mdm.rest.system.ro.security.RoleRO;
import com.huahui.datasphere.mdm.rest.system.ro.security.RoleTypeRO;
import com.huahui.datasphere.mdm.rest.system.ro.security.SecuredResourceCategoryRO;
import com.huahui.datasphere.mdm.rest.system.ro.security.SecuredResourceRO;
import com.huahui.datasphere.mdm.rest.system.ro.security.SecuredResourceTypeRO;
import com.huahui.datasphere.mdm.rest.system.ro.security.SecurityLabelAttributeRO;
import com.huahui.datasphere.mdm.rest.system.ro.security.SecurityLabelRO;

import com.huahui.datasphere.mdm.rest.core.ro.ResourceSpecificRightRO;

/**
 * The Class RolesConverter.
 */
public class RoleRoConverter {
    private RoleRoConverter() {
    }

    /**
     * Convert role ro.
     *
     * @param source the source
     * @return the role dtof
     */
    public static RoleDTO convertRoleRO(RoleRO source) {

        if (source == null) {
            return null;
        }

        final RoleDTO target = new RoleDTO();
        target.setName(source.getName());
        target.setDisplayName(source.getDisplayName());
        target.setRoleType(source.getType() != null ? RoleType.valueOf(source.getType().name()) : null);
        target.setRights(convertRightROs(source.getRights()));
        target.setSecurityLabels(from(source.getSecurityLabels()));
        target.setProperties(convertPropertiesValuesRoToDto(source.getProperties()));

        return target;
    }

    /**
     * Convert role r os.
     *
     * @param source the source
     * @return the list
     */
    public static List<Role> convertRoleROs(List<RoleRO> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        return source.stream()
                .map(RoleRoConverter::convertRoleRO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Convert role dto.
     *
     * @param source the source
     * @return the role ro
     */
    public static RoleRO convertRoleDTO(Role source) {

        if (source == null) {
            return null;
        }

        final RoleRO target = new RoleRO();
        if (source instanceof RoleDTO) {
            target.setCreatedAt(((RoleDTO) source).getCreatedAt());
            target.setCreatedBy(((RoleDTO) source).getCreatedBy());
            target.setUpdatedAt(((RoleDTO) source).getUpdatedAt());
            target.setUpdatedBy(((RoleDTO) source).getUpdatedBy());
        }

        target.setDisplayName(source.getDisplayName());
        target.setName(source.getName());
        target.setRights(convertRightDTOs(source.getRights()));
        target.setSecurityLabels(convertSecurityLabelDTOs(source.getSecurityLabels()));
        target.setProperties(convertPropertiesValuesDTOToRO(source.getProperties()));
        if (source.getRoleType() == null) {
            target.setType(RoleTypeRO.USER_DEFINED);
        } else {
            target.setType(RoleTypeRO.valueOf(source.getRoleType().name()));
        }

        return target;
    }

    /**
     * Convert role dt os.
     *
     * @param source the source
     * @return the list
     */
    public static List<RoleRO> convertRoleDTOs(final List<Role> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        return source.stream()
                .map(RoleRoConverter::convertRoleDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Convert rights.
     *
     * @param source the source
     * @return the list
     */
    private static List<Right> convertRightROs(List<RightRO> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        return source.stream()
                .map(RoleRoConverter::convertRightRO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Convert right ro.
     *
     * @param source the source
     * @return the right dto
     */
    private static RightDTO convertRightRO(RightRO source) {
        if (source == null) {
            return null;
        }
        RightDTO target = new RightDTO();
        target.setDelete(source.isDelete());
        target.setCreate(source.isCreate());
        target.setRead(source.isRead());
        target.setUpdate(source.isUpdate());
        target.setSecuredResource(convertSecuredResourceRO(source.getSecuredResource()));
        return target;
    }

    /**
     * Convert secured resource.
     *
     * @param source the source
     * @return the secured resource dto
     */
    private static SecuredResourceDTO convertSecuredResourceRO(SecuredResourceRO source) {

        if (source == null) {
            return null;
        }

        SecuredResourceDTO target = new SecuredResourceDTO();
        target.setName(source.getName());
        target.setDisplayName(source.getDisplayName());
        target.setType(SecuredResourceType.valueOf(source.getType().name()));
        target.setCategory(SecuredResourceCategory.valueOf(source.getCategory().name()));

        target.setCreatedAt(source.getCreatedAt());
        target.setCreatedBy(source.getCreatedBy());
        target.setUpdatedAt(source.getUpdatedAt());
        target.setUpdatedBy(source.getUpdatedBy());
        return target;
    }

    /**
     * Convert security label dt os.
     *
     * @param source the source
     * @return the list
     */
    public static List<SecurityLabelRO> convertSecurityLabelDTOs(final List<SecurityLabel> source) {
        if (source == null || source.isEmpty()) {
            return new ArrayList<>();
        }
        List<SecurityLabelRO> target = new ArrayList<>();
        source.forEach(s -> target.add(convertSecurityLabelDTO(s)));
        return target;
    }

    /**
     * Convert security label dto.
     *
     * @param source the source
     * @return the security label ro
     */
    public static SecurityLabelRO convertSecurityLabelDTO(SecurityLabel source) {
        if (source == null) {
            return null;
        }
        SecurityLabelRO target = new SecurityLabelRO();
        target.setName(source.getName());
        target.setDisplayName(source.getDisplayName());
        target.setDescription(source.getDescription());
        target.setAttributes(convertSecurityAttributeDTOs(source.getAttributes()));
        return target;
    }

    public static SecurityLabel from(SecurityLabelRO source) {

        if (Objects.isNull(source)) {
            return null;
        }

        final SecurityLabelDTO target = new SecurityLabelDTO();
        target.setName(source.getName());
        target.setAttributes(convertLabelAttributesRO(source.getAttributes()));
        target.setDescription(source.getDescription());
        target.setDisplayName(source.getDisplayName());

        return target;
    }

    public static List<SecurityLabel> from(List<SecurityLabelRO> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        final List<SecurityLabel> labels = new ArrayList<>();
        for (final SecurityLabelRO securityLabelRO : source) {
            labels.add(from(securityLabelRO));
        }

        return labels;
    }

    /**
     * Convert properties.
     *
     * @param source the source
     * @return the list
     */
    private static List<CustomProperty> convertPropertiesValuesRoToDto(List<RolePropertyRO> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        return source.stream()
                .map(RoleRoConverter::convertPropertyRoToDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    /**
     * Convert properties.
     *
     * @param source the source
     * @return the list
     */
    private static List<RolePropertyRO> convertPropertiesValuesDTOToRO(List<CustomProperty> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        return source.stream()
                .map(RoleRoConverter::convertPropertyValueDTOToRO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Convert property.
     *
     * @param source the source
     * @return the role property
     */
    private static RolePropertyRO convertPropertyValueDTOToRO(CustomProperty source) {

        if (source == null) {
            return null;
        }

        RolePropertyRO target = new RolePropertyRO();
        target.setName(source.getName());
        target.setDisplayName(source.getDisplayName());
        target.setValue(source.getValue());
        target.setId(source instanceof RolePropertyDTO ? ((RolePropertyDTO) source).getId() : -1);
        target.setRequired(source instanceof RolePropertyDTO && ((RolePropertyDTO) source).isRequired());
        return target;
    }

    /**
     * Convert security attribute dt os.
     *
     * @param source the source
     * @return the list
     */
    private static List<SecurityLabelAttributeRO> convertSecurityAttributeDTOs(List<SecurityLabelAttribute> source) {
        if (source == null || source.isEmpty()) {
            return new ArrayList<>();
        }
        List<SecurityLabelAttributeRO> target = new ArrayList<>();
        source.forEach(s -> target.add(SecurityLabelAttributeConverter.convertSecurityLabelAttributeDTO(s)));
        return target;
    }


    /**
     * Convert right dt os.
     *
     * @param source the source
     * @return the list
     */
    private static List<RightRO> convertRightDTOs(List<Right> source) {

        if (CollectionUtils.isEmpty(source)) {
            return new ArrayList<>();
        }

        List<RightRO> target = new ArrayList<>();
        source.forEach(s -> target.add(convertRightDTO(s)));
        return target;
    }

    /**
     * Convert right dto.
     *
     * @param source the source
     * @return the right ro
     */
    public static RightRO convertRightDTO(Right source) {
        if (source == null) {
            return null;
        }

        RightRO target = new RightRO();
        target.setCreate(source.isCreate());
        if (source instanceof RightDTO) {
            target.setCreatedAt(((RightDTO) source).getCreatedAt());
            target.setCreatedBy(((RightDTO) source).getCreatedBy());
            target.setUpdatedAt(((RightDTO) source).getUpdatedAt());
            target.setUpdatedBy(((RightDTO) source).getUpdatedBy());
        }
        target.setDelete(source.isDelete());
        target.setRead(source.isRead());
        target.setSecuredResource(convertSecuredResourceDTO(source.getSecuredResource()));
        target.setUpdate(source.isUpdate());
        return target;
    }


    /**
     * Convert right dto.
     *
     * @param source the source
     * @return the right ro
     */
    public static ResourceSpecificRightRO convertResourceSpecificRights(ResourceSpecificRightDTO source) {

        if (source == null) {
            return null;
        }

        ResourceSpecificRightRO target = new ResourceSpecificRightRO();
        target.setCreate(source.isCreate());
        target.setDelete(source.isDelete());
        target.setRead(source.isRead());
        target.setUpdate(source.isUpdate());
        target.setMerge(source.isMerge());
        target.setRestore(source.isRestore());

        target.setSecuredResource(convertSecuredResourceDTO(source.getSecuredResource()));

        target.setCreatedAt(source.getCreatedAt());
        target.setCreatedBy(source.getCreatedBy());
        target.setUpdatedAt(source.getUpdatedAt());
        target.setUpdatedBy(source.getUpdatedBy());

        return target;
    }

    /**
     * Convert secured resource dto.
     *
     * @param source the source
     * @return the secured resource ro
     */
    private static SecuredResourceRO convertSecuredResourceDTO(final SecuredResource source) {

        if (source == null) {
            return null;
        }

        final SecuredResourceRO target = new SecuredResourceRO();

        target.setName(source.getName());
        target.setDisplayName(source.getDisplayName());
        target.setCategory(source.getCategory() == null
                ? SecuredResourceCategoryRO.SYSTEM
                : SecuredResourceCategoryRO.valueOf(source.getCategory().name()));
        target.setType(source.getType() == null
                ? SecuredResourceTypeRO.SYSTEM
                : SecuredResourceTypeRO.valueOf(source.getType().name()));

        if (source instanceof SecuredResourceDTO) {

            SecuredResourceDTO srDTO = (SecuredResourceDTO) source;

            target.setCreatedAt(srDTO.getCreatedAt());
            target.setCreatedBy(srDTO.getCreatedBy());
            target.setUpdatedAt(srDTO.getUpdatedAt());
            target.setUpdatedBy(srDTO.getUpdatedBy());

            List<SecuredResourceRO> children = new ArrayList<>();
            srDTO.getChildren().forEach(child -> {
                SecuredResourceRO childRO = convertSecuredResourceDTO(child);
                if (childRO != null) {
                    children.add(childRO);
                }
            });

            target.setChildren(CollectionUtils.isEmpty(children) ? null : children);
        }

        return target;
    }

    /**
     * Convert secured resource dt os.
     *
     * @param source the source
     * @return the list
     */
    public static List<SecuredResourceRO> convertSecuredResourceDTOs(List<SecuredResourceDTO> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        List<SecuredResourceRO> target = new ArrayList<>();
        source.forEach(s -> target.add(convertSecuredResourceDTO(s)));
        return target;
    }

    /**
     * Convert security label ro.
     *
     * @param source the source
     * @return the security label dto
     */
    public static SecurityLabel convertSecurityLabelRO(SecurityLabelRO source) {
        if (source == null) {
            return null;
        }
        SecurityLabelDTO target = new SecurityLabelDTO();
        target.setDescription(source.getDescription());
        target.setDisplayName(source.getDisplayName());
        target.setName(source.getName());
        target.setAttributes(convertLabelAttributesRO(source.getAttributes()));
        return target;
    }

    /**
     * Convert label attributes ro.
     *
     * @param source the source
     * @return the list
     */
    private static List<SecurityLabelAttribute> convertLabelAttributesRO(List<SecurityLabelAttributeRO> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        List<SecurityLabelAttribute> target = new ArrayList<>();
        source.forEach(s -> target.add(convertAttributeRO(s)));
        return target;
    }

    /**
     * Convert attribute ro.
     *
     * @param source the source
     * @return the security label attribute dto
     */
    private static SecurityLabelAttributeDTO convertAttributeRO(SecurityLabelAttributeRO source) {
        if (source == null) {
            return null;
        }
        SecurityLabelAttributeDTO target = new SecurityLabelAttributeDTO();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setPath(source.getPath());
        target.setDescription(source.getDescription());
        target.setValue(source.getValue());
        return target;
    }

    /**
     * Convert properties.
     *
     * @param source the source
     * @return the list
     */
    public static List<RolePropertyRO> convertPropertiesDtoToRo(final List<RolePropertyDTO> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        final List<RolePropertyRO> target = new ArrayList<>();
        source.forEach(s -> target.add(convertPropertyDtoToRo(s)));
        return target;
    }

    /**
     * Convert property.
     *
     * @param source the source
     * @return the role property
     */
    public static RolePropertyRO convertPropertyDtoToRo(final RolePropertyDTO source) {
        if (source == null) {
            return null;
        }
        RolePropertyRO target = new RolePropertyRO();
        target.setName(source.getName());
        target.setDisplayName(source.getDisplayName());
        target.setValue(source.getValue());
        target.setRequired(source.isRequired());
        target.setFieldType(source.getFieldType());
        target.setRequired(source.isRequired());
        target.setReadOnly(source.isReadOnly());
        target.setId(source.getId());
        return target;
    }

    /**
     * Convert properties.
     *
     * @param source the source
     * @return the list
     */
    public static List<RolePropertyDTO> convertPropertiesRoToDto(List<RolePropertyRO> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        return source.stream()
                .map(RoleRoConverter::convertPropertyRoToDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Convert property.
     *
     * @param source the source
     * @return the role property
     */
    public static RolePropertyDTO convertPropertyRoToDto(RolePropertyRO source) {

        if (source == null) {
            return null;
        }

        RolePropertyDTO target = new RolePropertyDTO();
        target.setName(source.getName());
        target.setDisplayName(source.getDisplayName());
        target.setValue(source.getValue());
        target.setFieldType(source.getFieldType());
        target.setRequired(source.isRequired());
        target.setReadOnly(source.isReadOnly());
        target.setId(source.getId());
        return target;
    }

}
