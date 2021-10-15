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
package com.huahui.datasphere.platform.rest.core.ro.configuration;

import java.util.Collection;

public class ConfigurationPropertyMetaRO {
    private Collection<ConfigurationPropertyAvailableValuePO> availableValues;
    private boolean required;
    private boolean readonly;

    public ConfigurationPropertyMetaRO() {
    }

    public ConfigurationPropertyMetaRO(
            final Collection<ConfigurationPropertyAvailableValuePO> availableValues,
            final boolean required,
            final boolean readonly
    ) {
        this.availableValues = availableValues;
        this.required = required;
        this.readonly = readonly;
    }

    public Collection<ConfigurationPropertyAvailableValuePO> getAvailableValues() {
        return availableValues;
    }

    public void setAvailableValues(Collection<ConfigurationPropertyAvailableValuePO> availableValues) {
        this.availableValues = availableValues;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }
}
