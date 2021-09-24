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
 * Any external or system errors during platform work.
 *
 * @author theseusyang
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
