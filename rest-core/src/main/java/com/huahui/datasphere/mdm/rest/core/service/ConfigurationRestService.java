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
import static com.huahui.datasphere.mdm.core.util.SecurityUtils.PLATFORM_PARAMETERS_MANAGEMENT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import com.huahui.datasphere.mdm.core.service.RuntimePropertiesImportExportService;
import com.huahui.datasphere.mdm.core.util.FileUtils;
import com.huahui.datasphere.mdm.rest.system.ro.ErrorResponse;
import com.huahui.datasphere.mdm.rest.system.ro.RestResponse;
import com.huahui.datasphere.mdm.rest.system.service.AbstractRestService;
import com.huahui.datasphere.mdm.system.service.RuntimePropertiesService;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationProperty;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationValue;
import com.huahui.datasphere.mdm.system.util.TextUtils;

import com.huahui.datasphere.mdm.rest.core.ro.configuration.ConfigurationPropertyAvailableValuePO;
import com.huahui.datasphere.mdm.rest.core.ro.configuration.ConfigurationPropertyMetaRO;
import com.huahui.datasphere.mdm.rest.core.ro.configuration.ConfigurationPropertyRO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@Path("/configuration")
@Consumes({"application/json"})
@Produces({"application/json"})
public class ConfigurationRestService extends AbstractRestService {

    /**
     * The Constant DATA_PARAM_FILE.
     */
    private static final String DATA_PARAM_FILE = "file";

    @Autowired
    private RuntimePropertiesService runtimePropertiesService;

    @Autowired
    private RuntimePropertiesImportExportService runtimePropertiesImportExportService;

    @GET
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + PLATFORM_PARAMETERS_MANAGEMENT + "')")
    @Operation(
        description = "Вернуть список всех настроек.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Collection.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response availableProperties() {
        return ok(
                runtimePropertiesService.getAll().stream()
                        .map(this::toConfigurationPropertyRO)
                        .sorted(this::compareProperties)
                        .collect(Collectors.toList())
        );
    }

    @GET
    @Path("{name}")
    @Operation(
        description = "Вернуть список всех настроек для группы.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Collection.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response getPropertiesByName(@Parameter(description = "Код группы") @PathParam("name") String name) {
        return ok(
                runtimePropertiesService.getByGroup(name).stream()
                        .map(this::toConfigurationPropertyRO)
                        .sorted(this::compareProperties)
                        .collect(Collectors.toList())
        );
    }

    private ConfigurationPropertyRO toConfigurationPropertyRO(ConfigurationValue<?> p) {

        final ConfigurationProperty<?> property = p.getProperty();
        final Collection<ConfigurationPropertyAvailableValuePO> availableValues =
                property.getAvailableValues().entrySet().stream()
                        .map(v -> new ConfigurationPropertyAvailableValuePO(v.getKey(), v.getValue()))
                        .collect(Collectors.toCollection(ArrayList::new));
        String propertyKey = property.getKey();
        String propertyGroupKey = property.getGroupKey();
        return new ConfigurationPropertyRO(
                propertyKey,
                TextUtils.getText(propertyKey),
                propertyGroupKey,
                TextUtils.getText(propertyGroupKey),
                property.getPropertyType().value(),
                p.serialize(),
                new ConfigurationPropertyMetaRO(
                        availableValues,
                        property.isRequired(),
                        property.isReadOnly()));
    }

    private int compareProperties(ConfigurationPropertyRO o1, ConfigurationPropertyRO o2) {
        int groupCompare = o1.getGroupCode().compareTo(o2.getGroupCode());
        if (groupCompare == 0) {
            return o1.getName().compareTo(o2.getName());
        }
        return groupCompare;
    }

    @PUT
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isUpdateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + PLATFORM_PARAMETERS_MANAGEMENT + "')")
    @Operation(
        description = "Обновить настройки приложения.",
        method = "PUT",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Collection.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response upsert(@Parameter(description = "Список настроек") Map<String, String> properties) {
        runtimePropertiesService.update(properties);
        // TODO What is this?
        return ok(Collections.emptyList());
    }

    /**
     * Export backend configuration.
     *
     * @return the response
     * @throws Exception the exception
     */
    @GET
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + PLATFORM_PARAMETERS_MANAGEMENT + "')")
    @Path(value = "/export-config")
    @Operation(
        description = "Экспорт конфигурации.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Boolean.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response exportConfig() {
        runtimePropertiesImportExportService.exportProperties();
        return ok(new RestResponse<>(true));
    }

    /**
     * Import backend configuration.
     *
     * @param fileAttachment JSON file with jobs definitions.
     * @return the response
     * @throws Exception the exception
     */
    @POST
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isUpdateRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + PLATFORM_PARAMETERS_MANAGEMENT + "')")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path(value = "/import-config")
    @Operation(
        description = "Импорт конфигурации.",
        method = "POST",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Boolean.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response importJobs(@Parameter(description = "Импортируемый файл") @Multipart(value = DATA_PARAM_FILE) final Attachment fileAttachment) {
        if (Objects.isNull(fileAttachment)) {
            return okOrNotFound(null);
        }
        final java.nio.file.Path path = FileUtils.saveFileTempFolder(fileAttachment);
        runtimePropertiesImportExportService.importProperties(path);

        return ok(new RestResponse<>(true));
    }
}
