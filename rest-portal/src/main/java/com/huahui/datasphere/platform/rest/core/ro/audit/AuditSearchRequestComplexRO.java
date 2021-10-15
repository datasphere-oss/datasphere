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
package com.huahui.datasphere.platform.rest.core.ro.audit;

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
