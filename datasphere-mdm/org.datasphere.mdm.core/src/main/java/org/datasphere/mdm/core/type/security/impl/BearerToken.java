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

package org.datasphere.mdm.core.type.security.impl;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.datasphere.mdm.core.type.security.AuthenticationToken;
import org.datasphere.mdm.core.type.security.SecurityToken;
import org.datasphere.mdm.core.type.security.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * The Class BearerToken.
 */
public class BearerToken extends AbstractAuthenticationToken implements AuthenticationToken {
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -8611526426415399927L;
    /**
     * Prolong token ttl or not.
     */
    private final boolean prolongTTL;
    /**
     * Token string (Ist phase).
     */
    private final String token;
    /**
	 * Instantiates a new bearer token.
	 *
	 * @param token
	 *            the token
	 * @param prolongTTL
	 *            is prolong token ttl
	 */
    public BearerToken(String token, boolean prolongTTL) {
        super(null);
        this.token = token;
        this.prolongTTL = prolongTTL;
        setAuthenticated(false);
    }

    /**
     * Instantiates a new bearer token for authenticated state with additional details.
     *
     * @param tokenObject
     *            the token session object
     * @param authorities
     *            the authorities
     */
    public BearerToken(SecurityToken tokenObject, List<GrantedAuthority> authorities) {
        super(authorities);
        super.setDetails(tokenObject);
        this.token = null;
        this.prolongTTL = false;
        setAuthenticated(true);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.core.Authentication#getCredentials()
     */
    @Override
    public Object getCredentials() {

        if (Objects.nonNull(token)) {
            return token;
        }

        SecurityToken thisToken = getSessionToken();
        if (Objects.isNull(thisToken)) {
            return null;
        }

        return thisToken.getToken();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.core.Authentication#getPrincipal()
     */
    @Override
    public Object getPrincipal() {
        return this.getCredentials();
    }
    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.security.authentication.AbstractAuthenticationToken
     * #getName()
     */
    @Override
    public String getName() {
        User user = getUserDetails();
        return user == null ? null : user.getLogin();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserName() {
        return getName();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentToken() {
        Object credentials = getCredentials();
        return credentials == null ? null : credentials.toString();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<SecurityParam, Object> getSecurityParams() {

        SecurityToken thisToken = getSessionToken();
        if (Objects.isNull(thisToken)) {
            return Collections.emptyMap();
        }

        Map<SecurityParam, Object> params = new EnumMap<>(SecurityParam.class);
        params.put(AuthenticationToken.SecurityParam.ROLES_MAP, thisToken.getRolesMap());
        params.put(AuthenticationToken.SecurityParam.RIGHTS_MAP, thisToken.getRightsMap());
        params.put(AuthenticationToken.SecurityParam.LABELS_MAP, thisToken.getLabelsMap());
        return params;
    }
	/**
	 * Checks if is prolong ttl.
	 *
	 * @return true, if is prolong ttl
	 */
	public boolean prolongTTL() {
		return prolongTTL;
	}
    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserDetails() {

        SecurityToken thisToken = getSessionToken();
        if (Objects.isNull(thisToken)) {
            return null;
        }

        return thisToken.getUser();
    }

    /**
     * Extracts (casts) details to {@link SecurityToken} session token.
     * @return instance or null
     */
    private SecurityToken getSessionToken() {

        if (SecurityToken.class.isInstance(super.getDetails())) {
            return (SecurityToken) super.getDetails();
        }

        return null;
    }
}
