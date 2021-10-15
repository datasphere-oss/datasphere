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

package org.datasphere.mdm.core.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import org.datasphere.mdm.core.type.security.AuthenticationSystemParameter;
import org.datasphere.mdm.core.type.security.Right;
import org.datasphere.mdm.core.type.security.SecurityDataSource;
import org.datasphere.mdm.core.type.security.SecurityToken;
import org.datasphere.mdm.core.type.security.User;

/**
 * @author Mikhail Mikhailov
 * Security stuff, visible everywhere.
 */
public interface SecurityService {
    /**
     * Registers a security data source
     * @param sds the data source
     */
    void register(SecurityDataSource sds);
    /**
     * Gets all registered data sources in REVERSE order of registration (in LIFO fashion).
     * @return list of data sources
     */
    List<SecurityDataSource> getSecurityDataSources();
    /**
     * Gets a security datasource by its name.
     * @param name the datasource name
     * @return security datasource or null, if not found
     */
    @Nullable
    SecurityDataSource getSecurityDataSourceByName(String name);
    /**
     * Check login and password and if they are correct generate token and return it back.
     * TODO refactor this method to accept standard AuthenticationRequestContext.
     *
     * @param params login parameters
     *
     * @return the security token.
     */
    SecurityToken login(Map<AuthenticationSystemParameter, Object> params);
    /**
     * Logout.
     * TODO refactor this method to accept standard AuthenticationRequestContext.
     *
     * @param tokenString the token string
     * @param params addition params
     * @return true, if successful
     */
    boolean logout(String tokenString, Map<AuthenticationSystemParameter, Object> params);
    /**
     * Verifies token and sets authentication context up, if successful.
     * @param token the token string
     * @param prolongTTL whether to prolong token TTL or not
     * @return true if successful, false otherwise
     */
    boolean authenticate(String token, boolean prolongTTL);
    /**
     * Gets the token ttl.
     *
     * @return the tokenTTL
     */
    long getTokenTTL();
    /**
     * Sets the password.
     *
     * @param password    the password
     * @param oldPassword the old password
     */
    void updatePassword(String password, String oldPassword);

    /**
     * Validate token. This is called by Spring via authentication provider.
     * The chain starts  at {@link #authenticate(String, boolean)}.
     *
     * @param tokenString the token string
     * @param prolongTTL prolong token TTL or not.
     * @return true, if successful
     */
    boolean validateAndProlongToken(String tokenString, boolean prolongTTL);
    /**
     * Gets the user by token.
     *
     * @param tokenString the token string
     * @return the user by token
     */
    User getUserByToken(String tokenString);
    /**
     * Gets security token by token string.
     * @param tokenString the string
     * @return token object or null
     */
    SecurityToken getTokenObjectByToken(String tokenString);
    /**
     * Gets the roles by token.
     *
     * @param tokenString the token string
     * @return the roles by token
     */
    List<Right> getRightsByToken(String tokenString);
    /**
     * Logout user by name.
     *
     * @param userName the user name
     */
    void logoutUserByName(String userName);
    /**
     * Logout all currently logged in users that attached to the provided role.
     *
     * @param roleName Role name.
     */
    void logoutByRoleName(String roleName);
    /**
     * Gets current user's name.
     * @return name
     */
    String getCurrentUserName();
    /**
     * Gets current user token.
     * @return token
     */
    String getCurrentUserToken();
    /**
     * Gets current user's storage id.
     * @return storage id
     */
    String getCurrentUserStorageId();
    /**
     * Check whether the user with name is an administrator.
     */
    boolean isAdminUser(String login);
    /**
     *
     * @param login
     * @return
     */
    String getOrCreateInnerTokenByLogin(String login);
    /**
     * Change user locale for a token
     * @param login user login
     * @param locale new locale
     */
    void changeLocale(String login, Locale locale);
    /**
     * Adds role names to all active real user tokens.
     * @param roleNames the role names
     */
    void updateUserTokensWithRole(List<String> roleNames);
    /**
     * update inner token by login
     * @param login
     */
    void updateInnerToken(String login);
    /**
     * update inner token by login
     * @param roleName
     */
    void updateInnerTokensWithRole(String roleName);
}
