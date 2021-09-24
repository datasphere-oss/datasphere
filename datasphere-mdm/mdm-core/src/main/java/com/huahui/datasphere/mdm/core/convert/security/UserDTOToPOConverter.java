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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.huahui.datasphere.mdm.core.dto.UserWithPasswordDTO;
import com.huahui.datasphere.mdm.core.po.security.ApiPO;
import com.huahui.datasphere.mdm.core.po.security.PasswordPO;
import com.huahui.datasphere.mdm.core.po.security.RolePO;
import com.huahui.datasphere.mdm.core.po.security.UserPO;
import com.huahui.datasphere.mdm.core.po.security.UserPropertyPO;
import com.huahui.datasphere.mdm.core.po.security.UserPropertyValuePO;
import com.huahui.datasphere.mdm.core.type.security.Endpoint;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;

/**
 * The Class UserDTOToPOConverter.
 * @author ilya.bykov
 */
public class UserDTOToPOConverter {

    private UserDTOToPOConverter() {
        super();
    }

    /**
     * Convert.
     *
     * @param source the source
     * @param target the target
     */
    public static void convert(UserWithPasswordDTO source, UserPO target) {
        if (source == null || target == null) {
            return;
        }
        target.setActive(source.isActive());
        target.setAdmin(source.isAdmin());
        if (source.getCreatedAt() != null) {
            target.setCreatedAt(new Timestamp(source.getCreatedAt().getTime()));
        }
        if (!StringUtils.isEmpty(source.getCreatedBy())) {
            target.setCreatedBy(source.getCreatedBy());
        }
        target.setEmail(source.getEmail());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        // Populate user first and last name, if is need.
        if(StringUtils.isEmpty(target.getFirstName())
                && StringUtils.isEmpty(target.getLastName())
                && StringUtils.isNotEmpty(source.getFullName())){
            target.setFirstName(StringUtils.substringBefore(source.getFullName(), " "));
            target.setLastName(StringUtils.substringAfter(source.getFullName(), " "));
        }
        target.setLogin(source.getLogin());
        target.setNotes(null);
        target.setExternal(source.isExternal());
        target.setSource(source.getSecurityDataSource());
        target.setLocale(source.getLocale() != null ? source.getLocale().getLanguage() : null);
        target.setApis(convertAPIs(source.getEndpoints()));
        target.setEmailNotification(source.isEmailNotification());

        if (source.getUpdatedAt() != null) {
            target.setUpdatedAt(new Timestamp(source.getUpdatedAt().getTime()));
        }
        if (!StringUtils.isEmpty(source.getUpdatedBy())) {
            target.setUpdatedBy(source.getUpdatedBy());
        }
        if (CollectionUtils.isNotEmpty(source.getCustomProperties())) {
            target.setProperties(source.getCustomProperties().stream()
                    .map(customProperty -> {
                        final UserPropertyValuePO userPropertyValuePO = new UserPropertyValuePO();
                        userPropertyValuePO.setValue(customProperty.getValue());
                        final UserPropertyPO property = new UserPropertyPO();
                        property.setName(customProperty.getName());
                        userPropertyValuePO.setProperty(property);
                        return userPropertyValuePO;
                    }).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(source.getRoles())) {
            target.setRoles(source.getRoles().stream()
                    .map(r -> new RolePO(r.getName()))
                    .collect(Collectors.toList()));
        }
        // password changed?
		if (!StringUtils.isEmpty(source.getPassword())) {
			target.getPassword().forEach(p->p.setActive(false));
			PasswordPO password =convertPassword(source.getPassword());
			password.setUser(target);
			target.getPassword().add(password);
		}
    }

    public static List<UserPO> convert(final List<UserWithPasswordDTO> users) {
        return users.stream().map(u -> {
            final UserPO user = new UserPO();
            convert(u, user);
            return user;
        }).collect(Collectors.toList());
    }


    /**
     * Convert AP is.
     *
     * @param source the source
     * @return the list
     */
    private static List<ApiPO> convertAPIs(List<Endpoint> source) {

        if(source == null){
			return Collections.emptyList();
		}

		List<ApiPO> target = new ArrayList<>();
		source.stream().forEach(a-> target.add(convertAPI(a)));
		return target;
	}


	/**
	 * Convert API.
	 *
	 * @param source the source
	 * @return the api PO
	 */
	private static ApiPO convertAPI(Endpoint source) {
		if (source == null) {
			return null;
		}
		ApiPO target = new ApiPO();
		target.setName(source.getName());
		target.setDisplayName(source.getDisplayName());
		target.setDescription(source.getDescription());
		return target;
	}


	/**
	 * Convert password.
	 *
	 * @param password
	 *            the password
	 * @return the password po
	 */
    private static PasswordPO convertPassword(String password) {
        PasswordPO target = new PasswordPO();
        target.setPasswordText(BCrypt.hashpw(password, BCrypt.gensalt()));
        target.setActive(true);
        target.setCreatedAt(new Timestamp(new Date().getTime()));
        target.setCreatedBy(SecurityUtils.getCurrentUserName());
        target.setUpdatedAt(new Timestamp(new Date().getTime()));
        target.setUpdatedBy(SecurityUtils.getCurrentUserName());
        return target;
    }

}
