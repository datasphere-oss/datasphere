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

import java.util.Date;

/**
 * The Class UserWithPasswordRO.
 * @author ilya.bykov
 */
public class UserWithPasswordDTO extends UserDTO {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = -5010154347779875267L;
    
    /** The password. */
    private String password;
    
    /** The password last changed at. */
    private Date passwordLastChangedAt;
    
    /** The password updated by. */
    private String passwordUpdatedBy;

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
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

	/**
	 * Gets the password last changed at.
	 *
	 * @return the passwordLastChangedAt
	 */
	public Date getPasswordLastChangedAt() {
		return passwordLastChangedAt;
	}

	/**
	 * Sets the password last changed at.
	 *
	 * @param passwordLastChangedAt
	 *            the passwordLastChangedAt to set
	 */
	public void setPasswordLastChangedAt(Date passwordLastChangedAt) {
		this.passwordLastChangedAt = passwordLastChangedAt;
	}

	/**
	 * Gets the password updated by.
	 *
	 * @return the passwordUpdatedBy
	 */
	public String getPasswordUpdatedBy() {
		return passwordUpdatedBy;
	}

	/**
	 * Sets the password updated by.
	 *
	 * @param passwordUpdatedBy
	 *            the passwordUpdatedBy to set
	 */
	public void setPasswordUpdatedBy(String passwordUpdatedBy) {
		this.passwordUpdatedBy = passwordUpdatedBy;
	}
}
