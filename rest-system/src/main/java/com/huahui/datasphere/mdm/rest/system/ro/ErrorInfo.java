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
package com.huahui.datasphere.mdm.rest.system.ro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.huahui.datasphere.mdm.system.dto.Param;

public class ErrorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Type {
        VALIDATION_ERROR,
        INTERNAL_ERROR,
        AUTHENTICATION_ERROR,
        AUTHORIZATION_ERROR,
        USER_ALREADY_EXIST,
        USER_CANNOT_BE_DEACTIVATED
    }

    public enum Severity{
    	LOW("Низкая"),
    	NORMAL("Средняя"),
    	HIGH("Высокая"),
    	CRITICAL("Максимальная");

        private String displayName;

        Severity(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private Type type;
    private Severity severity;
    private String errorCode;

    private String userMessage;

    private String internalMessage;

    private String userMessageDetails;

    private List<Param> params;

    public ErrorInfo() {
        super();
    }

    public ErrorInfo(Type type) {
        if (type != null) {
            this.type = type;
        }
    }

    public String getType() {
        if (type != null) {
            return type.name();
        }
        return null;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getInternalMessage() {
        return internalMessage;
    }

    public void setInternalMessage(String internalMessage) {
        this.internalMessage = internalMessage;
    }

    public String getUserMessageDetails() {
        return userMessageDetails;
    }

    public ErrorInfo setUserMessageDetails(String userMessageDetails) {
        this.userMessageDetails = userMessageDetails;
        return this;
    }

    /**
     * Gets the params.
     *
     * @return the params
     */
    public List<Param> getParams() {
        if (this.params == null) {
            this.params = new ArrayList<>();
        }

        return params;
    }

    /**
     * Sets the params.
     *
     * @param params the new params
     */
    public void setParams(List<Param> params) {
        this.params = params;
    }

    /**
     * Adds the param.
     *
     * @param param the param
     */
    public void addParam(Param param) {
        getParams().add(param);
    }

	/**
	 * @return the severity
	 */
	public Severity getSeverity() {
		if(this.severity==null){
			return Severity.NORMAL;
		}
		return severity;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
}
