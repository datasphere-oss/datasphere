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
package com.huahui.datasphere.mdm.rest.core.ro.audit;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The Class AuditSearchRequest.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditSearchRequestComplexRO {

    private int count = 30;
    private int page = 0;
    private LocalDateTime from;
    private LocalDateTime to;

    private List<AuditSearchFieldRO> searchFields;
    /**
     * Parameters.
     */
    private List<AuditSearchFieldRO> parameters;

    private List<AuditSortFieldRO> sortFields;

    public List<AuditSearchFieldRO> getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(List<AuditSearchFieldRO> searchFields) {
        this.searchFields = searchFields;
    }

    /**
     * @return the parameters
     */
    public List<AuditSearchFieldRO> getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(List<AuditSearchFieldRO> parameters) {
        this.parameters = parameters;
    }

    public List<AuditSortFieldRO> getSortFields() {
        return sortFields;
    }

    public void setSortFields(List<AuditSortFieldRO> sortFields) {
        this.sortFields = sortFields;
    }

    /**
     * Return count.
     */
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Return page.
     */
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Optional validity range start date.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    /**
     * Optional validity range end date.
     */
    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }
}
