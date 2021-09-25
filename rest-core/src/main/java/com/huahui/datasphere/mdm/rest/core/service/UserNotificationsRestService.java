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
/**
 *
 */
package com.huahui.datasphere.mdm.rest.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import com.huahui.datasphere.mdm.core.service.UserService;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;
import com.huahui.datasphere.mdm.rest.system.ro.ErrorResponse;
import com.huahui.datasphere.mdm.rest.system.ro.RestResponse;
import com.huahui.datasphere.mdm.rest.system.service.AbstractRestService;
import com.huahui.datasphere.mdm.rest.system.util.RestConstants;
import com.huahui.datasphere.mdm.system.type.runtime.MeasurementContextName;
import com.huahui.datasphere.mdm.system.type.runtime.MeasurementPoint;
import com.huahui.datasphere.mdm.system.util.ConvertUtils;

import com.huahui.datasphere.mdm.rest.core.converter.UserEventConverter;
import com.huahui.datasphere.mdm.rest.core.ro.UserEventRO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * @author theseusyang
 * User notifications REST service.
 */
@Path("/" + RestConstants.PATH_PARAM_USER)
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class UserNotificationsRestService extends AbstractRestService {

    /** The user service. */
    @Autowired
    private UserService userService;

    /**
     * Constructor.
     */
    public UserNotificationsRestService() {
        super();
    }

    /**
     * Gets user notifications.
     * @param dateAsString
     * @return
     */
    @GET
    @Path("/" + RestConstants.PATH_PARAM_NOTIFICATIONS + "{p:/?}{"
              + RestConstants.DATA_PARAM_DATE  + ": " + RestConstants.DEFAULT_TIMESTAMP_PATTERN +  "}")
    @Operation(
        description = "Возвращает все возможные user properties.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response getUserNotifications(
            @Parameter(description = "Дата ограничения") @PathParam(RestConstants.DATA_PARAM_DATE) String dateAsString) {

        MeasurementPoint.init(MeasurementContextName.MEASURE_UI_GET_USER_NOTIFICATIONS);
        MeasurementPoint.start();
        try {

            String login = SecurityUtils.getCurrentUserName();
            Date asOf = ConvertUtils.string2Date(dateAsString);
            ArrayList<UserEventRO> result = new ArrayList<>();
            UserEventConverter.to(userService.getUserEvents(login, asOf, -1, -1), result);

            return Response.ok(new RestResponse<>(result)).build();
        } finally {
            MeasurementPoint.stop();
        }
    }

    /**
     * Count user notifications.
     * @return
     */
    @GET
    @Path("/" + RestConstants.PATH_PARAM_NOTIFICATIONS + "/count")
    @Operation(
        description = "Возвращает все возможные user properties.",
        method = "GET",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response getUserNotifications() {

        MeasurementPoint.init(MeasurementContextName.MEASURE_UI_COUNT_USER_NOTIFICATIONS);
        MeasurementPoint.start();
        try {

            String login = SecurityUtils.getCurrentUserName();
            return Response.ok(new RestResponse<>(userService.countUserEvents(login))).build();
        } finally {
            MeasurementPoint.stop();
        }
    }

    /**
     * Deletes user notification.
     * @param id the event id
     * @return response
     */
    @DELETE
    @Path("/" + RestConstants.PATH_PARAM_NOTIFICATIONS + "/{" + RestConstants.DATA_PARAM_ID  + "}")
    @Operation(
        description = "Удаляет возможные user properties по идентификатору.",
        method = "DELETE",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response deleteUserNotification(@Parameter(description = "Идентификатор сообщения") @PathParam(RestConstants.DATA_PARAM_ID) String id) {
        MeasurementPoint.init(MeasurementContextName.MEASURE_UI_DELETE_USER_NOTIFICATION);
        MeasurementPoint.start();
        try {
            return Response.ok(new RestResponse<>(userService.deleteUserEvent(id))).build();
        } finally {
            MeasurementPoint.stop();
        }
    }

    /**
     * Deletes all notifications for current user.
     * @return response
     */
    @DELETE
    @Path("/" + RestConstants.PATH_PARAM_NOTIFICATIONS)
    @Operation(
        description = "Удаляет все пользовательские события.",
        method = "DELETE",
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response deleteAllUserNotifications() {
        MeasurementPoint.init(MeasurementContextName.MEASURE_UI_DELETE_ALL_USER_NOTIFICATIONS);
        MeasurementPoint.start();
        try {
            return Response.ok(new RestResponse<>(userService.deleteAllEventsForCurrentUser(null)))
                    .build();
        } finally {
            MeasurementPoint.stop();
        }
    }

    /**
     * Deletes notifications by specific IDs.
     * @return response
     */
    @POST
    @Path("/" + RestConstants.PATH_PARAM_NOTIFICATIONS + "/idsdelete")
    @Operation(
        description = "Удаляет выбранные пользовательские события.",
        method = "POST",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = List.class)), description = "Список идентификаторов"),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = RestResponse.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
        }
    )
    public Response deleteUserNotificationsByIds(List<String> ids) {
        MeasurementPoint.init(MeasurementContextName.MEASURE_UI_DELETE_SELECTED_USER_NOTIFICATIONS);
        MeasurementPoint.start();
        try {
            return Response.ok(new RestResponse<>(userService.deleteUserEvents(ids))).build();
        } finally {
            MeasurementPoint.stop();
        }
    }
}
