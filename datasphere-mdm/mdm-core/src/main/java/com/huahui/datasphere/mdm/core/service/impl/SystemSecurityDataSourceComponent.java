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

package com.huahui.datasphere.mdm.core.service.impl;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import com.huahui.datasphere.mdm.system.service.AfterModuleStartup;
import com.huahui.datasphere.mdm.system.util.IpUtils;

import com.huahui.datasphere.mdm.core.context.AuthenticationRequestContext;
import com.huahui.datasphere.mdm.core.dto.UserWithPasswordDTO;
import com.huahui.datasphere.mdm.core.service.PasswordPolicyService;
import com.huahui.datasphere.mdm.core.service.SecurityService;
import com.huahui.datasphere.mdm.core.service.UserService;
import com.huahui.datasphere.mdm.core.type.security.AuthenticationSystemParameter;
import com.huahui.datasphere.mdm.core.type.security.SecurityDataSource;
import com.huahui.datasphere.mdm.core.type.security.SecurityToken;
import com.huahui.datasphere.mdm.core.type.security.User;
import com.huahui.datasphere.mdm.core.type.security.impl.UserInfo;
import com.huahui.datasphere.mdm.core.type.security.provider.AuthRequestHandleResult;
import com.huahui.datasphere.mdm.core.type.security.provider.AuthenticationProvider;
import com.huahui.datasphere.mdm.core.type.security.provider.AuthorizationProvider;
import com.huahui.datasphere.mdm.core.type.security.provider.InterceptionProvider;
import com.huahui.datasphere.mdm.core.type.security.provider.NotAuthRequestHandleResult;
import com.huahui.datasphere.mdm.core.type.security.provider.ProfileProvider;
import com.huahui.datasphere.mdm.core.type.security.provider.RequestHandleResult;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;

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

        List<com.huahui.datasphere.mdm.core.type.security.Endpoint> endpoints
                = Objects.isNull(token)
                ? null
                : token.getUser().getEndpoints();

        if (CollectionUtils.isEmpty(endpoints)) {
            return false;
        }

        if (token.isInner()) {
            return false;
        }

        for (com.huahui.datasphere.mdm.core.type.security.Endpoint endpoint : endpoints) {
            if (StringUtils.equals(endpoint.getName(), com.huahui.datasphere.mdm.core.type.security.EndpointType.REST.name())) {
                return true;
            }
        }
        return false;
    }
}
