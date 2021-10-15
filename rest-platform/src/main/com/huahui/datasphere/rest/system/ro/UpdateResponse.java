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

import java.util.ArrayList;
import java.util.List;

import com.huahui.datasphere.system.dto.Param;


/**
 * This response should be returned to frontend in case of update. This is
 * needed because sencha doesn't recognize empty responses.
 */
public class UpdateResponse {

    /**  Is successful?. */
    private boolean success;

    /** Updated record id. */
    private String id;
    
    /** List with errors. */
    private List<ErrorInfo> errors;
    
    /** List with additional parameters. */
    private List<Param> params;

    /**
     * Instantiates a new update response.
     *
     * @param success
     *            is update successful?
     * @param id
     *            updated record id.
     */
    public UpdateResponse(boolean success, String id) {
        this.success = success;
        this.id = id;
    }
    /**
     * Instantiates a new update response.
     *
     * @param success
     *            is update successful?
     * @param id
     *            updated record id.
     * @param params 
     *            list with additional parameters.
     */
    public UpdateResponse(boolean success, String id, List<Param> params) {
        this.success = success;
        this.id = id;
        this.params = params;
    }
    /**
     * Checks if is success.
     *
     * @return true, if is success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Error info.
     * 
     * @return list with errors.
     */
    public List<ErrorInfo> getErrors() {
        return errors;
    }

    /**
     * Set errors.
     * 
     * @param errors
     *            list with errors.
     */
    public void setErrors(List<ErrorInfo> errors) {
        this.errors = errors;
    }

    /**
     * Gets the params.
     *
     * @return the params
     */
    public List<Param> getParams() {
        if (this.params == null) {
            this.params = new ArrayList<Param>();
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

}
