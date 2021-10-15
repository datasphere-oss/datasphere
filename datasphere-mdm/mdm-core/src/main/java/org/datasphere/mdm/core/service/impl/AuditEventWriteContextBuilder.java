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

package com.huahui.datasphere.mdm.core.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import com.huahui.datasphere.mdm.system.type.messaging.SystemHeaders;

import com.huahui.datasphere.mdm.core.context.AuditEventWriteContext;

public final class AuditEventWriteContextBuilder {

    private AuditEventWriteContextBuilder() {}

    public static AuditEventWriteContext build(final Exchange exchange) {
        final Map<String, Object> headers = new HashMap<>(exchange.getIn().getHeaders());
        return AuditEventWriteContext.builder()
                .domain(headers.remove(SystemHeaders.DOMAIN.getName()).toString())
                .type(headers.remove(SystemHeaders.TYPE.getName()).toString())
                .userLogin(headers.remove(SystemHeaders.LOGIN.getName()).toString())
                .clientIp(headers.remove(SystemHeaders.CLIENT_IP.getName()).toString())
                .serverIp(headers.remove(SystemHeaders.SERVER_IP.getName()).toString())
                .endpoint(headers.remove(SystemHeaders.ENDPOINT.getName()).toString())
                .whenHappened(LocalDateTime.ofInstant((Instant) headers.remove(SystemHeaders.WHEN_HAPPENED.getName()), ZoneId.systemDefault()))
                .success(!headers.containsKey(SystemHeaders.THROWABLE.getName()))
                .throwableDump((Throwable) headers.remove(SystemHeaders.THROWABLE.getName()))
                .parameters(headers)
                .build();
    }
}
