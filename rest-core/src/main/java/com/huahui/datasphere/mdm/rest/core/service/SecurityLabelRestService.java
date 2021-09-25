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
import static com.huahui.datasphere.mdm.core.util.SecurityUtils.SECURITY_MARKS_MANAGEMENT;

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
import com.huahui.datasphere.mdm.core.service.RoleService;
import com.huahui.datasphere.mdm.core.type.security.SecurityLabel;
import com.huahui.datasphere.mdm.rest.system.ro.ErrorResponse;
import com.huahui.datasphere.mdm.rest.system.ro.RestResponse;
import com.huahui.datasphere.mdm.rest.system.ro.UpdateResponse;
import com.huahui.datasphere.mdm.rest.system.ro.security.SecurityLabelRO;
import com.huahui.datasphere.mdm.rest.system.service.AbstractRestService;

import com.huahui.datasphere.mdm.rest.core.converter.RoleRoConverter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/security/label")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class SecurityLabelRestService extends AbstractRestService {

    @Autowired
    private RoleService roleServiceExt;

    /**
     * Creates the.
     *
     * @param role
     *            the role
     * @return the response
     * @throws Exception
     *             the exception
     */
    @POST
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isCreateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + SECURITY_MARKS_MANAGEMENT + "')")
    @Operation(
        description = "Создание новой метки.",
        method = "POST",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SecurityLabelRO.class))),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response createLabel(SecurityLabelRO label) throws Exception {
        roleServiceExt.createLabel(RoleRoConverter.convertSecurityLabelRO(label));

        return ok(new RestResponse<>(new UpdateResponse(true, label.getName())));
    }

    /**
     * Update.
     *
     * @param roleName
     *            the role name
     * @param role
     *            the role
     * @return the response
     * @throws Exception
     *             the exception
     */
    @PUT
    @Path(value = "{labelName}")
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isUpdateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + SECURITY_MARKS_MANAGEMENT + "')")
    @Operation(
        description = "Модификация существующей метки.",
        method = "PUT",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SecurityLabelRO.class))),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response updateLabel(@Parameter(description = "Label name", in = ParameterIn.PATH) @PathParam(value = "labelName") String labelName,
            SecurityLabelRO label) {

        roleServiceExt.updateLabel(RoleRoConverter.convertSecurityLabelRO(label), labelName);
        return ok(new RestResponse<UpdateResponse>(new UpdateResponse(true, labelName)));
    }

    /**
     * Read.
     *
     * @param roleName
     *            the role name
     * @return the response
     * @throws Exception
     *             the exception
     */
    @GET
    @Path(value = "{labelName}")
    @Operation(
        description = "Возвращает существующую метку.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response read(@Parameter(description = "Label name", in = ParameterIn.PATH) @PathParam(value = "labelName") String labelName) throws Exception {
        SecurityLabel label = roleServiceExt.findLabel(labelName);
        return ok(new RestResponse<SecurityLabelRO>(RoleRoConverter.convertSecurityLabelDTO(label)));
    }

    /**
     * Delete.
     *
     * @param roleName
     *            the role name
     * @return the response
     * @throws Exception
     *             the exception
     */
    @DELETE
    @Path(value = "{labelName}")
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.SecurityUtils).isDeleteRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + SECURITY_MARKS_MANAGEMENT + "')")
    @Operation(
        description = "Удаление метки.",
        method = "DELETE",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = UpdateResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response delete(@Parameter(description = "Имя метки") @PathParam(value = "labelName") String labelName) {
        roleServiceExt.deleteLabel(labelName);
        return ok(new RestResponse<>(new UpdateResponse(true, labelName)));
    }
}
