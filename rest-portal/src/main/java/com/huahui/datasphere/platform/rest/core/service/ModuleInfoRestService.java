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

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.unidata.mdm.rest.system.service.AbstractRestService;
import org.unidata.mdm.system.service.ModuleService;

import com.huahui.datasphere.platform.rest.core.ro.ModuleInfoRo;
import com.huahui.datasphere.rest.system.ro.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * @author maria.chistyakova
 * @since 18.03.2020
 */
@Path("/module")
@Consumes({"application/json"})
@Produces({"application/json"})
public class ModuleInfoRestService extends AbstractRestService {

    @Autowired
    ModuleService moduleService;

    @GET
    @Operation(
        description = "Вернуть список всех модулей.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Collection.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response dsf() {
        List<ModuleInfoRo> moduleInfoRoList = moduleService.getModules().stream()
                .map(module ->
                        new ModuleInfoRo(
                                module.getId(),
                                module.getDescription(),
                                module.getVersion()
                        )
                )
                .sorted(Comparator.comparing(ModuleInfoRo::getId))
                .collect(Collectors.toList());

        return ok(moduleInfoRoList);
    }

}
