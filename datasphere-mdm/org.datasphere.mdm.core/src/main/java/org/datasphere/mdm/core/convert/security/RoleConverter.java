/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 * 
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.convert.security;

import static org.datasphere.mdm.core.util.SecurityUtils.convertSecurityLabels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.dto.RoleDTO;
import org.datasphere.mdm.core.dto.SecurityLabelAttributeDTO;
import org.datasphere.mdm.core.dto.SecurityLabelDTO;
import org.datasphere.mdm.core.po.security.LabelAttributePO;
import org.datasphere.mdm.core.po.security.LabelAttributeValuePO;
import org.datasphere.mdm.core.po.security.LabelPO;
import org.datasphere.mdm.core.po.security.RolePO;
import org.datasphere.mdm.core.type.security.RoleType;
import org.datasphere.mdm.core.type.security.SecurityLabel;
import org.datasphere.mdm.core.type.security.SecurityLabelAttribute;

/**
 * @author mikhail
 * Roles PO* objects converter.
 */
public final class RoleConverter {


    /**
     * Disable instances.
     */
    private RoleConverter() {
        super();
    }

    /**
     * Convert role po.
     *
     * @param source
     *            the source
     * @return the role dto
     */
    public static RoleDTO convertRole(RolePO source) {

        if (source == null) {
            return null;
        }

        final RoleDTO target = new RoleDTO();
        target.setName(source.getName());
        target.setDisplayName(source.getDisplayName());
        if (source.getRType() != null) {
            target.setRoleType(RoleType.valueOf(source.getRType()));
        }
        target.setRights(RightConverter.convertRightsPoToDto(source.getConnectedResourceRights()));
        final List<SecurityLabel> securityLabels = convertSecurityLabels(source.getLabelAttributeValues());
        target.setSecurityLabels(addSecurityLabelsWithoutValues(securityLabels, source.getLabelPOs()));
        target.setProperties(RolePropertyConverter.convertPropertyValues(source.getProperties()));
        return target;
    }


    private static List<SecurityLabel> addSecurityLabelsWithoutValues(List<SecurityLabel> securityLabels, List<LabelPO> labelPOs) {
        if (CollectionUtils.isEmpty(labelPOs)) {
            return securityLabels;
        }
        final List<SecurityLabel> result = new ArrayList<>(securityLabels);
        final Set<String> existLabelNames = securityLabels.stream()
                .map(SecurityLabel::getName)
                .collect(Collectors.toSet());
        result.addAll(
                labelPOs.stream()
                        .filter(l -> !existLabelNames.contains(l.getName()))
                        .map(RoleConverter::convertLabel)
                        .collect(Collectors.toList())
        );
        return result;
    }

    /**
     * Convert labels.
     *
     * @param source
     *            the source
     * @return the list
     */
    public static List<SecurityLabel> convertLabels(final List<LabelPO> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        final List<SecurityLabel> target = new ArrayList<>();
        source.forEach(s -> target.add(convertLabel(s)));
        return target;
    }

    /**
     * Convert label.
     *
     * @param source
     *            the source
     * @return the security label dto
     */
    public static SecurityLabel convertLabel(LabelPO source) {

        if (source == null) {
            return null;
        }

        SecurityLabelDTO target = new SecurityLabelDTO();
        target.setName(source.getName());
        target.setDisplayName(source.getDisplayName());
        target.setDescription(source.getDescription());
        target.setAttributes(convertAttributes(source.getLabelAttribute()));
        return target;
    }

    /**
     * Convert attributes.
     *
     * @param source
     *            the source
     * @return the list
     */
    public static List<SecurityLabelAttribute> convertAttributes(List<LabelAttributePO> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        List<SecurityLabelAttribute> target = new ArrayList<>();
        source.forEach(s -> {
                    if (CollectionUtils.isEmpty(s.getLabelAttributeValues())) {
                        target.add(convertAttribute(s));
                    } else {
                        target.addAll(convertAttributeValues(s.getLabelAttributeValues()));
                    }
                }
        );
        return target;
    }

    private static Collection<SecurityLabelAttribute> convertAttributeValues(Collection<LabelAttributeValuePO> labelAttributeValues) {
        return labelAttributeValues.stream()
                .map(lav ->
                        new SecurityLabelAttributeDTO(
                                lav.getId(),
                                lav.getLabelAttribute().getName(),
                                lav.getLabelAttribute().getPath(),
                                lav.getValue(),
                                lav.getLabelAttribute().getDescription()
                        )
                )
                .collect(Collectors.toList());
    }

    /**
     * Convert attribute.
     *
     * @param source
     *            the source
     * @return the security label attribute dto
     */
    public static SecurityLabelAttributeDTO convertAttribute(LabelAttributePO source) {

        if (source == null) {
            return null;
        }

        SecurityLabelAttributeDTO target = new SecurityLabelAttributeDTO();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setPath(source.getPath());
        target.setValue(null);
        target.setDescription(source.getDescription());
        return target;
    }


    public static List<RoleDTO> convertRoles(Collection<RolePO> roles) {
        return roles.stream().map(RoleConverter::convertRole).collect(Collectors.toList());
    }





}
