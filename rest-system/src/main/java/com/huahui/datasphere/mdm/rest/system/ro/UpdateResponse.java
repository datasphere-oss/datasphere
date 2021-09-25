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

import java.util.ArrayList;
import java.util.List;

import com.huahui.datasphere.mdm.system.dto.Param;


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
