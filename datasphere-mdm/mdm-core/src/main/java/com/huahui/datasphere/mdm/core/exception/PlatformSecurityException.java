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

package com.huahui.datasphere.mdm.core.exception;

import com.huahui.datasphere.mdm.system.exception.DomainId;
import com.huahui.datasphere.mdm.system.exception.ExceptionId;
import com.huahui.datasphere.mdm.system.exception.PlatformRuntimeException;

/**
 * @author theseusyang on Nov 6, 2019
 */
public class PlatformSecurityException extends PlatformRuntimeException {
    /**
     * This domain ID.
     */
    private static final DomainId SECURITY_EXCEPTION = () -> "SECURITY_EXCEPTION";
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = -2666211650358078564L;
    /**
     * Constructor.
     * @param message
     * @param id
     * @param args
     */
    public PlatformSecurityException(String message, ExceptionId id, Object... args) {
        super(message, id, args);
    }
    /**
     * Constructor.
     * @param message
     * @param cause
     * @param id
     * @param args
     */
    public PlatformSecurityException(String message, Throwable cause, ExceptionId id, Object... args) {
        super(message, cause, id, args);
    }
    /**
     * Constructor.
     * @param cause
     * @param id
     * @param args
     */
    public PlatformSecurityException(Throwable cause, ExceptionId id, Object... args) {
        super(cause, id, args);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public DomainId getDomain() {
        return SECURITY_EXCEPTION;
    }

}
