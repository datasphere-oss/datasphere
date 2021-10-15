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

package com.huahui.datasphere.rest.system.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.unidata.mdm.rest.system.exception.IllegalParameterFormatException;
import org.unidata.mdm.rest.system.exception.MissingParameterException;

import com.huahui.datasphere.system.util.IpUtils;
import com.huahui.datasphere.rest.system.ro.RestResponse;
import com.huahui.datasphere.system.util.ConvertUtils;

/*
 http://cxf.apache.org/docs/jax-rs-data-bindings.html#JAX-RSDataBindings-JSONsupport
 http://cxf.apache.org/docs/jax-rs-basics.html
 */

@Produces({ "application/json" })
@Consumes({ "application/json" })
// , "application/x-www-form-urlencoded"
public abstract class AbstractRestService {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestService.class);
    /**
     * Default object mapper.
     */
    @Autowired
    @Qualifier("objectMapper")
    protected ObjectMapper defaultMapper;
    /**
     * HTTP servlet request.
     */
    @Context
    private HttpServletRequest hsr;

    public static final String DELETE_REQUEST = "DELETE";

    public static final String GET_REQUEST = "GET";
    /**
     * Allow options requests.
     */
    public static final String OPTIONS_REQUEST = "OPTIONS";

    public static final String POST_REQUEST = "POST";

    public static <T> T notNull(String parameterName, T value) {
        if (value == null) {
            throw new MissingParameterException(parameterName);
        }
        return value;
    }

    public static LocalDateTime parseDateTime(String parameterName, String str) {
        try {
            return ConvertUtils.string2LocalDateTime(str);
        } catch (Exception ex) {
            throw new IllegalParameterFormatException(parameterName, "yyyy-MM-ddTHH:mm:ss.SSS");
        }
    }

    protected Response ok(Object entity) {
        return Response.ok(entity).allow(OPTIONS_REQUEST).build();
    }

    protected Response okRestResponse(Object value) {
        return ok(new RestResponse<>(value));
    }

    protected Response error(Object entity){
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(entity)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .encoding(StandardCharsets.UTF_8.name())
                .build();
    }

    protected Response okOrNotFound(Object entity) {
        if (entity == null) {
            return notFound();
        }
        return ok(entity);
    }

    protected Response accepted() {
        return Response.accepted().build();
    }

    protected Response notAuthorized(Object entity) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(entity).build();
    }

    protected Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    protected javax.servlet.http.HttpServletRequest getHSR() {
        return this.hsr;
    }

    protected String getClientIp() {
        return IpUtils.clientIp(getHSR());
    }

    protected String getServerIp() {
        return  getHSR() == null ?  null : getHSR().getLocalAddr();
    }

    /**
     * Local (in-place) unrest of a content string.
     *
     * @param content
     *            the string
     * @param cls
     *            class to map the content to
     * @return new instance
     */
    public <T> T unrestInplace(String content, Class<T> cls) {
        try {
            if (!StringUtils.isBlank(content)) {
                return defaultMapper.readValue(content, cls);
            }
        } catch (JsonParseException e) {
            LOGGER.warn("Caught a 'JsonParseException' while local unrest. {}", e.getMessage(), e);
        } catch (JsonMappingException e) {
            LOGGER.warn("Caught a 'JsonMappingException' while local unrest. {}", e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.warn("Caught a 'IOException' while doing unrest. {}", e.getMessage(), e);
        }

        return null;
    }
}
