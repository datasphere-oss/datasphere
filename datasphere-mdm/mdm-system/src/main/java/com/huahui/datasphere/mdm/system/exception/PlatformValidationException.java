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

package com.huahui.datasphere.mdm.system.exception;

import java.util.Collection;
import java.util.Collections;

/**
 * Data validation error.
 *
 * @author theseusyang
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
