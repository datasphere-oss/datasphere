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

import java.util.Collection;

import org.datasphere.mdm.system.exception.DomainId;
import org.datasphere.mdm.system.exception.ExceptionId;
import org.datasphere.mdm.system.exception.PlatformValidationException;
import org.datasphere.mdm.system.exception.ValidationResult;

/**
 * @author Mikhail Mikhailov on Jun 29, 2021
 */
public class JobValidationException extends PlatformValidationException {
    /**
     * This exception domain.
     */
    private static final DomainId JOB_VALIDATION = () -> "JOB_VALIDATION";
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 4056104613221814605L;
    /**
     * Constructor.
     * @param message
     * @param id
     * @param validationResult
     * @param args
     */
    public JobValidationException(String message, ExceptionId id, Collection<ValidationResult> validationResult,
            Object... args) {
        super(message, id, validationResult, args);
    }
    /**
     * Constructor.
     * @param message
     * @param cause
     * @param id
     * @param validationResult
     * @param args
     */
    public JobValidationException(String message, Throwable cause, ExceptionId id,
            Collection<ValidationResult> validationResult, Object... args) {
        super(message, cause, id, validationResult, args);
    }
    /**
     * Constructor.
     * @param message
     * @param id
     * @param validationResults
     */
    public JobValidationException(String message, ExceptionId id, Collection<ValidationResult> validationResults) {
        super(message, id, validationResults);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DomainId getDomain() {
        return JOB_VALIDATION;
    }
}
