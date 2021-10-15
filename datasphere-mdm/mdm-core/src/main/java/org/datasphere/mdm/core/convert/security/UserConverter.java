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
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.huahui.datasphere.mdm.core.dto.PasswordDTO;
import com.huahui.datasphere.mdm.core.dto.UserDTO;
import com.huahui.datasphere.mdm.core.dto.UserEndpointDTO;
import com.huahui.datasphere.mdm.core.dto.UserWithPasswordDTO;
import com.huahui.datasphere.mdm.core.po.security.ApiPO;
import com.huahui.datasphere.mdm.core.po.security.PasswordPO;
import com.huahui.datasphere.mdm.core.po.security.RolePO;
import com.huahui.datasphere.mdm.core.po.security.UserPO;
import com.huahui.datasphere.mdm.core.type.security.Endpoint;
import com.huahui.datasphere.mdm.core.type.security.Role;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;


/**
 * The Class UserConverter.
 *
 * @author ilya.bykov
 */
public final class UserConverter {

    /**
     * No instances.
     */
    private UserConverter() {
        super();
    }

    /**
     * Convert po.
     *
     * @param source
     *            the source
     * @return the user with password dto
     */
    public static UserWithPasswordDTO convertPO(UserPO source) {

        if (source == null) {
            return null;
        }

        final UserWithPasswordDTO target = new UserWithPasswordDTO();
        target.setActive(source.isActive());
        target.setAdmin(source.isAdmin());
        if (source.getCreatedAt() != null) {
            target.setCreatedAt(source.getCreatedAt());
        }
        if (source.getUpdatedAt() != null) {
            target.setUpdatedAt(source.getUpdatedAt());
        }
        target.setEmail(source.getEmail());
        if (source.getLocale() != null) {
            target.setLocale(new Locale(source.getLocale()));
        }
        target.setFirstName(source.getFirstName());
        target.setFullName(String.join(" ", source.getFirstName(), source.getLastName()));
        target.setLastName(source.getLastName());
        target.setLogin(source.getLogin());
        target.setUpdatedBy(source.getUpdatedBy());
        target.setCreatedBy(source.getCreatedBy());
        target.setSecurityLabels(SecurityUtils.convertSecurityLabels(source.getLabelAttributeValues()));
        target.setRoles(convertRoles(source.getRoles()));

        target.setExternal(source.isExternal());
        target.setSecurityDataSource(source.getSource());
        target.setEnpoints(convertAPIs(source.getApis()));
        target.setEmailNotification(source.isEmailNotification());

        if (source.getPassword() != null) {
            final Optional<PasswordPO> currentPassword = source.getPassword().stream().filter(PasswordPO::getActive)
                    .findFirst();
            if (currentPassword.isPresent()) {
                final PasswordPO pwd = currentPassword.get();
                target.setPassword(pwd.getPasswordText());
                if (pwd.getUpdatedAt() != null) {
                    target.setPasswordLastChangedAt(pwd.getUpdatedAt());
                    target.setPasswordUpdatedBy(pwd.getCreatedBy());
                } else {
                    target.setPasswordLastChangedAt(pwd.getCreatedAt());
                    target.setPasswordUpdatedBy(pwd.getCreatedBy());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(source.getProperties())) {
            target.setProperties(UserPropertyConverter.convertValuePOs(source.getProperties()));
        }
        return target;
    }

    /**
     * Convert AP is.
     *
     * @param source the source
     * @return the list
     */
    public static List<Endpoint> convertAPIs(List<ApiPO> source) {
        if (source == null) {
            return null;
        }
        List<Endpoint> target = new ArrayList<>();
        source.stream().forEach(a -> {
            target.add(convertAPI(a));
        });
        return target;
    }

    /**
     * Convert API.
     *
     * @param source the source
     * @return the user APIDTO
     */
    public static Endpoint convertAPI(ApiPO source) {
        if (source == null) {
            return null;
        }
        UserEndpointDTO target = new UserEndpointDTO();
        target.setCreatedAt(source.getCreatedAt());
        target.setCreatedBy(source.getCreatedBy());
        target.setDescription(source.getDescription());
        target.setDisplayName(source.getDisplayName());
        target.setName(source.getName());
        target.setUpdatedAt(source.getUpdatedAt());
        target.setUpdatedBy(source.getUpdatedBy());
        return target;
    }

    /**
     * Convert p os.
     *
     * @param source
     *            the source
     * @return the list
     */
    public static List<UserDTO> convertPOs(List<UserPO> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        List<UserDTO> target = new ArrayList<>();
        for (UserPO userPO : source) {
            target.add(convertPO(userPO));
        }

        return target;
    }

    /**
     * Convert roles po.
     *
     * @param source
     *            the source
     * @return the list
     */
    private static List<Role> convertRoles(List<RolePO> source) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        List<Role> target = new ArrayList<>();
        for (RolePO po : source) {
            target.add(RoleConverter.convertRole(po));
        }

        return target;
    }

    public static List<PasswordDTO> covertPasswords(final List<PasswordPO> passwords) {
        return passwords.stream()
                .map(p -> new PasswordDTO(
                        UserConverter.convertPO(p.getUser()),
                        p.getPasswordText(),
                        p.getActive(),
                        p.getCreatedAt() != null ? p.getCreatedAt().toLocalDateTime() : null
                ))
                .collect(Collectors.toList());
    }
}
