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

/**
 * Business exception
 *
 * @author theseusyang
 */
public class PlatformBusinessException extends PlatformRuntimeException {
    /**
     * ABI SVUID.
     */
    private static final long serialVersionUID = -3206300072672935440L;
    /**
     * This exception domain.
     */
    private static final DomainId GENERIC_BUSINESS_EXCEPTION = () -> "GENERIC_BUSINESS_EXCEPTION";
    /**
     * Constructor.
     * @param message the message
     * @param id the id
     * @param args arguments
     */
    public PlatformBusinessException(String message, ExceptionId id, Object... args) {
        super(message, id, args);
    }
    /**
     * Constructor.
     * @param message the message
     * @param cause throwable cause
     * @param id the id
     * @param args arguments
     */
    public PlatformBusinessException(String message, Throwable cause, ExceptionId id, Object... args) {
        super(message, cause, id, args);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DomainId getDomain() {
        return GENERIC_BUSINESS_EXCEPTION;
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
}
