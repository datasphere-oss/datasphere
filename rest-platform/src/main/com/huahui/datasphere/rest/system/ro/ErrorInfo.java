/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package com.huahui.datasphere.rest.system.ro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.unidata.mdm.system.dto.Param;

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
