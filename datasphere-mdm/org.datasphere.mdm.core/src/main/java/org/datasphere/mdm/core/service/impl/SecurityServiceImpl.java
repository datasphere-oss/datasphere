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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.configuration.CoreConfigurationConstants;
import org.datasphere.mdm.core.configuration.CoreMessagingDomain;
import org.datasphere.mdm.core.context.AuthenticationRequestContext;
import org.datasphere.mdm.core.dao.UserDao;
import org.datasphere.mdm.core.dto.UserWithPasswordDTO;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.exception.PlatformSecurityException;
import org.datasphere.mdm.core.service.PasswordPolicyService;
import org.datasphere.mdm.core.service.RoleService;
import org.datasphere.mdm.core.service.SecurityService;
import org.datasphere.mdm.core.service.UserService;
import org.datasphere.mdm.core.type.messaging.CoreTypes;
import org.datasphere.mdm.core.type.security.AuthenticationSystemParameter;
import org.datasphere.mdm.core.type.security.EndpointType;
import org.datasphere.mdm.core.type.security.Right;
import org.datasphere.mdm.core.type.security.Role;
import org.datasphere.mdm.core.type.security.SecurityDataSource;
import org.datasphere.mdm.core.type.security.SecurityLabel;
import org.datasphere.mdm.core.type.security.SecurityToken;
import org.datasphere.mdm.core.type.security.User;
import org.datasphere.mdm.core.type.security.impl.BearerToken;
import org.datasphere.mdm.core.type.security.impl.UserInfo;
import org.datasphere.mdm.core.type.security.provider.AuthenticationProvider;
import org.datasphere.mdm.core.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.datasphere.mdm.system.exception.PlatformBusinessException;
import org.datasphere.mdm.system.exception.PlatformRuntimeException;
import org.datasphere.mdm.system.service.ConfigurationActionService;
import org.datasphere.mdm.system.service.TextService;
import org.datasphere.mdm.system.type.action.ConfigurationAction;
import org.datasphere.mdm.system.type.annotation.ConfigurationRef;
import org.datasphere.mdm.system.type.annotation.DomainRef;
import org.datasphere.mdm.system.type.configuration.ConfigurationValue;
import org.datasphere.mdm.system.type.messaging.DomainInstance;
import org.datasphere.mdm.system.type.messaging.Header;
import org.datasphere.mdm.system.type.messaging.Message;
import org.datasphere.mdm.system.type.messaging.SystemHeaders;
import org.datasphere.mdm.system.type.runtime.MeasurementPoint;
import org.datasphere.mdm.system.util.IdUtils;
import org.datasphere.mdm.system.util.TransactionUtils;

import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MergePolicyConfig;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

/**
 * Service contains methods for authentication and authorization. User and role
 * management in different one.
 *
 * @author ilya.bykov
 */
@Service("securityService")
public class SecurityServiceImpl implements SecurityService {

    private static final String LOGOUT_AFTER_CHANGE_SETTINGS ="app.audit.record.operation.logout.changeSettings";
    private static final String LOGOUT_AFTER_CHANGE_ROLES = "app.audit.record.operation.logout.changeRoles";
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityServiceImpl.class);

    // TODO: Commented out in scope of UN-11834. Refactor this notification API!
    /*
    @Autowired
    private UserNotificationService userNotificationService;
    */
    /**
     * Text service.
     */
    @Autowired
    private TextService textService;
    /**
     * Camel router.
     */
    @DomainRef(CoreMessagingDomain.NAME)
    private DomainInstance coreMessagingDomain;
    /**
     * User service. Contains methods for user management.
     */
    @Autowired
    private UserService userService;
    /**
     * Role service. Contains methods for role management.
     */
    @Autowired
    private RoleService roleService;
    /**
     * Password policy
     */
    @Autowired
    private PasswordPolicyService passwordPolicy;
    /**
     * CAS.
     */
    @Autowired
    private ConfigurationActionService configurationActionService;
    /**
     * The token cache.
     */
    private IMap<String, SecurityToken> tokenCache;
    /**
     * Bogus operation for timestamp renewal.
     */
    private TokenTimestampRefresher tokenRefresher = new TokenTimestampRefresher();
    /**
     * HZ instance.
     */
    @Autowired
    private HazelcastInstance cache;
    /**
     * Authentication manager.
     */
    @Autowired
    @Qualifier(value = "authenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDao userDao;
    /**
     * Token time to live. By default set to 30 minutes.
     */
    @ConfigurationRef(CoreConfigurationConstants.PROPERTY_SECURITY_TOKEN_TTL)
    private ConfigurationValue<Long> tokenTTL;
    /**
     * Registered security data sources in the order of registration.
     */
    private List<SecurityDataSource> dataSources = new ArrayList<>(4);
    /**
     * Config action.
     */
    private final ConfigurationAction ensureTokensAction = new ConfigurationAction() {
        /**
         * {@inheritDoc}
         */
        @Override
        public int getTimes() {
            return 1;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String getId() {
            return CoreConfigurationConstants.CORE_SECURITY_TOKENS_MAP_ACTION_NAME;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean execute() {

            try {

                final MapConfig tokensMapConfig = new MapConfig(CoreConfigurationConstants.CORE_SECURITY_TOKENS_MAP_NAME)
                        .setBackupCount(1)
                        .setReadBackupData(true)
                        .setMaxIdleSeconds(tokenTTL.getValue().intValue())
                        .setMergePolicyConfig(new MergePolicyConfig("com.hazelcast.spi.merge.PassThroughMergePolicy", 1))
                        .setNearCacheConfig(new NearCacheConfig()
                                .setCacheLocalEntries(true)
                                .setInMemoryFormat(InMemoryFormat.OBJECT)
                                .setMaxIdleSeconds(tokenTTL.getValue().intValue())
                                .setInvalidateOnChange(true));

                cache.getConfig().addMapConfig(tokensMapConfig);

            } catch (Exception e) {
                LOGGER.warn("Cannot set up security tokens map configuration. Exception caught.", e);
                return false;
            }

            return true;
        }
    };
    /**
     * {@inheritDoc}
     */
    @Override
    public void register(SecurityDataSource sds) {

        Objects.requireNonNull(sds, "Cannot add null security datasource.");
        Objects.requireNonNull(sds.getName(), "Security datasource name must not be null.");

        boolean alreadyRegistered = dataSources.stream().anyMatch(s -> s.getName().equals(sds.getName()));
        if (alreadyRegistered) {
            throw new PlatformSecurityException("A security datasource with name {} already registered.",
                    CoreExceptionIds.EX_ERROR_SECURITY_DATASOURCE_REGISTERED, sds.getName());
        }

        dataSources.add(0, sds);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<SecurityDataSource> getSecurityDataSources() {
        return dataSources;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SecurityDataSource getSecurityDataSourceByName(String name) {
        return dataSources.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    /**
     * get or create inner token without endpoints
     *
     * @return token
     */
    @Override
    public String getOrCreateInnerTokenByLogin(String login) {
        SecurityToken token = tokenCache.values().stream()
                .filter(Objects::nonNull)
                .filter(t -> t.isInner() && login.equals(t.getUser().getLogin()))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(token)) {
            UserWithPasswordDTO dto = userService.getUserByName(login);

            if (Objects.isNull(dto) || !dto.isActive()) {
                return null;
            }

            UserInfo userInfo = new UserInfo();
            userInfo.setLogin(login);
            userInfo.setRights(dto.getRights());
            userInfo.setRoles(dto.getRoles());
            userInfo.setLabels(dto.getSecurityLabels());
            userInfo.setAdmin(dto.isAdmin());

            Map<AuthenticationSystemParameter, Object> params = new EnumMap<>(AuthenticationSystemParameter.class);
            params.put(AuthenticationSystemParameter.PARAM_USER_NAME, login);

            token = createToken(userInfo, params, true);
            tokenCache.put(token.getToken(), token);
        }

        return token.getToken();
    }

    @Override
    public void changeLocale(String login, Locale locale) {
        List<SecurityToken> tokens = tokenCache.values().stream()
                .filter(Objects::nonNull)
                .filter(entity -> entity.getUser() != null)
                .filter(t -> login.equals(t.getUser().getLogin()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(tokens)) {
            return;
        }

        for (SecurityToken token : tokens) {
            if (token.getUser() != null) {
                token.getUser().setLocale(locale);
                tokenCache.put(token.getToken(), token);
            }
        }

    }

    @Override
    public void updateInnerToken(String login) {
        SecurityToken token = tokenCache.values().stream()
                .filter(Objects::nonNull)
                .filter(entity -> entity.getUser() != null)
                .filter(t -> t.isInner() && login.equals(t.getUser().getLogin()))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(token)) {
            return;
        }
        updateInnerToken(token);
    }

    private void updateInnerToken(SecurityToken token) {
        if (!token.isInner()) {
            return;
        }
        refreshToken(token);
    }

    private void refreshToken(SecurityToken token) {
        UserWithPasswordDTO dto = userService.getUserByName(token.getUser().getLogin());
        if (Objects.isNull(dto)) {
            return;
        }

        if (!dto.isActive()) {
            tokenCache.delete(token.getToken());
            return;
        }

        setTokenAuthority(token, dto.getRoles(), dto.getRights(), dto.getSecurityLabels());
        // refresh roles manually
        token.getUser().setRoles(dto.getRoles());

        tokenCache.put(token.getToken(), token);
    }

    private void updateToken(SecurityToken token) {

        if (token.isInner()) {
            return;
        }

        refreshToken(token);
    }

    @Override
    public void updateInnerTokensWithRole(String roleName) {
        List<SecurityToken> toUpdate = tokenCache.values().stream()
                .filter(Objects::nonNull)
                .filter(entity -> entity.getUser() != null)
                .filter(t ->
                        t.isInner() &&
                                t.getRolesMap().values().stream()
                                        .anyMatch(role -> role.getName().equals(roleName))
                )
                .collect(Collectors.toList());


        toUpdate.forEach(this::updateInnerToken);
    }

    @Override
    public void updateUserTokensWithRole(List<String> roleNames) {
        List<SecurityToken> toUpdate = tokenCache.values().stream()
                .filter(Objects::nonNull)
                .filter(entity -> entity.getUser() != null)
                .filter(t ->
                        roleNames.stream().anyMatch(roleName -> t.getRolesMap().get(roleName) != null)
                )
                .collect(Collectors.toList());


        toUpdate.forEach(this::updateToken);
    }

    /**
     * Login procedure, v3.
     *
     * @return new token or null in case of failure
     */
    @Override
    public SecurityToken login(Map<AuthenticationSystemParameter, Object> params) {

        final Map<Header, Object> auditParams = new HashMap<>();
        auditParams.put(SystemHeaders.LOGIN, params.get(AuthenticationSystemParameter.PARAM_USER_NAME));
        auditParams.put(SystemHeaders.CLIENT_IP, params.get(AuthenticationSystemParameter.PARAM_CLIENT_IP));
        auditParams.put(SystemHeaders.SERVER_IP, params.get(AuthenticationSystemParameter.PARAM_SERVER_IP));
        auditParams.put(SystemHeaders.ENDPOINT, params.get(AuthenticationSystemParameter.PARAM_ENDPOINT));

        MeasurementPoint.start();
        try {

            if (!preValidate(params)) {
                final String message = "Invalid input. Pre-validate failed.";
                LOGGER.warn(message);
                throwLoginOrPasswordNotValid();
            }

            // 1. Load existing, if login was supplied
            UserWithPasswordDTO existing = null;
            if (Objects.nonNull(params.get(AuthenticationSystemParameter.PARAM_USER_NAME))) {
                existing = userService.getUserByName(params.get(AuthenticationSystemParameter.PARAM_USER_NAME).toString());
            }

            User result;
            String dataSourceName = null;
            // UC 1. Try all registered external SDS, if existing user is null
            // This is the case for external users, which may even have no login or password,
            // but cookies or similar mechanics for authentication are used instead.
            // A user will be created in UD though.
            if (existing == null || Objects.isNull(existing.getSecurityDataSource())) {
                result = processLogin(params);
                // UC 2. Authentication with login name. This includes two sub use cases:
                // - external user, authenticated elsewhere, but still using authorization and / or profile in UD
                // - internal user managed in UD completely.
            } else {
                dataSourceName = !existing.isExternal()
                        ? SecurityUtils.UNIDATA_SECURITY_DATA_SOURCE
                        : existing.getSecurityDataSource();

                SecurityDataSource source = getSecurityDataSourceByName(dataSourceName);
                if (Objects.isNull(source)) {
                    LOGGER.warn("Trying to authenticate user '{}' with not existing SDS '{}'. Aborting.",
                            existing.getLogin(), dataSourceName);
                    return null;
                }

                result = processLogin(params, source, true);
            }

            if (Objects.isNull(result)) {
                throwLoginOrPasswordNotValid();
            }
            if (!isCorrectEndpoint(result, params)) {
                throw new PlatformBusinessException("No rights for endpoint!", CoreExceptionIds.EX_SECURITY_USER_HAS_NO_RIGHTS_FOR_ENDPOINT);
            }

            if (SecurityUtils.UNIDATA_SECURITY_DATA_SOURCE.equals(dataSourceName)
                    && result.getPasswordUpdatedAt() != null
                    && passwordPolicy.isExpired(result.getPasswordUpdatedAt().toInstant(), result.isAdmin())
                    && !result.getForcePasswordChangeFlag()) {
                throw new PlatformBusinessException("User password expired!", CoreExceptionIds.EX_SECURITY_USER_PASSWORD_EXPIRED);
            }

            // 3. Enrich user object and create token
            SecurityToken token = createToken(result, params, false);

            userService.insertToken(token);
            tokenCache.set(token.getToken(), token);

            params.put(AuthenticationSystemParameter.PARAM_USER_TOKEN, token.getToken());

            coreMessagingDomain.send(new Message(CoreTypes.LOGIN)
                    .withHeaders(auditParams));
            return token;
        } catch (Exception e) {
            coreMessagingDomain.send(new Message(CoreTypes.LOGIN)
                    .withHeaders(auditParams)
                    .withCause(e));

            throw e;
        } finally {
            MeasurementPoint.stop();
        }
    }

    private void throwLoginOrPasswordNotValid() {
        throw new PlatformBusinessException("Login or password not valid!", CoreExceptionIds.EX_SECURITY_CANNOT_LOGIN);
    }

    /**
     * Determines is user have rights to access this endpoint.
     *
     * @param result result
     * @param params param map.
     * @return is user have rights to access this endpoint.
     */
    private boolean isCorrectEndpoint(User result, Map<AuthenticationSystemParameter, Object> params) {
        if (result == null || result.getEndpoints() == null) {
            return false;
        }
        return result.getEndpoints().stream().map(org.datasphere.mdm.core.type.security.Endpoint::getName)
                .anyMatch(el ->
                        StringUtils.equals(
                                el,
                                ((EndpointType) params.get(AuthenticationSystemParameter.PARAM_ENDPOINT)).name()
                        )
                );
    }

    /**
     * Go thru all security data sources and try to authenticate with any authentication provider.
     *
     * @param params authentication params
     * @return user or null
     */
    private User processLogin(Map<AuthenticationSystemParameter, Object> params) {

        for (SecurityDataSource sds : dataSources) {

            User result = processLogin(params, sds, false);
            if (Objects.isNull(result)) {
                continue;
            }

            result.setSecurityDataSource(sds.getName());

            try {
                userService.verifyAndUpserExternalUser(result);
            } catch (Exception exc) {
                LOGGER.warn("Cannot verify and save external user.", exc);
                return null;
            }

            return result;
        }

        return null;
    }

    /**
     * Tries to authenticate / authorize / get profile, using the supplied security data source and given credentials.
     *
     * @param params auth params
     * @param sds security data source
     * @param existingProfile existing profile or not
     * @return user or null
     */
    private User processLogin(Map<AuthenticationSystemParameter, Object> params, SecurityDataSource sds, boolean existingProfile) {

        try {

            final AuthenticationProvider authProvider = sds.getAuthenticationProvider();
            if (Objects.isNull(authProvider)) {
                return null;
            }

            User authUser = authProvider.login(AuthenticationRequestContext.of(params));
            if (authUser != null) {

                processAuthorization(authUser, sds, existingProfile);
                processProfile(authUser, sds, existingProfile);
            }

            return authUser;
        } catch (final PlatformRuntimeException sdpe) {
            LOGGER.warn("Unable to authenticate against security provider '{}'.", sds.getName(), sdpe);
        }

        return null;
    }

    /**
     * Calls authorization part.
     *
     * @param authUser user
     * @param sds source
     * @param existingProfile existing profile
     */
    private void processAuthorization(User authUser, SecurityDataSource sds, boolean existingProfile) {

        boolean runSelectedAuthorize
                = !authUser.hasAuthorization() && !Objects.isNull(sds.getAuthorizationProvider());
        boolean runSystemAuthorize
                = !authUser.hasAuthorization()
                && Objects.isNull(sds.getAuthorizationProvider())
                && existingProfile;
        if (runSelectedAuthorize) {
            sds.getAuthorizationProvider().authorize(authUser);
        } else if (runSystemAuthorize) {
            getSecurityDataSourceByName(SecurityUtils.UNIDATA_SECURITY_DATA_SOURCE).getAuthorizationProvider().authorize(authUser);
        }
    }

    /**
     * Calls profile part.
     *
     * @param authUser user
     * @param sds source
     * @param existingProfile existing profile
     */
    private void processProfile(User authUser, SecurityDataSource sds, boolean existingProfile) {

        boolean runSelectedProfile
                = !authUser.hasProfile() && !Objects.isNull(sds.getProfileProvider());
        boolean runSystemProfile
                = !authUser.hasProfile() && Objects.isNull(sds.getProfileProvider()) && existingProfile;
        if (runSelectedProfile) {
            sds.getProfileProvider().load(authUser);
        } else if (runSystemProfile) {
            getSecurityDataSourceByName(SecurityUtils.UNIDATA_SECURITY_DATA_SOURCE).getProfileProvider().load(authUser);
        }
    }

    /**
     * Fill security token with data. Enrich user object, if needed.
     *
     * @return the security token
     */
    private SecurityToken createToken(final User user, Map<AuthenticationSystemParameter, Object> params, boolean isInnerToken) {

        // 1. Create token and set necessary fields.
        final SecurityToken token = new SecurityToken(isInnerToken);
        if (!StringUtils.isBlank(user.getCustomToken())) {
            token.setToken(user.getCustomToken());
        } else {
            token.setToken(generateToken());
        }

        token.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        token.setUser(user);

        setTokenAuthority(token, user.getRoles(), user.getRights(), user.getLabels());

        token.setEndpoint((EndpointType) params.get(AuthenticationSystemParameter.PARAM_ENDPOINT));
        token.setUserIp((String) params.get(AuthenticationSystemParameter.PARAM_CLIENT_IP));
        token.setServerIp((String) params.get(AuthenticationSystemParameter.PARAM_SERVER_IP));

        return token;
    }

    private void setTokenAuthority(SecurityToken token, List<Role> roles, List<Right> rights, List<SecurityLabel> userLabels) {
        // Extract stuff, which may have been supplied by authorization
        roles = roles == null ? Collections.emptyList() : roles;
        rights = rights == null ? Collections.emptyList() : rights;
        userLabels = CollectionUtils.isEmpty(userLabels) ? Collections.emptyList() : userLabels;
        List<SecurityLabel> rolesLabels =
                CollectionUtils.isEmpty(roles) ?
                        Collections.emptyList() :
                        roles.stream()
                                .filter(r -> !CollectionUtils.isEmpty(r.getSecurityLabels()))
                                .flatMap(r -> r.getSecurityLabels().stream())
                                .collect(Collectors.toList());

        // Create maps from roles
        final Map<String, Right> rightsMap = SecurityUtils.createRightsMap(roles);
        final Map<String, Role> rolesMap = SecurityUtils.createRolesMap(roles);

        // Overwrite calculated rights with manually supplied one
        Map<String, Right> overwriteRights = SecurityUtils.extractRightsMap(rights);
        overwriteRights.forEach(rightsMap::put);

        token.getRightsMap().clear();
        token.getRightsMap().putAll(rightsMap);

        token.getRolesMap().clear();
        token.getRolesMap().putAll(rolesMap);

        token.getLabelsMap().clear();
        token.getLabelsMap().putAll(
                SecurityUtils.extractLabelsMap(
                        SecurityUtils.mergeSecurityLabels(userLabels, rolesLabels)
                )
        );
    }

    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.service.security.ISecurityService#logout(java.lang.String)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean logout(String tokenString, Map<AuthenticationSystemParameter, Object> params) {

        final Map<Header, Object> auditParams = new HashMap<>();
        auditParams.put(SystemHeaders.LOGIN, params.get(AuthenticationSystemParameter.PARAM_USER_NAME));
        auditParams.put(SystemHeaders.CLIENT_IP, params.get(AuthenticationSystemParameter.PARAM_CLIENT_IP));
        auditParams.put(SystemHeaders.SERVER_IP, params.get(AuthenticationSystemParameter.PARAM_SERVER_IP));
        auditParams.put(SystemHeaders.ENDPOINT, params.get(AuthenticationSystemParameter.PARAM_ENDPOINT));

        params.put(AuthenticationSystemParameter.PARAM_USER_TOKEN, tokenString);
        try {

            SecurityToken token = tokenCache.get(tokenString);
            boolean isTokenValid = token != null;
            if (token != null) {

                tokenCache.delete(tokenString);
                coreMessagingDomain.send(new Message(CoreTypes.LOGOUT)
                        .withHeaders(auditParams));
            }

            return isTokenValid;
        } catch (Exception e) {

            coreMessagingDomain.send(new Message(CoreTypes.LOGOUT)
                    .withHeaders(auditParams)
                    .withCause(e));

            throw e;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean authenticate(String token, boolean prolongTTL) {
        // Will call #validateToken and #getTokenByTokenString via delegation
        // This method is about to hide BearerToken type and auth context setting
        Authentication authentication = authenticationManager == null
                ? null
                : authenticationManager.authenticate(new BearerToken(token, prolongTTL));
        boolean isAuthenticated = authentication != null;
        if (isAuthenticated) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return isAuthenticated;
    }

    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.service.security.ISecurityService#updatePassword(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String password, String oldPassword) {

        final UserWithPasswordDTO oldUser = userService.getUserByName(SecurityUtils.getCurrentUserName());
        if (!oldUser.isActive()) {
            throw new PlatformBusinessException("User account is not active!", CoreExceptionIds.EX_SECURITY_USER_NOT_ACTIVE);
        }

        if (StringUtils.isBlank(oldPassword)) {

            SecurityToken token = getTokenObjectByToken(getCurrentUserToken());
            if (token == null || !token.getUser().getForcePasswordChangeFlag()) {
                throwPasswordChangeFailed("Old password is null!");
            }

            userService.updatePassword(oldUser.getLogin(), null, password, false);
            return;
        }

        if (!BCrypt.checkpw(oldPassword, oldUser.getPassword())) {
            throwPasswordChangeFailed("Old password doesn't match!");
        }

        userService.updatePassword(oldUser.getLogin(), null, password, false);
    }

    private void throwPasswordChangeFailed(String message) {
        throw new PlatformBusinessException(message, CoreExceptionIds.EX_SECURITY_USER_PASSWORD_CHANGE_FAILED);
    }

    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.service.security.ISecurityService#validateToken(java.lang.String, boolean)
     */
    @Override
    public boolean validateAndProlongToken(String tokenString, boolean prolongTTL) {

        SecurityToken token = tokenCache.get(tokenString);

        // 1. No such object exist
        if (Objects.isNull(token)) {
            return false;
        }

        // 2. Some failure caused data corruption / inconsistency
        String existingToken = token.getToken();
        boolean isValid = StringUtils.isNotBlank(existingToken) && StringUtils.equals(tokenString, existingToken);

        // 3. Submit bogus get operation to master partition, to cause access time renewal,
        // since near cache gets do not renew access times.
        if (isValid && prolongTTL) {
            tokenCache.submitToKey(tokenString, tokenRefresher);
        }

        return isValid;
    }

    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.service.security.ISecurityService#getUserByToken(java.lang.String)
     */
    @Override
    public User getUserByToken(String tokenString) {
        SecurityToken securityToken = tokenCache.get(tokenString);
        return securityToken != null ? securityToken.getUser() : null;
    }

    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.service.security.ISecurityService#getTokenByTokenString(java.lang.String)
     */
    @Override
    public SecurityToken getTokenObjectByToken(String tokenString) {
        return tokenCache.get(tokenString);
    }

    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.service.security.ISecurityService#getRightsByToken(java.lang.String)
     */
    @Override
    public List<Right> getRightsByToken(final String tokenString) {
        SecurityToken token = tokenCache.get(tokenString);
        return token == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(token.getRightsMap().values()));
    }

    /**
     * Mostly a placeholder for pre-validating logic, which may be suitable in the future.
     *
     * @param params auth params
     * @return
     */
    private boolean preValidate(Map<AuthenticationSystemParameter, Object> params) {
        return MapUtils.isNotEmpty(params);
    }

    /**
     * Generate token.
     *
     * @return the string
     */
    private static String generateToken() {
        return IdUtils.v4String();
    }

    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.service.security.ISecurityService#getTokenTTL()
     */
    @Override
    public long getTokenTTL() {
        return this.tokenTTL.getValue();
    }
    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.service.security.ISecurityService#afterContextRefresh()
     */
    @PostConstruct
    public void init() {

        configurationActionService.execute(ensureTokensAction);

        this.tokenCache = cache.getMap(CoreConfigurationConstants.CORE_SECURITY_TOKENS_MAP_NAME);
        this.tokenCache.addEntryListener(new TokenListener(coreMessagingDomain, userDao), true);
    }

    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.service.security.ISecurityService#logoutUserByName(java.lang.String)
     */
    @Transactional
    @Override
    public void logoutUserByName(String userName) {

        Map<AuthenticationSystemParameter, Object> params = new EnumMap<>(AuthenticationSystemParameter.class);
        params.put(AuthenticationSystemParameter.PARAM_USER_NAME, userName);
        params.put(AuthenticationSystemParameter.PARAM_DETAILS, textService.getText(LOGOUT_AFTER_CHANGE_SETTINGS));

        tokenCache.entrySet()
                .stream()
                .filter(Objects::nonNull)
                .filter(entity -> entity.getValue() != null && !entity.getValue().isInner())
                .filter(entity -> entity.getValue().getUser() != null)
                .filter(entity -> StringUtils.equals(entity.getValue().getUser().getLogin(), userName))
                .forEach(entity -> this.logout(entity.getKey(), params));
    }

    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.service.security.ISecurityService#logoutByRoleName(java.lang.String)
     */
    @Override
    public void logoutByRoleName(String roleName) {
        tokenCache.entrySet()
                .stream()
                .filter(Objects::nonNull)
                .filter(entity -> entity.getValue() != null && !entity.getValue().isInner())
                .filter(entity -> entity.getValue().getUser() != null)
                .filter(entity -> roleService.isUserInRole(entity.getValue().getUser().getLogin(), roleName))
                .forEach(this::forceLogout);
        TransactionUtils.executeAfterCommit(() -> updateInnerTokensWithRole(roleName));
    }

    private void forceLogout(Entry<String, SecurityToken> entity) {

        Map<AuthenticationSystemParameter, Object> params = new EnumMap<>(AuthenticationSystemParameter.class);
        params.put(AuthenticationSystemParameter.PARAM_DETAILS, textService.getText(LOGOUT_AFTER_CHANGE_ROLES));

        params.put(AuthenticationSystemParameter.PARAM_USER_NAME, entity.getValue().getUser().getLogin());
        this.logout(entity.getKey(), params);
    }

    @Override
    public boolean isAdminUser(String login) {
        return userService.isAdminUser(login);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentUserName() {
        return SecurityUtils.getCurrentUserName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentUserToken() {
        return SecurityUtils.getCurrentUserToken();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentUserStorageId() {
        return SecurityUtils.getCurrentUserStorageId();
    }
}
