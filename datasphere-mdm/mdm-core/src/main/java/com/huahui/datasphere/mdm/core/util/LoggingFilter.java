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
package com.huahui.datasphere.mdm.core.util;

import org.apache.cxf.interceptor.Fault;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Custom logback filter.
 * It's needed to distinguish client logs from backend logs.
 * @author ilya.bykov
 *
 */
public class LoggingFilter extends AbstractMatcherFilter<ILoggingEvent> {
    /**
     * Logger name of rest client.
     */
    private static final String UNIDATA_REST_CLIENT = "UNIDATA_REST_CLIENT";

    /** {@inheritDoc} */
    @Override
    public FilterReply decide(ILoggingEvent event) {

        if (event.getThrowableProxy() instanceof ThrowableProxy
        && Fault.class.isInstance(((ThrowableProxy) event.getThrowableProxy()).getThrowable())) {
            return FilterReply.DENY;
        }

        if (event.getLoggerName().equals(UNIDATA_REST_CLIENT)) {
            return onMatch;
        } else {
            return onMismatch;
        }
    }

}
