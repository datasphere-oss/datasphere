/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package com.huahui.datasphere.platform.rest.core.service;

import static org.unidata.mdm.core.util.SecurityUtils.ADMIN_SYSTEM_MANAGEMENT;
import static org.unidata.mdm.core.util.SecurityUtils.SECURITY_MARKS_MANAGEMENT;

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
import org.unidata.mdm.core.service.RoleService;
import org.unidata.mdm.core.type.security.SecurityLabel;
import org.unidata.mdm.rest.system.service.AbstractRestService;

import com.huahui.datasphere.platform.rest.core.converter.RoleRoConverter;
import com.huahui.datasphere.rest.system.ro.ErrorResponse;
import com.huahui.datasphere.rest.system.ro.RestResponse;
import com.huahui.datasphere.rest.system.ro.UpdateResponse;
import com.huahui.datasphere.rest.system.ro.security.SecurityLabelRO;

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
    @PreAuthorize("T(org.unidata.mdm.core.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(org.unidata.mdm.core.util.SecurityUtils).isCreateRightsForResource('"
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
    @PreAuthorize("T(org.unidata.mdm.core.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(org.unidata.mdm.core.util.SecurityUtils).isUpdateRightsForResource('"
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
    @PreAuthorize("T(org.unidata.mdm.core.util.SecurityUtils).isAdminUser()"
            + " or"
            + " T(org.unidata.mdm.core.util.SecurityUtils).isDeleteRightsForResource('"
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
