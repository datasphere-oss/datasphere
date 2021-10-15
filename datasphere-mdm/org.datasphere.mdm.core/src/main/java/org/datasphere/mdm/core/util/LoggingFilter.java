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
package org.datasphere.mdm.core.util;

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
