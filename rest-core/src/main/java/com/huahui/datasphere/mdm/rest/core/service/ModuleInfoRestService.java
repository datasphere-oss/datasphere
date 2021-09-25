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
import com.huahui.datasphere.mdm.rest.system.ro.ErrorResponse;
import com.huahui.datasphere.mdm.rest.system.service.AbstractRestService;
import com.huahui.datasphere.mdm.system.service.ModuleService;

import com.huahui.datasphere.mdm.rest.core.ro.ModuleInfoRo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * @author theseusyang
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
