/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 *
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.datasphere.mdm.core.context.AuditEventWriteContext;
import org.datasphere.mdm.system.type.messaging.SystemHeaders;
/**
 * Core event builder.
 */
public final class CoreAuditEventContextBuilder {
    /**
     * Constructor.
     */
    private CoreAuditEventContextBuilder() {
        super();
    }
    /**
     * Build the target audit context.
     * @param exchange the exchange
     * @return audit context
     */
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
