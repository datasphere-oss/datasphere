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

package org.datasphere.mdm.core.exception;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.datasphere.mdm.system.exception.DomainId;
import org.datasphere.mdm.system.exception.ExceptionId;
import org.datasphere.mdm.system.exception.PlatformRuntimeException;

/**
 * Job domain exceptions (thrown by the jobs subsys).
 * @author Alexander Magdenko
 */
public class JobException extends PlatformRuntimeException {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = 4401101538239705307L;
    /**
     * This exception domain.
     */
    private static final DomainId JOB_EXCEPTION = () -> "JOB_EXCEPTION";
    /**
     * Key-value pairs.
     */
    private final List<Pair<String, String>> params;
    /**
     * @param message
     */
    public JobException(String message, ExceptionId id, Object... args) {
        super(message, id, args);
        this.params = null;
    }
    /**
     * Validation error
     * @param message    Validation message.
     * @param id         Error id.
     * @param params     Params in error.
     * @param args       Params for message.
     */
    public JobException(String message, ExceptionId id, List<Pair<String, String>> params, Object... args) {
        super(message, id, args);
        this.params = params;
    }
    /**
     * @param message
     * @param cause
     */
    public JobException(String message, Throwable cause, ExceptionId id, Object... args) {
        super(message, cause, id, args);
        this.params = null;
    }

    /**
     * @param cause
     */
    public JobException(Throwable cause, ExceptionId id, Object... args) {
        super(cause, id, args);
        this.params = null;
    }
    /**
     * Gets the job params
     * @return params
     */
    public List<Pair<String, String>> getParams() {
        return params;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DomainId getDomain() {
        return JOB_EXCEPTION;
    }
}
