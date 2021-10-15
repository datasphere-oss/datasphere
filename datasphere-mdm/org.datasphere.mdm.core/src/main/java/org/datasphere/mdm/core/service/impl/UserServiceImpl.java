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

package org.datasphere.mdm.core.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.context.UpsertUserEventRequestContext;
import org.datasphere.mdm.core.convert.security.UserConverter;
import org.datasphere.mdm.core.convert.security.UserDTOToPOConverter;
import org.datasphere.mdm.core.convert.security.UserPropertyConverter;
import org.datasphere.mdm.core.dao.RoleDao;
import org.datasphere.mdm.core.dao.UserDao;
import org.datasphere.mdm.core.dto.PasswordDTO;
import org.datasphere.mdm.core.dto.SecurityLabelAttributeDTO;
import org.datasphere.mdm.core.dto.SecurityLabelDTO;
import org.datasphere.mdm.core.dto.UserDTO;
import org.datasphere.mdm.core.dto.UserEventDTO;
import org.datasphere.mdm.core.dto.UserPropertyDTO;
import org.datasphere.mdm.core.dto.UserWithPasswordDTO;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.po.security.ApiPO;
import org.datasphere.mdm.core.po.security.LabelAttributePO;
import org.datasphere.mdm.core.po.security.PasswordPO;
import org.datasphere.mdm.core.po.security.RolePO;
import org.datasphere.mdm.core.po.security.TokenPO;
import org.datasphere.mdm.core.po.security.UserEventPO;
import org.datasphere.mdm.core.po.security.UserPO;
import org.datasphere.mdm.core.po.security.UserPropertyPO;
import org.datasphere.mdm.core.po.security.UserPropertyValuePO;
import org.datasphere.mdm.core.service.PasswordPolicyService;
import org.datasphere.mdm.core.service.SecurityService;
import org.datasphere.mdm.core.service.UserService;
import org.datasphere.mdm.core.type.security.CustomProperty;
import org.datasphere.mdm.core.type.security.Endpoint;
import org.datasphere.mdm.core.type.security.Right;
import org.datasphere.mdm.core.type.security.Role;
import org.datasphere.mdm.core.type.security.SecurityConstants;
import org.datasphere.mdm.core.type.security.SecurityLabel;
import org.datasphere.mdm.core.type.security.SecurityLabelAttribute;
import org.datasphere.mdm.core.type.security.SecurityToken;
import org.datasphere.mdm.core.type.security.User;
import org.datasphere.mdm.core.util.CryptUtils;
import org.datasphere.mdm.core.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.datasphere.mdm.system.exception.PlatformBusinessException;
import org.datasphere.mdm.system.exception.PlatformValidationException;
import org.datasphere.mdm.system.exception.ValidationResult;
import org.datasphere.mdm.system.type.runtime.MeasurementPoint;
import org.datasphere.mdm.system.util.IdUtils;

import com.datasphere.mdm.security.UserPasswordDef;

/**
 * The Class UserService.
 */
@Component
public class UserServiceImpl implements UserService {
    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    /**
     * Parameter name length limit.
     */
    private static final int PARAM_NAME_LIMIT = 2044;
    /**
     * Ordinary field length limit.
     */
    private static final int USER_FIELD_LIMIT = 255;
    /**
     * Parameter display name length limit.
     */
    private static final int PARAM_DISPLAY_NAME_LIMIT = 2044;
    /**
     * Validation tags.
     */
    private static final String VIOLATION_NAME_PROPERTY_EMPTY = "app.user.property.validationError.name.property.empty";
    private static final String VIOLATION_REQUIRED_PROPERTY_VALUE = "app.user.property.validationError.value.not.set";
    private static final String VIOLATION_NAME_PROPERTY_LENGTH = "app.user.property.validationError.name.property.length";
    private static final String VIOLATION_DISPLAY_NAME_PROPERTY_EMPTY = "app.user.property.validationError.displayName.property.empty";
    private static final String VIOLATION_DISPLAY_NAME_PROPERTY_LENGTH = "app.user.property.validationError.displayName.property.length";
    private static final String VIOLATION_NAME_PROPERTY_NOT_UNIQUE = "app.user.property.validationError.name.not.unique";
    private static final String VIOLATION_DISPLAY_NAME_PROPERTY_NOT_UNIQUE = "app.user.property.validationError.displayName.not.unique";
    private static final String VIOLATION_USER_NAME_EMPTY = "app.user.data.validationError.userName.empty";
    private static final String VIOLATION_USER_NAME_LENGTH = "app.user.data.validationError.userName.length";
    private static final String VIOLATION_PASSWORD_EMPTY = "app.user.data.validationError.password.empty";
    private static final String VIOLATION_PASSWORD_LENGTH = "app.user.data.validationError.password.length";
    private static final String VIOLATION_EMAIL_LENGTH = "app.user.data.validationError.email.length";
    private static final String VIOLATION_FIRSTNAME_LENGTH = "app.user.data.validationError.firstname.length";
    private static final String VIOLATION_LASTNAME_LENGTH = "app.user.data.validationError.lastname.length";

    /** The user dao. */
    @Autowired
    private UserDao userDAO;

    /** The role dao. */
    @Autowired
    private RoleDao roleDAO;
    /** The security service. */
    @Autowired
    private SecurityService securityService;
    /** The password policy. */
    @Autowired
    private PasswordPolicyService passwordPolicy;
    /*
    @Autowired
    private AuditEventsWriter auditEventsWriter;

    @Autowired
    private UserNotificationService userNotificationService;
    */
    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.service.security.IUserService#create(com.unidata.
     * mdm.backend.service.security.dto.UserWithPasswordDTO)
     */
    @Override
    @Transactional
    public void create(final UserWithPasswordDTO user) {
        try {
            if (!user.isExternal()) {
                user.setSecurityDataSource(SecurityUtils.UNIDATA_SECURITY_DATA_SOURCE);
            }

            validateUser(user, true);

            final List<Role> roles = user.getRoles();
            final UserPO toSave = new UserPO();
            UserDTOToPOConverter.convert(user, toSave);

            final List<RolePO> rolePOs = new ArrayList<>();
            for (int i = 0; roles != null && i < roles.size(); i++) {

                final Role role = roles.get(i);
                if (StringUtils.isBlank(role.getName())) {
                    continue;
                }

                final RolePO rolePO = roleDAO.findByName(role.getName());
                if (rolePO != null) {
                    rolePOs.add(rolePO);
                }
            }

            toSave.setRoles(rolePOs);

            userDAO.create(toSave, user.getSecurityLabels());


            saveUserPropertyValues(toSave.getId(), user.getProperties());

            // auditEventsWriter.writeSuccessEvent(AuditActions.USER_CREATE, user);
        } catch (Exception e) {
            // auditEventsWriter.writeUnsuccessfulEvent(AuditActions.USER_CREATE, e, user);
            throw e;
        }
    }

    @Override
    public void updateUser(
            final String login,
            final UserWithPasswordDTO toUpdateDTO
    ) {
        updateUser(login, toUpdateDTO, true);
    }

    @Override
    @Transactional
    public void updateUserLocale(final String login, final Locale newLocale) {
        final UserPO toUpdatePO = userDAO.findByLogin(login);
        if (toUpdatePO != null && !newLocale.getLanguage().equals(toUpdatePO.getLocale())) {
            userDAO.updateLocale(login, newLocale.getLanguage());
            securityService.changeLocale(login, newLocale);

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.service.security.IUserService#updateUser(java.
     * lang.String,
     * com.unidata.mdm.backend.common.dto.security.UserWithPasswordDTO,
     * java.lang.String)
     */
    @Override
    @Transactional
    public void updateUser(
            final String login,
            final UserWithPasswordDTO toUpdateDTO,
            final boolean logout
    ) {
        try {
        	validateUser(toUpdateDTO, false);
            final List<String> newRoles = toUpdateDTO.getRoles() == null ?
                    Collections.emptyList() :
                    toUpdateDTO.getRoles().stream().map(Role::getName).collect(Collectors.toList());

            // retrieve user from database
            final UserPO toUpdatePO = userDAO.findByLogin(login);
            toUpdatePO.setPassword(null);

            boolean inactiveOrNotAdminUpdate = !toUpdateDTO.isActive() || !toUpdateDTO.isAdmin();
            if (toUpdatePO.isActive() && toUpdatePO.isAdmin() && userDAO.isLastAdmin() && inactiveOrNotAdminUpdate) {
                throw new PlatformBusinessException("Unable to deactivate user! At least one active admin user must exist!",
                        CoreExceptionIds.EX_SECURITY_CANNOT_DEACTIVATE_USER);
            }

            final List<RolePO> rolesToUpdate = toUpdatePO.getRoles();
            // prepare list with user roles
            final List<RolePO> filteredRoles = rolesToUpdate.stream()
                    .filter(r -> newRoles.contains(r.getName()))
                    .collect(Collectors.toList());

            filteredRoles.forEach(r -> newRoles.remove(r.getName()));
            newRoles.forEach(roleName -> {
                RolePO toAdd = roleDAO.findByName(roleName);
                if (toAdd != null) {
                    filteredRoles.add(toAdd);
                }
            });

            if (!toUpdateDTO.isExternal()) {
                toUpdateDTO.setSecurityDataSource(SecurityUtils.UNIDATA_SECURITY_DATA_SOURCE);
            }

            UserDTOToPOConverter.convert(toUpdateDTO, toUpdatePO);
            toUpdatePO.setRoles(filteredRoles);

            toUpdatePO.setRoles(filteredRoles);
            toUpdatePO.setProperties(UserPropertyConverter.convertPropertyDTOs(toUpdateDTO.getProperties()));

            if(StringUtils.isNotBlank(toUpdateDTO.getPassword())){
                updatePassword(toUpdateDTO.getLogin(), null, toUpdateDTO.getPassword(), false);
            }

            userDAO.update(login, toUpdatePO, toUpdateDTO.getSecurityLabels());
            saveUserPropertyValues(toUpdatePO.getId(), toUpdateDTO.getProperties());

            if (logout) {
                securityService.logoutUserByName(toUpdateDTO.getLogin());
            }
            securityService.updateInnerToken(toUpdateDTO.getLogin());
            // auditEventsWriter.writeSuccessEvent(AuditActions.USER_UPDATE, toUpdateDTO);
        } catch (Exception e) {
            // auditEventsWriter.writeUnsuccessfulEvent(AuditActions.USER_UPDATE, e, toUpdateDTO);
            throw e;
        }
    }

    @Override
    @Transactional
    public void updatePassword(String login, String activationCode, String newPassword, boolean temp) {
        final UserPO toUpdatePO = userDAO.findByLogin(login);
        updatePassword(toUpdatePO.getId(), activationCode, newPassword, temp);
    }

    private void updatePassword(Integer userId, String activationCode, String newPassword, boolean temp) {

        if (!passwordPolicy.isLengthEnough(newPassword)) {
            throw new PlatformBusinessException("Password is too short, according to the current policy.",
                    CoreExceptionIds.EX_SECURITY_USER_PASSWORD_TOO_SHORT);
        }
        if (!passwordPolicy.regexpMatching(newPassword)) {
            throw new PlatformBusinessException("Password doesn't match.",
                    CoreExceptionIds.EX_SECURITY_USER_PASSWORD_DOESNT_MATCH, passwordPolicy.regexpExample());
        }

        if (passwordPolicy.getLastRepeatCount() != null && passwordPolicy.getLastRepeatCount() > 0) {
            List<PasswordPO> passwordPOS = userDAO.fetchLastUserPasswords(userId, passwordPolicy.getLastRepeatCount());
            passwordPOS.forEach(p -> {
                if (BCrypt.checkpw(newPassword, p.getPasswordText())) {
                    throw new PlatformBusinessException("New password is the same as the old one, what is prohibited by the policy.",
                            CoreExceptionIds.EX_SECURITY_USER_PASSWORD_UPDATE_SAME_PASSWORD);
                }
            });
        }

        PasswordPO password = new PasswordPO();
        password.setActive(!temp);
        password.setActivationCode(activationCode);
        password.setPasswordText(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        password.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        //If temporary password generated set updated_by to 'SYSTEM', it will force user to change password at next login.
		password.setUpdatedBy(temp ? SecurityConstants.SYSTEM_USER_NAME : SecurityUtils.getCurrentUserName());

        userDAO.updatePassword(userId, password, temp);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.service.security.IUserService#getLocalUserByName(
     * java.lang.String)
     */
    @Override
    @Transactional
    public UserWithPasswordDTO getUserByName(final String login) {
        MeasurementPoint.start();
        try {
            final UserPO user = userDAO.findByLogin(login);
            return enrichRegularUser(user);
        } finally {
            MeasurementPoint.stop();
        }
    }

    private UserWithPasswordDTO enrichRegularUser(final UserPO user) {
        if (user == null) {
            return null;
        }

        final UserWithPasswordDTO userDTO = UserConverter.convertPO(user);
        enrichSecurityLabels(userDTO.getSecurityLabels());
        userDTO.getRoles().forEach(u -> enrichSecurityLabels(u.getSecurityLabels()));

        userDTO.setProperties(UserPropertyConverter.convertValuePOs(user.getProperties()));

        return userDTO;
    }

    private void enrichSecurityLabels(List<SecurityLabel> slas) {
        if (slas != null) {
            for (final SecurityLabel label : slas) {
                final List<LabelAttributePO> toCheck = roleDAO.findSecurityLabelByName(label.getName())
                        .getLabelAttribute();

                final List<LabelAttributePO> toEnrich = toCheck.stream()
                        .filter(labelAttributePO -> label.getAttributes().stream()
                                .noneMatch(sla -> StringUtils.equals(sla.getName(), labelAttributePO.getName())))
                        .collect(Collectors.toList());

                for (final LabelAttributePO labelAttributePO : toEnrich) {
                    final SecurityLabelAttributeDTO attributeDTO = new SecurityLabelAttributeDTO();
                    attributeDTO.setId(labelAttributePO.getId());
                    attributeDTO.setDescription(labelAttributePO.getDescription());
                    attributeDTO.setPath(labelAttributePO.getPath());
                    attributeDTO.setName(labelAttributePO.getName());
                    attributeDTO.setValue("");
                    label.getAttributes().add(attributeDTO);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.service.security.IUserService#getAllUsers()
     */
    @Override
    @Transactional
    public List<UserDTO> getAllUsers() {
        List<UserPO> users = userDAO.getAll();
        return UserConverter.convertPOs(users);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.service.security.IUserService#deactivateUser(java
     * .lang.String)
     */
    @Override
    @Transactional
    public void deactivateUser(String login) {
        UserWithPasswordDTO user = getUserByName(login);
        user.setActive(false);
        updateUser(login, user);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.service.security.IUserService#getAllProperties()
     */
    @Override
    @Transactional
    public List<UserPropertyDTO> getAllProperties() {
        return UserPropertyConverter.convertPropertyPOs(userDAO.loadAllProperties());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.service.security.IUserService#saveProperty(com.
     * unidata.mdm.backend.service.security.dto.UserPropertyDTO)
     */
    @Override
    @Transactional
    public void saveProperty(final UserPropertyDTO property) {

        validateUserProperty(property);

        final UserPropertyPO po = UserPropertyConverter.convert(property);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        String userName = SecurityUtils.getCurrentUserName();

        po.setCreatedAt(now);
        po.setCreatedBy(userName);
        po.setUpdatedAt(now);
        po.setUpdatedBy(userName);

        userDAO.saveProperty(po);

        property.setId(po.getId());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.service.security.IUserService#deleteProperty(
     * long)
     */
    @Override
    @Transactional
    public void deleteProperty(long id) {
        userDAO.deleteProperty(id);

        LOGGER.debug("Delete user property [id={}]", id);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.service.security.IUserService#
     * loadUserPropertyValues(long)
     */
    @Override
    public List<UserPropertyDTO> loadUserPropertyValues(int userId) {
        return UserPropertyConverter.convertValuePOs(userDAO.loadUserPropertyValuesByUserId(userId));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.service.security.IUserService#
     * saveUserPropertyValues(long, java.util.List)
     */
    @Override
    @Transactional
    public void saveUserPropertyValues(long userId, List<UserPropertyDTO> userProperties) {
    	validateUserPropertyValues(userProperties);
        userDAO.deleteUserPropertyValuesByUserId(userId);
        if (!CollectionUtils.isEmpty(userProperties)) {

            final List<UserPropertyValuePO> valuePOs = UserPropertyConverter.convertPropertyDTOs(userProperties);
            valuePOs.forEach(valuePO -> {
                valuePO.setUserId(userId);

                Timestamp now = new Timestamp(System.currentTimeMillis());
                String userName = SecurityUtils.getCurrentUserName();

                valuePO.setCreatedAt(now);
                valuePO.setCreatedBy(userName);
                valuePO.setUpdatedAt(now);
                valuePO.setUpdatedBy(userName);
            });

            userDAO.saveUserPropertyValues(valuePOs);
        }
    }
    /**
     * Validate user properties values.
     * @param toCheck properties to validate.
     */
    private void validateUserPropertyValues(List<UserPropertyDTO> toCheck) {
		if (CollectionUtils.isEmpty(toCheck)) {
			return;
		}
		final List<ValidationResult> validationResult = new ArrayList<>();
		List<UserPropertyDTO> allProperties = getAllProperties();
		if (allProperties != null) {
			Map<String, UserPropertyDTO> propertiesMap = allProperties.stream()
					.collect(Collectors.toMap(UserPropertyDTO::getName, r -> r));
			for (CustomProperty property : toCheck) {
				if (propertiesMap.get(property.getName()).isRequired() && StringUtils.isEmpty(property.getValue())) {
					 validationResult.add(new ValidationResult("Required value for user property {0} not set.",
			                    VIOLATION_REQUIRED_PROPERTY_VALUE, property.getName()));
				}
			}
		}
		if (!CollectionUtils.isEmpty(validationResult)) {
			throw new PlatformValidationException("User property values validation error.",
					CoreExceptionIds.EX_USER_PROPERTY_VALUES_VALIDATION_ERROR, validationResult);
		}

	}
    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.service.security.IUserService#insertToken(com.
     * unidata.mdm.backend.dto.storage.SecurityToken)
     */
    @Override
    @Transactional
    public void insertToken(SecurityToken token) {
        final TokenPO tokenPO = new TokenPO();
        tokenPO.setToken(token.getToken());
        tokenPO.setCreatedAt(new Timestamp(token.getCreatedAt().getTime()));
        tokenPO.setCreatedBy(token.getUser().getName());
        tokenPO.setUser(userDAO.findByLogin(token.getUser().getLogin()));
        userDAO.saveToken(tokenPO);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.service.security.IUserService#getUserEvents(java.
     * lang.String, java.util.Date, int, int)
     */
    @Override
    public List<UserEventDTO> getUserEvents(String login, Date from, int page, int count) {

        MeasurementPoint.start();
        try {
            List<UserEventPO> events = userDAO.loadUserEvents(login, from, page, count);
            return events.stream().map(e -> {
                UserEventDTO dto = new UserEventDTO(e.getId());
                dto.setBinaryDataId(e.getBinaryDataId());
                dto.setCharacterDataId(e.getCharacterDataId());
                dto.setContent(e.getContent());
                dto.setCreateDate(e.getCreateDate());
                dto.setCreatedBy(e.getCreatedBy());
                dto.setType(e.getType());
                dto.setDetails(e.getDetails());

                return dto;
            }).collect(Collectors.toList());

        } finally {
            MeasurementPoint.stop();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.service.security.IUserService#countUserEvents(
     * java.lang.String)
     */
    @Override
    public Long countUserEvents(String login) {

        MeasurementPoint.start();
        try {
            return userDAO.countUserEvents(login);
        } finally {
            MeasurementPoint.stop();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.service.security.IUserService#deleteUserEvent(
     * java.lang.String)
     */
    @Override
    @Transactional
    public boolean deleteUserEvent(String eventId) {

        MeasurementPoint.start();
        try {
            return userDAO.deleteUserEvent(eventId);
        } finally {
            MeasurementPoint.stop();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteUserEvents(List<String> eventIds) {

        MeasurementPoint.start();
        try {
            return userDAO.deleteUserEvents(eventIds);
        } finally {
            MeasurementPoint.stop();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteAllEventsForCurrentUser(Date point) {

        MeasurementPoint.start();
        try {
            return userDAO.deleteAllUserEvents(SecurityUtils.getCurrentUserName(), point);
        } finally {
            MeasurementPoint.stop();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.service.security.IUserService#upsert(com.unidata.
     * mdm.backend.service.ctx.UpsertUserEventRequestContext)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserEventDTO upsert(UpsertUserEventRequestContext ueCtx) {

        MeasurementPoint.start();
        try {

            UserEventPO po = new UserEventPO();
            po.setContent(ueCtx.getContent());
            po.setDetails(ueCtx.getDetails());
            po.setCreatedBy(SecurityUtils.getCurrentUserName());
            po.setType(ueCtx.getType());

            if (ueCtx.getLogin() != null) {
                po = userDAO.create(po, ueCtx.getLogin());
            } else if (ueCtx.getUserId() != null) {
                po.setUserId(ueCtx.getUserId());
                po = userDAO.create(po);
            } else {
                final String message = "No user id/login supplied. Cannot upsert user event.";
                LOGGER.warn(message);
                throw new PlatformBusinessException(message, CoreExceptionIds.EX_DATA_USER_EVENT_NO_USER);
            }

            return po != null ? new UserEventDTO(po.getId()) : null;
        } finally {
            MeasurementPoint.stop();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.service.security.IUserService#
     * verifyAndUpserExternalUser(com.unidata.mdm.backend.common.integration.auth.dto.User,
     * java.util.List, java.lang.String)
     */
    @Override
    @Transactional
    public void verifyAndUpserExternalUser(final User user) {

        if (StringUtils.isBlank(user.getLogin())) {
            final String message = "Error while saving externally authentified user with no Unidata profile: "
                    + "Mandatory fields are empty, check 'login'.";
            LOGGER.warn(message);
            throw new PlatformBusinessException(message, CoreExceptionIds.EX_DATA_USER_UPSERT_EXTERNAL_USER_FAILED);
        }

        final UserPO userPo = userDAO.findByLogin(user.getLogin());
        if (userPo == null) {
            LOGGER.debug("Creating external user with login {}.", user.getLogin());
            create(createUserWithPasswordDTO(user));
        } else {
            final Date dateFromDb = userPo.getUpdatedAt() != null ? userPo.getUpdatedAt() : userPo.getCreatedAt();
            if (dateFromDb != null && user.getUpdatedAt() != null) {
                if (dateFromDb.before(user.getUpdatedAt())) {
                    LOGGER.debug("Updating external user with login {}.", user.getLogin());
                    final UserWithPasswordDTO dto = createUserWithPasswordDTO(user);
                    dto.setUpdatedBy("SYSTEM");
                    dto.setUpdatedAt(new Date());
                    updateUser(user.getLogin(), dto);
                } else {
                    LOGGER.debug("External user with login [" + user.getLogin() + "] and provider ["
                            + user.getSecurityDataSource() + "] is up to date");
                }
            } else {
                LOGGER.warn("Cannot check user update/create date, login: {}.", userPo.getLogin());
            }
        }
    }

    /**
     * Creates user DTO for externally authenticated user.
     * @param authUser the user
     * @return DTO
     */
    private UserWithPasswordDTO createUserWithPasswordDTO(User authUser) {

        final UserWithPasswordDTO dto = new UserWithPasswordDTO();
        dto.setLogin(authUser.getLogin());
        dto.setPasswordLastChangedAt(authUser.getPasswordUpdatedAt());

        List<Role> roles = authUser.getRoles() == null ? Collections.emptyList() : authUser.getRoles();
        List<Right> rights = authUser.getRights() == null ? Collections.emptyList() : authUser.getRights();

        dto.setRights(rights);
        dto.setRoles(roles);
        dto.setEmail(authUser.getEmail());
        dto.setLocale(authUser.getLocale());
        dto.setAdmin(authUser.isAdmin());
        dto.setFullName(authUser.getName());
        dto.setActive(true);
        dto.setCreatedAt(new Date());
        dto.setCreatedBy("SYSTEM");
        dto.setExternal(true);
        dto.setSecurityDataSource(authUser.getSecurityDataSource());
        dto.setEmailNotification(authUser.isEmailNotification());

        final List<SecurityLabel> secLabelList = new ArrayList<>();
        for (final Role r : roles) {

            if (CollectionUtils.isEmpty(r.getSecurityLabels())) {
                continue;
            }

            for (final SecurityLabel secLabel : r.getSecurityLabels()) {

                final SecurityLabelDTO target = new SecurityLabelDTO();
                target.setName(secLabel.getName());
                target.setDisplayName(secLabel.getDisplayName());

                final List<SecurityLabelAttribute> attributes = new ArrayList<>();
                for (final SecurityLabelAttribute attr : secLabel.getAttributes()) {

                    final SecurityLabelAttributeDTO attrDto = new SecurityLabelAttributeDTO();
                    attrDto.setId(attr.getId());
                    attrDto.setName(attr.getName());
                    attrDto.setDescription(attr.getDescription());
                    attrDto.setPath(attr.getPath());
                    attrDto.setValue(attr.getValue());
                    attributes.add(attrDto);
                }

                target.setAttributes(attributes);
                secLabelList.add(target);
            }
        }
        dto.setSecurityLabels(secLabelList);

        return dto;
    }

    private void validateUserProperty(final UserPropertyDTO property) {

        final List<ValidationResult> validationResult = new ArrayList<>();

        property.setName(StringUtils.trim(property.getName()));
        property.setDisplayName(StringUtils.trim(property.getDisplayName()));

        if (StringUtils.isEmpty(property.getName())) {
            validationResult.add(new ValidationResult("Property 'name' is blank/empty. Rejected.",
                    VIOLATION_NAME_PROPERTY_EMPTY));
        } else if (property.getName().length() > PARAM_NAME_LIMIT) {
            validationResult.add(new ValidationResult("The lenght of the 'name' parameter is larger than {0} limit.",
                    VIOLATION_NAME_PROPERTY_LENGTH, PARAM_NAME_LIMIT));
        }

        if (StringUtils.isEmpty(property.getDisplayName())) {
            validationResult.add(new ValidationResult("Property 'displayName' is blank/empty. Rejected.",
                    VIOLATION_DISPLAY_NAME_PROPERTY_EMPTY));
        } else if (property.getDisplayName().length() > PARAM_DISPLAY_NAME_LIMIT) {
            validationResult.add(new ValidationResult("The lenght of the 'displayName' parameter is larger than {0} limit.",
                    VIOLATION_DISPLAY_NAME_PROPERTY_LENGTH, PARAM_DISPLAY_NAME_LIMIT));
        }

        if (validationResult.isEmpty()) {

            UserPropertyPO existProperty = userDAO.loadPropertyByName(property.getName());
            if (existProperty != null && !existProperty.getId().equals(property.getId())) {
                validationResult.add(new ValidationResult("User property 'name' must be unique. Found existing property with name {0}.",
                        VIOLATION_NAME_PROPERTY_NOT_UNIQUE, property.getName()));
            }

            existProperty = userDAO.loadPropertyByDisplayName(property.getDisplayName());
            if (existProperty != null && !existProperty.getId().equals(property.getId())) {
                validationResult.add(new ValidationResult("User property 'displayName' must be unique. Found existing property with displayName {0}.",
                        VIOLATION_DISPLAY_NAME_PROPERTY_NOT_UNIQUE, property.getDisplayName()));
            }
        }

        if (!CollectionUtils.isEmpty(validationResult)) {
            throw new PlatformValidationException("User properties validation error.",
                    CoreExceptionIds.EX_USER_PROPERTY_VALIDATION_ERROR, validationResult);
        }
    }

    /**
     * Checks the record for validity.
     * @param user the record to validate
     */
    private void validateUser(final UserWithPasswordDTO user, boolean isNew) {

        final List<ValidationResult> validationResult = new ArrayList<>();

        // 1. Login name
        if (StringUtils.isBlank(user.getLogin())) {
            validationResult.add(new ValidationResult("User name is empty.",
                    VIOLATION_USER_NAME_EMPTY));
        } else if (user.getLogin().length() > USER_FIELD_LIMIT) {
            validationResult.add(new ValidationResult("User name is larger than the field limit.",
                    VIOLATION_USER_NAME_LENGTH, user.getLogin(), USER_FIELD_LIMIT));
        }

        // 2. Password
        if (isNew && !user.isExternal()) {
            if (StringUtils.isBlank(user.getPassword())) {
                validationResult.add(new ValidationResult("Supplied password is empty.", VIOLATION_PASSWORD_EMPTY));
            } else if (user.getPassword().length() > USER_FIELD_LIMIT) {
                validationResult.add(new ValidationResult("Supplied password is larger than the field limit.",
                        VIOLATION_PASSWORD_LENGTH, USER_FIELD_LIMIT));
            }
        }

        // 3. Email
        if (!StringUtils.isEmpty(user.getEmail()) && user.getEmail().length() > USER_FIELD_LIMIT) {
            validationResult.add(new ValidationResult("Email is larger than the field limit.",
                    VIOLATION_EMAIL_LENGTH, user.getEmail(), USER_FIELD_LIMIT));
        }

        if (!StringUtils.isEmpty(user.getFirstName()) && user.getFirstName().length() > USER_FIELD_LIMIT) {
            validationResult.add(new ValidationResult("First name is larger than the field limit.",
                    VIOLATION_FIRSTNAME_LENGTH,  user.getFirstName(), USER_FIELD_LIMIT));
        }

        // 5. Last name
        if (!StringUtils.isEmpty(user.getLastName()) && user.getLastName().length() > USER_FIELD_LIMIT) {
            validationResult.add(new ValidationResult("Last name is larger than the field limit.",
                    VIOLATION_LASTNAME_LENGTH,  user.getLastName(), USER_FIELD_LIMIT));
        }

        if (!CollectionUtils.isEmpty(validationResult)) {
            throw new PlatformValidationException("User {} data validation error.",
                    CoreExceptionIds.EX_USER_DATA_VALIDATION_ERROR, validationResult, user.getLogin());
        }

        // 6. Duplicate
        if (isNew && userDAO.isExist(user.getLogin())) {
            throw new PlatformBusinessException("User {} already exists.",
                    CoreExceptionIds.EX_SECURITY_USER_ALREADY_EXISTS, user.getLogin());
        }
    }

    /**
     *
     */
    @Override
    public List<Endpoint> getAPIList() {
        List<ApiPO> source = userDAO.getAPIList();
        return UserConverter.convertAPIs(source);
    }

    @Override
    public boolean isAdminUser(String login){
        boolean result = false;
        if(StringUtils.isNotEmpty(login)){
            UserPO user = userDAO.findByLogin(login);
            if(user != null) {
                result = user.isAdmin();
            }
        }
        return result;
    }

    @Override
    public List<UserDTO> loadAllUsers() {
        return UserConverter.convertPOs(userDAO.fetchUsersFullInfo());
    }

    @Override
    public List<PasswordDTO> loadUserPasswords() {
        return UserConverter.covertPasswords(userDAO.fetchAllUsersPasswords());
    }

    @Override
    public void removeUsersByLogin(final List<String> logins) {
        userDAO.deleteUsersByLogin(logins);
    }

    @Override
    public void saveUsers(final List<UserWithPasswordDTO> users) {
        userDAO.saveUsers(
                UserDTOToPOConverter.convert(users),
                users.stream()
                        .filter(u -> CollectionUtils.isNotEmpty(u.getSecurityLabels()))
                        .collect(Collectors.toMap(UserDTO::getLogin, UserDTO::getSecurityLabels))
        );
    }

    @Override
    public void addUsersPasswords(Map<String, List<UserPasswordDef>> usersPasswords) {
        if (MapUtils.isNotEmpty(usersPasswords)) {
            final List<PasswordPO> passwords = usersPasswords.entrySet().stream()
                    .flatMap(e -> {
                        final UserPO user = new UserPO();
                        user.setLogin(e.getKey());
                        return e.getValue().stream().map(p -> {
                            final PasswordPO passwordPO = new PasswordPO();
                            passwordPO.setPasswordText(p.getPasswordText());
                            passwordPO.setActive(p.isActive());
                            passwordPO.setUser(user);
                            return passwordPO;
                        });
                    })
                    .collect(Collectors.toList());
            userDAO.saveUsersPasswords(passwords);
        }
    }

    @Override
    @Transactional
    public void addUsersRoles(Map<String, Set<String>> userRoleNames) {
        userDAO.addUsersRoles(userRoleNames);

        userRoleNames.keySet().forEach(login -> {
            // auditEventsWriter.writeSuccessEvent(AuditActions.USER_UPDATE_ROLES, login);
            securityService.updateInnerToken(login);
        });
    }

	@Override
	@Transactional
	public void forgotPassword(String login, String email) {
		//if user|email not defined do nothing
		if (StringUtils.isEmpty(login) && StringUtils.isEmpty(email)) {
			return;
		}
		//if user name is defined but user not found do nothing
		if (StringUtils.isNotEmpty(login) && !userDAO.isExist(login)) {
			return;
		}
		//
		UserPO user = null;
		if (StringUtils.isEmpty(login) && StringUtils.isNotEmpty(email)) {
			user = userDAO.findByEmail(email, SecurityUtils.UNIDATA_SECURITY_DATA_SOURCE);
			//if user not found by email do nothing
			if (user == null || StringUtils.isEmpty(user.getLogin())) {
				return;
			}
            login = user.getLogin();
		}
		user = userDAO.findByLogin(login);
		String newPassword = IdUtils.v4String();
		String activationCode = CryptUtils.toMurmurString(IdUtils.v4String());
		updatePassword(login, activationCode, newPassword, true);
		// userNotificationService.onPasswordReset(user.getLogin(), activationCode, newPassword, user.getEmail(), user.getLocale(), true);

	}

	@Override
	public void activatePassword(String activationCode) {
		if(StringUtils.isEmpty(activationCode)) {
			return;
		}
		userDAO.activatePassword(activationCode);
	}
}
