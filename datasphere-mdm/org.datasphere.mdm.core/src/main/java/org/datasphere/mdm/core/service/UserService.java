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

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.datasphere.mdm.core.context.UpsertUserEventRequestContext;
import org.datasphere.mdm.core.dto.PasswordDTO;
import org.datasphere.mdm.core.dto.UserDTO;
import org.datasphere.mdm.core.dto.UserEventDTO;
import org.datasphere.mdm.core.dto.UserPropertyDTO;
import org.datasphere.mdm.core.dto.UserWithPasswordDTO;
import org.datasphere.mdm.core.type.security.Endpoint;
import org.datasphere.mdm.core.type.security.SecurityToken;
import org.datasphere.mdm.core.type.security.User;

import com.datasphere.mdm.security.UserPasswordDef;

public interface UserService {
    /**
     * Creates the new user.
     *
     * @param user
     *            the user
     */
    void create(UserWithPasswordDTO user);

    /**
     * Update user.
     *
     * @param login
     *            the login
     * @param user
     *            the user
     */
    void updateUser(String login, UserWithPasswordDTO user);

    /**
     * Update user.
     *
     * @param login
     *            the login
     * @param user
     *            the user
     * @param logout logout after update
     */
    void updateUser(String login, UserWithPasswordDTO user, boolean logout);

    /**
     * Update user locale
     * @param login user login
     * @param newLocale new locale
     */
    void updateUserLocale(String login, Locale newLocale);


    /**
     * Gets the user by name.
     *
     * @param login
     *            the login
     * @return the user by name
     */
    UserWithPasswordDTO getUserByName(String login);
    /**
     * Gets the all users.
     *
     * @return the all users
     */
    List<UserDTO> getAllUsers();

    /**
     * Deactivate user.
     *
     * @param login
     *            the login
     */
    void deactivateUser(String login);

    /**
     * Gets the all properties.
     *
     * @return the all properties
     */
    List<UserPropertyDTO> getAllProperties();

    /**
     *
     * @param property
     */
    void saveProperty(UserPropertyDTO property);

    /**
     *
     * @param id
     */
    void deleteProperty(long id);

    /**
     *
     * @param userId
     * @return
     */
    List<UserPropertyDTO> loadUserPropertyValues(int userId);

    /**
     *
     * @param userId
     * @param userProperties
     */
    void saveUserPropertyValues(long userId, List<UserPropertyDTO> userProperties);

    /**
     * Insert token.
     *
     * @param token
     *            the token
     */
    void insertToken(SecurityToken token);

    /**
     * Gets user events.
     * @return
     */
    List<UserEventDTO> getUserEvents(String login, Date from, int page, int count);

    /**
     * Count user events.
     * @param login user login
     * @return count
     */
    Long countUserEvents(String login);

    /**
     * Deletes an event by id.
     * @param eventId the event id
     * @return true, if successfl, false otherwise
     */
    boolean deleteUserEvent(String eventId);

    /**
     * Deletes selected events by ids.
     * @param eventIds the event ids
     * @return true, if successful, false otherwise
     */
    boolean deleteUserEvents(List<String> eventIds);

    /**
     * Deletes all events of a user.
     * @param point the point in time
     * @return true, if successful, false otherwise
     */
    boolean deleteAllEventsForCurrentUser(Date point);

    /**
     * Saves an event.
     * @param ueCtx the save context
     * @return DTO
     */
    UserEventDTO upsert(UpsertUserEventRequestContext ueCtx);
    /**
     * Verifies and creates external user for full external authentication.
     * @param user the user
     */
    void verifyAndUpserExternalUser(User user);
    /**
     * List of available APIs(eg SOAP, REST)
     * @return List of available APIs(eg SOAP, REST)
     */
    List<Endpoint> getAPIList();

    /**
     * Check whether the user with name is an administrator.
     * @param login user login
     * @return if login null or empty return false, else return value for user
     */
    boolean isAdminUser(String login);

    /**
     * Load all users with full info
     * @return List of users
     */
    List<UserDTO> loadAllUsers();

    List<PasswordDTO> loadUserPasswords();

    void removeUsersByLogin(List<String> collect);

    void saveUsers(final List<UserWithPasswordDTO> users);

    void addUsersPasswords(Map<String, List<UserPasswordDef>> usersPasswords);
    /**
     * update password
     * @param login
     * @param activationCode
     * @param newPassword
     */
    void updatePassword(String login, String activationCode, String newPassword, boolean temp);

    void addUsersRoles(Map<String, Set<String>> userRoleNames);
    /**
     * Generate new temporary password for user.
     * Send email notification.
     * If email not defined exception will be thrown.
     * @param login login.
     * @param email user email.
     */
	void forgotPassword(String login, String email);

	void activatePassword(String activationCode);
}