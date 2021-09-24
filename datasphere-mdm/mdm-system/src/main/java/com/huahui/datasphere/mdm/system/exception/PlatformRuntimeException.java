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

import org.slf4j.helpers.MessageFormatter;

/**
 * Common parent type for exception used in DataSphere Platform
 *
 * @author theseusyang
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
