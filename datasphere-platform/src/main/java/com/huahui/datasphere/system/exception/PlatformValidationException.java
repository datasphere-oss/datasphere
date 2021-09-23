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

import java.util.Collection;
import java.util.Collections;

/**
 * Data validation error.
 *
 * @author Alexander Malyshev
 */
public class PlatformValidationException extends PlatformRuntimeException {
    /**
     * ABI SVUID.
     */
    private static final long serialVersionUID = 6846532046659833773L;
    /**
     * This exception domain.
     */
    private static final DomainId GENERIC_VALIDATION_EXCEPTION = () -> "GENERIC_VALIDATION_EXCEPTION";
    /**
     * The validation result.
     */
    private final Collection<ValidationResult> validationResults;
    /**
     * Constructor.
     * @param message the message
     * @param id the id
     * @param validationResult validation result
     * @param args the arguments
     */
    public PlatformValidationException(String message, ExceptionId id, Collection<ValidationResult> validationResult, Object... args) {
        super(message, id, args);
        this.validationResults = validationResult == null ? Collections.emptyList() : validationResult;
    }
    /**
     * Constructor.
     * @param message the message
     * @param cause the exception cause
     * @param id exception id
     * @param validationResult validation elements
     * @param args arguments
     */
    public PlatformValidationException(String message, Throwable cause, ExceptionId id, Collection<ValidationResult> validationResult, Object... args) {
        super(message, cause, id, args);
        this.validationResults = validationResult == null ? Collections.emptyList() : validationResult;
    }
    /**
     * Constructor.
     * @param message the message
     * @param id exception id
     * @param validationResults validation elements
     */
    public PlatformValidationException(
            final String message,
            final ExceptionId id,
            final Collection<ValidationResult> validationResults
    ) {
        super(message, id);
        this.validationResults = validationResults == null ? Collections.emptyList() : validationResults;
    }
    /**
     * @return the validationResult
     */
    public Collection<ValidationResult> getValidationResults() {
        return validationResults;
    }

    //for our internal exceptions we have and ExceptionId for identifying exception place!
    @Override
    public synchronized Throwable fillInStackTrace() {
        final String enableFillStackTrace = System.getProperty("enableFillStackTrace", "false");
        return enableFillStackTrace.equalsIgnoreCase("true") ? super.fillInStackTrace() : this;
    }

    // for presenting information to external users about exceptions, we should use cause exception
    @Override
    public StackTraceElement[] getStackTrace() {
        return getCause() == null ? super.getStackTrace() : getCause().getStackTrace();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DomainId getDomain() {
        return GENERIC_VALIDATION_EXCEPTION;
    }
}
