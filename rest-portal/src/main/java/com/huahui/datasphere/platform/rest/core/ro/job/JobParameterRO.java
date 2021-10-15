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
package com.huahui.datasphere.platform.rest.core.ro.job;

import java.time.ZonedDateTime;

import org.unidata.mdm.core.type.job.JobParameterType;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.huahui.datasphere.platform.rest.core.serialization.json.JobParameterRODeserializer;
import com.huahui.datasphere.platform.rest.core.serialization.json.JobParameterROSerializer;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
@JsonSerialize(using = JobParameterROSerializer.class)
@JsonDeserialize(using = JobParameterRODeserializer.class)
public class JobParameterRO {
    private boolean multiSelect;
    private Long id;
    private String name;
    private Object[] value;
    private JobParameterType type;

    public JobParameterRO(final String name, final String... value) {
        this.name = name;
        this.value = value;
        this.type = JobParameterType.STRING;
    }

    public JobParameterRO(final String name, final ZonedDateTime... value) {
        this.name = name;
        this.value = value;
        this.type = JobParameterType.DATE;
    }

    public JobParameterRO(final String name, final Long... value) {
        this.name = name;
        this.value = value;
        this.type = JobParameterType.LONG;
    }

    public JobParameterRO(final String name, final Double... value) {
        this.name = name;
        this.value = value;
        this.type = JobParameterType.DOUBLE;
    }

    public JobParameterRO(final String name, final Boolean... value) {
        this.name = name;
        this.value = value;
        this.type = JobParameterType.BOOLEAN;
    }

    public JobParameterRO(final String name, final JobParameterType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public JobParameterType getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStringValue() {
        if (JobParameterType.STRING == type && value != null && value.length > 0) {
            return (String) value[0];
        }
        return null;
    }

    public String[] getStringArrayValue() {
        if (JobParameterType.STRING == type && value != null && value.length > 0) {
            return (String[])value;
        }
        return null;
    }

    public ZonedDateTime getDateValue() {
        if (JobParameterType.DATE == type && value != null && value.length > 0) {
            return (ZonedDateTime) value[0];
        }
        return null;
    }

    public ZonedDateTime[] getDateArrayValue() {
        if (JobParameterType.DATE == type && value != null && value.length > 0) {
            return (ZonedDateTime[])value;
        }
        return null;
    }

    public Long getLongValue() {
        if (JobParameterType.LONG == type && value != null && value.length > 0) {
            return (Long) value[0];
        }
        return null;
    }

    public Long[] getLongArrayValue() {
        if (JobParameterType.LONG == type && value != null && value.length > 0) {
            return (Long[])value;
        }
        return null;
    }

    public Double getDoubleValue() {
        if (JobParameterType.DOUBLE == type && value != null && value.length > 0) {
            return (Double) value[0];
        }
        return null;
    }

    public Double[] getDoubleArrayValue() {
        if (JobParameterType.DOUBLE == type && value != null && value.length > 0) {
            return (Double[])value;
        }
        return null;
    }

    public Boolean getBooleanValue() {
        if (JobParameterType.BOOLEAN == type && value != null && value.length > 0) {
            return (Boolean) value[0];
        }
        return null;
    }

    public Boolean[] getBooleanArrayValue() {
        if (JobParameterType.BOOLEAN == type && value != null && value.length > 0) {
            return (Boolean[])value;
        }
        return null;
    }

    public int getValueSize() {
        if (value != null) {
            return value.length;
        }

        return 0;
    }

    public Object[] getArrayValue() {
        return value;
    }

    /**
     * @return the multiSelect
     */
    public boolean isMultiSelect() {
        return multiSelect;
    }

    /**
     * @param multiSelect the multiSelect to set
     */
    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }
}
