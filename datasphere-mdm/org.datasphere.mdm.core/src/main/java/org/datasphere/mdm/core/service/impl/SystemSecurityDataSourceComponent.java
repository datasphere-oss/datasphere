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

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.datasphere.mdm.core.context.AuthenticationRequestContext;
import org.datasphere.mdm.core.dto.UserWithPasswordDTO;
import org.datasphere.mdm.core.service.PasswordPolicyService;
import org.datasphere.mdm.core.service.SecurityService;
import org.datasphere.mdm.core.service.UserService;
import org.datasphere.mdm.core.type.security.AuthenticationSystemParameter;
import org.datasphere.mdm.core.type.security.SecurityDataSource;
import org.datasphere.mdm.core.type.security.SecurityToken;
import org.datasphere.mdm.core.type.security.User;
import org.datasphere.mdm.core.type.security.impl.UserInfo;
import org.datasphere.mdm.core.type.security.provider.AuthRequestHandleResult;
import org.datasphere.mdm.core.type.security.provider.AuthenticationProvider;
import org.datasphere.mdm.core.type.security.provider.AuthorizationProvider;
import org.datasphere.mdm.core.type.security.provider.InterceptionProvider;
import org.datasphere.mdm.core.type.security.provider.NotAuthRequestHandleResult;
import org.datasphere.mdm.core.type.security.provider.ProfileProvider;
import org.datasphere.mdm.core.type.security.provider.RequestHandleResult;
import org.datasphere.mdm.core.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.datasphere.mdm.system.service.AfterModuleStartup;
import org.datasphere.mdm.system.util.IpUtils;

/**
 * System security data source, implementing the corresponding interface..
 *
 * @author Denis Kostovarov
 */
@Component
public final class SystemSecurityDataSourceComponent
    implements
        AuthenticationProvider,
        AuthorizationProvider,
        ProfileProvider,
        InterceptionProvider,
        SecurityDataSource,
        AfterModuleStartup {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemSecurityDataSourceComponent.class);
    /**
     * TOKEN query param name.
     */
    private static final String TOKEN = "token";
    /**
     * Prolong TTL header name.
     */
    private static final String PROLONG_TTL_HEADER = "PROLONG_TTL";
    /**
     * The security service.
     */
    @Autowired
    private SecurityService securityService;
    /**
     * User service.
     */
    @Autowired
    private UserService userService;
    /**
     * Password policy
     */
    @Autowired
    private PasswordPolicyService passwordPolicy;
    /**
     * Constructor.
     */
    public SystemSecurityDataSourceComponent() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterModuleStartup() {
        securityService.register(this);
    }
    /**
     * Name of security provider.
     *
     * @return security provider name.
     */
    @Override
    public String getName() {
        return SecurityUtils.UNIDATA_SECURITY_DATA_SOURCE;
    }
    /**
     * Description of the sec. data source.
     *
     * @return description
     */
    @Override
    public String getDescription() {
        return "System default security data source.";
    }
    /**
     * Authentication provider to check username/password pair and/or fill other user specific information.
     *
     * @return authentication provider.
     */
    @Override
    public AuthenticationProvider getAuthenticationProvider() {
        return this;
    }
    /**
     * Authorization provider to get rights for secured resources.
     *
     * @return authorization provider.
     */
    @Override
    public AuthorizationProvider getAuthorizationProvider() {
        return this;
    }
    /**
     * Profile provider to fetch user specific information.
     *
     * @return profile provider.
     */
    @Override
    public ProfileProvider getProfileProvider() {
        return this;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public InterceptionProvider getInterceptionProvider() {
        return this;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void load(User user) {

        // External user, but UD profile support
        UserWithPasswordDTO existing = userService.getUserByName(user.getLogin());
        user.setPasswordUpdatedAt(existing.getPasswordLastChangedAt());
        user.setAdmin(existing.isAdmin());
        user.setSecurityDataSource(existing.getSecurityDataSource() == null
                ? SecurityUtils.UNIDATA_SECURITY_DATA_SOURCE
                : existing.getSecurityDataSource());
        user.setUpdatedAt(existing.getUpdatedAt());
        user.setForcePasswordChangeFlag(false);
        user.setEmail(existing.getEmail());
        user.setLocale(existing.getLocale());
        user.setEndpoints(existing.getEndpoints());
        user.setName(existing.getFullName());
        user.setHasProfile(true);
        user.setEmailNotification(existing.isEmailNotification());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void authorize(User user) {

        // External user, but UD authorization support
        UserWithPasswordDTO existing = userService.getUserByName(user.getLogin());
        user.setRoles(existing.getRoles());
        user.setLabels(existing.getSecurityLabels());
        user.setEndpoints(existing.getEndpoints());
        user.setHasAuthorization(true);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public User login(AuthenticationRequestContext context) {

        Map<AuthenticationSystemParameter, Object> params = context.getParams();
        String username = (String) params.get(AuthenticationSystemParameter.PARAM_USER_NAME);
        String password = (String) params.get(AuthenticationSystemParameter.PARAM_USER_PASSWORD);

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return null;
        }

        // Authenticate
        UserWithPasswordDTO existing = userService.getUserByName(username);
        boolean isAuthenticated = existing != null
                && existing.getPassword() != null
                && existing.isActive()
                && BCrypt.checkpw(password, existing.getPassword());

        if (isAuthenticated) {

            UserInfo user = new UserInfo();

            // Authorize
            user.setRoles(existing.getRoles());
            user.setLabels(existing.getSecurityLabels());
            user.setCustomProperties(existing.getCustomProperties());
            user.setHasAuthorization(true);
            user.setEndpoints(existing.getEndpoints());

            // Profile
            user.setLogin(username);
            user.setPassword(password);
            user.setPasswordUpdatedAt(existing.getPasswordLastChangedAt());
            user.setPasswordUpdatedBy(existing.getPasswordUpdatedBy());
            user.setAdmin(existing.isAdmin());
            user.setSecurityDataSource(existing.getSecurityDataSource() == null
                    ? SecurityUtils.UNIDATA_SECURITY_DATA_SOURCE
                    : existing.getSecurityDataSource());
            user.setCreatedAt(existing.getCreatedAt());
            user.setUpdatedBy(existing.getPasswordUpdatedBy());
            user.setUpdatedAt(existing.getUpdatedAt());
            user.setExternal(existing.isExternal());
            user.setForcePasswordChangeFlag(forceChangePassword(user));
            user.setEmail(existing.getEmail());
            user.setLocale(existing.getLocale());
            user.setName(existing.getFullName());
            user.setHasProfile(true);
            user.setEmailNotification(existing.isEmailNotification());

            return user;
        }

        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public RequestHandleResult handleRequest(final HttpServletRequest request) {

        String token = extractToken(request);
        if (Objects.nonNull(token)) {
            try {

                // authentication with spring
                // UN-7140
                String prolongTTLAsString = request.getHeader(PROLONG_TTL_HEADER);
                boolean prolongTTL = StringUtils.isBlank(prolongTTLAsString) || BooleanUtils.toBoolean(prolongTTLAsString);

                boolean isAuthorized = securityService.authenticate(token, prolongTTL);
                if (isAuthorized) {

                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    if (authentication != null && authentication.getDetails() instanceof SecurityToken) {

                        // This works, because tokens are saved locally
                        // in near cache and the object's format is OBJECT (i. e. plain java heap objects).
                        SecurityToken current = (SecurityToken) authentication.getDetails();
                        current.setUserIp(IpUtils.serverIp(request));
                        current.setServerIp(IpUtils.serverIp(request));
                        if (!isRestUser(current)) {
                            return NotAuthRequestHandleResult.get();
                        }
                    }

                    return AuthRequestHandleResult.get();
                }
            } catch (Exception e) {
                LOGGER.error("Token: {} not valid!", token);
            }
        }

        return NotAuthRequestHandleResult.get();
    }

    /**
     * Check if the password should be changed.
     *
     * @param user user
     * @return true if the password should be chnaged
     */
    private boolean forceChangePassword(UserInfo user) {

        if (user.getPasswordUpdatedAt() != null) {
            return Duration.between(user.getPasswordUpdatedAt().toInstant(), user.getCreatedAt().toInstant()).abs().getSeconds() < 3
                    || (passwordPolicy.isExpired(user.getPasswordUpdatedAt().toInstant(), user.isAdmin()) && passwordPolicy.isAllowChangeExpiredPassword(user.isAdmin()))
                    || (!StringUtils.equals(user.getLogin(), user.getPasswordUpdatedBy()) && passwordPolicy.isAllowChangeExpiredPassword(user.isAdmin()));
        }

        return Boolean.FALSE;
    }
    private String extractToken(final HttpServletRequest request) {

        List<String> token;
        Map<String, List<String>> queryParams =
                JAXRSUtils.getStructuredParams(request.getQueryString(), "&", false, false);

        if (queryParams.containsKey(TOKEN)) {
            token = queryParams.get(TOKEN);
        } else {
            token = Collections.list(request.getHeaders(HttpHeaders.AUTHORIZATION));
        }

        return CollectionUtils.isNotEmpty(token) && token.size() == 1 ? token.get(0) : null;
    }
    /**
     * Checks if is rest user.
     *
     * @param token the token string
     * @return true, if is rest user
     */
    private boolean isRestUser(@Nullable SecurityToken token) {

        List<org.datasphere.mdm.core.type.security.Endpoint> endpoints
                = Objects.isNull(token)
                ? null
                : token.getUser().getEndpoints();

        if (CollectionUtils.isEmpty(endpoints)) {
            return false;
        }

        if (token.isInner()) {
            return false;
        }

        for (org.datasphere.mdm.core.type.security.Endpoint endpoint : endpoints) {
            if (StringUtils.equals(endpoint.getName(), org.datasphere.mdm.core.type.security.EndpointType.REST.name())) {
                return true;
            }
        }
        return false;
    }
}
