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
package com.huahui.datasphere.mdm.rest.core.service;

import static com.huahui.datasphere.mdm.core.util.SecurityUtils.ADMIN_SYSTEM_MANAGEMENT;
import static com.huahui.datasphere.mdm.core.util.SecurityUtils.ROLE_MANAGEMENT;
import static com.huahui.datasphere.mdm.core.util.SecurityUtils.SECURITY_MARKS_MANAGEMENT;

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
import com.huahui.datasphere.mdm.core.dto.RolePropertyDTO;
import com.huahui.datasphere.mdm.core.dto.SecuredResourceDTO;
import com.huahui.datasphere.mdm.core.service.RoleService;
import com.huahui.datasphere.mdm.core.type.security.Role;
import com.huahui.datasphere.mdm.core.type.security.SecurityLabel;
import com.huahui.datasphere.mdm.rest.system.ro.ErrorResponse;
import com.huahui.datasphere.mdm.rest.system.ro.RestResponse;
import com.huahui.datasphere.mdm.rest.system.ro.UpdateResponse;
import com.huahui.datasphere.mdm.rest.system.ro.security.RolePropertyRO;
import com.huahui.datasphere.mdm.rest.system.ro.security.RoleRO;
import com.huahui.datasphere.mdm.rest.system.service.AbstractRestService;

import com.huahui.datasphere.mdm.rest.core.converter.RoleRoConverter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * The Class RoleRestService.
 */
@Path("/security/role")
@Consumes({"application/json"})
@Produces({"application/json"})
public class RoleRestService extends AbstractRestService {

    /**
     * The role service.
     */
    @Autowired
    private RoleService roleServiceExt;

    /**
     * Creates the.
     *
     * @param role the role
     * @return the response
     * @throws Exception the exception
     */
    @POST
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isCreateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + ROLE_MANAGEMENT + "')")
    @Operation(
        description = "Создание новой роли",
        method = "POST",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RoleRO.class)), description = "Описвние роли"),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response create(RoleRO role) {
        roleServiceExt.create(RoleRoConverter.convertRoleRO(role));
        return ok(new RestResponse<>(new UpdateResponse(true, role.getName())));
    }

    /**
     * Update.
     *
     * @param roleName the role name
     * @param role the role
     * @return the response
     */
    @PUT
    @Path(value = "{roleName}")
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isUpdateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + ROLE_MANAGEMENT + "')")
    @Operation(
        description = "Модификация существующей роли",
        method = "PUT",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RoleRO.class)), description = "Описвние роли"),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response update(@Parameter(description = "Идентификатор роли", in = ParameterIn.PATH) @PathParam(value = "roleName") String roleName, RoleRO role) {
        roleServiceExt.update(roleName, RoleRoConverter.convertRoleRO(role));
        return ok(new RestResponse<>(new UpdateResponse(true, role.getName())));
    }

    /**
     * Read.
     *
     * @param roleName the role names
     * @return the response
     * @throws Exception the exception
     */
    @GET
    @Path(value = "{roleName}")
    @Operation(
        description = "Возвращает существующую роль",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response read(@Parameter(description = "Идентификатор роли", in = ParameterIn.PATH) @PathParam(value = "roleName") String roleName) {
        final Role roleDTO = roleServiceExt.getRoleByName(roleName);
        return ok(new RestResponse<>(RoleRoConverter.convertRoleDTO(roleDTO)));
    }

    /**
     * Delete.
     *
     * @param roleName the role name
     * @return the response
     * @throws Exception
     *             the exception
     */
    @DELETE
    @Path(value = "{roleName}")
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isDeleteRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + ROLE_MANAGEMENT + "')")
    @Operation(
        description = "Удаляет существующую роль",
        method = "DELETE",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response delete(@Parameter(description = "Идентификатор роли", in = ParameterIn.PATH) @PathParam(value = "roleName") String roleName) {
        roleServiceExt.delete(roleName);
        return ok(new RestResponse<>(new UpdateResponse(true, roleName)));
    }

    /**
     * Unlink all connected resources.
     *
     * @param roleName role name
     * @param resourceName resource name
     * @return response
     */
    @DELETE
    @Path(value = "/unlink/{roleName}/{resourceName}")
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isDeleteRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + ROLE_MANAGEMENT + "')")
    @Operation(
        description = "Удаление всех связей между ролью, ресурсом и правами",
        method = "DELETE",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response unlink(
            @Parameter(description = "Идентификатор роли", in = ParameterIn.PATH) @PathParam(value = "roleName") String roleName,
            @Parameter(description = "Идентификатор ресурса", in = ParameterIn.PATH) @PathParam(value = "resourceName") String resourceName) {
        roleServiceExt.unlink(roleName, resourceName);
        return ok(new RestResponse<>(new UpdateResponse(true, roleName)));
    }

    /**
     * Read all.
     *
     * @return the response
     * @throws Exception the exception
     */
    @GET
    @Operation(
        description = "Возвращает все роли",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response readAll() {
        List<Role> roles = roleServiceExt.getAllRoles();
        return ok(new RestResponse<>(RoleRoConverter.convertRoleDTOs(roles)));
    }

    /**
     * Gets the all secured resources.
     *
     * @return the all secured resources
     */
    @GET
    @Path(value = "/get-all-secured-resources")
    @Operation(
        description = "Возвращает все возможные SecuredResource",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response getAllSecuredResources() {
        List<SecuredResourceDTO> securedResourceDTOs = roleServiceExt.getAllSecuredResources();
        return ok(new RestResponse<>(RoleRoConverter.convertSecuredResourceDTOs(securedResourceDTOs)));
    }

    /**
     * Gets the all security labels.
     *
     * @return the all security labels
     */
    @GET
    @Path(value = "/get-all-security-labels")
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + SECURITY_MARKS_MANAGEMENT + "')"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isUpdateRightsForResource('"
            + ROLE_MANAGEMENT + "')"
    )
    @Operation(
        description = "Возвращает все возможные метки безопасности",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response getAllSecurityLabels() {
        final List<SecurityLabel> securityLabels = roleServiceExt.getAllSecurityLabels();
        return ok(new RestResponse<>(RoleRoConverter.convertSecurityLabelDTOs(securityLabels)));
    }

    /**
     * @return
     * @throws Exception
     */
    @GET
    @Path("/role-properties/list")
    @Operation(
        description = "Возвращает все свойства ролей",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response loadAllRoleProperties() {
        return ok(new RestResponse<>(RoleRoConverter.convertPropertiesDtoToRo(roleServiceExt.loadAllProperties())));
    }

    /**
     * @param roleProperty
     * @return
     * @throws Exception
     */
    @PUT
    @Path("/role-properties/")
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isUpdateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + ROLE_MANAGEMENT + "')")
    @Operation(
        description = "Сохранить новое свойство роли",
        method = "PUT",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RolePropertyRO.class)), description = "Свойство роли"),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response createRoleProperty(final RolePropertyRO roleProperty) {
        roleProperty.setId(null);
        final RolePropertyDTO dto = RoleRoConverter.convertPropertyRoToDto(roleProperty);
        roleServiceExt.saveProperty(dto);
        return ok(new RestResponse<>(RoleRoConverter.convertPropertyDtoToRo(dto)));
    }

    /**
     * @param rolePropertyId
     * @param roleProperty
     * @return
     * @throws Exception
     */
    @PUT
    @Path("/role-properties/{rolePropertyId}")
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isUpdateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + ROLE_MANAGEMENT + "')")
    @Operation(
        description = "Редактировать свойство роли",
        method = "PUT",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RolePropertyRO.class)), description = "Свойство роли"),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response saveRoleProperty(
            @Parameter(description = "Идентификатор свойства роли", in = ParameterIn.PATH) @PathParam("rolePropertyId") final Long rolePropertyId,
            final RolePropertyRO roleProperty) {
        final RolePropertyDTO dto = RoleRoConverter.convertPropertyRoToDto(roleProperty);
        dto.setId(rolePropertyId);

        roleServiceExt.saveProperty(dto);

        return ok(new RestResponse<>(RoleRoConverter.convertPropertyDtoToRo(dto)));
    }

    /**
     * @param rolePropertyId
     * @return
     * @throws Exception
     */
    @DELETE
    @Path("/role-properties/{rolePropertyId}")
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isDeleteRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + ROLE_MANAGEMENT + "')")
    @Operation(
        description = "Удалить свойство роли",
        method = "DELETE",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response removeRoleProperty(
            @Parameter(description = "Идентификатор свойства роли", in = ParameterIn.PATH) @PathParam("rolePropertyId") final Long rolePropertyId) {
        roleServiceExt.deleteProperty(rolePropertyId);
        return ok(new RestResponse<>(true));
    }
}