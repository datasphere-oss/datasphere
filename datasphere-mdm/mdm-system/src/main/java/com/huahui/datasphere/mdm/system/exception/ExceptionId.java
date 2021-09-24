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

import java.util.Objects;
/**
 *
 * @author theseusyang
 * The exception ID class.
 */
public final class ExceptionId {
    /**
     * Unique exception code. Must be used only once in the app.
     */
    private final String code;
    /**
     * Translation code of a localized message (not the final message).
     */
    private final String message;
    /**
     * Constructor.
     * @param code the code.
     * @param message the translation code of a localized message.
     */
    public ExceptionId(String code, String message) {
        this.code = code;
        this.message = message;
    }
    /**
     * Msg code.
     * @return code
     */
    public String code() {
        return code;
    }
    /**
     * Translation code.
     * @return translation code
     */
    public String message() {
        return message;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExceptionId)) {
            return false;
        }
        ExceptionId that = (ExceptionId) o;
        return Objects.equals(code, that.code);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return code;
    }
}