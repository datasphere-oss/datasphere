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
package com.huahui.datasphere.mdm.rest.core.ro.configuration;

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
