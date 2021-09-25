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
package com.huahui.datasphere.mdm.rest.core.ro;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class LoginRequest.
 */
public class LoginRequest {

    /** The user name. */
    @JsonProperty(required = true, value = "userName")
    private String userName;

    /** The password. */
    @JsonProperty(required = true, value = "password")
    private String password;

    /** The locale. */
    @JsonProperty(required = true, value = "locale")
    private String locale;


    /**
     * Gets the user name.
     *
     * @return the userName
     */
    public String getUserName() {
	return userName;
    }

    /**
     * Sets the user name.
     *
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
	this.userName = userName;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
	return password;
    }

    /**
     * Sets the password.
     *
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
	this.password = password;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
