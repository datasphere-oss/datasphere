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

/**
 * Any external or system errors during platform work.
 *
 * @author Alexander Malyshev
 */
public class PlatformFailureException extends PlatformRuntimeException {
    /**
     * ABI SVUID.
     */
    private static final long serialVersionUID = 6597321286543795338L;
    /**
     * This exception domain.
     */
    private static final DomainId PLATFORM_FAILURE_EXCEPTION = () -> "PLATFORM_FAILURE_EXCEPTION";

    public PlatformFailureException(String message, ExceptionId id, Object... args) {
        super(message, id, args);
    }

    public PlatformFailureException(String message, Throwable cause, ExceptionId id, Object... args) {
        super(message, cause, id, args);
    }

    public PlatformFailureException(Throwable cause, ExceptionId id, Object... args) {
        super(cause, id, args);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DomainId getDomain() {
        return PLATFORM_FAILURE_EXCEPTION;
    }
}
