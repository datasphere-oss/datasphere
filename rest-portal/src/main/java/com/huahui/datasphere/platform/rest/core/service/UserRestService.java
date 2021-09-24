
package com.huahui.datasphere.platform.rest.core.service;

import static com.huahui.datasphere.portal.util.SecurityUtils.ADMIN_SYSTEM_MANAGEMENT;
import static com.huahui.datasphere.portal.util.SecurityUtils.USER_MANAGEMENT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import com.huahui.datasphere.portal.dto.UserDTO;
import com.huahui.datasphere.portal.dto.UserPropertyDTO;
import com.huahui.datasphere.portal.service.UserService;
import com.huahui.datasphere.portal.util.SecurityUtils;
import com.huahui.datasphere.platform.rest.core.converter.UsersConverter;
import com.huahui.datasphere.platform.rest.core.ro.ForgotPasswordRequestRO;
import com.huahui.datasphere.platform.rest.core.ro.UserPropertyRO;
import com.huahui.datasphere.platform.rest.core.ro.UserRO;
import com.huahui.datasphere.platform.rest.core.ro.UserWithPasswordRO;
import com.huahui.datasphere.rest.system.ro.ErrorResponse;
import com.huahui.datasphere.rest.system.ro.RestResponse;
import com.huahui.datasphere.rest.system.ro.UpdateResponse;
import com.huahui.datasphere.rest.system.service.AbstractRestService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * The Class UserRestService.
 */
@Path("/security/user")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class UserRestService extends AbstractRestService {

    /** The user service. */
    @Autowired
    private UserService userService;

    // TODO @Modules
//    /**
//     * Configuration service.
//     */
//    @Autowired
//    private ConfigurationServiceExt configurationService;
//
//    @Autowired
//    private UserNotificationService userNotificationService;

    /**
     * Creates the.
     *
     * @param user
     *            the user
     * @return the response
     * @throws Exception
     *             the exception
     */
    @POST
    @PreAuthorize("T(com.huahui.datasphere.portal.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(org.unidata.mdm.core.util.SecurityUtils).isCreateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + USER_MANAGEMENT + "')")
    @Operation(
        description = "创建一个新用户",
        method = "POST",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UserWithPasswordRO.class)), description = "用户描述"),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response create(final UserWithPasswordRO user) {
        userService.create(UsersConverter.convertUserRO(user));
        // TODO @Modules
//        if (user.isEmailNotification()) {
//            userNotificationService.onCreate(user);
//        }
        return ok(new RestResponse<>(new UpdateResponse(true, user.getLogin())));
    }

    /**
     * Creates the.
     *
     * @param request
     *            the user
     * @return the response
     * @throws Exception
     *             the exception
     */
    @POST
    @Path("/forgot-password")
    @Operation(
        description = "重置密码",
        method = "POST",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ForgotPasswordRequestRO.class)), description = "用户描述"),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response forgotPassword(final ForgotPasswordRequestRO request) {
        userService.forgotPassword(request.getLogin(), request.getEmail());
        return ok(new RestResponse<>());
    }

    /**
     * Update.
     *
     * @param login
     *            the login
     * @param user
     *            the user
     * @return the response
     * @throws Exception
     *             the exception
     */
    @PUT
    @Path(value = "{login}")
    @PreAuthorize("T(com.huahui.datasphere.portal.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.portal.util.SecurityUtils).isUpdateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + USER_MANAGEMENT + "')")
    @Operation(
        description = "修改现有用户",
        method = "PUT",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UserWithPasswordRO.class)), description = "用户描述"),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response update(@Parameter(description = "用户登录", in = ParameterIn.PATH) @PathParam(value = "login") String login, UserWithPasswordRO user) {
        userService.updateUser(login, UsersConverter.convertUserRO(user));
        return ok(new RestResponse<>(new UpdateResponse(true, user.getLogin())));
    }

    /**
     * Read.
     *
     * @param login
     *            the login
     * @return the response
     * @throws Exception
     *             the exception
     */
    @GET
    @Path(value = "{login}")
    @PreAuthorize("T(com.huahui.datasphere.portal.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.portal.util.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + USER_MANAGEMENT + "')")
    @Operation(
        description = "读取现有用户",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response read(@Parameter(description = "用户登录", in = ParameterIn.PATH) @PathParam(value = "login") String login) throws Exception {
        final UserDTO userDTO = userService.getUserByName(login);
        final UserRO userRO = UsersConverter.convertUserDTO(userDTO);
        return ok(new RestResponse<>(userRO));
    }

    /**
     * Read for UN-11566.
     *
     * @param login the login
     * @return the response
     * @throws Exception the exception
     */
    @GET
    @Path(value = "/user-info/{login}")
    @Operation(
        description = "读取有关现有用户的信息",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response readInfo(@Parameter(description = "用户登录", in = ParameterIn.PATH) @PathParam(value = "login") String login) {

        UserDTO userDTO = userService.getUserByName(login);
        if (SecurityUtils.isAdminUser() ||
                SecurityUtils.isReadRightsForResource(ADMIN_SYSTEM_MANAGEMENT, USER_MANAGEMENT)) {
            return ok(new RestResponse<>(UsersConverter.convertUserDTO(userDTO)));
        }

        UserRO userRO = new UserRO();
//        userRO.setFirstName(userDTO.getFirstName());
//        userRO.setLastName(userDTO.getLastName());
        userRO.setFullName(userDTO.getFullName());
        userRO.setLogin(userDTO.getLogin());
        userRO.setEmail(userDTO.getEmail());
        userRO.setActive(userDTO.isActive());
        userRO.setAdmin(userDTO.isAdmin());
        userRO.setFullName(userDTO.getFullName());

        return ok(new RestResponse<>(userRO));
    }

    /**
     * Read all.
     *
     * @return the response
     * @throws Exception
     *             the exception
     */
    @GET
    @Operation(
        description = "返回所有用户",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response readAll() {
        final List<UserDTO> userDTOs = userService.getAllUsers();
        final List<UserRO> result = UsersConverter.convertUserDTOs(userDTOs);
        result.forEach(s -> s.setProperties(new ArrayList<>()));
        return ok(new RestResponse<>(result));
    }

    /**
     *
     * @return
     * @throws Exception
     */
    @GET
    @Path("/auth-sources/list")
    @Operation(
        description = "返回外部身份验证/授权/配置文件的所有来源",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = List.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response loadAllAuthenticationSources() {
        return ok(new RestResponse<>(Collections.emptyList()/*UsersConverter.convertSecurityDataSources(
                configurationService.getSecurityDataSources().values()) TODO @Modules*/));
    }

    /**
     *
     * @return
     * @throws Exception
     */
    @GET
    @Path("/user-properties/list")
    @Operation(
        description = "返回所有用户属性",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response loadAllUserProperties() {
        return ok(new RestResponse<>(UsersConverter.convertPropertiesDtoToRo(userService.getAllProperties())));
    }
    /**
     *List with all available user apis(e.g. REST, SOAP, etc)
     * @return List with all available user apis(e.g. REST, SOAP, etc)
     * @throws Exception if something wrong happened.
     */
    @GET
    @Path("/user-api/list")
    @Operation(
        description = "返回所有用户API",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response loadAllUserAPIs() {
        return ok(new RestResponse<>(UsersConverter.convertAPIsDtoToRo(userService.getAPIList())));
    }
    /**
     *
     * @param userProperty
     * @return
     * @throws Exception
     */
    @PUT
    @Path("/user-properties/")
    @PreAuthorize("T(com.huahui.datasphere.portal.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.portal.util.SecurityUtils).isUpdateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + USER_MANAGEMENT + "')")
    @Operation(
        description = "保存新的自定义属性",
        method = "PUT",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UserPropertyRO.class)), description = "用户属性"),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response createUserProperty(final UserPropertyRO userProperty) {
        userProperty.setId(null);

        final UserPropertyDTO dto = UsersConverter.convertPropertyRoToDto(userProperty);
        userService.saveProperty(dto);
        return ok(new RestResponse<>(UsersConverter.convertPropertyDtoToRo(dto)));
    }

    /**
     *
     * @param userProperty
     * @return
     * @throws Exception
     */
    @PUT
    @Path("/user-properties/{userPropertyId}")
    @PreAuthorize("T(com.huahui.datasphere.portal.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.portal.util.SecurityUtils).isUpdateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + USER_MANAGEMENT + "')")
    @Operation(
        description = "编辑自定义属性",
        method = "PUT",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UserPropertyRO.class)), description = "用户属性"),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response saveUserProperty(
            @Parameter(description = "属性Id", in = ParameterIn.PATH) @PathParam("userPropertyId") final Long userPropertyId,
            final UserPropertyRO userProperty) {

        final UserPropertyDTO dto = UsersConverter.convertPropertyRoToDto(userProperty);
        dto.setId(userPropertyId);
        userService.saveProperty(dto);
        return ok(new RestResponse<>(UsersConverter.convertPropertyDtoToRo(dto)));
    }

    /**
     *
     * @param userPropertyId
     * @return
     * @throws Exception
     */
    @DELETE
    @Path("/user-properties/{userPropertyId}")
    @PreAuthorize("T(com.huahui.datasphere.portal.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.portal.util.SecurityUtils).isDeleteRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + USER_MANAGEMENT + "')")
    @Operation(
        description = "删除自定义属性",
        method = "DELETE",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response removeUserProperty(
            @Parameter(description = "用户属性标识符", in = ParameterIn.PATH) @PathParam("userPropertyId") final Long userPropertyId) {
        userService.deleteProperty(userPropertyId);
        return ok(new RestResponse<>(true));
    }
}
