/*
 * Apache License
 * 
 * Copyright (c) 2020 HuahuiData
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

package com.huahui.datasphere.platform.rest.core.service;

import static com.huahui.datasphere.platform.rest.core.converter.UsersConverter.convertUserDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;


import com.huahui.datasphere.platform.rest.core.converter.RoleRoConverter;
import com.huahui.datasphere.platform.rest.core.converter.UsersConverter;
import com.huahui.datasphere.platform.rest.core.exception.CoreRestExceptionIds;
import com.huahui.datasphere.platform.rest.core.ro.LoginRequest;
import com.huahui.datasphere.platform.rest.core.ro.LoginResponse;
import com.huahui.datasphere.platform.rest.core.ro.SetPasswordRequest;
import com.huahui.datasphere.platform.rest.core.ro.UserRO;
import com.huahui.datasphere.portal.dto.UserWithPasswordDTO;
import com.huahui.datasphere.portal.security.po.User;
import com.huahui.datasphere.portal.service.RoleService;
import com.huahui.datasphere.portal.service.SecurityService;
import com.huahui.datasphere.portal.service.UserService;
import com.huahui.datasphere.portal.type.security.AuthenticationSystemParameter;
import com.huahui.datasphere.portal.type.security.EndpointTypeInf;
import com.huahui.datasphere.portal.type.security.SecurityToken;
import com.huahui.datasphere.portal.util.SecurityUtils;
import com.huahui.datasphere.rest.system.ro.ErrorInfo;
import com.huahui.datasphere.rest.system.ro.ErrorResponse;
import com.huahui.datasphere.rest.system.ro.RestResponse;
import com.huahui.datasphere.rest.system.ro.UpdateResponse;
import com.huahui.datasphere.rest.system.service.AbstractRestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * The Class AuthenticationRestService.
 */
@Path("/authentication")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class LoginRestService extends AbstractRestService {

    /** The security service. */
    @Autowired
    private SecurityService securityService;

    /** The user service. */
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * Activate temporary user password, after password reset
     * @param activationCode
     * @return
     * @throws Exception
     */
    @GET
    @Path("/activate-password")
    @Operation(
        description = "在密码重置之后, 激活临时用户密码.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response activate(@Parameter(description = "激活代码") @QueryParam("activationCode") String activationCode) {
        userService.activatePassword(activationCode);
        return ok(new RestResponse<>());
    }

    /**
     * Login.
     *
     * @param loginRequest
     *            the login request
     * @return the response
     * @throws Exception
     *             the exception
     */
    @POST
    @Path(value = "/login")
    @Operation(
        description = "创建新的口令",
        method = "POST",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = LoginResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response login(final LoginRequest loginRequest){
        MeasurementPoint.init(MeasurementContextName.MEASURE_UI_AUTH);
        MeasurementPoint.start();


        LoginResponse loginResponse = new LoginResponse();
        String login = loginRequest.getUserName();
        Map<AuthenticationSystemParameter, Object> params = new EnumMap<>(AuthenticationSystemParameter.class);
        params.put(AuthenticationSystemParameter.PARAM_USER_NAME, login);
        params.put(AuthenticationSystemParameter.PARAM_USER_PASSWORD, loginRequest.getPassword());
        params.put(AuthenticationSystemParameter.PARAM_USER_LOCALE, loginRequest.getLocale());
        params.put(AuthenticationSystemParameter.PARAM_CLIENT_IP, getClientIp());
        params.put(AuthenticationSystemParameter.PARAM_SERVER_IP, getServerIp());
        params.put(AuthenticationSystemParameter.PARAM_ENDPOINT, EndpointTypeInf.REST);
        params.put(AuthenticationSystemParameter.PARAM_HTTP_SERVLET_REQUEST, getHSR());

        try {
            final SecurityToken token = securityService.login(params);

            loginResponse.setToken(token.getToken());
            final UserWithPasswordDTO user = userService.getUserByName(token.getUser().getLogin());
            if (loginRequest.getLocale() != null) {
                Locale newLocale = new Locale(loginRequest.getLocale());
                userService.updateUserLocale(login, newLocale);
                user.setLocale(newLocale);
            }
            final UserRO userInfo = convertUserDTO(user);
            userInfo.setSecurityLabels(
                    UsersConverter.convertSecurityLabelDTOs(
                            SecurityUtils.mergeSecurityLabels(
                                    user.getSecurityLabels(),
                                    user.getRoles().stream()
                                            .flatMap(r -> r.getSecurityLabels().stream())
                                            .collect(Collectors.toList())
                            )
                    )
            );
            userInfo.setRolesData(
                    RoleRoConverter.convertRoleDTOs(roleService.loadRolesData(userInfo.getRoles()))
            );

            loginResponse.setUserInfo(userInfo);
            loginResponse.setRights(new ArrayList<>(token.getRightsMap().values()));
            loginResponse.setTokenTTL(securityService.getTokenTTL());
            loginResponse.setForcePasswordChange(token.getUser().getForcePasswordChangeFlag());

            final String buildVersion = TextUtils.getText("app.version") +
                    "." +
                    TextUtils.getText("app.svn_revision") +
                    "." +
                    TextUtils.getText("app.build_number") +
                    " " +
                    TextUtils.getText("app.build_date");
            loginResponse.setBuildVersion(buildVersion);

            return ok(new RestResponse<>(loginResponse));
        } catch (org.springframework.transaction.CannotCreateTransactionException|org.springframework.transaction.TransactionSystemException e) {
            //UN-3952
            throw new PlatformBusinessException("Cannot connect to database.", CoreRestExceptionIds.EX_SYSTEM_DATABASE_CANNOT_CONNECT);
        } finally {
            MeasurementPoint.stop();
        }
    }

    /**
     * Returns info about current user.
     *
     * @return Information about current logged in user.
     * @throws Exception
     *             In case of some technical problem.
     */
    @GET
    @Path(value = "/get-current-user")
    @Operation(
        description = "返回有关当前用户的信息",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = LoginResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response getCurrentUserInfo(){

        LoginResponse loginResponse = new LoginResponse();
        String tokenString = null;
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            tokenString = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        if (tokenString == null) {
            return notAuthorized(loginResponse);
        }

        final SecurityToken token = securityService.getTokenObjectByToken(tokenString);
        loginResponse.setForcePasswordChange(token.getUser().getForcePasswordChangeFlag());
        loginResponse.setToken(tokenString);
        loginResponse.setUserInfo(convertUserDTO(userService.getUserByName(token.getUser().getLogin())));
        loginResponse.setRights(new ArrayList<>(token.getRightsMap().values()));
        loginResponse.setTokenTTL(securityService.getTokenTTL());

        String buildVersion = TextUtils.getText("app.version") +
                "." +
                TextUtils.getText("app.svn_revision") +
                "." +
                TextUtils.getText("app.build_number") +
                " " +
                TextUtils.getText("app.build_date");
        loginResponse.setBuildVersion(buildVersion);

        return ok(loginResponse);
    }

    /**
     * Set password.
     *
     * @param setPasswordRequest
     *            the set password request
     * @return the response
     * @throws Exception
     *             the exception
     */
    @POST
    @Path(value = "/setpassword")
    @Operation(
        description = "设置新的用户密码",
        method = "POST",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = UpdateResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response setPassword(@Parameter(description = "设置密码请求") SetPasswordRequest setPasswordRequest) {

        try {
            securityService.updatePassword(setPasswordRequest.getPassword(), setPasswordRequest.getOldPassword());
        }catch(PlatformBusinessException e){
            ErrorInfo error = new ErrorInfo();
            error.setSeverity(ErrorInfo.Severity.LOW);
            error.setInternalMessage(e.getMessage());
            error.setUserMessage(TextUtils.getText(e.getId().code(), e.getArgs()));
            RestResponse<?> restResponse = new RestResponse<>(false);
            restResponse.setErrors(Collections.singletonList(error));
            return error(restResponse);
        }
        return ok(new RestResponse<>(new UpdateResponse(true, setPasswordRequest.getUserName())));
    }

    /**
     * Logout.
     *
     * @param tokenString
     *            the token string
     * @return the response
     * @throws Exception
     *             the exception
     */
    @POST
    @Path(value = "/logout")
    @Operation(
        description = "令牌销毁.",
        method = "POST",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = UpdateResponse.class)), responseCode = "202"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response logout(
            @Parameter(description = "授权令牌", in = ParameterIn.HEADER) @HeaderParam(value = HttpHeaders.AUTHORIZATION) String tokenString) {

        User user = securityService.getUserByToken(tokenString);
        String userName = user != null ? user.getLogin() : null;
        Map<AuthenticationSystemParameter, Object> params = new EnumMap<>(AuthenticationSystemParameter.class);
        params.put(AuthenticationSystemParameter.PARAM_USER_NAME, userName);
        params.put(AuthenticationSystemParameter.PARAM_CLIENT_IP, getClientIp());
        params.put(AuthenticationSystemParameter.PARAM_SERVER_IP, getServerIp());
        params.put(AuthenticationSystemParameter.PARAM_ENDPOINT, EndpointTypeInf.REST);
        params.put(AuthenticationSystemParameter.PARAM_HTTP_SERVLET_REQUEST, getHSR());
        params.put(AuthenticationSystemParameter.PARAM_DETAILS,
                TextUtils.getTextWithLocaleAndDefault(user == null ?
                                TextUtils.getDefaultLocale() :
                                user.getLocale(),
                        "app.audit.record.operation.logout.userLogout", null));
        securityService.logout(tokenString, params);
        return Response.accepted().build();
    }
}
