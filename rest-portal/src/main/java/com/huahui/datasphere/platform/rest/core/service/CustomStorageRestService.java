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

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.unidata.mdm.core.dto.CustomStorageRecordDTO;
import org.unidata.mdm.core.service.CustomStorageService;
import org.unidata.mdm.rest.system.service.AbstractRestService;
import org.unidata.mdm.system.type.runtime.MeasurementContextName;
import org.unidata.mdm.system.type.runtime.MeasurementPoint;

import com.huahui.datasphere.platform.rest.core.converter.CustomStorageRecordsConverter;
import com.huahui.datasphere.platform.rest.core.ro.settings.CustomStorageRecordRO;
import com.huahui.datasphere.rest.system.ro.ErrorResponse;
import com.huahui.datasphere.rest.system.ro.RestResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * rest service for work with custom statistic
 */
@Path("/custom-storage")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class CustomStorageRestService extends AbstractRestService {

    private static final String PATH_KEY = "key";

    private static final String PATH_USER_NAME = "user-name";

    private static final String PARAM_KEY = "key";

    private static final String PARAM_USER_NAME = "userName";

    /** The stat service. */
    @Autowired
    private CustomStorageService customStorageService;

    /**
     * Create a records
     *
     * @param customStorageRecords records list
     * @return true if success, else false
     */
    @POST
    @Operation(
        description = "Создать записи.",
        method = "POST",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response create(@Parameter(description = "Список записей для сохранения") List<CustomStorageRecordRO> customStorageRecords) {

        MeasurementPoint.init(MeasurementContextName.MEASURE_UI_CREATE);
        MeasurementPoint.start();
        try {
            boolean result = customStorageService.createRecords(CustomStorageRecordsConverter.convertToDTO(customStorageRecords));
            RestResponse<Boolean> response = new RestResponse<>(null, result);
            return ok(response);
        } finally {
            MeasurementPoint.stop();
        }
    }

    /**
     * Update records
     *
     * @param customStorageRecords records list
     * @return true if success, else false
     */
    @PUT
    @Operation(
        description = "Обновить записи.",
        method = "PUT",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response update(@Parameter(description = "Список записей для обновления") List<CustomStorageRecordRO> customStorageRecords) {
        MeasurementPoint.init(MeasurementContextName.MEASURE_UI_CREATE);
        MeasurementPoint.start();
        try {
            boolean result = customStorageService.updateRecords(CustomStorageRecordsConverter.convertToDTO(customStorageRecords));
            RestResponse<Boolean> response = new RestResponse<>(null, result);
            return ok(response);
        } finally {
            MeasurementPoint.stop();
        }
    }

    /**
     * Delete list of custom storage records by user name
     * @param userName user name
     * @return true if success, else false
     * @throws Exception if something went wrong
     */
    @DELETE
    @Path(PATH_USER_NAME + "/{" + PARAM_USER_NAME + "}")
    @Operation(
        description = "Удалить параметры по имени пользователя.",
        method = "DELETE",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response deleteByUserName(@Parameter(description = "Имя пользователя")  @PathParam(PARAM_USER_NAME) String userName) {

        boolean result = customStorageService.deleteRecordsByUserName(userName);
        RestResponse<List<CustomStorageRecordRO>> response = new RestResponse<>(null, result);
        return ok(response);
    }

    /**
     * Delete list of custom storage records by key
     * @param key user name
     * @return true if success, else false
     * @throws Exception if something went wrong
     */
    @DELETE
    @Path(PATH_KEY + "/{" + PARAM_KEY + "}")
    @Operation(
        description = "Удалить параметры по ключу.",
        method = "DELETE",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response deleteByKey(@Parameter(description = "Ключ") @PathParam(PARAM_KEY) String key) {

        boolean result = customStorageService.deleteRecordsByKey(key);
        RestResponse<List<CustomStorageRecordRO>> response = new RestResponse<>(null, result);
        return ok(response);
    }

    /**
     * Delete list of custom storage records
     * @param customStorageRecords records for delete
     * @return true if success, else false
     * @throws Exception if something went wrong
     */
    @DELETE
    @Operation(
        description = "Удалить список заданнных параметров.",
        method = "DELETE",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response deleteRecords(@Parameter(description = "Список записей для удаления") List<CustomStorageRecordRO> customStorageRecords) {
        boolean result = customStorageService.deleteRecords(CustomStorageRecordsConverter.convertToDTO(customStorageRecords));
        RestResponse<Boolean> response = new RestResponse<>(null, result);
        return ok(response);
    }

    /**
     * Get list of custom storage records by key
     * @param key key
     * @return list of custom storage records
     * @throws Exception if something went wrong
     */
    @GET
    @Path(PATH_KEY + "/{" + PARAM_KEY + "}")
    @Operation(
        description = "Получить параметры по ключу.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response getParametersByKey(@Parameter(description = "Ключ") @PathParam(PARAM_KEY) String key) {
        List<CustomStorageRecordDTO> customSettingsList = customStorageService.getRecordsByKey(key);
        RestResponse<List<CustomStorageRecordRO>> response =
                new RestResponse<>(CustomStorageRecordsConverter.convertToRO(customSettingsList), true);
        return ok(response);
    }

    /**
     * Get list of custom storage records by user name
     * @param userName user name
     * @return list of custom storage records
     * @throws Exception if something went wrong
     */
    @GET
    @Path(PATH_USER_NAME + "/{" + PARAM_USER_NAME + "}")
    @Operation(
        description = "Получить параметры по имени пользователя.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response getParametersByUserName(@Parameter(description = "Имя пользователя")  @PathParam(PARAM_USER_NAME) String userName) {
        List<CustomStorageRecordDTO> customSettingsList = customStorageService.getRecordsByUserName(userName);
        RestResponse<List<CustomStorageRecordRO>> response =
                new RestResponse<>(CustomStorageRecordsConverter.convertToRO(customSettingsList), true);
        return ok(response);
    }

    /**
     * Get list of custom storage records by user name
     * @param userName user name
     * @param key key
     * @return list of custom storage records
     * @throws Exception if something went wrong
     */
    @GET
    @Operation(
        description = "Получить параметры по ключу и имени пользователя.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response getParametersByKeyAndUserName(@Parameter(description = "Ключ") @QueryParam(PARAM_KEY) String key,
                                                  @Parameter(description = "Имя пользователя") @QueryParam(PARAM_USER_NAME) String userName) {
        List<CustomStorageRecordDTO> customSettingsList = customStorageService.getRecordsByKeyAndUser(key, userName);
        RestResponse<List<CustomStorageRecordRO>> response =
                new RestResponse<>(CustomStorageRecordsConverter.convertToRO(customSettingsList), true);
        return ok(response);
    }

}