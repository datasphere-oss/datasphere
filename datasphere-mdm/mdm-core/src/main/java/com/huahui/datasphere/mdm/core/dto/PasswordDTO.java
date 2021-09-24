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

package com.huahui.datasphere.mdm.core.dto;

import java.time.LocalDateTime;

public class PasswordDTO extends BaseSecurityDTO {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = -5612881974042201325L;

    private final UserDTO user;
    private final String hashedText;
    private final String text;
    private final LocalDateTime createAt;
    private final boolean isActive;

    public PasswordDTO(UserDTO user, String hashedText, boolean isActive, LocalDateTime createAt) {
        this.user = user;
        this.hashedText = hashedText;
        this.isActive = isActive;
        this.text = null;
        this.createAt = createAt;
    }

    public PasswordDTO(UserDTO user, String hashedText, String text, boolean isActive, LocalDateTime createAt) {
        this.user = user;
        this.hashedText = hashedText;
        this.text = text;
        this.isActive = isActive;
        this.createAt = createAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public String getHashedText() {
        return hashedText;
    }

    public String getText() {
        return text;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }
}
