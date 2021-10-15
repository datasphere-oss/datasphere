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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.dto.UserWithPasswordDTO;
import org.datasphere.mdm.core.po.security.ApiPO;
import org.datasphere.mdm.core.po.security.PasswordPO;
import org.datasphere.mdm.core.po.security.RolePO;
import org.datasphere.mdm.core.po.security.UserPO;
import org.datasphere.mdm.core.po.security.UserPropertyPO;
import org.datasphere.mdm.core.po.security.UserPropertyValuePO;
import org.datasphere.mdm.core.type.security.Endpoint;
import org.datasphere.mdm.core.util.SecurityUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;

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
