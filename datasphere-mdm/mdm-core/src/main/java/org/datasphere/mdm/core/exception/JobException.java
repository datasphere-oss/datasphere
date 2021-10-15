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

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import com.huahui.datasphere.mdm.system.exception.DomainId;
import com.huahui.datasphere.mdm.system.exception.ExceptionId;
import com.huahui.datasphere.mdm.system.exception.PlatformRuntimeException;

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
