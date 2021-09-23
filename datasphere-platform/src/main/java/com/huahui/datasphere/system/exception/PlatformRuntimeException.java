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

package com.huahui.datasphere.system.exception;

import org.slf4j.helpers.MessageFormatter;

/**
 * Common parent type for exception used in Unidata Platform
 *
 * @author Alexander Malyshev
 */
public abstract class PlatformRuntimeException extends RuntimeException {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 6127303709077828586L;
    /**
     * This exception Id.
     */
    private final transient ExceptionId id;
    /**
     * Arguments if any.
     */
    private final transient Object[] args;

    /**
     * Constructor from superclass.
     *
     * @param message the message
     * @param id      exception id
     */
    public PlatformRuntimeException(String message, ExceptionId id, Object... args) {
        super(message);
        this.id = id;
        this.args = args;
    }

    /**
     * Constructor from superclass.
     *
     * @param message the message
     * @param cause   exception cause
     * @param id      exception id
     */
    public PlatformRuntimeException(String message, Throwable cause, ExceptionId id, Object... args) {
        super(message, cause);
        this.id = id;
        this.args = args;
    }

    /**
     * Constructor from superclass.
     *
     * @param cause exception cause
     * @param id    exception id
     */
    public PlatformRuntimeException(Throwable cause, ExceptionId id, Object... args) {
        super(cause);
        this.id = id;
        this.args = args;
    }

    /**
     * @return the id
     */
    public ExceptionId getId() {
        return id;
    }

    /**
     * @return the args
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * Overridden to include exception ID.
     *
     * @see Throwable#toString()
     */
    @Override
    public String toString() {
        String localizedMessage = getLocalizedMessage();
        return getClass().getName() + ": [" + id.code() + "] " + (localizedMessage == null ? "" : localizedMessage);
    }

    /**
     * Overridden to support args.
     *
     * @see Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        String thisMessage = super.getMessage();
        if (thisMessage != null && args != null && args.length > 0) {
            return MessageFormatter.arrayFormat(thisMessage, args).getMessage();
        }

        return thisMessage;
    }
    /**
     * Gets the exception domain descriptor.
     * @return domain descriptor
     */
    public abstract DomainId getDomain();
}
